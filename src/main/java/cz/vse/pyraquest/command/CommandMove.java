package cz.vse.pyraquest.command;

import cz.vse.pyraquest.game.GameState;

public class CommandMove implements ICommand {
    private static final String NAME = "jdi";

    @Override
    public String execute(GameState state, String... parameters) {
        return null;
    }

    @Override
    public String getName() {
        return NAME;
    }
}
