package Tests.JSONContainer;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

public class TST_editJSONObject {

	public static void main(String[] args) {
		// The data type of the value of a key can always the overwritten
		JSONObject json = new JSONObject();
		Object test = JSONObject.stringToValue("26");
		json.put("age", test.toString()); // Always use put ==> append is for JSONArray
		System.out.println(json.toString(1));
		System.out.println();
		json.put("age", 26);
		System.out.println(json.toString(1));
		System.out.println();
		List<Integer> ages = new ArrayList<>();
		ages.add(26);
		ages.add(23);
		json.put("age", ages);
		System.out.println(json.toString(1));
	}
}
