package org.example;

import javax.swing.*;
import java.awt.*;

public class EmployeeDialog extends JDialog {
    private JTextField fName = new JTextField(20);
    private JTextField fRole = new JTextField(20);
    private JTextField fSalary = new JTextField(20);
    private JTextField fLogin = new JTextField(20);
    private JPasswordField fPassword = new JPasswordField(20); // Повертаємо поле пароля
    private JButton btnSave = new JButton("Зберегти");
    private JButton btnCancel = new JButton("Скасувати");
    private boolean succeeded = false;
    private Integer employeeId = null;

    public EmployeeDialog(Frame parent, String title, Employee employee) {
        super(parent, title, true);
        setLayout(new GridLayout(6, 2, 10, 10)); // Збільшуємо кількість рядків до 6

        add(new JLabel("ПІБ:")); add(fName);
        add(new JLabel("Посада:")); add(fRole);
        add(new JLabel("ЗП:")); add(fSalary);
        add(new JLabel("Login:")); add(fLogin);
        add(new JLabel("Password:")); add(fPassword); // Додаємо мітку та поле
        add(btnSave); add(btnCancel);

        if (employee != null) {
            employeeId = employee.getId();
            fName.setText(employee.getFullName());
            fRole.setText(employee.getRole());
            fSalary.setText(String.valueOf(employee.getSalary()));
            fLogin.setText(employee.getLogin());
            fPassword.setText(employee.getPassword());
        }

        btnSave.addActionListener(e -> {
            if (validateFields()) {
                saveAction();
                succeeded = true;
                dispose();
            }
        });

        btnCancel.addActionListener(e -> dispose());
        pack();
        setLocationRelativeTo(parent);
    }

    private boolean validateFields() {
        if (fName.getText().trim().isEmpty() || fRole.getText().trim().isEmpty() ||
                fLogin.getText().trim().isEmpty() || fPassword.getPassword().length == 0) {
            JOptionPane.showMessageDialog(this, "Заповніть всі поля!");
            return false;
        }
        return true;
    }

    private void saveAction() {
        String name = fName.getText();
        String role = fRole.getText();
        int salary = Integer.parseInt(fSalary.getText());
        String login = fLogin.getText();
        String pass = new String(fPassword.getPassword()); // Отримуємо введений пароль

        if (employeeId == null) {
            EmployeeService.addEmployee(name, role, salary, login, pass);
        } else {
            // Викликаємо оновлений метод, який приймає пароль
            EmployeeService.updateEmployee(employeeId, name, role, salary, login, pass);
        }
    }

    public boolean isSucceeded() { return succeeded; }
}