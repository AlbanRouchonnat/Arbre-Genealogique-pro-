package com.example.pro;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;

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




    public static int countRelations(String person, String relationType) {
            int count = 0;
            try {
                // Lire le contenu du fichier JSON en tant que chaîne
                String content = new String(Files.readAllBytes(Paths.get(JSON_FILE_PATH3)));
                JSONArray relations = new JSONArray(content);

                // Parcourir les relations et compter les occurrences
                for (int i = 0; i < relations.length(); i++) {
                    JSONObject relation = relations.getJSONObject(i);
                    String from = relation.optString("from", "");
                    String to = relation.optString("to", "");
                    String type = relation.optString("relation", "");

                    if (relationType.equals("parent")) {
                        System.out.println(person);
                        System.out.println(from);
                        if (person.equals(from) && "parent".equals(type)) {
                            count++;
                        }
                        if (person.equals(to) && "child".equals(type)) {
                            count++;
                        }
                    } else if (relationType.equals("child")) {
                        if (person.equals(from) && "child".equals(type)) {
                            count++;
                        }
                        if (person.equals(to) && "parent".equals(type)) {
                            count++;
                        }
                    } else if (relationType.equals("spouse")) {
                        if (person.equals(from) && "spouse".equals(type)) {
                            count++;
                        }
                        if (person.equals(to) && "spouse".equals(type)) {
                            count++;
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println(count);
            return count;
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
                        UserInfo.firstname=object.getString("firstName");
                        UserInfo.name=object.getString("name");
                        UserInfo.SSN=object.getString("socialSecurityNumber");
                        UserInfo.nationality=object.getString("nationality");
                        UserInfo.dateOfBirth=object.getString("dateOfBirth");
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
            UserInfo.firstname=firstName;
            UserInfo.name=name;
            UserInfo.SSN=socialSecurityNumber;
            UserInfo.nationality=nationality;
            UserInfo.dateOfBirth=dateOfBirth;
            //System.out.println("New person added successfully.");
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
            newPerson.put("socialSecurityNumber", socialSecurityNumber);
            newPerson.put("name", name);
            newPerson.put("firstName", firstName);
            newPerson.put("dateOfBirth", dateOfBirth);
            newPerson.put("nationality", nationality);
            newPerson.put("secretCode", secretCode);
            newPerson.put("passWord", password);

            jsonArray.put(newPerson);

            Files.write(path, jsonArray.toString(2).getBytes());

            //System.out.println("New person added successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void suppLink(String name) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        // Lire le contenu du fichier JSON
        List<Link> links = objectMapper.readValue(new File(JSON_FILE_PATH3), new TypeReference<List<Link>>() {});

        // Filtrer les liens à supprimer
        List<Link> updatedLinks = links.stream()
                .filter(link -> !link.getFrom().equals(name) && !link.getTo().equals(name))
                .collect(Collectors.toList());

        // Écrire les liens filtrés dans le fichier JSON
        objectMapper.writeValue(new File(JSON_FILE_PATH3), updatedLinks);
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

            //System.out.println("New person added successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void editMember(String name, String firstName, String SSN, String dateOfBirth, String nationality) {
        try {
            // Lire le contenu du fichier JSON en tant que chaîne
            System.out.println(name);
            Path path = Paths.get(JSON_FILE_PATH2);
            String data = new String(Files.readAllBytes(path));
            //System.out.println(data);
            JSONArray jsonArray = new JSONArray(data);
            // Parcourir le tableau JSON pour trouver l'entrée avec le nom spécifié
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);
                if (object.getString("name").equals(name)) {
                    // Mettre à jour les champs spécifiés
                    object.put("firstName", firstName);
                    object.put("socialSecurityNumber", SSN);
                    object.put("dateOfBirth", dateOfBirth);
                    object.put("nationality", nationality);
                    break;
                }
            }

            // Écrire le tableau JSON mis à jour dans le fichier
            Files.write(path, jsonArray.toString(2).getBytes());

            System.out.println("Member updated successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void suppPerson(String name) {
        try {
            // Lire le contenu du fichier JSON en tant que chaîne
            Path path = Paths.get(JSON_FILE_PATH2);
            String data = new String(Files.readAllBytes(path));
            JSONArray jsonArray = new JSONArray(data);

            // Parcourir le tableau JSON pour trouver l'entrée avec le nom spécifié
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);
                if (object.getString("name").equals(name)) {
                    // Supprimer l'entrée trouvée
                    jsonArray.remove(i);
                    break;
                }
            }

            // Écrire le tableau JSON mis à jour dans le fichier
            Files.write(path, jsonArray.toString(2).getBytes());

            System.out.println("Person removed successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public static void main(String[] args) {
        launch(args);
    }
}
