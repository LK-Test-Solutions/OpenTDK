package RegressionTest.JSONContainer;

import org.json.JSONObject;
import org.opentdk.api.datastorage.DataContainer;
import org.opentdk.api.io.FileUtil;

import RegressionTest.BaseRegression;

public class RT_Container_readJsonObject extends BaseRegression {

	public static void main(String[] args) {
		new RT_Container_readJsonObject();
	}

	@Override
	protected void runTest() {
		DataContainer dc = new DataContainer("testdata/RegressionTestData/JsonExample.json");
		String content = null;
		if (!dc.getFileName().isEmpty()) {
			content = FileUtil.getContent(dc.getFileName());
		} else if (dc.getInputStream() != null) {
			content = FileUtil.getContent(dc.getInputStream());
		}
		if (content != null) {
			JSONObject json = new JSONObject(content);
			
			String city = json.query("/address/city").toString();
			BaseRegression.testResult(city, "City", "Munich");
			
			String phoneNumbers = json.query("/phoneNumbers").toString();
			BaseRegression.testResult(phoneNumbers, "City", "[123456,987654]");			
			
		}
		
	}

}
