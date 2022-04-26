package Tests.StringUtility;

import org.junit.Test;
import org.opentdk.api.util.StringUtil;

public class TST_StringUtility_regex {

	@Test
	public void test() {
		String toConvert = "Test\u2736";
		System.out.println(StringUtil.toUnicode(toConvert));
		
		String literalString = "Test*";
		System.out.println(StringUtil.stringToRegEx(literalString));
		
		String regex = "Test\\";
		System.out.println(StringUtil.regExToString(regex));
		
		// Correct ? 
		String withDoubleBackSlash = "Test\\\\\\\\"; // Gets reduced to Test\\\\ when initializing the string, so Test\\ remains after the StringUtil method
		System.out.println(StringUtil.replaceDoubleBackslashes(withDoubleBackSlash));
		
		String withQuotes = "\"Test\"";
		System.out.println(StringUtil.removeEnclosingQuotes(withQuotes));
		
	}

}
