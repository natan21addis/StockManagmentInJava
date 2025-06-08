package com.inventory.UI;

import com.inventory.OTP.OtpManager;
import com.inventory.Auth.Authenticator;

import javax.mail.PasswordAuthentication;


import javax.swing.*;
import java.awt.*;

/**
 * LoginPage.java
 * Dual authentication for ADMINISTRATOR and EMPLOYEE
 */
public class LoginPage extends JFrame {

    private JComboBox<String> roleComboBox;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JTextField emailField;
    private JTextField otpField;
    private JButton sendOtpButton;
    private JButton loginButton;

    private String generatedOtp = null;
    private OtpManager otpManager = new OtpManager();

    public LoginPage() {
        setTitle("Login System");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initializeUI();
        setVisible(true);
    }

    private void initializeUI() {
        JPanel panel = new JPanel();
        panel.setLayout(null);

        JLabel roleLabel = new JLabel("Select Role:");
        roleLabel.setBounds(30, 20, 100, 25);
        panel.add(roleLabel);

        String[] roles = {"ADMINISTRATOR", "EMPLOYEE"};
        roleComboBox = new JComboBox<>(roles);
        roleComboBox.setBounds(150, 20, 200, 25);
        panel.add(roleComboBox);

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setBounds(30, 60, 100, 25);
        panel.add(usernameLabel);

        usernameField = new JTextField();
        usernameField.setBounds(150, 60, 200, 25);
        panel.add(usernameField);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(30, 100, 100, 25);
        panel.add(passwordLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(150, 100, 200, 25);
        panel.add(passwordField);

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setBounds(30, 140, 100, 25);
        panel.add(emailLabel);

        emailField = new JTextField();
        emailField.setBounds(150, 140, 200, 25);
        panel.add(emailField);

        sendOtpButton = new JButton("Send OTP");
        sendOtpButton.setBounds(150, 170, 100, 25);
        panel.add(sendOtpButton);

        JLabel otpLabel = new JLabel("Enter OTP:");
        otpLabel.setBounds(30, 210, 100, 25);
        panel.add(otpLabel);

        otpField = new JTextField();
        otpField.setBounds(150, 210, 200, 25);
        otpField.setEnabled(false);  // Disabled until OTP sent
        panel.add(otpField);

        loginButton = new JButton("Login");
        loginButton.setBounds(150, 260, 100, 30);
        panel.add(loginButton);

        // Set fields visibility based on role selection
        updateVisibility();

        // Role selection listener to toggle fields
        roleComboBox.addActionListener(e -> updateVisibility());

        // Send OTP button action
        sendOtpButton.addActionListener(e -> {
            String email = emailField.getText().trim();
            if (!email.isEmpty()) {
                generatedOtp = otpManager.sendOtpToEmail(email);
                if (generatedOtp != null && !generatedOtp.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "OTP sent to your email: " + email);
                    otpField.setEnabled(true);
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to send OTP. Please try again.");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please enter a valid email.");
            }
        });

        // Login button action
        loginButton.addActionListener(e -> {
            String selectedRole = (String) roleComboBox.getSelectedItem();

            if ("ADMINISTRATOR".equals(selectedRole)) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());

                // Replace this with your actual admin authentication logic
                if (Authenticator.authenticateAdmin(username, password)) {
                    JOptionPane.showMessageDialog(this, "Administrator login successful.");
                    // Proceed to admin dashboard or next steps here
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid admin credentials.");
                }
            } else if ("EMPLOYEE".equals(selectedRole)) {
                String otp = otpField.getText().trim();

                if (generatedOtp != null && generatedOtp.equals(otp)) {
                    JOptionPane.showMessageDialog(this, "Employee login successful.");
                    // Proceed to employee dashboard or next steps here
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid OTP.");
                }
            }
        });

        add(panel);
    }

    private void updateVisibility() {
        boolean isAdmin = "ADMINISTRATOR".equals(roleComboBox.getSelectedItem());

        usernameField.setEnabled(isAdmin);
        passwordField.setEnabled(isAdmin);
        emailField.setEnabled(!isAdmin);
        sendOtpButton.setEnabled(!isAdmin);
        otpField.setEnabled(!isAdmin ? otpField.isEnabled() : false);

        usernameField.setVisible(isAdmin);
        passwordField.setVisible(isAdmin);

        emailField.setVisible(!isAdmin);
        sendOtpButton.setVisible(!isAdmin);
        otpField.setVisible(!isAdmin);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginPage::new);
    }
}
