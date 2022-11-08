package RegressionTest.Application;

import org.opentdk.api.application.EBaseSettings;
import org.opentdk.api.dispatcher.BaseDispatchComponent;

@SuppressWarnings("exports")
public class EAppSettings {
	public static final BaseDispatchComponent HOMEDIR = new BaseDispatchComponent(EBaseSettings.class, "HomeDir", "/AppSettings", "");	
	public static final BaseDispatchComponent BASEURL = new BaseDispatchComponent(EBaseSettings.class, "BaseURL", "/AppSettings", "");	
}
