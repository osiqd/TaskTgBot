package app.oprosnik.command;

import app.oprosnik.sender.ExaminatorSender;
import app.oprosnik.sender.Sender;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public class ExemCommand extends Command {
    @Override
    public Sender execute(SendMessage message) {
        return new ExaminatorSender();
    }
}
