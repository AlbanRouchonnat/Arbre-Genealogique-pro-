
package com.example.pro;
import java.nio.file.Path;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.geometry.Insets;
import org.json.JSONArray;
import org.json.JSONObject;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class AfterLogin{

    @FXML
    private AnchorPane familyTreePane;

    private Map<String, VBox> memberNodes = new HashMap<>();
    private VBox selectedMember;



    @FXML
    public void initialize() {
        // Exemple de données pour l'utilisateur racine
        Map<String, String> rootUserData = new HashMap<>();
        rootUserData.put("name", UserInfo.name);
        rootUserData.put("firstName", UserInfo.firstname);
        rootUserData.put("nationality", UserInfo.SSN);
        rootUserData.put("socialSecurityNumber", UserInfo.nationality);
        rootUserData.put("dateOfBirth", UserInfo.dateOfBirth);

        VBox rootMember = createPersonBox(rootUserData);
        rootMember.setLayoutX(500);
        rootMember.setLayoutY(300);
        familyTreePane.getChildren().add(rootMember);
        memberNodes.put(UserInfo.firstname, rootMember);
    }
    private static final String JSON_FILE_PATH = "Link.json";

    Main m=new Main();

    @FXML
    private void handleAddChildAction() throws IOException {
        if (selectedMember == null) {
            showAlert("No Selection", "Please select a family member to add a child.");
            return;
        }

        // Récupérer le nom du parent à partir de selectedMember
        String parentName = ((Label) selectedMember.getChildren().get(0)).getText().replace("Name: ", "");

        // Créer la boîte de dialogue
        Dialog<Map<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Add Child");
        dialog.setHeaderText("Add a new child");

        // Définir les boutons
        ButtonType okButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);

        // Créer les champs de texte pour l'entrée utilisateur
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField nameField = new TextField();
        nameField.setPromptText("Name");
        TextField firstNameField = new TextField();
        firstNameField.setPromptText("First Name");
        TextField nationalityField = new TextField();
        nationalityField.setPromptText("Nationality");
        TextField ssnField = new TextField();
        ssnField.setPromptText("Social Security Number");
        TextField dobField = new TextField();
        dobField.setPromptText("Date of Birth");

        grid.add(new Label("Name:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("First Name:"), 0, 1);
        grid.add(firstNameField, 1, 1);
        grid.add(new Label("Nationality:"), 0, 2);
        grid.add(nationalityField, 1, 2);
        grid.add(new Label("Social Security Number:"), 0, 3);
        grid.add(ssnField, 1, 3);
        grid.add(new Label("Date of Birth:"), 0, 4);
        grid.add(dobField, 1, 4);

        dialog.getDialogPane().setContent(grid);

        // Convertir le résultat en une map de valeurs
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == okButtonType) {
                if (nameField.getText().isEmpty() || firstNameField.getText().isEmpty() ||
                        nationalityField.getText().isEmpty() || ssnField.getText().isEmpty() ||
                        dobField.getText().isEmpty()) {
                    showAlert("Invalid Input", "All fields must be filled out.");
                    return null; // Pour garder la boîte de dialogue ouverte
                }
                Map<String, String> data = new HashMap<>();
                data.put("name", nameField.getText());
                data.put("firstName", firstNameField.getText());
                data.put("nationality", nationalityField.getText());
                data.put("socialSecurityNumber", ssnField.getText());
                data.put("dateOfBirth", dobField.getText());
                return data;
            }
            return null;
        });

        Optional<Map<String, String>> result = dialog.showAndWait();
        result.ifPresent(data -> {
            VBox newChild = createPersonBox(data);
            familyTreePane.getChildren().add(newChild);
            memberNodes.put(data.get("name"), newChild);
            positionNodesChild(selectedMember, newChild);
            drawLine(selectedMember, newChild);
            //System.out.println("New child added: " + data.get("name"));

            // Appeler writeLink avec le nom du parent
            m.writeLink(parentName, data.get("name"), "child");
            m.addPersonDirect(data.get("socialSecurityNumber"), data.get("name"), data.get("firstName"), data.get("dateOfBirth"), data.get("nationality"), "", "");
        });
    }

    @FXML
    private void goHome(ActionEvent event) throws IOException {
        Main mainApp = new Main();
        mainApp.changeScene("Home.fxml", 600, 400);
    }

    @FXML
    private void handleAddParentAction() throws IOException {
        if (selectedMember == null) {
            showAlert("No Selection", "Please select a family member to add a parent.");
            return;
        }

        // Récupérer le nom de l'enfant à partir de selectedMember
        String childName = ((Label) selectedMember.getChildren().get(0)).getText().replace("Name: ", "");
        System.out.println((childName));
        // Vérifier le nombre de parents existants
        int parentCount = m.countRelations(childName, "parent");
        if (parentCount >= 2) {
            showAlert("Limit Reached", "This member already has two parents.");
            return;
        }

        // Créer la boîte de dialogue
        Dialog<Map<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Add Parent");
        dialog.setHeaderText("Add a new parent");

        // Définir les boutons
        ButtonType okButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);

        // Créer les champs de texte pour l'entrée utilisateur
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField nameField = new TextField();
        nameField.setPromptText("Name");
        TextField firstNameField = new TextField();
        firstNameField.setPromptText("First Name");
        TextField nationalityField = new TextField();
        nationalityField.setPromptText("Nationality");
        TextField ssnField = new TextField();
        ssnField.setPromptText("Social Security Number");
        TextField dobField = new TextField();
        dobField.setPromptText("Date of Birth");
        TextField passwordField = new TextField();
        passwordField.setPromptText("Password");

        grid.add(new Label("Name:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("First Name:"), 0, 1);
        grid.add(firstNameField, 1, 1);
        grid.add(new Label("Nationality:"), 0, 2);
        grid.add(nationalityField, 1, 2);
        grid.add(new Label("Social Security Number:"), 0, 3);
        grid.add(ssnField, 1, 3);
        grid.add(new Label("Date of Birth:"), 0, 4);
        grid.add(dobField, 1, 4);

        dialog.getDialogPane().setContent(grid);

        // Convertir le résultat en une map de valeurs
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == okButtonType) {
                if (nameField.getText().isEmpty() || firstNameField.getText().isEmpty() ||
                        nationalityField.getText().isEmpty() || ssnField.getText().isEmpty() ||
                        dobField.getText().isEmpty()) {
                    showAlert("Invalid Input", "All fields must be filled out.");
                    return null; // Pour garder la boîte de dialogue ouverte
                }
                Map<String, String> data = new HashMap<>();
                data.put("name", nameField.getText());
                data.put("firstName", firstNameField.getText());
                data.put("nationality", nationalityField.getText());
                data.put("socialSecurityNumber", ssnField.getText());
                data.put("dateOfBirth", dobField.getText());
                return data;
            }
            return null;
        });

        Optional<Map<String, String>> result = dialog.showAndWait();
        result.ifPresent(data -> {
            VBox newParent = createPersonBox(data);
            familyTreePane.getChildren().add(newParent);
            memberNodes.put(data.get("name"), newParent);
            positionNodes( selectedMember,newParent);
            drawLine(newParent, selectedMember);
            //System.out.println("New parent added: " + data.get("name"));

            // Appeler writeLink avec le nom de l'enfant
            m.writeLink(childName,data.get("name"), "parent");
            m.addPersonDirect(data.get("socialSecurityNumber"), data.get("name"), data.get("firstName"), data.get("dateOfBirth"), data.get("nationality"), "", "");
        });
    }

    @FXML
    private void handleAddSpouseAction() throws IOException {
        if (selectedMember == null) {
            showAlert("No Selection", "Please select a family member to add a spouse.");
            return;
        }

        // Récupérer le nom de l'enfant à partir de selectedMember
        String childName = ((Label) selectedMember.getChildren().get(0)).getText().replace("Name: ", "");
        System.out.println((childName));
        // Vérifier le nombre de parents existants
        int spouseCount = m.countRelations(childName, "spouse");
        if (spouseCount >= 1) {
            showAlert("Limit Reached", "This member already has one spouse.");
            return;
        }

        // Créer la boîte de dialogue
        Dialog<Map<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Add Spouse");
        dialog.setHeaderText("Add a new Spouse");

        // Définir les boutons
        ButtonType okButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);

        // Créer les champs de texte pour l'entrée utilisateur
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField nameField = new TextField();
        nameField.setPromptText("Name");
        TextField firstNameField = new TextField();
        firstNameField.setPromptText("First Name");
        TextField nationalityField = new TextField();
        nationalityField.setPromptText("Nationality");
        TextField ssnField = new TextField();
        ssnField.setPromptText("Social Security Number");
        TextField dobField = new TextField();
        dobField.setPromptText("Date of Birth");
        TextField passwordField = new TextField();
        passwordField.setPromptText("Password");

        grid.add(new Label("Name:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("First Name:"), 0, 1);
        grid.add(firstNameField, 1, 1);
        grid.add(new Label("Nationality:"), 0, 2);
        grid.add(nationalityField, 1, 2);
        grid.add(new Label("Social Security Number:"), 0, 3);
        grid.add(ssnField, 1, 3);
        grid.add(new Label("Date of Birth:"), 0, 4);
        grid.add(dobField, 1, 4);

        dialog.getDialogPane().setContent(grid);

        // Convertir le résultat en une map de valeurs
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == okButtonType) {
                if (nameField.getText().isEmpty() || firstNameField.getText().isEmpty() ||
                        nationalityField.getText().isEmpty() || ssnField.getText().isEmpty() ||
                        dobField.getText().isEmpty()) {
                    showAlert("Invalid Input", "All fields must be filled out.");
                    return null; // Pour garder la boîte de dialogue ouverte
                }
                Map<String, String> data = new HashMap<>();
                data.put("name", nameField.getText());
                data.put("firstName", firstNameField.getText());
                data.put("nationality", nationalityField.getText());
                data.put("socialSecurityNumber", ssnField.getText());
                data.put("dateOfBirth", dobField.getText());
                return data;
            }
            return null;
        });

        Optional<Map<String, String>> result = dialog.showAndWait();
        result.ifPresent(data -> {
            VBox newSpouse = createPersonBox(data);
            familyTreePane.getChildren().add(newSpouse);
            memberNodes.put(data.get("name"), newSpouse);
            positionNodesSpouse( selectedMember,newSpouse);
            drawLine(newSpouse, selectedMember);
            //System.out.println("New parent added: " + data.get("name"));

            // Appeler writeLink avec le nom de l'enfant
            m.writeLink(childName,data.get("name"), "spouse");
            m.addPersonDirect(data.get("socialSecurityNumber"), data.get("name"), data.get("firstName"), data.get("dateOfBirth"), data.get("nationality"), "", "");
        });
    }



    @FXML
    private void handleEditMemberAction() {
        if (selectedMember == null) {
            showAlert("No Selection", "Please select a family member to edit.");
            return;
        }

        // Créer la boîte de dialogue
        Dialog<Map<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Edit Member");
        dialog.setHeaderText("Edit the member's information");

        // Définir les boutons
        ButtonType okButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);

        // Créer les champs de texte pour l'entrée utilisateur
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        // Extraire les informations actuelles du membre sélectionné
        Label nameLabel = (Label) selectedMember.getChildren().get(0);
        Label firstNameLabel = (Label) selectedMember.getChildren().get(1);
        Label nationalityLabel = (Label) selectedMember.getChildren().get(2);
        Label ssnLabel = (Label) selectedMember.getChildren().get(3);
        Label dobLabel = (Label) selectedMember.getChildren().get(4);

        TextField nameField = new TextField(nameLabel.getText().split(": ")[1]);
        TextField firstNameField = new TextField(firstNameLabel.getText().split(": ")[1]);
        TextField nationalityField = new TextField(nationalityLabel.getText().split(": ")[1]);
        TextField ssnField = new TextField(ssnLabel.getText().split(": ")[1]);
        TextField dobField = new TextField(dobLabel.getText().split(": ")[1]);

        grid.add(new Label("First Name:"), 0, 1);
        grid.add(firstNameField, 1, 1);
        grid.add(new Label("Nationality:"), 0, 2);
        grid.add(nationalityField, 1, 2);
        grid.add(new Label("Social Security Number:"), 0, 3);
        grid.add(ssnField, 1, 3);
        grid.add(new Label("Date of Birth:"), 0, 4);
        grid.add(dobField, 1, 4);

        dialog.getDialogPane().setContent(grid);

        // Convertir le résultat en une map de valeurs
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == okButtonType) {
                Map<String, String> data = new HashMap<>();
                //data.put("name", nameField.getText());
                data.put("firstName", firstNameField.getText());
                data.put("nationality", nationalityField.getText());
                data.put("socialSecurityNumber", ssnField.getText());
                data.put("dateOfBirth", dobField.getText());
                return data;
            }
            return null;
        });
        Optional<Map<String, String>> result = dialog.showAndWait();
        result.ifPresent(data -> {
            //nameLabel.setText("Name: " + data.get("name"));
            firstNameLabel.setText("First Name: " + data.get("firstName"));
            nationalityLabel.setText("Nationality: " + data.get("nationality"));
            ssnLabel.setText("SSN: " + data.get("socialSecurityNumber"));
            dobLabel.setText("Date of Birth: " + data.get("dateOfBirth"));

            String oldName = nameLabel.getText().split(": ")[1];
            memberNodes.remove(oldName);
            memberNodes.put(data.get("name"), selectedMember);
        });
        m.editMember(nameField.getText(),  firstNameField.getText(), nationalityField.getText(),dobField.getText(),ssnField.getText() );
    }


    @FXML
    private void handleRemoveMemberAction() throws IOException {
        if (selectedMember == null) {
            showAlert("No Selection", "Please select a family member to remove.");
            return;
        }
        Label nameLabel = (Label) selectedMember.getChildren().get(0);
        TextField nameField = new TextField(nameLabel.getText().split(": ")[1]);
        m.suppPerson(nameField.getText());
        m.suppLink(nameField.getText());
        familyTreePane.getChildren().remove(selectedMember);
        memberNodes.values().remove(selectedMember);
        selectedMember = null;
    }

    private VBox createPersonBox(Map<String, String> data) {
        VBox personBox = new VBox(5);
        Text nameLabel1 = new Text(data.get("name"));
        Label nameLabel = new Label("Name: " + data.get("name"));
        Label firstNameLabel = new Label("First Name: " + data.get("firstName"));
        Label nationalityLabel = new Label("Nationality: " + data.get("nationality"));
        Label ssnLabel = new Label("SSN: " + data.get("socialSecurityNumber"));
        //Label secretCodeLabel = new Label("Secret Code: " + data.get("secretCode"));
        Label dobLabel = new Label("Date of Birth: " + data.get("dateOfBirth"));
        //Label passwordLabel = new Label("Password: " + data.get("password"));
        personBox.getChildren().addAll(nameLabel, firstNameLabel, nationalityLabel, ssnLabel, dobLabel);
        personBox.setStyle("-fx-border-color: black; -fx-padding: 10;");
        personBox.setOnMouseClicked(event -> selectedMember = personBox);
        return personBox;
    }

    private void positionNodesChild(VBox parent, VBox child) {
        // Récupérer le nom de la VBox parent
        String parentName = ((Label) parent.getChildren().get(0)).getText().replace("Name: ", "");
        //System.out.println(parentName);
        // Utiliser le nom pour compter les relations
        int nbChild = m.countRelations(parentName, "child");

        // Ajuster la position de l'enfant
        child.setLayoutX(parent.getLayoutX() - 200 + 200 * nbChild); // Adjust the position
        child.setLayoutY(parent.getLayoutY() + 200); // Adjust the position

        //System.out.println("Child position set to (" + child.getLayoutX() + ", " + child.getLayoutY() + ")");
    }

    private void positionNodes(VBox parent, VBox child) {
        // Récupérer le nom de la VBox parent
        String parentName = ((Label) parent.getChildren().get(0)).getText().replace("Name: ", "");
        //System.out.println(parentName);
        // Utiliser le nom pour compter les relations
        int nbChild = m.countRelations(parentName, "parent");

        // Ajuster la position de l'enfant
        child.setLayoutX(parent.getLayoutX() - 200 + 400 * nbChild); // Adjust the position
        child.setLayoutY(parent.getLayoutY() - 200); // Adjust the position

        //System.out.println("Child position set to (" + child.getLayoutX() + ", " + child.getLayoutY() + ")");
    }

    private void positionNodesSpouse(VBox parent, VBox child) {
        // Récupérer le nom de la VBox parent
        String parentName = ((Label) parent.getChildren().get(0)).getText().replace("Name: ", "");

        // Ajuster la position de l'enfant
        child.setLayoutX(parent.getLayoutX() +200); // Adjust the position
        child.setLayoutY(parent.getLayoutY()); // Adjust the position

        //System.out.println("Child position set to (" + child.getLayoutX() + ", " + child.getLayoutY() + ")");
    }

    private void drawLine(VBox parent, VBox child) {
        Line line = new Line(
                parent.getLayoutX() + parent.getWidth() / 2, parent.getLayoutY() + parent.getHeight(),
                child.getLayoutX() + child.getWidth() / 2, child.getLayoutY()
        );
        line.setStroke(Color.BLACK);
        familyTreePane.getChildren().add(line);
        //System.out.println("Line drawn from (" + line.getStartX() + ", " + line.getStartY() + ") to (" + line.getEndX() + ", " + line.getEndY() + ")");
    }
    @FXML
    private void handleConsultOtherMemberFamilyTree(){
        Dialog<Map<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Consult Family Tree");
        dialog.setHeaderText("Consult Other Member's Family Tree");

        // Définir les boutons
        ButtonType okButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);

        // Créer les champs de texte pour l'entrée utilisateur
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField nameField = new TextField();
        nameField.setPromptText("Name");

        grid.add(new Label("Name:"), 0, 0);
        grid.add(nameField, 1, 0);

        dialog.getDialogPane().setContent(grid);

        // Convertir le résultat en une map de valeurs
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == okButtonType) {
                if (nameField.getText().isEmpty()) {
                    showAlert("Invalid Input", "All fields must be filled out.");
                    return null; // Pour garder la boîte de dialogue ouverte
                }
                Map<String, String> data = new HashMap<>();
                data.put("name", nameField.getText());
                UserInfo.name = nameField.getText();
                Main mainApp = new Main();
                try {
                    mainApp.changeScene("AfterLogin.fxml", 1500, 900);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                return data;
            }
            return null;
        });

        dialog.showAndWait();
    }
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}