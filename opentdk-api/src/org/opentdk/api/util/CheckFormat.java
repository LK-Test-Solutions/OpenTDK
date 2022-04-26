package org.opentdk.api.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Deprecated
public class CheckFormat {

	/**
	 * Checks if the given value matches a defined pattern and/or if the value is (not) <code>null</code>.<br>
	 *
	 * @param value the value to check
	 * @param check one of the check pattern constants, defined in the <code>ECheck</code> enum<br>
	 * @return <code>true</code> if the value correlates the pattern and/or if the value is <strong>not</strong> <code>null</code>, otherwise it returns
	 *         <code>false</code>
	 *
	 */
	@Deprecated
	public static boolean check(String value, EFormat check) {
		boolean returnCode = false;
		if (value != null) {
			if (check == EFormat.NOT_NULL) {
				return true;
			} else {
				if ((value.length() == check.getDateFormat().length()) || (check.getDateFormat().length() < 0)) {
					returnCode = matchPattern(check.getFormatPattern(), value);
				}
			}
		}
		return returnCode;
	}

	/**
	 * Checks, if the given string matches to a date format.
	 *
	 * @param srcDate String with the date value.
	 * @return boolean true = is a date format or false = is not a date format
	 */
	@Deprecated
	public static boolean checkDate(String srcDate) {
		boolean isDate = false;
		for (EFormat f : EFormat.values()) {
			if (f.name().toUpperCase().startsWith("DATE")) {
				if (f.getDateFormat().length() == srcDate.length()) {
					if (srcDate.matches(f.getFormatPattern())) {
						isDate = true;
						break;
					}
				}
			}
		}
		return isDate;
	}

	/**
	 * Checks if a value contains a pattern
	 *
	 * @param inPattern the pattern to search for in a String value
	 * @param value     the String value to search in
	 *
	 * @return <tt>true</tt> if the pattern is found in value, otherwise it returns <tt>false</tt>
	 */
	@Deprecated
	private static boolean matchPattern(String inPattern, String value) {
		Pattern chkPattern = Pattern.compile(inPattern);
		Matcher mIn = chkPattern.matcher(value);
		if (mIn.find()) {
			return true;
		} else {
			return false;
		}

	}
}
