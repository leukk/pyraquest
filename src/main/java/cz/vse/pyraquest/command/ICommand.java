package cz.vse.pyraquest.command;

import cz.vse.pyraquest.game.GameState;

public interface ICommand {
    String getName();

    String execute(GameState state, String... parameters);

    default boolean isSystem() {
        return false;
    }
}