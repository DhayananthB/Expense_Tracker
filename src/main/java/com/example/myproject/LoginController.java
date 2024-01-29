package com.example.myproject;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Hyperlink;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import java.io.IOException;
import java.sql.*;
import javafx.scene.control.Alert;

public class LoginController {
    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;



    @FXML
    protected void onLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        // Perform authentication logic here
        if (isValidUser(username, password)) {
            // Close the login window
            UserSession.getInstance().setUsername(username);
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.close();

            // Open the main application window
            openMainApplication();
        } else {
            // Display authentication error (show an alert, etc.)
            //System.out.println("Invalid credentials");
            showErrorMessage("Enter correct credentials");
        }
    }

    private boolean isValidUser(String username, String password) {
        // JDBC connection parameters (update these based on your database)
        String url = "jdbc:mysql://localhost:3306/expenses";
        String dbUsername = "root";
        String dbPassword = "password";

        try (Connection connection = DriverManager.getConnection(url, dbUsername, dbPassword)) {
            // Check the provided credentials against the database
            String query = "SELECT * FROM users WHERE username = ? AND password = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, username);
                statement.setString(2, password);

                ResultSet resultSet = statement.executeQuery();
                return resultSet.next(); // If there's a matching user, return true
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception appropriately in a real application
        }

        return false;
    }

    private void openMainApplication() {
        // Load and show the main application window
        try {
            Stage mainStage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("hello-view.fxml"));
            mainStage.setScene(new Scene(loader.load()));
            mainStage.setTitle("Main Application");
            mainStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private Hyperlink registerLink;

    @FXML
    protected void onRegisterLinkClick() {
        openRegister();
    }

    private void openRegister() {
        try {
            Stage registerStage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("RegisterView.fxml"));
            registerStage.setScene(new Scene(loader.load()));
            registerStage.setTitle("Register");
            registerStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void showErrorMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}