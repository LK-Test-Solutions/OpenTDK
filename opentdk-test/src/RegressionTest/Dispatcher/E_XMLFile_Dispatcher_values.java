package RegressionTest.Dispatcher;

import org.opentdk.api.application.EBaseSettings;
import org.opentdk.api.dispatcher.BaseDispatchComponent;

public class E_XMLFile_Dispatcher_values extends EBaseSettings {

	public static final BaseDispatchComponent PROJECT_LOCATIONS = new BaseDispatchComponent(EBaseSettings.class, "ProjectLocations", "/AppSettings", "");
	public static final BaseDispatchComponent PROJECT_LOCATION = new BaseDispatchComponent(EBaseSettings.class, "ProjectLocation", "/AppSettings/ProjectLocations", "");
	public static final BaseDispatchComponent THEMES = new BaseDispatchComponent(EBaseSettings.class, "Themes", "/AppSettings", "");
	public static final BaseDispatchComponent THEME = new BaseDispatchComponent(EBaseSettings.class, "theme", "/AppSettings/Themes", "");
	public static final BaseDispatchComponent BACKGROUND_IMAGE = new BaseDispatchComponent(EBaseSettings.class, "bg_image", "/AppSettings/Themes/theme[@name='{param_1}']", "");
}