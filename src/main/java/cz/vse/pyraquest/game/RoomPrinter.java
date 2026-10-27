package cz.vse.pyraquest.game;

import cz.vse.pyraquest.data.Texts;
import cz.vse.pyraquest.domain.Backpack;
import cz.vse.pyraquest.domain.Enemy;
import cz.vse.pyraquest.domain.Item;
import cz.vse.pyraquest.domain.Npc;
import cz.vse.pyraquest.domain.Room;

import java.util.ArrayList;
import java.util.List;

public class RoomPrinter {

    private static final String EMPTY = "-";

    public static String describe(GameState state) {
        Room room = state.getPlayer().getCurrentRoom();
        Backpack backpack = state.getPlayer().getBackpack();
        Texts texts = state.getTexts();

        String exits = joinOrDash(new ArrayList<>(room.getExits().keySet()));
        String items = joinOrDash(visibleItemCodes(room));
        String npcs = joinOrDash(visibleNpcCodes(room));
        String inBackpack = joinOrDash(backpackCodes(backpack));
        int count = backpack.getItems().size();

        String result = room.getName() + "\n\n"
                + room.getDescription() + "\n\n"
                + texts.get("system.room.exits") + " " + exits + "\n\n"
                + texts.get("system.room.items") + " " + items + "\n\n"
                + texts.get("system.room.npcs") + " " + npcs + "\n\n"
                + texts.get("system.room.backpack") + " (" + count + "/" + Backpack.CAPACITY + "): " + inBackpack;

        for (Npc npc : room.getNpcs()) {
            if (npc instanceof Enemy enemy && enemy.isAlive()) {
                result += "\n\n" + texts.get("system.warning." + enemy.getCode());
            }
        }

        return result;
    }

    private static List<String> visibleItemCodes(Room room) {
        List<String> codes = new ArrayList<>();
        for (Item item : room.getItems()) {
            codes.add(item.getCode());
            if (item.isContainer() && item.isOpened()) {
                for (Item inside : item.getContents()) {
                    codes.add(inside.getCode());
                }
            }
        }
        return codes;
    }

    private static List<String> visibleNpcCodes(Room room) {
        List<String> codes = new ArrayList<>();
        for (Npc npc : room.getNpcs()) {
            if (npc instanceof Enemy enemy && !enemy.isAlive()) {
                continue;
            }
            codes.add(npc.getCode());
        }
        return codes;
    }

    private static List<String> backpackCodes(Backpack backpack) {
        List<String> codes = new ArrayList<>();
        for (Item item : backpack.getItems()) {
            codes.add(item.getCode());
        }
        return codes;
    }

    private static String joinOrDash(List<String> codes) {
        return codes.isEmpty() ? EMPTY : String.join(", ", codes);
    }
}