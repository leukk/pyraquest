package cz.vse.pyraquest.command;

import cz.vse.pyraquest.data.WorldLoader;
import cz.vse.pyraquest.domain.Backpack;
import cz.vse.pyraquest.domain.Item;
import cz.vse.pyraquest.domain.Room;
import cz.vse.pyraquest.game.GameState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CommandDropTest {
    GameState state;
    Room room;
    Backpack backpack;
    ICommand command;
    Item knife;

    @BeforeEach
    void setUp() {
        state = WorldLoader.load();
        room = state.getRoom("sal");
        state.getPlayer().setCurrentRoom(room);
        backpack = state.getPlayer().getBackpack();
        command = new CommandDrop();
        knife = new Item("nuz", "Nuz", true, false, false, false);
        backpack.add(knife);
    }

    @Test
    void dropItemFromBackpack() {
        command.execute(state, "nuz");
        assertFalse(backpack.contains("nuz"));
        assertEquals(knife, room.getItem("nuz"));
    }

    @Test
    void dropItemNotInBackpackFails() {
        command.execute(state, "kresadlo");
        assertTrue(backpack.contains("nuz"));
        assertNull(room.getItem("kresadlo"));
    }
}
