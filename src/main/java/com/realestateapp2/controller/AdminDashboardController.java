// This controller manages the Admin Dashboard of the real estate application.
// It handles admin actions like viewing users, managing properties,
// viewing reports, maintenance requests, and database interaction.

// Package name – tells Java where this class belongs
package com.realestateapp2.controller;

// Import database connection class
import com.realestateapp2.db.DatabaseConnection;

// JavaFX imports for UI components
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

// SQL imports for database operations
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

// Controller class for the Admin Dashboard screen
public class AdminDashboardController {

    // -----------------------------
    // Top bar UI elements
    // -----------------------------

    // Label that shows admin name (top-left)
    @FXML private Label adminLabel;

    // Label in the center top (shows current section name)
    @FXML private Label centerLabel;

    // -----------------------------
    // Center table (main table in dashboard)
    // -----------------------------

    // TableView to display users, properties, reports, etc.
    @FXML private TableView<Object> adminTable;

    // Table columns
    @FXML private TableColumn<Object, Integer> colId;
    @FXML private TableColumn<Object, String> colName;
    @FXML private TableColumn<Object, String> colEmail;
    @FXML private TableColumn<Object, String> colRole;

    // -----------------------------
    // Bottom status bar
    // -----------------------------

    // Label used to show status messages
    @FXML private Label statusLabel;

    // -----------------------------
    // Set admin name on top bar
    // -----------------------------

    // Called after login to display admin name
    public void setAdminName(String name) {
        if (adminLabel != null) {
            adminLabel.setText("Admin: " + name);
        }
    }

    // -----------------------------
    // Logout button action
    // -----------------------------

