package app.oprosnik.sender;

import app.oprosnik.state.StateSession;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.io.FileNotFoundException;

public abstract class Sender {
    StateSession stateSession; //режим
    long chatID;//пользователь

    public abstract void onMessageReceived(String message);//получить сообщение

    public abstract SendMessage createSendMessage() throws FileNotFoundException;//отправить сообщение
}