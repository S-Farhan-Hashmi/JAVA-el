package com.skyhigh.model;

public abstract class Booking {

    protected int userId;
    protected String flightId;

    public Booking(int userId, String flightId) {
        this.userId = userId;
        this.flightId = flightId;
    }

    public int getUserId() {
        return userId;
    }

    public String getFlightId() {
        return flightId;
    }

    public abstract double calculateFinalPrice(double basePrice);
}
