package cz.vse.pyraquest.command;

import cz.vse.pyraquest.data.WorldLoader;
import cz.vse.pyraquest.domain.Backpack;
import cz.vse.pyraquest.domain.Item;
import cz.vse.pyraquest.domain.Room;
import cz.vse.pyraquest.game.GameState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CommandMoveTest {
    GameState state;
    Room room;
    Backpack backpack;
    ICommand command;
    Item torch;

    @BeforeEach
    void setUp() {
        state = WorldLoader.load();
        room = state.getRoom("rozcesti-zapad");
        state.getPlayer().setCurrentRoom(room);
        backpack = state.getPlayer().getBackpack();
        command = new CommandMove();
        torch = new Item("pochoden", "Pochoden", true, false, false, true);
    }

    @Test
    void moveToAdjacentRoom() {
        command.execute(state, "pokladnice");
        assertEquals(state.getRoom("pokladnice"), state.getPlayer().getCurrentRoom());
    }

    @Test
    void moveToNonAdjacentRoomFails() {
        command.execute(state, "hrobka");
        assertEquals(room, state.getPlayer().getCurrentRoom());
    }

    @Test
    void moveConditionDarknessWithoutTorchFails() {
        command.execute(state, "kostnice");
        assertEquals(room, state.getPlayer().getCurrentRoom());
    }

    @Test
    void moveConditionDarknessWithUnlitTorchFails() {
        backpack.add(torch);
        command.execute(state, "kostnice");
        assertEquals(room, state.getPlayer().getCurrentRoom());
    }

    @Test
    void moveConditionDarknessWithLitTorch() {
        torch.setLit(true);
        backpack.add(torch);
        command.execute(state, "kostnice");
        assertEquals(state.getRoom("kostnice"), state.getPlayer().getCurrentRoom());
    }

    @Test
    void moveToTombThroughWallFails() {
        Room predsin = state.getRoom("predsin");
        state.getPlayer().setCurrentRoom(predsin);
        command.execute(state, "hrobka");
        assertEquals(predsin, state.getPlayer().getCurrentRoom());
    }

    @Test
    void moveToEntranceWithMaskWins() {
        state.getPlayer().setCurrentRoom(state.getRoom("chodba"));
        backpack.add(new Item("maska", "Maska", true, false, false, false));
        command.execute(state, "vstup");
        assertEquals(GameState.Status.WON, state.getStatus());
    }
}
