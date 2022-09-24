package RegressionTest.JSONContainer;

import org.opentdk.api.dispatcher.BaseDispatchComponent;
import org.opentdk.api.dispatcher.BaseDispatcher;

public class EWebControlStep extends BaseDispatcher {
    public static final BaseDispatchComponent STEPSTATUS = new BaseDispatchComponent(EWebControlStep.class, "status", "", "");
    public static final BaseDispatchComponent STEPNAME = new BaseDispatchComponent(EWebControlStep.class, "name", "", "");
    public static final BaseDispatchComponent STEPACTUALRESULT = new BaseDispatchComponent(EWebControlStep.class, "actualResult", "", "");
    public static final BaseDispatchComponent STEPEXPECTEDRESULT = new BaseDispatchComponent(EWebControlStep.class, "expectedResult", "", "");
    public static final BaseDispatchComponent STEPDESCRIPTION = new BaseDispatchComponent(EWebControlStep.class, "description", "", "");
}
