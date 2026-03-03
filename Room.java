package org.example;

public class Room {
    private String roomNumber;
    private String type;
    private int capacity;
    private int occupied;
    private double pricePerDay;
    private String status;

    public Room(String roomNumber, String type, int capacity, int occupied, double pricePerDay, String status) {
        this.roomNumber = roomNumber;
        this.type = type;
        this.capacity = capacity;
        this.occupied = occupied;
        this.pricePerDay = pricePerDay;
        this.status = status;
    }

    public String getRoomNumber() { return roomNumber; }
    public String getType() { return type; }
    public int getCapacity() { return capacity; }
    public int getOccupied() { return occupied; }
    public double getPricePerDay() { return pricePerDay; }
    public String getStatus() { return status; }
}