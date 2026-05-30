// Apartment class represents a property of type APARTMENT.
// It extends the general Property class and overrides the property type.

package com.realestateapp2.model;

// Apartment is a specific type of Property
public class Apartment extends Property {

    // Constructor to initialize all fields inherited from Property
    public Apartment(int id, String address, double rentPrice, int rooms, String status, int landlordId) {
        super(id, address, rentPrice, rooms, status, landlordId); // Call superclass constructor
    }

    // Override getPropertyType to return "APARTMENT"
    @Override
    public String getPropertyType() {
        return "APARTMENT";
    }
}
