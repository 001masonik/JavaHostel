package org.example;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class SettlementDialog extends JDialog {
    private final DateTimeFormatter ukrFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private JComboBox<String> comboRooms;
    private JComboBox<Integer> comboClients;
    private JTextField fIn = new JTextField(LocalDate.now().format(ukrFormatter), 10);
    private JTextField fOut = new JTextField(LocalDate.now().plusDays(1).format(ukrFormatter), 10);
    private JTextField fGuests = new JTextField("1", 10);
    private JTextField fPaid = new JTextField("0", 10);
    private JLabel lblTotal = new JLabel("0 грн");
    private JButton btnSave = new JButton("Оформити");
    private boolean succeeded = false;

    public SettlementDialog(Frame parent) {
        super(parent, "Нове поселення", true);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 15, 8, 15);
        gbc.anchor = GridBagConstraints.WEST;

        comboRooms = new JComboBox<>(RoomService.getAllRooms().stream()
                .filter(r -> r.getOccupied() < r.getCapacity())
                .map(Room::getRoomNumber).toArray(String[]::new));
        comboClients = new JComboBox<>(ClientService.getAllClients().stream()
                .map(Clients::getId).toArray(Integer[]::new));

        gbc.gridx = 0; gbc.gridy = 0; add(new JLabel("Кімната:"), gbc);
        gbc.gridx = 1; add(comboRooms, gbc);
        gbc.gridx = 0; gbc.gridy = 1; add(new JLabel("ID Клієнта:"), gbc);
        gbc.gridx = 1; add(comboClients, gbc);
        gbc.gridx = 0; gbc.gridy = 2; add(new JLabel("Кількість людей:"), gbc);
        gbc.gridx = 1; add(fGuests, gbc);
        gbc.gridx = 0; gbc.gridy = 3; add(new JLabel("Заїзд (ДД-ММ-РРРР):"), gbc);
        gbc.gridx = 1; add(fIn, gbc);
        gbc.gridx = 0; gbc.gridy = 4; add(new JLabel("Виїзд (ДД-ММ-РРРР):"), gbc);
        gbc.gridx = 1; add(fOut, gbc);
        gbc.gridx = 0; gbc.gridy = 5; add(new JLabel("Внесена оплата:"), gbc);
        gbc.gridx = 1; add(fPaid, gbc);
        gbc.gridx = 0; gbc.gridy = 6; add(new JLabel("До сплати:"), gbc);
        gbc.gridx = 1; lblTotal.setFont(new Font("Arial", Font.BOLD, 13)); add(lblTotal, gbc);

        gbc.gridx = 0; gbc.gridy = 7; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
        add(btnSave, gbc);

        btnSave.addActionListener(e -> {
            try {
                int guests = Integer.parseInt(fGuests.getText());
                LocalDate start = LocalDate.parse(fIn.getText(), ukrFormatter);
                LocalDate end = LocalDate.parse(fOut.getText(), ukrFormatter);
                long days = ChronoUnit.DAYS.between(start, end);
                if (days <= 0) days = 1;

                Room room = RoomService.getAllRooms().stream()
                        .filter(r -> r.getRoomNumber().equals(comboRooms.getSelectedItem()))
                        .findFirst().get();

                if (guests > (room.getCapacity() - room.getOccupied())) {
                    JOptionPane.showMessageDialog(this, "Тільки " + (room.getCapacity() - room.getOccupied()) + " місць вільно!");
                    return;
                }

                SettlementService.addSettlement(room.getRoomNumber(), (Integer) comboClients.getSelectedItem(),
                        start.toString(), end.toString(), (room.getPricePerDay() * days),
                        Double.parseDouble(fPaid.getText()), guests);

                succeeded = true;
                dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Помилка даних! Формат: ДД-ММ-РРРР");
            }
        });
        pack(); setLocationRelativeTo(parent);
    }
    public boolean isSucceeded() { return succeeded; }
}