package app.oprosnik.state;

import app.oprosnik.Question;
import app.oprosnik.QuestionsList;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class Learner implements StateSession {
    private Question currentQuestion;
    private State state = State.INIT;
    private QuestionsList questionList;
    private List<Question> askedQuestions = new ArrayList<>();
    private int attempts = 1;
    private boolean answerCorrect = false;

    public Learner() {
        try {
            questionList = new QuestionsList(new File("src/main/resources/quest"));
        } catch (FileNotFoundException e) {
            state = State.ERROR;
        }
    }

    @Override
    public Question action() {
        if (answerCorrect || state == State.INIT) {
            currentQuestion = getNewQuestion();
            attempts = 1;
            answerCorrect = false;
        }
        state = State.ACTION;
        return currentQuestion;
    }

    private Question getNewQuestion() {
        Question question;
        do {
            question = questionList.getRandomQuestion();
        } while (askedQuestions.contains(question) && askedQuestions.size() < questionList.getTotalQuestions());

        askedQuestions.add(question);
        return question;
    }

    @Override
    public boolean check(String answer) {
        attempts++;
        try {
            int selectedAnswer = Integer.parseInt(answer.trim());
            answerCorrect = currentQuestion.isCorrect(selectedAnswer);
            state = answerCorrect ? State.CHECK : State.ACTION;
            return answerCorrect;
        } catch (NumberFormatException e) {
            state = State.ERROR;
            return false;
        }
    }

    @Override
    public String end() {
        if (answerCorrect) {
            return "Правильно! Следующий вопрос:";
        } else {
            return "Неправильно. Попробуйте еще раз:";
        }
    }

    @Override
    public State getState() {
        return state;
    }

    public int getAttempts() {
        return attempts;
    }
}