package app.oprosnik;

import java.util.List;

public class Question {
    private String question;
    private List<String> variants;
    private List<Integer> correctAnswers; // Multiple correct answers support
    private List<Integer> incorrectAnswers;

    // Getters and setters
    public String getQuestion() { return question; }
    public void setQuestion(String question) { this.question = question; }
    public List<String> getVariants() { return variants; }
    public void setVariants(List<String> variants) { this.variants = variants; }
    public List<Integer> getCorrectAnswers() { return correctAnswers; }
    public void setCorrectAnswers(List<Integer> correctAnswers) { this.correctAnswers = correctAnswers; }
    public List<Integer> getIncorrectAnswers() { return incorrectAnswers; }
    public void setIncorrectAnswers(List<Integer> incorrectAnswers) { this.incorrectAnswers = incorrectAnswers; }

    public boolean isCorrect(int answer) {
        return correctAnswers.contains(answer);
    }
}