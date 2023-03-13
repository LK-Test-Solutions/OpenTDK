package RegressionTest.Container;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.opentdk.api.datastorage.DataContainer;
import org.opentdk.api.datastorage.EHeader;
import org.opentdk.api.util.ListUtil;

import RegressionTest.BaseRegression;

public class RT_Container_getHeader extends BaseRegression {

	public static void main(String[] args) {
		new RT_Container_getHeader();
	}
	
	@Override
	public void runTest() {
		DataContainer dc = new DataContainer(EHeader.COLUMN);
		dc.tabInstance().setHeaders(new String[] { "header1", "header2", "header3"});
		dc.tabInstance().setHeaders(new String[] { "header1", "header2", "header3", "header3" }, true);

		HashMap<String, Integer> compareHeaders = new HashMap<>();
		compareHeaders.put("header1", 0);
		compareHeaders.put("header2", 1);
		compareHeaders.put("header3", 2);
		compareHeaders.put("header3_2", 3);

		boolean headersEqual = dc.tabInstance().getHeaders().equals(compareHeaders);
		BaseRegression.testResult(headersEqual, "Header Compare - getHeaders", true);

		int headerIndex = dc.tabInstance().getHeaderIndex("header2");
		BaseRegression.testResult(headerIndex, "Header Compare - getHeaderIndex", 1);

		String headerName = dc.tabInstance().getHeaderName(2);
		BaseRegression.testResult(headerName, "Header Compare - getHeaderName", "header3");

		int[] headerIndices = dc.tabInstance().getHeadersIndexes("header3");
		BaseRegression.testResult(headerIndices[0], "Header Compare - getHeadersIndexes", 2);
		
		HashMap<Integer, String> headersIndexed = dc.tabInstance().getHeadersIndexed();
		Set<Integer> reference = headersIndexed.keySet();
		Set<Integer> compare = new HashSet<>();
		for(Integer value : compareHeaders.values()) {
			compare.add(value);
		}
		boolean equals = compare.equals(reference);
		BaseRegression.testResult(equals, "Header Compare - getHeadersIndexed", true);
		
		String[] headerNamesIndexed = dc.tabInstance().getHeaderNamesIndexed();
		boolean sameHeaders = compareHeaders.keySet().containsAll(ListUtil.asList(headerNamesIndexed));
		BaseRegression.testResult(sameHeaders, "Header Compare - getHeaderNamesIndexed", true);
		
		int occurances = dc.tabInstance().getHeaderOccurances(".*3");
		BaseRegression.testResult(occurances, "Header Compare - getHeaderOccurences", 1);
		
		BaseRegression.testResult(dc.getContainerFormat().getOrientation().name(), "Header Compare - getHeaderType", "COLUMN");
		BaseRegression.testResult(dc.tabInstance().getHeaderRowIndex(), "Header Compare - getHeaderRowIndex", 0);
		BaseRegression.testResult(dc.getImplicitHeaders().size(), "Header Compare - getImplicitHeaders", 0);
		
		Set<String> compareSet = new HashSet<>();
		compareSet.add("header1");
		compareSet.add("header1_2");
		compareSet.add("header2");
		compareSet.add("header3");
		compareSet.add("header3_2");
		compareSet.add("header4");
		dc.tabInstance().setHeaders(ListUtil.asList(new String[] { "header1", "header4" }));
		Set<String> dcHeaders = dc.tabInstance().getHeaders().keySet();
		BaseRegression.testResult(dcHeaders.equals(compareSet), "Header Compare - setHeaders", true);

	}

}
