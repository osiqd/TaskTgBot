package app.oprosnik.command;

import app.oprosnik.sender.LearnSender;
import app.oprosnik.sender.Sender;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public class LearnCommand extends Command {
    @Override
    public Sender execute(SendMessage message) {
        return new LearnSender();
    }
}