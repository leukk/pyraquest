package cz.vse.pyraquest.domain;

public class Player {
    private Room currentRoom;
    private final Backpack backpack = new Backpack();

    public Player(Room startRoom) {
        this.currentRoom = startRoom;
    }

    public Room getCurrentRoom() {
        return currentRoom;
    }

    public void setCurrentRoom(Room room) {
        this.currentRoom = room;
    }

    public Backpack getBackpack() {
        return backpack;
    }
}
