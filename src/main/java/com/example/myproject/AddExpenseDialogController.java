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

public class AddExpenseDialogController {
    @FXML
    private TextField amountField;

    @FXML
    private TextField remarksField;

    @FXML
    private ChoiceBox<String> categoryChoiceBox;

    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @FXML
    protected void onAddExpense() {
        String amount = amountField.getText();
        String remarks = remarksField.getText();
        String category = categoryChoiceBox.getValue();

        // Validate input
        if (amount.isEmpty() || category == null) {
            // Handle validation error (show an alert, etc.)
            return;
        }
        String loggegInUsername = UserSession.getInstance().getUsername();
        // Insert data into transaction and debit tables
        addExpenseToDatabase(amount, category, remarks,loggegInUsername);

        // Close the dialog
        closeDialog();
    }

    private void addExpenseToDatabase(String amount, String category, String remarks, String loggedInUsername) {
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
                insertTransactionStatement.setString(4, "Expense"); // Assuming "Expense" is the type for expense transactions
                insertTransactionStatement.setString(5, loggedInUsername); // Set the logged-in username
                insertTransactionStatement.executeUpdate();
            }

            // Insert data into the debit table
            String insertDebitQuery = "INSERT INTO debit (tid, category, remarks, username) VALUES (?, ?, ?, ?)";
            try (PreparedStatement insertDebitStatement = connection.prepareStatement(insertDebitQuery)) {
                insertDebitStatement.setString(1, transactionId);
                insertDebitStatement.setString(2, category);
                insertDebitStatement.setString(3, remarks);
                insertDebitStatement.setString(4, loggedInUsername); // Set the logged-in username
                insertDebitStatement.executeUpdate();
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
