package app.oprosnik;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class QuestionsList {
    private List<Question> questions;
    private Random random = new Random();

    public QuestionsList(File file) throws FileNotFoundException {
        questions = new ArrayList<>();
        Scanner scanner = new Scanner(file);

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();
            if (line.isEmpty()) continue;

            if (!line.startsWith(" ")) {
                Question question = new Question();
                question.setQuestion(line);
                question.setCorrectAnswers(new ArrayList<>());

                List<String> variants = new ArrayList<>();
                int variantNumber = 1;
                while (scanner.hasNextLine()) {
                    String variantLine = scanner.nextLine().trim();
                    if (variantLine.isEmpty()) break;

                    // Удаляем звездочку (*) при сохранении варианта
                    boolean isCorrect = variantLine.endsWith("*");
                    String cleanVariant = isCorrect
                            ? variantLine.substring(0, variantLine.length() - 1)
                            : variantLine;

                    variants.add(cleanVariant);
                    if (isCorrect) {
                        question.getCorrectAnswers().add(variantNumber);
                    }
                    variantNumber++;
                }

                question.setVariants(variants);
                questions.add(question);
            }
        }
        scanner.close();
    }

    // Добавляем метод для получения всех вопросов
    public List<Question> getAllQuestions() {
        return new ArrayList<>(questions); // Возвращаем копию списка
    }

    // Старый метод getRandomQuestion (переименованный для ясности)
    public Question getRandomQuestion() {
        return questions.get(random.nextInt(questions.size()));
    }

    // Метод для общего количества вопросов
    public int getTotalQuestions() {
        return questions.size();
    }
}