package com.example;

import javax.swing.*;
import org.jdesktop.swingx.JXDatePicker;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class PrescriptionManagerPanel extends JPanel {
    private JTextField patientNameField;
    private JTextField patientEmailField;
    private JTextField medicationField;
    private JTextField dosageField;
    private JTextField frequencyField;
    private JXDatePicker startDatePicker;
    private JXDatePicker endDatePicker;
    private JButton saveButton;
    private JButton trackButton;

    public PrescriptionManagerPanel() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel nameLabel = new JLabel("Patient Name:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(nameLabel, gbc);

        patientNameField = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 0;
        add(patientNameField, gbc);

        JLabel emailLabel = new JLabel("Patient Email:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(emailLabel, gbc);

        patientEmailField = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 1;
        add(patientEmailField, gbc);

        JLabel medicationLabel = new JLabel("Medication:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(medicationLabel, gbc);

        medicationField = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 2;
        add(medicationField, gbc);

        JLabel dosageLabel = new JLabel("Dosage:");
        gbc.gridx = 0;
        gbc.gridy = 3;
        add(dosageLabel, gbc);

        dosageField = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 3;
        add(dosageField, gbc);

        JLabel frequencyLabel = new JLabel("Frequency:");
        gbc.gridx = 0;
        gbc.gridy = 4;
        add(frequencyLabel, gbc);

        frequencyField = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 4;
        add(frequencyField, gbc);

        JLabel startDateLabel = new JLabel("Start Date:");
        gbc.gridx = 0;
        gbc.gridy = 5;
        add(startDateLabel, gbc);

        startDatePicker = new JXDatePicker();
        gbc.gridx = 1;
        gbc.gridy = 5;
        add(startDatePicker, gbc);

        JLabel endDateLabel = new JLabel("End Date:");
        gbc.gridx = 0;
        gbc.gridy = 6;
        add(endDateLabel, gbc);

        endDatePicker = new JXDatePicker();
        gbc.gridx = 1;
        gbc.gridy = 6;
        add(endDatePicker, gbc);

        saveButton = new JButton("Save Prescription");
        gbc.gridx = 1;
        gbc.gridy = 7;
        add(saveButton, gbc);

        trackButton = new JButton("Track Prescription");
        gbc.gridx = 1;
        gbc.gridy = 8;
        add(trackButton, gbc);

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                savePrescription();
            }
        });

        trackButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                trackPrescription();
            }
        });
    }

    private void savePrescription() {
        String patientName = patientNameField.getText();
        String patientEmail = patientEmailField.getText();
        String medication = medicationField.getText();
        String dosage = dosageField.getText();
        String frequency = frequencyField.getText();
        java.sql.Date startDate = new java.sql.Date(startDatePicker.getDate().getTime());
        java.sql.Date endDate = new java.sql.Date(endDatePicker.getDate().getTime());

        try (Connection connection = getConnection()) {
            // Save patient if not exists
            int patientId = getPatientId(connection, patientName, patientEmail);
            if (patientId == -1) {
                patientId = savePatient(connection, patientName, patientEmail);
            }

            // Save prescription
            String sql = "INSERT INTO prescriptions (patient_id, medication, dosage, frequency, start_date, end_date, status) VALUES (?, ?, ?, ?, ?, ?, 'Active')";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, patientId);
                statement.setString(2, medication);
                statement.setString(3, dosage);
                statement.setString(4, frequency);
                statement.setDate(5, startDate);
                statement.setDate(6, endDate);
                statement.executeUpdate();
                JOptionPane.showMessageDialog(this, "Prescription saved successfully!");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error saving prescription: " + ex.getMessage());
        }
    }

    private int getPatientId(Connection connection, String name, String email) throws SQLException {
        String sql = "SELECT id FROM patients WHERE name = ? AND email = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, name);
            statement.setString(2, email);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("id");
                }
            }
        }
        return -1; // Patient not found
    }

    private int savePatient(Connection connection, String name, String email) throws SQLException {
        String sql = "INSERT INTO patients (name, email) VALUES (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, name);
            statement.setString(2, email);
            statement.executeUpdate();
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                }
            }
        }
        throw new SQLException("Creating patient failed, no ID obtained.");
    }

    private void trackPrescription() {
        // Implement prescription tracking functionality here
    }

    private Connection getConnection() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/prescription_db";
        String username = "root"; // Change to your MySQL username
        String password = ""; // Change to your MySQL password
        return DriverManager.getConnection(url, username, password);
    }
}

