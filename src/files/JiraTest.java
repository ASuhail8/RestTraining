package files;

import static io.restassured.RestAssured.given;

import java.io.File;

import org.testng.Assert;

import io.restassured.RestAssured;
import io.restassured.filter.session.SessionFilter;
import io.restassured.path.json.JsonPath;

public class JiraTest {

	public static void main(String[] args) {

		// Create a Session

		RestAssured.baseURI = "http://localhost:8080";
		SessionFilter session = new SessionFilter();
		given().relaxedHTTPSValidation().log().all().header("Content-Type", "application/json")
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
		String expectedMessage = "Hello, how you doin";
		String addCommentResponse = given().log().all().header("Content-Type", "application/json")
				.pathParam("id", "10102")
				.body("{\r\n" + "    \"body\": \"" + expectedMessage + "\",\r\n" + "    \"visibility\": {\r\n"
						+ "        \"type\": \"role\",\r\n" + "        \"value\": \"Administrators\"\r\n" + "    }\r\n"
						+ "}")
				.filter(session).when().post("/rest/api/2/issue/{id}/comment").then().log().all().assertThat()
				.statusCode(201).extract().response().asString();

		JsonPath js = new JsonPath(addCommentResponse);
		String commentId = js.getString("id");

		// Add a attachemnt using multipart

		given().log().all().header("X-Atlassian-Token", "no-check").header("Content-Type", "multipart/form-data")
				.multiPart("file", new File("jira.txt")).filter(session).pathParam("id", "10102").when()
				.post("/rest/api/2/issue/{id}/attachments").then().log().all().assertThat().statusCode(200);

		// get issue

		String issueDetails = given().filter(session).pathParam("id", "10102").queryParam("fields", "comment").log()
				.all().when().get("/rest/api/2/issue/{id}").then().log().all().assertThat().statusCode(200).extract()
				.response().asString();
		System.out.println(issueDetails);

		JsonPath js1 = new JsonPath(issueDetails);
		int commentCount = js1.getInt("fields.comment.comments.size()");

		for (int i = 0; i < commentCount; i++) {
			String issueCommentId = js1.get("fields.comment.comments[" + i + "].id").toString();
			if (issueCommentId.equalsIgnoreCase(commentId)) {
				String actualMsg = js1.get("fields.comment.comments[" + i + "].body").toString();
				System.out.println(actualMsg);
				Assert.assertEquals(expectedMessage, actualMsg);
			}
		}

	}

}
