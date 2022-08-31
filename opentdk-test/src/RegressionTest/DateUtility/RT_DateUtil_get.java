package RegressionTest.DateUtility;

import org.opentdk.api.util.DateUtil;
import org.opentdk.api.util.EFormat;

import RegressionTest.BaseRegression;

import java.time.temporal.ChronoUnit;

public class RT_DateUtil_get extends BaseRegression {
	
	public static void main(String[] args) {
		new RT_DateUtil_get();
	}

	@Override
	public void runTest() {
		
		BaseRegression.testResult(DateUtil.get("31.12.2018", EFormat.DATE_1.getDateFormat()), "Given date in FORMAT DATE_1", "20181231");
		BaseRegression.testResult(DateUtil.get("20000113", EFormat.DATE_2.getDateFormat()), "Given date in FORMAT DATE_2", "2000-01-13");
		BaseRegression.testResult(DateUtil.get("1999-02-28", EFormat.DATE_3.getDateFormat()), "Given date in FORMAT DATE_3", "28-02-1999");
		BaseRegression.testResult(DateUtil.get("2020.11.02", EFormat.DATE_4.getDateFormat()), "Given date in FORMAT DATE_4", "02.11.2020");
		BaseRegression.testResult(DateUtil.get("12/24/1985", EFormat.DATE_5.getDateFormat()), "Given date in FORMAT DATE_5", "1985.12.24");
		BaseRegression.testResult(DateUtil.get("24-12-1985", EFormat.DATE_6.getDateFormat()), "Given date in FORMAT DATE_6", "12/24/1985");

		String date = "12/31/2018";
		BaseRegression.testResult(DateUtil.get(date, EFormat.DATE_1.getDateFormat()), "Given date in FORMAT DATE_1", "20181231");
		BaseRegression.testResult(DateUtil.get(date, EFormat.DATE_2.getDateFormat()), "Given date in FORMAT DATE_2", "2018-12-31");
		BaseRegression.testResult(DateUtil.get(date, EFormat.DATE_3.getDateFormat()), "Given date in FORMAT DATE_3", "31-12-2018");
		BaseRegression.testResult(DateUtil.get(date, EFormat.DATE_4.getDateFormat()), "Given date in FORMAT DATE_4", "31.12.2018");
		BaseRegression.testResult(DateUtil.get(date, EFormat.DATE_5.getDateFormat()), "Given date in FORMAT DATE_5", "2018.12.31");
		BaseRegression.testResult(DateUtil.get(date, EFormat.DATE_6.getDateFormat()), "Given date in FORMAT DATE_6", "12/31/2018");
		
		BaseRegression.testResult(DateUtil.get("31.12.2018", -42, EFormat.DATE_2.getDateFormat(), ChronoUnit.DAYS), "Given date minus 42 days in FORMAT DATE_2", "2018-11-19");
		BaseRegression.testResult(DateUtil.get(date, -42, EFormat.DATE_2.getDateFormat(), ChronoUnit.DAYS), "Given date minus 42 days in FORMAT DATE_2", "2018-11-19");							
		BaseRegression.testResult(DateUtil.get("2021/01/01", EFormat.DATE_1.getDateFormat()), "Given date in format DATE_1", "20210101");
		BaseRegression.testResult(DateUtil.get(1289375173771L, EFormat.DATE_1.getDateFormat()), "Millseconds to date", "20101110");

		BaseRegression.testResult(DateUtil.get("2021.04.04 12:00:00.000000000", 2, EFormat.TIMESTAMP_11.getDateFormat(), ChronoUnit.NANOS), "Travel nanos", "2021.04.04 12:00:00.000000002");
		BaseRegression.testResult(DateUtil.get("2021.01.01 12:00:00", 2, EFormat.DATE_1.getDateFormat(), ChronoUnit.MICROS), "Travel micros", "20210101");
		BaseRegression.testResult(DateUtil.get("2021-01-01-00:00:00,000", 2, EFormat.TIMESTAMP_2.getDateFormat(), ChronoUnit.MILLIS), "Travel millis", "2021-01-01-00:00:00.002000");
		BaseRegression.testResult(DateUtil.get("2021.04.04 12:00:30", 2, EFormat.TIMESTAMP_6.getDateFormat(), ChronoUnit.SECONDS), "Travel seconds", "20210404 12:00:32");
		BaseRegression.testResult(DateUtil.get("2021.04.04 12:00:30", 2, EFormat.TIMESTAMP_6.getDateFormat(), ChronoUnit.MINUTES), "Travel minutes", "20210404 12:02:30");
		BaseRegression.testResult(DateUtil.get("2021.04.04 12:00:30", 2, EFormat.DATE_1.getDateFormat(), ChronoUnit.HOURS), "Travel hours", "20210404");
		BaseRegression.testResult(DateUtil.get("2021.04.04 12:00:30", -2, EFormat.DATE_1.getDateFormat(), ChronoUnit.DAYS), "Travel days", "20210402");
		BaseRegression.testResult(DateUtil.get("2021.04.04 12:00:30", 2, EFormat.DATE_1.getDateFormat(), ChronoUnit.WEEKS), "Travel weeks", "20210418");
		BaseRegression.testResult(DateUtil.get("2021.04.04 12:00:30", 2, EFormat.DATE_1.getDateFormat(), ChronoUnit.MONTHS), "Travel months", "20210604");
		BaseRegression.testResult(DateUtil.get("2021.04.04 12:00:30", 2, EFormat.DATE_4.getDateFormat(), ChronoUnit.YEARS), "Travel years", "04.04.2023");
		
	}

}
