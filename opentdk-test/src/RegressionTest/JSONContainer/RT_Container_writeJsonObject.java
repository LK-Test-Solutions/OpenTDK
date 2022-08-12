package RegressionTest.JSONContainer;

import org.json.JSONObject;
import org.opentdk.api.datastorage.DataContainer;
import org.opentdk.api.io.FileUtil;

import RegressionTest.BaseRegression;

public class RT_Container_writeJsonObject extends BaseRegression {

	public static void main(String[] args) {
		new RT_Container_writeJsonObject();
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

			json.getJSONObject("address").put("city", "Berlin");
			FileUtil.writeOutputFile(json.toString(1), "output/JsonOutput.json");
			
			String city = json.query("/address/city").toString();
			BaseRegression.testResult(city, "City", "Berlin");
			
		}
		
	}

}
