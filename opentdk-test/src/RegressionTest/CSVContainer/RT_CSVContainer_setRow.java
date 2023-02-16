package RegressionTest.CSVContainer;

import org.opentdk.api.datastorage.DataContainer;
import org.opentdk.api.datastorage.EHeader;

import RegressionTest.BaseRegression;

public class RT_CSVContainer_setRow extends BaseRegression {
	
	public static void main(String[] args) {
		new RT_CSVContainer_setRow();
	}
	
	@Override
	public void runTest() {
		
		DataContainer dc = new DataContainer(EHeader.COLUMN);
		dc.tabInstance().setHeaders(new String[] { "Index", "Unternehmen", "Anwendung" });
		dc.tabInstance().setColumnDelimiter(",");
		dc.tabInstance().addRow(0, new String[] { "1", "Neotys", "Neoload" });
		dc.tabInstance().addRow(1, new String[] { "2", "Tricentis", "Tosca" });
		dc.tabInstance().addRow(2, new String[] { "3", "", "" });
		dc.tabInstance().addRow(3, new String[] { "4", "Atlassian", "Jira" });
	
		dc.tabInstance().setRow(2, new String[] { "3", "Atlassian", "Confluence"});
				
		testResult(dc.tabInstance().getValue("Anwendung", 2), "setRow - value", "Confluence");
	}
}
