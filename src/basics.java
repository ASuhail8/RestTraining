import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

import static org.hamcrest.Matchers.*;

import org.testng.Assert;

import files.ReusableMethods;
import files.payload;

import static io.restassured.RestAssured.*;

public class basics {

	public static void main(String[] args) {

		// Add Place
		RestAssured.baseURI = "https://rahulshettyacademy.com";
		String response = given().log().all().queryParam("key", "qaclick123").header("Content-Type", "application/json")
				.body(payload.AddPlace()).when().post("/maps/api/place/add/json").then().assertThat().statusCode(200)
				.body("scope", equalTo("APP")).header("Server", "Apache/2.4.18 (Ubuntu)").extract().response()
				.asString();

		System.out.println("Response :" + response);

		// To Parse Json

		JsonPath js = new JsonPath(response);
		String place_id = js.getString("place_id");
		System.out.println("placeID = :" + place_id);

		// update Place
		String newAddress = "Annasandra Palya, India";

		given().log().all().queryParam("key", "qaclick123").header("Content-Type", "application/json")
				.body(payload.updatePlace(place_id, newAddress)).when().put("/maps/api/place/update/json").then().log()
				.all().assertThat().statusCode(200).body("msg", equalTo("Address successfully updated"));

		// Get Place

		String getPlaceResponse = given().log().all().queryParam("key", "qaclick123").queryParam("place_id", place_id)
				.when().get("/maps/api/place/get/json").then().log().all().assertThat().statusCode(200).extract()
				.response().asString();

		JsonPath js1 = ReusableMethods.rawToJson(getPlaceResponse);
		String actualAddress = js1.getString("address");

		System.out.println(actualAddress);

		Assert.assertEquals(actualAddress, newAddress);

	}

}
