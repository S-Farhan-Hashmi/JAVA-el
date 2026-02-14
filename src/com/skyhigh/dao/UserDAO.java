package com.skyhigh.dao;

import com.skyhigh.db.DBConnection;
import com.skyhigh.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

public class UserDAO {

    // Register new user
    // Returns: 1 for success, 0 for general error, -1 for duplicate email
    public int registerUser(User user) {

        String query = "INSERT INTO users (name, email, password, phone, role) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, user.getName());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getPassword());
            stmt.setString(4, user.getPhone());
            stmt.setString(5, user.getRole());

            int rows = stmt.executeUpdate();
            return rows > 0 ? 1 : 0;

        } catch (SQLIntegrityConstraintViolationException e) {
            // Duplicate email error (UNIQUE constraint violation)
            System.err.println("Duplicate email detected: " + user.getEmail());
            e.printStackTrace();
            return -1;
        } catch (SQLException e) {
            System.err.println("Database error during registration:");
            System.err.println("Error Code: " + e.getErrorCode());
            System.err.println("SQL State: " + e.getSQLState());
            System.err.println("Message: " + e.getMessage());
            e.printStackTrace();
            return 0;
        }
    }

    // Find user by email (for login)
    public User getUserByEmail(String email) {

        String query = "SELECT * FROM users WHERE email = ?";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, email);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new User(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("phone"),
                        rs.getString("role"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}
