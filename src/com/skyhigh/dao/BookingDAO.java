package com.skyhigh.dao;

import com.skyhigh.db.DBConnection;
import com.skyhigh.exception.SeatUnavailableException;
import com.skyhigh.model.Booking;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookingDAO {

    public boolean bookFlight(Booking booking) throws SeatUnavailableException {

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
                conn.rollback();
                return false;
            }

            int ecoSeats = rs.getInt("economy_seats");
            int busSeats = rs.getInt("business_seats");

            boolean isEconomy = booking.getClassType().equalsIgnoreCase("Economy");

            if (isEconomy) {
                if (ecoSeats <= 0) {
                    conn.rollback();
                    throw new SeatUnavailableException("No Economy seats available.");
                }

                PreparedStatement updateStmt = conn.prepareStatement(updateEconomy);
                updateStmt.setString(1, booking.getFlightId());
                updateStmt.executeUpdate();

            } else {
                if (busSeats <= 0) {
                    conn.rollback();
                    throw new SeatUnavailableException("No Business seats available.");
                }

                PreparedStatement updateStmt = conn.prepareStatement(updateBusiness);
                updateStmt.setString(1, booking.getFlightId());
                updateStmt.executeUpdate();
            }

            PreparedStatement insertStmt = conn.prepareStatement(insertQuery);
            insertStmt.setInt(1, booking.getUserId());
            insertStmt.setString(2, booking.getFlightId());
            insertStmt.setString(3, booking.getClassType());
            insertStmt.executeUpdate();

            conn.commit();
            return true;

        } catch (SQLException e) {
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

        try (Connection conn = DBConnection.getConnection()) {

            conn.setAutoCommit(false);

            PreparedStatement getStmt = conn.prepareStatement(getQuery);
            getStmt.setInt(1, bookingId);
            ResultSet rs = getStmt.executeQuery();

            if (!rs.next()) {
                conn.rollback();
                return false;
            }

            String flightId = rs.getString("flight_id");
            String classType = rs.getString("class_type");

            PreparedStatement deleteStmt = conn.prepareStatement(deleteQuery);
            deleteStmt.setInt(1, bookingId);
            deleteStmt.executeUpdate();

            if (classType.equalsIgnoreCase("Economy")) {
                PreparedStatement restoreStmt = conn.prepareStatement(restoreEconomy);
                restoreStmt.setString(1, flightId);
                restoreStmt.executeUpdate();
            } else {
                PreparedStatement restoreStmt = conn.prepareStatement(restoreBusiness);
                restoreStmt.setString(1, flightId);
                restoreStmt.executeUpdate();
            }

            conn.commit();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
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
                bookings.add(new String[]{
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
