package app.oprosnik.sender;

import app.oprosnik.Question;
import app.oprosnik.state.Learner;
import app.oprosnik.state.StateSession;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import java.io.FileNotFoundException;

public class LearnSender extends Sender {
    public LearnSender() {
        stateSession = new Learner();
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
            message.setText(formatQuestion(question, state));
        } else if (state == StateSession.State.CHECK) {
            String response = stateSession.end();
            Question nextQuestion = stateSession.action();
            message.setText(response + "\n\n" + formatQuestion(nextQuestion, state));
        } else {
            message.setText("Произошла ошибка");
        }

        return message;
    }

    private String formatQuestion(Question question, StateSession.State state) {
        StringBuilder sb = new StringBuilder();
        if (state == StateSession.State.ACTION) {
            sb.append("Попытка ").append(((Learner)stateSession).getAttempts()).append(":\n");
        }
        sb.append(question.getQuestion()).append("\n");
        for (int i = 0; i < question.getVariants().size(); i++) {
            sb.append(i + 1).append(". ").append(question.getVariants().get(i)).append("\n");
        }
        return sb.toString();
    }
}