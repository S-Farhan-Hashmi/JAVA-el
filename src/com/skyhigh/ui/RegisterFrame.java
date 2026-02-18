package com.skyhigh.ui;

import com.skyhigh.service.AuthService;

import javax.swing.*;
import java.awt.*;

public class RegisterFrame extends JFrame {

    private JTextField nameField;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JTextField phoneField;

    private AuthService authService;

    public RegisterFrame() {

        authService = new AuthService();

        setTitle("SkyHigh Airlines - Register");
        setSize(650, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setContentPane(new GradientPanel());
        setLayout(new BorderLayout());
        add(createRegisterPanel(), BorderLayout.CENTER);

        setVisible(true);
    }

    private JPanel createRegisterPanel() {

        JPanel panel1 = new JPanel(new GridBagLayout());
        panel1.setOpaque(false);

        JPanel card = new JPanel();
        card.setPreferredSize(new Dimension(450, 400));
        card.setBackground(new Color(45, 50, 70));
        card.setLayout(new GridBagLayout());
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(80, 90, 130), 1, true),
                BorderFactory.createEmptyBorder(35, 45, 35, 45)));


        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 10, 15, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("Create Account");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setHorizontalAlignment(SwingConstants.CENTER);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        card.add(title, gbc);

        gbc.gridwidth = 1;

        // Name
        JLabel nameLabel = new JLabel("Full Name:");
        nameLabel.setForeground(Color.WHITE);

        gbc.gridx = 0;
        gbc.gridy = 1;
        card.add(nameLabel, gbc);

        nameField = new JTextField();
        styleInput(nameField);

        gbc.gridx = 1;
        card.add(nameField, gbc);

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
        JLabel passLabel = new JLabel("Password:");
        passLabel.setForeground(Color.WHITE);

        gbc.gridx = 0;
        gbc.gridy = 3;
        card.add(passLabel, gbc);

        passwordField = new JPasswordField();
        styleInput(passwordField);

        gbc.gridx = 1;
        card.add(passwordField, gbc);

        // Phone
        JLabel phoneLabel = new JLabel("Phone:");
        phoneLabel.setForeground(Color.WHITE);

        gbc.gridx = 0;
        gbc.gridy = 4;
        card.add(phoneLabel, gbc);

        phoneField = new JTextField();
        styleInput(phoneField);

        gbc.gridx = 1;
        card.add(phoneField, gbc);

        // Register Button
        JButton registerBtn = new JButton("Register");
        stylePrimaryButton(registerBtn);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        card.add(registerBtn, gbc);

        registerBtn.addActionListener(e -> registerUser());

        panel1.add(card);

        return panel1;
    }

    private void styleInput(JTextField field) {
        field.setBackground(new Color(70, 75, 100));
        field.setForeground(Color.WHITE);
        field.setCaretColor(Color.WHITE);
        field.setBorder(BorderFactory.createEmptyBorder(5, 8, 5, 8));
    }

    private void stylePrimaryButton(JButton button) {
        button.setBackground(new Color(0, 150, 255));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
    }

    private void registerUser() {

        String name = nameField.getText();
        String email = emailField.getText();
        String password = new String(passwordField.getPassword());
        String phone = phoneField.getText();

        String result = authService.register(name, email, password, phone);

        if (result.equals("SUCCESS")) {
            JOptionPane.showMessageDialog(this, "Registration Successful!");
            dispose();
        } else if (result.equals("EMPTY_FIELDS")) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.");
        } else if (result.equals("INVALID_PHONE")) {
            JOptionPane.showMessageDialog(this, "Invalid phone number (10-15 digits required).");
        } else if (result.equals("DUPLICATE_EMAIL")) {
            JOptionPane.showMessageDialog(this, "Email already registered.");
        } else {
            JOptionPane.showMessageDialog(this, "Registration Failed.");
        }
    }
}
