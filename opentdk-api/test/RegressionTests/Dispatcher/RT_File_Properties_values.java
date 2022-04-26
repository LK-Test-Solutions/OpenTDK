package RegressionTests.Dispatcher;

import java.io.IOException;

import org.junit.BeforeClass;
import org.junit.Test;
import org.opentdk.api.application.EBaseSettings;
import org.opentdk.api.util.ListUtil;

public class RT_File_Properties_values {

	@BeforeClass
	public static void init() throws IOException {
		E_PropertiesFile_Dispatcher_values.setDataContainer(E_PropertiesFile_Dispatcher_values.class, "testdata/RegressionTestData/RT_File_Properties_values.properties");
	}

	private void testSettingsField(String actual, String fieldName, String expected) {
		if(actual.contentEquals(expected)) {
			System.out.println("Success: " + fieldName + " == " + actual);
		} else {
			System.out.println("Failure: " + fieldName + " is \"" + actual + "\" but should be \"" + expected + "\"");
		}
	}

	@Test
	public void test() {
		
		// Check default values defined in E_PropertiesFile_Dispatcher_values.java
		testSettingsField(E_PropertiesFile_Dispatcher_values.COUNTRY.getValue(), "Country", "Germany");
		testSettingsField(E_PropertiesFile_Dispatcher_values.LANGUAGE.getValue(), "Language", "german");
		testSettingsField(E_PropertiesFile_Dispatcher_values.CAPITAL_CITY.getValue(), "CapitalCity", "Berlin");
		
		// Change one value and check the values of E_PropertiesFile_Dispatcher_values
		E_PropertiesFile_Dispatcher_values.COUNTRY.setValue("France");
		testSettingsField(E_PropertiesFile_Dispatcher_values.COUNTRY.getValue(), "Country", "France");
		testSettingsField(E_PropertiesFile_Dispatcher_values.LANGUAGE.getValue(), "Language", "german");
		testSettingsField(E_PropertiesFile_Dispatcher_values.CAPITAL_CITY.getValue(), "City", "Berlin");

		// Change the other values and check the values of E_PropertiesFile_Dispatcher_values
		E_PropertiesFile_Dispatcher_values.LANGUAGE.setValue("french");
		testSettingsField(E_PropertiesFile_Dispatcher_values.LANGUAGE.getValue(), "Language", "french");
		E_PropertiesFile_Dispatcher_values.CAPITAL_CITY.setValue("Paris");
		testSettingsField(E_PropertiesFile_Dispatcher_values.CAPITAL_CITY.getValue(), "City", "Paris");
		
		// Overwrite values in properties file
		E_PropertiesFile_Dispatcher_values.COUNTRY.setValue("England");
		E_PropertiesFile_Dispatcher_values.LANGUAGE.setValue("english");
		E_PropertiesFile_Dispatcher_values.CAPITAL_CITY.setValue("London");
		
		// Check count of each variable within properties file is 1
		testSettingsField(Integer.toString(E_PropertiesFile_Dispatcher_values.COUNTRY.getValues().length), "Country", "1");
		testSettingsField(Integer.toString(E_PropertiesFile_Dispatcher_values.LANGUAGE.getValues().length), "Language", "1");
		testSettingsField(Integer.toString(E_PropertiesFile_Dispatcher_values.CAPITAL_CITY.getValues().length), "City", "1");
		
		// Check changed values of properties file
		testSettingsField(E_PropertiesFile_Dispatcher_values.COUNTRY.getValue(), "Country", "England");
		testSettingsField(E_PropertiesFile_Dispatcher_values.LANGUAGE.getValue(), "Language", "english");
		testSettingsField(E_PropertiesFile_Dispatcher_values.CAPITAL_CITY.getValue(), "City", "London");
		
		// Delete the values from properties file
		E_PropertiesFile_Dispatcher_values.COUNTRY.delete();
		E_PropertiesFile_Dispatcher_values.LANGUAGE.delete();
		E_PropertiesFile_Dispatcher_values.CAPITAL_CITY.delete();

		// Check if default values defined in E_PropertiesFile_Dispatcher_values.java will be used again
		testSettingsField(E_PropertiesFile_Dispatcher_values.COUNTRY.getValue(), "Country", "Germany");
		testSettingsField(E_PropertiesFile_Dispatcher_values.LANGUAGE.getValue(), "Language", "german");
		testSettingsField(E_PropertiesFile_Dispatcher_values.CAPITAL_CITY.getValue(), "CapitalCity", "Berlin");
	}

}
