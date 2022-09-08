package RegressionTest.JSONContainer;

import org.opentdk.api.dispatcher.BaseDispatchComponent;
import org.opentdk.api.dispatcher.BaseDispatcher;

public class EWebControlResponse extends BaseDispatcher {
    public static final BaseDispatchComponent STATUS = new BaseDispatchComponent(EWebControlResponse.class, "status", "", "");
    public static final BaseDispatchComponent DATA = new BaseDispatchComponent(EWebControlResponse.class, "data", "", "");
    public static final BaseDispatchComponent LINK = new BaseDispatchComponent(EWebControlResponse.class, "link", "data", "");
    public static final BaseDispatchComponent REPORT_API_ID = new BaseDispatchComponent(EWebControlResponse.class, "report_api_id", "data", "");
}
