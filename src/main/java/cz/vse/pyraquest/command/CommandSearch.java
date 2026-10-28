package cz.vse.pyraquest.command;

import cz.vse.pyraquest.game.GameState;
import cz.vse.pyraquest.domain.Backpack;
import cz.vse.pyraquest.domain.Enemy;
import cz.vse.pyraquest.domain.Item;
import cz.vse.pyraquest.domain.Room;
import cz.vse.pyraquest.domain.Skeleton;


public class CommandSearch implements ICommand {
    private static final String NAME = "prohledni";

    @Override
    public String execute(GameState state, String... parameters) {

        if (parameters.length == 0) {
            return state.getTexts().get("search.error.noParam");
        }

        Room room = state.getPlayer().getCurrentRoom();

        Item item = room.getItem(parameters[0]);

        if (item == null) {
            return state.getTexts().get("search.error.notHere");
        }

        if (!item.isContainer()) {
            return state.getTexts().get("search.error.cannot");
        }

        if (item.isSearched()) {
            return state.getTexts().get("search.error.done");
        }

        if (room.getNpc("netopyr") instanceof Enemy) {
            Enemy bat = (Enemy) room.getNpc("netopyr");

            if (bat.isAlive()) {
                state.lose();
                return state.getTexts().get("search.error.bat");
            }
        }

        Skeleton skeleton = (Skeleton) state.getRoom("sal").getNpc("kostlivec");

        if (!skeleton.hasTalkedTo()) {
            return state.getTexts().get("search.error.notTalked");
        }

        Item bone = null;

        for (Item content : item.getContents()) {
            if (content.getCode().equals("kost")) {
                bone = content;
                break;
            }
        }

        if (bone == null) {
            item.setSearched(true);
            return state.getTexts().get("search.error.done");
        }

        item.removeItem(bone);

        room.addItem(bone);

        item.setSearched(true);

        Backpack backpack = state.getPlayer().getBackpack();

        if (backpack.isFull()) {
            return state.getTexts().get("search.successFull");
        }

        return state.getTexts().get("search.success");
    }

    @Override
    public String getName() {
        return NAME;
    }
}
