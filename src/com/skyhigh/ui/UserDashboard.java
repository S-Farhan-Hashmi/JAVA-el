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
    private JComboBox<String> searchModeCombo;
    private JPanel idPanel;
    private JPanel routePanel;

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

        // Create modern tabbed pane
        JTabbedPane tabbedPane = new JTabbedPane();

        // Customize tab appearance
        UIManager.put("TabbedPane.selected", new Color(0, 150, 255));
        UIManager.put("TabbedPane.background", new Color(15, 20, 35));
        UIManager.put("TabbedPane.foreground", Color.WHITE);
        UIManager.put("TabbedPane.darkShadow", new Color(15, 20, 35));
        UIManager.put("TabbedPane.light", new Color(15, 20, 35));
        UIManager.put("TabbedPane.highlight", new Color(15, 20, 35));
        UIManager.put("TabbedPane.shadow", new Color(15, 20, 35));
        UIManager.put("TabbedPane.contentAreaColor", new Color(15, 20, 35));
        UIManager.put("TabbedPane.borderHightlightColor", new Color(15, 20, 35));
        UIManager.put("TabbedPane.focus", new Color(15, 20, 35));

        tabbedPane.setBackground(new Color(15, 20, 35));
        tabbedPane.setForeground(new Color(180, 190, 210));
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 15));
        tabbedPane.setOpaque(true);

        // Add custom tab styling
        tabbedPane.setUI(new javax.swing.plaf.basic.BasicTabbedPaneUI() {
            @Override
            protected void paintTabBackground(Graphics g, int tabPlacement, int tabIndex,
                    int x, int y, int w, int h, boolean isSelected) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (isSelected) {
                    // Active tab - gradient with curved edges
                    GradientPaint gradient = new GradientPaint(
                            x, y, new Color(0, 150, 255),
                            x, y + h, new Color(0, 120, 200));
                    g2d.setPaint(gradient);
                } else {
                    // Inactive tab
                    g2d.setColor(new Color(35, 40, 60));
                }

                // Draw rounded rectangle
                g2d.fillRoundRect(x, y, w, h + 5, 25, 25);
            }

            @Override
            protected void paintTabBorder(Graphics g, int tabPlacement, int tabIndex,
                    int x, int y, int w, int h, boolean isSelected) {
                // Don't paint any border - this removes the white lines
            }

            @Override
            protected void paintText(Graphics g, int tabPlacement, Font font, FontMetrics metrics,
                    int tabIndex, String title, Rectangle textRect, boolean isSelected) {
                g.setFont(font);
                g.setColor(isSelected ? Color.WHITE : new Color(150, 160, 180));
                g.drawString(title, textRect.x, textRect.y + metrics.getAscent());
            }

            @Override
            protected int calculateTabWidth(int tabPlacement, int tabIndex, FontMetrics metrics) {
                return super.calculateTabWidth(tabPlacement, tabIndex, metrics) + 40;
            }

            @Override
            protected int calculateTabHeight(int tabPlacement, int tabIndex, int fontHeight) {
                return super.calculateTabHeight(tabPlacement, tabIndex, fontHeight) + 12;
            }
        });

        // Tab 1: Search Flights
        JPanel searchTab = createSearchFlightsTab();
        tabbedPane.addTab("Search Flights", searchTab);

        // Tab 2: My Bookings
        JPanel bookingsTab = createMyBookingsTab();
        tabbedPane.addTab("My Bookings", bookingsTab);

        main.add(tabbedPane, BorderLayout.CENTER);

        return main;
    }

    private JPanel createSearchFlightsTab() {

        JPanel tab = new JPanel(new BorderLayout());
        tab.setBackground(new Color(15, 20, 35));

        tab.add(createSearchPanel(), BorderLayout.NORTH);

        resultPanel = new JPanel();
        resultPanel.setLayout(new BoxLayout(resultPanel, BoxLayout.Y_AXIS));
        resultPanel.setBackground(new Color(15, 20, 35));

        JScrollPane scroll = new JScrollPane(resultPanel);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(new Color(15, 20, 35));
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.getVerticalScrollBar().setUnitIncrement(16);

        tab.add(scroll, BorderLayout.CENTER);

        return tab;
    }

    private JPanel createMyBookingsTab() {

        JPanel tab = new JPanel(new BorderLayout());
        tab.setBackground(new Color(15, 20, 35));

        // Refresh button at top
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        topPanel.setBackground(new Color(35, 40, 60));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton refreshBtn = styledButton("Refresh", new Color(0, 150, 255));
        topPanel.add(refreshBtn);

        // Bookings panel
        JPanel bookingsPanel = new JPanel();
        bookingsPanel.setLayout(new BoxLayout(bookingsPanel, BoxLayout.Y_AXIS));
        bookingsPanel.setBackground(new Color(15, 20, 35));

        JScrollPane scroll = new JScrollPane(bookingsPanel);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(new Color(15, 20, 35));
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.getVerticalScrollBar().setUnitIncrement(16);

        tab.add(topPanel, BorderLayout.NORTH);
        tab.add(scroll, BorderLayout.CENTER);

        // Load bookings
        refreshBtn.addActionListener(e -> loadMyBookings(bookingsPanel));
        loadMyBookings(bookingsPanel);

        return tab;
    }

    private JPanel createSearchPanel() {

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(35, 40, 60));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        // Top row - Search mode dropdown
        JPanel topRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topRow.setBackground(new Color(35, 40, 60));

        topRow.add(label("Search By:"));

        searchModeCombo = new JComboBox<>(new String[] { "Flight ID", "Route (Source → Destination)" });
        searchModeCombo.setPreferredSize(new Dimension(250, 30));
        searchModeCombo.setBackground(new Color(70, 75, 100));
        searchModeCombo.setForeground(Color.WHITE);
        searchModeCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        topRow.add(searchModeCombo);

        // Bottom row - Input fields and buttons
        JPanel bottomRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        bottomRow.setBackground(new Color(35, 40, 60));

        // ID Panel (shown when Flight ID is selected)
        idPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        idPanel.setBackground(new Color(35, 40, 60));
        idPanel.add(label("Flight ID:"));
        idField = new JTextField(12);
        styleInputField(idField);
        idPanel.add(idField);

        // Route Panel (shown when Route is selected)
        routePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        routePanel.setBackground(new Color(35, 40, 60));
        routePanel.add(label("Source:"));
        sourceField = new JTextField(10);
        styleInputField(sourceField);
        routePanel.add(sourceField);
        routePanel.add(Box.createHorizontalStrut(10));
        routePanel.add(label("Destination:"));
        destinationField = new JTextField(10);
        styleInputField(destinationField);
        routePanel.add(destinationField);
        routePanel.setVisible(false);

        bottomRow.add(idPanel);
        bottomRow.add(routePanel);

        JButton searchBtn = styledButton("Search", new Color(0, 150, 255));
        JButton showAllBtn = styledButton("Show All", new Color(100, 100, 200));
        JButton logoutBtn = styledButton("Logout", new Color(200, 50, 50));

        bottomRow.add(searchBtn);
        bottomRow.add(showAllBtn);
        bottomRow.add(logoutBtn);

        // Combine top and bottom rows
        JPanel combined = new JPanel();
        combined.setLayout(new BoxLayout(combined, BoxLayout.Y_AXIS));
        combined.setBackground(new Color(35, 40, 60));
        combined.add(topRow);
        combined.add(Box.createVerticalStrut(10));
        combined.add(bottomRow);

        panel.add(combined, BorderLayout.CENTER);

        // Event listeners
        searchModeCombo.addActionListener(e -> {
            boolean isFlightId = searchModeCombo.getSelectedIndex() == 0;
            idPanel.setVisible(isFlightId);
            routePanel.setVisible(!isFlightId);
            panel.revalidate();
            panel.repaint();
        });

        searchBtn.addActionListener(e -> {
            if (searchModeCombo.getSelectedIndex() == 0) {
                // Search by Flight ID
                if (!idField.getText().trim().isEmpty()) {
                    searchById(idField.getText().trim());
                } else {
                    show("Please enter a Flight ID");
                }
            } else {
                // Search by Route
                searchFlights();
            }
        });

        showAllBtn.addActionListener(e -> showAllFlights());
        logoutBtn.addActionListener(e -> {
            dispose();
            new LoginFrame();
        });

        return panel;
    }

    private void styleInputField(JTextField field) {
        field.setBackground(new Color(70, 75, 100));
        field.setForeground(Color.WHITE);
        field.setCaretColor(Color.WHITE);
        field.setBorder(BorderFactory.createEmptyBorder(6, 10, 6, 10));
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

    private void loadMyBookings(JPanel bookingsPanel) {
        bookingsPanel.removeAll();

        BookingDAO bookingDAO = new BookingDAO();
        List<String[]> bookings = bookingDAO.getBookingsByUser(loggedInUser.getId());

        if (bookings.isEmpty()) {
            JLabel noBookings = new JLabel("No bookings found.");
            noBookings.setForeground(new Color(150, 150, 150));
            noBookings.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            noBookings.setAlignmentX(Component.CENTER_ALIGNMENT);
            bookingsPanel.add(Box.createVerticalStrut(50));
            bookingsPanel.add(noBookings);
        } else {
            for (String[] booking : bookings) {
                bookingsPanel.add(createBookingCard(booking));
                bookingsPanel.add(Box.createVerticalStrut(15));
            }
        }

        bookingsPanel.revalidate();
        bookingsPanel.repaint();
    }

    private JPanel createBookingCard(String[] booking) {
        // booking: [booking_id, flight_id, source, destination, class_type]

        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(new Color(45, 55, 90));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(70, 80, 120), 1),
                BorderFactory.createEmptyBorder(25, 30, 25, 30)));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 140));
        card.setPreferredSize(new Dimension(1000, 140));

        // Info panel
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(new Color(45, 55, 90));

        JLabel bookingId = new JLabel("Booking #" + booking[0]);
        bookingId.setForeground(new Color(100, 200, 255));
        bookingId.setFont(new Font("Segoe UI", Font.BOLD, 20));
        bookingId.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel route = new JLabel(booking[2] + " → " + booking[3]);
        route.setForeground(Color.WHITE);
        route.setFont(new Font("Segoe UI", Font.BOLD, 18));
        route.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel flightId = new JLabel("Flight: " + booking[1]);
        flightId.setForeground(new Color(200, 200, 220));
        flightId.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        flightId.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel classType = new JLabel("Class: " + booking[4]);
        classType.setForeground(booking[4].equalsIgnoreCase("Economy")
                ? new Color(150, 255, 150)
                : new Color(255, 215, 100));
        classType.setFont(new Font("Segoe UI", Font.BOLD, 15));
        classType.setAlignmentX(Component.LEFT_ALIGNMENT);

        infoPanel.add(bookingId);
        infoPanel.add(Box.createVerticalStrut(8));
        infoPanel.add(route);
        infoPanel.add(Box.createVerticalStrut(10));
        infoPanel.add(flightId);
        infoPanel.add(Box.createVerticalStrut(5));
        infoPanel.add(classType);

        // Cancel button
        JButton cancelBtn = styledButton("Cancel Booking", new Color(200, 50, 50));

        cancelBtn.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to cancel this booking?",
                    "Cancel Booking",
                    JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                BookingDAO dao = new BookingDAO();
                if (dao.cancelBooking(Integer.parseInt(booking[0]))) {
                    show("Booking cancelled successfully!");
                    loadMyBookings((JPanel) card.getParent());
                } else {
                    show("Failed to cancel booking.");
                }
            }
        });

        card.add(infoPanel, BorderLayout.CENTER);
        card.add(cancelBtn, BorderLayout.EAST);

        return card;
    }
}
