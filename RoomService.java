package org.example;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class RoomService {
    public static List<Room> getAllRooms() {
        List<Room> rooms = new ArrayList<>();
        String sql = "SELECT * FROM rooms";
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                rooms.add(new Room(
                        rs.getString("room_number"),
                        rs.getString("type"),
                        rs.getInt("capacity"), // ВИПРАВЛЕНО: збігається з БД
                        rs.getInt("occupied"), // ВИПРАВЛЕНО: збігається з БД
                        rs.getDouble("price_per_day"),
                        rs.getString("status")
                ));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return rooms;
    }

    public static void addRoom(String number, String type, int capacity, double price) {
        // SQL запит тепер використовує правильні назви стовпців
        String sql = "INSERT INTO rooms (room_number, type, capacity, occupied, price_per_day, status) VALUES (?, ?, ?, 0, ?, 'Вільно')";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, number);
            ps.setString(2, type);
            ps.setInt(3, capacity);
            ps.setDouble(4, price);
            ps.executeUpdate();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Помилка додавання: " + e.getMessage());
        }
    }

    public static void updateStatus(String roomNumber, String newStatus) {
        String sql = "UPDATE rooms SET status = ? WHERE room_number = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newStatus);
            ps.setString(2, roomNumber);
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public static void deleteRoom(String roomNumber) {
        String sql = "DELETE FROM rooms WHERE room_number = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, roomNumber);
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }
}