package skx;
import org.json.JSONArray;
import org.json.JSONObject;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;

public class DatabseConnection {
    public static void main(String[] args) {
        try {
            // Lecture du fichier JSON
            String jsonText = new String(Files.readAllBytes(Paths.get("src/main/arbregen_person.json")));
            JSONArray users = new JSONArray(jsonText);

            // Ici, tu peux ajouter une méthode pour saisir les informations de l'utilisateur
            String username = "user1";  // Simule la saisie de l'utilisateur
            String password = "pass1";  // Simule la saisie de l'utilisateur

            // Authentification
            JSONObject user = authenticate(users, username, password);
            if (user != null) {
                System.out.println("Connecté !");
                System.out.println(user.getJSONObject("details").toString());
            } else {
                System.out.println("Échec de la connexion.");
            }
        } catch (IOException e) {
            System.out.println("Erreur lors de la lecture du fichier : " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Erreur : " + e.getMessage());
        }
    }

    private static JSONObject authenticate(@org.jetbrains.annotations.NotNull JSONArray users, String username, String password) {
        for (int i = 0; i < users.length(); i++) {
            JSONObject user = users.getJSONObject(i);
            if (user.getString("username").equals(username) && user.getString("password").equals(password)) {
                return user;
            }
        }
        return null;
    }
}