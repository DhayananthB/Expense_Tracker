package com.example.myproject;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class BudgetController {
    @FXML
    private TextField budgetAmountField;

    @FXML
    protected void onSetBudget() {
        // Get the entered budget amount
        String budgetAmount = budgetAmountField.getText();

        // Get the current month and year
        LocalDate currentDate = LocalDate.now();
        String currentMonthYear = currentDate.format(DateTimeFormatter.ofPattern("MMMMyyyy"));

        // Save the budget amount to the database
        saveBudgetToDatabase(currentMonthYear, budgetAmount);

        // Close the Set Budget window
        budgetAmountField.getScene().getWindow().hide();
    }

    private void saveBudgetToDatabase(String currentMonthYear, String budgetAmount) {
        // JDBC connection parameters (update these based on your database)
        String url = "jdbc:mysql://localhost:3306/expenses";
        String username = "root";
        String password = "password";
        String loggedInUsername = UserSession.getInstance().getUsername();

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            // Check if the currentMonthYear exists for the specific username in the table
            String checkQuery = "SELECT * FROM budget WHERE username = ? AND month = ?";
            try (PreparedStatement checkStatement = connection.prepareStatement(checkQuery)) {
                checkStatement.setString(1, loggedInUsername); // Set the logged-in username
                checkStatement.setString(2, currentMonthYear);
                ResultSet resultSet = checkStatement.executeQuery();

                if (resultSet.next()) {
                    // Update the existing row
                    String updateQuery = "UPDATE budget SET amount = ? WHERE username = ? AND month = ?";
                    try (PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {
                        updateStatement.setString(1, budgetAmount);
                        updateStatement.setString(2, loggedInUsername); // Set the logged-in username
                        updateStatement.setString(3, currentMonthYear);
                        updateStatement.executeUpdate();
                    }
                } else {
                    // Insert a new row
                    String insertQuery = "INSERT INTO budget (username, month, amount) VALUES (?, ?, ?)";
                    try (PreparedStatement insertStatement = connection.prepareStatement(insertQuery)) {
                        insertStatement.setString(1, loggedInUsername); // Set the logged-in username
                        insertStatement.setString(2, currentMonthYear);
                        insertStatement.setString(3, budgetAmount);
                        insertStatement.executeUpdate();
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception appropriately in a real application
        }

    }
}
