package app.oprosnik.sender;

import app.oprosnik.Question;
import app.oprosnik.state.StateSession;
import app.oprosnik.state.Study;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import java.io.FileNotFoundException;

public class StudySender extends Sender {
    public StudySender() {
        stateSession = new Study();
    }

    @Override
    public void onMessageReceived(String message) {
        stateSession.check(message);
    }

    @Override
    public SendMessage createSendMessage() throws FileNotFoundException {
        SendMessage message = new SendMessage();
        StateSession.State state = stateSession.getState();

        if (state == StateSession.State.INIT || state == StateSession.State.ACTION) {
            Question question = stateSession.action();
            message.setText(formatQuestion(question));
        } else {
            message.setText(stateSession.end());
            // Автоматически переходим к следующему вопросу
            stateSession.action();
            message.setText(message.getText() + "\n\nНовый вопрос:\n" +
                    formatQuestion(stateSession.action()));
        }
        return message;
    }

    private String formatQuestion(Question question) {
        StringBuilder sb = new StringBuilder(question.getQuestion() + "\n");
        for (int i = 0; i < question.getVariants().size(); i++) {
            sb.append(i+1).append(") ").append(question.getVariants().get(i)).append("\n");
        }
        return sb.toString();
    }
}