package RegressionTest.Dispatcher;

import org.opentdk.api.dispatcher.BaseDispatchComponent;
import org.opentdk.api.dispatcher.BaseDispatcher;

public class RT_Settings_values_Dispatcher extends BaseDispatcher {
	
	public static final BaseDispatchComponent NAME = new BaseDispatchComponent(RT_Settings_values_Dispatcher.class.getSimpleName(), "Name", "/Environment/Variable", "");
	public static final BaseDispatchComponent VALUE = new BaseDispatchComponent(RT_Settings_values_Dispatcher.class.getSimpleName(), "Value", "/Environment/Variable[{param_1}]", "");

}
