package org.example;

public class Clients {
    private int id;
    private String fullName;
    private String passportData;
    private String phone;
    private String email;

    public Clients(int id, String fullName, String passportData, String phone, String email) {
        this.id = id;
        this.fullName = fullName;
        this.passportData = passportData;
        this.phone = phone;
        this.email = email;
    }

    // Геттери для відображення в таблиці
    public int getId() { return id; }
    public String getFullName() { return fullName; }
    public String getPassportData() { return passportData; }
    public String getPhone() { return phone; }
    public String getEmail() { return email; }
}