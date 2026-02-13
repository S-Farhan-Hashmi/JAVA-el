package com.skyhigh.model;

public class Flight {

    private String flightId;
    private String source;
    private String destination;
    private String departureTime;
    private String arrivalTime;
    private double price;
    private int seatsAvailable;

    public Flight(String flightId, String source, String destination,
                  String departureTime, String arrivalTime,
                  double price, int seatsAvailable) {
        this.flightId = flightId;
        this.source = source;
        this.destination = destination;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.price = price;
        this.seatsAvailable = seatsAvailable;
    }

    public String getFlightId() { return flightId; }
    public String getSource() { return source; }
    public String getDestination() { return destination; }
    public String getDepartureTime() { return departureTime; }
    public String getArrivalTime() { return arrivalTime; }
    public double getPrice() { return price; }
    public int getSeatsAvailable() { return seatsAvailable; }
}
