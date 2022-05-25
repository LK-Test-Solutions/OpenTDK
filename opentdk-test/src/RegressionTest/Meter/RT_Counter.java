package RegressionTest.Meter;

import org.opentdk.api.meter.EMeter;

import RegressionTest.BaseRegression;

public class RT_Counter extends BaseRegression {

	public static void main(String[] args) {
		new RT_Counter();
	}
	
	@Override
	public void runTest() {
		System.out.println("Start RT_Counter");
		System.out.println(EMeter.COUNTER.increase("Counter1")); // 1
		System.out.println(EMeter.COUNTER.increase("Counter1")); // 2
		System.out.println(EMeter.COUNTER.increase("Counter1", 5)); // 7
		int count1 = EMeter.COUNTER.decrease("Counter1");
		System.out.println(count1); // 6
		testResult(count1, "Count 1", 6);

		System.out.println(EMeter.COUNTER.increase("Counter2", 10, 1)); // 11
		System.out.println(EMeter.COUNTER.increase("Counter2")); // 12
		System.out.println(EMeter.COUNTER.increase("Counter2", 3)); // 15
		int count2 = EMeter.COUNTER.decrease("Counter2", 2);
		System.out.println(count2); // 13
		testResult(count2, "Count 2", 13);

		System.out.println(EMeter.COUNTER.decrease("Counter3", 2)); // -2
		int count3 = EMeter.COUNTER.decrease("Counter3");
		System.out.println(count3); // -3
		testResult(count3, "Count 3", -3);

		int count4 = EMeter.COUNTER.decrease("Counter4", 100, 2);
		System.out.println(count4); // 98
		testResult(count4, "Count 4", 98);
	}

}
