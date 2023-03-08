package RegressionTest.CSVContainer;

import RegressionTest.BaseRegression;
import org.opentdk.api.datastorage.DataContainer;
import org.opentdk.api.datastorage.EHeader;

import java.io.File;
import java.io.IOException;

public class RT_CSVContainer_writeData extends BaseRegression {
	
	public static void main(String[] args) {
		new RT_CSVContainer_writeData();
	}
	
	@Override
	public void runTest() {
		
		DataContainer dc = new DataContainer();
		dc.tabInstance().setHeaders(new String[] { "Index", "Unternehmen", "Anwendung" });
		dc.tabInstance().setColumnDelimiter(",");
		dc.tabInstance().addRow(0, new String[] { "1", "Neotys", "Neoload" });
		dc.tabInstance().addRow(1, new String[] { "2", "Tricentis", "Tosca" });
		dc.tabInstance().addRow(2, new String[] { "3", "Atlassian", "Jira" });

		testResult(dc.getContainerFormat().name(), "writeData - orientation", "TEXT");
		testResult(dc.getContainerFormat().getOrientation().name(), "writeData - orientation", "UNKNOWN");

		try {
			dc.writeData(location + "output" + File.separator + "RT_CSVContainer_writeData.csv");
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
}
