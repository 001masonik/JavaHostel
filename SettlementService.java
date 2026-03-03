package org.example;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SettlementService {
    public static List<Settlement> getAllSettlements() {
        List<Settlement> list = new ArrayList<>();
        String sql = "SELECT * FROM settlements";
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Settlement(
                        rs.getInt("id"), rs.getString("room_number"),
                        rs.getInt("client_id"), rs.getDate("check_in_date"),
                        rs.getDate("check_out_date"), rs.getDouble("total_cost"),
                        rs.getDouble("amount_paid")
                ));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    public static void addSettlement(String roomNum, int clientId, String in, String out, double total, double paid, int guests) {
        String insertSql = "INSERT INTO settlements (room_number, client_id, check_in_date, check_out_date, total_cost, amount_paid) VALUES (?, ?, ?, ?, ?, ?)";
        // Оновлюємо кількість зайнятих місць та змінюємо статус на "Зайнято", якщо місць більше немає
        String updateRoomSql = "UPDATE rooms SET occupied = occupied + ?, " +
                "status = CASE WHEN occupied + ? >= capacity THEN 'Зайнято' ELSE 'Вільно' END " +
                "WHERE room_number = ?";

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);
            try {
                PreparedStatement ps = conn.prepareStatement(insertSql);
                ps.setString(1, roomNum);
                ps.setInt(2, clientId);
                ps.setDate(3, Date.valueOf(in));
                ps.setDate(4, Date.valueOf(out));
                ps.setDouble(5, total);
                ps.setDouble(6, paid);
                ps.executeUpdate();

                PreparedStatement psRoom = conn.prepareStatement(updateRoomSql);
                psRoom.setInt(1, guests);
                psRoom.setInt(2, guests);
                psRoom.setString(3, roomNum);
                psRoom.executeUpdate();

                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        } catch (SQLException e) { e.printStackTrace(); }
    }
}