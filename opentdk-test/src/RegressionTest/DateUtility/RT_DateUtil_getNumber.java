package RegressionTest.DateUtility;

import java.time.temporal.ChronoField;

import org.opentdk.api.util.DateUtil;

import RegressionTest.BaseRegression;

public class RT_DateUtil_getNumber extends BaseRegression {

	public static void main(String[] args) {
		new RT_DateUtil_getNumber();
	}
	
	@Override
	public void runTest() {	
		BaseRegression.testResult(DateUtil.getNumber("04.04.2021", ChronoField.DAY_OF_WEEK), "Day number of week", 7);
		BaseRegression.testResult(DateUtil.getNumber("2021.04.04 12:00:30", ChronoField.DAY_OF_MONTH), "Day number of month", 4);
		BaseRegression.testResult(DateUtil.getNumber("2021.04.04 12:00:30", ChronoField.DAY_OF_YEAR), "Day number of year", 94);
		BaseRegression.testResult(DateUtil.getNumber("2021.04.04 12:00:30.999", ChronoField.MILLI_OF_SECOND), "Millis of seconds", 999);		
	}

}
