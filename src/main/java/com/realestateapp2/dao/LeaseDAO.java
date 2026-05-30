// LeaseDAO handles database operations related to leases.
// It creates a new lease record for a property and tenant,
// sets the lease duration, total amount, and updates property status to "RENTED".

package com.realestateapp2.dao;

import com.realestateapp2.db.DatabaseConnection;
import java.sql.*;
import java.time.LocalDate;

public class LeaseDAO {

    // -----------------------------
    // Create a new lease
    // -----------------------------
    public void createLease(int propertyId, int tenantId, double amount) throws SQLException {
        // SQL insert statement for leases table
        String sql = "INSERT INTO leases (property_id, tenant_id, start_date, end_date, total_amount) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, propertyId);                   // Set property ID
            pstmt.setInt(2, tenantId);                     // Set tenant ID
            pstmt.setDate(3, Date.valueOf(LocalDate.now()));       // Start date: today
            pstmt.setDate(4, Date.valueOf(LocalDate.now().plusYears(1))); // End date: 1 year from today
            pstmt.setDouble(5, amount);                    // Total lease amount

            pstmt.executeUpdate(); // Execute insert query

            // AUTOMATION: Update the property status to "RENTED" after lease creation
            new PropertyDAO().updatePropertyStatus(propertyId, "RENTED");
        }
    }
}
