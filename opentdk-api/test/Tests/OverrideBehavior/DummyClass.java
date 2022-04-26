package Tests.OverrideBehavior;

// No chance to override or manipulate
public final class DummyClass {

	private final String test;
	
	private DummyClass(String test) {
		this.test = test;
	}
	
	public static DummyClass newDummyClass(String test) {
		return new DummyClass(test);
	}
	
	public final String getTest() {
		return test;
	}
}
