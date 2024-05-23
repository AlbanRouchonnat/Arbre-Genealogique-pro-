package skx;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
// fonction permettant d'ecrire dans un fichier existant en modifiant certaines valeurs de certaines cles
// fonction qui crée un fichier pour ecrire dedans si celui ci n'est pas créee
public class WriteJSONExample {
    public static void main(String[] args) {
        JSONObject emps1 = new JSONObject();
        emps1.put("firstname","Aude");
        emps1.put("lastname","S");
        emps1.put("website","www.audes.com");

        JSONObject empObj1 = new JSONObject();
        empObj1.put("employee", emps1); //ici employee regroupe tout les infos du dessus
        // (firstname, lastname, website)

        JSONObject emps2 = new JSONObject();
        emps2.put("firstname","Amy");
        emps2.put("lastname","Bing");
        emps2.put("website","www.amb.com");

        JSONObject empObj2 = new JSONObject();
        empObj2.put("employee", emps2); // ici Obj1 et Obj2 iront dans le meme block en JSON
        // car le clé est la meme pour les deux a savoir "employee"

        JSONArray empList = new JSONArray(); // but ici ajouter les objets a la liste
        empList.put(empObj1);
        empList.put(empObj2);

        try(FileWriter file = new FileWriter("hello.json")){
            file.write(empList.toString());
            file.flush();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
