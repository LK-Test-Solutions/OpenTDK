package RegressionTest.JSONContainer;

import java.io.File;

import org.opentdk.api.datastorage.DataContainer;

import RegressionTest.BaseRegression;

public class RT_JSONContainer_readViaDc extends BaseRegression {

	public static void main(String[] args) {
		new RT_JSONContainer_readViaDc();
	}

	@Override
	protected void runTest() {
		DataContainer dc = new DataContainer(new File(location + "testdata/RegressionTestData/JsonExample.json"));
		// Only possible at top level
		BaseRegression.testResult(dc.get("id")[0], "ID", "1");
		BaseRegression.testResult(dc.get("name")[0], "NAME", "LK");
		BaseRegression.testResult(dc.get("permanent")[0], "PERM", "true");
		BaseRegression.testResult(dc.get("address")[0], "ADDRESS", "{\"zipcode\":87463,\"city\":\"Munich\",\"street\":\"Schneckenburgerstrasse\"}");
		BaseRegression.testResult(dc.get("phoneNumbers")[0], "PHONE_NUMBERS", "123456");
		BaseRegression.testResult(dc.get("cities")[0], "CITIES", "Munich");
			
//		BaseRegression.testResult(dc.getAttributes("properties", "salary")[0], "SALARY", "1000 EUR");
//		BaseRegression.testResult(dc.getAttributes("properties/titles", "Sir")[0], "Sir", "true");
//		BaseRegression.testResult(dc.getAttributes("properties;titles", "Sir")[0], "Sir", "true");
		
	}

}
