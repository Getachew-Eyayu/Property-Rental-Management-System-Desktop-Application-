// Villa.java represents a property of type VILLA.
// It extends the base Property class and specifies its type.

package com.realestateapp2.model;

/**
 * Villa is a specific type of Property.
 * Inherits common fields and methods from Property and defines its type as "VILLA".
 */
public class Villa extends Property {

    // -------------------------
    // Constructor
    // -------------------------
    // Initializes a Villa with all necessary details
    public Villa(int id, String address, double rentPrice, int rooms, String status, int landlordId) {
        super(id, address, rentPrice, rooms, status, landlordId); // Call the Property constructor
    }

    // -------------------------
    // Override method
    // -------------------------
    // Returns the property type as "VILLA"
    @Override
    public String getPropertyType() {
        return "VILLA";
    }
}
