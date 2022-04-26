package Tests.StringUtility;


import org.junit.Test;

public class TST_StringUtility_split {

	@Test
	public void test() {
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
