package RegressionTest.DateUtility;

import org.opentdk.api.util.DateUtil;
import org.opentdk.api.util.EFormat;

import RegressionTest.BaseRegression;

import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;

public class RT_DateUtil_get extends BaseRegression {
	
	public static void main(String[] args) {
		new RT_DateUtil_get();
	}

	@Override
	public void runTest() {
		System.out.println("######## Get formated date from current date ########");
		System.out.println("current date in FORMAT DATE_1 = " + DateUtil.get("yyyyMMdd") + " ## expected format = yyyyMMdd");
		System.out.println("current date in FORMAT DATE_2 = " + DateUtil.get("yyyy-MM-dd") + " ## expected format = yyyy-MM-dd");
		System.out.println("current date in FORMAT DATE_3 = " + DateUtil.get("dd-MM-yyyy") + " ## expected format = dd-MM-yyyy");
		System.out.println("current date in FORMAT DATE_4 = " + DateUtil.get("dd.MM.yyyy") + " ## expected format = dd.MM.yyyy");
		System.out.println("current date in FORMAT DATE_5 = " + DateUtil.get("yyyy.MM.dd") + " ## expected format = yyyy.MM.dd");
		System.out.println("current date in FORMAT DATE_6 = " + DateUtil.get("MM/dd/yyyy") + " ## expected format = MM/dd/yyyy");
		
		System.out.println();
		System.out.println("######## Get formated date from current date with static day ########");
		System.out.println("current date in FORMAT DATE_4 = " + DateUtil.get("01.MM.yyyy") + " ## expected format = dd.MM.yyyy");

		System.out.println();
		System.out.println("######## Get formated date from defined date of type String ########");
		BaseRegression.testResult(DateUtil.get("31.12.2018", EFormat.DATE_1.getDateFormat()), "Given date in FORMAT DATE_1", "20181231");
		BaseRegression.testResult(DateUtil.get("20000113", EFormat.DATE_2.getDateFormat()), "Given date in FORMAT DATE_2", "2000-01-13");
		BaseRegression.testResult(DateUtil.get("1999-02-28", EFormat.DATE_3.getDateFormat()), "Given date in FORMAT DATE_3", "28-02-1999");
		BaseRegression.testResult(DateUtil.get("2020.11.02", EFormat.DATE_4.getDateFormat()), "Given date in FORMAT DATE_4", "02.11.2020");
		BaseRegression.testResult(DateUtil.get("12/24/1985", EFormat.DATE_5.getDateFormat()), "Given date in FORMAT DATE_5", "1985.12.24");
		BaseRegression.testResult(DateUtil.get("24-12-1985", EFormat.DATE_6.getDateFormat()), "Given date in FORMAT DATE_6", "12/24/1985");

		System.out.println("");
		System.out.println("######## get formated date from defined object of type Date ########");
		String date = "12/31/2018";
		BaseRegression.testResult(DateUtil.get(date, EFormat.DATE_1.getDateFormat()), "Given date in FORMAT DATE_1", "20181231");
		BaseRegression.testResult(DateUtil.get(date, EFormat.DATE_2.getDateFormat()), "Given date in FORMAT DATE_2", "2018-12-31");
		BaseRegression.testResult(DateUtil.get(date, EFormat.DATE_3.getDateFormat()), "Given date in FORMAT DATE_3", "31-12-2018");
		BaseRegression.testResult(DateUtil.get(date, EFormat.DATE_4.getDateFormat()), "Given date in FORMAT DATE_4", "31.12.2018");
		BaseRegression.testResult(DateUtil.get(date, EFormat.DATE_5.getDateFormat()), "Given date in FORMAT DATE_5", "2018.12.31");
		BaseRegression.testResult(DateUtil.get(date, EFormat.DATE_6.getDateFormat()), "Given date in FORMAT DATE_6", "12/31/2018");
		
		System.out.println("");
		System.out.println("######## Get calculated and formated date from current date ########");
		System.out.println("current date plus 12 days in FORMAT DATE_1 = " + DateUtil.get(12, EFormat.DATE_1.getDateFormat(), ChronoUnit.DAYS) + " ## expected = today + 12 in format yyyyMMdd");

		System.out.println("");
		System.out.println("######## Get calculated and formated date from date of type string ########");
		BaseRegression.testResult(DateUtil.get("31.12.2018", -42, EFormat.DATE_2.getDateFormat(), ChronoUnit.DAYS), "Given date minus 42 days in FORMAT DATE_2", "2018-11-19");
		
		System.out.println("");
		System.out.println("######## Get calculated and formated date from defined object of type date ########");
		BaseRegression.testResult(DateUtil.get(date, -42, EFormat.DATE_2.getDateFormat(), ChronoUnit.DAYS), "Given date minus 42 days in FORMAT DATE_2", "2018-11-19");
	
//		BaseRegressionSet.testResult(String.valueOf(DateUtil.compare("20210329", "2021-03-29")), "Compare date with date", "1");
		System.out.println("Compare date with date ==> " + DateUtil.compare("20210329", "2021-03-29"));
		System.out.println("Compare time with time ==> " + DateUtil.compare("23:45:31", "23:45:30"));
		System.out.println("Compare timestamp with timestamp ==> " + DateUtil.compare("2021.03.29 12:00:56", "2021.03.29 12:00:56.001002003"));
		System.out.println("Compare timestamp with date ==> " + DateUtil.compare("20210329 15:14:22", "20210330"));
		System.out.println("Compare date with time ==> " + DateUtil.compare("2021-03-30", "15:14:22.123"));
		System.out.println("Compare time with timestamp ==> " + DateUtil.compare("15:14:22.123456", "2021-03-10.15:14:22"));
		System.out.println("Compare timestamp with timestamp ms diff ==> " + DateUtil.compare("2021-03-10.15:14:22", "2021-03-10.15:14:22.175"));
		System.out.println();
		System.out.println("Day diff date with date ==> " + DateUtil.diff("2020-12-31", "20210102", ChronoUnit.DAYS)); // 2
		System.out.println("Day diff time with time ==> " + DateUtil.diff("23:59:59", "00.00.00", ChronoUnit.DAYS)); // 0
		System.out.println("Day diff timestamp with timestamp ==> " + DateUtil.diff("2021-04-01-12.30.00.000000", "2021-04-05-23.30.00.000000", ChronoUnit.DAYS));// 4
		System.out.println("Day diff timestamp with date ==> " + DateUtil.diff("2021-04-01-12.30.00.000000", "2020-04-01-12:30:00,000", ChronoUnit.DAYS));// 365
		System.out.println("Day diff date with time ==> " + DateUtil.diff("2021/01/01", "12:00:01", ChronoUnit.DAYS));
		System.out.println("Day diff time with timestamp ==> " + DateUtil.diff("232323", "20210404-232323", ChronoUnit.DAYS)); // 0
		System.out.println();
		System.out.println("Millis diff date with date ==> " + DateUtil.diff("2021/03/29", "2021-03-30", ChronoUnit.MILLIS)); // 86400000
		System.out.println("Millis diff time with time ==> " + DateUtil.diff("14:15:32", "14:15:30", ChronoUnit.MILLIS)); // 2000
		System.out.println("Millis diff timestamp with timestamp ==> " + DateUtil.diff("2021-01-01 12:30:45.000000", "2021-01-01 12:30:46.100000", ChronoUnit.MILLIS)); // 1100
		System.out.println("Millis diff timestamp with date ==> " + DateUtil.diff("2021-03-30 01:00:00.000", "2021/03/30", ChronoUnit.MILLIS)); // 3600000
		System.out.println("Millis diff date with time ==> " + DateUtil.diff("2021/03/29", "00:00:00.123", ChronoUnit.MILLIS)); // ...123
		System.out.println("Millis diff time with timestamp ==> " + DateUtil.diff("01:00:00.000", "2021-03-30 01:00:00.000", ChronoUnit.MILLIS));
		System.out.println();
		System.out.println("Current date in format yyyyMMdd ==> " + DateUtil.get(EFormat.DATE_1.getDateFormat()));
		System.out.println("Current date in format yyyy-MM-dd ==> " + DateUtil.get(EFormat.DATE_2.getDateFormat()));
		System.out.println("Current date in format dd-MM-yyyy ==> " + DateUtil.get(EFormat.DATE_3.getDateFormat()));
		System.out.println("Current date in format dd.MM.yyyy ==> " + DateUtil.get(EFormat.DATE_4.getDateFormat()));
		System.out.println("Current date in format yyyy.MM.dd ==> " + DateUtil.get(EFormat.DATE_5.getDateFormat()));
		System.out.println("Current date in format MM/dd/yyyy ==> " + DateUtil.get(EFormat.DATE_6.getDateFormat()));
		System.out.println("Current date in format yyyy/MM/dd ==> " + DateUtil.get(EFormat.DATE_7.getDateFormat()));
		System.out.println("Current date in format ddMM ==> " + DateUtil.get(EFormat.DATE_8.getDateFormat()));
		System.out.println();
		System.out.println("Current time in format HH.mm.ss ==> " + DateUtil.get(EFormat.TIME_1.getDateFormat()));
		System.out.println("Current time in format HH:mm:ss ==> " + DateUtil.get(EFormat.TIME_2.getDateFormat()));
		System.out.println("Current time in format HH.mm.ss.SSSSSS ==> " + DateUtil.get(EFormat.TIME_3.getDateFormat()));
		System.out.println("Current time in format HH:mm:ss.SSSSSS ==> " + DateUtil.get(EFormat.TIME_4.getDateFormat()));
		System.out.println("Current time in format HHmmss ==> " + DateUtil.get(EFormat.TIME_5.getDateFormat()));
		System.out.println("Current time in format HH:mm:ss,SSS ==> " + DateUtil.get(EFormat.TIME_6.getDateFormat()));
		System.out.println("Current time in format SSS ==> " + DateUtil.get(EFormat.TIME_7.getDateFormat()));
		System.out.println("Current time in format HH:mm:ss.SSS ==> " + DateUtil.get(EFormat.TIME_8.getDateFormat()));
		System.out.println("Current time in format HH:mm:ss.SSSSSS ==> " + DateUtil.get(EFormat.TIME_9.getDateFormat()));
		System.out.println("Current time in format HH:mm:ss.SSSSSSSSS ==> " + DateUtil.get(EFormat.TIME_10.getDateFormat()));
		System.out.println();
		System.out.println("Current time stamp in format yyyy-MM-dd-HH.mm.ss.SSSSSS ==> " + DateUtil.get(EFormat.TIMESTAMP_1.getDateFormat()));
		System.out.println("Current time stamp in format yyyy-MM-dd-HH:mm:ss,SSS ==> " + DateUtil.get(EFormat.TIMESTAMP_2.getDateFormat()));
		System.out.println("Current time stamp in format yyyyMMdd-HHmmss ==> " + DateUtil.get(EFormat.TIMESTAMP_3.getDateFormat()));
		System.out.println("Current time stamp in format yyyy-MM-dd HH:mm:ss.SSS ==> " + DateUtil.get(EFormat.TIMESTAMP_4.getDateFormat()));
		System.out.println("Current time stamp in format yyyy-MM-dd HH:mm:ss.SSSSSS ==> " + DateUtil.get(EFormat.TIMESTAMP_5.getDateFormat()));
		System.out.println("Current time stamp in format yyyy.MM.dd HH:mm:ss ==> " + DateUtil.get(EFormat.TIMESTAMP_6.getDateFormat()));
		System.out.println("Current time stamp in format yyyy.MM.dd HH:mm:ss.SSSSSSSSS ==> " + DateUtil.get(EFormat.TIMESTAMP_7.getDateFormat()));
		System.out.println("Current time stamp in format yyyyMMdd HH:mm:ss ==> " + DateUtil.get(EFormat.TIMESTAMP_8.getDateFormat()));
		System.out.println("Current time stamp in format yyyy.MM.dd.HH:mm:ss ==> " + DateUtil.get(EFormat.TIMESTAMP_9.getDateFormat()));
		System.out.println("Current time stamp in format yyyy.MM.dd.HH:mm:ss.SSS ==> " + DateUtil.get(EFormat.TIMESTAMP_10.getDateFormat()));
		System.out.println();
		System.out.println(DateUtil.get("2021/01/01", EFormat.DATE_1.getDateFormat())); // 20210101
		System.out.println(DateUtil.get(1289375173771L, EFormat.DATE_1.getDateFormat())); // 20101110
		System.out.println();
		System.out.println("Travel nanos ==> " + DateUtil.get(2, EFormat.TIMESTAMP_1.getDateFormat(), ChronoUnit.NANOS));
		System.out.println("Travel micros ==> " + DateUtil.get(2, EFormat.DATE_1.getDateFormat(), ChronoUnit.MICROS));
		System.out.println("Travel millis ==> " + DateUtil.get(2, EFormat.DATE_1.getDateFormat(), ChronoUnit.MILLIS));
		System.out.println("Travel seconds ==> " + DateUtil.get(2, EFormat.DATE_1.getDateFormat(), ChronoUnit.SECONDS));
		System.out.println("Travel minutes ==> " + DateUtil.get(2, EFormat.DATE_1.getDateFormat(), ChronoUnit.MINUTES));
		System.out.println("Travel hours ==> " + DateUtil.get(2, EFormat.DATE_1.getDateFormat(), ChronoUnit.HOURS));
		System.out.println("Travel days ==> " + DateUtil.get(2, EFormat.DATE_1.getDateFormat(), ChronoUnit.DAYS));
		System.out.println("Travel weeks ==> " + DateUtil.get(2, EFormat.DATE_1.getDateFormat(), ChronoUnit.WEEKS));
		System.out.println("Travel months ==> " + DateUtil.get(2, EFormat.DATE_1.getDateFormat(), ChronoUnit.MONTHS));
		System.out.println("Travel years ==> " + DateUtil.get(2, EFormat.DATE_1.getDateFormat(), ChronoUnit.YEARS));
		System.out.println();
		System.out.println("Travel nanos ==> " + DateUtil.get("2021.04.04 12:00:00.000000000", 2, EFormat.TIMESTAMP_7.getDateFormat(), ChronoUnit.NANOS)); // 2021-04-04 12:00:00.00000002
		System.out.println("Travel micros ==> " + DateUtil.get("2021.01.01 12:00:00", 2, EFormat.DATE_1.getDateFormat(), ChronoUnit.MICROS)); // 20210101
		System.out.println("Travel millis ==> " + DateUtil.get("2021-01-01-00:00:00,000", 2, EFormat.TIMESTAMP_2.getDateFormat(), ChronoUnit.MILLIS)); // 2021-01-01-00:00:00,002
		System.out.println("Travel seconds ==> " + DateUtil.get("2021.04.04 12:00:30", 2, EFormat.TIMESTAMP_6.getDateFormat(), ChronoUnit.SECONDS)); // 2021.04.04 12:00:32
		System.out.println("Travel minutes ==> " + DateUtil.get("2021.04.04 12:00:30", 2, EFormat.TIMESTAMP_6.getDateFormat(), ChronoUnit.MINUTES)); // 2021.04.04 12:02:30
		System.out.println("Travel hours ==> " + DateUtil.get("2021.04.04 12:00:30", 2, EFormat.DATE_1.getDateFormat(), ChronoUnit.HOURS)); // 20210404
		System.out.println("Travel days ==> " + DateUtil.get("2021.04.04 12:00:30", -2, EFormat.DATE_1.getDateFormat(), ChronoUnit.DAYS)); // 20210402
		System.out.println("Travel weeks ==> " + DateUtil.get("2021.04.04 12:00:30", 2, EFormat.DATE_1.getDateFormat(), ChronoUnit.WEEKS)); // 20210418
		System.out.println("Travel months ==> " + DateUtil.get("2021.04.04 12:00:30", 2, EFormat.DATE_1.getDateFormat(), ChronoUnit.MONTHS)); // 20210604
		System.out.println("Travel years ==> " + DateUtil.get("2021.04.04 12:00:30", 2, EFormat.DATE_4.getDateFormat(), ChronoUnit.YEARS)); // 04.04.2023
		System.out.println();
		System.out.println("First of week ==> " + DateUtil.getFirstOf("04.04.2021", ChronoField.DAY_OF_WEEK, EFormat.DATE_2.getDateFormat())); // 2021-03-29
		System.out.println("First of month ==> " + DateUtil.getFirstOf("2021.04.04 12:00:30", ChronoField.DAY_OF_MONTH, EFormat.DATE_5.getDateFormat())); // 2021.04.01
		System.out.println("First of year ==> " + DateUtil.getFirstOf("2021.04.04 12:00:30", ChronoField.DAY_OF_YEAR, EFormat.DATE_4.getDateFormat())); // 01.01.2021
		System.out.println();
		System.out.println("Last of week ==> " + DateUtil.getLastOf("04.04.2021", ChronoField.DAY_OF_WEEK, EFormat.DATE_2.getDateFormat())); // 2021-04-04
		System.out.println("Last of month ==> " + DateUtil.getLastOf("2021.04.04 12:00:30", ChronoField.DAY_OF_MONTH, EFormat.DATE_5.getDateFormat())); // 2021.04.30
		System.out.println("Last of year ==> " + DateUtil.getLastOf("2021.04.04 12:00:30", ChronoField.DAY_OF_YEAR, EFormat.DATE_4.getDateFormat())); // 31.12.2021
		System.out.println();
		System.out.println("First time of day ==> " + DateUtil.getFirstOf(ChronoField.NANO_OF_DAY, EFormat.TIME_1.getDateFormat())); // 00.00.00
		System.out.println("First time of day ==> " + DateUtil.getFirstOf("2021-04-04", ChronoField.NANO_OF_DAY, EFormat.TIMESTAMP_1.getDateFormat())); // 2021-04-04-00.00.00.000000
		System.out.println("Last time of day ==> " + DateUtil.getLastOf(ChronoField.NANO_OF_DAY, EFormat.TIME_1.getDateFormat())); // 23.59.59
		System.out.println("Last time of day ==> " + DateUtil.getLastOf("2021-04-04", ChronoField.NANO_OF_DAY, EFormat.TIMESTAMP_1.getDateFormat())); // 2021-04-04-23.59.59.999999
		System.out.println();
		System.out.println("Day number of week ==> " + DateUtil.getNumber("04.04.2021", ChronoField.DAY_OF_WEEK)); // 7
		System.out.println("Day number of month ==> " + DateUtil.getNumber("2021.04.04 12:00:30", ChronoField.DAY_OF_MONTH)); // 4
		System.out.println("Day number of year ==> " + DateUtil.getNumber("2021.04.04 12:00:30", ChronoField.DAY_OF_YEAR)); // 94
		System.out.println("Millis of seconds ==> " + DateUtil.getNumber("2021.04.04 12:00:30.999", ChronoField.MILLI_OF_SECOND)); // 999
	}

}
