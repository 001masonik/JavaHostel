package org.example;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class RoomsPanel extends JPanel {
    private JTable table;
    private DefaultTableModel model;
    private JButton btnAdd = new JButton("➕ Додати");
    private JButton btnStatus = new JButton("✏️ Статус");
    private JButton btnDel = new JButton("❌ Видалити");
    private String userRole;

    public RoomsPanel(String role) {
        this.userRole = role;
        setLayout(new BorderLayout(10, 10));

        model = new DefaultTableModel(new String[]{"Номер", "Тип", "Місць", "Зайнято", "Ціна", "Статус"}, 0);
        table = new JTable(model);

        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
                Object statusObj = table.getValueAt(row, 5);
                String status = (statusObj != null) ? statusObj.toString() : "";
                if (!isSelected) {
                    if (status.equalsIgnoreCase("Вільно")) c.setBackground(new Color(200, 255, 200));
                    else if (status.equalsIgnoreCase("Зайнято")) c.setBackground(new Color(255, 200, 200));
                    else if (status.equalsIgnoreCase("Технічне обслуговування")) c.setBackground(new Color(255, 255, 200));
                }
                return c;
            }
        });

        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel btnPanel = new JPanel();
        btnPanel.add(btnAdd); btnPanel.add(btnStatus); btnPanel.add(btnDel);
        add(btnPanel, BorderLayout.NORTH);

        if (userRole != null && !userRole.equalsIgnoreCase("admin")) {
            btnAdd.setVisible(false);
            btnDel.setVisible(false);
        }

        btnAdd.addActionListener(e -> {
            RoomDialog diag = new RoomDialog((Frame) SwingUtilities.getWindowAncestor(this), "Нова кімната");
            diag.setVisible(true);
            if (diag.isSucceeded()) refresh();
        });

        btnStatus.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                String num = table.getValueAt(row, 0).toString();
                String[] opts = {"Вільно", "Зайнято", "Технічне обслуговування"};
                String res = (String) JOptionPane.showInputDialog(this, "Новий статус:", "Зміна", JOptionPane.QUESTION_MESSAGE, null, opts, opts[0]);
                if (res != null) { RoomService.updateStatus(num, res); refresh(); }
            }
        });

        btnDel.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1 && JOptionPane.showConfirmDialog(this, "Ви впевнені?", "Видалення", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                RoomService.deleteRoom(table.getValueAt(row, 0).toString());
                refresh();
            }
        });

        refresh();
    }

    public void refresh() {
        model.setRowCount(0);
        RoomService.getAllRooms().forEach(r -> model.addRow(new Object[]{
                r.getRoomNumber(), r.getType(), r.getCapacity(), r.getOccupied(), r.getPricePerDay(), r.getStatus()
        }));
    }
}