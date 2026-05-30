// RegisterController handles user registration for the real estate app.
// Validates input fields, checks username availability, creates a new tenant user,
// and redirects to the login screen after successful registration.

package com.realestateapp2.controller;

import com.realestateapp2.db.DatabaseConnection;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class RegisterController {

    // -----------------------------
    // FXML UI elements
    // -----------------------------
    @FXML private TextField usernameField;         // Input field for username
    @FXML private PasswordField passwordField;     // Input field for password
    @FXML private PasswordField confirmPasswordField; // Input field to confirm password

    // -----------------------------
    // Register button action
    // -----------------------------
    @FXML
    private void handleRegister() {
        // Get values from UI fields and remove extra spaces
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();
        String confirmPassword = confirmPasswordField.getText().trim();

        // Check if any field is empty
        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "All fields are required.");
            return;
        }

        // Check if password and confirm password match
        if (!password.equals(confirmPassword)) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "Passwords do not match.");
            return;
        }

        try (Connection conn = DatabaseConnection.getConnection()) {

            // -----------------------------
            // Check if username already exists
            // -----------------------------
            String checkSql = "SELECT id FROM users WHERE username = ?";
            try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
                checkStmt.setString(1, username);
                ResultSet rs = checkStmt.executeQuery();
                if (rs.next()) { // If a row exists, username is taken
                    showAlert(Alert.AlertType.ERROR, "Registration Failed", "Username already exists.");
                    return;
                }
            }

            // -----------------------------
            // Insert new user into database
            // -----------------------------
            String insertSql = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";
            try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                insertStmt.setString(1, username);
                insertStmt.setString(2, password); // Optional: hash password for security
                insertStmt.setString(3, "tenant"); // Default role is tenant
                insertStmt.executeUpdate();        // Execute insert query
            }

            // Notify user of successful registration
            showAlert(Alert.AlertType.INFORMATION, "Success", "Registration successful! You can now login.");

            // -----------------------------
            // Redirect to login page
            // -----------------------------
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Login.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setTitle("Real Estate Management System - Login");
            stage.setScene(new Scene(root, 400, 300));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "An error occurred: " + e.getMessage());
        }
    }

    // -----------------------------
    // Go to login button action
    // -----------------------------
    @FXML
    private void handleGoToLogin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Login.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setTitle("Real Estate Management System - Login");
            stage.setScene(new Scene(root, 400, 300));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace(); // Print error if loading fails
        }
    }

    // -----------------------------
    // Utility method to show alerts
    // -----------------------------
    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);      // Set alert title
        alert.setHeaderText(null);  // No header
        alert.setContentText(content); // Alert content message
        alert.showAndWait();        // Show alert and wait for user
    }
}
