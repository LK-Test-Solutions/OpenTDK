package RegressionTest.Dispatcher;

import org.opentdk.api.dispatcher.BaseDispatchComponent;

public class ESettings_values_Dispatcher {
	
	public static final BaseDispatchComponent NAME = new BaseDispatchComponent(ESettings_values_Dispatcher.class, "Name", "/Environment/Variable", "");
	public static final BaseDispatchComponent VALUE = new BaseDispatchComponent(ESettings_values_Dispatcher.class, "Value", "/Environment/Variable[{param_1}]", "");

}
