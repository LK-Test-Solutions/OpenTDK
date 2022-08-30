package RegressionTest.DateUtility;

import java.time.temporal.ChronoUnit;

import org.opentdk.api.util.DateUtil;

import RegressionTest.BaseRegression;

public class RT_DateUtil_diff extends BaseRegression {

	public static void main(String[] args) {
		new RT_DateUtil_diff();
	}
	
	@Override
	public void runTest() {
		
		BaseRegression.testResult(DateUtil.diff("2020-12-31", "20210102", ChronoUnit.DAYS), "Day diff date with date", 2);
		BaseRegression.testResult(DateUtil.diff("23:59:59", "00.00.00", ChronoUnit.DAYS), "Day diff time with time", 0);
		BaseRegression.testResult(DateUtil.diff("2021-04-01-12.30.00.000000", "2021-04-05-23.30.00.000000", ChronoUnit.DAYS), "Day diff timestamp with timestamp", 4);
		BaseRegression.testResult(DateUtil.diff("2021-04-01-12.30.00.000000", "2020-04-01-12:30:00,000", ChronoUnit.DAYS), "Day diff timestamp with date", 365);
		BaseRegression.testResult(DateUtil.diff("232323", "20210404-232323", ChronoUnit.DAYS), "Day diff time with timestamp", 0);

		BaseRegression.testResult(DateUtil.diff("2021/03/29", "2021-03-30", ChronoUnit.MILLIS), "Millis diff date with date", 86400000);
		BaseRegression.testResult(DateUtil.diff("14:15:32", "14:15:30", ChronoUnit.MILLIS), "Millis diff time with time", 2000);
		BaseRegression.testResult(DateUtil.diff("2021-01-01 12:30:45.000000", "2021-01-01 12:30:46.100000", ChronoUnit.MILLIS), "Millis diff timestamp with timestamp", 1100);
		BaseRegression.testResult(DateUtil.diff("2021-03-30 01:00:00.000", "2021/03/30", ChronoUnit.MILLIS), "Millis diff timestamp with date", 3600000);
		
	}

}
