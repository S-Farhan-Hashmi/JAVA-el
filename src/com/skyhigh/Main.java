package com.skyhigh;
import com.skyhigh.ui.LoginFrame;
import javax.swing.UIManager;
import java.awt.Font;
public class Main {

    public static void main(String[] args) {

        try {

            UIManager.put("Label.font", new Font("Segoe UI", Font.PLAIN, 15));
            UIManager.put("Button.font", new Font("Segoe UI", Font.BOLD, 14));
            UIManager.put("TextField.font", new Font("Segoe UI", Font.PLAIN, 14));
            UIManager.put("PasswordField.font", new Font("Segoe UI", Font.PLAIN, 14));

        } catch (Exception ignored) {}

        new LoginFrame();
    }
}
