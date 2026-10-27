package cz.vse.pyraquest.command;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ParserTest {

    private final Parser parser = new Parser();

    @Test
    void splitsNameAndParameters() {
        Parser.ParsedInput result = parser.parse("dej kost kostlivec");
        assertEquals("dej", result.getCommandName());
        assertEquals(List.of("kost", "kostlivec"), result.getParameters());
    }

    @Test
    void commandWithoutParametersHasEmptyList() {
        Parser.ParsedInput result = parser.parse("pomoc");
        assertEquals("pomoc", result.getCommandName());
        assertTrue(result.getParameters().isEmpty());
    }

    @Test
    void convertsToLowercase() {
        Parser.ParsedInput result = parser.parse("JDI Chodba");
        assertEquals("jdi", result.getCommandName());
        assertEquals(List.of("chodba"), result.getParameters());
    }

    @Test
    void ignoresExtraSpaces() {
        Parser.ParsedInput result = parser.parse("   jdi     chodba   ");
        assertEquals("jdi", result.getCommandName());
        assertEquals(List.of("chodba"), result.getParameters());
    }

    @Test
    void blankLineGivesNull() {
        assertNull(parser.parse(""));
        assertNull(parser.parse("     "));
    }

    @Test
    void nullGivesNull() {
        assertNull(parser.parse(null));
    }
}