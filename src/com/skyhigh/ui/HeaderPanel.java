package com.skyhigh.ui;

import javax.swing.*;
import java.awt.*;

public class HeaderPanel extends JPanel {

    public HeaderPanel(String titleText) {

        setBackground(new Color(20, 25, 40));
        setPreferredSize(new Dimension(1000, 70));
        setLayout(new BorderLayout());

        JLabel title = new JLabel("  SkyHigh Airlines");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));

        JLabel subtitle = new JLabel(titleText + "  ");
        subtitle.setForeground(new Color(180, 200, 255));
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        subtitle.setHorizontalAlignment(SwingConstants.RIGHT);

        add(title, BorderLayout.WEST);
        add(subtitle, BorderLayout.EAST);
    }
}
