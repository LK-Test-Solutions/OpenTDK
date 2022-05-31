package Tests.Utility;

import org.opentdk.api.application.EBaseSettings;
import org.opentdk.api.dispatcher.BaseDispatchComponent;

public class ESettings extends EBaseSettings{
	public static final BaseDispatchComponent HUNDNAME = new BaseDispatchComponent(EBaseSettings.class, "Hundname", "/AppSettings/Haustier", "Luna");	

}
