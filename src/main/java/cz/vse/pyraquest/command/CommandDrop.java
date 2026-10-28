package cz.vse.pyraquest.command;

import cz.vse.pyraquest.game.GameState;

public class CommandDrop implements ICommand {
    private static final String NAME = "poloz";

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String execute(GameState state, String... parameters) {
        if (parameters.length == 0)
            return state.getTexts().get("drop.error.noParam");

        var backpack = state.getPlayer().getBackpack();
        var item = backpack.get(parameters[0]);
        if (item == null)
            return state.getTexts().get("drop.error.notInBackpack");

        backpack.remove(item);
        state.getPlayer().getCurrentRoom().addItem(item);
        return state.getTexts().get("drop.success", item.getName());
    }
}
