package RegressionTest.Dispatcher;

import org.opentdk.api.dispatcher.BaseDispatcher;

import RegressionTest.BaseRegression;

public class RT_Settings_values extends BaseRegression {

	public static void main(String[] args) {
		new RT_Settings_values();
	}

	@Override
	public void runTest() {
		BaseDispatcher.setDataContainer(ESettings_values_Dispatcher.class, "testdata/RegressionTestData/Parameter.xml");
		String[] result = ESettings_values_Dispatcher.NAME.getValues();
		for (int i = 0; i < result.length; i++) {
			String name = result[i];
			String value = ESettings_values_Dispatcher.VALUE.getValue(String.valueOf(i + 1));
			System.out.println(name + ": " + value);
		}
	}
}
