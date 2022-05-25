package RegressionTest.Dispatcher;

import RegressionTest.BaseRegression;

public class RT_Settings_attributes extends BaseRegression {
	
	public static void main(String[] args) {
		new RT_Settings_attributes();
	}
	
	@Override
	public void runTest() {
		// Generate some data
//		testResult(EAttributes.RULE.getAttribute("name"), "EAttributes.RULE", "");
		EAttributes.RULE.setAttribute("name", "Select_Statement");
		testResult(EAttributes.RULE.getAttribute("name"), "EAttributes.RULE", "Select_Statement");

//		E_XMLFile_Dispatcher_values.ATTRIBUTE.addValue("Orbit;SQL_Statement", "INSERT");
//		System.out.println(E_XMLFile_Dispatcher_values.ATTRIBUTE.getValue());
//		
//		// Check the generation
//		testResult(E_XMLFile_Dispatcher_values.ATTRIBUTE.getValue("Orbit;SQL_Statement"), "E_XMLFile_Dispatcher_values.ATTRIBUTE", "SELECT");
//		
//		// Rename the parent tag
//		E_XMLFile_Dispatcher_values.ELEMENT.setAttribute("SQL_Statement", "Orbit", "name", "Prepared_Statement");
//		testResult(E_XMLFile_Dispatcher_values.ELEMENT.getAttribute("Orbit", "name"), "E_XMLFile_Dispatcher_values.ELEMENT", "Prepared_Statement");
//		
//		E_XMLFile_Dispatcher_values.RULE.delete("name", "Orbit");
//		testResult(E_XMLFile_Dispatcher_values.RULE.getAttribute("name"), "E_XMLFile_Dispatcher_values.RULE", null);
	}
}
