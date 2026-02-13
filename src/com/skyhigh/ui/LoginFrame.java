package com.skyhigh.ui;

import com.skyhigh.model.User;
import com.skyhigh.service.AuthService;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {

    private JTextField emailField;
    private JPasswordField passwordField;
    private AuthService authService;

    public LoginFrame() {

        authService = new AuthService();

        setTitle("SkyHigh Airlines - Login");
        setSize(650, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Use Gradient Background
        setContentPane(new GradientPanel());
        setLayout(new BorderLayout());

        add(createLoginPanel(), BorderLayout.CENTER);

        setVisible(true);
    }

    private JPanel createLoginPanel() {

        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setOpaque(false); // allow gradient to show

        JPanel card = new JPanel();
        card.setPreferredSize(new Dimension(420, 380));
        card.setBackground(new Color(45, 50, 70));
        card.setLayout(new GridBagLayout());
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(80, 90, 130), 1, true),
                BorderFactory.createEmptyBorder(35, 45, 35, 45)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 10, 15, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title
        JLabel title = new JLabel("SkyHigh Airlines");
        title.setForeground(new Color(220, 230, 255));
        title.setFont(new Font("Segoe UI", Font.BOLD, 30));
        title.setHorizontalAlignment(SwingConstants.CENTER);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        card.add(title, gbc);

        // Subtitle
        JLabel subtitle = new JLabel("Airline Reservation System");
        subtitle.setForeground(new Color(150, 170, 220));
        subtitle.setHorizontalAlignment(SwingConstants.CENTER);

        gbc.gridy = 1;
        card.add(subtitle, gbc);

        gbc.gridwidth = 1;

        // Email
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setForeground(Color.WHITE);

        gbc.gridx = 0;
        gbc.gridy = 2;
        card.add(emailLabel, gbc);

        emailField = new JTextField();
        styleInput(emailField);

        gbc.gridx = 1;
        card.add(emailField, gbc);

        // Password
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setForeground(Color.WHITE);

        gbc.gridx = 0;
        gbc.gridy = 3;
        card.add(passwordLabel, gbc);

        passwordField = new JPasswordField();
        styleInput(passwordField);

        gbc.gridx = 1;
        card.add(passwordField, gbc);

        // Login Button
        JButton loginButton = new JButton("Login");
        stylePrimaryButton(loginButton);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        card.add(loginButton, gbc);

        // Register Button
        JButton registerButton = new JButton("Register");
        styleSecondaryButton(registerButton);

        gbc.gridy = 5;
        card.add(registerButton, gbc);

        loginButton.addActionListener(e -> login());
        registerButton.addActionListener(e -> new RegisterFrame());

        wrapper.add(card);

        return wrapper;
    }

    private void styleInput(JTextField field) {
        field.setBackground(new Color(70, 75, 100));
        field.setForeground(Color.WHITE);
        field.setCaretColor(Color.WHITE);
        field.setBorder(BorderFactory.createEmptyBorder(6, 10, 6, 10));
    }

    private void stylePrimaryButton(JButton button) {
        button.setBackground(new Color(0, 150, 255));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
    }

    private void styleSecondaryButton(JButton button) {
        button.setBackground(new Color(100, 100, 200));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
    }

    private void login() {

        String email = emailField.getText();
        String password = new String(passwordField.getPassword());

        User user = authService.login(email, password);

        if (user != null) {

            dispose();

            if (user.getRole().equalsIgnoreCase("ADMIN")) {
                new AdminDashboard();
            } else {
                new UserDashboard(user);
            }

        } else {
            JOptionPane.showMessageDialog(this,
                    "Invalid email or password",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
