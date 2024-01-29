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


public class ViewSavings {

    @FXML
    private Label budgetLabel;



    @FXML
    private void initialize() {
        // Fetch and display the balance information
        String username = UserSession.getInstance().getUsername();

        // JDBC connection parameters (update these based on your database)
        String url = "jdbc:mysql://localhost:3306/expenses";
        String dbUsername = "root";
        String dbPassword = "password";

        try (Connection connection = DriverManager.getConnection(url, dbUsername, dbPassword)) {
            String query = "SELECT " +
                    "COALESCE(SUM(CASE WHEN t.type = 'deposit' THEN t.Amount ELSE 0 END), 0) AS totalIncome, " +
                    "COALESCE(SUM(CASE WHEN t.type = 'expense' THEN t.Amount ELSE 0 END), 0) AS totalExpenses " +
                    "FROM transaction t " +
                    "WHERE t.username = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, username);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        double totalIncome = resultSet.getDouble("totalIncome");
                        double totalExpenses = resultSet.getDouble("totalExpenses");
                        double difference = totalIncome - totalExpenses;


                        budgetLabel.setText("Total Savings: ₹" + difference);
                    } else {
                        budgetLabel.setText("No transaction data available for the current user.");
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
