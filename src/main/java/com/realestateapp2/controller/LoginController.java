// LoginController handles user login functionality for the real estate app.
// It validates username and password, checks credentials against the database,
// and navigates users to the appropriate dashboard based on their role (admin or tenant).

// Package declaration – tells Java where this class belongs
package com.realestateapp2.controller;

import com.realestateapp2.db.DatabaseConnection;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private void handleLogin() {
        String user = usernameField.getText().trim();
        String pass = passwordField.getText().trim();

        if (user.isEmpty() || pass.isEmpty()) {
            showAlert(Alert.AlertType.WARNING,
                    "Validation Error",
                    "Username and Password cannot be empty.");
            return;
        }

        String sql = "SELECT id, username, role FROM users WHERE username = ? AND password = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, user);
            pstmt.setString(2, pass);

            ResultSet rs = pstmt.executeQuery();

            if (!rs.next()) {
                showAlert(Alert.AlertType.ERROR,
                        "Login Failed",
                        "Invalid username or password.");
                return;
            }

            // ✅ READ ALL VALUES FIRST
            int userId = rs.getInt("id");
            String username = rs.getString("username");
            String role = rs.getString("role");

            if (role == null) {
                role = "tenant";
            }

            // ✅ SELECT DASHBOARD
            String fxmlPath;
            if (role.equalsIgnoreCase("admin")) {
                fxmlPath = "/admin_dashboard.fxml";
            } else {
                fxmlPath = "/dashboard.fxml";
            }

            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();

            // ✅ PASS DATA TO CONTROLLER
            if (role.equalsIgnoreCase("admin")) {
                AdminDashboardController adminController = loader.getController();
                adminController.setAdminName(username);
            } else {
                DashboardController tenantController = loader.getController();
                tenantController.setUserName(username, userId);
            }

            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setTitle("Real Estate Management System - " + role);
            stage.setScene(new Scene(root, 900, 650));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR,
                    "Error",
                    "An error occurred: " + e.getMessage());
        }
    }

    @FXML
    private void handleGoToRegister() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/register.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setTitle("Real Estate Management System - Register");
            stage.setScene(new Scene(root, 400, 300));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
