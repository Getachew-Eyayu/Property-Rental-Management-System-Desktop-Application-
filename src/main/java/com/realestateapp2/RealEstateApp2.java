// RealEstateApp2.java
// Main entry point of the Real Estate Management System.
// Launches the JavaFX application and opens the login screen.

package com.realestateapp2;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class RealEstateApp2 extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        // -------------------------
        // Load the login UI first
        // -------------------------
        FXMLLoader loader = new FXMLLoader(
                RealEstateApp2.class.getResource("/com/realestate/view/login.fxml")
        );
        Parent root = loader.load(); // Load FXML layout

        // -------------------------
        // Set up the stage (window)
        // -------------------------
        stage.setTitle("Real Estate Management System - Login"); // Window title
        stage.setScene(new Scene(root, 400, 300));              // Set scene size for login
        stage.show();                                           // Show the window
    }

    public static void main(String[] args) {
        // Launch JavaFX application
        launch(args);
    }
}
