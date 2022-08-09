package Template.Application;

import org.opentdk.api.dispatcher.BaseDispatchComponent;
import org.opentdk.api.dispatcher.BaseDispatcher;

public class ERuntimeProperties extends BaseDispatcher{
	public static final BaseDispatchComponent HOMEDIR = new BaseDispatchComponent(ERuntimeProperties.class, "HomeDir", "");	
	public static final BaseDispatchComponent SETTINGSFILE = new BaseDispatchComponent(ERuntimeProperties.class, "SettingsFile", "");	
	public static final BaseDispatchComponent BASEURL = new BaseDispatchComponent(ERuntimeProperties.class, "BaseURL", "");	
}
