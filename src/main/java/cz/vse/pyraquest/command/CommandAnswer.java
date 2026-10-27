package cz.vse.pyraquest.command;

public class CommandAnswer implements ICommand {
    private static final String NAME = "odpovez";

    @Override
    public String execute(String... parameters) {
        return null;
    }

    @Override
    public String getName() {
        return NAME;
    }
}
