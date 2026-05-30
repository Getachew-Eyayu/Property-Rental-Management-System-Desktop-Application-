// DashboardController manages the main user dashboard for the real estate app.
// It allows searching for properties, renting a property, submitting maintenance requests,
// and handles user logout and interaction with DAOs and services.

// Package name – tells Java where this class belongs
package com.realestateapp2.controller;

import com.realestateapp2.dao.*;
import com.realestateapp2.model.Property;
import com.realestateapp2.service.RentCalculator;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.sql.SQLException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class DashboardController {
    @FXML private ListView<Property> propertyList;
    @FXML private ComboBox<String> typeFilter;
    @FXML private TextField minPriceField, maxPriceField;
    @FXML private Label statusLabel, userLabel; // <-- Top bar user label

    private PropertyDAO propertyDAO;

    {
        propertyDAO = new PropertyDAO();
    }

    private LeaseDAO leaseDAO = new LeaseDAO();
    private MaintenanceDAO maintenanceDAO;

    private int currentUserId; // Will be set dynamically

    public DashboardController() {
        maintenanceDAO = new MaintenanceDAO();
    }

    @FXML
    public void initialize() {
        typeFilter.setItems(FXCollections.observableArrayList("ALL", "HOUSE", "APARTMENT", "VILLA"));
        typeFilter.setValue("ALL");
        handleSearch();
    }

    // -------------------------
    // NEW METHOD: Setter for LoginController
    // -------------------------
    public void setUserName(String username, int userId) {
        userLabel.setText("User: " + username);
        this.currentUserId = userId; // store ID for lease/maintenance actions
    }

    @FXML
    private void handleSearch() {
        try {
            Double min = minPriceField.getText().isEmpty() ? null : Double.parseDouble(minPriceField.getText());
            Double max = maxPriceField.getText().isEmpty() ? null : Double.parseDouble(maxPriceField.getText());
            propertyList.setItems(FXCollections.observableArrayList(
                    propertyDAO.searchProperties(typeFilter.getValue(), min, max)
            ));
        } catch (Exception e) {
            showAlert("Error", "Invalid input for price filters.");
        }
    }

    @FXML
    private void handleRent() {
        Property selected = propertyList.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Select Property", "Please select a property from the list.");
            return;
        }
        if ("RENTED".equals(selected.getStatus())) {
            showAlert("Unavailable", "This property is already occupied.");
            return;
        }

        double total = RentCalculator.calculateInitialTotal(selected.getRentPrice());

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Lease Agreement");
        confirm.setHeaderText("Total Due Now: $" + total);
        confirm.setContentText("By clicking OK, you sign the digital lease and rent this property.");

        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    leaseDAO.createLease(selected.getId(), currentUserId, total);
                    showAlert("Success", "Lease Signed! The property is now yours.");
                    handleSearch();
                } catch (SQLException e) {
                    showAlert("DB Error", e.getMessage());
                }
            }
        });
    }

    @FXML
    private void handleSubmitMaintenance() {
        Property selected = propertyList.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Maintenance Request");
        dialog.setHeaderText("Describe the issue for: " + selected.getAddress());
        dialog.showAndWait().ifPresent(desc -> {
            try {
                maintenanceDAO.createRequest(selected.getId(), currentUserId, desc);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            showAlert("Sent", "Landlord has been notified.");
        });
    }

    @FXML
    private void handleLogout() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/login.fxml")
            );
            Parent root = loader.load();

            Stage stage = (Stage) userLabel.getScene().getWindow();
            stage.setTitle("Login");
            stage.setScene(new Scene(root, 600, 400));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Unable to logout.");
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.show();
    }
}


