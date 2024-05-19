package skx.coding;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) throws IOException {
        //read in string
        String data = new String(Files.readAllBytes(Paths.get("./demo.json")));

        //read as json array
        JSONArray jsonArray = new JSONArray(data);
        for(int i = 0; i < jsonArray.length(); i++){
            //parse in json object
            //JSONObject object = jsonArray.getJSONObject(i);
//           or
            String str = jsonArray.get(i).toString();
            JSONObject object1 = new JSONObject(str);

            String name = object1.getString("name");
            int age = object1.getInt("age");
            System.out.println("Name: " + name);
            System.out.println("Age: " + age);

        }

    }
}