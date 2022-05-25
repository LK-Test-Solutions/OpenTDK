package Tests.OverrideBehavior;

public class Client {

	public static void main(String[] args) {
		DummyClass dummy = DummyClass.newDummyClass("Test");
		System.out.println(dummy.getTest());
	}

}
