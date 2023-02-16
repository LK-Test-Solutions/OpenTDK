package RegressionTest.CSVContainer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.opentdk.api.datastorage.DataContainer;
import org.opentdk.api.filter.Filter;
import org.opentdk.api.mapping.EOperator;

import RegressionTest.BaseRegression;

public class RT_CSVContainer_getRows extends BaseRegression {

	public static void main(String[] args) {
		new RT_CSVContainer_getRows();
	}
	
	@Override
	public void runTest() {
		DataContainer dc = new DataContainer(new File("./testdata/RegressionTestData/CSVContainer_Contacts.csv"));
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
		expResults1.add("LK Test Solutions GmbH;Walter;81675;Muenchen;Schneckenburgerstrasse;32;089/45709053;hwa@lk-test.com");
		expResults1.add("Muster AG;Mustermann;12345;Musterhausen;Musterstrasse;1;0123/4567890;max@muster.mu");
		expResults1.add("LK Test Solutions GmbH;Meisinger;86450;Muenchen;Schneckenburgerstrasse;32;null;fme@lk-test.de");
		expResults1.add("LK Test Solutions GmbH;Winkler;81675;Muenchen;Schneckenburgerstrasse;32;null;lwi@lk-test.de");
		for (int i = 0; i < dc.tabInstance().getRowCount(); i++) {
			testResult(String.join(";", dc.tabInstance().getRow(i)), "getRow(" + i + ")", expResults1.get(i));
		}
	}

	public void test2(DataContainer dc) {
		List<String> expResults1 = new ArrayList<>();
		expResults1.add("LK Test Solutions GmbH;81675;Muenchen");
		expResults1.add("Muster AG;12345;Musterhausen");
		expResults1.add("LK Test Solutions GmbH;86450;Muenchen");
		expResults1.add("LK Test Solutions GmbH;81675;Muenchen");;
		for (int i = 0; i < dc.tabInstance().getRowCount(); i++) {
			testResult(String.join(";", dc.tabInstance().getRow(i, "Firma;PLZ;Ort")), "getRow(" + i + ", headers)", expResults1.get(i));
		}
	}

	public void test3(DataContainer dc) {
		List<String> expResults1 = new ArrayList<>();
		expResults1.add("LK Test Solutions GmbH;81675;Muenchen");
		expResults1.add("Muster AG;12345;Musterhausen");
		expResults1.add("LK Test Solutions GmbH;86450;Muenchen");
		expResults1.add("LK Test Solutions GmbH;81675;Muenchen");
		for (int i = 0; i < dc.tabInstance().getRowCount(); i++) {
			testResult(String.join(";", dc.tabInstance().getRow(i, new String[] { "Firma", "PLZ", "Ort" })), "getRow(" + i + ", headers)", expResults1.get(i));
		}
	}

	public void test4(DataContainer dc) {
		List<String> expResults1 = new ArrayList<>();
		expResults1.add("LK Test Solutions GmbH;81675;Muenchen");
		expResults1.add("");
		expResults1.add("");
		expResults1.add("LK Test Solutions GmbH;81675;Muenchen");
		Filter fltr1 = new Filter();
		fltr1.addFilterRule("PLZ", "81675", EOperator.CONTAINS);
		for (int i = 0; i < dc.tabInstance().getRowCount(); i++) {
			testResult(String.join(";", dc.tabInstance().getRow(i, "Firma;PLZ;Ort", fltr1)), "getRow(" + i + ", headers)", expResults1.get(i));
		}
	}

	public void test5(DataContainer dc) {
		List<String> expResults1 = new ArrayList<>();
		expResults1.add("LK Test Solutions GmbH;Walter;81675;Muenchen;Schneckenburgerstrasse;32;089/45709053;hwa@lk-test.com");
		expResults1.add("Muster AG;Mustermann;12345;Musterhausen;Musterstrasse;1;0123/4567890;max@muster.mu");
		expResults1.add("LK Test Solutions GmbH;Meisinger;86450;Muenchen;Schneckenburgerstrasse;32;null;fme@lk-test.de");
		expResults1.add("LK Test Solutions GmbH;Winkler;81675;Muenchen;Schneckenburgerstrasse;32;null;lwi@lk-test.de");
		List<String[]> resLst = dc.tabInstance().getRowsList();
		for (int i = 0; i < resLst.size(); i++) {
			testResult(String.join(";", resLst.get(i)), "getRowsList() " + i, expResults1.get(i));
		}
	}

	public void test6(DataContainer dc) {
		List<String> expResults1 = new ArrayList<>();
		expResults1.add("LK Test Solutions GmbH;Walter;81675;Muenchen;Schneckenburgerstrasse;32;089/45709053;hwa@lk-test.com");
		expResults1.add("LK Test Solutions GmbH;Meisinger;86450;Muenchen;Schneckenburgerstrasse;32;null;fme@lk-test.de");
		expResults1.add("LK Test Solutions GmbH;Winkler;81675;Muenchen;Schneckenburgerstrasse;32;null;lwi@lk-test.de");
		Filter fltr1 = new Filter();
		fltr1.addFilterRule("Firma", "LK Test", EOperator.STARTS_WITH);
		try {
			List<String[]> resLst = dc.tabInstance().getRowsList(fltr1);
			for (int i = 0; i < resLst.size(); i++) {
				testResult(String.join(";", resLst.get(i)), "getRowsList(Filter) " + i, expResults1.get(i));
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	public void test7(DataContainer dc) {
		List<String> expResults1 = new ArrayList<>();
		expResults1.add("Muenchen;Walter");
		expResults1.add("Muenchen;Meisinger");
		expResults1.add("Muenchen;Winkler");
		Filter fltr2 = new Filter();
		fltr2.addFilterRule("Firma", "GmbH", EOperator.ENDS_WITH);
		try {
			List<String[]> resLst = dc.tabInstance().getRowsList("Ort;Nachname", fltr2);
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
			List<String[]> resLst = dc.tabInstance().getRowsList(new int[] { 0, 1, 2, 3 }, new String[] { "Firma", "Nachname" }, fltr3);
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
		fltr3.addFilterRule("Strasse", "Musterstrasse", EOperator.NOT_EQUALS);
		try {
			int[] resLst = dc.tabInstance().getRowsIndexes(fltr3);
			for (int i = 0; i < resLst.length; i++) {
				testResult(String.valueOf(resLst[i]), "getRowsIndexed(filter) - number", expResults1.get(i));
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

}
