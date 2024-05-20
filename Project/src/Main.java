import org.json.JSONArray;
import org.json.JSONObject;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws Exception {
        String contentPerson = new String(Files.readAllBytes(Paths.get("arbregen_person.json")));
        JSONArray personsJson = new JSONArray(contentPerson);
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
                System.out.println("  Époux/Épouse: " + person.getSpouse());
            }
            if (!person.getChildren().isEmpty()) {
                System.out.println("  Enfants: " + person.getChildren());
            }
        }
    }
}
