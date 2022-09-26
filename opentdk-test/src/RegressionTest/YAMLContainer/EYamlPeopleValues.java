package RegressionTest.YAMLContainer;

import org.opentdk.api.dispatcher.BaseDispatchComponent;
import org.opentdk.api.dispatcher.BaseDispatcher;

public class EYamlPeopleValues extends BaseDispatcher {
	
	public static final BaseDispatchComponent PEOPLE_AGE = new BaseDispatchComponent(EYamlPeopleValues.class, "age");

}
