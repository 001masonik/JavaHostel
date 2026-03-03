package org.example;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;

public class SettlementPanel extends JPanel {
    private JTable table;
    private DefaultTableModel model;
    private JButton btnAdd = new JButton("➕ Нове поселення");
    private final DateTimeFormatter ukrFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public SettlementPanel(String role) {
        setLayout(new BorderLayout(10, 10));
        model = new DefaultTableModel(new String[]{"ID", "Кімната", "Клієнт", "Заїзд", "Виїзд", "Вартість", "Оплата"}, 0);
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel p = new JPanel(); p.add(btnAdd); add(p, BorderLayout.NORTH);

        btnAdd.addActionListener(e -> {
            SettlementDialog dialog = new SettlementDialog((Frame) SwingUtilities.getWindowAncestor(this));
            dialog.setVisible(true);
            if (dialog.isSucceeded()) {
                refresh();
                JTabbedPane tabs = (JTabbedPane) SwingUtilities.getAncestorOfClass(JTabbedPane.class, this);
                if (tabs != null) {
                    for (Component c : tabs.getComponents()) {
                        if (c instanceof RoomsPanel) ((RoomsPanel) c).refresh();
                    }
                }
            }
        });
        refresh();
    }

    public void refresh() {
        model.setRowCount(0);
        SettlementService.getAllSettlements().forEach(s -> model.addRow(new Object[]{
                s.getId(), s.getRoomNumber(), s.getClientId(),
                s.getCheckIn().toLocalDate().format(ukrFormatter),
                s.getCheckOut().toLocalDate().format(ukrFormatter),
                s.getTotalPrice(), s.getPaidAmount()
        }));
    }
}