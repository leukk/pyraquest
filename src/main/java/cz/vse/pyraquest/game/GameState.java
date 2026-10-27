package cz.vse.pyraquest.game;

import cz.vse.pyraquest.data.Texts;
import cz.vse.pyraquest.domain.Player;
import cz.vse.pyraquest.domain.Room;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class GameState {

    public enum Status {
        RUNNING,
        WON,
        LOST,
        QUIT
    }

    private final Player player;
    private final Map<String, Room> rooms;
    private final Texts texts;

    private final List<String> moves = new ArrayList<>();
    private Status status = Status.RUNNING;
    private boolean fuseLit;
    private int wrongAnswers;

    public GameState(Player player, Map<String, Room> rooms, Texts texts) {
        this.player = player;
        this.rooms = rooms;
        this.texts = texts;
    }

    public Player getPlayer() {
        return player;
    }

    public Room getRoom(String code) {
        return rooms.get(code);
    }

    public Map<String, Room> getRooms() {
        return Collections.unmodifiableMap(rooms);
    }

    public Texts getTexts() {
        return texts;
    }

    public Status getStatus() {
        return status;
    }

    public boolean isRunning() {
        return status == Status.RUNNING;
    }

    public void win() {
        status = Status.WON;
    }

    public void lose() {
        status = Status.LOST;
    }

    public void quit() {
        status = Status.QUIT;
    }

    public boolean isFuseLit() {
        return fuseLit;
    }

    public void setFuseLit(boolean fuseLit) {
        this.fuseLit = fuseLit;
    }

    public int getWrongAnswers() {
        return wrongAnswers;
    }

    public void addWrongAnswer() {
        wrongAnswers++;
    }

    public List<String> getMoves() {
        return Collections.unmodifiableList(moves);
    }

    public void recordMove(String move) {
        moves.add(move);
    }

    // TODO (save/load): store the drawn mummy questions here as well
}