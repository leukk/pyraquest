package cz.vse.pyraquest.domain;

import java.util.ArrayList;
import java.util.List;

public class Npc {

    public enum Type {
        ENEMY,
        FRIENDLY
    }

    private final String code;
    private final String name;
    private final Type type;

    private final List<Item> items = new ArrayList<>();
    private boolean alive = true;

    public Npc(String code, String name, Type type) {
        if (code == null || code.isBlank() || name == null || name.isBlank()) {
            throw new IllegalArgumentException("Npc needs a code and a name.");
        }
        if (type == null) {
            throw new IllegalArgumentException(code + ": Npc needs a type.");
        }
        this.code = code;
        this.name = name;
        this.type = type;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public Type getType() {
        return type;
    }

    public boolean isEnemy() {
        return type == Type.ENEMY;
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public List<Item> getItems() {
        return items;
    }

    public void addItem(Item item) {
        items.add(item);
    }

    public void removeItem(Item item) {
        items.remove(item);
    }
}