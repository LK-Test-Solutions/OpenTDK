package RegressionTest.Dispatcher;

import org.opentdk.api.dispatcher.BaseDispatchComponent;

public class ESettings_attributes_Dispatcher {
	
	public static final BaseDispatchComponent RULE = new BaseDispatchComponent(ESettings_attributes_Dispatcher.class, "rule", "/Rules", "");
	public static final BaseDispatchComponent QUERY_FILTER = new BaseDispatchComponent(ESettings_attributes_Dispatcher.class, "filter", "/Rules/rule[@name='{param_1}'][@id='{param_2}']/Query", "");
	public static final BaseDispatchComponent QUERY_FILTERVALUE = new BaseDispatchComponent(ESettings_attributes_Dispatcher.class, "value", "/Rules/rule[@{attribute_1}='{param_1}'][@{attribute_2}='{param_2}']/Query/filter[@{attribute_3}='{param_3}']", "");
}
