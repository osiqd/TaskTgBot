package app.oprosnik.TelegramBot;

import app.oprosnik.command.Command;
import app.oprosnik.command.ExemCommand;
import app.oprosnik.sender.Sender;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Objects;

public class Bot extends TelegramLongPollingBot {
    Command command = new ExemCommand();
    HashMap<Long, Sender> userchat = new HashMap<>();

    public Bot(String botToken) {
        super(botToken);
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            Long chatId = update.getMessage().getChatId();
            String text = update.getMessage().getText();
            SendMessage message = new SendMessage();//новое сообщение
            // Определение текущего режима бота
            if (text.startsWith("/")) {
                Command comd = command.getText(text);
                if (comd == null) {
                    if (Objects.equals(text, "/exit")) {

                    }
                    message.setChatId(chatId);
                    message.setText(help());
                    try {
                        execute(message);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                }
                Sender stateSession = comd.execute(message);
                if (stateSession != null) {
                    userchat.put(chatId, stateSession);
                    try {
                        message = stateSession.createSendMessage();
                    } catch (FileNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                }
                // если не сменили режим, а просто была команда
            } else {
                Sender stateSession = userchat.get(chatId);
                if (stateSession == null) message.setText("Ошибочная команда!");
                else {
                    stateSession.onMessageReceived(text);
                    try {
                        message = stateSession.createSendMessage();
                    } catch (FileNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                }
                //обработать сообщение как команду или отдельно
            }
            message.setChatId(chatId.toString());
            try {
                execute(message);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String getBotUsername() {
        return "ExaminatorMolchanovBot";
    }

    private String help() {
        return "/help - список команд\n" +
                "/exam - режим экзамена\n" +
                "/study - режим викторины\n" +
                "/learn - режим обучения\n";
    }
}
