// ViewBalanceController.java
package com.example.myproject;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import java.time.format.DateTimeFormatter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;


public class ViewBalanceController {

    @FXML
    private Label budgetLabel;

    @FXML
    private Label expensesLabel;

    @FXML
    private Label remainingLabel;

    @FXML
    private void initialize() {
        // Fetch and display the balance information
        String username = UserSession.getInstance().getUsername();

        // JDBC connection parameters (update these based on your database)
        String url = "jdbc:mysql://localhost:3306/expenses";
        String dbUsername = "root";
        String dbPassword = "password";

        try (Connection connection = DriverManager.getConnection(url, dbUsername, dbPassword)) {
            String query = "SELECT b.amount AS budgetAmount, COALESCE(SUM(t.Amount), 0) AS totalExpenses " +
                    "FROM budget b " +
                    "LEFT JOIN transaction t ON b.username = t.username AND " +
                    "SUBSTRING(b.month, LENGTH(b.month) - 3, 4) = DATE_FORMAT(t.DateofTrans, '%Y') " +
                    "WHERE b.username = ? AND SUBSTRING(b.month, 1, LENGTH(b.month) - 4) = DATE_FORMAT(t.DateofTrans, '%M') AND t.type=? " +
                    "GROUP BY b.amount";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, username);
                preparedStatement.setString(2,"Expense");
                // preparedStatement.setString(2, LocalDate.now().format(DateTimeFormatter.ofPattern("MMMMyyyy")));

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        double budgetAmount = resultSet.getDouble("budgetAmount");
                        double totalExpenses = resultSet.getDouble("totalExpenses");
                        double remainingBalance = budgetAmount - totalExpenses;

                        budgetLabel.setText("Budget: " + budgetAmount);
                        expensesLabel.setText("Total Expenses: " + totalExpenses);
                        remainingLabel.setText("Remaining Balance: " + remainingBalance);
                    } else {
                        budgetLabel.setText("No budget data available for the current month.");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception appropriately in a real application
            budgetLabel.setText("Error fetching balance information.");
        }
    }

    @FXML
    private void closeWindow() {
        Stage stage = (Stage) budgetLabel.getScene().getWindow();
        stage.close();
    }
}