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
		DataContainer dc = new DataContainer(new File("testdata/RegressionTestData/JsonExample.json"));
		// Only possible at top level
		BaseRegression.testResult(dc.get("id"), "ID", "1");
		BaseRegression.testResult(dc.get("name"), "NAME", "LK");
		BaseRegression.testResult(dc.get("permanent"), "PERM", "true");
		BaseRegression.testResult(dc.get("address"), "ADDRESS", "{\"zipcode\":87463,\"city\":\"Munich\",\"street\":\"Schneckenburgerstrasse\"}");
		BaseRegression.testResult(dc.get("phoneNumbers"), "PHONE_NUMBERS", "123456");
		BaseRegression.testResult(dc.get("cities"), "CITIES", "Munich");
			
//		BaseRegression.testResult(dc.getAttributes("properties", "salary")[0], "SALARY", "1000 EUR");
//		BaseRegression.testResult(dc.getAttributes("properties/titles", "Sir")[0], "Sir", "true");
//		BaseRegression.testResult(dc.getAttributes("properties;titles", "Sir")[0], "Sir", "true");
		
	}

}
