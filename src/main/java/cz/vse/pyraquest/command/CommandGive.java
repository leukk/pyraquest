package cz.vse.pyraquest.command;

public class CommandGive implements ICommand {
    private static final String NAME = "dej";

    @Override
    public String execute(String... parameters) {
        return null;
    }

    @Override
    public String getName() {
        return NAME;
    }
}
