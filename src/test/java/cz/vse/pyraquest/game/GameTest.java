package cz.vse.pyraquest.game;

import cz.vse.pyraquest.data.Texts;
import cz.vse.pyraquest.domain.Player;
import cz.vse.pyraquest.domain.Room;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    private Game createGame() {
        Texts texts = new Texts(Map.of(
                "system.unknown", "Tomu nerozumím.",
                "system.help", "Seznam příkazů",
                "system.quit", "Hra končí."
        ));
        return new Game(() -> {
            Room start = new Room("vstup", "Start", "popis", null);
            return new GameState(new Player(start), Map.of("vstup", start), texts);
        });
    }

    @Test
    void unknownCommandIsRejected() {
        assertEquals("Tomu nerozumím.", createGame().processLine("skoc"));
    }

    @Test
    void blankLineGivesNoOutput() {
        assertEquals("", createGame().processLine("    "));
    }

    @Test
    void commandNameIsCaseAndSpaceInsensitive() {
        assertEquals("Seznam příkazů", createGame().processLine("   POMOC   "));
    }

    @Test
    void quitStopsTheGame() {
        Game game = createGame();
        assertTrue(game.isRunning());
        assertEquals("Hra končí.", game.processLine("konec"));
        assertFalse(game.isRunning());
    }
}