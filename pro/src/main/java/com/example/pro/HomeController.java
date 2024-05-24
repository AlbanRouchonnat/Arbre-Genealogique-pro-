package com.example.pro;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import java.io.IOException;

public class HomeController {
    Main m = new Main();

    @FXML
    private void handleSeConnecter(ActionEvent event) throws IOException {
        // Logique pour le bouton "Se connecter"
        // Par exemple, afficher une nouvelle fenêtre pour la connexion
        m.changeScene("sample.fxml",600,400);
    }

    @FXML
    private void handleSInscrire(ActionEvent event) throws IOException{
        // Logique pour le bouton "S'inscrire"
        // Par exemple, afficher une nouvelle fenêtre pour l'inscription
        m.changeScene("SignUp.fxml",600,800);
    }
    @FXML
    private void admin(ActionEvent event)throws IOException{
        m.changeScene("/com/example/pro/Admin.fxml",600,800);
    }
}
