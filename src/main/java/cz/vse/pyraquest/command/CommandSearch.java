package cz.vse.pyraquest.command;

public class CommandSearch implements ICommand {
    private static final String NAME = "prohledni";

    @Override
    public String execute(String... parameters) {
        return null;
    }

    @Override
    public String getName() {
        return NAME;
    }
}
