package RegressionTest.JSONContainer;

import org.opentdk.api.dispatcher.BaseDispatcher;

import RegressionTest.BaseRegression;

public class RT_Container_dispatchJson extends BaseRegression {

	public static void main(String[] args) {
		new RT_Container_dispatchJson();
	}

	@Override
	protected void runTest() {
		BaseDispatcher.setDataContainer(EJsonValues.class, "testdata/RegressionTestData/JsonExample.json");		
		
		// GET
		BaseRegression.testResult(EJsonValues.SIR.getValue(), "SIR", "true");
		BaseRegression.testResult(EJsonValues.SIR.getValues()[0], "SIR", "true");
		BaseRegression.testResult(EJsonValues.PHONE_NUMBERS.getValue(), "PHONE_NUMBER FIRST VALUE", "123456");
		BaseRegression.testResult(EJsonValues.PHONE_NUMBERS.getValues()[1], "PHONE_NUMBER SECOND VALUE", "987654");
		BaseRegression.testResult(EJsonValues.CITIES.getValue(), "CITIES FIRST VALUE", "\"Munich\"");
		BaseRegression.testResult(EJsonValues.CITIES.getValues()[1], "CITIES SECOND VALUE", "86637");
		BaseRegression.testResult(EJsonValues.EMPLOYEE_AGE.getValue(), "EMPLOYEE AGE", "28");
		BaseRegression.testResult(EJsonValues.BOSS_SALARY.getValue(), "BOSS SALARY", "25000");
 
		try {
			EJsonValues.INVALID.getValue();
		} catch(Exception e) {
			// Correct behavior
		}
		
		// SET
		EJsonValues.ID.setValue("2");
		EJsonValues.SIR.setValue("false");
		EJsonValues.PHONE_NUMBERS.setValue("[123456,654321]");
		EJsonValues.CITIES.setValue("[\"Munich\",86637,\"Augsburg\"]");
		EJsonValues.BOSS_SALARY.setValue("30000");
		
		BaseRegression.testResult(EJsonValues.ID.getValue(), "ID after set", "2");
		BaseRegression.testResult(EJsonValues.SIR.getValue(), "SIR after set", "false");
		BaseRegression.testResult(EJsonValues.PHONE_NUMBERS.getValues()[1], "PHONE_NUMBER after set", "654321");
		BaseRegression.testResult(EJsonValues.CITIES.getValues()[2], "CITIES after set", "\"Augsburg\"");
		BaseRegression.testResult(EJsonValues.BOSS_SALARY.getValue(), "BOSS SALARY after set", "30000");

		// ADD
		EJsonValues.PHONE_NUMBERS.addValue("485625");
		
		// DELETE
		
		BaseDispatcher.getDataContainer(EJsonValues.class).writeData("output/JsonOutput.json");
	}

}
