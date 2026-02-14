package com.skyhigh.dao;

import com.skyhigh.db.DBConnection;
import com.skyhigh.exception.SeatUnavailableException;
import com.skyhigh.model.Booking;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookingDAO {

    public boolean bookFlight(Booking booking) throws SeatUnavailableException {

        System.out.println("======= BOOKING FLIGHT =======");
        System.out.println("Flight ID: " + booking.getFlightId());
        System.out.println("Class Type: " + booking.getClassType());
        System.out.println("User ID: " + booking.getUserId());

        String selectQuery = """
                SELECT economy_seats, business_seats,
                       economy_price, business_price
                FROM flights
                WHERE flight_id = ?
                """;

        String updateEconomy = """
                UPDATE flights
                SET economy_seats = economy_seats - 1
                WHERE flight_id = ?
                """;

        String updateBusiness = """
                UPDATE flights
                SET business_seats = business_seats - 1
                WHERE flight_id = ?
                """;

        String insertQuery = """
                INSERT INTO bookings (user_id, flight_id, class_type)
                VALUES (?, ?, ?)
                """;

        try (Connection conn = DBConnection.getConnection()) {

            conn.setAutoCommit(false);

            PreparedStatement selectStmt = conn.prepareStatement(selectQuery);
            selectStmt.setString(1, booking.getFlightId());
            ResultSet rs = selectStmt.executeQuery();

            if (!rs.next()) {
                System.err.println("Flight not found!");
                conn.rollback();
                return false;
            }

            int ecoSeats = rs.getInt("economy_seats");
            int busSeats = rs.getInt("business_seats");

            System.out.println("Current Economy Seats: " + ecoSeats);
            System.out.println("Current Business Seats: " + busSeats);

            boolean isEconomy = booking.getClassType().equalsIgnoreCase("Economy");
            System.out.println("Is Economy? " + isEconomy);

            if (isEconomy) {
                if (ecoSeats <= 0) {
                    System.err.println("No Economy seats available!");
                    conn.rollback();
                    throw new SeatUnavailableException("No Economy seats available.");
                }

                System.out.println("Reducing Economy seats by 1...");
                PreparedStatement updateStmt = conn.prepareStatement(updateEconomy);
                updateStmt.setString(1, booking.getFlightId());
                int updated = updateStmt.executeUpdate();
                System.out.println("Economy seats updated: " + updated + " row(s)");

            } else {
                if (busSeats <= 0) {
                    System.err.println("No Business seats available!");
                    conn.rollback();
                    throw new SeatUnavailableException("No Business seats available.");
                }

                System.out.println("Reducing Business seats by 1...");
                PreparedStatement updateStmt = conn.prepareStatement(updateBusiness);
                updateStmt.setString(1, booking.getFlightId());
                int updated = updateStmt.executeUpdate();
                System.out.println("Business seats updated: " + updated + " row(s)");
            }

            PreparedStatement insertStmt = conn.prepareStatement(insertQuery);
            insertStmt.setInt(1, booking.getUserId());
            insertStmt.setString(2, booking.getFlightId());
            insertStmt.setString(3, booking.getClassType());
            insertStmt.executeUpdate();
            System.out.println("Booking record inserted");

            conn.commit();
            System.out.println("Transaction committed successfully!");
            System.out.println("======= BOOKING COMPLETE =======");
            return true;

        } catch (SQLException e) {
            System.err.println("SQL Error during booking:");
            e.printStackTrace();
            return false;
        }
    }

    public boolean cancelBooking(int bookingId) {

        String getQuery = """
                SELECT flight_id, class_type
                FROM bookings
                WHERE booking_id = ?
                """;

        String deleteQuery = """
                DELETE FROM bookings
                WHERE booking_id = ?
                """;

        String restoreEconomy = """
                UPDATE flights
                SET economy_seats = economy_seats + 1
                WHERE flight_id = ?
                """;

        String restoreBusiness = """
                UPDATE flights
                SET business_seats = business_seats + 1
                WHERE flight_id = ?
                """;

        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);

            System.out.println("Canceling booking ID: " + bookingId);

            PreparedStatement getStmt = conn.prepareStatement(getQuery);
            getStmt.setInt(1, bookingId);
            ResultSet rs = getStmt.executeQuery();

            if (!rs.next()) {
                System.err.println("Booking not found: " + bookingId);
                conn.rollback();
                return false;
            }

            String flightId = rs.getString("flight_id");
            String classType = rs.getString("class_type");
            System.out.println("Found booking - Flight: " + flightId + ", Class: " + classType);

            PreparedStatement deleteStmt = conn.prepareStatement(deleteQuery);
            deleteStmt.setInt(1, bookingId);
            int deletedRows = deleteStmt.executeUpdate();
            System.out.println("Deleted booking rows: " + deletedRows);

            PreparedStatement restoreStmt;
            if (classType.equalsIgnoreCase("Economy")) {
                restoreStmt = conn.prepareStatement(restoreEconomy);
                restoreStmt.setString(1, flightId);
                int updatedRows = restoreStmt.executeUpdate();
                System.out.println("Restored " + updatedRows + " Economy seat(s) to flight " + flightId);
            } else {
                restoreStmt = conn.prepareStatement(restoreBusiness);
                restoreStmt.setString(1, flightId);
                int updatedRows = restoreStmt.executeUpdate();
                System.out.println("Restored " + updatedRows + " Business seat(s) to flight " + flightId);
            }

            conn.commit();
            System.out.println("Booking cancelled successfully!");
            return true;

        } catch (SQLException e) {
            System.err.println("Error canceling booking:");
            System.err.println("Error Code: " + e.getErrorCode());
            System.err.println("SQL State: " + e.getSQLState());
            System.err.println("Message: " + e.getMessage());
            e.printStackTrace();

            if (conn != null) {
                try {
                    conn.rollback();
                    System.err.println("Transaction rolled back");
                } catch (SQLException rollbackEx) {
                    System.err.println("Rollback failed: " + rollbackEx.getMessage());
                }
            }
            return false;
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public List<String[]> getBookingsByUser(int userId) {

        List<String[]> bookings = new ArrayList<>();

        String query = """
                SELECT b.booking_id,
                       b.flight_id,
                       b.class_type,
                       f.source,
                       f.destination
                FROM bookings b
                JOIN flights f ON b.flight_id = f.flight_id
                WHERE b.user_id = ?
                """;

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                bookings.add(new String[] {
                        rs.getString("booking_id"),
                        rs.getString("flight_id"),
                        rs.getString("source"),
                        rs.getString("destination"),
                        rs.getString("class_type")
                });
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return bookings;
    }
}
