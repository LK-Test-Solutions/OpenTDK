package Tests.Logging;

import org.junit.Test;

public class TST_printException {

	@Test
	public void test() {
		RuntimeException e = new RuntimeException("Dummy Exception");
		
		System.out.println(e.getMessage());
		for(StackTraceElement element : e.getStackTrace()) {
			System.out.println(element.toString());
		}
	}

}
