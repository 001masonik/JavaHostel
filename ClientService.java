package org.example;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClientService {
    public static List<Clients> getAllClients() {
        List<Clients> clients = new ArrayList<>();
        String sql = "SELECT * FROM clients";
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                clients.add(new Clients(
                        rs.getInt("id"),
                        rs.getString("full_name"),
                        rs.getString("passportdata"),
                        rs.getString("phone"),
                        rs.getString("email")
                ));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return clients;
    }

    public static void addClient(String name, String passport, String phone, String email) {
        String sql = "INSERT INTO clients (full_name, passportdata, phone, email) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setString(2, passport);
            ps.setString(3, phone);
            ps.setString(4, email);
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }
}