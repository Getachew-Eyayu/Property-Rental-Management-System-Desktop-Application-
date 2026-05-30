// House class represents a property of type HOUSE.
// It extends the general Property class and specifies its type.

package com.realestateapp2.model;

// House is a specific type of Property
public class House extends Property {

    // Constructor to initialize all fields inherited from Property
    public House(int id, String address, double rentPrice, int rooms, String status, int landlordId) {
        super(id, address, rentPrice, rooms, status, landlordId); // Call superclass constructor
    }

    // Override getPropertyType to return "HOUSE"
    @Override
    public String getPropertyType() {
        return "HOUSE";
    }
}
