package cz.vse.pyraquest.game;

import cz.vse.pyraquest.data.Texts;
import cz.vse.pyraquest.domain.Player;
import cz.vse.pyraquest.domain.Room;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GameStateTest {

    private GameState createState() {
        Room start = new Room("vstup", "Start", "popis", null);
        return new GameState(new Player(start), Map.of("vstup", start), new Texts(Map.of()));
    }

    @Test
    void newGameIsRunning() {
        assertTrue(createState().isRunning());
    }

    @Test
    void loseChangesStatus() {
        GameState state = createState();
        state.lose();
        assertEquals(GameState.Status.LOST, state.getStatus());
    }

    @Test
    void missingTextKeyIsVisible() {
        assertEquals("[missing text: x.y]", createState().getTexts().get("x.y"));
    }
}