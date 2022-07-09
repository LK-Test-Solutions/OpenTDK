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
		dc.writeData("output/JsonExample.json");
		
	}

}
