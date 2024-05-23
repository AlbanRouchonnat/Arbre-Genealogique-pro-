package skx;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
// programme qui verifie si le fichier .json existe si c'est le cas il ecrit a la suite
public class WriteJSONExample2 {
    public static void main(String[] args) {
        // Create new employee JSON objects
        JSONObject emps1 = new JSONObject();
        emps1.put("firstname", "Blabla");
        emps1.put("lastname", "S");
        emps1.put("website", "www.blablas.com");

        JSONObject empObj1 = new JSONObject();
        empObj1.put("employee", emps1);

        JSONObject emps2 = new JSONObject();
        emps2.put("firstname", "Loza");
        emps2.put("lastname", "Rossa");
        emps2.put("website", "www.lozr.com");

        JSONObject empObj2 = new JSONObject();
        empObj2.put("employee", emps2);

        JSONArray empList = new JSONArray();
        empList.put(empObj1);
        empList.put(empObj2);

        // File name
        String fileName = "hello.json";
        File file = new File(fileName);

        if (file.exists()) {
            try (FileReader reader = new FileReader(file)) {
                // Read existing JSON data
                char[] chars = new char[(int) file.length()];
                reader.read(chars);
                String existingContent = new String(chars);

                // Convert existing content to JSONArray
                JSONArray existingArray = new JSONArray(existingContent);

                // Append new JSON objects to existing array
                for (int i = 0; i < empList.length(); i++) {
                    existingArray.put(empList.get(i));
                }

                // Write the updated array back to the file
                try (FileWriter writer = new FileWriter(fileName)) {
                    writer.write(existingArray.toString());
                    writer.flush();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            // Write new JSON data to a new file
            try (FileWriter writer = new FileWriter(fileName)) {
                writer.write(empList.toString());
                writer.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
