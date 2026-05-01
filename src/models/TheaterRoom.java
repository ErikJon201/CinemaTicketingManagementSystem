package models;

public class TheaterRoom {
    private String roomID;
    private String roomName;

    //Constructors 
    public TheaterRoom() {
    }

    public TheaterRoom(String roomID, String roomName) {
        this.roomID = roomID;
        this.roomName = roomName;
    }

    //Getters 
    public String getRoomID() {
        return roomID;
    }

    public String getRoomName() {
        return roomName;
    }

    //Setters 
    public void setRoomID(String roomID) {
        this.roomID = roomID;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    @Override
    public String toString() {
        return String.format("[%s] %s", roomID, roomName);
    }
}