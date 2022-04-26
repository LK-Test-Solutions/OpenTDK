package RegressionTests.CSVContainer;

import org.junit.Test;
import org.opentdk.api.datastorage.DataContainer;
import org.opentdk.api.datastorage.BaseContainer.EHeader;
import org.opentdk.api.util.DateUtil;
import org.opentdk.api.util.EFormat;

public class RT_CSVContainer_putMetaData {

	@Test
	public void test() {
		
		/* 1. initial Row with metadata */
		DataContainer dc = new DataContainer();
		dc.putMetaData("config", "configA");
		dc.addColumn("col1");
		dc.addRow(new String[] {"value1"});
		System.out.println(String.join(";", dc.getRow(0)));
		
		/* 2. modify field of added row and check row content again */
		dc.setValue("col1", 0, "modified1");
		System.out.println(String.join(";", dc.getRow(0)));
		

		/* 3. second row with modified metadata */
		dc.putMetaData("config", "configB");
//		dc.getMetaData().replace("config", "configB");
		dc.addRow(new String[] {"value2"});
		System.out.println(String.join(";", dc.getRow(1)));
		
		/* 4. read content from CSV file with appended metadata column (Timestamp) */
		DataContainer dc1 = new DataContainer("", ";");
		dc1.putMetaData("Timestamp", DateUtil.get(EFormat.TIMESTAMP_1.getDateFormat()));
		dc1.readData("./testdata/RegressionTestData/CSVContainer_Contacts.csv");
		for(int i=0; i<dc1.getRowCount(); i++) {
			System.out.println(String.join(";", dc1.getRow(i)));
		}
	
	}

}
