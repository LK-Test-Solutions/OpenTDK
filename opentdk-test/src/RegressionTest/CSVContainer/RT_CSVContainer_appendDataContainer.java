package RegressionTest.CSVContainer;

import RegressionTest.BaseRegression;
import org.opentdk.api.datastorage.DataContainer;
import org.opentdk.api.datastorage.EHeader;

import java.util.ArrayList;

public class RT_CSVContainer_appendDataContainer extends BaseRegression {
	
	public static void main(String[] args) {
		new RT_CSVContainer_appendDataContainer();
	}

	@Override
	public void runTest() {
		
		ArrayList<String[]> rows = new ArrayList<>();
		rows.add(new String[] { "1", "2", "3" });
		rows.add(new String[] { "4", "5", "6" });
		rows.add(new String[] { "7", "8", "9" });

		DataContainer dc = new DataContainer(EHeader.COLUMN);
		dc.tabInstance().setHeaders(new String[] { "header1", "header2", "header3" });
		dc.tabInstance().addRow();
		dc.tabInstance().addRow(new String[] {});
		dc.tabInstance().addRow(1, new String[] { "a", "b", "c" });
		dc.tabInstance().addRows(rows);

		DataContainer dcAppend = new DataContainer(EHeader.COLUMN);
		dcAppend.tabInstance().setHeaders(new String[] { "header1", "header2", "header3" });
		dcAppend.tabInstance().addRow(new String[] { "10", "11", "12" });
		dc.tabInstance().appendDataContainer(dcAppend);

		BaseRegression.testResult(dc.tabInstance().getColumnDelimiter(), "appendData - delimiter", ";");
		BaseRegression.testResult(dc.tabInstance().getValue("header2", 1), "appendData - value", "b");
		BaseRegression.testResult(dc.tabInstance().getValue("header3", 3), "appendData - value", "3");
		BaseRegression.testResult(dc.tabInstance().getValue("header1", 6), "appendData - value", "10");

	}
}
