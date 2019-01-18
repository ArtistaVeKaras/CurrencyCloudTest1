package classes;
import files.resources;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import static org.hamcrest.Matchers.equalTo;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import static io.restassured.RestAssured.given;

public class getAuthenticationAPIKey {

     // creating an object of properties so their properties can be accessed from another class
     Properties prop = new Properties();

     /*
     This method obtains input and reads bytes from the resource file
      */
     @BeforeTest
    public void getData() throws IOException {

        FileInputStream fis = new FileInputStream("C:\\Users\\claud\\eclipse-workspace\\CurrencyCloudTest\\src\\test\\java\\files\\enviro.properties");
         prop.load(fis);
         prop.get("Host");
    }

    /*
    this method the user authenticates against the REst APi and
    retrieves the auth_token by sending the right credentials
    response of the body is also retrieved and assertion of the
    status code is also performed
     */
    //@Test
    public void getAPIKey(){
        RestAssured.baseURI = prop.getProperty("HOST");
        Response res = given().
        param("login_id","claudiooartista@hotmail.co.uk").
        param("api_key","557b23ec359284c7a05a603f7354fa0207624ba69c4ec0029796ffa73f92d473").
        when().
        post(resources.getResource()).
        then().statusCode(200).and().extract().response();
        String responseBody = res.body().prettyPrint();
        JsonPath js = new JsonPath(responseBody);
        System.out.println(js);
      }

   /*
     This method the user requests a quote for selling GBP
     and buying USD, by sending the query params the response
      of the body is retrieved in Json format
     and assertion of the status code is performed
    */
//   @Test
   public void createQuoteForSellingGBP(){

       RestAssured.baseURI = prop.getProperty("HOST");
       Response bodyResponse =  given().
       queryParam("buy_currency","USD").
       queryParam("sell_currency","GBP").
       queryParam("fixed_side","sell").
       queryParam("amount","1500").
       and().headers("Accept","application/json",
              "Content-Type","multipart/form-data","X-Auth-Token","9c7f069274c13ed22e4945de8d27044e").
       when().
       get(resources.getQuote()).
       then().statusCode(200).and().body("client_buy_amount",equalTo("2114.85")).extract().response();
       String res = bodyResponse.getBody().prettyPrint();
       JsonPath js = new JsonPath(res);
       System.out.println(js);
     }

     /*
     this method the user makes another request but this
     time a negative assertion is performed
     */
    @Test
    public void crateQuoteButPerformNegativeAssertion(){

        RestAssured.baseURI = prop.getProperty("HOST");
        Response responseBody = given().
        queryParam("buy_currency","EUR").
        queryParam("sell_currency","USD").
        queryParam("fixed_side","sell").
        queryParam("amount","1500").
        and().headers("Accept","application/json","Content-Type","multipart/form-data","X-Auth-Token","9c7f069274c13ed22e4945de8d27044e").
        when().
        post(resources.getQuote()).
        then().assertThat().statusCode(200).and().body("fixed_side",equalTo("sell")).extract().response();
        String respo = responseBody.toString();
        JsonPath js = new JsonPath(respo);
        System.out.println(js);

    }
    /*
    this is the end of Session
     */
    @AfterTest
    public void endSession(){
        System.out.println("End of Testing");
    }
}
