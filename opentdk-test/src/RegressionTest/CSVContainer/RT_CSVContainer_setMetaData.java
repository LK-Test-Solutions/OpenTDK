package RegressionTest.CSVContainer;

import org.opentdk.api.datastorage.BaseContainer.EHeader;

import RegressionTest.BaseRegression;

import org.opentdk.api.datastorage.DataContainer;

public class RT_CSVContainer_setMetaData extends BaseRegression {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		RT_CSVContainer_setMetaData csvdc = new RT_CSVContainer_setMetaData();
		csvdc.runTest();
	}
	
	@Override
	protected void runTest() {
		
		String[] row1 = {"A11", "B12", "C13"};
		String[] row2 = {"A21", "B22", "C23"};
		String[] row3 = {"A31", "B32", "C33"};
		
		
		// Instantiate DataContainer and populate with data from CSV
		DataContainer dc = new DataContainer("./testdata/RegressionTestData/CSVContainer_TestData.csv", ";", EHeader.COLUMN);
		testResult(dc.getRowsList().size(), "# of rows in datacontainer", 4);
		
		// Add Metadata header/value pair
		dc.setMetaData("MetaDataHeader1", "MetaDataValue1");
		testResult(dc.getMetaData().size(), "# of metadata Key/Value pairs in datacontainer", 1);
		for (String[] value : dc.getRowsList()) {
			testResult(value.length, "# of columns in values row:" + value[0].toString(), 4);
		}
		
		// Add record to datacontainer value dataset
		dc.addRow(row1);
		testResult(dc.getRowsList().size(), "# of rows in datacontainer", 5);
		for (String[] value : dc.getRowsList()) {
			testResult(value.length, "# of columns in values row:" + value[0].toString(), 4);
		}
		
		// Add Metadata header/value pair
		dc.setMetaData("MetaDataHeader2", "MetaDataValue2");
		testResult(dc.getMetaData().size(), "# of metadata Key/Value pairs in datacontainer", 2);
		for (String[] value : dc.getRowsList()) {
			testResult(value.length, "# of columns in values row:" + value[0].toString(), 5);
		}
		
		// Add record to datacontainer value dataset
		dc.addRow(row2);
		testResult(dc.getRowsList().size(), "# of rows in datacontainer", 6);
		for (String[] value : dc.getRowsList()) {
			testResult(value.length, "# of columns in values row:" + value[0].toString(), 5);
		}
		
		// Add new record to datacontainer value dataset
		dc.addRow(row3);
		testResult(dc.getRowsList().size(), "# of rows in datacontainer", 7);
		for (String[] value : dc.getRowsList()) {
			testResult(value.length, "# of columns in values row:" + value[0].toString(), 5);
		}
		
		// Read new data from CSV file. These records should also have the previously defined Metadata columns
		dc.readData("./testdata/RegressionTestData/CSVContainer_TestData.csv");
		testResult(dc.getRowsList().size(), "# of rows in datacontainer", 11);
		for (String[] value : dc.getRowsList()) {
			testResult(value.length, "# of columns in values row:" + value[0].toString(), 5);
		}
		
	}

}
