package cz.vse.pyraquest.command;

import cz.vse.pyraquest.data.WorldLoader;
import cz.vse.pyraquest.domain.Mummy;
import cz.vse.pyraquest.domain.Question;
import cz.vse.pyraquest.domain.Room;
import cz.vse.pyraquest.game.GameState;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CommandAnswerTest {
    @Test
    void correctAnswerMovesToNextQuestion() {
        GameState state = WorldLoader.load(123);

        Room room = state.getRoom("komnata");
        state.getPlayer().setCurrentRoom(room);

        Mummy mummy = (Mummy) room.getNpc("mumie");
        mummy.getRiddle().start();

        Question question = mummy.getRiddle().getCurrentQuestion();
        int correctAnswer = question.getCorrect();

        CommandAnswer command = new CommandAnswer();

        command.execute(state, String.valueOf(correctAnswer));

        assertEquals(1, mummy.getRiddle().getCorrectAnswers());
        assertEquals(1, mummy.getRiddle().getCurrentQuestionIndex());
    }

    @Test
    void wrongAnswerIncreasesWrongAnswers() {
        GameState state = WorldLoader.load(123);

        Room room = state.getRoom("komnata");
        state.getPlayer().setCurrentRoom(room);

        Mummy mummy = (Mummy) room.getNpc("mumie");
        mummy.getRiddle().start();

        Question question = mummy.getRiddle().getCurrentQuestion();

        int wrongAnswer = question.getCorrect() == 1 ? 2 : 1;

        CommandAnswer command = new CommandAnswer();

        command.execute(state, String.valueOf(wrongAnswer));

        assertEquals(1, state.getWrongAnswers());
        assertEquals(0, mummy.getRiddle().getCorrectAnswers());
    }

    @Test
    void thirdWrongAnswerCausesLoss() {
        GameState state = WorldLoader.load(123);

        Room room = state.getRoom("komnata");
        state.getPlayer().setCurrentRoom(room);

        Mummy mummy = (Mummy) room.getNpc("mumie");
        mummy.getRiddle().start();

        CommandAnswer command = new CommandAnswer();

        for (int i = 0; i < 3; i++) {
            Question question = mummy.getRiddle().getCurrentQuestion();

            int wrongAnswer = question.getCorrect() == 1 ? 2 : 1;

            command.execute(state, String.valueOf(wrongAnswer));
        }

        assertEquals(GameState.Status.LOST, state.getStatus());
        assertEquals(3, state.getWrongAnswers());
    }

}
