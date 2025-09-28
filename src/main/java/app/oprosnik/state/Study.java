package app.oprosnik.state;

import app.oprosnik.Question;
import app.oprosnik.QuestionsList;
import java.io.File;
import java.io.FileNotFoundException;

public class Study implements StateSession {
    private Question currentQuestion;
    private State state = State.INIT;
    private QuestionsList questionList;

    public Study() {
        try {
            questionList = new QuestionsList(new File("src/main/resources/quest"));
        } catch (FileNotFoundException e) {
            state = State.ERROR;
        }
    }

    @Override
    public Question action() {
        currentQuestion = questionList.getRandomQuestion();
        state = State.ACTION;
        return currentQuestion;
    }

    @Override
    public boolean check(String answer) {
        state = State.CHECK;
        try {
            int selectedAnswer = Integer.parseInt(answer.trim());
            return currentQuestion.isCorrect(selectedAnswer);
        } catch (NumberFormatException e) {
            state = State.ERROR;
            return false;
        }
    }

    @Override
    public String end() {
        state = State.END;
        StringBuilder sb = new StringBuilder("Правильный ответ: ");
        for (Integer correct : currentQuestion.getCorrectAnswers()) {
            sb.append(correct).append(") ").append(currentQuestion.getVariants().get(correct-1)).append(" ");
        }
        return sb.toString();
    }

    @Override
    public State getState() {
        return state;
    }
}