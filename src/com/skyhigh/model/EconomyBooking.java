package com.skyhigh.model;

public class EconomyBooking extends Booking {

    public EconomyBooking(int userId, String flightId) {
        super(userId, flightId, "Economy");
    }

    @Override
    public double calculateFinalPrice(double basePrice) {
        return basePrice;
    }
}
