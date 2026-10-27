package cz.vse.pyraquest.domain;

import java.util.*;

public class Riddle {
    private final List<Question> allQuestions;
    private final List<Question> selectedQuestions;

    private final Random random;

    private int currentQuestionIndex;
    private int correctAnswers;

    private boolean started;
    private boolean solved;

    public Riddle(List<Question> allQuestions, long seed) {
        this.allQuestions = new ArrayList<>(allQuestions);
        this.selectedQuestions = new ArrayList<>();

        this.random = new Random(seed);

        this.currentQuestionIndex = 0;
        this.correctAnswers = 0;

        this.started = false;
        this.solved = false;

        validateQuestions();
    }

    private void validateQuestions() {
        if (allQuestions.size() < 3) {
            throw new IllegalArgumentException(
                    "Hádanka musí obsahovat alespoň 3 otázky."
            );
        }

        for (Question question : allQuestions) {
            if (question.getCorrect() < 1 || question.getCorrect() > 4) {
                throw new IllegalArgumentException(
                        "Správná odpověď musí být číslo od 1 do 4.."
                );
            }
        }
    }

    public void start() {
        if (started) {
            return;
        }

        List<Question> shuffledQuestions = new ArrayList<>(allQuestions);

        Collections.shuffle(shuffledQuestions, random);

        for (int i = 0; i < 3; i++) {
            selectedQuestions.add(shuffledQuestions.get(i));
        }

        started = true;
    }

    public Question getCurrentQuestion() {
        if (!started || solved) {
            return null;
        }

        return selectedQuestions.get(currentQuestionIndex);
    }

    public void answerCorrectly() {
        if (!started || solved) {
            return;
        }

        correctAnswers++;
        currentQuestionIndex++;

        if (correctAnswers == 3) {
            solved = true;
        }
    }

    public boolean isStarted() {return started;}

    public boolean isSolved() {return solved;}

    public int getCorrectAnswers() {return correctAnswers;}

    public int getCurrentQuestionIndex() {return currentQuestionIndex;}

    public List<Question> getSelectedQuestions() {return new ArrayList<>(selectedQuestions);}

}
