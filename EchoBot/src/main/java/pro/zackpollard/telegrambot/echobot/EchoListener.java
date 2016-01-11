package pro.zackpollard.telegrambot.echobot;

import pro.zackpollard.telegrambot.api.TelegramBot;
import pro.zackpollard.telegrambot.api.chat.message.send.ParseMode;
import pro.zackpollard.telegrambot.api.chat.message.send.SendableTextMessage;
import pro.zackpollard.telegrambot.api.event.Listener;
import pro.zackpollard.telegrambot.api.event.chat.message.TextMessageReceivedEvent;

/**
 * @author Zack Pollard
 */
public class EchoListener implements Listener {

    private final TelegramBot telegramBot;

    public EchoListener(TelegramBot telegramBot) {

        this.telegramBot = telegramBot;
    }

    //All events are in the Listener class in the bot API classes.
    //You can see the available events here https://github.com/zackpollard/JavaTelegramBot-API/blob/master/src/main/java/pro/zackpollard/telegrambot/api/event/Listener.java
    //You can use any of those events in the same way as the one below is used.

    //This event simply listens for any received message and replies with the same message parsed through markdown.
    @Override
    public void onTextMessageReceived(TextMessageReceivedEvent event) {

        //This is where we construct a SendableTextMessage
        //You can send any type of SendableMessage
        //The available types of messages can be found here https://github.com/zackpollard/JavaTelegramBot-API/tree/master/src/main/java/pro/zackpollard/telegrambot/api/chat/message/send
        SendableTextMessage sendableMessage = SendableTextMessage.builder()
                .message(event.getContent().getContent())
                .replyTo(event.getMessage())
                .parseMode(ParseMode.MARKDOWN)
                .build();

        //This is how you send a message when you have a chat object.
        //You could also send the message to the same chat by using the following line of code.
        //telegramBot.sendMessage(event.getChat(), sendableMessage);
        event.getChat().sendMessage(sendableMessage, telegramBot);
    }
}
