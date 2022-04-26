package RegressionTests.Container;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.opentdk.api.datastorage.DataContainer;
import org.opentdk.api.io.FileUtil;

import RegressionSets.Api.BaseRegressionSet;

public class RT_Container_exportContainer {

	private final DataContainer dc = new DataContainer("./testdata/RegressionTestData/CSVContainer_Contacts.csv");
	private static final String exportFile = "testdata/out.csv";

	@Test
	public void test1() {
		System.out.println();
		System.out.println("### " + this.getClass().getSimpleName() + " test1 ###");

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

		BaseRegressionSet.testResult(String.valueOf(equal), "exportContainer", "true");

		try {
			FileUtil.deleteFile("testdata/out.csv");
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("### " + this.getClass().getSimpleName() + " test1 ###");
		System.out.println();
	}

}
