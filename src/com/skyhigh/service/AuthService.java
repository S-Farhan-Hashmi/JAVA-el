package com.skyhigh.service;

import com.skyhigh.dao.UserDAO;
import com.skyhigh.model.User;

public class AuthService {

    private UserDAO userDAO;

    public AuthService() {
        userDAO = new UserDAO();
    }

    // Registration
    public boolean register(String name, String email, String password, String phone) {

        // By default, new users are USER role
        User user = new User(name, email, password, phone, "USER");

        return userDAO.registerUser(user);
    }

    // Login
    public User login(String email, String password) {

        User user = userDAO.getUserByEmail(email);

        if (user != null) {
            if (user.getPassword().equals(password)) {
                return user;  // Successful login
            }
        }

        return null; // Invalid credentials
    }
}
