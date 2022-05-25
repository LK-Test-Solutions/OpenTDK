package Tests.StringUtility;

public class TST_StringUtility_split {

	public static void main(String[] args) {
		String test = "test";
		System.out.println(test.split("-")[0]);
		System.out.println(test.length());
		
		System.out.println(Short.MAX_VALUE);
		
		String test2 = "test.de";
		if(test2.contains(".")) {
			System.out.println(test2);
		}
	}

}
