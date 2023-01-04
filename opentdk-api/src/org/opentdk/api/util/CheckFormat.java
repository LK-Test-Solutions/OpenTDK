/* 
 * BSD 2-Clause License
 * 
 * Copyright (c) 2022, LK Test Solutions GmbH
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. 
 */
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
