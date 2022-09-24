package RegressionTest.JSONContainer;

import org.opentdk.api.dispatcher.BaseDispatchComponent;
import org.opentdk.api.dispatcher.BaseDispatcher;

public class EWebControlRequest extends BaseDispatcher {
    public static final BaseDispatchComponent BROWSERNAME = new BaseDispatchComponent(EWebControlRequest.class, "browserName", "", "");
    public static final BaseDispatchComponent DEVICENAME = new BaseDispatchComponent(EWebControlRequest.class, "deviceName", "", "");
    public static final BaseDispatchComponent OSNAME = new BaseDispatchComponent(EWebControlRequest.class, "osName", "", "");
    public static final BaseDispatchComponent TESTNAME = new BaseDispatchComponent(EWebControlRequest.class, "testName", "", "");
    public static final BaseDispatchComponent TESTSTEPS = new BaseDispatchComponent(EWebControlRequest.class, "testSteps", "", "");
    public static final BaseDispatchComponent TESTSTEPS_NAME = new BaseDispatchComponent(EWebControlRequest.class, "name", "testSteps/0", "");

}
