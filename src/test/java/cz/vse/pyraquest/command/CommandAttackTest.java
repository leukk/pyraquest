package cz.vse.pyraquest.command;

import cz.vse.pyraquest.data.WorldLoader;
import cz.vse.pyraquest.domain.Enemy;
import cz.vse.pyraquest.domain.Item;
import cz.vse.pyraquest.domain.Room;
import cz.vse.pyraquest.game.GameState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CommandAttackTest {
    GameState state;
    Room room;
    Enemy enemy;
    ICommand command;
    Item knife;


    @BeforeEach
    void setUp() {
        state = WorldLoader.load();
        room = state.getRoom("doupe");
        state.getPlayer().setCurrentRoom(room);
        enemy = (Enemy) room.getNpc("skorpion");
        command = new CommandAttack();
        knife = new Item("nuz", "Nuz", true, false, false, false);
    }

    @Test
    void attackWithoutKnifeFails() {
        command.execute(state, "skorpion");
        assertEquals(GameState.Status.LOST, state.getStatus());
        assertTrue(enemy.isAlive());
    }

    @Test
    void attackWithKnife() {
        state.getPlayer().getBackpack().add(knife);
        command.execute(state, "skorpion");
        assertEquals(GameState.Status.RUNNING, state.getStatus());
        assertFalse(enemy.isAlive());
    }
}
