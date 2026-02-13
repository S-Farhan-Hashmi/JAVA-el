package com.skyhigh.ui;

import javax.swing.*;
import java.awt.*;

public class GradientPanel extends JPanel {

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;
        GradientPaint gradient = new GradientPaint(
                0, 0, new Color(15, 20, 35),
                0, getHeight(), new Color(40, 50, 80)
        );

        g2.setPaint(gradient);
        g2.fillRect(0, 0, getWidth(), getHeight());
    }
}
