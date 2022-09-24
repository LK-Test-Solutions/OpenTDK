package RegressionTest.JSONContainer;

import java.io.ByteArrayInputStream;

import org.opentdk.api.dispatcher.BaseDispatcher;

import RegressionTest.BaseRegression;

public class RT_JSONContainer_inputStream extends BaseRegression {
	
	private final String jsonString = "{\r\n"
			+ "  \"status\" : \"SUCCESS\",\r\n"
			+ "  \"data\" : {\r\n"
			+ "    \"link\" : \"https://F-I.experitest.com:443/selenium-934/#/7lseTUanqYhvUP5KOUnEDg\",\r\n"
			+ "    \"report_api_id\" : \"558b8b19-96d4-4b7a-99ee-070a655718f1\"\r\n"
			+ "  },\r\n"
			+ "  \"code\" : \"OK\"\r\n"
			+ "}\r\n"
			+ "";

	public static void main(String[] args) {
		new RT_JSONContainer_inputStream();
	}

	@Override
	protected void runTest() {
		
		BaseDispatcher.setDataContainer(EWebControlResponse.class, new ByteArrayInputStream(jsonString.getBytes()));
		BaseRegression.testResult(EWebControlResponse.STATUS.getValue(), "STATUS", "SUCCESS");
		
		BaseDispatcher.setDataContainer(EWebControlResponse.class, new ByteArrayInputStream("{}".getBytes()));
		EWebControlResponse.STATUS.setValue("test");		
		BaseRegression.testResult(BaseDispatcher.getDataContainer(EWebControlResponse.class).asString(), "DC AS STRING", "{\"status\": \"test\"}");

	}
	
}
