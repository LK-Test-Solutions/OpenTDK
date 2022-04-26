package RegressionTests.CSVContainer;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.opentdk.api.datastorage.*;

import java.util.ArrayList;

public class RT_CSVContainer_getColumns {

	DataContainer dc = new DataContainer("./testdata/RegressionTestData/CSVContainer_Contacts.csv");

	private void testResult(String actual, String fieldName, String expected) {
		if (actual.contentEquals(expected)) {
			System.out.println("Success: " + fieldName + " == " + actual);
		} else {
			System.out.println("Failure: " + fieldName + " is \"" + actual + "\" but should be \"" + expected + "\"");
			Assert.fail();
		}
	}

	@Test
	public void test1() {
		List<String> expResults1 = new ArrayList<String>();
		expResults1.add("LK Test Solutions GmbH;Muster AG;LK Test Solutions GmbH;LK Test Solutions GmbH");
		expResults1.add("Walter;Mustermann;Meisinger;Winkler");
		expResults1.add("81675;12345;86450;81675");
		expResults1.add("München;Musterhausen;München;München");
		expResults1.add("Schneckenburgerstraße;Musterstraße;Schneckenburgerstraße;Schneckenburgerstraße");
		expResults1.add("32;1;32;32");
		expResults1.add("089/45709053;0123/4567890;;");
		expResults1.add("hwa@lk-test.com;max@muster.mu;fme@lk-test.de;lwi@lk-test.de");
		System.out.println("#####################################");
		System.out.println("#  1 -  Method getColum(index)      #");
		System.out.println("#####################################");
		for (int i = 0; i < dc.getColumnCount(); i++) {
			testResult(String.join(";", dc.getColumn(i)), "getColumn " + i, expResults1.get(i));
		}
	}

	@Test
	public void test2() {
		List<String> expResults1 = new ArrayList<String>();
		expResults1.add("LK Test Solutions GmbH;Muster AG;LK Test Solutions GmbH;LK Test Solutions GmbH");
		expResults1.add("Walter;Mustermann;Meisinger;Winkler");
		expResults1.add("81675;12345;86450;81675");
		expResults1.add("München;Musterhausen;München;München");
		expResults1.add("Schneckenburgerstraße;Musterstraße;Schneckenburgerstraße;Schneckenburgerstraße");
		expResults1.add("32;1;32;32");
		expResults1.add("089/45709053;0123/4567890;;");
		expResults1.add("hwa@lk-test.com;max@muster.mu;fme@lk-test.de;lwi@lk-test.de");
		System.out.println();
		System.out.println("#####################################");
		System.out.println("#  2 -  Method getColum(colName)    #");
		System.out.println("#####################################");
		for (int i = 0; i < dc.getColumnCount(); i++) {
			testResult(String.join(";", dc.getColumn(dc.getHeaderName(i))), "getColumn " + i, expResults1.get(i));
		}
	}

	@Test
	public void test3() {
		List<String> expResults1 = new ArrayList<String>();
		expResults1.add("hwa@lk-test.com;fme@lk-test.de;lwi@lk-test.de");
		System.out.println();
		System.out.println("###########################################");
		System.out.println("#  3 -  Method getColum(index, Filter)    #");
		System.out.println("###########################################");
		Filter fltr1 = new Filter();
		fltr1.addFilterRule("Firma", "LK Test", EOperator.STARTS_WITH);
		testResult(String.join(";", dc.getColumn(7, fltr1)), "getColum(index, Filter)", expResults1.get(0));
	}

	@Test
	public void test4() {
		List<String> expResults1 = new ArrayList<String>();
		expResults1.add("Meisinger;Winkler");
		System.out.println();
		System.out.println("#############################################");
		System.out.println("#  4 -  Method getColum(colName, Filter)    #");
		System.out.println("#############################################");
		Filter fltr2 = new Filter();
		fltr2.addFilterRule("Email", "lk-test.de", EOperator.ENDS_WITH);
		testResult(String.join(";", dc.getColumn("Nachname", fltr2)), "getColum(colName, Filter)", expResults1.get(0));
	}

