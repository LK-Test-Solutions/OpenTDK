package Template.Application;

import java.util.List;

import org.opentdk.api.dispatcher.BaseDispatchComponent;
import org.opentdk.api.dispatcher.BaseDispatcher;

import RegressionTest.BaseRegression;

import java.lang.reflect.Field;

public class Application extends BaseRegression {

	public static void main(String[] args) {
		new Application();
	}
	
	@Override
	protected void runTest() {
		String[] params = { "SettingsFile=conf/TestSettings.xml" };
		parseArgs(params);		
	}

	private void parseArgs(String[] args) {		
		// Initialize the ERuntimeProperties class
		BaseDispatcher.setDataContainer(ERuntimeProperties.class, "./conf/AppSettings.xml");
		// Get all fields of the ERuntimeProperties
		List<Field> propertyFields = BaseDispatcher.getFields(ERuntimeProperties.class);
		for (String arg : args) {

			String key = arg.split("=")[0];
			String value = arg.split("=")[1];

			for (Field fld : propertyFields) {
				if (fld.getName().equalsIgnoreCase(key)) {
					
					// Get the runtime object to execute the method with
					Object fieldInstance = null;
					try {
						fieldInstance = fld.get(ERuntimeProperties.class);
					} catch (IllegalArgumentException | IllegalAccessException e1) {
						e1.printStackTrace();
					}
					
					if(fieldInstance != null) {
						if(fieldInstance instanceof BaseDispatchComponent) {
							((BaseDispatchComponent) fieldInstance).setValue(value);
						}
					}
				}
			}
		}
		BaseRegression.testResult(ERuntimeProperties.SETTINGSFILE.getValue(), "New settings file", "conf/TestSettings.xml");

	}
}
