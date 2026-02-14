package com.skyhigh.service;

import com.skyhigh.dao.UserDAO;
import com.skyhigh.model.User;

public class AuthService {

    private UserDAO userDAO;

    public AuthService() {
        userDAO = new UserDAO();
    }

    // Registration
    public String register(String name, String email, String password, String phone) {

        // Validate input fields
        if (name == null || name.trim().isEmpty()) {
            return "EMPTY_FIELDS";
        }
        if (email == null || email.trim().isEmpty()) {
            return "EMPTY_FIELDS";
        }
        if (password == null || password.trim().isEmpty()) {
            return "EMPTY_FIELDS";
        }
        if (phone == null || phone.trim().isEmpty()) {
            return "EMPTY_FIELDS";
        }

        // Validate phone number (must be 10-15 digits only)
        String phoneDigits = phone.replaceAll("[^0-9]", ""); // Remove non-digits
        if (phoneDigits.length() < 10 || phoneDigits.length() > 15) {
            return "INVALID_PHONE";
        }

        // By default, new users are USER role
        User user = new User(name, email, password, phone, "USER");

        int result = userDAO.registerUser(user);

        // result: 1=success, 0=db error, -1=duplicate email
        if (result == 1) {
            return "SUCCESS";
        } else if (result == -1) {
            return "DUPLICATE_EMAIL";
        } else {
            return "DATABASE_ERROR";
        }
    }

    // Login
    public User login(String email, String password) {

        User user = userDAO.getUserByEmail(email);

        if (user != null) {
            if (user.getPassword().equals(password)) {
                return user; // Successful login
            }
        }

        return null; // Invalid credentials
    }
}
