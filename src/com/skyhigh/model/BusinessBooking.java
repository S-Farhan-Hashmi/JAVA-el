package com.skyhigh.model;

public class BusinessBooking extends Booking {

    public BusinessBooking(int userId, String flightId) {
        super(userId, flightId);
    }

    @Override
    public double calculateFinalPrice(double basePrice) {
        return basePrice * 1.5;
    }
}
