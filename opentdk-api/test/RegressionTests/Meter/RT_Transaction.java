package RegressionTests.Meter;

import org.junit.Assert;
import org.junit.Test;
import org.opentdk.api.meter.EMeter;

public class RT_Transaction {

	@Test
	public void test() {
		System.out.println("Start RT_Transaction");		
		EMeter.TRANSACTION.start("trn1");
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {	
			Assert.fail();
			e.printStackTrace();
		}
		System.out.println(EMeter.TRANSACTION.end("trn1"));
	}

}