    // Handles logout and returns user to login screen
    @FXML
    private void handleLogout(ActionEvent event) {
        try {
            // Load login.fxml
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/login.fxml")
            );
            Parent root = loader.load();

            // Get current window (stage)
            Stage stage = (Stage) adminLabel.getScene().getWindow();

            // Set new scene
            stage.setTitle("Login");
            stage.setScene(new Scene(root, 600, 400));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            statusLabel.setText("Logout failed!");
        }
    }

    // -----------------------------
    // View Users button action
    // -----------------------------

    // Loads all users from database and displays them in table
    @FXML
    private void handleViewUsers(ActionEvent event) {

        // Update UI labels
        centerLabel.setText("All Users");
        statusLabel.setText("Viewing all users");

        // Set column titles
        colId.setText("ID");
        colName.setText("Username");
        colEmail.setText("Password");
        colRole.setText("Role");

        // Link table columns to User class getters
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("username"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("password"));
        colRole.setCellValueFactory(new PropertyValueFactory<>("role"));

        // List to hold users
        ObservableList<User> userList = FXCollections.observableArrayList();

        // SQL query
        String sql = "SELECT id, username, password, role FROM users";

        try (
                // Open database connection
                Connection conn = DatabaseConnection.getConnection();

                // Prepare SQL statement
                PreparedStatement stmt = conn.prepareStatement(sql);

                // Execute query
                ResultSet rs = stmt.executeQuery()
        ) {

            // Read each row from database
            while (rs.next()) {
                userList.add(new User(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("role")
                ));
            }

            // Show users in table
            adminTable.setItems(FXCollections.observableArrayList(userList));

        } catch (Exception e) {
            e.printStackTrace();
            statusLabel.setText("Failed to load users");
        }
    }

    // -----------------------------
    // Manage Properties button action
    // -----------------------------

    // Shows property list and opens add-property dialog
    @FXML
    private void handleManageProperties(ActionEvent event) {

        // Update labels
        centerLabel.setText("Manage Properties");
        statusLabel.setText("Managing properties");

        // Set column titles
        colId.setText("ID");
        colName.setText("Type");
        colEmail.setText("Address");
        colRole.setText("Status");

        // Link columns to Property class
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("propertyType"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("address"));
        colRole.setCellValueFactory(new PropertyValueFactory<>("status"));

        // Load properties into table
        loadProperties();

        // Show dialog to add new property
        showAddPropertyDialog();
    }

    // -----------------------------
    // Load properties from database
    // -----------------------------

    private void loadProperties() {

        // List to store properties
        ObservableList<Property> propertyList = FXCollections.observableArrayList();

        // SQL query
        String sql = "SELECT * FROM properties";

        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()
        ) {

            // Read each property from database
            while (rs.next()) {
                propertyList.add(new Property(
                        rs.getInt("id"),
                        rs.getString("property_type"),
                        rs.getString("address"),
                        rs.getDouble("rent_price"),
                        rs.getInt("num_rooms"),
                        rs.getString("status"),
                        rs.getInt("landlord_id")
                ));
            }

            // Display properties in table
            adminTable.setItems(FXCollections.observableArrayList(propertyList));

        } catch (Exception e) {
            e.printStackTrace();
            statusLabel.setText("Failed to load properties");
        }
    }

    // -----------------------------
    // Dialog to add a new property
    // -----------------------------

    private void showAddPropertyDialog() {

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Add New Property");

        GridPane grid = new GridPane();
        grid.setVgap(10);
        grid.setHgap(10);

        TextField typeField = new TextField();
        TextField addressField = new TextField();
        TextField rentPriceField = new TextField();
        TextField numRoomsField = new TextField();
        TextField statusField = new TextField();
        TextField landlordIdField = new TextField();

        grid.add(new Label("Type:"), 0, 0);        grid.add(typeField, 1, 0);
        grid.add(new Label("Address:"), 0, 1);     grid.add(addressField, 1, 1);
        grid.add(new Label("Rent Price:"), 0, 2);  grid.add(rentPriceField, 1, 2);
        grid.add(new Label("Num Rooms:"), 0, 3);   grid.add(numRoomsField, 1, 3);
        grid.add(new Label("Status:"), 0, 4);      grid.add(statusField, 1, 4);
        grid.add(new Label("Landlord ID:"), 0, 5); grid.add(landlordIdField, 1, 5);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.showAndWait().ifPresent(result -> {
            if (result == ButtonType.OK) {
                try {
                    String sql = "INSERT INTO properties (property_type, address, rent_price, num_rooms, status, landlord_id) VALUES (?, ?, ?, ?, ?, ?)";

                    try (
                            Connection conn = DatabaseConnection.getConnection();
                            PreparedStatement stmt = conn.prepareStatement(sql)
                    ) {

                        stmt.setString(1, typeField.getText());
                        stmt.setString(2, addressField.getText());
                        stmt.setDouble(3, Double.parseDouble(rentPriceField.getText()));
                        stmt.setInt(4, Integer.parseInt(numRoomsField.getText()));
                        stmt.setString(5, statusField.getText());
                        stmt.setInt(6, Integer.parseInt(landlordIdField.getText()));

                        stmt.executeUpdate();
                        loadProperties();
                        statusLabel.setText("Property added successfully");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    statusLabel.setText("Failed to add property");
                }
            }
        });
    }

    // -----------------------------
    // View Reports button action
    // -----------------------------

    @FXML
    private void handleViewReports(ActionEvent event) {

        centerLabel.setText("Reports");
        statusLabel.setText("Viewing reports");

        colId.setText("ID");
        colName.setText("Type");
        colEmail.setText("Status");
        colRole.setText("Rent Price");

        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("propertyType"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("status"));
        colRole.setCellValueFactory(new PropertyValueFactory<>("rentPrice"));

        ObservableList<Property> propertyList = FXCollections.observableArrayList();
        double totalProfit = 0;

        String sql = "SELECT * FROM properties";

        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()
        ) {

            while (rs.next()) {
                Property p = new Property(
                        rs.getInt("id"),
                        rs.getString("property_type"),
                        rs.getString("address"),
                        rs.getDouble("rent_price"),
                        rs.getInt("num_rooms"),
                        rs.getString("status"),
                        rs.getInt("landlord_id")
                );

                propertyList.add(p);

                if ("RENTED".equalsIgnoreCase(p.getStatus())) {
                    totalProfit += p.getRentPrice();
                }
            }

            adminTable.setItems(FXCollections.observableArrayList(propertyList));
            statusLabel.setText("Total Profit: $" + totalProfit);

        } catch (Exception e) {
            e.printStackTrace();
            statusLabel.setText("Failed to load reports");
        }
    }

    // -----------------------------
    // View Maintenance Requests
    // -----------------------------

    @FXML
    private void handleViewMaintenance(ActionEvent event) {

        centerLabel.setText("Maintenance Requests");
        statusLabel.setText("Viewing maintenance requests");

        colId.setText("ID");
        colName.setText("Property ID");
        colEmail.setText("Description");
        colRole.setText("Status");

        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("propertyId"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("description"));
        colRole.setCellValueFactory(new PropertyValueFactory<>("status"));

        ObservableList<MaintenanceRequest> list = FXCollections.observableArrayList();
        String sql = "SELECT id, property_id, description, status FROM maintenance_requests";

        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()
        ) {

            while (rs.next()) {
                list.add(new MaintenanceRequest(
                        rs.getInt("id"),
                        rs.getInt("property_id"),
                        rs.getString("description"),
                        rs.getString("status")
                ));
            }

            adminTable.setItems(FXCollections.observableArrayList(list));

        } catch (Exception e) {
            e.printStackTrace();
            statusLabel.setText("Failed to load maintenance requests");
        }
    }

    // -----------------------------
    // User model class
    // -----------------------------

    public static class User {
        private final Integer id;
        private final String username;
        private final String password;
        private final String role;

        public User(Integer id, String username, String password, String role) {
            this.id = id;
            this.username = username;
            this.password = password;
            this.role = role;
        }

        public Integer getId() { return id; }
        public String getUsername() { return username; }
        public String getPassword() { return password; }
        public String getRole() { return role; }
    }

    // -----------------------------
    // Property model class
    // -----------------------------

    public static class Property {
        private final Integer id;
        private final String propertyType;
        private final String address;
        private final Double rentPrice;
        private final Integer numRooms;
        private final String status;
        private final Integer landlordId;

        public Property(Integer id, String propertyType, String address, Double rentPrice,
                        Integer numRooms, String status, Integer landlordId) {
            this.id = id;
            this.propertyType = propertyType;
            this.address = address;
            this.rentPrice = rentPrice;
            this.numRooms = numRooms;
            this.status = status;
            this.landlordId = landlordId;
        }

        public Integer getId() { return id; }
        public String getPropertyType() { return propertyType; }
        public String getAddress() { return address; }
        public Double getRentPrice() { return rentPrice; }
        public Integer getNumRooms() { return numRooms; }
        public String getStatus() { return status; }
        public Integer getLandlordId() { return landlordId; }
    }

    // -----------------------------
    // MaintenanceRequest model class
    // -----------------------------

    public static class MaintenanceRequest {
        private final Integer id;
        private final Integer propertyId;
        private final String description;
        private final String status;

        public MaintenanceRequest(Integer id, Integer propertyId, String description, String status) {
            this.id = id;
            this.propertyId = propertyId;
            this.description = description;
            this.status = status;
        }

        public Integer getId() { return id; }
        public Integer getPropertyId() { return propertyId; }
        public String getDescription() { return description; }
        public String getStatus() { return status; }
    }
}
