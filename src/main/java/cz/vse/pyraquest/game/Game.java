package cz.vse.pyraquest.game;

import cz.vse.pyraquest.command.CommandAnswer;
import cz.vse.pyraquest.command.CommandAttack;
import cz.vse.pyraquest.command.CommandGive;
import cz.vse.pyraquest.command.CommandHelp;
import cz.vse.pyraquest.command.CommandHint;
import cz.vse.pyraquest.command.CommandIgnite;
import cz.vse.pyraquest.command.CommandMove;
import cz.vse.pyraquest.command.CommandOpen;
import cz.vse.pyraquest.command.CommandQuit;
import cz.vse.pyraquest.command.CommandSearch;
import cz.vse.pyraquest.command.CommandTalk;
import cz.vse.pyraquest.command.CommandUnlock;
import cz.vse.pyraquest.command.ICommand;
import cz.vse.pyraquest.command.Parser;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Supplier;

public class Game {

    private final Supplier<GameState> worldFactory;
    private final Parser parser = new Parser();
    private final Map<String, ICommand> commands = new HashMap<>();
    private GameState state;

    public Game(Supplier<GameState> worldFactory) {
        this.worldFactory = worldFactory;
        this.state = worldFactory.get();

        register(new CommandHelp());
        register(new CommandHint());
        register(new CommandQuit());

        register(new CommandMove());
        register(new CommandAttack());
        register(new CommandGive());
        register(new CommandIgnite());
        register(new CommandOpen());
        register(new CommandSearch());
        register(new CommandTalk());
        register(new CommandUnlock());
        register(new CommandAnswer());
        // jeste seber a poloz
    }

    private void register(ICommand command) {
        commands.put(command.getName(), command);
    }

    public boolean isRunning() {
        return state.isRunning();
    }

    //intro
    public String start() {
        return state.getTexts().get("system.intro") + "\n\n" + RoomPrinter.describe(state);
    }

    public String processLine(String line) {
        Parser.ParsedInput input = parser.parse(line);
        if (input == null) {
            return "";
        }

        ICommand command = commands.get(input.getCommandName());
        if (command == null) {
            return state.getTexts().get("system.unknown");
        }

        String[] parameters = input.getParameters().toArray(new String[0]);
        String output = command.execute(state, parameters);
        return output == null ? "" : output;
    }

    public void run() {
        System.out.println(start());

        Scanner scanner = new Scanner(System.in);
        while (isRunning() && scanner.hasNextLine()) {
            String output = processLine(scanner.nextLine());
            if (!output.isEmpty()) {
                System.out.println();
                System.out.println(output);
            }
        }
    }
}