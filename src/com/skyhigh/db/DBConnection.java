package com.skyhigh.db;

import java.sql.*;

public class DBConnection {

    private static final String URL = "jdbc:mysql://localhost:3306/skyhigh";
    private static final String USER = "root";
    private static final String PASSWORD = "Gourd@13";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
