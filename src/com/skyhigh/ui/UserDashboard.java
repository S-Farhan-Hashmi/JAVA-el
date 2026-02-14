package com.skyhigh.ui;

import com.skyhigh.dao.BookingDAO;
import com.skyhigh.dao.FlightDAO;
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
        setSize(1200, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        getContentPane().setBackground(new Color(15, 20, 35));

        add(createHeader(), BorderLayout.NORTH);
        add(createMainPanel(), BorderLayout.CENTER);

        setVisible(true);
    }

    private JPanel createHeader() {

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(10, 15, 28));
        header.setPreferredSize(new Dimension(1000, 75));

        JLabel title = new JLabel("  SkyHigh Airlines");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));

        JLabel welcome = new JLabel("Welcome, " + loggedInUser.getName() + "  ");
        welcome.setForeground(new Color(150, 200, 255));
        welcome.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        welcome.setHorizontalAlignment(SwingConstants.RIGHT);

        header.add(title, BorderLayout.WEST);
        header.add(welcome, BorderLayout.EAST);

        return header;
    }

    private JPanel createMainPanel() {

        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(new Color(15, 20, 35));
        main.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        main.add(createSearchPanel(), BorderLayout.NORTH);

        resultPanel = new JPanel();
        resultPanel.setLayout(new BoxLayout(resultPanel, BoxLayout.Y_AXIS));
        resultPanel.setBackground(new Color(15, 20, 35));

        JScrollPane scroll = new JScrollPane(resultPanel);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(new Color(15, 20, 35));
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.getVerticalScrollBar().setUnitIncrement(16);

        main.add(scroll, BorderLayout.CENTER);

        return main;
    }

    private JPanel createSearchPanel() {

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(35, 40, 60));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);

        idField = new JTextField(8);
        sourceField = new JTextField(8);
        destinationField = new JTextField(8);

        JButton searchBtn = styledButton("Search", new Color(0, 150, 255));
        JButton showAllBtn = styledButton("Show All", new Color(100, 100, 200));
        JButton logoutBtn = styledButton("Logout", new Color(200, 50, 50));

        gbc.gridx = 0;
        panel.add(label("Flight ID:"), gbc);
        gbc.gridx = 1;
        panel.add(idField, gbc);

        gbc.gridx = 2;
        panel.add(label("Source:"), gbc);
        gbc.gridx = 3;
        panel.add(sourceField, gbc);

        gbc.gridx = 4;
        panel.add(label("Destination:"), gbc);
        gbc.gridx = 5;
        panel.add(destinationField, gbc);

        gbc.gridx = 6;
        panel.add(searchBtn, gbc);
        gbc.gridx = 7;
        panel.add(showAllBtn, gbc);
        gbc.gridx = 8;
        panel.add(logoutBtn, gbc);

        searchBtn.addActionListener(e -> {
            if (!idField.getText().isEmpty())
                searchById(idField.getText());
            else
                searchFlights();
        });

        showAllBtn.addActionListener(e -> showAllFlights());
        logoutBtn.addActionListener(e -> {
            dispose();
            new LoginFrame();
        });

        return panel;
    }

    private JPanel createFlightCard(Flight f) {

        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(new Color(45, 55, 90));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(70, 80, 120), 1),
                BorderFactory.createEmptyBorder(25, 30, 25, 30)));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));
        card.setPreferredSize(new Dimension(1000, 200));

        // Left panel with flight info
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(new Color(45, 55, 90));

        JLabel flightId = new JLabel(f.getFlightId());
        flightId.setForeground(new Color(100, 200, 255));
        flightId.setFont(new Font("Segoe UI", Font.BOLD, 22));
        flightId.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel route = new JLabel(f.getSource() + " → " + f.getDestination());
        route.setForeground(Color.WHITE);
        route.setFont(new Font("Segoe UI", Font.BOLD, 18));
        route.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel timing = new JLabel("Departure: " + f.getDepartureTime() + " | Arrival: " + f.getArrivalTime());
        timing.setForeground(new Color(200, 200, 220));
        timing.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        timing.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel economy = new JLabel("Economy: ₹" + f.getEconomyPrice() + " (" + f.getEconomySeats() + " seats)");
        economy.setForeground(new Color(150, 255, 150));
        economy.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        economy.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel business = new JLabel("Business: ₹" + f.getBusinessPrice() + " (" + f.getBusinessSeats() + " seats)");
        business.setForeground(new Color(255, 215, 100));
        business.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        business.setAlignmentX(Component.LEFT_ALIGNMENT);

        infoPanel.add(flightId);
        infoPanel.add(Box.createVerticalStrut(8));
        infoPanel.add(route);
        infoPanel.add(Box.createVerticalStrut(10));
        infoPanel.add(timing);
        infoPanel.add(Box.createVerticalStrut(10));
        infoPanel.add(economy);
        infoPanel.add(Box.createVerticalStrut(5));
        infoPanel.add(business);

        JButton bookBtn = styledButton("Book Now", new Color(0, 180, 120));
        bookBtn.setPreferredSize(new Dimension(140, 50));
        bookBtn.setFont(new Font("Segoe UI", Font.BOLD, 16));

        bookBtn.addActionListener(e -> bookFlight(f));

        card.add(infoPanel, BorderLayout.CENTER);
        card.add(bookBtn, BorderLayout.EAST);

        return card;
    }

    private void searchFlights() {
        resultPanel.removeAll();
        List<Flight> flights = flightDAO.searchFlights(sourceField.getText(), destinationField.getText());
        displayFlights(flights);
    }

    private void searchById(String id) {
        resultPanel.removeAll();
        Flight f = flightDAO.getFlightById(id);
        if (f != null)
            resultPanel.add(createFlightCard(f));
        else
            show("No flight found.");
        resultPanel.revalidate();
        resultPanel.repaint();
    }

    private void showAllFlights() {
        resultPanel.removeAll();
        displayFlights(flightDAO.getAllFlights());
    }

    private void displayFlights(List<Flight> flights) {
        if (flights.isEmpty()) {
            show("No flights found.");
        } else {
            for (Flight f : flights) {
                resultPanel.add(createFlightCard(f));
                resultPanel.add(Box.createVerticalStrut(15));
            }
        }
        resultPanel.revalidate();
        resultPanel.repaint();
    }

    private void bookFlight(Flight f) {

        String[] options = { "Economy", "Business" };

        int choice = JOptionPane.showOptionDialog(
                this,
                "Select Class",
                "Booking",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                options,
                options[0]);

        if (choice == -1)
            return;

        String card = JOptionPane.showInputDialog(this, "Enter 8-digit Card Number:");

        if (card == null || !card.matches("\\d{8}")) {
            show("Invalid Card Number (8 digits required)");
            return;
        }

        Booking booking = new Booking(
                loggedInUser.getId(),
                f.getFlightId(),
                choice == 0 ? "Economy" : "Business");

        BookingDAO dao = new BookingDAO();

        try {
            if (dao.bookFlight(booking)) {
                show("Booking Successful!");
                showAllFlights(); // 🔥 REFRESH AFTER BOOKING
            } else {
                show("Booking Failed.");
            }
        } catch (Exception e) {
            show(e.getMessage());
        }
    }

    private JButton styledButton(String text, Color color) {
        JButton b = new JButton(text);
        b.setBackground(color);
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setFont(new Font("Segoe UI", Font.BOLD, 14));
        return b;
    }

    private JLabel label(String text) {
        JLabel l = new JLabel(text);
        l.setForeground(Color.WHITE);
        l.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        return l;
    }

    private void show(String msg) {
        UIManager.put("Panel.background", new Color(30, 35, 55));
        UIManager.put("OptionPane.background", new Color(30, 35, 55));
        UIManager.put("OptionPane.messageForeground", Color.WHITE);
        JOptionPane.showMessageDialog(this, msg);
    }
}
