package com.realestateapp2.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import com.realestateapp2.db.DatabaseConnection;

public class MaintenanceDAO {

    public void createRequest(int propId, int tenantId, String desc) throws SQLException {

        String sql = "INSERT INTO maintenance_requests (property_id, tenant_id, description) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, propId);
            pstmt.setInt(2, tenantId);
            pstmt.setString(3, desc);

            pstmt.executeUpdate();
        }
    }
}
