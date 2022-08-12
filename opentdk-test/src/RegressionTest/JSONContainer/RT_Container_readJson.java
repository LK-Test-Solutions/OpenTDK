package RegressionTest.JSONContainer;

import org.opentdk.api.datastorage.DataContainer;

import RegressionTest.BaseRegression;

public class RT_Container_readJson extends BaseRegression {

	public static void main(String[] args) {
		new RT_Container_readJson();
	}

	@Override
	protected void runTest() {
		DataContainer dc = new DataContainer("testdata/RegressionTestData/JsonExample.json");
		BaseRegression.testResult(dc.getValue("cities"), "JSON node 'citites' with two values", "[\"Munich\",\"Berlin\"]");
		BaseRegression.testResult(dc.getAttributes("properties", "salary")[0], "JSON value of attribute 'salary' under key 'properties'", "1000 EUR");

//		dc.writeData("output/JsonOutput.json");
		
	}

}
