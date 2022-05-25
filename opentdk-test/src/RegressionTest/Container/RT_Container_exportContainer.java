package RegressionTest.Container;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import org.opentdk.api.datastorage.DataContainer;
import org.opentdk.api.io.FileUtil;

import RegressionTest.BaseRegression;

public class RT_Container_exportContainer extends BaseRegression {

	public static void main(String[] args) {
		new RT_Container_exportContainer();
	}
	
	@Override
	public void runTest() {
		DataContainer dc = new DataContainer("./testdata/RegressionTestData/CSVContainer_Contacts.csv");
		String exportFile = "testdata/out.csv";
		
		try {
			dc.exportContainer("testdata/out.csv");
		} catch (IOException e) {
			e.printStackTrace();
		}

		boolean equal = true;

		DataContainer exportedContent = new DataContainer(exportFile);
		Set<String> exportedHeaders = exportedContent.getHeaders().keySet();
		Set<String> originalHeaders = dc.getHeaders().keySet();
		for (String header : exportedHeaders) {
			if (!originalHeaders.contains(header)) {
				equal = false;
			}
		}

		List<String[]> exportedValues = exportedContent.getValues();
		List<String[]> originalValues = dc.getValues();
		for (int i = 0; i < exportedValues.size(); i++) {
			String[] exportedRow = exportedValues.get(i);
			String[] originalRow = originalValues.get(i);
			for (int k = 0; k < exportedRow.length; k++) {
				String exportedValue = exportedRow[k];
				String originalValue = originalRow[k];
				if (exportedValue != null && originalValue != null) {
					if (!exportedValue.contentEquals(originalValue)) {
						equal = false;
					}
				}
			}
		}

		BaseRegression.testResult(String.valueOf(equal), "exportContainer", "true");

		try {
			FileUtil.deleteFile("testdata/out.csv");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
