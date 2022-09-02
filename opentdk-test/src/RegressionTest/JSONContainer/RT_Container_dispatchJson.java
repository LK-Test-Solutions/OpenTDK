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
		BaseRegression.testResult(EJsonValues.SIR.getValue(), "SIR", "true");
		BaseRegression.testResult(EJsonValues.SIR.getValues()[0], "SIR", "true");
		BaseRegression.testResult(EJsonValues.PHONE_NUMBERS.getValue(), "PHONE_NUMBERS", "[123456,987654]");
		BaseRegression.testResult(EJsonValues.PHONE_NUMBERS.getValues()[0], "PHONE_NUMBERS", "[123456,987654]");

		EJsonValues.SIR.setValue("false");
		EJsonValues.PHONE_NUMBERS.setValue("[123456,654321]");
		EJsonValues.PHONE_NUMBERS.addValue("485625");
		BaseRegression.testResult(EJsonValues.SIR.getValue(), "SIR after set", "false");
		BaseRegression.testResult(EJsonValues.PHONE_NUMBERS.getValue(), "PHONE_NUMBERS after set", "[123456,987654,485625]");
		
		BaseDispatcher.getDataContainer(EJsonValues.class).writeData("output/JsonOutput.json");
	}

}
