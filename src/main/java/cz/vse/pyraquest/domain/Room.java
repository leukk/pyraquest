package cz.vse.pyraquest.domain;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Room {

    private final String code;
    private final String name;
    private final String description;
    private final String entryCondition;

    private final Map<String, Room> exits = new LinkedHashMap<>();
    private final List<Item> items = new ArrayList<>();
    private final List<Npc> npcs = new ArrayList<>();

    public Room(String code, String name, String description, String entryCondition) {
        if (code == null || code.isBlank() || name == null || name.isBlank()) {
            throw new IllegalArgumentException("Room needs a code and a name.");
        }
        this.code = code;
        this.name = name;
        this.description = description;
        this.entryCondition = entryCondition;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getEntryCondition() {
        return entryCondition;
    }

    public Map<String, Room> getExits() {
        return exits;
    }

    public void addExit(Room room) {
        exits.put(room.getCode(), room);
    }

    public Room getExit(String code) {
        return exits.get(code);
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

    public List<Npc> getNpcs() {
        return npcs;
    }

    public void addNpc(Npc npc) {
        npcs.add(npc);
    }

    public void removeNpc(Npc npc) {
        npcs.remove(npc);
    }

    public Item getItem(String code) {
        for (Item item : items) {
            if (item.getCode().equals(code)) {
                return item;
            }
        }
        return null;
    }

    public Npc getNpc(String code) {
        for (Npc npc : npcs) {
            if (npc.getCode().equals(code)) {
                return npc;
            }
        }
        return null;
    }

}