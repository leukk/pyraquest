package cz.vse.pyraquest.domain;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class RiddleTest {

    private List<Question> createQuestions() {
        List<Question> questions = new ArrayList<>();

        for (int i = 1; i <= 9; i++) {
            Question question = new Question();
            questions.add(createQuestion(i));
        }

        return questions;
    }

    private Question createQuestion(int id) {
        return new TestQuestion(id).toQuestion();
    }

    @Test
    void selectsThreeQuestions() {
        Riddle riddle = new Riddle(createQuestions(), 123);

        riddle.start();

        assertEquals(3, riddle.getSelectedQuestions().size());
    }

    @Test
    void sameSeedSelectsSameQuestions() {
        Riddle riddle1 = new Riddle(createQuestions(), 123);
        Riddle riddle2 = new Riddle(createQuestions(), 123);

        riddle1.start();
        riddle2.start();

        assertEquals(
                getQuestionIds(riddle1),
                getQuestionIds(riddle2)
        );
    }

    @Test
    void otherSeedSelectOtherQuestions() {
        Riddle riddle1 = new Riddle(createQuestions(), 123);
        Riddle riddle2 = new Riddle(createQuestions(), 456);

        riddle1.start();
        riddle2.start();

        assertNotEquals(
                getQuestionIds(riddle1),
                getQuestionIds(riddle2)
        );
    }

    @Test
    void correctAnswerMovesToNextQuestion() {
        Riddle riddle = new Riddle(createQuestions(), 123);

        riddle.start();

        Question firstQuestion = riddle.getCurrentQuestion();

        riddle.answerCorrectly();

        Question secondQuestion = riddle.getCurrentQuestion();

        assertNotEquals(firstQuestion.getId(), secondQuestion.getId());
        assertEquals(1, riddle.getCorrectAnswers());
    }

    @Test
    void threeCorrectAnswersSolveRiddle() {
        Riddle riddle = new Riddle(createQuestions(), 123);

        riddle.start();

        riddle.answerCorrectly();
        riddle.answerCorrectly();
        riddle.answerCorrectly();

        assertTrue(riddle.isSolved());
        assertEquals(3, riddle.getCorrectAnswers());
    }

    @Test
    void questionsAreNotDuplicated() {
        Riddle riddle = new Riddle(createQuestions(), 123);

        riddle.start();

        List<Question> selectedQuestions = riddle.getSelectedQuestions();

        Set<Integer> questionIds = new HashSet<>();

        for (Question question : selectedQuestions) {
            questionIds.add(question.getId());
        }

        assertEquals(3, questionIds.size());
    }

    private List<Integer> getQuestionIds(Riddle riddle) {
        List<Integer> ids = new ArrayList<>();

        for (Question question : riddle.getSelectedQuestions()) {
            ids.add(question.getId());
        }

        return ids;
    }


    private static class TestQuestion {
        private final int id;

        private TestQuestion(int id) {
            this.id = id;
        }

        private Question toQuestion() {
            Question question = new Question();

            try {
                var idField = Question.class.getDeclaredField("id");
                var textField = Question.class.getDeclaredField("text");
                var optionsField = Question.class.getDeclaredField("options");
                var correctField = Question.class.getDeclaredField("correct");

                idField.setAccessible(true);
                textField.setAccessible(true);
                optionsField.setAccessible(true);
                correctField.setAccessible(true);

                idField.set(question, id);
                textField.set(question, "Testovaci otazka " + id);
                optionsField.set(question, List.of("A", "B", "C", "D"));
                correctField.set(question, 1);

            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            return question;
        }
    }
}