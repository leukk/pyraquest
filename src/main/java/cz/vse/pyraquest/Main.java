package cz.vse.pyraquest;

import cz.vse.pyraquest.data.WorldLoader;
import cz.vse.pyraquest.game.Game;

public class Main {
    public static void main(String[] args) {
        new Game(WorldLoader::load).run();
    }
}