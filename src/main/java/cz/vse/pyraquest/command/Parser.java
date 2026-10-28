package cz.vse.pyraquest.command;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Parser {

    public static class ParsedInput {
        private final String commandName;
        private final List<String> parameters;

        public ParsedInput(String commandName, List<String> parameters) {
            this.commandName = commandName;
            this.parameters = parameters;
        }

        public String getCommandName() {
            return commandName;
        }

        public List<String> getParameters() {
            return parameters;
        }
    }

    public ParsedInput parse(String line) {
        if (line == null) {
            return null;
        }

        String[] words = line.toLowerCase(Locale.ROOT).split("\\s+");

        List<String> parsedWords = new ArrayList<>();
        for (String word : words) {
            if (!word.isEmpty()) {
                parsedWords.add(word);
            }
        }

        if (parsedWords.isEmpty()) {
            return null;
        }

        String name = parsedWords.get(0);
        List<String> parameters = parsedWords.subList(1, parsedWords.size());
        return new ParsedInput(name, List.copyOf(parameters));
    }
}