	@Test
	public void test5() {
		List<String> expResults1 = new ArrayList<String>();
		expResults1.add("Meisinger;Winkler");
		System.out.println();
		System.out.println("##########################################################");
		System.out.println("#  5 -  Method getColum(colName, rowIndexes, Filter)    #");
		System.out.println("##########################################################");
		Filter fltr2 = new Filter();
		fltr2.addFilterRule("Email", "lk-test.de", EOperator.ENDS_WITH);
		testResult(String.join(";", dc.getColumn("Nachname", new int[] { 1, 2, 3 }, fltr2)), "getColum(colName, rowIndexes, Filter)", expResults1.get(0));
	}

	@Test
	public void test6() {
		List<String> expResults1 = new ArrayList<String>();
		expResults1.add("LK Test Solutions GmbH;Muster AG;LK Test Solutions GmbH;LK Test Solutions GmbH");
		expResults1.add("Walter;Mustermann;Meisinger;Winkler");
		expResults1.add("81675;12345;86450;81675");
		expResults1.add("München;Musterhausen;München;München");
		expResults1.add("Schneckenburgerstraße;Musterstraße;Schneckenburgerstraße;Schneckenburgerstraße");
		expResults1.add("32;1;32;32");
		expResults1.add("089/45709053;0123/4567890;;");
		expResults1.add("hwa@lk-test.com;max@muster.mu;fme@lk-test.de;lwi@lk-test.de");
		System.out.println();
		System.out.println("####################################");
		System.out.println("#  6 - Method getColumnsList()     #");
		System.out.println("####################################");
		List<String[]> resLst = dc.getColumnsList();
		for (int i = 0; i < resLst.size(); i++) {
			testResult(String.join(";", resLst.get(i)), "getColumnsList()", expResults1.get(i));
		}
	}

	@Test
	public void test7() {
		List<String> expResults1 = new ArrayList<String>();
		expResults1.add("LK Test Solutions GmbH;LK Test Solutions GmbH;LK Test Solutions GmbH");
		expResults1.add("Walter;Meisinger;Winkler");
		expResults1.add("81675;86450;81675");
		expResults1.add("München;München;München");
		expResults1.add("Schneckenburgerstraße;Schneckenburgerstraße;Schneckenburgerstraße");
		expResults1.add("32;32;32");
		expResults1.add("089/45709053;;");
		expResults1.add("hwa@lk-test.com;fme@lk-test.de;lwi@lk-test.de");
		System.out.println();
		System.out.println("##########################################");
		System.out.println("#  7 - Method getColumnsList(Filter)     #");
		System.out.println("##########################################");
		Filter fltr3 = new Filter();
		fltr3.addFilterRule("Firma", "LK Test", EOperator.STARTS_WITH);
		try {
			List<String[]> resLst = dc.getColumnsList(fltr3);
			for (int i = 0; i < resLst.size(); i++) {
				testResult(String.join(";", resLst.get(i)), "getColumnsList(Filter)", expResults1.get(i));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void test8() {
		List<String> expResults1 = new ArrayList<String>();
		expResults1.add("Walter;Meisinger;Winkler");
		expResults1.add("hwa@lk-test.com;fme@lk-test.de;lwi@lk-test.de");
		System.out.println();
		System.out.println("#######################################################");
		System.out.println("#  8 - Method getColumnsList(columnNames, Filter)     #");
		System.out.println("#######################################################");
		Filter fltr4 = new Filter();
		fltr4.addFilterRule("Firma", "LK Test", EOperator.STARTS_WITH);
		try {
			List<String[]> resLst = dc.getColumnsList("Nachname;Email", fltr4);
			for (int i = 0; i < resLst.size(); i++) {
				testResult(String.join(";", resLst.get(i)), "getColumnsList(columnNames, Filter)", expResults1.get(i));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void test9() {
		List<String> expResults1 = new ArrayList<String>();
		expResults1.add("Meisinger;Winkler");
		expResults1.add("fme@lk-test.de;lwi@lk-test.de");
		System.out.println();
		System.out.println("#####################################################################");
		System.out.println("#  9 - Method getColumnsList(columnNames[], rowIndexes[], Filter)   #");
		System.out.println("#####################################################################");
		Filter fltr5 = new Filter();
		fltr5.addFilterRule("Firma", "LK Test", EOperator.STARTS_WITH);
		try {
			List<String[]> resLst = dc.getColumnsList(new String[] { "Nachname", "Email" }, new int[] { 2, 3 }, fltr5);
			for (int i = 0; i < resLst.size(); i++) {
				testResult(String.join(";", resLst.get(i)), "getColumnsList(columnNames[], rowIndexes[], Filter)", expResults1.get(i));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
