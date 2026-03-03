package org.example;

import javax.swing.*;
import java.awt.*;

public class ClientDialog extends JDialog {
    private JTextField fName = new JTextField(20);
    private JTextField fPassport = new JTextField(20);
    private JTextField fPhone = new JTextField(20);
    private JTextField fEmail = new JTextField(20);
    private JButton btnSave = new JButton("Зберегти");
    private JButton btnCancel = new JButton("Скасувати");
    private boolean succeeded = false;

    public ClientDialog(Frame parent, String title) {
        super(parent, title, true);
        setLayout(new GridLayout(5, 2, 10, 10));

        add(new JLabel("ПІБ:")); add(fName);
        add(new JLabel("Паспортні дані:")); add(fPassport);
        add(new JLabel("Телефон:")); add(fPhone);
        add(new JLabel("Email:")); add(fEmail);
        add(btnSave); add(btnCancel);

        btnSave.addActionListener(e -> {
            if (validateFields()) {
                ClientService.addClient(fName.getText(), fPassport.getText(), fPhone.getText(), fEmail.getText());
                succeeded = true;
                dispose();
            }
        });

        btnCancel.addActionListener(e -> dispose());

        pack();
        setLocationRelativeTo(parent);
    }

    private boolean validateFields() {
        if (fName.getText().trim().isEmpty() || fPassport.getText().trim().isEmpty() ||
                fPhone.getText().trim().isEmpty() || fEmail.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Заповніть всі поля!");
            return false;
        }
        return true;
    }

    public boolean isSucceeded() { return succeeded; }
}