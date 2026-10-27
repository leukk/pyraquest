package cz.vse.pyraquest.command;

public class CommandUnlock implements ICommand {
    private static final String NAME = "odemkni";

    @Override
    public String execute(String... parameters) {
        return null;
    }

    @Override
    public String getName() {
        return NAME;
    }
}
