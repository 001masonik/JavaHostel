package org.example;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;

public class LoginPanel extends JPanel {
    public static interface IOnLoginCallBack {
        void onLogin(Employee employee);
    }

    private JLabel loginLabel;
    private JLabel passwordLabel;
    private JTextField loginInput;
    private JPasswordField passwordInput;
    private JButton loginButton;
    private IOnLoginCallBack onLoginCallBack;

    public LoginPanel(IOnLoginCallBack onLoginCallBack) {
        this.onLoginCallBack = onLoginCallBack;

        loginLabel = new JLabel("Логін:");
        passwordLabel = new JLabel("Пароль:");
        loginInput = new JTextField(15);
        passwordInput = new JPasswordField(15);
        loginButton = new JButton("Увійти");
        loginButton.setEnabled(false);

        setLayout(new FlowLayout());

        add(loginLabel);
        add(loginInput);
        add(passwordLabel);
        add(passwordInput);
        add(loginButton);

        setInputListeners();
        setButtonListeners();

        SwingUtilities.invokeLater(() -> {
            JRootPane rootPane = SwingUtilities.getRootPane(loginButton);
            if (rootPane != null) {
                rootPane.setDefaultButton(loginButton);
            }
        });
    }

    private void setButtonListeners() {
        loginButton.addActionListener((ActionEvent e) -> {
            performLogin(); // Винесено в окремий метод для зручності
        });
    }

    private void performLogin() {
        String login = loginInput.getText();
        String password = new String(passwordInput.getPassword());

        Employee employee = EmployeeService.login(login, password);

        if (employee == null) {
            JOptionPane.showMessageDialog(this, "Користувача не знайдено або дані невірні", "Помилка входу", JOptionPane.ERROR_MESSAGE);
        } else {
            this.onLoginCallBack.onLogin(employee);
        }
    }

    private void checkButtonState() {
        String login = loginInput.getText().trim();
        String password = new String(passwordInput.getPassword()).trim();
        loginButton.setEnabled(!login.isEmpty() && !password.isEmpty());
    }

    private void setInputListeners() {
        DocumentListener dl = new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { checkButtonState(); }
            @Override
            public void removeUpdate(DocumentEvent e) { checkButtonState(); }
            @Override
            public void changedUpdate(DocumentEvent e) { checkButtonState(); }
        };

        loginInput.getDocument().addDocumentListener(dl);
        passwordInput.getDocument().addDocumentListener(dl);
    }
}