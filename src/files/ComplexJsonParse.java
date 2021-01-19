package files;

import io.restassured.path.json.JsonPath;

public class ComplexJsonParse {

	public static void main(String args[]) {

		JsonPath js = new JsonPath(payload.coursePrice());

		// Print the No of courses returned by API
		int count = js.getInt("courses.size()");
		System.out.println("No of courses :" + count);

		// Print purchase Amount

		int purchaseAmount = js.getInt("dashboard.purchaseAmount");
		System.out.println("Purchase Amount :" + purchaseAmount);

		// Print title of first course

		String firstTitle = js.getString("courses[0].title");
		System.out.println("Title of first course :" + firstTitle);

		// print all course title and respective prices

		for (int i = 0; i < count; i++) {
			String courseTitle = js.get("courses[" + i + "].title");
			System.out.println(courseTitle);
			System.out.println(js.get("courses[" + i + "].price").toString());
		}

		// print no of copies sold by RPA course

		for (int i = 0; i < count; i++) {
			String title = js.get("courses[" + i + "].title");
			if (title.equals("RPA")) {
				System.out.println("no of copies sold by RPA :" + js.get("courses[" + i + "].copies").toString());
				break;
			}
		}

		// verify if sum of all courses prices matches with purchase amount

		for (int i = 0; i < count; i++) {
			System.out.println("price of course : " + js.get("courses[" + i + "].price").toString());
			System.out.println("copies sold :" + js.get("courses[" + i + "].copies").toString());

		}
	}
}
