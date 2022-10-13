package RegressionTest.Dispatcher;

import org.opentdk.api.dispatcher.BaseDispatchComponent;
import org.opentdk.api.dispatcher.BaseDispatcher;

public class RT_Settings_attributes_Dispatcher extends BaseDispatcher {
	
	public static final BaseDispatchComponent RULE = new BaseDispatchComponent(RT_Settings_attributes_Dispatcher.class.getSimpleName(), "rule", "/Rules", "");
	public static final BaseDispatchComponent QUERY_FILTER = new BaseDispatchComponent(RT_Settings_attributes_Dispatcher.class.getSimpleName(), "filter", "/Rules/rule[@name='{param_1}'][@id='{param_2}']/Query", "");
	public static final BaseDispatchComponent QUERY_FILTERVALUE = new BaseDispatchComponent(RT_Settings_attributes_Dispatcher.class.getSimpleName(), "value", "/Rules/rule[@{attribute_1}='{param_1}'][@{attribute_2}='{param_2}']/Query/filter[@{attribute_3}='{param_3}']", "");
}
