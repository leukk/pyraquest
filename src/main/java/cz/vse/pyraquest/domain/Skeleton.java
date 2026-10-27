package cz.vse.pyraquest.domain;

public class Skeleton extends Npc {

    private boolean talkedTo;
    private boolean traded;

    public Skeleton(String code, String name) {
        super(code, name);
    }

    public boolean hasTalkedTo() {
        return talkedTo;
    }

    public boolean hasTraded() {
        return traded;
    }

    public void markTraded() {
        traded = true;
    }

    @Override
    public String talk() {
        talkedTo = true;
        return traded ? "TODO: text po výměně" : "TODO: text před výměnou";
    }

    @Override
    public boolean acceptsGift(Item item) {
        return item.getCode().equals("kost");
    }
}