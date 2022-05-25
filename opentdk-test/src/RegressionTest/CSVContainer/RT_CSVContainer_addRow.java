package RegressionTest.CSVContainer;

import java.util.ArrayList;

import org.opentdk.api.datastorage.DataContainer;

import RegressionTest.BaseRegression;

public class RT_CSVContainer_addRow extends BaseRegression {
	
	public static void main(String[] args) {
		new RT_CSVContainer_addRow();
	}

	@Override
	public void runTest() {
		
		ArrayList<String[]> rows = new ArrayList<>();
		rows.add(new String[] { "1", "2", "3" });
		rows.add(new String[] { "4", "5", "6" });
		rows.add(new String[] { "7", "8", "9" });

		DataContainer dc = new DataContainer(new String[] { "header1", "header2", "header3" });
		dc.addRow();
		dc.addRow(new String[] {});
		dc.addRow(1, new String[] { "a", "b", "c" });
		dc.addRows(rows);

		BaseRegression.testResult(dc.getColumnDelimiter(), "addRow - delimiter", ";");
		BaseRegression.testResult(dc.getValue("header2", 1), "addRow - value", "b");
		BaseRegression.testResult(dc.getValue("header3", 3), "addRow - value", "3");

	}
}
