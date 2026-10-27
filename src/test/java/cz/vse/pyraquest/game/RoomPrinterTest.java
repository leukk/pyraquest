package cz.vse.pyraquest.game;

import cz.vse.pyraquest.data.Texts;
import cz.vse.pyraquest.domain.Enemy;
import cz.vse.pyraquest.domain.Item;
import cz.vse.pyraquest.domain.Player;
import cz.vse.pyraquest.domain.Room;
import cz.vse.pyraquest.domain.Skeleton;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class RoomPrinterTest {

    private final Texts texts = new Texts(Map.of(
            "system.room.exits", "Východy:",
            "system.room.items", "Věci:",
            "system.room.npcs", "Postavy:",
            "system.room.backpack", "Batoh",
            "system.warning.skorpion", "Pozor, škorpion!"
    ));

    private GameState stateIn(Room room) {
        return new GameState(new Player(room), Map.of(room.getCode(), room), texts);
    }

    @Test
    void printsRoomLikeTheGdd() {
        Room hall = new Room("sal", "Sloupová síň", "Obrovská síň.", null);
        hall.addExit(new Room("chodba", "Chodba", "popis", null));
        hall.addNpc(new Skeleton("kostlivec", "Kostlivec"));
        GameState state = stateIn(hall);
        state.getPlayer().getBackpack().add(new Item("nuz", "Nůž", true, false, false));

        String expected = "Sloupová síň\n\n"
                + "Obrovská síň.\n\n"
                + "Východy: chodba\n\n"
                + "Věci: -\n\n"
                + "Postavy: kostlivec\n\n"
                + "Batoh (1/2): nuz";
        assertEquals(expected, RoomPrinter.describe(state));
    }

    @Test
    void livingEnemyAddsWarning() {
        Room den = new Room("doupe", "Doupě", "popis", null);
        den.addNpc(new Enemy("skorpion", "Škorpion", "pochoden"));

        assertTrue(RoomPrinter.describe(stateIn(den)).endsWith("Pozor, škorpion!"));
    }

    @Test
    void deadEnemyIsHiddenAndHasNoWarning() {
        Room den = new Room("doupe", "Doupě", "popis", null);
        Enemy scorpion = new Enemy("skorpion", "Škorpion", "pochoden");
        scorpion.kill();
        den.addNpc(scorpion);

        String output = RoomPrinter.describe(stateIn(den));
        assertTrue(output.contains("Postavy: -"));
        assertFalse(output.contains("Pozor"));
    }

    @Test
    void openedContainerShowsItsContents() {
        Room chamber = new Room("komnata", "Komnata", "popis", null);
        Item chest = new Item("truhla", "Truhla", false, true, false);
        chest.addItem(new Item("tnt", "TNT", true, false, false));
        chamber.addItem(chest);
        GameState state = stateIn(chamber);

        assertTrue(RoomPrinter.describe(state).contains("Věci: truhla\n"));

        chest.setOpened(true);
        assertTrue(RoomPrinter.describe(state).contains("Věci: truhla, tnt\n"));
    }
}