package RegressionTest.CSVContainer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.opentdk.api.datastorage.DataContainer;
import org.opentdk.api.util.DateUtil;
import org.opentdk.api.util.EFormat;

import RegressionTest.BaseRegression;

public class RT_CSVContainer_putMetaData extends BaseRegression {

	public static void main(String[] args) {
		new RT_CSVContainer_putMetaData();
	}

	@Override
	public void runTest() {

		DataContainer dc = new DataContainer();
		dc.tabInstance().putMetaData("config", "configA");
		dc.tabInstance().addColumn("col1");
		dc.tabInstance().addRow(new String[] { "value1" });
		testResult(String.join(";", dc.tabInstance().getRow(0)), "Row with metadata", "value1;configA");

		dc.tabInstance().setValue("col1", 0, "modified1");
		testResult(String.join(";", dc.tabInstance().getRow(0)), "Modify field of added row", "modified1;configA");

		dc.tabInstance().putMetaData("config", "configB");
		dc.tabInstance().addRow(new String[] { "value2" });
		testResult(String.join(";", dc.tabInstance().getRow(1)), "Second row with modified metadata", "value2;configB");

		List<String> expResults1 = new ArrayList<>();
		expResults1.add("LK Test Solutions GmbH;Walter;81675;Muenchen;Schneckenburgerstrasse;32;089/45709053;hwa@lk-test.com;2022-01-01-00.00.00.000000");
		expResults1.add("Muster AG;Mustermann;12345;Musterhausen;Musterstrasse;1;0123/4567890;max@muster.mu;2022-01-01-00.00.00.000000");
		expResults1.add("LK Test Solutions GmbH;Meisinger;86450;Muenchen;Schneckenburgerstrasse;32;null;fme@lk-test.de;2022-01-01-00.00.00.000000");
		expResults1.add("LK Test Solutions GmbH;Winkler;81675;Muenchen;Schneckenburgerstrasse;32;null;lwi@lk-test.de;2022-01-01-00.00.00.000000");
		
		DataContainer dc1 = new DataContainer();
		dc1.tabInstance().setColumnDelimiter(";");
		dc1.tabInstance().putMetaData("Timestamp", DateUtil.get("2022-01-01", EFormat.TIMESTAMP_1.getDateFormat()));
		try {
			dc1.readData(new File(location + "testdata/RegressionTestData/CSVContainer_Contacts.csv"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		for (int i = 0; i < dc1.tabInstance().getRowCount(); i++) {
			testResult(String.join(";", dc1.tabInstance().getRow(i)), "CSV file with appended metadata column", expResults1.get(i));
		}

	}

}
