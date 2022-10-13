package RegressionTest.Dispatcher;

import RegressionTest.BaseRegression;

public class RT_Settings_values extends BaseRegression {
	
	public static void main(String[] args) {
		new RT_Settings_values();
	}
	
	@Override
	public void runTest() {
		RT_Settings_values_Dispatcher.VALUE.getValue("name=");
	}
}
