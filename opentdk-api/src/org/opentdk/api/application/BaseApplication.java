package org.opentdk.api.application;

import java.util.Map;
import org.opentdk.api.dispatcher.BaseDispatcher;
import org.opentdk.api.dispatcher.BaseDispatchComponent;

/**
 * This class can be used to implement main classes of non-GUI applications. The inherited properties and methods
 * provide basic functionality and concepts to easily connect the application with configuration-data, runtime-data 
 * and generating output like writing log-messages.
 * 
 * @author LK Test Solutions GmbH
 */
public class BaseApplication {
	
	/**
	 * The main method that will automatically be called when starting the application.
	 * 
	 * @param args			String array with arguments, passed to the application from the caller (e.g. command-line)
	 * @throws Exception	Exceptions that are not handled by the application will be thrown
	 */
	public static void main(String[] args) throws Exception {
		
	}
	
	/**
	 * Default constructor that will be called when creating an instance of BaseApplication without arguments.
	 */
	public BaseApplication() {
		
	}
	
	/**
	 * Constructor that will be called, when creating an instance of BaseApplication with a String Array of arguments.
	 * This will be the case when starting the application from command-line. 
	 */
	public BaseApplication(String[] args) {
		
	}
	
    /**
     * This method can be used in case the arguments passed by command-line or any other caller are defined in the 
     * following format: 
     * <pre>
     * -key=value
     * e.g.
     * -logfile=/tmp/logs/application.log
     * </pre>
     * In addition a declaration of type {@link BaseDispatchComponent} must be implemented with the name of the key within 
     * a dispatcher class of type {@link BaseDispatcher}.
     * <pre>
     * public class ERuntimeProperties extends BaseDispatcher{
     * 	public static final BaseDispatchComponent LOGFILE = new BaseDispatchComponent(ERuntimeProperties.class, "Logfile", "");	
     * }
     * </pre>
     * If these prerequisites are fulfilled, the value of the argument will be assigned to the instance of the declared {@link BaseDispatchComponent}
     * and can be accessed by the getValue method at runtime of the application in the following way:
     * <pre>
     * ERuntimeProperties.LOGFILE.getValue();
     * </pre>
     * 
     * @param	rtproperties The class of type {@link BaseDispatcher} which includes the declaration of all runtime properties
     * @param 	args String array with keys and values passed by the commandline (e.g. -homedir=c:/applications/myApp)
     */
    public final void parseArgs(Class<?> runtimePropertiesClass, String[] args){
    	Map<String, BaseDispatchComponent> dispatchComponents = BaseDispatcher.getDeclaredComponents(runtimePropertiesClass);
        for (String arg : args) {
            String argKey = arg.split("=")[0];
            if(argKey.startsWith("-")) {
            	argKey = argKey.replace("-", "");
            }
            String value = arg.split("=")[1];
            for(String mapKey:dispatchComponents.keySet()) {
            	if(mapKey.equalsIgnoreCase(argKey)) {
            		dispatchComponents.get(mapKey).setValue(value);
            		break;
            	}
            }
        }
    }
    
    /**
     * This method checks the values from all instances of type {@link BaseDispatchComponent} that are declared within the class
     * that is defined as <code>runtimePropertiesClass</code>. For all empty values, the method will search a declared field of 
     * type {@link BaseDispatchComponent} with the same name within the defined <code>appSettingsClass</code> class and assign 
     * its value to the instance within <code>runtimePropertiesClass</code>.<br>
     * When calling this method after the method {@link #parseArgs(Class, String[])}, then several parameters of an application 
     * that are responsible for the flow control, 
     * 
     * @param runtimePropertiesClass
     * @param appSettingsClass
     */
    public final void initRuntimeProperties(Class<?> runtimePropertiesClass, Class<?> appSettingsClass) {
    	Map<String, BaseDispatchComponent> runtimeProperties = BaseDispatcher.getDeclaredComponents(runtimePropertiesClass);
    	for(String propertiesKey:runtimeProperties.keySet()) {
    		if(runtimeProperties.get(propertiesKey).getValue().isEmpty()) {
    			if(!propertiesKey.equalsIgnoreCase("SETTINGSFILE")) {
    				Map<String, BaseDispatchComponent> appSettings = BaseDispatcher.getDeclaredComponents(appSettingsClass);
    				for(String appSettingsKey:appSettings.keySet()) {
    					if(appSettingsKey.equalsIgnoreCase(propertiesKey)) {
    						runtimeProperties.get(propertiesKey).setValue(appSettings.get(appSettingsKey).getValue());
    					}
    				}
    			}
    		}
    	}
    }
}
