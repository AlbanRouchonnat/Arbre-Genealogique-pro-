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
    public void userLogIn(ActionEvent event) throws IOException {
        checkLogin();
    }

    private void checkLogin() throws IOException {
        Main m = new Main();

        if (username.getText().toString().equals("javacoding") && password.getText().toString().equals("123")) {
            wrongLogin.setText("Success!");
            m.changeScene("afterLogin.fxml");
        } else if (username.getText().isEmpty() && password.getText().isEmpty()) {
            wrongLogin.setText("Please enter your data.");
        } else {
            wrongLogin.setText("Wrong username or password!");
        }
    }
}
