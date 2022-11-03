package RegressionTest.Dispatcher;

import org.opentdk.api.dispatcher.BaseDispatcher;

import RegressionTest.BaseRegression;

public class RT_Settings_attributes extends BaseRegression {

	public static void main(String[] args) {
		new RT_Settings_attributes();
	}

	@Override
	public void runTest() {
		for (int i = 0; i < 1; i++) {

			ESettings_attributes_Dispatcher.QUERY_FILTERVALUE.setValue("name=Select_Statement;id=1;column=filter1", "queryFilterValue");

			System.out.println(BaseDispatcher.getDataContainer(ESettings_attributes_Dispatcher.class).asString());
			System.out.println();

			BaseRegression.testResult(ESettings_attributes_Dispatcher.RULE.getAttribute("name"), "RULE", "Select_Statement");
			BaseRegression.testResult(ESettings_attributes_Dispatcher.QUERY_FILTER.getAttribute("Select_Statement;1", "column"), "QUERY_FILTER", "filter1");
			BaseRegression.testResult(ESettings_attributes_Dispatcher.QUERY_FILTERVALUE.getValue("name=Select_Statement;id=1;column=filter1"), "QUERY_FILTERVALUE", "queryFilterValue");

		}
	}
}
