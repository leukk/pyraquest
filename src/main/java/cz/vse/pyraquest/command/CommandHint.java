package cz.vse.pyraquest.command;

import cz.vse.pyraquest.game.GameState;

public class CommandHint implements ICommand {
    private static final String NAME = "napoveda";

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public boolean isSystem() {
        return true;
    }

    @Override
    public String execute(GameState state, String... parameters) {
        return state.getTexts().get("system.hint");
    }
}