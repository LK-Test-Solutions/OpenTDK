package RegressionTest.DateUtility;

import java.time.temporal.ChronoField;

import org.opentdk.api.util.DateUtil;
import org.opentdk.api.util.EFormat;

import RegressionTest.BaseRegression;

public class RT_DateUtil_getFirstOf extends BaseRegression {

	public static void main(String[] args) {
		new RT_DateUtil_getFirstOf();
	}
	
	@Override
	public void runTest() {		
		System.out.println("First of week ==> " + DateUtil.getFirstOf("04.04.2021", ChronoField.DAY_OF_WEEK, EFormat.DATE_2.getDateFormat())); // 2021-03-29
		System.out.println("First of month ==> " + DateUtil.getFirstOf("2021.04.04 12:00:30", ChronoField.DAY_OF_MONTH, EFormat.DATE_5.getDateFormat())); // 2021.04.01
		System.out.println("First of year ==> " + DateUtil.getFirstOf("2021.04.04 12:00:30", ChronoField.DAY_OF_YEAR, EFormat.DATE_4.getDateFormat())); // 01.01.2021
		System.out.println("First time of day ==> " + DateUtil.getFirstOf(ChronoField.NANO_OF_DAY, EFormat.TIME_1.getDateFormat())); // 00.00.00
		System.out.println("First time of day ==> " + DateUtil.getFirstOf("2021-04-04", ChronoField.NANO_OF_DAY, EFormat.TIMESTAMP_1.getDateFormat())); // 2021-04-04-00.00.00.000000
		
	}

}
