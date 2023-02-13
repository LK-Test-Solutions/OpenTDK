package RegressionTest.Application;

import org.opentdk.api.dispatcher.BaseDispatchComponent;

public class ERuntimeProperties {
	public static final BaseDispatchComponent HOMEDIR = new BaseDispatchComponent(ERuntimeProperties.class, "HomeDir", "/AppSettings", "");
	public static final BaseDispatchComponent SETTINGSFILE = new BaseDispatchComponent(ERuntimeProperties.class, "SettingsFile", "/AppSettings", "");
	public static final BaseDispatchComponent BASEURL = new BaseDispatchComponent(ERuntimeProperties.class, "BaseURL", "/AppSettings", "");
}
