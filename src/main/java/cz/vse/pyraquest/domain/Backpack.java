package cz.vse.pyraquest.domain;

import java.util.ArrayList;
import java.util.List;

public class Backpack {

    public static final int CAPACITY = 2;

    private final List<Item> items = new ArrayList<>();

    public List<Item> getItems() {
        return items;
    }

    public boolean isFull() {
        return items.size() >= CAPACITY;
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public boolean contains(String code) {
        for (Item item : items) {
            if (item.getCode().equals(code)) {
                return true;
            }
        }
        return false;
    }

    public Item get(String code) {
        for (Item item : items) {
            if (item.getCode().equals(code)) {
                return item;
            }
        }
        return null;
    }

    public boolean add(Item item) {
        if (isFull())
            return false;

        items.add(item);
        return true;
    }

    public void remove(Item item) {
        items.remove(item);
    }
}
