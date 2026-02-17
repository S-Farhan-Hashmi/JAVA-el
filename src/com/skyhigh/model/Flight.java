package com.skyhigh.model;

public class Flight {

    private String flightId;
    private String source;
    private String destination;
    private String departureTime;
    private String arrivalTime;
    private double economyPrice;
    private double businessPrice;
    private int economySeats;
    private int businessSeats;

    // Constructor with economy and business pricing (used by UserDashboard)
    public Flight(String flightId, String source, String destination,
            String departureTime, String arrivalTime,
            double economyPrice, double businessPrice,
            int economySeats, int businessSeats) {
        this.flightId = flightId;
        this.source = source;
        this.destination = destination;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.economyPrice = economyPrice;
        this.businessPrice = businessPrice;
        this.economySeats = economySeats;
        this.businessSeats = businessSeats;
    }

    // Legacy constructor for backward compatibility (single price)
    public Flight(String flightId, String source, String destination,
            String departureTime, String arrivalTime,
            double price, int seatsAvailable) {
        this.flightId = flightId;
        this.source = source;
        this.destination = destination;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.economyPrice = price;
        this.businessPrice = price * 1.5;
        this.economySeats = seatsAvailable;
        this.businessSeats = (int) (seatsAvailable * 0.3);
    }

    public String getFlightId() {
        return flightId;
    }

    public String getSource() {
        return source;
    }

    public String getDestination() {
        return destination;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    // New getters for economy/business
    public double getEconomyPrice() {
        return economyPrice;
    }

    public double getBusinessPrice() {
        return businessPrice;
    }

    public int getEconomySeats() {
        return economySeats;
    }

    public int getBusinessSeats() {
        return businessSeats;
    }

    // Legacy getters for backward compatibility
    public double getPrice() {
        return economyPrice;
    }

    public int getSeatsAvailable() {
        return economySeats + businessSeats;
    }

    // Setters for updating seats after booking
    public void setEconomySeats(int economySeats) {
        this.economySeats = economySeats;
    }

    public void setBusinessSeats(int businessSeats) {
        this.businessSeats = businessSeats;
    }
}
