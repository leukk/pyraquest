package cz.vse.pyraquest.command;

public class CommandOpen implements ICommand {
    private static final String NAME = "otevri";

    @Override
    public String execute(String... parameters) {
        return null;
    }

    @Override
    public String getName() {
        return NAME;
    }
}
