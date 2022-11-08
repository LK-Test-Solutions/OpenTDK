package RegressionTest.Application;

import RegressionTest.BaseRegression;

public class RT_SampleCall_SettingsFile extends BaseRegression {

	public static void main(String[] args) {
		new RT_SampleCall_SettingsFile();
	}

	@Override
	protected void runTest() {
		String[] args = new String[] {"-settingsfile=testdata/RegressionTestData/TemplateApplicationSettings.xml"};
		
		Application.main(args);		
	
		BaseRegression.testResult(EAppSettings.HOMEDIR.getValue(), "HOMEDIR", "SETTINGS /users/myApplication");
		BaseRegression.testResult(EAppSettings.BASEURL.getValue(), "BASEURL", "SETTINGS https://www.myURL.com");	
		BaseRegression.testResult(ERuntimeProperties.HOMEDIR.getValue(), "HOMEDIR", "SETTINGS /users/myApplication");
		BaseRegression.testResult(ERuntimeProperties.BASEURL.getValue(), "BASEURL", "SETTINGS https://www.myURL.com");

		ERuntimeProperties.HOMEDIR.setValue("");
		ERuntimeProperties.BASEURL.setValue("");
//		BaseDispatcher.clearDataContainer();
	}

}
