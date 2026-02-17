package com.skyhigh.model;

public abstract class Booking {

    protected int userId;
    protected String flightId;
    protected String classType;

    public Booking(int userId, String flightId, String classType) {
        this.userId = userId;
        this.flightId = flightId;
        this.classType = classType;
    }

    public int getUserId() {
        return userId;
    }

    public String getFlightId() {
        return flightId;
    }

    public String getClassType() {
        return classType;
    }

    public abstract double calculateFinalPrice(double basePrice);
}
