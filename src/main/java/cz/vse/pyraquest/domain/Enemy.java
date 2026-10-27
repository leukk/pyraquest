package cz.vse.pyraquest.domain;

public class Enemy extends Npc {

    private final String guardedItemCode;
    private boolean alive = true;

    public Enemy(String code, String name, String guardedItemCode) {
        super(code, name);
        this.guardedItemCode = guardedItemCode;
    }

    public String getGuardedItemCode() {
        return guardedItemCode;
    }

    public boolean isAlive() {
        return alive;
    }

    public void kill() {
        alive = false;
    }

    @Override
    public String talk() {
        return "Tohle ti neodpoví.";
    }
}