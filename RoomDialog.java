package org.example;

import javax.swing.*;
import java.awt.*;

public class RoomDialog extends JDialog {
    private JTextField fNumber = new JTextField(15);
    private JComboBox<String> fType = new JComboBox<>(new String[]{"Стандарт", "Люкс"});
    private JTextField fCapacity = new JTextField(15);
    private JTextField fPrice = new JTextField(15);
    private JButton btnSave = new JButton("Зберегти");
    private JButton btnCancel = new JButton("Скасувати");
    private boolean succeeded = false;

    public RoomDialog(Frame parent, String title) {
        super(parent, title, true);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0; add(new JLabel("Номер:"), gbc);
        gbc.gridx = 1; add(fNumber, gbc);
        gbc.gridx = 0; gbc.gridy = 1; add(new JLabel("Тип:"), gbc);
        gbc.gridx = 1; add(fType, gbc);
        gbc.gridx = 0; gbc.gridy = 2; add(new JLabel("Місць:"), gbc);
        gbc.gridx = 1; add(fCapacity, gbc);
        gbc.gridx = 0; gbc.gridy = 3; add(new JLabel("Ціна/доба:"), gbc);
        gbc.gridx = 1; add(fPrice, gbc);

        JPanel btnPanel = new JPanel();
        btnPanel.add(btnSave); btnPanel.add(btnCancel);
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2; add(btnPanel, gbc);

        btnSave.addActionListener(e -> {
            if (validateFields()) {
                RoomService.addRoom(fNumber.getText(), fType.getSelectedItem().toString(),
                        Integer.parseInt(fCapacity.getText()), Double.parseDouble(fPrice.getText()));
                succeeded = true;
                dispose();
            }
        });
        btnCancel.addActionListener(e -> dispose());
        pack();
        setLocationRelativeTo(parent);
    }

    private boolean validateFields() {
        try {
            if (fNumber.getText().isEmpty()) return false;
            Integer.parseInt(fCapacity.getText());
            Double.parseDouble(fPrice.getText());
            return true;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Перевірте дані (тільки цифри для ціни/місць)!");
            return false;
        }
    }
    public boolean isSucceeded() { return succeeded; }
}