import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.net.http.HttpRequest.BodyPublishers;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

  public class APICaller {
    
    private String searchExp;
    public String body="OWU3OGEwOTAzMTY5NDcxNGFlMjMzY2M0MTVhNDMxOGE6ODYwYzQ4OTBlNjM2NDYxYWFiNjY1MzBjZmJkNWFmNzM=";
    private String access_token;
    
    public APICaller() throws IOException, InterruptedException {
      this.searchExp = "Chicken"; //test
      this.access_token = getToken();
    }
        
    //constructor
    APICaller(String searchExp) throws IOException, InterruptedException {
      this.searchExp = searchExp;
      this.access_token = getToken();
    }
    
    //creates uri// static b/c it only depends on input
    public String getURI(String searchExpr) {
      String uri = "https://platform.fatsecret.com/rest/"
          + "server.api?method=foods.search&search_expression=" + 
          searchExpr + "&format=json";
      
      return uri;
    }
    
    public String getToken() throws IOException, InterruptedException {
      
      HttpClient client = HttpClient.newBuilder().build();
      HttpRequest request = HttpRequest.newBuilder()
       .uri(URI.create("https://oauth.fatsecret.com/connect/token"))
              .POST(BodyPublishers.ofString("grant_type=client_credentials"))
              //CHANGE PER API (sometimes)
              .header("Authorization", "Basic " + this.body)
              .header("Content-Type", "application/x-www-form-urlencoded")
              .header("Cache-Control", "no-cache")
              //.header("Host", "oauth.fatsecret.com")
              .header("Content-type", "application/x-www-form-urlencoded")
              .build();

      HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
     
      JSONObject j = new JSONObject(response.body());
      String token = j.getString("access_token");
      this.access_token = token;
     // System.out.println(response.body());
      
      return token;
    }
    
    
  //calls API, returns response w/o using token
  public String post(String token, String searchExpr) throws Exception {
     
    HttpClient client = HttpClient.newBuilder().build();
    HttpRequest request = HttpRequest.newBuilder()
     .uri(URI.create(this.getURI(searchExpr)))
            .POST(BodyPublishers.ofString("{}"))
            //CHANGE PER API (sometimes)
            .header("Authorization", "Bearer "+ this.access_token)
            .header("Content-Type", "application/json")
            .build();

    HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
    
    
    return response.body();
     
  }
  

  //gets original JSONObject for the search expression
  public JSONObject getJSONObj(String searchExp) throws JSONException, Exception {
    JSONObject jo = new JSONObject(post(this.access_token, searchExp));
    return jo;
  }
  
  //gets JSONObject "foods"
  public JSONObject getFoodsObj() throws JSONException, Exception {
    JSONObject foods = this.getJSONObj(searchExp).getJSONObject("foods");
    return foods;
  }
  
  //gets JSONArray "food"
  public JSONArray getFoodArray() throws JSONException, Exception {
    JSONArray food = this.getFoodsObj().getJSONArray("food");
    return food;
  }
  
  
  }
  

