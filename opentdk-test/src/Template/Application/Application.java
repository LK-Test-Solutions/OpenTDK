package Template.Application;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.lang.reflect.Field;

public class Application {

	public static void main(String[] args) {
		new Application(args);
	}
	
	public Application(String[] args) {
		
	}

	// -homepath=.
	public void parseArgs(String[] args) {
		Map<String,String> prop = new HashMap<String,String>();
		
		List<Field> propertyFields = ERuntimeProperties.getFields(ERuntimeProperties.class);
		for(String arg:args) {
			String key = arg.split("=")[0];
			String value = arg.split("=")[1];
			for(Field fld:propertyFields) {
				if(fld.getName().equalsIgnoreCase(key)) {
					prop.put(key, value);
					
					
					if(fld.getName().equalsIgnoreCase("homepath")) {
						 ERuntimeProperties.HOMEDIR.setValue(value);
					}else if(fld.getName().equalsIgnoreCase("SettingsFile")) {
						
					}
					
//					(BaseDispatchComponent) fld.getName().setValue(value);
					
				}
			}
		}
		
		
		prop.get("homepath");
		ERuntimeProperties.HOMEDIR.getValue();
		

	}
}
