package cz.vse.pyraquest.command;

import cz.vse.pyraquest.game.GameState;

public class CommandOpen implements ICommand {
    private static final String NAME = "otevri";

    @Override
    public String execute(GameState state, String... parameters) {
        return null;
    }

    @Override
    public String getName() {
        return NAME;
    }
}
