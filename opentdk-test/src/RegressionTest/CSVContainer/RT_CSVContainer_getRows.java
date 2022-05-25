package RegressionTest.CSVContainer;

import java.util.ArrayList;
import java.util.List;

import org.opentdk.api.datastorage.DataContainer;
import org.opentdk.api.datastorage.EOperator;
import org.opentdk.api.datastorage.Filter;

import RegressionTest.BaseRegression;

public class RT_CSVContainer_getRows extends BaseRegression {

	public static void main(String[] args) {
		new RT_CSVContainer_getRows();
	}
	
	@Override
	public void runTest() {
		DataContainer dc = new DataContainer("./testdata/RegressionTestData/CSVContainer_Contacts.csv");
		test1(dc);
		test2(dc);
		test3(dc);
		test4(dc);
		test5(dc);
		test6(dc);
		test7(dc);
		test8(dc);
		test9(dc);
	}

	public void test1(DataContainer dc) {
		List<String> expResults1 = new ArrayList<>();
		expResults1.add("LK Test Solutions GmbH;Walter;81675;München;Schneckenburgerstraße;32;089/45709053;hwa@lk-test.com");
		expResults1.add("Muster AG;Mustermann;12345;Musterhausen;Musterstraße;1;0123/4567890;max@muster.mu");
		expResults1.add("LK Test Solutions GmbH;Meisinger;86450;München;Schneckenburgerstraße;32;null;fme@lk-test.de");
		expResults1.add("LK Test Solutions GmbH;Winkler;81675;München;Schneckenburgerstraße;32;null;lwi@lk-test.de");
		for (int i = 0; i < dc.getRowCount(); i++) {
			testResult(String.join(";", dc.getRow(i)), "getRow(" + i + ")", expResults1.get(i));
		}
	}

	public void test2(DataContainer dc) {
		List<String> expResults1 = new ArrayList<>();
		expResults1.add("LK Test Solutions GmbH;81675;München");
		expResults1.add("Muster AG;12345;Musterhausen");
		expResults1.add("LK Test Solutions GmbH;86450;München");
		expResults1.add("LK Test Solutions GmbH;81675;München");;
		for (int i = 0; i < dc.getRowCount(); i++) {
			testResult(String.join(";", dc.getRow(i, "Firma;PLZ;Ort")), "getRow(" + i + ", headers)", expResults1.get(i));
		}
	}

	public void test3(DataContainer dc) {
		List<String> expResults1 = new ArrayList<>();
		expResults1.add("LK Test Solutions GmbH;81675;München");
		expResults1.add("Muster AG;12345;Musterhausen");
		expResults1.add("LK Test Solutions GmbH;86450;München");
		expResults1.add("LK Test Solutions GmbH;81675;München");
		for (int i = 0; i < dc.getRowCount(); i++) {
			testResult(String.join(";", dc.getRow(i, new String[] { "Firma", "PLZ", "Ort" })), "getRow(" + i + ", headers)", expResults1.get(i));
		}
	}

	public void test4(DataContainer dc) {
		List<String> expResults1 = new ArrayList<>();
		expResults1.add("LK Test Solutions GmbH;81675;München");
		expResults1.add("");
		expResults1.add("");
		expResults1.add("LK Test Solutions GmbH;81675;München");
		Filter fltr1 = new Filter();
		fltr1.addFilterRule("PLZ", "81675", EOperator.CONTAINS);
		for (int i = 0; i < dc.getRowCount(); i++) {
			testResult(String.join(";", dc.getRow(i, "Firma;PLZ;Ort", fltr1)), "getRow(" + i + ", headers)", expResults1.get(i));
		}
	}

	public void test5(DataContainer dc) {
		List<String> expResults1 = new ArrayList<>();
		expResults1.add("LK Test Solutions GmbH;Walter;81675;München;Schneckenburgerstraße;32;089/45709053;hwa@lk-test.com");
		expResults1.add("Muster AG;Mustermann;12345;Musterhausen;Musterstraße;1;0123/4567890;max@muster.mu");
		expResults1.add("LK Test Solutions GmbH;Meisinger;86450;München;Schneckenburgerstraße;32;null;fme@lk-test.de");
		expResults1.add("LK Test Solutions GmbH;Winkler;81675;München;Schneckenburgerstraße;32;null;lwi@lk-test.de");
		List<String[]> resLst = dc.getRowsList();
		for (int i = 0; i < resLst.size(); i++) {
			testResult(String.join(";", resLst.get(i)), "getRowsList() " + i, expResults1.get(i));
		}
	}

	public void test6(DataContainer dc) {
		List<String> expResults1 = new ArrayList<>();
		expResults1.add("LK Test Solutions GmbH;Walter;81675;München;Schneckenburgerstraße;32;089/45709053;hwa@lk-test.com");
		expResults1.add("LK Test Solutions GmbH;Meisinger;86450;München;Schneckenburgerstraße;32;null;fme@lk-test.de");
		expResults1.add("LK Test Solutions GmbH;Winkler;81675;München;Schneckenburgerstraße;32;null;lwi@lk-test.de");
		Filter fltr1 = new Filter();
		fltr1.addFilterRule("Firma", "LK Test", EOperator.STARTS_WITH);
		try {
			List<String[]> resLst = dc.getRowsList(fltr1);
			for (int i = 0; i < resLst.size(); i++) {
				testResult(String.join(";", resLst.get(i)), "getRowsList(Filter) " + i, expResults1.get(i));
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	public void test7(DataContainer dc) {
		List<String> expResults1 = new ArrayList<>();
		expResults1.add("München;Walter");
		expResults1.add("München;Meisinger");
		expResults1.add("München;Winkler");
		Filter fltr2 = new Filter();
		fltr2.addFilterRule("Firma", "GmbH", EOperator.ENDS_WITH);
		try {
			List<String[]> resLst = dc.getRowsList("Ort;Nachname", fltr2);
			for (int i = 0; i < resLst.size(); i++) {
				testResult(String.join(";", resLst.get(i)), "getRowsList(headers, filter) " + i, expResults1.get(i));
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	public void test8(DataContainer dc) {
		List<String> expResults1 = new ArrayList<>();
		expResults1.add("LK Test Solutions GmbH;Meisinger");
		expResults1.add("LK Test Solutions GmbH;Winkler");
		Filter fltr3 = new Filter();
		fltr3.addFilterRule("Email", "lk-test.de", EOperator.ENDS_WITH);
		try {
			List<String[]> resLst = dc.getRowsList(new int[] { 0, 1, 2, 3 }, new String[] { "Firma", "Nachname" }, fltr3);
			for (int i = 0; i < resLst.size(); i++) {
				testResult(String.join(";", resLst.get(i)), "getRowsList(rowIndexes[], columnHeaders[], filter)", expResults1.get(i));
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
	
	public void test9(DataContainer dc) {
		List<String> expResults1 = new ArrayList<>();
		expResults1.add("0");
		expResults1.add("2");
		expResults1.add("3");
		Filter fltr3 = new Filter();
		fltr3.addFilterRule("Straße", "Musterstraße", EOperator.NOT_EQUALS);
		try {
			int[] resLst = dc.getRowsIndexes(fltr3);
			for (int i = 0; i < resLst.length; i++) {
				testResult(String.valueOf(resLst[i]), "getRowsIndexed(filter) - number", expResults1.get(i));
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

}
