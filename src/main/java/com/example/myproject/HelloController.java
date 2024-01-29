package com.example.myproject;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;  // Import FXMLLoader
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
public class HelloController {
    @FXML
    private Label welcomeText;
    @FXML
    protected void onLoginButtonClick() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Login.fxml"));
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Login");
            stage.setScene(new Scene(loader.load()));
            stage.showAndWait();
            stage.setResizable(false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void onAddExpenseButtonClick() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ExpenseDialog.fxml"));
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Add Expense");
            stage.setScene(new Scene(loader.load()));
            stage.showAndWait();
            stage.setResizable(false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void onAddIncomeButtonClick() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("IncomeDialog.fxml"));
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Add income");
            stage.setScene(new Scene(loader.load()));
            stage.showAndWait();
            stage.setResizable(false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void onViewExpensesButtonClick() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("view-expenses.fxml"));
             loader.setController(new ViewExpensesController()); // Set the controller
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("View Expense");
            stage.setScene(new Scene(loader.load()));
            stage.showAndWait();
            stage.setResizable(false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    protected void onSetBudgetButtonClick() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("set-budget-view.fxml"));
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Set Budget");
            stage.setScene(new Scene(loader.load()));
            stage.showAndWait();
            stage.setResizable(false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    protected void onViewBalanceButtonClick() {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("view-balance.fxml"));
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("View Balance");
            stage.setScene(new Scene(loader.load()));
            stage.showAndWait();
            stage.setResizable(false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void onViewSavingsButtonClick() {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("view-savings.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Money in Hand");
            stage.setScene(new Scene(loader.load()));
            stage.showAndWait();
            stage.setResizable(false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    protected void onViewLoanButtonClick() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("view-loan.fxml"));
            Stage stage = new Stage();
            stage.setTitle("This Month Expenses");
            stage.setScene(new Scene(loader.load()));
            stage.showAndWait();
            stage.setResizable(false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
