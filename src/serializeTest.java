import static io.restassured.RestAssured.given;

import java.util.ArrayList;
import java.util.List;

import io.restassured.RestAssured;
import io.restassured.parsing.Parser;
import pojo.AddApiResponse;
import pojo.GoogleAddAPI;
import pojo.Location;

public class serializeTest {

	public static void main(String[] args) {

		RestAssured.baseURI = "https://rahulshettyacademy.com";
		GoogleAddAPI p = new GoogleAddAPI();
		p.setAccuracy(50);
		p.setName("Frontline house");
		p.setPhone_number("(+91) 983 893 3937");
		p.setAddress("29, side layout, cohen 09");
		p.setWebsite("http://google.com");
		p.setLanguage("French-IN");

		List<String> list = new ArrayList<String>();
		list.add("shoe park");
		list.add("shop");
		p.setTypes(list);

		Location l = new Location();
		l.setLat(-38.383494);
		l.setLng(33.427362);
		p.setLocation(l);

		AddApiResponse response = given().log().all().queryParam("key", "qaclick123").body(p).when()
				.post("/maps/api/place/add/json").as(AddApiResponse.class);

		System.out.println("PlaceID :" + response.getPlace_id());

	}

}
