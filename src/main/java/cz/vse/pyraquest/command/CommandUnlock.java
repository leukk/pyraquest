package cz.vse.pyraquest.command;

import cz.vse.pyraquest.game.GameState;
import cz.vse.pyraquest.domain.Item;
import cz.vse.pyraquest.domain.Mummy;
import cz.vse.pyraquest.domain.Room;


public class CommandUnlock implements ICommand {
    private static final String NAME = "odemkni";

    @Override
    public String execute(GameState state, String... parameters) {

        if (parameters.length == 0) {
            return state.getTexts().get("unlock.error.noParam");
        }

        Room room = state.getPlayer().getCurrentRoom();

        Item item = room.getItem(parameters[0]);

        if (item == null) {
            return state.getTexts().get("unlock.error.notHere");
        }

        if (!item.isContainer() || !item.isLocked()) {
            return state.getTexts().get("unlock.error.cannot");
        }

        if (item.isOpened()) {
            return state.getTexts().get("unlock.error.already");
        }

        if (room.getNpc("mumie") instanceof Mummy) {
            Mummy mummy = (Mummy) room.getNpc("mumie");

            if (!mummy.getRiddle().isSolved()) {
                return state.getTexts().get("unlock.error.riddle");
            }
        }

        if (!state.getPlayer().getBackpack().contains("klic")) {
            return state.getTexts().get("unlock.error.noKey");
        }

        item.setLocked(false);

        return state.getTexts().get("unlock.success");
    }

    @Override
    public String getName() {
        return NAME;
    }
}
