package cz.vse.pyraquest.command;

import cz.vse.pyraquest.game.GameState;
import cz.vse.pyraquest.domain.Backpack;
import cz.vse.pyraquest.domain.Item;
import cz.vse.pyraquest.domain.Npc;
import cz.vse.pyraquest.domain.Room;
import cz.vse.pyraquest.domain.Skeleton;

public class CommandGive implements ICommand {
    private static final String NAME = "dej";

    @Override
    public String execute(GameState state, String... parameters) {

        if (parameters.length < 2) {
            return state.getTexts().get("give.error.noParam");
        }

        Backpack backpack = state.getPlayer().getBackpack();

        Item item = backpack.get(parameters[0]);

        if (item == null) {
            return state.getTexts().get("give.error.notInBackpack");
        }

        Room room = state.getPlayer().getCurrentRoom();

        Npc npc = room.getNpc(parameters[1]);

        if (npc == null) {
            return state.getTexts().get("give.error.notHere");
        }

        if (npc.getCode().equals("mumie")) {
            return state.getTexts().get("give.error.mummy");
        }

        if (!(npc instanceof Skeleton)) {
            return state.getTexts().get("give.error.notNpc");
        }

        Skeleton skeleton = (Skeleton) npc;

        if (skeleton.hasTraded()) {
            return state.getTexts().get("give.error.skeleton");
        }

        if (!skeleton.acceptsGift(item)) {
            return state.getTexts().get("give.error.skeleton");
        }

        Item key = null;

        for (Item npcItem : skeleton.getItems()) {
            if (npcItem.getCode().equals("klic")) {
                key = npcItem;
                break;
            }
        }

        if (key == null) {
            return state.getTexts().get("give.error.skeleton");
        }

        backpack.remove(item);

        skeleton.addItem(item);

        skeleton.removeItem(key);

        backpack.add(key);

        skeleton.markTraded();

        return state.getTexts().get("skeleton.give.success");
    }

    @Override
    public String getName() {
        return NAME;
    }
}
