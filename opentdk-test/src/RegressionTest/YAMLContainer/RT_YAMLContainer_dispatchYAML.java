package RegressionTest.YAMLContainer;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.json.JSONException;
import org.opentdk.api.dispatcher.BaseDispatcher;

import RegressionTest.BaseRegression;

public class RT_YAMLContainer_dispatchYAML extends BaseRegression {
	
	public static void main(String[] args) {
		new RT_YAMLContainer_dispatchYAML();
	}

	@Override
	protected void runTest() {
		BaseDispatcher.setDataContainer(EYamlValues.class, "testdata/RegressionTestData/YamlExample.yaml");

		// GET
		BaseRegression.testResult(EYamlValues.SIR.getValue(), "SIR", "true");
		BaseRegression.testResult(EYamlValues.SIR.getValues()[0], "SIR", "true");
		BaseRegression.testResult(EYamlValues.NAME.getValue(), "NAME", "LK");
		BaseRegression.testResult(EYamlValues.PHONE_NUMBERS.getValue(), "PHONE_NUMBER FIRST VALUE", "123456");
		BaseRegression.testResult(EYamlValues.PHONE_NUMBERS.getValues()[1], "PHONE_NUMBER SECOND VALUE", "987654");
		BaseRegression.testResult(EYamlValues.CITIES.getValue(), "CITIES FIRST VALUE", "Munich");
		BaseRegression.testResult(EYamlValues.CITIES.getValues()[1], "CITIES SECOND VALUE", "86637");
		BaseRegression.testResult(EYamlValues.EMPLOYEE_AGE.getValue(), "EMPLOYEE AGE", "28");
		BaseRegression.testResult(EYamlValues.BOSS_SALARY.getValue(), "BOSS SALARY", "25000");
		BaseRegression.testResult(EYamlValues.BOSS_NAME.getValue(), "BOSS_NAME", "Boss");
		
		String[] peoples = EYamlValues.PEOPLE.getValues();
		for(String people : peoples) {
			BaseDispatcher.setDataContainer(EYamlPeopleValues.class, new ByteArrayInputStream(people.getBytes()));
			System.out.println(EYamlPeopleValues.PEOPLE_AGE.getValue());
		}
		
		try {
			EYamlValues.INVALID.getValue();
		} catch (JSONException e) {
			System.out.println("Exception correctly catched ==> Field 'invalid' does not exist");
		}

		// SET
		EYamlValues.ID.setValue("2");
		EYamlValues.SIR.setValue("false");
		EYamlValues.PHONE_NUMBERS.setValue("[123456,654321]");
		EYamlValues.CITIES.setValue("[\"Munich\",86637,\"Augsburg\"]");
		EYamlValues.BOSS_SALARY.setValue("30000");
		EYamlValues.NEW.setValue("{\"person\": 1}");

		BaseRegression.testResult(EYamlValues.ID.getValue(), "ID after set", "2");
		BaseRegression.testResult(EYamlValues.SIR.getValue(), "SIR after set", "false");
		BaseRegression.testResult(EYamlValues.PHONE_NUMBERS.getValues()[1], "PHONE_NUMBER after set", "654321");
		BaseRegression.testResult(EYamlValues.CITIES.getValues()[2], "CITIES after set", "Augsburg");
		BaseRegression.testResult(EYamlValues.BOSS_SALARY.getValue(), "BOSS SALARY after set", "30000");

		// ADD
		try {
			EYamlValues.ROLE.addValue("Employee");
		} catch (JSONException e) {
			System.out.println("Exception correctly catched ==> Field 'role' is no array to add values to");
		}
		try {
			EYamlValues.SIR.addValue("true");
		} catch (JSONException e) {
			System.out.println("Exception correctly catched ==> Field 'sir' is no array to add values to");
		}
		EYamlValues.PHONE_NUMBERS.addValue("485625");
		EYamlValues.PEOPLE.addValue("{\"name\": \"Volunteer\",\"age\": 16}");

		BaseRegression.testResult(EYamlValues.ROLE.getValue(), "ROLE after add", "Manager");
		BaseRegression.testResult(EYamlValues.PHONE_NUMBERS.getValues()[2], "PHONE_NUMBERS after add", "485625");
		BaseRegression.testResult(EYamlValues.PEOPLE_AGE.getValue(), "PEOPLE_AGE after add", "16");


		// DELETE
		EYamlValues.ADDRESS.delete();
		EYamlValues.BOSS_SALARY.delete();
		try {
			EYamlValues.ADDRESS.getValue();
			System.err.println("Exception not correctly catched ==> Field 'address' should be deleted");
		} catch (JSONException e) {
			System.out.println("Exception correctly catched ==> Field 'address' was deleted");
		}
		try {
			EYamlValues.BOSS_SALARY.getValue();
			System.err.println("Exception not correctly catched ==> Field 'salary' should be deleted");
		} catch (JSONException e) {
			System.out.println("Exception correctly catched ==> Field 'salary' was deleted");
		}
		
		try {
			BaseDispatcher.getDataContainer(EYamlValues.class).writeData("output/YamlOutput.yaml");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
