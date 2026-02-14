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

    public String getFlightId() { return flightId; }
    public String getSource() { return source; }
    public String getDestination() { return destination; }
    public String getDepartureTime() { return departureTime; }
    public String getArrivalTime() { return arrivalTime; }

    public double getEconomyPrice() { return economyPrice; }
    public double getBusinessPrice() { return businessPrice; }

    public int getEconomySeats() { return economySeats; }
    public int getBusinessSeats() { return businessSeats; }
}
