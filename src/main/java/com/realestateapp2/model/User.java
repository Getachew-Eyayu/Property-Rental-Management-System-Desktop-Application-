// User.java is the base class for all types of users in the system (Tenant, Landlord, Admin).
// It uses abstraction to define common fields and behavior, while letting subclasses implement specific permissions.

package com.realestateapp2.model;

/**
 * Abstract User class – represents a generic user in the system.
 * Subclasses (Tenant, Landlord, Admin) will inherit this and define their own permissions.
 */
public abstract class User {

    // -------------------------
    // Fields (Encapsulation)
    // -------------------------
    private int id;            // Unique ID of the user
    private String username;   // Username used for login
    private String fullName;   // Full name of the user
    private String role;       // Role: TENANT, LANDLORD, ADMIN

    // -------------------------
    // Constructor
    // -------------------------
    public User(int id, String username, String fullName, String role) {
        this.id = id;
        this.username = username;
        this.fullName = fullName;
        this.role = role;
    }

    // -------------------------
    // Getter methods (Encapsulation)
    // -------------------------
    public int getId() { return id; }
    public String getUsername() { return username; }
    public String getFullName() { return fullName; }
    public String getRole() { return role; }

    // -------------------------
    // Abstract method
    // -------------------------
    // Each subclass must implement this to define its permissions
    public abstract void showPermissions();
}
