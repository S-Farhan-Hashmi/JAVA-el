package com.skyhigh.dao;

import com.skyhigh.db.DBConnection;
import com.skyhigh.model.Flight;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FlightDAO {

    // ADD FLIGHT
    public boolean addFlight(Flight flight) {

        String query = """
        INSERT INTO flights 
        (flight_id, source, destination, departure_time, arrival_time,
         economy_price, business_price, economy_seats, business_seats)
        VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, flight.getFlightId());
            stmt.setString(2, flight.getSource());
            stmt.setString(3, flight.getDestination());
            stmt.setString(4, flight.getDepartureTime());
            stmt.setString(5, flight.getArrivalTime());
            stmt.setDouble(6, flight.getEconomyPrice());
            stmt.setDouble(7, flight.getBusinessPrice());
            stmt.setInt(8, flight.getEconomySeats());
            stmt.setInt(9, flight.getBusinessSeats());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // GET ALL FLIGHTS
    public List<Flight> getAllFlights() {

        List<Flight> flights = new ArrayList<>();
        String query = "SELECT * FROM flights";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {

                flights.add(new Flight(
                        rs.getString("flight_id"),
                        rs.getString("source"),
                        rs.getString("destination"),
                        rs.getString("departure_time"),
                        rs.getString("arrival_time"),
                        rs.getDouble("economy_price"),
                        rs.getDouble("business_price"),
                        rs.getInt("economy_seats"),
                        rs.getInt("business_seats")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return flights;
    }

    // GET FLIGHT BY ID
    public Flight getFlightById(String id) {

        String query = "SELECT * FROM flights WHERE flight_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {

                return new Flight(
                        rs.getString("flight_id"),
                        rs.getString("source"),
                        rs.getString("destination"),
                        rs.getString("departure_time"),
                        rs.getString("arrival_time"),
                        rs.getDouble("economy_price"),
                        rs.getDouble("business_price"),
                        rs.getInt("economy_seats"),
                        rs.getInt("business_seats")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    // SEARCH FLIGHTS BY SOURCE & DESTINATION
    public List<Flight> searchFlights(String source, String destination) {

        List<Flight> flights = new ArrayList<>();

        String query = """
        SELECT * FROM flights 
        WHERE source = ? AND destination = ?
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, source);
            stmt.setString(2, destination);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {

                flights.add(new Flight(
                        rs.getString("flight_id"),
                        rs.getString("source"),
                        rs.getString("destination"),
                        rs.getString("departure_time"),
                        rs.getString("arrival_time"),
                        rs.getDouble("economy_price"),
                        rs.getDouble("business_price"),
                        rs.getInt("economy_seats"),
                        rs.getInt("business_seats")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return flights;
    }

    // DELETE FLIGHT
    public boolean deleteFlight(String flightId) {

        String deleteBookings = "DELETE FROM bookings WHERE flight_id = ?";
        String deleteFlight = "DELETE FROM flights WHERE flight_id = ?";

        try (Connection conn = DBConnection.getConnection()) {

            conn.setAutoCommit(false);

            try (
                    PreparedStatement stmt1 = conn.prepareStatement(deleteBookings);
                    PreparedStatement stmt2 = conn.prepareStatement(deleteFlight)
            ) {

                stmt1.setString(1, flightId);
                stmt1.executeUpdate();

                stmt2.setString(1, flightId);
                int rows = stmt2.executeUpdate();

                conn.commit();
                return rows > 0;

            } catch (SQLException e) {
                conn.rollback();
                e.printStackTrace();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
}
