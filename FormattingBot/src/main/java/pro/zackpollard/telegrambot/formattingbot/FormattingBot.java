package pro.zackpollard.telegrambot.formattingbot;

import pro.zackpollard.telegrambot.api.TelegramBot;

/**
 * @author Zack Pollard
 */
public class FormattingBot {

    private static String API_KEY;

    private final TelegramBot telegramBot;

    public static void main(String[] args) {

        //This simply takes the bots API key from the first command line argument sent to the bot.
        //You do not have to retrieve the API key in this way.
        API_KEY = args[0];
        new FormattingBot();
    }

    public FormattingBot() {

        //This returns a logged in TelegramBot instance or null if the API key was invalid.
        telegramBot = TelegramBot.login(API_KEY);
        //This registers the FormattingListener Listener to this bot.
        telegramBot.getEventsManager().register(new FormattingListener(telegramBot));
        //This method starts the retrieval of updates.
        //The boolean it accepts is to specify whether to retrieve messages
        //which were sent before the bot was started but after the bot was last turned off.
        telegramBot.startUpdates(false);

        //The following while(true) loop is simply for keeping the java application alive.
        //You can do this however you like, but none of the above methods are blocking and
        //so without this code the bot would simply boot then exit.
        while (true) {
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}