// PropertyDAO handles database operations for property records.
// It allows searching properties by type and price range, updating property status,
// and retrieving all properties from the database. Maps rows to specific property types.

package com.realestateapp2.dao;

import com.realestateapp2.model.House;
import com.realestateapp2.model.Apartment;
import com.realestateapp2.model.Villa;
import com.realestateapp2.db.DatabaseConnection;
import com.realestateapp2.model.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PropertyDAO {

    // -----------------------------
    // Search properties with optional filters
    // -----------------------------
    public List<Property> searchProperties(String type, Double minPrice, Double maxPrice) throws SQLException {
        List<Property> list = new ArrayList<>();

        // Build SQL query dynamically based on filters
        StringBuilder sql = new StringBuilder("SELECT * FROM properties WHERE 1=1");
        if (type != null && !type.equals("ALL")) sql.append(" AND property_type = ?");
        if (minPrice != null) sql.append(" AND rent_price >= ?");
        if (maxPrice != null) sql.append(" AND rent_price <= ?");

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {

            // Set parameter values dynamically
            int idx = 1;
            if (type != null && !type.equals("ALL")) pstmt.setString(idx++, type);
            if (minPrice != null) pstmt.setDouble(idx++, minPrice);
            if (maxPrice != null) pstmt.setDouble(idx++, maxPrice);

            // Execute query and map results
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                list.add(map(rs));
            }
        }

        return list; // Return list of matching properties
    }

    // -----------------------------
    // Update property status
    // -----------------------------
    public void updatePropertyStatus(int id, String status) throws SQLException {
        String sql = "UPDATE properties SET status = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, status); // Set new status (e.g., "RENTED")
            pstmt.setInt(2, id);         // Set property ID
            pstmt.executeUpdate();       // Execute update
        }
    }

    // -----------------------------
    // Map a ResultSet row to a Property object
    // -----------------------------
    private Property map(ResultSet rs) throws SQLException {
        String type = rs.getString("property_type"); // Property type
        int id = rs.getInt("id");
        String addr = rs.getString("address");
        double rent = rs.getDouble("rent_price");
        int rooms = rs.getInt("num_rooms");
        String status = rs.getString("status");
        int lId = rs.getInt("landlord_id");

        // Return specific subclass based on type
        if ("HOUSE".equals(type)) return new House(id, addr, rent, rooms, status, lId);
        if ("APARTMENT".equals(type)) return new Apartment(id, addr, rent, rooms, status, lId);
        return new Villa(id, addr, rent, rooms, status, lId);
    }

    // -----------------------------
    // Retrieve all properties from the database
    // -----------------------------
    public List<Property> getAllProperties() throws SQLException {
        List<Property> list = new ArrayList<>();
        String sql = "SELECT * FROM properties";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                list.add(map(rs)); // Map each row to Property object
            }
        }

        return list; // Return list of all properties
    }
}
