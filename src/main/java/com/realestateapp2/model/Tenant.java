// Tenant.java represents a user of type TENANT in the system.
// Inherits from User and defines tenant-specific behavior like permissions.

package com.realestateapp2.model;

/**
 * Tenant class extends User to represent a tenant in the system.
 * Each tenant has specific permissions like searching, renting, and maintenance requests.
 */
public class Tenant extends User {

    // -------------------------
    // Constructor
    // -------------------------
    public Tenant(int id, String username, String fullName) {
        // Call User constructor with role "TENANT"
        super(id, username, fullName, "TENANT");
    }

    // -------------------------
    // Tenant-specific behavior
    // -------------------------
    @Override
    public void showPermissions() {
        // Print tenant permissions to console
        System.out.println("Search Properties, Rent, Maintenance Requests");
    }
}
