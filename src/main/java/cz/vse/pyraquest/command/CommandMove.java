package cz.vse.pyraquest.command;

import cz.vse.pyraquest.domain.Item;
import cz.vse.pyraquest.domain.Room;
import cz.vse.pyraquest.game.GameState;
import cz.vse.pyraquest.game.RoomPrinter;

public class CommandMove implements ICommand {
    private static final String NAME = "jdi";

    @Override
    public String execute(GameState state, String... parameters) {
        if (parameters.length == 0)
            return state.getTexts().get("move.error.noParam");

        var targetRoom = state.getPlayer().getCurrentRoom().getExit(parameters[0]);
        if (targetRoom == null)
            return state.getTexts().get("move.error.invalidParam");

        var unmetCondition = checkForUnmetEntryCondition(state, targetRoom);
        if (unmetCondition != null)
            return unmetCondition;

        state.getPlayer().setCurrentRoom(targetRoom);

        if (targetRoom.getCode().equals("vstup") && state.getPlayer().getBackpack().contains("maska")) {
            state.win();
            return state.getTexts().get("system.win");
        }

        String result = "";
        if (targetRoom.getCode().equals("rozcesti-sever") && state.isFuseLit()) {
            explode(state);
            result = state.getTexts().get("move.explosion") + "\n";
        }

        return result + RoomPrinter.describe(state);
    }

    private String checkForUnmetEntryCondition(GameState state, Room target) {
        var condition = target.getEntryCondition();
        if (condition == null)
            return null;

        switch (condition) {
            case "dark" -> {
                Item torch = state.getPlayer().getBackpack().get("pochoden");
                if (torch == null || !torch.isLit()) {
                    return state.getTexts().get("move.error.dark");
                }
            }
            case "wall" -> {
                if (state.getRoom("predsin").getItem("zed") != null) {
                    return state.getTexts().get("move.error.wall");
                }
            }
            default -> throw new IllegalStateException("Unknown entry condition: " + condition);
        }
        return null;
    }

    private void explode(GameState state) {
        Room tntPlacementRoom = state.getRoom("predsin");
        tntPlacementRoom.removeItem(tntPlacementRoom.getItem("zed"));
        tntPlacementRoom.removeItem(tntPlacementRoom.getItem("tnt"));
    }

    @Override
    public String getName() {
        return NAME;
    }
}
