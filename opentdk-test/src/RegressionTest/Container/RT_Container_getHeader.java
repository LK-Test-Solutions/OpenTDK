package RegressionTest.Container;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.opentdk.api.datastorage.DataContainer;
import org.opentdk.api.util.ListUtil;

import RegressionTest.BaseRegression;

public class RT_Container_getHeader extends BaseRegression {

	public static void main(String[] args) {
		new RT_Container_getHeader();
	}
	
	@Override
	public void runTest() {
		DataContainer dc = new DataContainer(new String[] { "header1", "header2", "header3", "header3" });

		HashMap<String, Integer> compareHeaders = new HashMap<>();
		compareHeaders.put("header1", 0);
		compareHeaders.put("header2", 1);
		compareHeaders.put("header3", 2);
		compareHeaders.put("header3_2", 3);

		boolean headersEqual = dc.getHeaders().equals(compareHeaders);
		BaseRegression.testResult(String.valueOf(headersEqual), "Header Compare - getHeaders", "true");

		int headerIndex = dc.getHeaderIndex("header2");
		BaseRegression.testResult(String.valueOf(headerIndex), "Header Compare - getHeaderIndex", "1");

		String headerName = dc.getHeaderName(2);
		BaseRegression.testResult(headerName, "Header Compare - getHeaderName", "header3");

		int[] headerIndices = dc.getHeadersIndexes("header3");
		BaseRegression.testResult(String.valueOf(headerIndices[0]), "Header Compare - getHeadersIndexes", "2");
		
		HashMap<Integer, String> headersIndexed = dc.getHeadersIndexed();
		Set<Integer> reference = headersIndexed.keySet();
		Set<Integer> compare = new HashSet<>();
		for(Integer value : compareHeaders.values()) {
			compare.add(value);
		}
		boolean equals = compare.equals(reference);
		BaseRegression.testResult(String.valueOf(equals), "Header Compare - getHeadersIndexed", "true");
		
		String[] headerNamesIndexed = dc.getHeaderNamesIndexed();
		boolean sameHeaders = compareHeaders.keySet().containsAll(ListUtil.asList(headerNamesIndexed));
		BaseRegression.testResult(String.valueOf(sameHeaders), "Header Compare - getHeaderNamesIndexed", "true");
		
		int occurances = dc.getHeaderOccurances(".*3");
		BaseRegression.testResult(String.valueOf(occurances), "Header Compare - getHeaderOccurances", "1");
		
		BaseRegression.testResult(dc.getContainerFormat().getHeaderType().name(), "Header Compare - getHeaderType", "COLUMN");
		BaseRegression.testResult(String.valueOf(dc.getHeaderRowIndex()), "Header Compare - getHeaderRowIndex", "-1");
		BaseRegression.testResult(String.valueOf(dc.getImplicitHeaders().size()), "Header Compare - getImplicitHeaders", "0");
		
		Set<String> compareSet = new HashSet<>();
		compareSet.add("header1");
		compareSet.add("header1_2");
		compareSet.add("header2");
		compareSet.add("header3");
		compareSet.add("header3_2");
		compareSet.add("header4");
		dc.setHeaders(ListUtil.asList(new String[] { "header1", "header4" }));
		Set<String> dcHeaders = dc.getHeaders().keySet();
		BaseRegression.testResult(String.valueOf(dcHeaders.equals(compareSet)), "Header Compare - setHeaders", "true");

	}

}
