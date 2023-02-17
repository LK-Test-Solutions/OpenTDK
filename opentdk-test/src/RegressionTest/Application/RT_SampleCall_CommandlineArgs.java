package RegressionTest.Application;

import RegressionTest.BaseRegression;

/**
 * Tests if the ERuntimeProperties and the EAppSettings classes in this package are set correctly
 * when the Application class gets called as {@link org.opentdk.api.application.BaseApplication} via
 * command line arguments. Triggers {@link org.opentdk.api.dispatcher.BaseDispatchComponent#setValue(String)} and
 * 
 * @author LK Test Solutions
 *
 */
public class RT_SampleCall_CommandlineArgs extends BaseRegression {

	public static void main(String[] args) {
		new RT_SampleCall_CommandlineArgs();
	}

	@Override
	protected void runTest() {
		String[] args = new String[] { "-homedir=CMD /users/holger", "-baseurl=CMD https://www.lk-test.de" };

		Application.main(args); 

		BaseRegression.testResult(ERuntimeProperties.HOMEDIR.getValue(), "HOMEDIR", "CMD /users/holger");
		BaseRegression.testResult(ERuntimeProperties.BASEURL.getValue(), "BASEURL", "CMD https://www.lk-test.de");
		// If running all test cases multiple times the EAppSettings are not empty any more and this check fails
		// The problem is if calling EAppSettings.setValue the XML file of RT_SampleCall_SettingsFile gets changed and is not valid any more
//		BaseRegression.testResult(EAppSettings.HOMEDIR.getValue(), "HOMEDIR", "");
//		BaseRegression.testResult(EAppSettings.BASEURL.getValue(), "BASEURL", "");

		ERuntimeProperties.HOMEDIR.setValue("");
		ERuntimeProperties.BASEURL.setValue("");
	}

}
