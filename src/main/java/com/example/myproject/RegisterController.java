package com.example.myproject;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import java.io.IOException;
import javafx.scene.control.Alert;

import java.sql.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RegisterController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField retypePasswordField;

    @FXML
    protected void onRegister() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String retypePassword = retypePasswordField.getText();

        // Validate input
        if (username.isEmpty() || password.isEmpty() || retypePassword.isEmpty()) {
            showErrorMessage("Please fill in all fields.");
            return;
        }

        // Check if the entered password matches the retype password
        if (!password.equals(retypePassword)) {
            showErrorMessage("Passwords do not match. Please re-enter.");
            return;
        }

        // Check if the username is unique in the database
        if (!isUsernameUnique(username)) {
            showErrorMessage("Username already exists. Please choose a different username.");
            return;
        }

        // Hash the password (you should use a secure password hashing algorithm)
        String hashedPassword = hashPassword(password);

        // Perform registration logic here - store the username and hashed password in the database
        registerUserInDatabase(username, hashedPassword);

        // Close the registration window
        //openLoginPage();
        showSuccessMessage("Registration successful! You can now log in.");
        closeDialog();
    }

    private boolean isUsernameUnique(String username) {
        // JDBC connection parameters (update these based on your database)
        String url = "jdbc:mysql://localhost:3306/expenses";
        String dbUsername = "root";
        String dbPassword = "password";

        try (Connection connection = DriverManager.getConnection(url, dbUsername, dbPassword)) {
            String checkUsernameQuery = "SELECT * FROM users WHERE username = ?";
            try (PreparedStatement checkUsernameStatement = connection.prepareStatement(checkUsernameQuery)) {
                checkUsernameStatement.setString(1, username);
                ResultSet resultSet = checkUsernameStatement.executeQuery();
                return !resultSet.next(); // Return true if the username is unique
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Handle the exception appropriately in a real application
        }
    }

    private String hashPassword(String password) {
        // Implement a secure password hashing algorithm (e.g., bcrypt) here
        // For simplicity, we'll just return the plain password for now
        return password;
    }

    private void registerUserInDatabase(String username, String hashedPassword) {
        // JDBC connection parameters (update these based on your database)
        String url = "jdbc:mysql://localhost:3306/expenses";
        String dbUsername = "root";
        String dbPassword = "password";

        try (Connection connection = DriverManager.getConnection(url, dbUsername, dbPassword)) {
            String insertUserQuery = "INSERT INTO users (username, password) VALUES (?, ?)";
            try (PreparedStatement insertUserStatement = connection.prepareStatement(insertUserQuery)) {
                insertUserStatement.setString(1, username);
                insertUserStatement.setString(2, hashedPassword);
                insertUserStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception appropriately in a real application
        }
    }

    private void showErrorMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    private void openLoginPage() {
        try {
            Stage loginStage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Login.fxml"));
            loginStage.setScene(new Scene(loader.load()));
            loginStage.setTitle("Login");
            loginStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
}
    private void closeDialog() {
        // Close the registration window
        Stage stage = (Stage) usernameField.getScene().getWindow();
        stage.close();
    }
    private void showSuccessMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
