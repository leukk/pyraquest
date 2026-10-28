package cz.vse.pyraquest.game;

import cz.vse.pyraquest.command.*;
import cz.vse.pyraquest.domain.Enemy;
import cz.vse.pyraquest.domain.Npc;
import cz.vse.pyraquest.domain.Room;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Supplier;

public class Game {
    private final Supplier<GameState> worldFactory;
    private final Parser parser = new Parser();
    private final Map<String, ICommand> commands = new HashMap<>();
    private GameState state;

    private static final String ATTACK = "utoc";
    private static final String MOVE = "jdi";
    private static final String FUSE_ESCAPE_ROOM = "rozcesti-sever";

    public GameState getState() {
        return state;
    }

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
        register(new CommandTake());
        register(new CommandDrop());
    }

    void register(ICommand command) {
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

        String output;
        String lossText = checkLossRules(command, input);
        if (lossText != null) {
            state.lose();
            output = lossText;
        } else {
            String[] parameters = input.getParameters().toArray(new String[0]);
            output = command.execute(state, parameters);
            if (output == null) {
                output = "";
            }
        }

        if (state.getStatus() == GameState.Status.LOST) {
            return restart(output);
        }
        return output;
    }

    //restart
    private String restart(String lossText) {
        state = worldFactory.get();
        return lossText + "\n\n"
                + state.getTexts().get("system.lose.suffix") + "\n\n"
                + RoomPrinter.describe(state);
    }

    //prohry
    private String checkLossRules(ICommand command, Parser.ParsedInput input) {
        if (command.isSystem()) {
            return null;
        }

        //prohra 3
        if (!input.getCommandName().equals(ATTACK)) {
            Room room = state.getPlayer().getCurrentRoom();
            for (Npc npc : room.getNpcs()) {
                if (npc instanceof Enemy enemy && enemy.isAlive()) {
                    return state.getTexts().get("system.lose.enemy." + enemy.getCode());
                }
            }
        }

        //prohra 5
        if (state.isFuseLit() && !isFuseEscape(input)) {
            return state.getTexts().get("system.lose.fuse");
        }

        return null;
    }

    private boolean isFuseEscape(Parser.ParsedInput input) {
        return input.getCommandName().equals(MOVE)
                && !input.getParameters().isEmpty()
                && input.getParameters().get(0).equals(FUSE_ESCAPE_ROOM);
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
