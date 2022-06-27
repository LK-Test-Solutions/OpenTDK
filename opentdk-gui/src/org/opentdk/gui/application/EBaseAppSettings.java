package org.opentdk.gui.application;

import org.opentdk.api.dispatcher.*;

/**
 * Settings class with predefined set of common application settings of type org.opentdk.api.settings.BaseDispatchComponent.
 * This class will be used as superclass to inherit the declared variables to application specific subclasses. <br><br>
 * 
 * Usage sample:<br>
 * <pre>
 * public class EApplicationSettings extends EBaseAppSettings{
 * 	...
 * }
 * </pre>
 * 
 * @author LK Test Solutions
 *
 */
public class EBaseAppSettings extends BaseDispatcher {

	/**
	 * The variable APP_AUTO_POSITION defines where the main window of an application will be opened on the screen. <br>
	 * true = center of primary screen<br>
	 * false = position where the window was closed the last time.
	 */
	public static final BaseDispatchComponent APP_AUTO_POSITION = new BaseDispatchComponent(EBaseAppSettings.class, "AutoPosition", "/AppSettings", "true");

	/**
	 * The variable APP_LANGUAGE defines the language of GUI representation.
	 */
	public static final BaseDispatchComponent APP_LANGUAGE = new BaseDispatchComponent(EBaseAppSettings.class, "Language", "/AppSettings", "en");

	/**
	 * The variable APP_LOGFILE defines the full path and name of the file, where {@link org.opentdk.api.logger.MLogger} will log all messages and errors.
	 */
	public static final BaseDispatchComponent APP_LOGFILE = new BaseDispatchComponent(EBaseAppSettings.class, "Logfile", "/AppSettings", "./logs/Application.log");
	
	/**
	 * The variable APP_LOGFILE_LIMIT defines the maximum size in kilobyte of the log file. When the limit is exceeded, MLogger archives the file
	 * with a date stamp and a new file will be created.
	 */
	public static final BaseDispatchComponent APP_LOGFILE_LIMIT = new BaseDispatchComponent(EBaseAppSettings.class, "LogFileLimit", "/AppSettings", "4000");
	
	/**
	 * The variable APP_LOGFILE_KEEP_AGE defines maximum age of log file in days. MLogger automatically deletes the file in case the age is exceeded.
	 */
	public static final BaseDispatchComponent APP_LOGFILE_KEEP_AGE = new BaseDispatchComponent(EBaseAppSettings.class, "LogKeepAge", "/AppSettings", "90");
	
	/**
	 * The variable APP_ROOT_LAYOUT_X defines the latest x-coordinate in pixel of the main windows upper left corner. The value will be overwritten when closing the application.
	 */
	public static final BaseDispatchComponent APP_ROOT_LAYOUT_POS_X = new BaseDispatchComponent(EBaseAppSettings.class, "RootLayoutPosX", "/AppSettings", "41.5");
	
	/**
	 * The variable APP_ROOT_LAYOUT_X defines the latest x-coordinate in pixel of the main windows upper left corner. The value will be overwritten when closing the application.
	 */
	public static final BaseDispatchComponent APP_ROOT_LAYOUT_POS_Y = new BaseDispatchComponent(EBaseAppSettings.class, "RootLayoutPosY", "/AppSettings", "28.5");

	/**
	 * The variable APP_ROOT_LAYOUT_HEIGHT defines the latest height of the main window in pixel. The value will be overwritten when closing the application.
	 */
	public static final BaseDispatchComponent APP_ROOT_LAYOUT_HEIGHT = new BaseDispatchComponent(EBaseAppSettings.class, "RootLayoutHeight", "/AppSettings", "600.0");

	/**
	 * The variable APP_ROOT_LAYOUT_WIDTH defines the latest width of the main window in pixel. The value will be overwritten when closing the application.
	 */
	public static final BaseDispatchComponent APP_ROOT_LAYOUT_WIDTH = new BaseDispatchComponent(EBaseAppSettings.class, "RootLayoutWidth", "/AppSettings", "1000.0");

	/**
	 * The variable APP_TRACE_LEVEL defines the level off logging messages by MLogger.<br>
	 * <pre>
	 * 0 = disable logging
	 * 1 = log errors only
	 * 2 = log errors and warnings
	 * 3 = log all messages
	 * </pre>
	 */
	public static final BaseDispatchComponent APP_TRACE_LEVEL = new BaseDispatchComponent(EBaseAppSettings.class, "TraceLevel", "/AppSettings", "1");

}
