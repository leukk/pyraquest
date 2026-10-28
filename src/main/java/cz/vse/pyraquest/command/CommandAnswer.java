package cz.vse.pyraquest.command;

import cz.vse.pyraquest.game.GameState;
import cz.vse.pyraquest.domain.Mummy;
import cz.vse.pyraquest.domain.Npc;
import cz.vse.pyraquest.domain.Question;
import cz.vse.pyraquest.domain.Riddle;

public class CommandAnswer implements ICommand {
    private static final String NAME = "odpovez";

    @Override
    public String execute(GameState state, String... parameters) {

        if (parameters.length == 0) {
            return state.getTexts().get("answer.error.invalid");
        }

        int answer;

        try {
            answer = Integer.parseInt(parameters[0]);
        } catch (NumberFormatException e) {
            return state.getTexts().get("answer.error.invalid");
        }

        if (answer < 1 || answer > 4) {
            return state.getTexts().get("answer.error.invalid");
        }

        Npc npc = state.getPlayer().getCurrentRoom().getNpc("mumie");

        if (!(npc instanceof Mummy)) {
            return state.getTexts().get("answer.error.noQuestion");
        }

        Mummy mummy = (Mummy) npc;
        Riddle riddle = mummy.getRiddle();

        if (!riddle.isStarted()) {
            return state.getTexts().get("answer.error.noQuestion");
        }

        if (riddle.isSolved()) {
            return state.getTexts().get("answer.error.alreadySolved");
        }

        Question question = riddle.getCurrentQuestion();

        if (question == null) {
            return state.getTexts().get("answer.error.noQuestion");
        }

        if (answer == question.getCorrect()) {
            riddle.answerCorrectly();

            if (riddle.isSolved()) {
                return state.getTexts().get("answer.solved");
            }

            return state.getTexts().get("answer.correct");
        }

        state.addWrongAnswer();

        if (state.getWrongAnswers() >= 3) {
            state.lose();
            return state.getTexts().get("answer.lose");
        }

        return state.getTexts().get(
                "answer.wrong",
                state.getWrongAnswers()
        );
    }

    @Override
    public String getName() {
        return NAME;
    }
}
