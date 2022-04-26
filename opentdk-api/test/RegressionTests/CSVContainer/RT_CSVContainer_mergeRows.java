package RegressionTests.CSVContainer;

import org.junit.Test;
import org.opentdk.api.datastorage.DataContainer;

import RegressionSets.Api.BaseRegressionSet;

public class RT_CSVContainer_mergeRows {
	
	@Test
	public void test() {
		System.out.println();
		System.out.println("### " + this.getClass().getSimpleName() + " test ###");

		DataContainer dc = new DataContainer(new String[] { "Index", "Time", "Value" });
		dc.addRow(new String[] { "1", "02:30", "Sleeping" });
		dc.addRow(new String[] { "2", "06:30", "Running" });
		dc.addRow(new String[] { "3", "", "" });
		dc.addRow(new String[] { "4", "12:00", "Eating" });
	
		dc.mergeRows(2, new String[] { "3", "9:30", "Meeting"});	
		BaseRegressionSet.testResult(dc.getValue("Value", 2), "mergeRows - value", "Meeting");
		
//		dc.mergeRows(2, new String[] { "3", "9:30", "Meeting", "Test"});

		System.out.println("### " + this.getClass().getSimpleName() + " test ###");
	}
}
