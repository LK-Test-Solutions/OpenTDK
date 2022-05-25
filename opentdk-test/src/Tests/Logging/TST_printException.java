package Tests.Logging;

public class TST_printException {

	public static void main(String[] args) {
		RuntimeException e = new RuntimeException("Dummy Exception");
		
		System.out.println(e.getMessage());
		for(StackTraceElement element : e.getStackTrace()) {
			System.out.println(element.toString());
		}
	}

}
