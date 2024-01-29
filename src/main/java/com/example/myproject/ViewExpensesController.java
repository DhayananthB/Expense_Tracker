package com.example.myproject;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import java.util.ArrayList;
import java.util.List;
import java.sql.*;

public class ViewExpensesController {

    @FXML
    private TableView<ExpenseEntry> expensesTable;

    @FXML
    private TableColumn<ExpenseEntry, String> dateColumn;

    @FXML
    private TableColumn<ExpenseEntry, Double> amountColumn;

    @FXML
    private TableColumn<ExpenseEntry, String> categoryColumn;

    @FXML
    private TableColumn<ExpenseEntry, String> remarksColumn;

    public void initialize() {
        // Set up the columns
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("dateOfTrans"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        remarksColumn.setCellValueFactory(new PropertyValueFactory<>("remarks"));

        // Fetch and populate the data
        List<ExpenseEntry> expenseEntries = fetchDataFromDatabase(); // Implement this method
        expensesTable.getItems().addAll(expenseEntries);
    }

    // Create a class representing the data for each row
    public static class ExpenseEntry {
        private String dateOfTrans;
        private Double amount;
        private String category;
        private String remarks;

        public ExpenseEntry(String dateOfTrans, Double amount, String category, String remarks) {
            this.dateOfTrans = dateOfTrans;
            this.amount = amount;
            this.category = category;
            this.remarks = remarks;
        }

        // Add getters and setters here

        public String getDateOfTrans() {
            return dateOfTrans;
        }

        public void setDateOfTrans(String dateOfTrans) {
            this.dateOfTrans = dateOfTrans;
        }

        public Double getAmount() {
            return amount;
        }

        public void setAmount(Double amount) {
            this.amount = amount;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public String getRemarks() {
            return remarks;
        }

        public void setRemarks(String remarks) {
            this.remarks = remarks;
        }
    }

    // Implement your logic to fetch data from the tables and create a list of ExpenseEntry objects
    private List<ExpenseEntry> fetchDataFromDatabase() {
        List<ExpenseEntry> expenseEntries = new ArrayList<>();

        // JDBC connection parameters (update these based on your database)
        String url = "jdbc:mysql://localhost:3306/expenses";
        String username = "root";
        String password = "password";
        String loginusername = UserSession.getInstance().getUsername();

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            String query = "SELECT t.DateofTrans, t.Amount, t.type, d.category, d.remarks " +
                    "FROM transaction t " +
                    "JOIN debit d ON t.tid = d.tid " +
                    "WHERE t.username = ? " +
                    "ORDER BY t.DateofTrans DESC";

            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                // Set the username parameter (replace "your_username" with the actual username)
                preparedStatement.setString(1,loginusername );

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        String dateOfTrans = resultSet.getString("DateofTrans");
                        Double amount = resultSet.getDouble("Amount");
                        String type = resultSet.getString("type");
                        String category = resultSet.getString("category");
                        String remarks = resultSet.getString("remarks");

                        // Check if the type is "Expense" before adding to the list
                        if ("Expense".equals(type)) {
                            expenseEntries.add(new ExpenseEntry(dateOfTrans, amount, remarks, category));
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception appropriately in a real application
        }

        return expenseEntries;
    }
}
