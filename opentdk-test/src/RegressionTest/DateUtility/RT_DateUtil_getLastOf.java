package RegressionTest.DateUtility;

import java.time.temporal.ChronoField;

import org.opentdk.api.util.DateUtil;
import org.opentdk.api.util.EFormat;

import RegressionTest.BaseRegression;

public class RT_DateUtil_getLastOf extends BaseRegression {

	public static void main(String[] args) {
		new RT_DateUtil_getLastOf();
	}
	
	@Override
	public void runTest() {		
		System.out.println("Last of week ==> " + DateUtil.getLastOf("04.04.2021", ChronoField.DAY_OF_WEEK, EFormat.DATE_2.getDateFormat())); // 2021-04-04
		System.out.println("Last of month ==> " + DateUtil.getLastOf("2021.04.04 12:00:30", ChronoField.DAY_OF_MONTH, EFormat.DATE_5.getDateFormat())); // 2021.04.30
		System.out.println("Last of year ==> " + DateUtil.getLastOf("2021.04.04 12:00:30", ChronoField.DAY_OF_YEAR, EFormat.DATE_4.getDateFormat())); // 31.12.2021
		System.out.println("Last time of day ==> " + DateUtil.getLastOf(ChronoField.NANO_OF_DAY, EFormat.TIME_1.getDateFormat())); // 23.59.59
		System.out.println("Last time of day ==> " + DateUtil.getLastOf("2021-04-04", ChronoField.NANO_OF_DAY, EFormat.TIMESTAMP_1.getDateFormat())); // 2021-04-04-23.59.59.999999
		
	}

}
