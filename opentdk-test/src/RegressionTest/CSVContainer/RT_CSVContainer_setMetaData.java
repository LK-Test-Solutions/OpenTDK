package RegressionTest.CSVContainer;

import RegressionTest.BaseRegression;

import java.io.File;
import java.io.IOException;

import org.opentdk.api.datastorage.DataContainer;

public class RT_CSVContainer_setMetaData extends BaseRegression {

	public static void main(String[] args) {
		new RT_CSVContainer_setMetaData();
	}
	
	@Override
	protected void runTest() {
		
		String[] row1 = {"A11", "B12", "C13"};
		String[] row2 = {"A21", "B22", "C23"};
		String[] row3 = {"A31", "B32", "C33"};
		
		
		// Instantiate DataContainer and populate with data from CSV
		DataContainer dc = new DataContainer(new File(location + "testdata/RegressionTestData/CSVContainer_TestData.csv"));
		dc.tabInstance().setColumnDelimiter(";");
		testResult(dc.tabInstance().getRowsList().size(), "# of rows in datacontainer", 4);
		
		// Add Metadata header/value pair
		dc.tabInstance().setMetaData("MetaDataHeader1", "MetaDataValue1");
		testResult(dc.tabInstance().getMetaData().size(), "# of metadata Key/Value pairs in datacontainer", 1);
		for (String[] value : dc.tabInstance().getRowsList()) {
			testResult(value.length, "# of columns in values row:" + value[0].toString(), 4);
		}
		
		// Add record to datacontainer value dataset
		dc.tabInstance().addRow(row1);
		testResult(dc.tabInstance().getRowsList().size(), "# of rows in datacontainer", 5);
		for (String[] value : dc.tabInstance().getRowsList()) {
			testResult(value.length, "# of columns in values row:" + value[0].toString(), 4);
		}
		
		// Add Metadata header/value pair
		dc.tabInstance().setMetaData("MetaDataHeader2", "MetaDataValue2");
		testResult(dc.tabInstance().getMetaData().size(), "# of metadata Key/Value pairs in datacontainer", 2);
		for (String[] value : dc.tabInstance().getRowsList()) {
			testResult(value.length, "# of columns in values row:" + value[0].toString(), 5);
		}
		
		// Add record to datacontainer value dataset
		dc.tabInstance().addRow(row2);
		testResult(dc.tabInstance().getRowsList().size(), "# of rows in datacontainer", 6);
		for (String[] value : dc.tabInstance().getRowsList()) {
			testResult(value.length, "# of columns in values row:" + value[0].toString(), 5);
		}
		
		// Add new record to datacontainer value dataset
		dc.tabInstance().addRow(row3);
		testResult(dc.tabInstance().getRowsList().size(), "# of rows in datacontainer", 7);
		for (String[] value : dc.tabInstance().getRowsList()) {
			testResult(value.length, "# of columns in values row:" + value[0].toString(), 5);
		}
		
		// Read new data from CSV file. These records should also have the previously defined Metadata columns
		try {
			dc.readData(new File(location + "testdata/RegressionTestData/CSVContainer_TestData.csv"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		testResult(dc.tabInstance().getRowsList().size(), "# of rows in datacontainer", 11);
		for (String[] value : dc.tabInstance().getRowsList()) {
			testResult(value.length, "# of columns in values row:" + value[0].toString(), 5);
		}
		
	}

}
