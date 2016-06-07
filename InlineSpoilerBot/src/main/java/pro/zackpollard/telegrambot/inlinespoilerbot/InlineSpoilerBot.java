package pro.zackpollard.telegrambot.inlinespoilerbot;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import pro.zackpollard.telegrambot.api.TelegramBot;

import java.io.*;

/**
 * @author Zack Pollard
 */
public class InlineSpoilerBot {

    private static String API_KEY;

    private final TelegramBot telegramBot;
    private InlineSpoilerListener listener;
    private static InlineSpoilerBot instance;

    private final static Gson gson = new Gson();
    private final static File saveLocation = new File("./spoilers.json");

    public static void main(String[] args) {

        //This simply takes the bots API key from the first command line argument sent to the bot.
        //You do not have to retrieve the API key in this way.
        API_KEY = args[0];
        new InlineSpoilerBot();
    }

    public InlineSpoilerBot() {

        instance = this;

        //This returns a logged in TelegramBot instance or null if the API key was invalid.
        telegramBot = TelegramBot.login(API_KEY);
        //This registers the SpoilerListener Listener to this bot.
        initSpoilerManager();
        telegramBot.getEventsManager().register(listener);
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

    public Gson getGson() {

        return gson;
    }

    public void initSpoilerManager() {

        if (saveLocation.exists()) {

            listener = loadSpoilerManager();
        } else {

            try {
                saveLocation.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

            listener = new InlineSpoilerListener();

            this.saveSpoilers();
        }

        if (listener == null) {

            System.err.println("The save file could not be loaded. Either fix the save file or delete it and restart the bot.");
            System.exit(1);
        }
    }

    private InlineSpoilerListener loadSpoilerManager() {

        InlineSpoilerListener loadedSaveFile;

        try (Reader reader = new InputStreamReader(new FileInputStream(saveLocation), "UTF-8")) {

            Gson gson = new GsonBuilder().create();
            loadedSaveFile = gson.fromJson(reader, InlineSpoilerListener.class);

            return loadedSaveFile;
        } catch (IOException e) {

            e.printStackTrace();
        }

        return null;
    }

    public boolean saveSpoilers() {

        String json = gson.toJson(listener);

        FileOutputStream outputStream;

        try {

            outputStream = new FileOutputStream(saveLocation);
            outputStream.write(json.getBytes());
            outputStream.close();

            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.err.println("The save file could not be saved as the file couldn't be found on the storage device. Please check the directories read/write permissions and contact the developer!");
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("The config could not be written to as an error occured. Please check the directories read/write permissions and contact the developer!");
        }

        return false;
    }

    public TelegramBot getTelegramBot() {
        return telegramBot;
    }

    public static InlineSpoilerBot getInstance() {
        return instance;
    }
}