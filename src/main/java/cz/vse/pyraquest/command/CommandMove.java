package cz.vse.pyraquest.command;

public class CommandMove implements ICommand {
    private static final String NAME = "jdi";

    public String execute(String... parameter){
        return null;
    }

    public static String getNAME() {
        return NAME;
    }
}
