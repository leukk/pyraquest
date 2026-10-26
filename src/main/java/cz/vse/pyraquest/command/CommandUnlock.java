package cz.vse.pyraquest.command;

public class CommandUnlock implements ICommand {
    private static final String NAME = "odemkni";

    public String execute(String... parameter){
        return null;
    }

    public static String getNAME() {
        return NAME;
    }
}
