package com.inventory.Auth;

public class Authenticator {
    public static boolean authenticateAdmin(String username, String password) {
        // Hardcoded logic for demo; replace with real DB logic
        return "admin".equals(username) && "admin123".equals(password);
    }
}
