import org.json.JSONArray;
import org.json.JSONObject;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws Exception {
        String filePath = "arbregen_person.json";
        String contentPersons = new String(Files.readAllBytes(Paths.get(filePath)), StandardCharsets.UTF_8);
        JSONArray personsJson = new JSONArray(contentPersons);
        Map<String, Person> personMap = new HashMap<>();

        for (int i = 0; i < personsJson.length(); i++) {
            JSONObject personJson = personsJson.getJSONObject(i);
            Person person = new Person();
            person.setName(personJson.getString("name"));
            person.setAge(personJson.getInt("age"));
            personMap.put(person.getName(), person);
        }

        String contentRelation = new String(Files.readAllBytes(Paths.get("arbregen_lien.json")));
        JSONArray relationsJson = new JSONArray(contentRelation);

        for (int i = 0; i < relationsJson.length(); i++) {
            JSONObject relationJson = relationsJson.getJSONObject(i);
            String relationType = relationJson.getString("relation");
            String from = relationJson.optString("from", null);
            String to = relationJson.optString("to", null);

            if (from == null || to == null) {
                System.out.println("Erreur: Relation sans 'from' ou 'to'");
                continue;
            }

            Person fromPerson = personMap.get(from);
            Person toPerson = personMap.get(to);

            if (fromPerson == null || toPerson == null) {
                System.out.println("Erreur: Personne non trouvée pour le lien spécifié");
                continue;
            }

            if ("parent".equals(relationType)) {
                fromPerson.addChild(toPerson);
            } else if ("married".equals(relationType)) {
                fromPerson.setSpouse(toPerson);
                toPerson.setSpouse(fromPerson); // Assuming bidirectional
            }
        }

        for (Person person : personMap.values()) {
            System.out.println(person);
            if (person.getSpouse() != null) {
                System.out.println("  Époux/Épouse: " + person.getSpouse().getName());
            }
            if (!person.getChildren().isEmpty()) {
                System.out.println("  Enfants: " + person.getChildren());
            }
        }


        System.out.println("Voulez vous supprimer un utilisateur ? ");

        System.out.println("Original list:");
        System.out.println(personsJson.toString(2));


        removePersonByName(personsJson, "Grace", filePath);


        System.out.println("\nList after removing 'Charlie':");
        System.out.println(personsJson.toString(2));
    }

    public static void removePersonByName(JSONArray personsJson, String name, String filePath) throws Exception {
        for (int i = 0; i < personsJson.length(); i++) {
            if (personsJson.getJSONObject(i).getString("name").equals(name)) {
                personsJson.remove(i);
                break; // Supposer que le nom est unique
            }
        }
        Files.write(Paths.get(filePath), personsJson.toString(2).getBytes(StandardCharsets.UTF_8));
    }
}
