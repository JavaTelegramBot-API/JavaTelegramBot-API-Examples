package pro.zackpollard.telegrambot.formattingbot;

import pro.zackpollard.telegrambot.api.TelegramBot;
import pro.zackpollard.telegrambot.api.chat.inline.send.InlineQueryResponse;
import pro.zackpollard.telegrambot.api.chat.inline.send.results.InlineQueryResult;
import pro.zackpollard.telegrambot.api.chat.inline.send.results.InlineQueryResultArticle;
import pro.zackpollard.telegrambot.api.chat.message.send.ParseMode;
import pro.zackpollard.telegrambot.api.chat.message.send.SendableTextMessage;
import pro.zackpollard.telegrambot.api.event.Listener;
import pro.zackpollard.telegrambot.api.event.chat.inline.InlineQueryReceivedEvent;
import pro.zackpollard.telegrambot.api.event.chat.message.TextMessageReceivedEvent;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Zack Pollard
 */
public class FormattingListener implements Listener {

    private final TelegramBot telegramBot;
    private final static Pattern pattern = Pattern.compile("(\\[[^\\(\\)\\[\\]]*\\]\\([^\\(\\)\\[\\]]*\\))|(\\*[^\\*]*\\*)|(\\_[^\\_]*\\_)|(\\`[^\\`]*\\`)");

    public FormattingListener(TelegramBot telegramBot) {

        this.telegramBot = telegramBot;
    }

    //All events are in the Listener class in the bot API classes.
    //You can see the available events here https://github.com/zackpollard/JavaTelegramBot-API/blob/master/src/main/java/pro/zackpollard/telegrambot/api/event/Listener.java
    //You can use any of those events in the same way as the one below is used.

    //This event simply listens for any received message and replies with the same message parsed through markdown.
    @Override
    public void onInlineQueryReceived(InlineQueryReceivedEvent event) {

        String senderName = event.getQuery().getSender().getUsername() != null ? event.getQuery().getSender().getUsername() : String.valueOf(event.getQuery().getSender().getId());

        System.out.println(senderName + " - " + event.getQuery().getQuery());

        String queryText = event.getQuery().getQuery();

        String newQueryText = queryText.replace("*", "*\\**")
                .replace("_", "_\\__")
                .replace("`", "`\\``");

        //This is a List of InlineQueryResult's in order to add the results for sending later
        List<InlineQueryResult> queryResults = new ArrayList<>();

        Matcher matcher;
        int start = 0;

        //This checks to see if there is valid markdown in the message and processes it if so
        if(pattern.matcher(queryText).find()) {
            while (start < queryText.length() && (matcher = pattern.matcher(queryText.substring(start))).find()) {

                String testText = queryText.substring(start);

                testText = testText
                        .substring(0, matcher.start())
                        .replace("*", "\\*")
                        .replace("_", "\\_")
                        .replace("`", "\\`")
                        + testText.substring(matcher.start(), matcher.end())
                        + testText.substring(matcher.end());

                queryText = queryText.substring(0, start) + testText;

                start += matcher.end();
            }

            String testText = queryText.substring(start);

            testText = testText
                    .replace("*", "\\*")
                    .replace("_", "\\_")
                    .replace("`", "\\`");

            queryText = queryText.substring(0, start) + testText;

            //This creates a new InlineQueryResultArticle and adds it to the list of results
            queryResults.add(InlineQueryResultArticle.builder()
                    .parseMode(ParseMode.MARKDOWN)
                    .title("Custom Markdown")
                    .description(event.getQuery().getQuery())
                    .disableWebPagePreview(true)
                    .messageText(queryText)
                    .build()
            );
        }

        //This creates a new InlineQueryResultArticle and adds it to the list of results
        queryResults.add(InlineQueryResultArticle.builder()
                .parseMode(ParseMode.MARKDOWN)
                .title("Bold")
                .description("*" + event.getQuery().getQuery() + "*")
                .disableWebPagePreview(true)
                .messageText("*" + newQueryText + "*")
                .build()
        );

        //This creates a new InlineQueryResultArticle and adds it to the list of results
        queryResults.add(InlineQueryResultArticle.builder()
                .parseMode(ParseMode.MARKDOWN)
                .title("Italic")
                .description("_" + event.getQuery().getQuery() + "_")
                .disableWebPagePreview(true)
                .messageText("_" + newQueryText + "_")
                .build()
        );

        //This creates a new InlineQueryResultArticle and adds it to the list of results
        queryResults.add(InlineQueryResultArticle.builder()
                .parseMode(ParseMode.MARKDOWN)
                .title("Code Formatting")
                .description("`" + event.getQuery().getQuery() + "`")
                .disableWebPagePreview(true)
                .messageText("`" + newQueryText + "`")
                .build()
        );

        //This creates an InlineQueryResponse object, including the list of results that
        //we created above. The is_personal(false) means that everyone will retrieve the
        //same results from cached version of results. cache_time(0) means that the results
        //will not be cached by telegrams servers, you can change the time accordingly
        event.getQuery().answer(telegramBot, InlineQueryResponse.builder()
                .results(queryResults)
                .is_personal(false)
                .cache_time(0)
                .build()
        );
    }
}