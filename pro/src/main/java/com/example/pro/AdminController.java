package com.example.pro;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class AdminController {

    @FXML
    private TableView<Person> personTable;
    @FXML
    private TableColumn<Person, String> firstNameColumn;
    @FXML
    private TableColumn<Person, String> nameColumn;
    @FXML
    private TableColumn<Person, String> nationalityColumn;
    @FXML
    private TableColumn<Person, String> socialSecurityNumberColumn;
    @FXML
    private TableColumn<Person, String> dateOfBirthColumn;
    @FXML
    private Button validateButton;

    private ObservableList<Person> personData = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        // Initialize the person table with the columns.
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        nationalityColumn.setCellValueFactory(new PropertyValueFactory<>("nationality"));
        socialSecurityNumberColumn.setCellValueFactory(new PropertyValueFactory<>("socialSecurityNumber"));
        dateOfBirthColumn.setCellValueFactory(new PropertyValueFactory<>("dateOfBirth"));

        // Load person data from JSON
        loadPersonDataFromFile();
        personTable.setItems(personData);
    }

    private void loadPersonDataFromFile() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            List<Person> persons = objectMapper.readValue(new File("UserValidation.json"), new TypeReference<List<Person>>(){});
            personData.addAll(persons);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleValidate() {
        // Handle validation logic here
    }
}
