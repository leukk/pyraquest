package cz.vse.pyraquest.command;

import cz.vse.pyraquest.game.GameState;

public class CommandIgnite implements ICommand {
    private static final String NAME = "zapal";

    @Override
    public String execute(GameState state, String... parameters) {
        if (parameters.length == 0)
            return state.getTexts().get("ignite.error.noParam");

        var backpack = state.getPlayer().getBackpack();
        var room = state.getPlayer().getCurrentRoom();

        var item = backpack.get(parameters[0]);
        if (item == null)
            item = room.getItem(parameters[0]);

        if (item == null)
            return state.getTexts().get("ignite.error.notHere");
        if (!item.isIgnitable())
            return state.getTexts().get("ignite.error.cannot");
        if (item.isLit())
            return state.getTexts().get("ignite.error.burning");
        if (!backpack.contains("kresadlo"))
            return state.getTexts().get("ignite.error.noFlint");

        var isTnt = item.getCode().equals("tnt");
        if (isTnt && backpack.contains("tnt"))
            return state.getTexts().get("ignite.error.tntInBackpack");

        if (isTnt && !room.getCode().equals("predsin"))
            return state.getTexts().get("ignite.error.tntWrongRoom");

        item.setLit(true);
        return state.getTexts().get(isTnt ? "ignite.tnt" : "ignite.torch");
    }

    @Override
    public String getName() {
        return NAME;
    }
}
