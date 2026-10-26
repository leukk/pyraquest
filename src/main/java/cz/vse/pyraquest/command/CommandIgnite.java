package cz.vse.pyraquest.command;

public class CommandIgnite implements ICommand {
    private static final String NAME = "zapal";

    public String execute(String... parameter){
        return null;
    }

    public static String getNAME() {
        return NAME;
    }
}
