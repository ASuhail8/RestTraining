package files;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

import static io.restassured.RestAssured.*;

public class DynamicJson {

	@Test(dataProvider = "BooksData")
	public void dynamicJson(String isbn, String aisle) {

		// Add a Book
		RestAssured.baseURI = "http://216.10.245.166";
		String response = given().log().all().header("Content-Type", "application/json")
				.body(payload.addBook(isbn, aisle)).when().post("Library/Addbook.php").then().log().all().assertThat()
				.statusCode(200).extract().response().asString();

		JsonPath js = ReusableMethods.rawToJson(response);
		String ID = js.get("ID");
		System.out.println(ID);

		// Delete a book
		given().log().all().header("Content-Type", "application/json")
				.body("{ \r\n" + "\"ID\" : \"" + ID + "\" \r\n" + "}").when().post("/Library/DeleteBook.php").then()
				.log().all().assertThat().statusCode(200);

	}

	@DataProvider(name = "BooksData")
	public Object[][] getData() {
		return new Object[][] { { "suhail", "81978" }, { "Sanae", "546" }, { "tazeens", "9856" },
				{ "Minige", "4568" } };
	}

}