package cz.vse.pyraquest.command;

import cz.vse.pyraquest.game.GameState;
import cz.vse.pyraquest.domain.Item;
import cz.vse.pyraquest.domain.Mummy;
import cz.vse.pyraquest.domain.Room;

public class CommandOpen implements ICommand {
    private static final String NAME = "otevri";

    @Override
    public String execute(GameState state, String... parameters) {

        if (parameters.length == 0) {
            return state.getTexts().get("open.error.noParam");
        }

        Room room = state.getPlayer().getCurrentRoom();

        Item item = room.getItem(parameters[0]);

        if (item == null) {
            return state.getTexts().get("open.error.notHere");
        }

        if (!item.isContainer()) {
            return state.getTexts().get("open.error.cannot");
        }

        if (item.isLocked()) {
            return state.getTexts().get("open.error.locked");
        }

        if (item.isOpened()) {
            return state.getTexts().get("open.error.already");
        }

        if (room.getNpc("mumie") instanceof Mummy) {
            Mummy mummy = (Mummy) room.getNpc("mumie");

            if (!mummy.getRiddle().isSolved()) {
                return state.getTexts().get("open.error.riddle");
            }
        }

        item.setOpened(true);

        for (Item content : item.getContents()) {
            room.addItem(content);
        }

        item.getContents().clear();

        if (item.getCode().equals("truhla")) {
            return state.getTexts().get("open.chest");
        }

        if (item.getCode().equals("sarkofag")) {
            return state.getTexts().get("open.sarcophagus");
        }

        return state.getTexts().get("open.error.cannot");
    }


    @Override
    public String getName() {
        return NAME;
    }
}
