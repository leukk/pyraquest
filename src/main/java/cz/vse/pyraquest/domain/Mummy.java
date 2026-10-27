package cz.vse.pyraquest.domain;

public class Mummy extends Npc {

    private final Riddle riddle;

    public Mummy(String code, String name, Riddle riddle) {
        super(code, name);
        this.riddle = riddle;
    }

    public Riddle getRiddle() {
        return riddle;
    }

    @Override
    public String talk() {
        return "TODO: spustit hádanku nebo zopakovat otázku";
    }
}