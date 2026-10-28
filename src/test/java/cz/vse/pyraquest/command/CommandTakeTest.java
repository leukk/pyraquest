package cz.vse.pyraquest.command;

import cz.vse.pyraquest.data.WorldLoader;
import cz.vse.pyraquest.domain.Backpack;
import cz.vse.pyraquest.domain.Item;
import cz.vse.pyraquest.domain.Room;
import cz.vse.pyraquest.game.GameState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CommandTakeTest {
    GameState state;
    Room room;
    Backpack backpack;
    ICommand command;

    @BeforeEach
    void setUp() {
        state = WorldLoader.load();
        room = state.getRoom("pokladnice");
        state.getPlayer().setCurrentRoom(room);
        backpack = state.getPlayer().getBackpack();
        command = new CommandTake();
    }

    @Test
    void takeItemFromRoom() {
        command.execute(state, "kresadlo");
        assertTrue(backpack.contains("kresadlo"));
        assertNull(room.getItem("kresadlo"));
    }

    @Test
    void takeItemNotInRoomFails() {
        command.execute(state, "nuz");
        assertFalse(backpack.contains("nuz"));
    }

    @Test
    void takeNonPortableItemFails() {
        room.addItem(new Item("truhla", "Truhla", false, true, false, false));
        command.execute(state, "truhla");
        assertFalse(backpack.contains("truhla"));
        assertNotNull(room.getItem("truhla"));
    }

    @Test
    void takeWithFullBackpackFails() {
        backpack.add(new Item("nuz", "Nuz", true, false, false, false));
        command.execute(state, "kresadlo");
        command.execute(state, "zlata-cihla");
        assertTrue(backpack.isFull());
        assertFalse(backpack.contains("zlata-cihla"));
        assertNotNull(room.getItem("zlata-cihla"));
    }
}
