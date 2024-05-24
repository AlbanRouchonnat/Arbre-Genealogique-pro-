package com.example.pro;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class SignUpController {

    @FXML
    private TextField socialSecurityNumberField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField nameField;

    @FXML
    private TextField firstNameField;

    @FXML
    private Label wrongSignUp;

    @FXML
    private DatePicker dateNaissancePicker; // Correspond à dateOfBirthField

    @FXML
    private TextField nationaliteField; // Correspond à nationalityField

    @FXML
    private Button ciButton;

    @FXML
    private Button photoButton;

    private File carteIdentiteFile;
    private File photoFile;

    @FXML
    private void handleCiButtonAction() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir une Carte d'Identité");
        carteIdentiteFile = fileChooser.showOpenDialog(ciButton.getScene().getWindow());
    }

    @FXML
    private void handlePhotoButtonAction() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir une Photo Numérique");
        photoFile = fileChooser.showOpenDialog(photoButton.getScene().getWindow());
    }

    @FXML
    private void handleSubmitButtonAction() throws IOException {
        if (validateFields() && carteIdentiteFile != null && photoFile != null) {
            handleAddPerson();
            System.out.println("Form submitted successfully!");
        } else {
            System.out.println("Please fill all fields and select the files.");
            wrongSignUp.setText("Please fill all fields and select the files.");
        }
    }

    private boolean validateFields() {
        return !socialSecurityNumberField.getText().isEmpty() &&
                !nameField.getText().isEmpty() &&
                !firstNameField.getText().isEmpty() &&
                dateNaissancePicker.getValue() != null &&
                !nationaliteField.getText().isEmpty();
    }

    @FXML
    private void goHome() throws IOException {
        Main m = new Main();
        m.changeScene("Home.fxml", 600, 400);
    }

    @FXML
    private void handleAddPerson() throws IOException {
        String socialSecurityNumber = socialSecurityNumberField.getText();
        String name = nameField.getText();
        String firstName = firstNameField.getText();
        LocalDate dateOfBirth = dateNaissancePicker.getValue();
        String nationality = nationaliteField.getText();
        String secretCode = UUID.randomUUID().toString(); // Récupérer le secretCode
        String password = passwordField.getText();

        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
        String formattedDateOfBirth = dateOfBirth.format(formatter);

        Main main = new Main();
        main.addPerson(socialSecurityNumber, name, firstName, formattedDateOfBirth, nationality, secretCode, password);
    }
}
