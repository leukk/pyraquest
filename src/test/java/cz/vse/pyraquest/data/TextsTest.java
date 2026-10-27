package cz.vse.pyraquest.data;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TextsTest {

    @Test
    void loadsTextsFromJson() {
        Texts texts = Texts.fromJson("{\"attack.scorpion.win\": \"Jsi rychlejší.\"}");
        assertEquals("Jsi rychlejší.", texts.get("attack.scorpion.win"));
    }

    @Test
    void missingKeyIsVisible() {
        Texts texts = Texts.fromJson("{}");
        assertEquals("[missing text: x.y]", texts.get("x.y"));
    }

    @Test
    void invalidJsonThrows() {
        assertThrows(IllegalStateException.class, () -> Texts.fromJson("{ not json"));
    }

    @Test
    void missingResourceThrows() {
        assertThrows(IllegalStateException.class, () -> Texts.fromResources("/texts/neexistuje.json"));
    }
}