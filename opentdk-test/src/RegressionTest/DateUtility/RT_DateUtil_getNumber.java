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
		System.out.println("Day number of week ==> " + DateUtil.getNumber("04.04.2021", ChronoField.DAY_OF_WEEK)); // 7
		System.out.println("Day number of month ==> " + DateUtil.getNumber("2021.04.04 12:00:30", ChronoField.DAY_OF_MONTH)); // 4
		System.out.println("Day number of year ==> " + DateUtil.getNumber("2021.04.04 12:00:30", ChronoField.DAY_OF_YEAR)); // 94
		System.out.println("Millis of seconds ==> " + DateUtil.getNumber("2021.04.04 12:00:30.999", ChronoField.MILLI_OF_SECOND)); // 999
		
	}

}
