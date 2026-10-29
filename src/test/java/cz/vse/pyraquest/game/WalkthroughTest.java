package cz.vse.pyraquest.game;

import cz.vse.pyraquest.data.WorldLoader;
import cz.vse.pyraquest.domain.Enemy;
import cz.vse.pyraquest.domain.Item;
import cz.vse.pyraquest.domain.Mummy;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class WalkthroughTest {

    private static final long SEED = 1L;

    private final Game game = new Game(seed -> WorldLoader.load(SEED));
    private final GameState state = game.getState();

    private void step(String command, String expectedRoom, String... expectedBackpack) {
        game.processLine(command);
        String where = "after '" + command + "'";

        assertSame(state, game.getState(), where + ": the game was lost and restarted");
        assertEquals(expectedRoom, state.getPlayer().getCurrentRoom().getCode(), where + ": room");
        assertEquals(Set.of(expectedBackpack), backpackCodes(), where + ": backpack");
    }

    private Set<String> backpackCodes() {
        return state.getPlayer().getBackpack().getItems().stream()
                .map(Item::getCode)
                .collect(Collectors.toSet());
    }

    @Test
    void gddWinningWalkthroughReachesVictory() {
        // knife and first talk with the skeleton
        step("jdi chodba", "chodba");
        step("seber nuz", "chodba", "nuz");
        step("jdi sal", "sal", "nuz");
        step("mluv kostlivec", "sal", "nuz");

        // flint from the treasury
        step("jdi rozcesti-zapad", "rozcesti-zapad", "nuz");
        step("jdi pokladnice", "pokladnice", "nuz");
        step("seber kresadlo", "pokladnice", "nuz", "kresadlo");
        step("jdi rozcesti-zapad", "rozcesti-zapad", "nuz", "kresadlo");
        step("jdi sal", "sal", "nuz", "kresadlo");

        // scorpion and torch
        step("jdi rozcesti-vychod", "rozcesti-vychod", "nuz", "kresadlo");
        step("jdi doupe", "doupe", "nuz", "kresadlo");
        step("utoc skorpion", "doupe", "nuz", "kresadlo");
        assertFalse(((Enemy) state.getRoom("doupe").getNpc("skorpion")).isAlive(), "scorpion should be dead");
        step("poloz nuz", "doupe", "kresadlo");
        step("seber pochoden", "doupe", "kresadlo", "pochoden");
        step("zapal pochoden", "doupe", "kresadlo", "pochoden");
        assertTrue(state.getPlayer().getBackpack().get("pochoden").isLit(), "torch should be burning");
        step("poloz kresadlo", "doupe", "pochoden");
        step("seber nuz", "doupe", "pochoden", "nuz");

        // ossuary: bat and bone
        step("jdi rozcesti-vychod", "rozcesti-vychod", "pochoden", "nuz");
        step("jdi sal", "sal", "pochoden", "nuz");
        step("jdi rozcesti-zapad", "rozcesti-zapad", "pochoden", "nuz");
        step("jdi kostnice", "kostnice", "pochoden", "nuz");
        step("utoc netopyr", "kostnice", "pochoden", "nuz");
        step("poloz pochoden", "kostnice", "nuz");
        step("prohledni hromada-kosti", "kostnice", "nuz");
        step("seber kost", "kostnice", "nuz", "kost");

        // trade the bone for the key
        step("jdi rozcesti-zapad", "rozcesti-zapad", "nuz", "kost");
        step("jdi sal", "sal", "nuz", "kost");
        step("dej kost kostlivec", "sal", "nuz", "klic");
        step("poloz nuz", "sal", "klic");

        // fetch the flint again
        step("jdi rozcesti-vychod", "rozcesti-vychod", "klic");
        step("jdi doupe", "doupe", "klic");
        step("seber kresadlo", "doupe", "klic", "kresadlo");
        step("jdi rozcesti-vychod", "rozcesti-vychod", "klic", "kresadlo");
        step("jdi sal", "sal", "klic", "kresadlo");

        // mummy's riddle
        step("jdi rozcesti-sever", "rozcesti-sever", "klic", "kresadlo");
        step("jdi komnata", "komnata", "klic", "kresadlo");
        step("mluv mumie", "komnata", "klic", "kresadlo");
        Mummy mummy = (Mummy) state.getRoom("komnata").getNpc("mumie");
        assertTrue(mummy.getRiddle().isStarted(), "riddle should have started");
        for (int i = 0; i < 3; i++) {
            int correct = mummy.getRiddle().getCurrentQuestion().getCorrect();
            step("odpovez " + correct, "komnata", "klic", "kresadlo");
        }
        assertTrue(mummy.getRiddle().isSolved(), "riddle should be solved");

        // chest and TNT
        step("odemkni truhla", "komnata", "klic", "kresadlo");
        step("otevri truhla", "komnata", "klic", "kresadlo");
        step("poloz klic", "komnata", "kresadlo");
        step("seber tnt", "komnata", "kresadlo", "tnt");

        // blow up the wall
        step("jdi rozcesti-sever", "rozcesti-sever", "kresadlo", "tnt");
        step("jdi predsin", "predsin", "kresadlo", "tnt");
        step("poloz tnt", "predsin", "kresadlo");
        step("zapal tnt", "predsin", "kresadlo");
        assertTrue(state.isFuseLit(), "fuse should be burning");
        step("jdi rozcesti-sever", "rozcesti-sever", "kresadlo");
        assertNull(state.getRoom("predsin").getItem("zed"), "wall should be destroyed");

        // the mask
        step("jdi predsin", "predsin", "kresadlo");
        step("jdi hrobka", "hrobka", "kresadlo");
        step("otevri sarkofag", "hrobka", "kresadlo");
        step("seber maska", "hrobka", "kresadlo", "maska");

        // back to the entrance
        step("jdi predsin", "predsin", "kresadlo", "maska");
        step("jdi rozcesti-sever", "rozcesti-sever", "kresadlo", "maska");
        step("jdi sal", "sal", "kresadlo", "maska");
        step("jdi chodba", "chodba", "kresadlo", "maska");
        step("jdi vstup", "vstup", "kresadlo", "maska");

        assertEquals(GameState.Status.WON, state.getStatus());
        assertFalse(game.isRunning());
    }

    @Test
    void gddLosingWalkthroughRestartsTheGame() {
        game.processLine("jdi chodba");
        game.processLine("jdi sal");
        game.processLine("jdi rozcesti-vychod");
        game.processLine("jdi doupe");

        String output = game.processLine("utoc skorpion");

        assertTrue(output.contains(state.getTexts().get("system.lose.suffix")));
        assertNotSame(state, game.getState());
        assertTrue(game.isRunning());
        assertEquals("vstup", game.getState().getPlayer().getCurrentRoom().getCode());
        assertTrue(game.getState().getPlayer().getBackpack().isEmpty());
    }
}