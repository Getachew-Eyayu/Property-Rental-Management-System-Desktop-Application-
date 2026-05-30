// DatabaseConnection provides a single reusable connection to the MySQL database.
// It implements a simple singleton pattern so that only one connection is used throughout the app.

package com.realestateapp2.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    // Database URL, username, and password – update as needed for your local setup
    private static final String URL = "jdbc:mysql://localhost:3306/real_estate_db";
    private static final String USER = "root";
    private static final String PASSWORD = "MyNewPassword123!"; // Update this to your MySQL password

    // Static Connection instance (singleton pattern)
    private static Connection connection = null;

    // Private constructor to prevent instantiation
    private DatabaseConnection() {}

    // -----------------------------
    // Get the database connection
    // -----------------------------
    public static Connection getConnection() throws SQLException {
        // If no connection exists or it's closed, create a new one
        if (connection == null || connection.isClosed()) {
            try {
                // Load MySQL JDBC driver
                Class.forName("com.mysql.cj.jdbc.Driver");

                // Establish connection using URL, username, and password
                connection = DriverManager.getConnection(URL, USER, PASSWORD);

            } catch (ClassNotFoundException e) {
                // Handle missing driver error
                throw new SQLException("JDBC Driver not found: " + e.getMessage());
            }
        }

        return connection; // Return the active connection
    }
}
