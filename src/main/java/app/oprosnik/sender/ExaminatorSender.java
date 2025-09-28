package app.oprosnik.sender;

import app.oprosnik.Question;
import app.oprosnik.state.Examinator;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import java.io.FileNotFoundException;

public class ExaminatorSender extends Sender {
    public ExaminatorSender() {
        stateSession = new Examinator();
    }

    @Override
    public void onMessageReceived(String message) {
        stateSession.check(message);
    }

    @Override
    public SendMessage createSendMessage() throws FileNotFoundException {
        SendMessage message = new SendMessage();

        switch (stateSession.getState()) {
            case INIT:
                // Инициализация + сразу первый вопрос
                message.setText("Начинаем экзамен! Количество вопросов: " +
                        ((Examinator)stateSession).getTotalQuestions() +
                        "\n\n" + formatQuestion(stateSession.action()));
                break;

            case ACTION:
                message.setText(formatQuestion(stateSession.action()));
                break;

            case CHECK:
                Question nextQuestion = stateSession.action();
                if (nextQuestion != null) {
                    message.setText("Ответ принят. Следующий вопрос:\n\n" +
                            formatQuestion(nextQuestion));
                } else {
                    message.setText(stateSession.end());
                }
                break;

            case END:
                message.setText(stateSession.end());
                break;

            default:
                message.setText("Произошла ошибка");
        }

        return message;
    }

    private String formatQuestion(Question question) {
        StringBuilder sb = new StringBuilder();
        sb.append("Вопрос ").append(((Examinator)stateSession).getCurrentQuestionNumber())
                .append(" из ").append(((Examinator)stateSession).getTotalQuestions()).append("\n");
        sb.append(question.getQuestion()).append("\n");

        for (int i = 0; i < question.getVariants().size(); i++) {
            sb.append(i + 1).append(". ").append(question.getVariants().get(i)).append("\n");
        }

        return sb.toString();
    }
}