package cz.vse.pyraquest.command;

public class CommandSearch implements ICommand {
    private static final String NAME = "prohledej";

    public String execute(String... parameter){
        return null;
    }

    public static String getNAME() {
        return NAME;
    }
}
