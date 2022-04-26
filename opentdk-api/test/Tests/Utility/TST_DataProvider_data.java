package Tests.Utility;

import org.junit.Test;
import org.opentdk.api.util.DataProvider;

public class TST_DataProvider_data {

	@Test
	public void test() {

		// INT
		System.out.println("Random integer values");
		DataProvider provider1 = new DataProvider(100, 200);
		for (int i = 0; i < 10; i++) {
			System.out.println(provider1.data());
		}

		// LONG
		System.out.println();
		System.out.println("Random long values");
		DataProvider provider2 = new DataProvider(1L, 10L);
		for (int i = 0; i < 10; i++) {
			System.out.println(provider2.data());
		}

		// FLOAT
		System.out.println();
		System.out.println("Random float values");
		DataProvider provider3 = new DataProvider(0.0f, 10.0f);
		for (int i = 0; i < 10; i++) {
			System.out.println(provider3.data());
		}

		// DOUBLE
		System.out.println();
		System.out.println("Random double values");
		DataProvider provider4 = new DataProvider(0.0d, 10.0d);
		for (int i = 0; i < 10; i++) {
			System.out.println(provider4.data());
		}
		
		// NOT ALLOWED
		// DataProvider provider5 = new DataProvider(0.0d, 10);
	}

}
