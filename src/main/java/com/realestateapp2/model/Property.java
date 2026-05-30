// Property.java is the abstract base class for all types of properties (House, Apartment, Villa).
// It uses Polymorphism and Abstraction so that each specific property type can define its own type.
// Encapsulation is used to protect property fields and provide getters/setters.

package com.realestateapp2.model;

/**
 * Abstract base class representing a generic property.
 * Contains common fields and behavior for all property types.
 */
public abstract class Property {

    // -------------------------
    // Fields (Encapsulation)
    // -------------------------
    private int id;             // Unique ID of the property
    private String address;     // Address of the property
    private double rentPrice;   // Rent price of the property
    private int rooms;          // Number of rooms
    private String status;      // Status: AVAILABLE or RENTED
    private int landlordId;     // ID of the landlord who owns this property

    // -------------------------
    // Constructor
    // -------------------------
    public Property(int id, String address, double rentPrice, int rooms, String status, int landlordId) {
        this.id = id;
        this.address = address;
        this.rentPrice = rentPrice;
        this.rooms = rooms;
        this.status = status;
        this.landlordId = landlordId;
    }

    // -------------------------
    // Getters (Encapsulation)
    // -------------------------
    public int getId() { return id; }
    public String getAddress() { return address; }
    public double getRentPrice() { return rentPrice; }
    public int getRooms() { return rooms; }
    public String getStatus() { return status; }
    public int getLandlordId() { return landlordId; }

    // -------------------------
    // Setter for status
    // -------------------------
    public void setStatus(String status) { this.status = status; }

    // -------------------------
    // Abstract method for type
    // -------------------------
    // Each subclass (House, Apartment, Villa) must implement this
    public abstract String getPropertyType();

    // -------------------------
    // ToString override for easy display in lists
    // -------------------------
    @Override
    public String toString() {
        return String.format("%s - %s ($%.2f)", getPropertyType(), address, rentPrice);
    }
}
