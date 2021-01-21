package files;

import static io.restassured.RestAssured.given;

import io.restassured.RestAssured;
import io.restassured.filter.session.SessionFilter;

public class JiraTest {

	public static void main(String[] args) {

		// Create a Session

		RestAssured.baseURI = "http://localhost:8080";
		SessionFilter session = new SessionFilter();
		given().log().all().header("Content-Type", "application/json")
				.body("{\r\n" + "    \"username\": \"ASuhail8\",\r\n" + "    \"password\": \"@5uh@il@1\"\r\n" + "}")
				.filter(session).when().post("/rest/auth/1/session").then().log().all().statusCode(200).extract()
				.response().asString();

		// Create a issue
		/*
		 * given().log().all().header("Content-Type", "application/json").body("{\r\n" +
		 * "  \"update\": {},\r\n" + "  \"fields\": {\r\n" +
		 * "    \"summary\": \"Creating first ticket from API  \",\r\n" +
		 * "    \"project\": {\r\n" + "      \"key\": \"RAS\"\r\n" + "    },\r\n" +
		 * "    \"issuetype\": {\r\n" + "      \"name\": \"Bug\"\r\n" + "    },\r\n" +
		 * "    \"description\": \"Decriptions\"\r\n" + "  }\r\n" + "}")
		 * .filter(session).when().post("/rest/api/2/issue").then().log().all().
		 * assertThat().statusCode(201);
		 * 
		 * 
		 */

		// Add a comment

		given().log().all().header("Content-Type", "application/json").pathParam("id", "10102")
				.body("{\r\n" + "    \"body\": \"Fourth comment on 21st jan\",\r\n" + "    \"visibility\": {\r\n"
						+ "        \"type\": \"role\",\r\n" + "        \"value\": \"Administrators\"\r\n" + "    }\r\n"
						+ "}")
				.filter(session).when().post("/rest/api/2/issue/{id}/comment").then().log().all().assertThat()
				.statusCode(201);

	}

}
