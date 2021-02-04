
import static io.restassured.RestAssured.given;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.Assert;

import io.restassured.parsing.Parser;
import io.restassured.path.json.JsonPath;
import pojo.API;
import pojo.WebAutomation;
import pojo.getCourse;

public class oAuthTest {

	public static void main(String[] args) throws InterruptedException {

		// Build the code and hit the url using selenium
		String[] WebAutomationCourseTitles = { "Selenium Webdriver Java", "Cypress", "Protractor" };
		System.setProperty("webdriver.gecko.driver", "./drivers/geckodriver.exe");
		WebDriver driver = new FirefoxDriver();
		driver.get(
				"https://accounts.google.com/o/oauth2/v2/auth?scope=https://www.googleapis.com/auth/userinfo.email&Auth_url=https://accounts.google.com/o/oauth2/v2/auth&client_id=692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com&response_type=code&redirect_uri=https://rahulshettyacademy.com/getCourse.php&state=verifyfjdss");
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(1000, TimeUnit.SECONDS);
		driver.findElement(By.xpath("//input[@type='email']")).sendKeys("flo4abs");
		driver.findElement(By.xpath("//input[@type='email']")).sendKeys(Keys.ENTER);
		Thread.sleep(3000);
		driver.findElement(By.xpath("//input[@type='password']")).sendKeys("floweroflover");
		driver.findElement(By.xpath("//input[@type='password']")).sendKeys(Keys.ENTER);
		Thread.sleep(5000);

		String url = driver.getCurrentUrl();
		driver.quit();

		String partialCode = url.split("code=")[1];
		String code = partialCode.split("&scope")[0];

		// get Access Token request

		String accessTokenResponse = given().urlEncodingEnabled(false).log().all().queryParam("code", code)
				.queryParam("client_id", "692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com")
				.queryParam("client_secret", "erZOWM9g3UtwNRj340YYaK_W")
				.queryParam("redirect_uri", "https://rahulshettyacademy.com/getCourse.php")
				.queryParam("grant_type", "authorization_code").when().log().all()
				.post("https://www.googleapis.com/oauth2/v4/token").asString();

		JsonPath js = new JsonPath(accessTokenResponse);
		String accessToken = js.getString("access_token");

		// Actual Request to hit rahul shetty academy

		getCourse gc = given().queryParam("access_token", accessToken).expect().defaultParser(Parser.JSON).when()
				.get("https://rahulshettyacademy.com/getCourse.php").as(getCourse.class);

		List<API> apiCourses = gc.getCourses().getApi();
		for (int i = 0; i < apiCourses.size(); i++) {
			if (apiCourses.get(i).getCourseTitle().equalsIgnoreCase("Rest Assured Automation using Java")) {
				System.out.println(apiCourses.get(i).getPrice());
			}
		}

		// get the course name of web automation
		List<WebAutomation> webAutomationCourses = gc.getCourses().getWebAutomation();
		// To store dynamic value use ArrayList
		ArrayList<String> al = new ArrayList<String>();
		for (int i = 0; i < webAutomationCourses.size(); i++) {
			al.add(webAutomationCourses.get(i).getCourseTitle());
		}
		System.out.println(al);

		// convert arrays to arrayList
		List<String> expectedList = Arrays.asList(WebAutomationCourseTitles);
		System.out.println(expectedList);

		Assert.assertTrue(al.equals(expectedList));
	}

}
