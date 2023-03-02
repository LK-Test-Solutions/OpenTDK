package RegressionTest.Application;

import RegressionTest.BaseRegression;

/**
 * Tests if the ERuntimeProperties and the EAppSettings classes in this package are set correctly
 * when the Application class gets called as {@link org.opentdk.api.application.BaseApplication} via
 * settings XML.
 * 
 * @author LK Test Solutions
 *
 */
public class RT_SampleCall_SettingsFile extends BaseRegression {

	public static void main(String[] args) {
		new RT_SampleCall_SettingsFile();
	}

	@Override
	protected void runTest() {
		// File exists in this scenario
		String file = "-settingsfile=" + location + "testdata/RegressionTestData/TemplateApplicationSettings.xml";
		String[] args = new String[] {file};
		
		Application.main(args);		
	
		BaseRegression.testResult(ERuntimeProperties.HOMEDIR.getValue(), "HOMEDIR", "SETTINGS /users/myApplication");
		BaseRegression.testResult(ERuntimeProperties.BASEURL.getValue(), "BASEURL", "SETTINGS https://www.myURL.com");
		BaseRegression.testResult(EAppSettings.HOMEDIR.getValue(), "HOMEDIR", "SETTINGS /users/myApplication");
		BaseRegression.testResult(EAppSettings.BASEURL.getValue(), "BASEURL", "SETTINGS https://www.myURL.com");	

		ERuntimeProperties.HOMEDIR.setValue("");
		ERuntimeProperties.BASEURL.setValue("");
	}

}
