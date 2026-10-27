package cz.vse.pyraquest.domain;

import java.util.ArrayList;
import java.util.List;

public class Item {

    private final String code;
    private final String name;
    private final boolean portable;
    private final boolean container;
    private boolean searched;

    public boolean isSearched() {
        return searched;
    }

    public void setSearched(boolean searched) {
        this.searched = searched;
    }


    private final List<Item> contents = new ArrayList<>();
    private boolean locked;
    private boolean opened;

    public Item(String code, String name, boolean portable, boolean container, boolean locked) {
        this.code = code;
        this.name = name;
        this.portable = portable;
        this.container = container;
        this.locked = locked;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public boolean isPortable() {
        return portable;
    }

    public boolean isContainer() {
        return container;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public boolean isOpened() {
        return opened;
    }

    public void setOpened(boolean opened) {
        this.opened = opened;
    }

    public List<Item> getContents() {
        return contents;
    }

    public void addItem(Item item) {
        contents.add(item);
    }

    public void removeItem(Item item) {
        contents.remove(item);
    }
}