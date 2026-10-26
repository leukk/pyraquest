package cz.vse.pyraquest.command;

public class CommandAttack implements ICommand {
    private static final String NAME = "utoc";

    public String execute(String... parameter){
        return null;
    }

    public static String getNAME() {
        return NAME;
    }
}
