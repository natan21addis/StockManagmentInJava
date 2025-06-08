package com.inventory.DAO;

import com.inventory.Database.ConnectionFactory;
import com.inventory.DTO.UserDTO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

public class UserDAO {
    private Connection conn = null;
    private PreparedStatement prepStatement = null;
    private Statement statement = null;
    private ResultSet resultSet = null;

    // Local list to track user logins (for dashboard display or audit)
    private final List<UserDTO> userLoginList = new ArrayList<>();

    // Constructor opens DB connection
    public UserDAO() {
        try {
            conn = new ConnectionFactory().getConn();
            statement = conn.createStatement();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    // Add a new user
    public void addUserDAO(UserDTO userDTO, String userType) {
        try {
            String query = "SELECT * FROM users WHERE name=? AND location=? AND phone=? AND usertype=?";
            prepStatement = conn.prepareStatement(query);
            prepStatement.setString(1, userDTO.getFullName());
            prepStatement.setString(2, userDTO.getLocation());
            prepStatement.setString(3, userDTO.getPhone());
            prepStatement.setString(4, userDTO.getUserType());

            resultSet = prepStatement.executeQuery();

            if (resultSet.next()) {
                JOptionPane.showMessageDialog(null, "User already exists");
            } else {
                addFunction(userDTO, userType);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void addFunction(UserDTO userDTO, String userType) {
        try {
            String query = "INSERT INTO users (name, location, phone, username, password, usertype) VALUES (?, ?, ?, ?, ?, ?)";
            prepStatement = conn.prepareStatement(query);
            prepStatement.setString(1, userDTO.getFullName());
            prepStatement.setString(2, userDTO.getLocation());
            prepStatement.setString(3, userDTO.getPhone());
            prepStatement.setString(4, userDTO.getUsername());
            prepStatement.setString(5, userDTO.getPassword());
            prepStatement.setString(6, userDTO.getUserType());
            prepStatement.executeUpdate();

            if ("ADMINISTRATOR".equalsIgnoreCase(userType)) {
                JOptionPane.showMessageDialog(null, "New administrator added.");
            } else {
                JOptionPane.showMessageDialog(null, "New employee added.");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // Edit existing user
    public void editUserDAO(UserDTO userDTO) {
        try {
            String query = "UPDATE users SET name=?, location=?, phone=?, usertype=? WHERE username=?";
            prepStatement = conn.prepareStatement(query);
            prepStatement.setString(1, userDTO.getFullName());
            prepStatement.setString(2, userDTO.getLocation());
            prepStatement.setString(3, userDTO.getPhone());
            prepStatement.setString(4, userDTO.getUserType());
            prepStatement.setString(5, userDTO.getUsername());
            prepStatement.executeUpdate();

            JOptionPane.showMessageDialog(null, "Updated Successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Delete user by username
    public void deleteUserDAO(String username) {
        try {
            String query = "DELETE FROM users WHERE username=?";
            prepStatement = conn.prepareStatement(query);
            prepStatement.setString(1, username);
            prepStatement.executeUpdate();
            JOptionPane.showMessageDialog(null, "User Deleted.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Get all users (ResultSet)
    public ResultSet getQueryResult() {
        try {
            String query = "SELECT * FROM users";
            resultSet = statement.executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    // Helper method to build a TableModel for UI tables
    public DefaultTableModel buildTableModel(ResultSet rs) throws SQLException {
        ResultSetMetaData metaData = rs.getMetaData();

        Vector<String> columnNames = new Vector<>();
        int columnCount = metaData.getColumnCount();

        for (int column = 1; column <= columnCount; column++) {
            columnNames.add(metaData.getColumnName(column).toUpperCase(Locale.ROOT));
        }

        Vector<Vector<Object>> data = new Vector<>();

        while (rs.next()) {
            Vector<Object> row = new Vector<>();
            for (int i = 1; i <= columnCount; i++) {
                row.add(rs.getObject(i));
            }
            data.add(row);
        }

        return new DefaultTableModel(data, columnNames);
    }

    // ✅ NEW METHOD: Store user login in local list (used by Dashboard)
    public void addUserLogin(UserDTO userDTO) {
        userLoginList.add(userDTO);
    }

    // ✅ Get all logins recorded in memory (can be used in Dashboard or Report)
    public List<UserDTO> getUserLogins() {
        return userLoginList;
    }
}
