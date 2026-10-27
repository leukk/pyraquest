package cz.vse.pyraquest.domain;

public class Mummy extends Npc {

    private final Riddle riddle = new Riddle();

    public Mummy(String code, String name) {
        super(code, name);
    }

    public Riddle getRiddle() {
        return riddle;
    }

    @Override
    public String talk() {
        return "TODO: spustit hádanku nebo zopakovat otázku";
    }
}