package com.skyhigh.ui;

import com.skyhigh.dao.BookingDAO;
import com.skyhigh.dao.FlightDAO;
import com.skyhigh.exception.SeatUnavailableException;
import com.skyhigh.model.*;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class UserDashboard extends JFrame {

    private FlightDAO flightDAO;
    private User loggedInUser;

    private JTextField idField;
    private JTextField sourceField;
    private JTextField destinationField;
    private JPanel resultPanel;

    public UserDashboard(User user) {

        this.loggedInUser = user;
        this.flightDAO = new FlightDAO();

        setTitle("SkyHigh Airlines - User Dashboard");
        setSize(1150, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        getContentPane().setBackground(new Color(20, 25, 40));

        add(createHeader(), BorderLayout.NORTH);
        add(createCenterPanel(), BorderLayout.CENTER);

        setVisible(true);
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(15, 20, 35));
        header.setPreferredSize(new Dimension(1000, 70));

        JLabel title = new JLabel("  SkyHigh Airlines");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));

        JLabel welcome = new JLabel("Welcome, " + loggedInUser.getName() + "  ");
        welcome.setForeground(new Color(180, 200, 255));
        welcome.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        welcome.setHorizontalAlignment(SwingConstants.RIGHT);

        header.add(title, BorderLayout.WEST);
        header.add(welcome, BorderLayout.EAST);

        return header;
    }

    private JPanel createCenterPanel() {

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(20, 25, 40));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        JPanel searchPanel = new JPanel(new BorderLayout());
        searchPanel.setBackground(new Color(45, 50, 70));
        searchPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        leftPanel.setBackground(new Color(45, 50, 70));

        JLabel idLabel = new JLabel("Flight ID:");
        idLabel.setForeground(Color.WHITE);
        idField = new JTextField(8);

        JLabel srcLabel = new JLabel("Source:");
        srcLabel.setForeground(Color.WHITE);
        sourceField = new JTextField(8);

        JLabel destLabel = new JLabel("Destination:");
        destLabel.setForeground(Color.WHITE);
        destinationField = new JTextField(8);

        JButton searchBtn = new JButton("Search");
        stylePrimaryButton(searchBtn);

        JButton showAllBtn = new JButton("Show All Flights");
        styleSecondaryButton(showAllBtn);

        JButton myBookingsBtn = new JButton("My Bookings");
        styleSecondaryButton(myBookingsBtn);

        leftPanel.add(idLabel);
        leftPanel.add(idField);
        leftPanel.add(srcLabel);
        leftPanel.add(sourceField);
        leftPanel.add(destLabel);
        leftPanel.add(destinationField);
        leftPanel.add(searchBtn);
        leftPanel.add(showAllBtn);
        leftPanel.add(myBookingsBtn);

        JButton logoutBtn = new JButton("Logout");
        styleDangerButton(logoutBtn);

        searchPanel.add(leftPanel, BorderLayout.WEST);
        searchPanel.add(logoutBtn, BorderLayout.EAST);

        mainPanel.add(searchPanel, BorderLayout.NORTH);

        resultPanel = new JPanel();
        resultPanel.setLayout(new BoxLayout(resultPanel, BoxLayout.Y_AXIS));
        resultPanel.setBackground(new Color(20, 25, 40));

        JScrollPane scrollPane = new JScrollPane(resultPanel);
        scrollPane.setBorder(null);

        mainPanel.add(scrollPane, BorderLayout.CENTER);

        searchBtn.addActionListener(e -> {
            if (!idField.getText().isEmpty()) {
                searchById(idField.getText());
            } else if (!sourceField.getText().isEmpty() &&
                    !destinationField.getText().isEmpty()) {
                searchFlights();
            }
        });

        showAllBtn.addActionListener(e -> showAllFlights());
        myBookingsBtn.addActionListener(e -> showMyBookings());
        logoutBtn.addActionListener(e -> {
            dispose();
            new LoginFrame();
        });

        return mainPanel;
    }

    private void stylePrimaryButton(JButton button) {
        button.setBackground(new Color(0, 150, 255));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(true);
        button.setOpaque(true);
    }

    private void styleSecondaryButton(JButton button) {
        button.setBackground(new Color(100, 100, 200));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(true);
        button.setOpaque(true);
    }

    private void styleDangerButton(JButton button) {
        button.setBackground(new Color(200, 50, 50));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(true);
        button.setOpaque(true);
    }

    private void showStyledMessage(String message) {
        UIManager.put("Panel.background", new Color(30, 35, 55));
        UIManager.put("OptionPane.background", new Color(30, 35, 55));
        UIManager.put("OptionPane.messageForeground", Color.WHITE);
        JOptionPane.showMessageDialog(this, message);
    }

    private void showStyledDialog(Component component, String title) {
        UIManager.put("Panel.background", new Color(30, 35, 55));
        UIManager.put("OptionPane.background", new Color(30, 35, 55));
        UIManager.put("OptionPane.messageForeground", Color.WHITE);
        JOptionPane.showMessageDialog(this, component, title, JOptionPane.PLAIN_MESSAGE);
    }

    private void searchFlights() {

        resultPanel.removeAll();
        List<Flight> flights =
                flightDAO.searchFlights(sourceField.getText(), destinationField.getText());

        if (flights.isEmpty()) {
            showStyledMessage("No flights found.");
        } else {
            for (Flight f : flights) {
                resultPanel.add(createFlightCard(f));
                resultPanel.add(Box.createVerticalStrut(15));
            }
        }

        resultPanel.revalidate();
        resultPanel.repaint();
    }

    private void searchById(String flightId) {

        resultPanel.removeAll();
        Flight flight = flightDAO.getFlightById(flightId);

        if (flight == null) {
            showStyledMessage("No flight found with ID: " + flightId);
        } else {
            resultPanel.add(createFlightCard(flight));
        }

        resultPanel.revalidate();
        resultPanel.repaint();
    }

    private void showAllFlights() {

        resultPanel.removeAll();
        List<Flight> flights = flightDAO.getAllFlights();

        for (Flight f : flights) {
            resultPanel.add(createFlightCard(f));
            resultPanel.add(Box.createVerticalStrut(15));
        }

        resultPanel.revalidate();
        resultPanel.repaint();
    }

    private JPanel createFlightCard(Flight flight) {

        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(new Color(50, 60, 90));
        card.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));

        JLabel info = new JLabel(
                "<html><div style='color:white;'>" +
                        "<b>" + flight.getFlightId() + "</b><br>" +
                        flight.getSource() + " → " + flight.getDestination() +
                        "<br>Departure: " + flight.getDepartureTime() +
                        " | Arrival: " + flight.getArrivalTime() +
                        "<br>Price: ₹" + flight.getPrice() +
                        " | Seats: " + flight.getSeatsAvailable() +
                        "</div></html>"
        );

        JButton bookBtn = new JButton("Book Now");
        bookBtn.setBackground(new Color(0, 180, 120));
        bookBtn.setForeground(Color.WHITE);
        bookBtn.setFocusPainted(false);
        bookBtn.setBorderPainted(false);
        bookBtn.setContentAreaFilled(true);
        bookBtn.setOpaque(true);

        bookBtn.addActionListener(e -> bookFlight(flight));

        card.add(info, BorderLayout.CENTER);
        card.add(bookBtn, BorderLayout.EAST);

        return card;
    }

    private void bookFlight(Flight flight) {

        String[] options = {"Economy", "Business"};

        int choice = JOptionPane.showOptionDialog(
                this,
                "Select Booking Type:",
                "Booking",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                options,
                options[0]
        );

        if (choice == -1) return;

        String card = JOptionPane.showInputDialog(this, "Enter 8-digit Card Number:");

        if (card == null || !card.matches("\\d{8}")) {
            showStyledMessage("Invalid Card Number! Must contain exactly 8 digits.");
            return;
        }

        showStyledMessage("Processing Payment...");

        Thread paymentThread = new Thread(() -> {
            try {
                Thread.sleep(2000);
                SwingUtilities.invokeLater(() -> {
                    showStyledMessage("Payment Successful!");
                });
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        paymentThread.start();
    }

    private void showMyBookings() {

        BookingDAO dao = new BookingDAO();
        List<String[]> bookings = dao.getBookingsByUser(loggedInUser.getId());

        if (bookings.isEmpty()) {
            showStyledMessage("No bookings found.");
            return;
        }

        String[] columns = {"Booking ID", "Flight ID", "Source", "Destination", "Class"};
        String[][] data = new String[bookings.size()][5];

        for (int i = 0; i < bookings.size(); i++) {
            data[i] = bookings.get(i);
        }

        JTable table = new JTable(data, columns);
        table.setRowHeight(28);
        table.setBackground(new Color(35, 40, 60));
        table.setForeground(Color.WHITE);
        table.setGridColor(new Color(60, 70, 100));
        table.setSelectionBackground(new Color(0, 150, 255));
        table.setSelectionForeground(Color.WHITE);

        table.getTableHeader().setBackground(new Color(20, 25, 45));
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));

        JScrollPane pane = new JScrollPane(table);
        pane.getViewport().setBackground(new Color(35, 40, 60));
        pane.setPreferredSize(new Dimension(750, 300));

        showStyledDialog(pane, "My Bookings");
    }
}
