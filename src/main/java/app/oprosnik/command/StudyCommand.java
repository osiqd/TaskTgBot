package app.oprosnik.command;

import app.oprosnik.sender.Sender;
import app.oprosnik.sender.StudySender;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public class StudyCommand extends Command {
    @Override
    public Sender execute(SendMessage message) {
        return new StudySender();
    }
}