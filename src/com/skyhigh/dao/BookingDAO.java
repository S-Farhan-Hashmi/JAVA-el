package com.skyhigh.dao;

import com.skyhigh.db.DBConnection;
import com.skyhigh.exception.SeatUnavailableException;
import com.skyhigh.model.Booking;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.skyhigh.model.Flight;

public class BookingDAO {

    public boolean bookFlight(Booking booking) throws SeatUnavailableException {

        String checkQuery = "SELECT seats_available, price FROM flights WHERE flight_id = ?";
        String updateQuery = "UPDATE flights SET seats_available = seats_available - 1 WHERE flight_id = ?";
        String insertQuery = "INSERT INTO bookings (user_id, flight_id, class_type) VALUES (?, ?, ?)";

        try (Connection conn = DBConnection.getConnection()) {

            conn.setAutoCommit(false); // Start transaction

            // 1️⃣ Check seats
            PreparedStatement checkStmt = conn.prepareStatement(checkQuery);
            checkStmt.setString(1, booking.getFlightId());
            ResultSet rs = checkStmt.executeQuery();

            if (!rs.next()) {
                conn.rollback();
                return false;
            }

            int seats = rs.getInt("seats_available");
            double basePrice = rs.getDouble("price");

            if (seats <= 0) {
                conn.rollback();
                throw new SeatUnavailableException("No seats available for this flight.");
            }

            // 2️⃣ Deduct seat
            PreparedStatement updateStmt = conn.prepareStatement(updateQuery);
            updateStmt.setString(1, booking.getFlightId());
            updateStmt.executeUpdate();

            // 3️⃣ Insert booking
            PreparedStatement insertStmt = conn.prepareStatement(insertQuery);
            insertStmt.setInt(1, booking.getUserId());
            insertStmt.setString(2, booking.getFlightId());
            insertStmt.setString(3, booking.getClassType());
            insertStmt.executeUpdate();

            conn.commit(); // Commit transaction

            double finalPrice = booking.calculateFinalPrice(basePrice);
            System.out.println("Booking successful. Final Price: " + finalPrice);

            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean cancelBooking(int bookingId) {

        String getFlightQuery = "SELECT flight_id FROM bookings WHERE booking_id = ?";
        String deleteQuery = "DELETE FROM bookings WHERE booking_id = ?";
        String restoreSeatQuery = "UPDATE flights SET seats_available = seats_available + 1 WHERE flight_id = ?";

        try (Connection conn = DBConnection.getConnection()) {

            conn.setAutoCommit(false);

            // 1️⃣ Get flight_id
            PreparedStatement getStmt = conn.prepareStatement(getFlightQuery);
            getStmt.setInt(1, bookingId);
            ResultSet rs = getStmt.executeQuery();

            if (!rs.next()) {
                conn.rollback();
                return false;
            }

            String flightId = rs.getString("flight_id");

            // 2️⃣ Delete booking
            PreparedStatement deleteStmt = conn.prepareStatement(deleteQuery);
            deleteStmt.setInt(1, bookingId);
            deleteStmt.executeUpdate();

            // 3️⃣ Restore seat
            PreparedStatement restoreStmt = conn.prepareStatement(restoreSeatQuery);
            restoreStmt.setString(1, flightId);
            restoreStmt.executeUpdate();

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
                SELECT b.booking_id, b.flight_id, b.class_type, f.source, f.destination
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
