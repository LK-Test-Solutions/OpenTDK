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
		
		String rowStr = "a;b;;d";
		String[] rowArray_1 = rowStr.split(";");
		String[] rowArray_2 = new String[]{"a","b","","d"};
		System.out.println("rowArray_1 = "+ String.join(";", rowArray_1));
		System.out.println("rowArray_2 = "+ String.join(";", rowArray_2));
	}

}
