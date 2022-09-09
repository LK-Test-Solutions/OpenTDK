package RegressionTest.JSONContainer;

import org.opentdk.api.dispatcher.BaseDispatchComponent;
import org.opentdk.api.dispatcher.BaseDispatcher;

public class EPeopleValues extends BaseDispatcher {
	
	public static final BaseDispatchComponent PEOPLE_AGE = new BaseDispatchComponent(EPeopleValues.class, "age");

}
