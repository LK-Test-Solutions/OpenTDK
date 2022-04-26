package RegressionTests.Dispatcher;

import java.io.IOException;

import org.junit.BeforeClass;
import org.junit.Test;
import org.opentdk.api.application.EBaseSettings;

public class RT_Settings_attributes {

	
	@BeforeClass 
	public static void init() throws IOException {
//		FileUtil.deleteFileOrFolder("conf/AppSettings.xml");
//		E_XMLFile_Dispatcher_values.setDataContainer(EBaseSettings.class, "testdata/RegressionTestData/AppSettings.xml");
//		E_XMLFile_Dispatcher_values.getDataContainer(EBaseAppSettings.class).exportContainer("conf/AppSettings.xml");
	}
	
	private void testSettingsField(String actual, String fieldName, String expected) {
		if((actual == null && expected == null) || actual.contentEquals(expected)) {
			System.out.println("Success: " + fieldName + " == " + actual);
		} else {
			System.out.println("Failure: " + fieldName + " is \"" + actual + "\" but should be \"" + expected + "\"");
		}
	}
	
	@Test
	public void testGetAttribute() {
		// Generate some data
//		testSettingsField(EAttributes.RULE.getAttribute("name"), "EAttributes.RULE", "");
		EAttributes.RULE.setAttribute("name", "Select_Statement");
		testSettingsField(EAttributes.RULE.getAttribute("name"), "EAttributes.RULE", "Select_Statement");

//		E_XMLFile_Dispatcher_values.ATTRIBUTE.addValue("Orbit;SQL_Statement", "INSERT");
//		System.out.println(E_XMLFile_Dispatcher_values.ATTRIBUTE.getValue());
//		
//		// Check the generation
//		testSettingsField(E_XMLFile_Dispatcher_values.ATTRIBUTE.getValue("Orbit;SQL_Statement"), "E_XMLFile_Dispatcher_values.ATTRIBUTE", "SELECT");
//		
//		// Rename the parent tag
//		E_XMLFile_Dispatcher_values.ELEMENT.setAttribute("SQL_Statement", "Orbit", "name", "Prepared_Statement");
//		testSettingsField(E_XMLFile_Dispatcher_values.ELEMENT.getAttribute("Orbit", "name"), "E_XMLFile_Dispatcher_values.ELEMENT", "Prepared_Statement");
//		
//		E_XMLFile_Dispatcher_values.RULE.delete("name", "Orbit");
//		testSettingsField(E_XMLFile_Dispatcher_values.RULE.getAttribute("name"), "E_XMLFile_Dispatcher_values.RULE", null);
	}
}
