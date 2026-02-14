package com.skyhigh.model;

public class Booking {

    private int userId;
    private String flightId;
    private String classType;

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
}
