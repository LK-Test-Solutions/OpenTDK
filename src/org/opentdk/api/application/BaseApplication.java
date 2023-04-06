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
package org.opentdk.api.application;

import java.io.IOException;
import java.util.Map;
import org.opentdk.api.dispatcher.BaseDispatcher;
import org.opentdk.api.dispatcher.BaseDispatchComponent;

/**
 * This class can be used as a superclass to implement main classes for non-GUI applications. The
 * inherited properties and methods provide basic functionality and concepts to easily connect the
 * application with configuration-data, runtime-properties and generating output like writing
 * log-messages.<br>
 * The following steps describe how to implement a new application using the concepts of OpenTDK:
 * 
 * <pre>
 * 1. Create a main class that extends {@link BaseApplication}
 * 2. Create a dispatcher class that extends {@link BaseDispatcher} for keeping runtime-properties 
 *    of the application
 * 3, Create a dispatcher class that extends {@link BaseDispatcher} for connecting settings files
 *    with comprehensive set of configuration data for the application
 * 4. Design your data-model for commandline-arguments and application settings within these two 
 *    dispatcher classes
 * 5. Call the methods parsArgs and initRuntimeProperties within the constructor of your application
 *    and pass the two dispatcher classes as arguments of the methods
 * </pre>
 * 
 * When following these steps, you are prepared to call your application with numerous
 * commandline-arguments and/or with a comprehensive settings file. This allows to run you
 * application with variable data and keep control about the flow within your application at
 * runtime.<br>
 * <br>
 * Sample main application:
 * 
 * <pre>
 * import org.opentdk.api.application.BaseApplication;
 * 
 * public class Application extends BaseApplication {
 * 	public static void main(String[] args) throws Exception {
 * 		new Application(args);
 * 	}
 * 
 * 	public Application(String[] args) throws Exception {
 * 		parseArgs(ERuntimeProperties.class, args);
 * 		initRuntimeProperties(ERuntimeProperties.class, EAppSettings.class);
 * 	}
 * }
 * </pre>
 * 
 * Sample dispatcher class for runtime-properties:
 * 
 * <pre>
 * import org.opentdk.api.dispatcher.BaseDispatchComponent;
 * import org.opentdk.api.dispatcher.BaseDispatcher;
 * 
 * public class ERuntimeProperties extends BaseDispatcher {
 * 	public static final BaseDispatchComponent HOMEDIR = new BaseDispatchComponent(ERuntimeProperties.class, "HomeDir", "");
 * 	public static final BaseDispatchComponent SETTINGSFILE = new BaseDispatchComponent(ERuntimeProperties.class, "SettingsFile", "");
 * 	public static final BaseDispatchComponent BASEURL = new BaseDispatchComponent(ERuntimeProperties.class, "BaseURL", "");
 * }
 * </pre>
 * 
 * Sample dispatcher class for application settings:
 * 
 * <pre>
 * import org.opentdk.api.application.EBaseSettings;
 * import org.opentdk.api.dispatcher.BaseDispatchComponent;
 * 
 * public class EAppSettings extends EBaseSettings {
 * 	public static final BaseDispatchComponent HOMEDIR = new BaseDispatchComponent(EBaseSettings.class, "HomeDir", "AppSettings", "");
 * 	public static final BaseDispatchComponent BASEURL = new BaseDispatchComponent(EBaseSettings.class, "BaseURL", "AppSettings", "");
 * }
 * </pre>
 * 
 * When implementing the classes like shown in the sample, you can either call the application from
 * commandline with each argument, defined in ERuntimeProperties like this:
 * 
 * <pre>
 * java.exe -jar myApplication.jar -homedir=/usr/myApplication -baseURL=https://myApp.com
 * </pre>
 * 
 * or you can define the values for homedir and baseURL within a settings-file (e.g.
 * /users/myUser/myApplicationSettings.xml) like this:
 * 
 * <pre>
 * &lt;?xml version="1.0" encoding="UTF-8" standalone="no"?&gt;
 * &lt;AppSettings&gt;
 * 	&lt;HomeDir&gt;/usr/myApplication&lt;/HomeDir&gt;
 * 	&lt;BaseURL&gt;https://myApp.com&lt;/BaseURL&gt;
 * &lt;/AppSettings&gt;
 * </pre>
 * 
 * and then call the application like this:
 * 
 * <pre>
 * java.exe -jar myApplication.jar -settingsFile=/users/myUser/myApplicationSettings.xml
 * </pre>
 * 
 * In both cases the values defined for HomeDir and BaseURL will be passed to the ERuntimeProperties
 * and they are available at runtime of the application from anywhere within the application like
 * this:
 * 
 * <pre>
 * ERuntimeProperties.HOMEDIR.getValue();
 * </pre>
 * 
 * Some more helpful samples and tutorials will be provided at
 * <a href="https://github.com/LK-Test-Solutions/OpenTDK">GitHub:OpenTDK</a> soon!
 * 
 * @author LK Test Solutions GmbH
 */
public class BaseApplication {

