package RegressionTest.Application;

import org.opentdk.api.dispatcher.BaseDispatchComponent;

@SuppressWarnings("exports")
public class ERuntimeProperties {
	public static final BaseDispatchComponent HOMEDIR = new BaseDispatchComponent(ERuntimeProperties.class, "HomeDir", "");
	public static final BaseDispatchComponent SETTINGSFILE = new BaseDispatchComponent(ERuntimeProperties.class, "SettingsFile", "");
	public static final BaseDispatchComponent BASEURL = new BaseDispatchComponent(ERuntimeProperties.class, "BaseURL", "");
}
