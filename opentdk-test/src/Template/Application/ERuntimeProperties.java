package Template.Application;

import org.opentdk.api.dispatcher.BaseDispatchComponent;
import org.opentdk.api.dispatcher.BaseDispatcher;

public class ERuntimeProperties extends BaseDispatcher{
	
	public static final BaseDispatchComponent HOMEDIR = new BaseDispatchComponent(ERuntimeProperties.class, "HomeDir", "/Properties", ".");	
	public static final BaseDispatchComponent SETTINGSFILE = new BaseDispatchComponent(ERuntimeProperties.class, "SettingsFile", "/Properties", "./conf/AppSettings");	
	
}
