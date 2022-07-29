package org.opentdk.api.application;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.opentdk.api.dispatcher.BaseDispatcher;
import org.opentdk.api.dispatcher.BaseDispatchComponent;

public class BaseApplication {
	
	public static void main(String[] args) throws Exception {
		
	}
	
	public BaseApplication() {
		
	}
	
	public BaseApplication(String[] args) {
		
	}
	
    /**
     * Parses the args array and sets the values to the dispatcher class which includes declared components with the same name. 
     * If an argument is passed by the commandline, then the value will be used prior to the default, defined by the settings file. 
     * For all other properties, the values defined within the settings will be assigned to the dispatcher class.
     * 
     * @param	rtproperties The class of type BaseDispatcher which includes the declaration of all runtime properties
     * @param 	args String array with keys and values passed by the commandline (e.g. -homedir=c:/applications/myApp)
     * @return 	List of type BaseDispatchComponent with all the fields, declared within the class 
     */
    public final List<BaseDispatchComponent> parseArgs(Class<?> rtproperties, String[] args){
    	List<BaseDispatchComponent> fldLst = new ArrayList<>();
    	fldLst.add(null);
        // Get all fields of the ERuntimeProperties
    	List<Field> propertyFields = BaseDispatcher.getFields(rtproperties);
    	//List<Field> propertyFields = BaseDispatcher.getFields(rtproperties.getClass());        
        for (String arg : args) {
            String key = arg.split("=")[0];
            if(key.startsWith("-")) {
            	key = key.replace("-", "");
            }
            String value = arg.split("=")[1];
            for (Field fld : propertyFields) {
                if (fld.getName().equalsIgnoreCase(key)) {
                    // Get the runtime object to execute the method with
                    Object fieldInstance = null;
                    try {
                        fieldInstance = fld.get(rtproperties);
                    } catch (IllegalArgumentException | IllegalAccessException e1) {
                        e1.printStackTrace();
                    }
                    if(fieldInstance != null) {
                        if(fieldInstance instanceof BaseDispatchComponent) {
                        	fldLst.add((BaseDispatchComponent) fieldInstance);
                            ((BaseDispatchComponent) fieldInstance).setValue(value);
                        }
                    }
                }
            }
        }
        return fldLst;
    }

}
