package cz.vse.pyraquest.command;

import cz.vse.pyraquest.game.GameState;
import cz.vse.pyraquest.domain.Mummy;
import cz.vse.pyraquest.domain.Npc;
import cz.vse.pyraquest.domain.Question;
import cz.vse.pyraquest.domain.Room;
import cz.vse.pyraquest.domain.Skeleton;
import cz.vse.pyraquest.domain.Riddle;

public class CommandTalk implements ICommand {
    private static final String NAME = "mluv";

    @Override
    public String execute(GameState state, String... parameters) {

        if (parameters.length == 0) {
            return state.getTexts().get("talk.error.noParam");
        }


        Room room = state.getPlayer().getCurrentRoom();

        Npc npc = room.getNpc(parameters[0]);

        if (npc == null) {
            return state.getTexts().get("talk.error.notHere");
        }

        if (npc instanceof Skeleton) {
            Skeleton skeleton = (Skeleton) npc;

            skeleton.talk();

            if (skeleton.hasTraded()) {
                return state.getTexts().get("skeleton.talk.after");
            }

            return state.getTexts().get("skeleton.talk.before");
        }

        if (npc instanceof Mummy) {
            Mummy mummy = (Mummy) npc;
            Riddle riddle = mummy.getRiddle();

            if (riddle.isSolved()) {
                return state.getTexts().get("mummy.talk.solved");
            }

            boolean firstTalk = !riddle.isStarted();

            if (firstTalk) {
                riddle.start();
            }

            String result;

            if (firstTalk) {
                result = state.getTexts().get("mummy.talk.start");
            } else {
                result = state.getTexts().get("mummy.talk.waiting");
            }

            Question question = riddle.getCurrentQuestion();

            result += "\n\n" + state.getTexts().get(
                    "riddle.question.header",
                    riddle.getCurrentQuestionIndex() + 1
            );

            result += "\n" + question.getText();

            for (int i = 0; i < question.getOptions().size(); i++) {
                result += "\n" + (i + 1) + ". "
                        + question.getOptions().get(i);
            }

            result += "\n" + state.getTexts().get("riddle.question.footer");

            return result;
        }

        return state.getTexts().get("talk.error.cannot");
    }

    @Override
    public String getName() {
        return NAME;
    }
}
