package pro.zackpollard.telegrambot.inlinespoilerbot;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import pro.zackpollard.telegrambot.api.TelegramBot;
import pro.zackpollard.telegrambot.api.chat.inline.send.InlineQueryResponse;
import pro.zackpollard.telegrambot.api.chat.inline.send.content.InputTextMessageContent;
import pro.zackpollard.telegrambot.api.chat.inline.send.results.InlineQueryResultArticle;
import pro.zackpollard.telegrambot.api.event.Listener;
import pro.zackpollard.telegrambot.api.event.chat.inline.InlineCallbackQueryReceivedEvent;
import pro.zackpollard.telegrambot.api.event.chat.inline.InlineQueryReceivedEvent;
import pro.zackpollard.telegrambot.api.event.chat.inline.InlineResultChosenEvent;
import pro.zackpollard.telegrambot.api.event.chat.message.CommandMessageReceivedEvent;
import pro.zackpollard.telegrambot.api.keyboards.InlineKeyboardButton;
import pro.zackpollard.telegrambot.api.keyboards.InlineKeyboardMarkup;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author Zack Pollard
 */
public class InlineSpoilerListener implements Listener {

    private final transient TelegramBot telegramBot;
    private final transient InlineSpoilerBot spoilerBot;
    private final transient Cache<UUID, Spoiler> possibleSpoilers;
    private final Map<UUID, Spoiler> spoilers;
    private static URL mildWarning;
    private static URL moderateWarning;
    private static URL hugeWarning;
    private static URL insaneWarning;

