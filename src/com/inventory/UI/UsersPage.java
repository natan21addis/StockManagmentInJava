package com.inventory.UI;

import com.inventory.DAO.UserDAO;
import com.inventory.DTO.UserDTO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.sql.*;

public class UsersPage extends javax.swing.JPanel {

    public UsersPage() {
        initComponents();
        loadDataSet();
    }

    private void initComponents() {
        jLabel1 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        entryPanel = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        nameText = new javax.swing.JTextField();
        locationText = new javax.swing.JTextField();
        phoneText = new javax.swing.JTextField();
        usernameText = new javax.swing.JTextField();
        passText = new javax.swing.JPasswordField();
        userTypeCombo = new javax.swing.JComboBox<>(new String[]{"ADMINISTRATOR", "EMPLOYEE"});
        addButton = new javax.swing.JButton("Add");
        deleteButton = new javax.swing.JButton("Delete");
        clearButton = new javax.swing.JButton("Clear");
        exportButton = new javax.swing.JButton("Export");
        importButton = new javax.swing.JButton("Import");
        jScrollPane1 = new javax.swing.JScrollPane();
        userTable = new javax.swing.JTable();

        jLabel1.setFont(new java.awt.Font("Impact", 0, 24));
        jLabel1.setText("USERS");

        entryPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Enter User Details"));

        jLabel2.setText("Full Name:");
        jLabel3.setText("Location:");
        jLabel4.setText("Contact:");
        jLabel5.setText("Username:");
        jLabel6.setText("Password:");

        addButton.addActionListener(e -> addButtonActionPerformed());
        deleteButton.addActionListener(e -> deleteButtonActionPerformed());
        clearButton.addActionListener(e -> clearFields());
        exportButton.addActionListener(e -> exportUsersToFile());
        importButton.addActionListener(e -> importUsersFromFile()importUsersFromFile()importUsersFromFile());

        GroupLayout entryPanelLayout = new GroupLayout(entryPanel);
        entryPanel.setLayout(entryPanelLayout);
        entryPanelLayout.setAutoCreateGaps(true);
        entryPanelLayout.setAutoCreateContainerGaps(true);

        entryPanelLayout.setHorizontalGroup(
                entryPanelLayout.createSequentialGroup()
                        .addGroup(entryPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel2).addComponent(jLabel3).addComponent(jLabel4)
                                .addComponent(jLabel5).addComponent(jLabel6).addComponent(userTypeCombo)
                                .addGroup(entryPanelLayout.createSequentialGroup()
                                        .addComponent(addButton).addComponent(deleteButton)
                                        .addComponent(clearButton).addComponent(exportButton).addComponent(importButton))
                        )
                        .addGroup(entryPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(nameText).addComponent(locationText).addComponent(phoneText)
                                .addComponent(usernameText).addComponent(passText))
        );

        entryPanelLayout.setVerticalGroup(
                entryPanelLayout.createSequentialGroup()
                        .addGroup(entryPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel2).addComponent(nameText))
                        .addGroup(entryPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel3).addComponent(locationText))
                        .addGroup(entryPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel4).addComponent(phoneText))
                        .addGroup(entryPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel5).addComponent(usernameText))
                        .addGroup(entryPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel6).addComponent(passText))
                        .addComponent(userTypeCombo)
                        .addGroup(entryPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(addButton).addComponent(deleteButton).addComponent(clearButton)
                                .addComponent(exportButton).addComponent(importButton))
        );

        userTable.setModel(new DefaultTableModel());
        userTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                userTableMouseClicked();
            }
        });

        jScrollPane1.setViewportView(userTable);

        setLayout(new BorderLayout());
        add(jLabel1, BorderLayout.NORTH);
        add(jSeparator1, BorderLayout.CENTER);
        add(jScrollPane1, BorderLayout.WEST);
        add(entryPanel, BorderLayout.EAST);
    }

    private void addButtonActionPerformed() {
        if (nameText.getText().isEmpty() || locationText.getText().isEmpty() || phoneText.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please fill all required fields.");
            return;
        }

        UserDTO user = new UserDTO();
        user.setFullName(nameText.getText());
        user.setLocation(locationText.getText());
        user.setPhone(phoneText.getText());
        user.setUsername(usernameText.getText());
        user.setPassword(new String(passText.getPassword()));
        user.setUserType((String) userTypeCombo.getSelectedItem());

        new UserDAO().addUserDAO(user, user.getUserType());
        loadDataSet();
    }

    private void deleteButtonActionPerformed() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(null, "Please select a user to delete.");
            return;
        }
        String username = (String) userTable.getValueAt(selectedRow, 4);
        new UserDAO().deleteUserDAO(username);
        loadDataSet();
    }

    private void userTableMouseClicked() {
        int row = userTable.getSelectedRow();
        nameText.setText(userTable.getValueAt(row, 1).toString());
        locationText.setText(userTable.getValueAt(row, 2).toString());
        phoneText.setText(userTable.getValueAt(row, 3).toString());
        usernameText.setText(userTable.getValueAt(row, 4).toString());
        userTypeCombo.setSelectedItem(userTable.getValueAt(row, 6).toString());
    }

    private void clearFields() {
        nameText.setText("");
        locationText.setText("");
        phoneText.setText("");
        usernameText.setText("");
        passText.setText("");
    }

    private void exportUsersToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("users_export.csv"))) {
            for (int i = 0; i < userTable.getRowCount(); i++) {
                for (int j = 0; j < userTable.getColumnCount(); j++) {
                    writer.write(userTable.getValueAt(i, j).toString());
                    if (j < userTable.getColumnCount() - 1) writer.write(",");
                }
                writer.newLine();
            }
            JOptionPane.showMessageDialog(null, "User data exported to users_export.csv");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error exporting data: " + e.getMessage());
        }
    }

    private void importUsersFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader("users_export.csv"))) {
            String line;
            UserDAO userDAO = new UserDAO();
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 7) {
                    UserDTO user = new UserDTO();
                    user.setFullName(data[1]);
                    user.setLocation(data[2]);
                    user.setPhone(data[3]);
                    user.setUsername(data[4]);
                    user.setPassword("default123");
                    user.setUserType(data[6]);
                    userDAO.addUserDAO(user, data[6]);
                }
            }
            loadDataSet();
            JOptionPane.showMessageDialog(null, "User data imported from users_export.csv");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error importing data: " + e.getMessage());
        }
    }

    public void loadDataSet() {
        try {
            UserDAO dao = new UserDAO();
            userTable.setModel(dao.buildTableModel(dao.getQueryResult()));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private javax.swing.JButton addButton, clearButton, deleteButton, exportButton, importButton;
    private javax.swing.JPanel entryPanel;
    private javax.swing.JLabel jLabel1, jLabel2, jLabel3, jLabel4, jLabel5, jLabel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTextField locationText, nameText, phoneText, usernameText;
    private javax.swing.JPasswordField passText;
    private javax.swing.JComboBox<String> userTypeCombo;
    private javax.swing.JTable userTable;
}
