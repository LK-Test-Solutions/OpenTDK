package RegressionTest.Application;

import RegressionTest.BaseRegression;

public class RT_SampleCall_CommandlineArgs extends BaseRegression {

	public static void main(String[] args) {
		new RT_SampleCall_CommandlineArgs();
	}

	@Override
	protected void runTest() {
		String[] args = new String[] {"-homedir=CMD /users/holger", "-baseurl=CMD https://www.lk-test.de"};
		
		Application.main(args);		
		
		BaseRegression.testResult(ERuntimeProperties.HOMEDIR.getValue(), "HOMEDIR", "CMD /users/holger");
		BaseRegression.testResult(ERuntimeProperties.BASEURL.getValue(), "BASEURL", "CMD https://www.lk-test.de");
		BaseRegression.testResult(EAppSettings.HOMEDIR.getValue(), "HOMEDIR", "");
		BaseRegression.testResult(EAppSettings.BASEURL.getValue(), "BASEURL", "");	
		
		ERuntimeProperties.HOMEDIR.setValue("");
		ERuntimeProperties.BASEURL.setValue("");
//		BaseDispatcher.clearDataContainer();
	}

}
