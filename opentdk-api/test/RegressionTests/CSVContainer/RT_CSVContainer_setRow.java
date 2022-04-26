package RegressionTests.CSVContainer;

import java.io.IOException;

import org.junit.Test;
import org.opentdk.api.datastorage.DataContainer;

import RegressionSets.Api.BaseRegressionSet;

public class RT_CSVContainer_setRow {
	
	@Test
	public void test() throws IOException {
		System.out.println();
		System.out.println("### " + this.getClass().getSimpleName() + " test ###");

		DataContainer dc = new DataContainer(new String[] { "Index", "Unternehmen", "Anwendung" });
		dc.setColumnDelimiter(",");
		dc.addRow(0, new String[] { "1", "Neotys", "Neoload" });
		dc.addRow(1, new String[] { "2", "Tricentis", "Tosca" });
		dc.addRow(2, new String[] { "3", "", "" });
		dc.addRow(3, new String[] { "4", "Atlassian", "Jira" });
	
		dc.setRow(2, new String[] { "3", "Atlassian", "Confluence"});
				
		BaseRegressionSet.testResult(dc.getValue("Anwendung", 2), "setRow - value", "Confluence");
		
//		dc.exportContainer("testdata/test.csv");
		
		System.out.println("### " + this.getClass().getSimpleName() + " test ###");
	}
}
