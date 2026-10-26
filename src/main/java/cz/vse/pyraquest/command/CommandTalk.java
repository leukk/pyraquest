package cz.vse.pyraquest.command;

public class CommandTalk implements ICommand {
    private static String NAME = "mluv";

    public String execute(String... parameter){
        return null;
    }

    public static String getNAME() {
        return NAME;
    }
}
