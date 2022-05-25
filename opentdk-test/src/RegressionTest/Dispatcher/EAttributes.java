package RegressionTest.Dispatcher;

import org.opentdk.api.dispatcher.BaseDispatchComponent;
import org.opentdk.api.dispatcher.BaseDispatcher;

public class EAttributes extends BaseDispatcher{
	public static final BaseDispatchComponent RULE = new BaseDispatchComponent(EAttributes.class.getSimpleName(), "rule", "/Rules", "");
	public static final BaseDispatchComponent QUERY_FILTER = new BaseDispatchComponent(EAttributes.class.getSimpleName(), "filter", "/Rules/rule[@name='{param_1}']/Query", "");
	public static final BaseDispatchComponent QUERY_FILTERVALUE = new BaseDispatchComponent(EAttributes.class.getSimpleName(), "value", "/Rules/rule[@name='{param_1}']/Query/filter[@column='{param_2}']", "");
}
