package cz.vse.pyraquest.data;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import cz.vse.pyraquest.domain.Enemy;
import cz.vse.pyraquest.domain.Item;
import cz.vse.pyraquest.domain.Mummy;
import cz.vse.pyraquest.domain.Npc;
import cz.vse.pyraquest.domain.Player;
import cz.vse.pyraquest.domain.Question;
import cz.vse.pyraquest.domain.Riddle;
import cz.vse.pyraquest.domain.Room;
import cz.vse.pyraquest.domain.Skeleton;
import cz.vse.pyraquest.game.GameState;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class WorldLoader {

    private static final Gson GSON = new Gson();
    private static final String START_ROOM = "vstup";

    public static GameState load() {
        return load(System.nanoTime());
    }

    public static GameState load(long riddleSeed) {
        Texts texts = Texts.fromResources(
                "/texts/system.json", "/texts/commands.json", "/texts/npc-dialogue.json");
        Map<String, Room> rooms = loadRooms();
        List<Question> questions = requireList(
                read("/data/questions.json", QuestionsFile.class).questions, "questions");
        Map<String, Npc> npcs = loadNpcs(rooms, questions, riddleSeed);
        loadItems(rooms, npcs);

        Room start = find(rooms, START_ROOM, "start room");
        return new GameState(new Player(start), rooms, texts);
    }

    private static Map<String, Room> loadRooms() {
        List<RoomData> data = requireList(read("/data/rooms.json", RoomsFile.class).rooms, "rooms");
        Map<String, Room> rooms = new LinkedHashMap<>();
        for (RoomData d : data) {
            rooms.put(d.code, new Room(d.code, d.name, d.description, d.entryCondition));
        }
        for (RoomData d : data) {
            for (String exit : requireList(d.exits, d.code + ".exits")) {
                rooms.get(d.code).addExit(find(rooms, exit, "exit of " + d.code));
            }
        }
        return rooms;
    }

    private static Map<String, Npc> loadNpcs(Map<String, Room> rooms, List<Question> questions, long seed) {
        Map<String, Npc> npcs = new LinkedHashMap<>();
        for (NpcData d : requireList(read("/data/npcs.json", NpcsFile.class).npcs, "npcs")) {
            Npc npc = switch (d.type) {
                case "enemy" -> new Enemy(d.code, d.name, d.guardedItem);
                case "skeleton" -> new Skeleton(d.code, d.name);
                case "mummy" -> new Mummy(d.code, d.name, new Riddle(questions, seed));
                default -> throw new IllegalStateException("Unknown npc type: " + d.type);
            };
            npcs.put(npc.getCode(), npc);
            find(rooms, d.room, "room of " + d.code).addNpc(npc);
        }
        return npcs;
    }

    private static void loadItems(Map<String, Room> rooms, Map<String, Npc> npcs) {
        List<ItemData> data = requireList(read("/data/items.json", ItemsFile.class).items, "items");
        Map<String, Item> items = new LinkedHashMap<>();
        for (ItemData d : data) {
            items.put(d.code, new Item(d.code, d.name, d.portable, d.container, d.locked));
        }
        for (ItemData d : data) {
            Item item = items.get(d.code);
            String context = "location of " + d.code;
            switch (d.location.type) {
                case "room" -> find(rooms, d.location.code, context).addItem(item);
                case "item" -> find(items, d.location.code, context).addItem(item);
                case "npc" -> find(npcs, d.location.code, context).addItem(item);
                default -> throw new IllegalStateException("Unknown location type: " + d.location.type);
            }
        }
    }

    private static <T> T read(String path, Class<T> type) {
        try (InputStream in = WorldLoader.class.getResourceAsStream(path)) {
            if (in == null) {
                throw new IllegalStateException("Data file not found: " + path);
            }
            T result = GSON.fromJson(new InputStreamReader(in, StandardCharsets.UTF_8), type);
            if (result == null) {
                throw new IllegalStateException("Data file is empty: " + path);
            }
            return result;
        } catch (IOException | JsonParseException e) {
            throw new IllegalStateException("Cannot load " + path, e);
        }
    }

    private static <T> List<T> requireList(List<T> list, String what) {
        if (list == null) {
            throw new IllegalStateException("Missing list in data: " + what);
        }
        return list;
    }

    private static <T> T find(Map<String, T> map, String code, String context) {
        T value = map.get(code);
        if (value == null) {
            throw new IllegalStateException("Unknown code '" + code + "' (" + context + ")");
        }
        return value;
    }

    private static class RoomsFile { List<RoomData> rooms; }
    private static class RoomData {
        String code;
        String name;
        String description;
        String entryCondition;
        List<String> exits;
    }

    private static class ItemsFile { List<ItemData> items; }
    private static class ItemData {
        String code;
        String name;
        boolean portable;
        boolean container;
        boolean locked;
        Location location;
    }
    private static class Location {
        String type;
        String code;
    }

    private static class NpcsFile { List<NpcData> npcs; }
    private static class NpcData {
        String code;
        String name;
        String type;
        String room;
        String guardedItem;
    }

    private static class QuestionsFile { List<Question> questions; }
}