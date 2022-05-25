package RegressionTest.CSVContainer;

import org.opentdk.api.datastorage.DataContainer;

import RegressionTest.BaseRegression;

public class RT_CSVContainer_setRow extends BaseRegression {
	
	public static void main(String[] args) {
		new RT_CSVContainer_setRow();
	}
	
	@Override
	public void runTest() {
		
		DataContainer dc = new DataContainer(new String[] { "Index", "Unternehmen", "Anwendung" });
		dc.setColumnDelimiter(",");
		dc.addRow(0, new String[] { "1", "Neotys", "Neoload" });
		dc.addRow(1, new String[] { "2", "Tricentis", "Tosca" });
		dc.addRow(2, new String[] { "3", "", "" });
		dc.addRow(3, new String[] { "4", "Atlassian", "Jira" });
	
		dc.setRow(2, new String[] { "3", "Atlassian", "Confluence"});
				
		testResult(dc.getValue("Anwendung", 2), "setRow - value", "Confluence");
	}
}
