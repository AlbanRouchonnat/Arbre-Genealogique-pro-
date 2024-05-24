package com.example.pro;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;

import java.io.IOException;
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
        // Initialisation de l'arbre généalogique avec un exemple de membre racine
        VBox rootMember = createPersonBox("Root Member");
        rootMember.setLayoutX(350);
        rootMember.setLayoutY(50);
        familyTreePane.getChildren().add(rootMember);
        memberNodes.put("Root Member", rootMember);
    }

    @FXML
    private void handleAddChildAction() {
        if (selectedMember == null) {
            showAlert("No Selection", "Please select a family member to add a child.");
            return;
        }

        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Add Child");
        dialog.setHeaderText("Add a new child");
        dialog.setContentText("Please enter the name of the new child:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(name -> {
            VBox newChild = createPersonBox(name);
            familyTreePane.getChildren().add(newChild);
            memberNodes.put(name, newChild);
            positionNodes(selectedMember, newChild);
            drawLine(selectedMember, newChild);
        });
    }

    @FXML
    private void goHome(ActionEvent event) throws IOException {
        Main mainApp = new Main();
        mainApp.changeScene("Home.fxml", 600, 400);
    }

    @FXML
    private void handleAddParentAction() {
        if (selectedMember == null) {
            showAlert("No Selection", "Please select a family member to add a parent.");
            return;
        }

        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Add Parent");
        dialog.setHeaderText("Add a new parent");
        dialog.setContentText("Please enter the name of the new parent:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(name -> {
            VBox newParent = createPersonBox(name);
            familyTreePane.getChildren().add(newParent);
            memberNodes.put(name, newParent);
            positionNodes(newParent, selectedMember);
            drawLine(newParent, selectedMember);
        });
    }

    @FXML
    private void handleEditMemberAction() {
        if (selectedMember == null) {
            showAlert("No Selection", "Please select a family member to edit.");
            return;
        }

        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Edit Member");
        dialog.setHeaderText("Edit the member's name");
        dialog.setContentText("Please enter the new name:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(name -> {
            Text nameLabel = (Text) selectedMember.getChildren().get(0);
            String oldName = nameLabel.getText();
            nameLabel.setText(name);
            memberNodes.remove(oldName);
            memberNodes.put(name, selectedMember);
        });
    }

    @FXML
    private void handleRemoveMemberAction() {
        if (selectedMember == null) {
            showAlert("No Selection", "Please select a family member to remove.");
            return;
        }

        familyTreePane.getChildren().remove(selectedMember);
        memberNodes.values().remove(selectedMember);
        selectedMember = null;
    }

    private VBox createPersonBox(String name) {
        VBox personBox = new VBox(5);
        Text nameLabel = new Text(name);
        personBox.getChildren().add(nameLabel);
        personBox.setStyle("-fx-border-color: black; -fx-padding: 10;");
        personBox.setOnMouseClicked(event -> selectedMember = personBox);

        return personBox;
    }

    private void positionNodes(VBox parent, VBox child) {
        child.setLayoutX(parent.getLayoutX() + 100); // Adjust the position
        child.setLayoutY(parent.getLayoutY() + 100); // Adjust the position
    }

    private void drawLine(VBox parent, VBox child) {
        Line line = new Line(
                parent.getLayoutX() + parent.getWidth() / 2, parent.getLayoutY() + parent.getHeight(),
                child.getLayoutX() + child.getWidth() / 2, child.getLayoutY()
        );
        line.setStroke(Color.BLACK);
        familyTreePane.getChildren().add(line);
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
