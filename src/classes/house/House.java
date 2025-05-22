package classes.house;

import java.util.ArrayList;
import java.util.Comparator;

public class House {
    private String name;
    private int id;
    private float xCoordinate;
    private float yCoordinate;
    private ArrayList<Room> rooms;

    public House(String name, int id, float xCoordinate, float yCoordinate) {
        this.name = name;
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
        this.id = id;
        this.rooms = new ArrayList<>();
    }

    public void addRoom(Room room) {
        rooms.add(room);
        sortRoomsByType();
    }

    public void removeRoom(Room room) {
        rooms.remove(room);
    }

    public Boolean hasRooms() {
        return !rooms.isEmpty();
    }

    public int getNumOfRooms() {
        return rooms.size();
    }

    public void sortRoomsByType() {
        rooms.sort(Comparator.comparing(Room::getRoomType));
    }

    public ArrayList<Room> getRooms() {
        return rooms;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getXCoord() {
        return xCoordinate;
    }
    public float getYCoord() {
        return yCoordinate;
    }

    public int getId() {
        return id;
    }

}
