package app.oprosnik.state;

import app.oprosnik.Question;
import app.oprosnik.QuestionsList;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Examinator implements StateSession {
    private int correctCount = 0;
    private List<Question> examQuestions;
    private int currentQuestionIndex = 0;
    private State state = State.INIT;
    private Random random = new Random();
    private int totalExamQuestions;

    public Examinator() {
        try {
            QuestionsList questionList = new QuestionsList(new File("src/main/resources/quest"));
            prepareExamQuestions(questionList);
        } catch (FileNotFoundException e) {
            state = State.ERROR;
        }
    }

    private void prepareExamQuestions(QuestionsList questionList) {
        List<Question> allQuestions = new ArrayList<>(questionList.getAllQuestions());
        Collections.shuffle(allQuestions);

        int totalAvailable = allQuestions.size();
        this.totalExamQuestions = Math.max(5, random.nextInt(totalAvailable) + 1);

        this.examQuestions = allQuestions.subList(0, Math.min(totalExamQuestions, totalAvailable));

        if (totalAvailable < 5) {
            System.err.println("Внимание! В базе только " + totalAvailable + " вопросов");
        }
    }

    @Override
    public Question action() {
        if (state == State.INIT) {
            state = State.ACTION;
            return examQuestions.get(currentQuestionIndex++);
        }

        if (currentQuestionIndex >= examQuestions.size()) {
            state = State.END;
            return null;
        }

        return examQuestions.get(currentQuestionIndex++);
    }

    @Override
    public boolean check(String answer) {
        try {
            int selectedAnswer = Integer.parseInt(answer.trim());
            Question currentQuestion = examQuestions.get(currentQuestionIndex - 1);
            boolean isCorrect = currentQuestion.isCorrect(selectedAnswer);

            if (isCorrect) {
                correctCount++;
            }

            state = State.CHECK;
            return isCorrect;
        } catch (NumberFormatException e) {
            state = State.ERROR;
            return false;
        }
    }

    @Override
    public String end() {
        state = State.END;
        double percentage = (double) correctCount / totalExamQuestions * 100;
        return String.format(
                "Экзамен завершен!\n" +
                        "Всего вопросов: %d\n" +
                        "Правильных ответов: %d\n" +
                        "Процент правильных: %.1f%%",
                totalExamQuestions, correctCount, percentage
        );
    }

    @Override
    public State getState() {
        return state;
    }

    public int getCurrentQuestionNumber() {
        return currentQuestionIndex;
    }

    public int getTotalQuestions() {
        return totalExamQuestions;
    }
}