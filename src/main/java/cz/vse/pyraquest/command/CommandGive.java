package cz.vse.pyraquest.command;

public class CommandGive implements ICommand {
    private static final String NAME = "dej";

    public String execute(String... parameter){
        return null;
    }

    public static String getNAME() {
        return NAME;
    }
}
