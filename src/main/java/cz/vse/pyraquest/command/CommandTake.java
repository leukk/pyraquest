package cz.vse.pyraquest.command;

import cz.vse.pyraquest.game.GameState;

public class CommandTake implements ICommand {
    private static final String NAME = "seber";

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String execute(GameState state, String... parameters) {
        if (parameters.length == 0)
            return state.getTexts().get("take.error.noParam");

        var item = state.getPlayer().getCurrentRoom().getItem(parameters[0]);
        if (item == null)
            return state.getTexts().get("take.error.notHere");

        if (!item.isPortable())
            return state.getTexts().get("take.error.notPortable");

        if (state.getPlayer().getBackpack().add(item)) {
            state.getPlayer().getCurrentRoom().removeItem(item);
            return state.getTexts().get("take.success", item.getName());
        }

        return state.getTexts().get("take.error.full");
    }
}
