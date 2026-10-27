package cz.vse.pyraquest.command;

public class CommandMove implements ICommand {
    private static final String NAME = "jdi";

    @Override
    public String execute(String... parameters) {
        return null;
    }

    @Override
    public String getName() {
        return NAME;
    }
}
