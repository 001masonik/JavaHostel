package org.example;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ClientsPanel extends JPanel {
    private JTable table;
    private DefaultTableModel model;
    private JButton btnAdd = new JButton("Додати клієнта");
    private String userRole;

    public ClientsPanel(String role) {
        this.userRole = role;
        setLayout(new BorderLayout(10, 10));

        // Налаштування стовпців згідно вашої БД
        model = new DefaultTableModel(new String[]{"ID", "ПІБ", "Паспорт", "Телефон", "Email"}, 0);
        table = new JTable(model);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        add(scrollPane, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel();
        btnPanel.add(btnAdd);
        add(btnPanel, BorderLayout.NORTH);

        btnAdd.addActionListener(e -> {
            ClientDialog dialog = new ClientDialog((Frame) SwingUtilities.getWindowAncestor(this), "Додати клієнта");
            dialog.setVisible(true);
            if (dialog.isSucceeded()) refresh();
        });

        refresh();
    }

    public void refresh() {
        model.setRowCount(0);
        ClientService.getAllClients().forEach(c ->
                model.addRow(new Object[]{c.getId(), c.getFullName(), c.getPassportData(), c.getPhone(), c.getEmail()}));
    }
}