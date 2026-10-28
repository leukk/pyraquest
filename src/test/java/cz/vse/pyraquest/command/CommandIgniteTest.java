package cz.vse.pyraquest.command;

import cz.vse.pyraquest.data.WorldLoader;
import cz.vse.pyraquest.domain.Backpack;
import cz.vse.pyraquest.domain.Item;
import cz.vse.pyraquest.domain.Room;
import cz.vse.pyraquest.game.GameState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CommandIgniteTest {
    GameState state;
    Room room;
    Backpack backpack;
    ICommand ignite;
    ICommand move;
    Item tnt;
    Item torch;

    @BeforeEach
    void setUp() {
        state = WorldLoader.load();
        room = state.getRoom("predsin");
        state.getPlayer().setCurrentRoom(room);
        backpack = state.getPlayer().getBackpack();
        backpack.add(new Item("kresadlo", "Kresadlo", true, false, false, false));
        ignite = new CommandIgnite();
        move = new CommandMove();
        tnt = new Item("tnt", "TNT", true, false, false, true);
        torch = new Item("pochoden", "Pochoden", true, false, false, true);
    }

    @Test
    void igniteTntInBackpackFails() {
        backpack.add(tnt);
        ignite.execute(state, "tnt");
        assertFalse(tnt.isLit());
        assertFalse(state.isFuseLit());
    }

    @Test
    void ignitePlacedTntLightsFuse() {
        room.addItem(tnt);
        ignite.execute(state, "tnt");
        assertTrue(tnt.isLit());
        assertTrue(state.isFuseLit());
    }

    @Test
    void igniteTorchInBackpack() {
        backpack.add(torch);
        ignite.execute(state, "pochoden");
        assertTrue(torch.isLit());
    }

    @Test
    void igniteTorchNotInBackpackFails() {
        room.addItem(torch);
        ignite.execute(state, "pochoden");
        assertFalse(state.getRoom("doupe").getItem("pochoden").isLit());
    }

    @Test
    void igniteNonIgnitableFails() {
        ignite.execute(state, "zed");
        assertFalse(room.getItem("zed").isLit());
    }

    @Test
    void tntExplosionDestroysWall() {
        room.addItem(tnt);
        ignite.execute(state, "tnt");
        move.execute(state, "rozcesti-sever");
        assertFalse(state.isFuseLit());
        assertNull(room.getItem("zed"));
    }
}
