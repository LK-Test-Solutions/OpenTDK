/* 
 * BSD 2-Clause License
 * 
 * Copyright (c) 2022, LK Test Solutions GmbH
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. 
 */
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
