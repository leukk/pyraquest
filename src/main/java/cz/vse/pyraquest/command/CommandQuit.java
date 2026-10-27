package cz.vse.pyraquest.command;

import cz.vse.pyraquest.game.GameState;

public class CommandQuit implements ICommand {
    private static final String NAME = "konec";

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
        state.quit();
        return state.getTexts().get("system.quit");
    }
}