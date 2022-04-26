package RegressionTests.Dispatcher;

import org.opentdk.api.dispatcher.BaseDispatchComponent;
import org.opentdk.api.dispatcher.BaseDispatcher;

public class E_PropertiesFile_Dispatcher_values extends BaseDispatcher{

	public static final BaseDispatchComponent COUNTRY = new BaseDispatchComponent(E_PropertiesFile_Dispatcher_values.class, "Country", "Germany");
	public static final BaseDispatchComponent LANGUAGE = new BaseDispatchComponent(E_PropertiesFile_Dispatcher_values.class, "Language", "german");
	public static final BaseDispatchComponent CAPITAL_CITY = new BaseDispatchComponent(E_PropertiesFile_Dispatcher_values.class, "CapitalCity", "Berlin");

}
