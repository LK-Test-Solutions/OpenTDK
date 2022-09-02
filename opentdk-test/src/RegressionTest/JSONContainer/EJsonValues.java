package RegressionTest.JSONContainer;

import org.opentdk.api.dispatcher.BaseDispatchComponent;
import org.opentdk.api.dispatcher.BaseDispatcher;


public class EJsonValues extends BaseDispatcher {
	
	public static final BaseDispatchComponent ID = new BaseDispatchComponent(EJsonValues.class, "id", "", "");
	public static final BaseDispatchComponent SIR = new BaseDispatchComponent(EJsonValues.class, "Sir", "properties/titles", "");
	public static final BaseDispatchComponent PHONE_NUMBERS = new BaseDispatchComponent(EJsonValues.class, "phoneNumbers", "", "");


}
