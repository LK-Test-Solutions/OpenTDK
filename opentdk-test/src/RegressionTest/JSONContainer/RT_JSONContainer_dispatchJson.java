package RegressionTest.JSONContainer;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.json.JSONException;
import org.opentdk.api.dispatcher.BaseDispatcher;

import RegressionTest.BaseRegression;

public class RT_JSONContainer_dispatchJson extends BaseRegression {

	public static void main(String[] args) {
		new RT_JSONContainer_dispatchJson();
	}

	@Override
	protected void runTest() {
		BaseDispatcher.setDataContainer(EJsonValues.class, "testdata/RegressionTestData/JsonExample.json");

		// GET
		BaseRegression.testResult(EJsonValues.SIR.getValue(), "SIR", "true");
		BaseRegression.testResult(EJsonValues.SIR.getValues()[0], "SIR", "true");
		BaseRegression.testResult(EJsonValues.NAME.getValue(), "NAME", "LK");
		BaseRegression.testResult(EJsonValues.PHONE_NUMBERS.getValue(), "PHONE_NUMBER FIRST VALUE", "123456");
		BaseRegression.testResult(EJsonValues.PHONE_NUMBERS.getValues()[1], "PHONE_NUMBER SECOND VALUE", "987654");
		BaseRegression.testResult(EJsonValues.CITIES.getValue(), "CITIES FIRST VALUE", "Munich");
		BaseRegression.testResult(EJsonValues.CITIES.getValues()[1], "CITIES SECOND VALUE", "86637");
		BaseRegression.testResult(EJsonValues.EMPLOYEE_AGE.getValue(), "EMPLOYEE AGE", "28");
		BaseRegression.testResult(EJsonValues.BOSS_SALARY.getValue(), "BOSS SALARY", "25000");
		BaseRegression.testResult(EJsonValues.BOSS_NAME.getValue(), "BOSS_NAME", "Boss");
		
		String[] peoples = EJsonValues.PEOPLE.getValues();
		for(String people : peoples) {
			BaseDispatcher.setDataContainer(EPeopleValues.class, new ByteArrayInputStream(people.getBytes()));
			System.out.println(EPeopleValues.PEOPLE_AGE.getValue());
		}
		
		try {
			EJsonValues.INVALID.getValue();
		} catch (JSONException e) {
			System.out.println("Exception correctly catched ==> Field 'invalid' does not exist");
		}

		// SET
		EJsonValues.ID.setValue("2");
		EJsonValues.SIR.setValue("false");
		EJsonValues.PHONE_NUMBERS.setValue("[123456,654321]");
		EJsonValues.CITIES.setValue("[\"Munich\",86637,\"Augsburg\"]");
		EJsonValues.BOSS_SALARY.setValue("30000");
		EJsonValues.NEW.setValue("{\"person\": 1}");

		BaseRegression.testResult(EJsonValues.ID.getValue(), "ID after set", "2");
		BaseRegression.testResult(EJsonValues.SIR.getValue(), "SIR after set", "false");
		BaseRegression.testResult(EJsonValues.PHONE_NUMBERS.getValues()[1], "PHONE_NUMBER after set", "654321");
		BaseRegression.testResult(EJsonValues.CITIES.getValues()[2], "CITIES after set", "Augsburg");
		BaseRegression.testResult(EJsonValues.BOSS_SALARY.getValue(), "BOSS SALARY after set", "30000");

		// ADD
		try {
			EJsonValues.ROLE.addValue("Employee");
		} catch (JSONException e) {
			System.out.println("Exception correctly catched ==> Field 'role' is no array to add values to");
		}
		try {
			EJsonValues.SIR.addValue("true");
		} catch (JSONException e) {
			System.out.println("Exception correctly catched ==> Field 'sir' is no array to add values to");
		}
		EJsonValues.PHONE_NUMBERS.addValue("485625");
		EJsonValues.PEOPLE.addValue("{\"name\": \"Volunteer\",\"age\": 16}");

		BaseRegression.testResult(EJsonValues.ROLE.getValue(), "ROLE after add", "Manager");
		BaseRegression.testResult(EJsonValues.PHONE_NUMBERS.getValues()[2], "PHONE_NUMBERS after add", "485625");
		BaseRegression.testResult(EJsonValues.PEOPLE_AGE.getValue(), "PEOPLE_AGE after add", "16");


		// DELETE
		EJsonValues.ADDRESS.delete();
		EJsonValues.BOSS_SALARY.delete();
		try {
			EJsonValues.ADDRESS.getValue();
			System.err.println("Exception not correctly catched ==> Field 'address' should be deleted");
		} catch (JSONException e) {
			System.out.println("Exception correctly catched ==> Field 'address' was deleted");
		}
		try {
			EJsonValues.BOSS_SALARY.getValue();
			System.err.println("Exception not correctly catched ==> Field 'salary' should be deleted");
		} catch (JSONException e) {
			System.out.println("Exception correctly catched ==> Field 'salary' was deleted");
		}
		
		try {
			BaseDispatcher.getDataContainer(EJsonValues.class).writeData("output/JsonOutput.json");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		BaseDispatcher.clearDataContainer();
	}

}
