package cz.vse.pyraquest.command;

import cz.vse.pyraquest.data.SaveFile;
import cz.vse.pyraquest.game.GameState;

import java.io.IOException;

public class CommandSave implements ICommand {
    private static final String NAME = "uloz";

    private final SaveFile saveFile;

    public CommandSave(SaveFile saveFile) {
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
            return state.getTexts().get("save.error.noName");
        }

        String name = parameters[0];
        try {
            saveFile.write(name, new SaveFile.Data(state.getQuestionsSeed(), state.getMoves()));
        } catch (IOException e) {
            return state.getTexts().get("save.error.write");
        }
        return state.getTexts().get("save.success", name);
    }
}
