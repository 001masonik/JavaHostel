package org.example;

import javax.swing.*;

public class MainWindow extends JFrame {
    private LoginPanel loginPanel;

    public MainWindow() {
        super("Хостел");

        loginPanel = new LoginPanel((Employee employee) -> {
            JOptionPane.showMessageDialog(this, "Ви ввійшли як користувач " + employee.getFullName());

            getContentPane().removeAll();

            JTabbedPane tabbedPane = new JTabbedPane();

            tabbedPane.addTab("Працівники", new EmployeePanel(employee.getRole()));
            tabbedPane.addTab("Клієнти", new ClientsPanel(employee.getRole()));
            tabbedPane.addTab("Кімнати", new RoomsPanel(employee.getRole()));
            tabbedPane.addTab("Записи", new SettlementPanel(employee.getRole()));

            add(tabbedPane);

            revalidate();
            repaint();
        });

        add(loginPanel);

        setSize(1200, 900);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }
}