package cz.vse.pyraquest.data;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class TextFilesTest {

    private final Texts texts = Texts.fromResources(
            "/texts/system.json", "/texts/commands.json", "/texts/npc-dialogue.json");

    @Test
    void textFilesLoadWithoutDuplicateKeys() {
        assertFalse(texts.get("system.intro").startsWith("[missing"));
    }

    @Test
    void textsWithArgumentsAreFormatted() {
        assertEquals("Sebral jsi: nuz.", texts.get("take.success", "nuz"));
        assertEquals("Otázka 2 ze 3:", texts.get("riddle.question.header", 2));
    }

    @Test
    void enemyKeysMatchNpcCodes() {
        for (String code : new String[]{"skorpion", "netopyr"}) {
            assertFalse(texts.get("system.lose.enemy." + code).startsWith("[missing"));
            assertFalse(texts.get("system.warning." + code).startsWith("[missing"));
        }
    }

    @Test
    void winTextExists() {
        assertFalse(texts.get("system.win").startsWith("[missing"));
    }
}