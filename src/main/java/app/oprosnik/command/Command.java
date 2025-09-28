package app.oprosnik.command;

import app.oprosnik.sender.Sender;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.Objects;

public abstract class Command {
    protected String text;

    public abstract Sender execute(SendMessage message);

    public Command getText(String message) {
        if (Objects.equals(message, "/exam")) {
            return new ExemCommand();
        } else if (Objects.equals(message, "/study")) {
            return new StudyCommand();
        } else if (Objects.equals(message, "/learn")) {
            return new LearnCommand();
        }
        return null;
    }
}
