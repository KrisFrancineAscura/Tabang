package com.example;

import javax.swing.*;
import javax.swing.JFrame;
import com.example.PrescriptionManagerPanel; // Adjust package name if needed


public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Prescription Manager");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 600);
            frame.setLocationRelativeTo(null);
            frame.add(new PrescriptionManagerPanel());
            frame.setVisible(true);
        });
    }
}
