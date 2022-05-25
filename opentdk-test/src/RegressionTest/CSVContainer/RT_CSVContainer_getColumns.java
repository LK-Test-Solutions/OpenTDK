package RegressionTest.CSVContainer;

import java.util.List;

import org.opentdk.api.datastorage.*;

import RegressionTest.BaseRegression;

import java.util.ArrayList;

public class RT_CSVContainer_getColumns extends BaseRegression {

	public static void main(String[] args) {
		new RT_CSVContainer_getColumns();
	}
	
	@Override
	public void runTest() {
		DataContainer dc = new DataContainer("./testdata/RegressionTestData/CSVContainer_Contacts.csv");
		
		List<String> expResults1 = new ArrayList<>();
		expResults1.add("LK Test Solutions GmbH;Muster AG;LK Test Solutions GmbH;LK Test Solutions GmbH");
		expResults1.add("Walter;Mustermann;Meisinger;Winkler");
		expResults1.add("81675;12345;86450;81675");
		expResults1.add("München;Musterhausen;München;München");
		expResults1.add("Schneckenburgerstraße;Musterstraße;Schneckenburgerstraße;Schneckenburgerstraße");
		expResults1.add("32;1;32;32");
		expResults1.add("089/45709053;0123/4567890;null;null");
		expResults1.add("hwa@lk-test.com;max@muster.mu;fme@lk-test.de;lwi@lk-test.de");
		for (int i = 0; i < dc.getColumnCount(); i++) {
			testResult(String.join(";", dc.getColumn(i)), "getColumn " + i, expResults1.get(i));
		}
	
		expResults1 = new ArrayList<>();
		expResults1.add("LK Test Solutions GmbH;Muster AG;LK Test Solutions GmbH;LK Test Solutions GmbH");
		expResults1.add("Walter;Mustermann;Meisinger;Winkler");
		expResults1.add("81675;12345;86450;81675");
		expResults1.add("München;Musterhausen;München;München");
		expResults1.add("Schneckenburgerstraße;Musterstraße;Schneckenburgerstraße;Schneckenburgerstraße");
		expResults1.add("32;1;32;32");
		expResults1.add("089/45709053;0123/4567890;null;null");
		expResults1.add("hwa@lk-test.com;max@muster.mu;fme@lk-test.de;lwi@lk-test.de");
		for (int i = 0; i < dc.getColumnCount(); i++) {
			testResult(String.join(";", dc.getColumn(dc.getHeaderName(i))), "getColumn " + i, expResults1.get(i));
		}

		expResults1 = new ArrayList<>();
		expResults1.add("hwa@lk-test.com;fme@lk-test.de;lwi@lk-test.de");
		Filter fltr1 = new Filter();
		fltr1.addFilterRule("Firma", "LK Test", EOperator.STARTS_WITH);
		testResult(String.join(";", dc.getColumn(7, fltr1)), "getColum(index, Filter)", expResults1.get(0));

		expResults1 = new ArrayList<>();
		expResults1.add("Meisinger;Winkler");
		Filter fltr2 = new Filter();
		fltr2.addFilterRule("Email", "lk-test.de", EOperator.ENDS_WITH);
		testResult(String.join(";", dc.getColumn("Nachname", fltr2)), "getColum(colName, Filter)", expResults1.get(0));

		expResults1 = new ArrayList<>();
		expResults1.add("Meisinger;Winkler");
		Filter fltr3 = new Filter();
		fltr3.addFilterRule("Email", "lk-test.de", EOperator.ENDS_WITH);
		testResult(String.join(";", dc.getColumn("Nachname", new int[] { 1, 2, 3 }, fltr3)), "getColum(colName, rowIndexes, Filter)", expResults1.get(0));

		expResults1 = new ArrayList<>();
		expResults1.add("LK Test Solutions GmbH;Muster AG;LK Test Solutions GmbH;LK Test Solutions GmbH");
		expResults1.add("Walter;Mustermann;Meisinger;Winkler");
		expResults1.add("81675;12345;86450;81675");
		expResults1.add("München;Musterhausen;München;München");
		expResults1.add("Schneckenburgerstraße;Musterstraße;Schneckenburgerstraße;Schneckenburgerstraße");
		expResults1.add("32;1;32;32");
		expResults1.add("089/45709053;0123/4567890;null;null");
		expResults1.add("hwa@lk-test.com;max@muster.mu;fme@lk-test.de;lwi@lk-test.de");
		List<String[]> resLst = dc.getColumnsList();
		for (int i = 0; i < resLst.size(); i++) {
			testResult(String.join(";", resLst.get(i)), "getColumnsList()", expResults1.get(i));
		}

		expResults1 = new ArrayList<>();
		expResults1.add("LK Test Solutions GmbH;LK Test Solutions GmbH;LK Test Solutions GmbH");
		expResults1.add("Walter;Meisinger;Winkler");
		expResults1.add("81675;86450;81675");
		expResults1.add("München;München;München");
		expResults1.add("Schneckenburgerstraße;Schneckenburgerstraße;Schneckenburgerstraße");
		expResults1.add("32;32;32");
		expResults1.add("089/45709053;null;null");
		expResults1.add("hwa@lk-test.com;fme@lk-test.de;lwi@lk-test.de");
		Filter fltr4 = new Filter();
		fltr4.addFilterRule("Firma", "LK Test", EOperator.STARTS_WITH);
		try {
			resLst = dc.getColumnsList(fltr4);
			for (int i = 0; i < resLst.size(); i++) {
				testResult(String.join(";", resLst.get(i)), "getColumnsList(Filter)", expResults1.get(i));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		expResults1 = new ArrayList<>();
		expResults1.add("Walter;Meisinger;Winkler");
		expResults1.add("hwa@lk-test.com;fme@lk-test.de;lwi@lk-test.de");
		Filter fltr5 = new Filter();
		fltr5.addFilterRule("Firma", "LK Test", EOperator.STARTS_WITH);
		try {
			resLst = dc.getColumnsList("Nachname;Email", fltr5);
			for (int i = 0; i < resLst.size(); i++) {
				testResult(String.join(";", resLst.get(i)), "getColumnsList(columnNames, Filter)", expResults1.get(i));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		expResults1 = new ArrayList<>();
		expResults1.add("Meisinger;Winkler");
		expResults1.add("fme@lk-test.de;lwi@lk-test.de");
		Filter fltr6 = new Filter();
		fltr6.addFilterRule("Firma", "LK Test", EOperator.STARTS_WITH);
		try {
			resLst = dc.getColumnsList(new String[] { "Nachname", "Email" }, new int[] { 2, 3 }, fltr6);
			for (int i = 0; i < resLst.size(); i++) {
				testResult(String.join(";", resLst.get(i)), "getColumnsList(columnNames[], rowIndexes[], Filter)", expResults1.get(i));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
