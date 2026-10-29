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

    public void markTalkedTo() {
        talkedTo = true;
    }

    @Override
    public boolean acceptsGift(Item item) {
        return item.getCode().equals("kost");
    }
}