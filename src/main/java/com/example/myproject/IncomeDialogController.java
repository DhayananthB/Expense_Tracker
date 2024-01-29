package com.example.myproject;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class IncomeDialogController {
    @FXML
    private TextField amountField;

    @FXML
    private ChoiceBox<String> categoryChoiceBox;

    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @FXML
    protected void onAddIncome() {
        String amount = amountField.getText();
        String category = categoryChoiceBox.getValue();

        // Validate input
        if (amount.isEmpty() || category == null) {
            // Handle validation error (show an alert, etc.)
            return;
        }

        // Insert data into transaction and deposit tables
        String loggedInUsername = UserSession.getInstance().getUsername();
        insertIncomeIntoDatabase(amount, category,loggedInUsername);

        // Close the dialog
        closeDialog();
    }

    private void insertIncomeIntoDatabase(String amount, String category, String loggedInUsername) {
        // JDBC connection parameters (update these based on your database)
        String url = "jdbc:mysql://localhost:3306/expenses";
        String username = "root";
        String password = "password";


        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            // Insert data into the transaction table
            String transactionId = generateRandomTransactionId();
            String insertTransactionQuery = "INSERT INTO transaction (tid, DateofTrans, Amount, type, username) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement insertTransactionStatement = connection.prepareStatement(insertTransactionQuery)) {
                insertTransactionStatement.setString(1, transactionId);
                insertTransactionStatement.setString(2, LocalDateTime.now().format(dateTimeFormatter));
                insertTransactionStatement.setString(3, amount);
                insertTransactionStatement.setString(4, "deposit"); // Assuming "Income" is the type for income transactions
                insertTransactionStatement.setString(5, loggedInUsername); // Set the logged-in username
                insertTransactionStatement.executeUpdate();
            }

            // Insert data into the deposit table
            String insertDepositQuery = "INSERT INTO deposit (source, tid, username) VALUES (?, ?, ?)";
            try (PreparedStatement insertDepositStatement = connection.prepareStatement(insertDepositQuery)) {
                insertDepositStatement.setString(1, category);
                insertDepositStatement.setString(2, transactionId);
                insertDepositStatement.setString(3, loggedInUsername); // Set the logged-in username
                insertDepositStatement.executeUpdate();
            }

            // If the category is "savings," update the savingsbalance in the savings table
            if ("Savings".equals(category)) {
                String updateSavingsQuery = "UPDATE savings SET savingsbalance = savingsbalance - ? WHERE username = ?";
                try (PreparedStatement updateSavingsStatement = connection.prepareStatement(updateSavingsQuery)) {
                    double amountDouble = Double.parseDouble(amount);
                    updateSavingsStatement.setDouble(1, amountDouble);
                    updateSavingsStatement.setString(2, loggedInUsername); // Set the logged-in username
                    updateSavingsStatement.executeUpdate();
                }
            }

        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception appropriately in a real application
        }
    }





    private String generateRandomTransactionId() {
        // Generate a random transaction ID (you may want to implement a more robust method)
        return String.valueOf(new Random().nextInt(1000000));
    }

    private void closeDialog() {
        // Close the dialog
        Stage stage = (Stage) amountField.getScene().getWindow();
        stage.close();
    }
}
