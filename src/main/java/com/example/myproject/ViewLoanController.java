package com.example.myproject;

import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class ViewLoanController {

    @FXML
    private PieChart expensePieChart;

    @FXML
    private Label welcomeText;

    String username = UserSession.getInstance().getUsername();

    @FXML
    protected void initialize() {
        populateExpensePieChart();
    }

    private Connection establishConnection() throws SQLException {
        String jdbcUrl = "jdbc:mysql://localhost:3306/expenses"; // Replace with your actual database URL
        String dbUsername = "root"; // Replace with your actual database username
        String dbPassword = "password"; // Replace with your actual database password
        return DriverManager.getConnection(jdbcUrl, dbUsername, dbPassword);
    }

    private void populateExpensePieChart() {
        try (Connection connection = establishConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT d.category, SUM(t.Amount) AS totalAmount " +
                             "FROM debit d " +
                             "JOIN transaction t ON d.tid = t.tid " +
                             "WHERE t.type = 'expense' AND MONTH(t.DateofTrans) = MONTH(CURRENT_DATE()) " +
                             "AND t.username = ? " +
                             "GROUP BY d.category")) {

            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();

            Map<String, Double> categoryAmountMap = new HashMap<>();
            double totalAmount = 0;

            while (resultSet.next()) {
                String category = resultSet.getString("category");
                double amount = resultSet.getDouble("totalAmount");
                categoryAmountMap.put(category, amount);
                totalAmount += amount;
            }

            // Populate the PieChart with data and percentages
            for (Map.Entry<String, Double> entry : categoryAmountMap.entrySet()) {
                double percentage = (entry.getValue() / totalAmount) * 100;
                String labelText = String.format("%s %.2f%%", entry.getKey(), percentage);
                PieChart.Data pieChartData = new PieChart.Data(labelText, entry.getValue());
                expensePieChart.getData().add(pieChartData);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void onCloseButtonClick() {
        // Close the stage when the close button is clicked
        Stage stage = (Stage) expensePieChart.getScene().getWindow();
        stage.close();
    }
}
