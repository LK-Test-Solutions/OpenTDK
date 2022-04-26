package Tests.OverrideBehavior;

import org.junit.Test;

public class Client {

	@Test
	public void test() {
		DummyClass dummy = DummyClass.newDummyClass("Test");
		System.out.println(dummy.getTest());
	}

}
