package cz.vse.pyraquest.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class Npc {
    private final String code;
    private final String name;

    private final List<Item> items = new ArrayList<>();

    public Npc(String code, String name) {
        if (code == null || code.isBlank() || name == null || name.isBlank()) {
            throw new IllegalArgumentException("Npc needs a code and a name.");
        }
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public List<Item> getItems() {
        return Collections.unmodifiableList(items);
    }

    public void addItem(Item item) {
        items.add(item);
    }

    public void removeItem(Item item) {
        items.remove(item);
    }


    public boolean acceptsGift(Item item) {
        return false;
    }
}