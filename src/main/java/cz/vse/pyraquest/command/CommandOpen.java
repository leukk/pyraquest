package cz.vse.pyraquest.command;

public class CommandOpen implements ICommand {
    private static final String NAME = "otevri";

    public String execute(String... parameter){
        return null;
    }

    public static String getNAME() {
        return NAME;
    }
}
