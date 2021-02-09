import static io.restassured.RestAssured.given;

import java.util.ArrayList;
import java.util.List;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import pojo.GoogleAddAPI;
import pojo.Location;

public class SpecBuilderTest {

	public static void main(String[] args) {

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

		RequestSpecification reqSpec = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com")
				.setContentType(ContentType.JSON).addQueryParam("key", "qaclick123").build();

		RequestSpecification res = given().spec(reqSpec).body(p);

		ResponseSpecification resSpec = new ResponseSpecBuilder().expectStatusCode(200)
				.expectContentType(ContentType.JSON).build();

		String response = res.when().post("/maps/api/place/add/json").then().spec(resSpec).extract().response()
				.asString();

		System.out.println(response);

	}

}
