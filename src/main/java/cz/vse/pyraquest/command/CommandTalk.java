package cz.vse.pyraquest.command;

public class CommandTalk implements ICommand {
    private static final String NAME = "mluv";

    @Override
    public String execute(String... parameters) {
        return null;
    }

    @Override
    public String getName() {
        return NAME;
    }
}
