package cz.vse.pyraquest.command;

public class CommandAttack implements ICommand {
    private static final String NAME = "utoc";

    @Override
    public String execute(String... parameters) {
        return null;
    }

    @Override
    public String getName() {
        return NAME;
    }
}
