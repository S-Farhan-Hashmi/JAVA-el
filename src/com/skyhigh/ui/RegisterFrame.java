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
        setLayout(new BorderLayout());

        getContentPane().setBackground(new Color(20, 25, 40));

        add(createRegisterPanel(), BorderLayout.CENTER);

        setVisible(true);
    }

    private JPanel createRegisterPanel() {

        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setBackground(new Color(20, 25, 40));

        JPanel card = new JPanel();
        card.setPreferredSize(new Dimension(450, 400));
        card.setBackground(new Color(45, 50, 70));
        card.setLayout(new GridBagLayout());
        card.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

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

        wrapper.add(card);

        return wrapper;
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

        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();
        String phone = phoneField.getText().trim();

        // Validate empty fields
        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || phone.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "All fields are required!",
                    "Registration Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        String result = authService.register(name, email, password, phone);

        switch (result) {
            case "SUCCESS":
                JOptionPane.showMessageDialog(this,
                        "Registration Successful!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                dispose();
                break;

            case "DUPLICATE_EMAIL":
                JOptionPane.showMessageDialog(this,
                        "This email is already registered.\nPlease use a different email.",
                        "Email Already Exists",
                        JOptionPane.ERROR_MESSAGE);
                break;

            case "INVALID_PHONE":
                JOptionPane.showMessageDialog(this,
                        "Invalid phone number.\nPhone must contain 10-15 digits.",
                        "Invalid Phone Number",
                        JOptionPane.ERROR_MESSAGE);
                break;

            case "EMPTY_FIELDS":
                JOptionPane.showMessageDialog(this,
                        "All fields are required!",
                        "Registration Error",
                        JOptionPane.ERROR_MESSAGE);
                break;

            case "DATABASE_ERROR":
            default:
                JOptionPane.showMessageDialog(this,
                        "Registration failed. Please check:\n" +
                                "- Database connection is active\n" +
                                "- All required fields are filled correctly\n\n" +
                                "Check the console for detailed error information.",
                        "Registration Error",
                        JOptionPane.ERROR_MESSAGE);
                break;
        }
    }
}
