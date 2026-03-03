package org.example;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class EmployeeService {

    public static Employee login(String login, String password) {
        String request = "SELECT id, full_name, role, salary, password FROM employee WHERE login = ? AND password = ?";

        try (
                Connection connection = DBConnection.getConnection();
                PreparedStatement ps = connection.prepareStatement(request);
        ) {
            ps.setString(1, login);
            ps.setString(2, password);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }

                int id = rs.getInt("id");
                String name = rs.getString("full_name");
                String role = rs.getString("role");
                int salary = rs.getInt("salary");
                String dbPassword = rs.getString("password");

                return new Employee(id, name, role, salary, login, dbPassword);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Помилка: " + e.getMessage());
        }
        return null;
    }

    public static List<Employee> getAllEmployees() {
        List<Employee> employees = new ArrayList<>();
        String sql = "SELECT * FROM employee";

        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                employees.add(new Employee(
                        rs.getInt("id"),
                        rs.getString("full_name"),
                        rs.getString("role"),
                        rs.getInt("salary"),
                        rs.getString("login"),
                        rs.getString("password")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return employees;
    }

    public static void addEmployee(String name, String role, int salary, String login, String pass) {
        String sql = "INSERT INTO employee (full_name, role, salary, login, password) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setString(2, role);
            ps.setInt(3, salary);
            ps.setString(4, login);
            ps.setString(5, pass);
            ps.executeUpdate();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Помилка додавання: " + e.getMessage());
        }
    }

    // ОНОВЛЕНИЙ МЕТОД: тепер приймає та оновлює пароль (password)
    public static void updateEmployee(int id, String name, String role, int salary, String login, String password) {
        String sql = "UPDATE employee SET full_name=?, role=?, salary=?, login=?, password=? WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setString(2, role);
            ps.setInt(3, salary);
            ps.setString(4, login);
            ps.setString(5, password); // Оновлення пароля в базі
            ps.setInt(6, id);
            ps.executeUpdate();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Помилка оновлення: " + e.getMessage());
        }
    }

    public static void deleteEmployee(int id) {
        String sql = "DELETE FROM employee WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Помилка видалення: " + e.getMessage());
        }
    }
}