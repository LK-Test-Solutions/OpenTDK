package RegressionTest.Meter;

import org.opentdk.api.meter.EMeter;

import RegressionTest.BaseRegression;

public class RT_Transaction extends BaseRegression {

	public static void main(String[] args) {
		new RT_Transaction();
	}
	
	@Override
	public void runTest() {
		System.out.println("Start RT_Transaction");		
		EMeter.TRANSACTION.start("trn1");
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {	
			throw new RuntimeException(e);
		}
		System.out.println(EMeter.TRANSACTION.end("trn1"));
		System.out.println(EMeter.TRANSACTION.end("trnX"));
	}

}
