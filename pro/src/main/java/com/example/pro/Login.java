package com.example.pro;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;

public class Login {

    @FXML
    private Button button;

    @FXML
    private Label wrongLogin;

    @FXML
    private TextField username;

    @FXML
    private PasswordField password;

    @FXML
    private Button Home;

    @FXML
    public void userLogIn(ActionEvent event) throws IOException {
        checkLogin();
    }

    private void checkLogin() throws IOException {
        Main mainApp = new Main();

        if (username.getText().isEmpty() || password.getText().isEmpty()) {
            wrongLogin.setText("Please enter your data.");
        } else if (mainApp.checkUsername(username.getText(), password.getText())) {
            wrongLogin.setText("Success!");
            mainApp.changeScene("afterLogin.fxml", 1500, 900);
        } else {
            wrongLogin.setText("Wrong username or password!");
        }
    }

    @FXML
    private void goHome(ActionEvent event) throws IOException {
        Main mainApp = new Main();
        mainApp.changeScene("Home.fxml", 800, 600);
    }
}
