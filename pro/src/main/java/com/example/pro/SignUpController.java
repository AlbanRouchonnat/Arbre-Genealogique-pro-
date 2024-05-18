package com.example.pro;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import java.io.File;

public class SignUpController {

    @FXML
    private TextField ssnField;

    @FXML
    private TextField nomField;

    @FXML
    private TextField prenomField;

    @FXML
    private DatePicker dateNaissancePicker;

    @FXML
    private TextField nationaliteField;

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
    private void handleSubmitButtonAction() {
        if (validateFields() && carteIdentiteFile != null && photoFile != null) {
            // Logique pour soumettre les données du formulaire et les fichiers
            System.out.println("Form submitted successfully!");
        } else {
            System.out.println("Please fill all fields and select the files.");
        }
    }

    private boolean validateFields() {
        return !ssnField.getText().isEmpty() &&
                !nomField.getText().isEmpty() &&
                !prenomField.getText().isEmpty() &&
                dateNaissancePicker.getValue() != null &&
                !nationaliteField.getText().isEmpty();
    }
}
