package files;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import io.restassured.path.json.JsonPath;

public class ReusableMethods {

	public static JsonPath rawToJson(String response) {

		JsonPath js = new JsonPath(response);
		return js;
	}

	public static String GenerateStringFromResources(String path) throws IOException {

		return new String(Files.readAllBytes(Paths.get(path)));

	}

}
