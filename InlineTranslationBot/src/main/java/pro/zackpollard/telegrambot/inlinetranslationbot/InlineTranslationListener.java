package pro.zackpollard.telegrambot.inlinetranslationbot;

import pro.zackpollard.telegrambot.api.TelegramBot;
import pro.zackpollard.telegrambot.api.chat.inline.send.InlineQueryResponse;
import pro.zackpollard.telegrambot.api.chat.inline.send.results.InlineQueryResult;
import pro.zackpollard.telegrambot.api.chat.inline.send.results.InlineQueryResultArticle;
import pro.zackpollard.telegrambot.api.event.Listener;
import pro.zackpollard.telegrambot.api.event.chat.inline.InlineQueryReceivedEvent;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Zack Pollard
 */
public class InlineTranslationListener implements Listener {

    private final TelegramBot telegramBot;
    private static Languages[] langs = new Languages[]{Languages.ENGLISH, Languages.DUTCH, Languages.GERMAN, Languages.ITALIAN, Languages.SPANISH, Languages.FRENCH, Languages.PERSIAN, Languages.PORTUGUESE, Languages.ARABIC, Languages.CHINESE, Languages.JAPANESE, Languages.RUSSIAN};

    public InlineTranslationListener(TelegramBot telegramBot) {

        this.telegramBot = telegramBot;
    }

    //All events are in the Listener class in the bot API classes.
    //You can see the available events here https://github.com/zackpollard/JavaTelegramBot-API/blob/master/src/main/java/pro/zackpollard/telegrambot/api/event/Listener.java
    //You can use any of those events in the same way as the one below is used.

    //This event simply listens for any received message and replies with the same message parsed through markdown.
    @Override
    public void onInlineQueryReceived(InlineQueryReceivedEvent event) {

        //This gets the query text from the inline query
        String queryString = event.getQuery().getQuery();

        //Ignore query if the string is 2 or less characters long as we will just waste valuable API key usage otherwise
        if(queryString.length() <= 2) return;

        //This retrieves the senders username if they have one, otherwise gets their user ID
        String senderName = !event.getQuery().getSender().getUsername().equals("") ? event.getQuery().getSender().getUsername() : String.valueOf(event.getQuery().getSender().getId());

        //This simply prints out a debug message to console of the senders username/ID and their query
        System.out.println(senderName + " - " + event.getQuery().getQuery());

        //This List will be used to store the InlineQueryResults that we are going to create
        //for sending to the user that made the query
        List<InlineQueryResult> inlineQueryResults = new ArrayList<>();

        //This loops through the languages that are currently enabled in this bot
        for(Languages lang : langs) {

            //This calculates the translation for the text and stores it in a variable
            String translation = Translation.translateText(queryString, lang.getLangCode());

            try {
                //This adds a query result to the List that we created earlier.
                //This uses the builder to create an InlineQueryResultArticle and sets the title,
                //message text, description and also a thumb URL which is the flag of the country
                //that the text is being translated from.
                inlineQueryResults.add(InlineQueryResultArticle.builder()
                        .title(lang.getCountryName())
                        .messageText(translation)
                        .description(translation)
                        .thumbUrl(new URL("https://raw.githubusercontent.com/hjnilsson/country-flags/master/png250px/" + lang.getCountryCode() + ".png"))
                        .build()
                );
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }

        //This creates an InlineQueryResponse object, including the list of results that
        //we created above. The is_personal(false) means that everyone will retrieve the
        //same results from cached version of results. cache_time(3600) means that the results
        //will be cached by telegrams servers for an hour, you can change the time accordingly
        event.getQuery().answer(telegramBot, InlineQueryResponse.builder()
                .results(inlineQueryResults)
                .cache_time(3600)
                .is_personal(false)
                .build()
        );
    }
}