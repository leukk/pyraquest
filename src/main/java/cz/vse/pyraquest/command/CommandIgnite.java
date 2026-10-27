package cz.vse.pyraquest.command;

public class CommandIgnite implements ICommand {
    private static final String NAME = "zapal";

    @Override
    public String execute(String... parameters) {
        return null;
    }

    @Override
    public String getName() {
        return NAME;
    }
}
