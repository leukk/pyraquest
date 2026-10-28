package cz.vse.pyraquest.command;

import com.google.gson.JsonParseException;
import cz.vse.pyraquest.data.SaveFile;
import cz.vse.pyraquest.game.Game;
import cz.vse.pyraquest.game.GameState;
import cz.vse.pyraquest.game.RoomPrinter;

import java.io.IOException;
import java.nio.file.NoSuchFileException;

public class CommandLoad implements ICommand {
    private static final String NAME = "nacti";

    private final Game game;
    private final SaveFile saveFile;

    public CommandLoad(Game game, SaveFile saveFile) {
        this.game = game;
        this.saveFile = saveFile;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public boolean isSystem() {
        return true;
    }

    @Override
    public String execute(GameState state, String... parameters) {
        if (parameters.length == 0) {
            return state.getTexts().get("load.error.noName");
        }

        String name = parameters[0];
        String corrupt = state.getTexts().get("load.error.corrupt", name);

        SaveFile.Data data;
        try {
            data = saveFile.read(name);
        } catch (NoSuchFileException e) {
            return state.getTexts().get("load.error.notFound", name);
        } catch (IOException | JsonParseException e) {
            return corrupt;
        }

        // prehrani tahu
        Game replay = game.newGame(data.seed());
        String output = RoomPrinter.describe(replay.getState());
        for (String move : data.moves()) {
            output = replay.processLine(move, true);
        }

        if (!replay.isRunning() || !replay.getState().getMoves().equals(data.moves())) {
            return corrupt;
        }

        game.setState(replay.getState());
        return output;
    }
}
