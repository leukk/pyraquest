package cz.vse.pyraquest.command;

public class CommandAnswer implements ICommand {
    private static final String NAME = "odpovez";

    public String execute(String... parameter){
        return null;
    }

    public static String getNAME() {
        return NAME;
    }
}
