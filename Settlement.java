package org.example;

import java.sql.Date;

public class Settlement {
    private int id;
    private String roomNumber;
    private int clientId;
    private Date checkIn;
    private Date checkOut;
    private double totalPrice;
    private double paidAmount;

    public Settlement(int id, String roomNumber, int clientId, Date checkIn, Date checkOut, double totalPrice, double paidAmount) {
        this.id = id;
        this.roomNumber = roomNumber;
        this.clientId = clientId;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.totalPrice = totalPrice;
        this.paidAmount = paidAmount;
    }

    public int getId() { return id; }
    public String getRoomNumber() { return roomNumber; }
    public int getClientId() { return clientId; }
    public Date getCheckIn() { return checkIn; }
    public Date getCheckOut() { return checkOut; }
    public double getTotalPrice() { return totalPrice; }
    public double getPaidAmount() { return paidAmount; }
}