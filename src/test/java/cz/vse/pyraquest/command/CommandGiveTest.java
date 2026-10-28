package cz.vse.pyraquest.command;

import cz.vse.pyraquest.data.WorldLoader;
import cz.vse.pyraquest.domain.Backpack;
import cz.vse.pyraquest.domain.Item;
import cz.vse.pyraquest.domain.Room;
import cz.vse.pyraquest.domain.Skeleton;
import cz.vse.pyraquest.game.GameState;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CommandGiveTest {
    @Test
    void skeletonTradesBoneForKey() {
        GameState state = WorldLoader.load();

        Room room = state.getRoom("sal");
        state.getPlayer().setCurrentRoom(room);

        Skeleton skeleton = (Skeleton) room.getNpc("kostlivec");

        Backpack backpack = state.getPlayer().getBackpack();

        Item bone = new Item("kost", "Kost", true, false, false);
        backpack.add(bone);

        CommandGive command = new CommandGive();

        command.execute(state, "kost", "kostlivec");

        assertTrue(backpack.contains("klic"));
        assertFalse(backpack.contains("kost"));
        assertTrue(skeleton.hasTraded());
    }

    @Test
    void cannotTradeWithoutBone() {
        GameState state = WorldLoader.load();

        Room room = state.getRoom("sal");
        state.getPlayer().setCurrentRoom(room);

        Backpack backpack = state.getPlayer().getBackpack();

        CommandGive command = new CommandGive();

        command.execute(state, "kost", "kostlivec");

        assertFalse(backpack.contains("klic"));
        assertFalse(backpack.contains("kost"));
    }

    @Test
    void cannotTradeWithMummy() {
        GameState state = WorldLoader.load();

        Room room = state.getRoom("komnata");
        state.getPlayer().setCurrentRoom(room);

        Backpack backpack = state.getPlayer().getBackpack();
        backpack.add(new Item("kost", "Kost", true, false, false));
        CommandGive command = new CommandGive();

        command.execute(state, "kost", "mumie");

        assertTrue(backpack.contains("kost"));
        assertFalse(backpack.contains("klic"));
    }
}
