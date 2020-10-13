import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.net.http.HttpRequest.BodyPublishers;
import org.json.JSONArray;
import org.json.JSONObject;

public class MainApp {


public static void post(String uri, String data, String token) throws Exception {
   HttpClient client = HttpClient.newBuilder().build();
   HttpRequest request = HttpRequest.newBuilder()
    .uri(URI.create(uri))
           .POST(BodyPublishers.ofString(data))
           //CHANGE PER API 
           .header("Authorization", "Bearer "+ token)
           //CHANGE
           .header("Content-Type", "application/json")
           .build();

   HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
   
   System.out.println("Status: "+ response.statusCode());
   System.out.println("Body: " + response.body());
   
   JSONObject jo = new JSONObject(response.body());
   JSONObject foods = jo.getJSONObject("foods");
   JSONArray food = foods.getJSONArray("food");
   JSONObject item0 = food.getJSONObject(0);
   String name0 = item0.getString("food_name");
   String description0 = item0.getString("food_description");
   String id0 = item0.getString("food_id");
   String type0 = item0.getString("food_type");
   
   System.out.println("Food name: " + name0);
   System.out.println("Nutrition facts: " + description0);
   System.out.println("ID: " + id0);
   System.out.println("Type: " + type0);
}
}