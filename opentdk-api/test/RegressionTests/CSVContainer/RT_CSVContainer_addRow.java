package RegressionTests.CSVContainer;

import java.util.ArrayList;

import org.junit.Test;
import org.opentdk.api.datastorage.DataContainer;

import RegressionSets.Api.BaseRegressionSet;

public class RT_CSVContainer_addRow {

	@Test
	public void test1() {
		System.out.println();
		System.out.println("### " + this.getClass().getSimpleName() + " test1 ###");

		ArrayList<String[]> rows = new ArrayList<>();
		rows.add(new String[] { "1", "2", "3" });
		rows.add(new String[] { "4", "5", "6" });
		rows.add(new String[] { "7", "8", "9" });

		DataContainer dc = new DataContainer(new String[] { "header1", "header2", "header3" });
		dc.addRow();
		dc.addRow(new String[] {});
		dc.addRow(1, new String[] { "a", "b", "c" });
		dc.addRows(rows);

		BaseRegressionSet.testResult(dc.getColumnDelimiter(), "addRow - delimiter", ";");
		BaseRegressionSet.testResult(dc.getValue("header2", 1), "addRow - value", "b");
		BaseRegressionSet.testResult(dc.getValue("header3", 3), "addRow - value", "3");

		System.out.println("### " + this.getClass().getSimpleName() + " test1 ###");
	}
}
