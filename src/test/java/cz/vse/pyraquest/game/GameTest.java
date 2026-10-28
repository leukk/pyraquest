package cz.vse.pyraquest.game;

import cz.vse.pyraquest.command.ICommand;
import cz.vse.pyraquest.data.Texts;
import cz.vse.pyraquest.domain.Enemy;
import cz.vse.pyraquest.domain.Item;
import cz.vse.pyraquest.domain.Player;
import cz.vse.pyraquest.domain.Room;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    private final Texts texts = new Texts(Map.of(
            "system.unknown", "Tomu nerozumím.",
            "system.help", "Seznam příkazů",
            "system.quit", "Hra končí.",
            "system.lose.enemy.skorpion", "LOSS3",
            "system.lose.fuse", "LOSS5",
            "system.lose.suffix", "SUFFIX",
            "take.error.guarded.pochoden", "GUARD-TAKE",
            "search.error.bat", "GUARD-SEARCH"
    ));

    /** Every call to the factory builds a brand-new world, like WorldLoader.load() will. */
    private Game createGame(Supplier<Room> startRoomFactory) {
        return new Game(() -> {
            Room start = startRoomFactory.get();
            return new GameState(new Player(start), Map.of(start.getCode(), start), texts);
        });
    }

    private Game createGame() {
        return createGame(() -> new Room("vstup", "Start", "popis", null));
    }

    private Room roomWithScorpion() {
        Room den = new Room("doupe", "Doupě", "popis", null);
        den.addNpc(new Enemy("skorpion", "Škorpion", "pochoden"));
        return den;
    }

    private Game gameWithScorpion() {
        return createGame(this::roomWithScorpion);
    }

    private Room roomWithTnt() {
        Room predsin = new Room("predsin", "Předsíň", "popis", null);
        predsin.addExit(new Room("rozcesti-sever", "Severní rozcestí", "popis", null));
        predsin.addItem(new Item("tnt", "TNT", true, false, false));
        return predsin;
    }

    private void lightTnt(GameState state) {
        state.getRoom("predsin").getItem("tnt").setLit(true);
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

    @Test
    void anyMoveExceptAttackKillsYouNextToLivingEnemy() {
        Game game = gameWithScorpion();

        String output = game.processLine("jdi chodba");

        assertTrue(output.startsWith("LOSS3\n\nSUFFIX\n\n"));
    }

    @Test
    void attackIsNotKilledByLoss3() {
        Game game = gameWithScorpion();

        String output = game.processLine("utoc skorpion");

        assertFalse(output.contains("LOSS3"));
    }

    @Test
    void systemCommandIsSafeNextToLivingEnemy() {
        Game game = gameWithScorpion();

        assertEquals("Seznam příkazů", game.processLine("pomoc"));
        assertTrue(game.isRunning());
    }

    @Test
    void unknownCommandIsSafeNextToLivingEnemy() {
        Game game = gameWithScorpion();

        assertEquals("Tomu nerozumím.", game.processLine("skoc"));
        assertTrue(game.isRunning());
    }

    @Test
    void deadEnemyDoesNotKillYou() {
        Game game = createGame(() -> {
            Room den = new Room("doupe", "Doupě", "popis", null);
            Enemy scorpion = new Enemy("skorpion", "Škorpion", "pochoden");
            scorpion.kill();
            den.addNpc(scorpion);
            return den;
        });

        String output = game.processLine("jdi chodba");

        assertFalse(output.contains("LOSS3"));
    }

    @Test
    void takingGuardedItemLosesWithTheCommandsOwnText() {
        Game game = createGame(() -> {
            Room den = roomWithScorpion();
            den.addItem(new Item("pochoden", "Pochodeň", true, false, false));
            return den;
        });

        String output = game.processLine("seber pochoden");

        assertTrue(output.startsWith("GUARD-TAKE\n\nSUFFIX\n\n"));
    }

    @Test
    void takingAnotherItemNextToEnemyStillLosesByGeneralRule() {
        String output = gameWithScorpion().processLine("seber nuz");

        assertTrue(output.startsWith("LOSS3\n\nSUFFIX\n\n"));
    }

    @Test
    void searchingGuardedPileLosesWithTheCommandsOwnText() {
        Game game = createGame(() -> {
            Room ossuary = new Room("kostnice", "Kostnice", "popis", null);
            ossuary.addItem(new Item("hromada-kosti", "Hromada kostí", false, true, false));
            ossuary.addNpc(new Enemy("netopyr", "Netopýr", "hromada-kosti"));
            return ossuary;
        });

        String output = game.processLine("prohledni hromada-kosti");

        assertTrue(output.startsWith("GUARD-SEARCH\n\nSUFFIX\n\n"));
    }

    @Test
    void anyMoveExceptEscapeLosesWithLitFuse() {
        Game game = createGame(this::roomWithTnt);
        lightTnt(game.getState());

        String output = game.processLine("jdi chodba");

        assertTrue(output.startsWith("LOSS5\n\nSUFFIX\n\n"));
    }

    @Test
    void escapingNorthIsSafeWithLitFuse() {
        Game game = createGame(this::roomWithTnt);
        lightTnt(game.getState());

        String output = game.processLine("jdi rozcesti-sever");

        assertFalse(output.contains("LOSS5"));
        assertTrue(game.isRunning());
    }

    @Test
    void jdiWithoutParameterLosesWithLitFuse() {
        Game game = createGame(this::roomWithTnt);
        lightTnt(game.getState());

        assertTrue(game.processLine("jdi").startsWith("LOSS5"));
    }

    @Test
    void systemCommandIsSafeWithLitFuse() {
        Game game = createGame(this::roomWithTnt);
        lightTnt(game.getState());

        assertEquals("Seznam příkazů", game.processLine("pomoc"));
        assertTrue(game.isRunning());
    }

    @Test
    void lossRestartsTheGameAndPrintsStartRoom() {
        Game game = gameWithScorpion();

        String output = game.processLine("jdi chodba");

        assertTrue(output.contains("Doupě"));
        assertTrue(game.isRunning());
        assertEquals(GameState.Status.RUNNING, game.getState().getStatus());
    }

    @Test
    void restartGivesAFreshWorld() {
        Game game = createGame(() -> {
            Room predsin = roomWithTnt();
            predsin.addNpc(new Enemy("skorpion", "Škorpion", "pochoden"));
            return predsin;
        });
        GameState oldState = game.getState();

        ((Enemy) oldState.getPlayer().getCurrentRoom().getNpc("skorpion")).kill();
        oldState.getPlayer().getBackpack().add(new Item("nuz", "Nůž", true, false, false));
        oldState.addWrongAnswer();
        lightTnt(oldState);

        game.processLine("jdi chodba");   // loss 5

        GameState newState = game.getState();
        assertNotSame(oldState, newState);
        assertTrue(newState.getPlayer().getBackpack().isEmpty());
        assertEquals(0, newState.getWrongAnswers());
        assertFalse(newState.isFuseLit());
        Enemy scorpion = (Enemy) newState.getPlayer().getCurrentRoom().getNpc("skorpion");
        assertTrue(scorpion.isAlive());
    }

    @Test
    void commandThatLosesTheGameAlsoRestarts() {
        Game game = createGame();
        game.register(new ICommand() {
            @Override
            public String getName() {
                return "past";
            }

            @Override
            public String execute(GameState state, String... parameters) {
                state.lose();
                return "BOOM";
            }
        });

        String output = game.processLine("past");

        assertTrue(output.startsWith("BOOM\n\nSUFFIX\n\n"));
        assertTrue(game.isRunning());
    }
}