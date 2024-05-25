package com.example.pro;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.net.URISyntaxException;

public class Main extends Application {

    private static Stage stg;
    private static final String JSON_FILE_PATH = "src/main/resources/UserValidation.json";
    private static final String JSON_FILE_PATH2 = "src/main/resources/User.json";
    private static final String JSON_FILE_PATH3 = "src/main/resources/Link.json";

    @Override
    public void start(Stage primaryStage) throws Exception {
        stg = primaryStage;
        primaryStage.setResizable(false);
        Parent root = FXMLLoader.load(getClass().getResource("Home.fxml"));
        primaryStage.setTitle("pro ++");
        primaryStage.setScene(new Scene(root, 800, 540));
        primaryStage.show();
    }

    public void changeScene(String fxml, int width, int height) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(fxml));
        stg.setTitle("pro ++");
        stg.setScene(new Scene(root, width, height));
        stg.show();
    }


    public boolean checkUsername(String secretCode, String passWord) throws IOException {
        try {
            String data = new String(Files.readAllBytes(Paths.get(JSON_FILE_PATH)));

            JSONArray jsonArray = new JSONArray(data);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);

                if (object.has("secretCode") && object.has("passWord")) {
                    String secretCode1 = object.getString("secretCode");
                    String password1 = object.getString("passWord");

                    if (password1.equals(passWord) && secretCode1.equals(secretCode)) {
                        UserInfo.username=object.getString("name");
                        return true;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void addPerson(String socialSecurityNumber, String name, String firstName, String dateOfBirth, String nationality, String secretCode, String password) {
        try {
            Path path = Paths.get(JSON_FILE_PATH);
            String data = new String(Files.readAllBytes(path));
            JSONArray jsonArray = new JSONArray(data);

            JSONObject newPerson = new JSONObject();
            //System.out.println(socialSecurityNumber);
            newPerson.put("socialSecurityNumber", socialSecurityNumber);
            newPerson.put("name", name);
            newPerson.put("firstName", firstName);
            newPerson.put("dateOfBirth", dateOfBirth);
            newPerson.put("nationality", nationality);
            newPerson.put("secretCode", secretCode);
            newPerson.put("passWord", password);

            jsonArray.put(newPerson);

            Files.write(path, jsonArray.toString(2).getBytes());

            System.out.println("New person added successfully.");
            this.changeScene("afterLogin.fxml", 1500, 900);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addPersonDirect(String socialSecurityNumber, String name, String firstName, String dateOfBirth, String nationality, String secretCode, String password) {
        try {
            Path path = Paths.get(JSON_FILE_PATH2);
            String data = new String(Files.readAllBytes(path));
            JSONArray jsonArray = new JSONArray(data);

            JSONObject newPerson = new JSONObject();
            //System.out.println(socialSecurityNumber);
            newPerson.put("socialSecurityNumber", socialSecurityNumber);
            newPerson.put("name", name);
            newPerson.put("firstName", firstName);
            newPerson.put("dateOfBirth", dateOfBirth);
            newPerson.put("nationality", nationality);
            newPerson.put("secretCode", secretCode);
            newPerson.put("passWord", password);

            jsonArray.put(newPerson);

            Files.write(path, jsonArray.toString(2).getBytes());

            System.out.println("New person added successfully.");
            this.changeScene("afterLogin.fxml", 1500, 900);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeLink(String from, String to, String relation) {
        try {
            Path path = Paths.get(JSON_FILE_PATH3);
            String data = new String(Files.readAllBytes(path));
            JSONArray jsonArray = new JSONArray(data);

            JSONObject newLink = new JSONObject();
            //System.out.println(socialSecurityNumber);
            newLink.put("from", from);
            newLink.put("to", to);
            newLink.put("relation", relation);


            jsonArray.put(newLink);

            Files.write(path, jsonArray.toString(2).getBytes());

            System.out.println("New person added successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        launch(args);
    }
}