	/**
	 * The main method that will automatically be called when starting the application.
	 * 
	 * @param args String array with arguments, passed to the application from the caller (e.g.
	 *             command-line)
	 * @throws Exception Exceptions that are not handled by the application will be thrown
	 */
	public static void main(String[] args) throws Exception {

	}

	/**
	 * Default constructor that will be called when creating an instance of BaseApplication without
	 * arguments.
	 */
	public BaseApplication() {

	}

	/**
	 * Constructor that will be called, when creating an instance of BaseApplication with a String Array
	 * of arguments. This will be the case when starting the application from command-line.
	 * 
	 * @param args String array with arguments, passed to the application from the caller (e.g.
	 *             command-line)
	 */
	public BaseApplication(String[] args) {

	}

	/**
	 * This method can be used in case the arguments passed by command-line or any other caller are
	 * defined in the following format:
	 * 
	 * <pre>
	 * -key=value
	 * e.g.
	 * -logfile=/tmp/logs/application.log
	 * </pre>
	 * 
	 * In addition a declaration of type {@link BaseDispatchComponent} must be implemented with the name
	 * of the key within a dispatcher class of type {@link BaseDispatcher}.
	 * 
	 * <pre>
	 * public class ERuntimeProperties extends BaseDispatcher {
	 * 	public static final BaseDispatchComponent LOGFILE = new BaseDispatchComponent(ERuntimeProperties.class, "Logfile", "");
	 * }
	 * </pre>
	 * 
	 * If these prerequisites are fulfilled, the value of the argument will be assigned to the instance
	 * of the declared {@link BaseDispatchComponent} and can be accessed by the getValue method at
	 * runtime of the application in the following way:
	 * 
	 * <pre>
	 * ERuntimeProperties.LOGFILE.getValue();
	 * </pre>
	 * 
	 * @param runtimePropertiesClass The class of type {@link BaseDispatcher} which includes the
	 *                               declaration of all runtime properties
	 * @param args                   String array with keys and values passed by the commandline (e.g.
	 *                               -homedir=c:/applications/myApp)
	 */
	public final void parseArgs(Class<?> runtimePropertiesClass, String[] args) {
		String value = "";
		Map<String, BaseDispatchComponent> dispatchComponents = BaseDispatcher.getDeclaredComponents(runtimePropertiesClass);
		for (String arg : args) {
			String argKey = arg.split("=")[0];
			if (argKey.startsWith("-")) {
				value = arg.substring(argKey.length()+1);
				argKey = argKey.replace("-", "");
			}
//			String value = arg.split("=")[1];
			for (String mapKey : dispatchComponents.keySet()) {
				if (mapKey.equalsIgnoreCase(argKey)) {
					dispatchComponents.get(mapKey).setValue(value);
					break;
				}
			}
		}
	}

	/**
	 * This method checks the values from all instances of type {@link BaseDispatchComponent} that are
	 * declared within the class that is defined as <code>runtimePropertiesClass</code>. For all empty
	 * values, the method will search a declared field of type {@link BaseDispatchComponent} with the
	 * same name within the defined <code>appSettingsClass</code> class and assign its value to the
	 * instance within <code>runtimePropertiesClass</code>.<br>
	 * When calling this method after the method {@link #parseArgs(Class, String[])}, then several
	 * parameters of an application that are responsible for the flow control,
	 * 
	 * @param runtimePropertiesClass the class of type {@link BaseDispatcher} that includes the
	 *                               declarations of all runtimeProperties
	 * @param appSettingsClass       the class of type {@link BaseDispatcher} that includes the
	 *                               declarations of all application settings
	 * @throws IOException IOExceptions that are not handled by the application will be thrown
	 */
	public final void initRuntimeProperties(Class<?> runtimePropertiesClass, Class<?> appSettingsClass) throws IOException {
		Map<String, BaseDispatchComponent> runtimeProperties = BaseDispatcher.getDeclaredComponents(runtimePropertiesClass);
		Map<String, BaseDispatchComponent> appSettings = BaseDispatcher.getDeclaredComponents(appSettingsClass);
		// in case a settings-file is defined, the file will be assigned to the appSettingsClass
		if (runtimeProperties.keySet().contains("SETTINGSFILE")) {
			if (!runtimeProperties.get("SETTINGSFILE").getValue().isEmpty()) {
				BaseDispatcher.setDataContainer(EBaseSettings.class, runtimeProperties.get("SETTINGSFILE").getValue(), true);
			}
		}
		// assign the value from the appSettings duplicate instances to all empty runtimeProperties instances
		for (String propertiesKey : runtimeProperties.keySet()) {
			if (runtimeProperties.get(propertiesKey).getValue().isEmpty()) {
				if (!propertiesKey.equalsIgnoreCase("SETTINGSFILE")) {
					for (String appSettingsKey : appSettings.keySet()) {
						if (appSettingsKey.equalsIgnoreCase(propertiesKey)) {
							runtimeProperties.get(propertiesKey).setValue(appSettings.get(appSettingsKey).getValue());
						}
					}
				}
			}
		}
	}
}