    public InlineSpoilerListener() {

        this.spoilerBot = InlineSpoilerBot.getInstance();

        try {
            mildWarning = new URL("http://www.zackpollard.pro/at-screenshots/-s7vR5Py.png");
            moderateWarning = new URL("http://www.zackpollard.pro/at-screenshots/~VOn8ZTh.png");
            hugeWarning = new URL("http://www.zackpollard.pro/at-screenshots/A1Zl81kM.jpg");
            insaneWarning = new URL("http://www.zackpollard.pro/at-screenshots/ejQgM6Qz.png");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        this.telegramBot = spoilerBot.getTelegramBot();
        this.possibleSpoilers = CacheBuilder.newBuilder().expireAfterWrite(30, TimeUnit.MINUTES).build();

        this.spoilers = new HashMap<>();
    }

    //All events are in the Listener class in the bot API classes.
    //You can see the available events here https://github.com/zackpollard/JavaTelegramBot-API/blob/master/src/main/java/pro/zackpollard/telegrambot/api/event/Listener.java
    //You can use any of those events in the same way as the one below is used.

    //This event simply listens for any received message and replies with the same message parsed through markdown.
    @Override
    public void onInlineQueryReceived(InlineQueryReceivedEvent event) {

        //This gets the query text from the inline query
        String queryString = event.getQuery().getQuery();

        if(event.getQuery().getQuery().length() == 0 || event.getQuery().getQuery().length() > 200) return;

        //This retrieves the senders username if they have one, otherwise gets their user ID
        String senderName = !event.getQuery().getSender().getUsername().equals("") ? event.getQuery().getSender().getUsername() : String.valueOf(event.getQuery().getSender().getId());

        //This simply prints out a debug message to console of the senders username/ID and their query
        System.out.println(senderName + " - " + event.getQuery().getQuery());

        //This generates a UUID in order to identify the specific spoiler later
        UUID uuid = UUID.randomUUID();
        //This stores the spoiler against the UUID in a Map to recall later
        possibleSpoilers.put(uuid, new Spoiler(queryString));

        //This creates an InlineQueryResponse object, including some results that we generate.
        //The is_personal(true) means that results will be unique which isn't require but is better
        //due to the UUIDs that are used. cache_time(0) means that the results will never be cached
        //by telegrams servers. You can change the time accordingly to suit your needs.
        event.getQuery().answer(telegramBot, InlineQueryResponse.builder()
                .results(InlineQueryResultArticle.builder().id("0" + uuid.toString()).replyMarkup(InlineKeyboardMarkup.builder().addRow(InlineKeyboardButton.builder().text("View Spoiler!").callbackData(uuid.toString()).build()).build()).inputMessageContent(InputTextMessageContent.builder().messageText("Mild Spoiler Alert! Click below to view!").build()).description(queryString).title("Mild Spoiler").thumbUrl(mildWarning).build(),
                        InlineQueryResultArticle.builder().id("1" + uuid.toString()).replyMarkup(InlineKeyboardMarkup.builder().addRow(InlineKeyboardButton.builder().text("View Spoiler!").callbackData(uuid.toString()).build()).build()).inputMessageContent(InputTextMessageContent.builder().messageText("❕Moderate Spoiler Alert! Click below to view❕").build()).description(queryString).title("Moderate Spoiler").thumbUrl(moderateWarning).build(),
                        InlineQueryResultArticle.builder().id("2" + uuid.toString()).replyMarkup(InlineKeyboardMarkup.builder().addRow(InlineKeyboardButton.builder().text("View Spoiler!").callbackData(uuid.toString()).build()).build()).inputMessageContent(InputTextMessageContent.builder().messageText("❗️Huge Spoiler Alert Click below to view❗️").build()).description(queryString).title("Huge Spoiler").thumbUrl(hugeWarning).build(),
                        InlineQueryResultArticle.builder().id("3" + uuid.toString()).replyMarkup(InlineKeyboardMarkup.builder().addRow(InlineKeyboardButton.builder().text("View Spoiler!").callbackData(uuid.toString()).build()).build()).inputMessageContent(InputTextMessageContent.builder().messageText("‼️Insane Spoiler Alert Click below to confirm, and click again to view‼️").build()).description(queryString).title("Insane Spoiler, requires two clicks to view!").thumbUrl(insaneWarning).build()
                ).cache_time(0)
                .is_personal(true)
                //.switch_pm_text("Switch to PM!")
                .build()
        );
    }

    @Override
    public void onInlineCallbackQueryReceivedEvent(InlineCallbackQueryReceivedEvent event) {

        UUID uuid;

        try {
            uuid = UUID.fromString(event.getCallbackQuery().getData());
        } catch(IllegalArgumentException e) {
            event.getCallbackQuery().answer("An error occurred whilst trying to show the spoiler, contact @zackpollard.", false);
            return;
        }

        Spoiler spoiler = spoilers.get(uuid);

        if(spoiler != null) {

            if(spoiler.getSpoilerType().equals(SpoilerType.INSANE)) {

                if(spoiler.getClicked() != null) {

                    if (!spoiler.getClicked().contains(event.getCallbackQuery().getFrom().getId())) {

                        event.getCallbackQuery().answer("This is an insane spoiler, click again if you are sure.", false);
                        spoiler.getClicked().add(event.getCallbackQuery().getFrom().getId());
                        return;
                    }
                }
            }

            String text = spoiler.getSpoilerText();

            boolean popup = text.length() > 50;

            event.getCallbackQuery().answer(text, popup);

            spoiler.setTimeStamp();
            possibleSpoilers.put(uuid, spoiler);

            String senderName = !event.getCallbackQuery().getFrom().getUsername().equals("") ? event.getCallbackQuery().getFrom().getUsername() : String.valueOf(event.getCallbackQuery().getFrom().getId());

            System.out.println("Spoiler Opened: " +  senderName + " - " + spoiler.getSpoilerText());
        } else {

            event.getCallbackQuery().answer("This spoiler was not found, spoilers are retained for 7 days.", false);
        }
    }

    @Override
    public void onInlineResultChosen(InlineResultChosenEvent event) {

        String resultId = event.getChosenResult().getResultId();
        int resultType = Integer.valueOf(resultId.substring(0, 1));
        resultId = resultId.substring(1);

        UUID uuid;

        try {
            uuid = UUID.fromString(resultId);
        } catch(IllegalArgumentException e) {
            return;
        }

        Spoiler spoiler = possibleSpoilers.getIfPresent(uuid);

        if(spoiler != null) {

            switch(resultType) {

                case 0: spoiler.setSpoilerType(SpoilerType.MILD); break;
                case 1: spoiler.setSpoilerType(SpoilerType.MODERATE); break;
                case 2: spoiler.setSpoilerType(SpoilerType.HUGE); break;
                case 3: spoiler.setSpoilerType(SpoilerType.INSANE); break;
            }

            possibleSpoilers.invalidate(spoiler);
            spoilers.put(uuid, spoiler);

            spoilerBot.saveSpoilers();

            String senderName = !event.getChosenResult().getSender().getUsername().equals("") ? event.getChosenResult().getSender().getUsername() : String.valueOf(event.getChosenResult().getSender().getId());
            System.out.println("Spoiler Sent: " +  senderName + " - " + spoiler.getSpoilerText());
        }
    }

    @Override
    public void onCommandMessageReceived(CommandMessageReceivedEvent event) {

        if(event.getCommand().equalsIgnoreCase("start")) {

            //event.getChat().sendMessage(SendableTextMessage.builder().message("*SPOILER ALERT*").parseMode(ParseMode.MARKDOWN).replyTo(event.getMessage()).replyMarkup(InlineKeyboardMarkup.builder().addRow(InlineKeyboardButton.builder()/**.switchInlineQuery("Look how this query got entered, this could be an ID for an audio file!")**/.text("Send to original chat!").build()).build()).build());
        }
    }

    private class Spoiler {

        private final String spoilerText;
        private SpoilerType type = SpoilerType.UNKNOWN;
        private long timeStamp;
        private transient HashSet<Long> clicked;

        public Spoiler() {

            this.spoilerText = "";
            this.type = SpoilerType.UNKNOWN;
        }

        public Spoiler(String spoilerText) {

            this.spoilerText = spoilerText;
            this.timeStamp = System.currentTimeMillis();
        }

        public String getSpoilerText() {
            return spoilerText;
        }

        public SpoilerType getSpoilerType() {
            if(type == null) type = SpoilerType.UNKNOWN;
            return type;
        }

        public long getTimeStamp() {
            return timeStamp;
        }

        public void setTimeStamp() {
            timeStamp = System.currentTimeMillis();
        }

        public void setSpoilerType(SpoilerType type) {
            this.type = type;
        }

        public Set<Long> getClicked() {
            if(clicked == null) clicked = new HashSet<>();
            return clicked;
        }
    }

    public enum SpoilerType {

        MILD,
        MODERATE,
        HUGE,
        INSANE,
        UNKNOWN
    }
}