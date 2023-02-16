package RegressionTest.CSVContainer;

import org.opentdk.api.datastorage.DataContainer;

import RegressionTest.BaseRegression;

public class RT_CSVContainer_mergeRows extends BaseRegression {
	
	public static void main(String[] args) {
		new RT_CSVContainer_mergeRows();
	}
	
	@Override
	public void runTest() {
		DataContainer dc = new DataContainer();
		dc.tabInstance().setHeaders(new String[] { "Index", "Time", "Value" });
		dc.tabInstance().addRow(new String[] { "1", "02:30", "Sleeping" });
		dc.tabInstance().addRow(new String[] { "2", "06:30", "Running" });
		dc.tabInstance().addRow(new String[] { "3", "", "" });
		dc.tabInstance().addRow(new String[] { "4", "12:00", "Eating" });
	
		dc.tabInstance().mergeRows(2, new String[] { "3", "9:30", "Meeting"});	
		testResult(dc.tabInstance().getValue("Value", 2), "mergeRows - value", "Meeting");
		
//		dc.mergeRows(2, new String[] { "3", "9:30", "Meeting", "Test"});
	}
}
