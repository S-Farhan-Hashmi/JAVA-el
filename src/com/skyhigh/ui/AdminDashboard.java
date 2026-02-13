package com.skyhigh.ui;

import com.skyhigh.dao.FlightDAO;
import com.skyhigh.model.Flight;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.List;

public class AdminDashboard extends JFrame {

    private FlightDAO flightDAO;
    private JTable table;
    private DefaultTableModel model;

    public AdminDashboard() {

        flightDAO = new FlightDAO();

        setTitle("SkyHigh Airlines - Admin Portal");
        setSize(1200, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        getContentPane().setBackground(new Color(18, 22, 35));

        add(createHeader(), BorderLayout.NORTH);
        add(createMainPanel(), BorderLayout.CENTER);

        setVisible(true);
    }

    private JPanel createHeader() {

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(12, 16, 28));
        header.setPreferredSize(new Dimension(1000, 75));

        JLabel title = new JLabel("  SkyHigh Airlines - Admin Portal");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));

        header.add(title, BorderLayout.WEST);

        return header;
    }

    private JPanel createMainPanel() {

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(18, 22, 35));
        panel.setBorder(BorderFactory.createEmptyBorder(25, 50, 25, 50));

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0));
        top.setBackground(new Color(18, 22, 35));

        JButton addBtn = styledButton("Add Flight", new Color(0,150,255));
        JButton deleteBtn = styledButton("Delete Selected", new Color(200,50,50));
        JButton refreshBtn = styledButton("Refresh", new Color(100,100,200));
        JButton logoutBtn = styledButton("Logout", new Color(180,40,40));

        top.add(addBtn);
        top.add(deleteBtn);
        top.add(refreshBtn);
        top.add(logoutBtn);

        panel.add(top, BorderLayout.NORTH);

        model = new DefaultTableModel(
                new String[]{"ID","Source","Destination",
                        "Departure","Arrival",
                        "Economy ₹","Seats"},0);

        table = new JTable(model);
        table.setRowHeight(32);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setForeground(Color.WHITE);
        table.setGridColor(new Color(60,70,100));
        table.setSelectionBackground(new Color(0,140,255));
        table.setSelectionForeground(Color.WHITE);

        table.getTableHeader().setBackground(new Color(25,30,50));
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 15));

        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer(){
            @Override
            public Component getTableCellRendererComponent(JTable table,Object value,
                                                           boolean isSelected,
                                                           boolean hasFocus,
                                                           int row,int column){
                Component c = super.getTableCellRendererComponent(
                        table,value,isSelected,hasFocus,row,column);
                if(!isSelected){
                    c.setBackground(row%2==0 ?
                            new Color(28,35,60) :
                            new Color(24,30,50));
                }
                setHorizontalAlignment(SwingConstants.CENTER);
                return c;
            }
        });

        JScrollPane scroll = new JScrollPane(table);
        scroll.getViewport().setBackground(new Color(18,22,35));
        scroll.setBorder(BorderFactory.createEmptyBorder());

        panel.add(scroll, BorderLayout.CENTER);

        addBtn.addActionListener(e->addFlight());
        deleteBtn.addActionListener(e->deleteSelected());
        refreshBtn.addActionListener(e->loadFlights());

        logoutBtn.addActionListener(e->{
            dispose();
            new LoginFrame();
        });

        loadFlights();

        return panel;
    }

    private JButton styledButton(String text, Color color){
        JButton b = new JButton(text);
        b.setBackground(color);
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setFont(new Font("Segoe UI", Font.BOLD, 14));
        return b;
    }

    private void loadFlights(){
        model.setRowCount(0);
        List<Flight> flights = flightDAO.getAllFlights();
        for(Flight f : flights){
            model.addRow(new Object[]{
                    f.getFlightId(),
                    f.getSource(),
                    f.getDestination(),
                    f.getDepartureTime(),
                    f.getArrivalTime(),
                    f.getPrice(),
                    f.getSeatsAvailable()
            });
        }
    }

    private void addFlight(){

        JTextField id = new JTextField();
        JTextField src = new JTextField();
        JTextField dest = new JTextField();

        JTextField depTime = new JTextField("10:00");
        JComboBox<String> depAMPM =
                new JComboBox<>(new String[]{"AM","PM"});

        JTextField arrTime = new JTextField("12:00");
        JComboBox<String> arrAMPM =
                new JComboBox<>(new String[]{"AM","PM"});

        JTextField ecoPrice = new JTextField();
        JTextField seats = new JTextField();

        JPanel panel = new JPanel(new GridLayout(0,3,15,15));
        panel.setBackground(new Color(30,35,55));

        panel.add(label("Flight ID:")); panel.add(id); panel.add(new JLabel());
        panel.add(label("Source:")); panel.add(src); panel.add(new JLabel());
        panel.add(label("Destination:")); panel.add(dest); panel.add(new JLabel());

        panel.add(label("Departure:"));
        panel.add(depTime);
        panel.add(depAMPM);

        panel.add(label("Arrival:"));
        panel.add(arrTime);
        panel.add(arrAMPM);

        panel.add(label("Economy Price:")); panel.add(ecoPrice); panel.add(new JLabel());
        panel.add(label("Seats:")); panel.add(seats); panel.add(new JLabel());

        int result = JOptionPane.showConfirmDialog(this,panel,
                "Add Flight",JOptionPane.OK_CANCEL_OPTION);

        if(result==JOptionPane.OK_OPTION){
            try{
                String departure =
                        depTime.getText()+" "+depAMPM.getSelectedItem();
                String arrival =
                        arrTime.getText()+" "+arrAMPM.getSelectedItem();

                Flight flight = new Flight(
                        id.getText(),
                        src.getText(),
                        dest.getText(),
                        departure,
                        arrival,
                        Double.parseDouble(ecoPrice.getText()),
                        Integer.parseInt(seats.getText())
                );

                if(flightDAO.addFlight(flight)){
                    loadFlights();
                    show("Flight Added Successfully!");
                }else{
                    show("Failed to Add Flight!");
                }

            }catch(Exception e){
                show("Invalid Input!");
            }
        }
    }

    private void deleteSelected(){
        int row = table.getSelectedRow();
        if(row==-1){
            show("Select a flight first!");
            return;
        }
        String id = model.getValueAt(row,0).toString();
        if(flightDAO.deleteFlight(id)){
            loadFlights();
            show("Flight Deleted Successfully!");
        }else{
            show("Delete Failed!");
        }
    }

    private JLabel label(String text){
        JLabel l = new JLabel(text);
        l.setForeground(Color.WHITE);
        l.setFont(new Font("Segoe UI",Font.PLAIN,14));
        return l;
    }

    private void show(String msg){
        UIManager.put("Panel.background", new Color(30,35,55));
        UIManager.put("OptionPane.background", new Color(30,35,55));
        UIManager.put("OptionPane.messageForeground", Color.WHITE);
        JOptionPane.showMessageDialog(this,msg);
    }
}
