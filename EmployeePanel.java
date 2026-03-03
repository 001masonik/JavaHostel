package org.example;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class EmployeePanel extends JPanel {
    private JTable table;
    private DefaultTableModel model;
    private JButton btnAdd = new JButton("Add");
    private JButton btnUpd = new JButton("Update");
    private JButton btnDel = new JButton("Delete");
    private String userRole;

    public EmployeePanel(String role) {
        this.userRole = role;
        setLayout(new BorderLayout(10, 10));

        model = new DefaultTableModel(new String[]{"ID", "ПІБ", "Посада", "ЗП", "Login"}, 0);
        table = new JTable(model);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        add(scrollPane, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel();
        btnPanel.add(btnAdd); btnPanel.add(btnUpd); btnPanel.add(btnDel);
        add(btnPanel, BorderLayout.NORTH);

        if (userRole == null || !userRole.equalsIgnoreCase("admin")) {
            btnAdd.setVisible(false);
            btnUpd.setVisible(false);
            btnDel.setVisible(false);
        }

        btnUpd.setEnabled(false);
        btnDel.setEnabled(false);

        table.getSelectionModel().addListSelectionListener(e -> {
            boolean selected = table.getSelectedRow() != -1;
            if (userRole != null && userRole.equalsIgnoreCase("admin")) {
                btnUpd.setEnabled(selected);
                btnDel.setEnabled(selected);
            }
        });

        btnAdd.addActionListener(e -> {
            EmployeeDialog dialog = new EmployeeDialog((Frame) SwingUtilities.getWindowAncestor(this), "Додати працівника", null);
            dialog.setVisible(true);
            if (dialog.isSucceeded()) refresh();
        });

        btnUpd.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                Employee emp = getSelectedEmployee(row);
                EmployeeDialog dialog = new EmployeeDialog((Frame) SwingUtilities.getWindowAncestor(this), "Редагувати", emp);
                dialog.setVisible(true);
                if (dialog.isSucceeded()) refresh();
            }
        });

        btnDel.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                // Створюємо вікно з запитом
                int confirm = JOptionPane.showConfirmDialog(
                        this,
                        "Ви впевнені, що хочете видалити цей запис?",
                        "Підтвердження видалення",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE
                );

                if (confirm == JOptionPane.YES_OPTION) {
                    int id = (int) model.getValueAt(row, 0);
                    EmployeeService.deleteEmployee(id);
                    refresh();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Виберіть рядок для видалення!");
            }
        });

        refresh();
    }

    private Employee getSelectedEmployee(int row) {
        int id = (int) model.getValueAt(row, 0);
        return EmployeeService.getAllEmployees().stream()
                .filter(emp -> emp.getId() == id)
                .findFirst()
                .orElse(null);
    }

    private void refresh() {
        model.setRowCount(0);
        EmployeeService.getAllEmployees().forEach(emp ->
                model.addRow(new Object[]{
                        emp.getId(),
                        emp.getFullName(),
                        emp.getRole(),
                        emp.getSalary(),
                        emp.getLogin()
                }));
    }
}