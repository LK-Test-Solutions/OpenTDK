package Tests.Utility;

import java.io.File;
import java.util.Map;

import org.opentdk.api.datastorage.DataContainer;

public class dataContainer_getRow {

	public static void main(String[] args) {
		DataContainer dc = new DataContainer(new File("./testdata/RegressionTestData/CSVContainer_Contacts.csv"));
		Map<String, String> rowMap = dc.tabInstance().getRowAsMap(2);
		for(String key:rowMap.keySet()) {
			System.out.println(key + " = " + rowMap.get(key));
		}
	}

}
