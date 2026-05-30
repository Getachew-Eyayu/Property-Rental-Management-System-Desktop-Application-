// RentCalculator.java calculates the initial payment required to rent a property.
// It includes the monthly rent, a security deposit, and administrative fees.

package com.realestateapp2.service;

import com.realestateapp2.model.Property;

public class RentCalculator {

    // -------------------------
    // Constants
    // -------------------------
    private static final double SECURITY_DEPOSIT_MULTIPLIER = 1.0; // Deposit equals one month of rent
    private static final double ADMIN_FEE = 150.0;                  // Fixed admin fee for renting

    // -------------------------
    // Calculate total initial payment for a given property
    // -------------------------
    public static double calculateTotalInitialPayment(Property property) {
        double monthlyRent = property.getRentPrice(); // Get the rent of the property
        // Total = rent + security deposit + admin fee
        return monthlyRent + (monthlyRent * SECURITY_DEPOSIT_MULTIPLIER) + ADMIN_FEE;
    }

    // -------------------------
    // Optional: Calculate total from just a monthly rent (for backward compatibility)
    // -------------------------
    public static double calculateInitialTotal(double monthlyRent) {
        return monthlyRent + (monthlyRent * SECURITY_DEPOSIT_MULTIPLIER) + ADMIN_FEE;
    }
}
