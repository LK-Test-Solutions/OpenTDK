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

import org.apache.commons.lang3.StringUtils;

import java.nio.charset.Charset;

/**
 * Helper class with string methods for direct use in a static way.
 * 
 * @author LK Test Solutions
 */
public class StringUtil {

	/**
	 * @param toConvert Valid String object.
	 * @return The UNICODE transformation or null, if the input string is null, empty or too large.
	 */
	public static String toUnicode(String toConvert) {
		if(StringUtils.isBlank(toConvert) || toConvert.length() > Short.MAX_VALUE) {
			return null;
		}
		return new String(toConvert.getBytes(), Charset.defaultCharset());
	}

	/**
	 * @param inStr Valid String object.
	 * @return The regular expression transformation or null, if the input string is null, empty or too large.
	 */
	public static String stringToRegEx(String inStr) {
		if(StringUtils.isBlank(inStr) || inStr.length() > Short.MAX_VALUE) {
			return null;
		}		
		String regexStr = inStr.replace("\"", "\\\"");
		regexStr = regexStr.replaceAll("\\[", "\\\\[");
		regexStr = regexStr.replaceAll("\\]", "\\\\]");
		regexStr = regexStr.replaceAll("\\.", "\\\\.");
		regexStr = regexStr.replaceAll("\\*", "\\\\*");
		regexStr = regexStr.replaceAll("\\(", "\\\\(");
		regexStr = regexStr.replaceAll("\\)", "\\\\)");
		return regexStr;
	}

	/**
	 * @param inStr Valid String object.
	 * @return The literal expression transformation or null, if the input string is null, empty or too large.
	 */
	public static String regExToString(String inStr) {
		if(StringUtils.isBlank(inStr) || inStr.length() > Short.MAX_VALUE) {
			return null;
		}	
		String normalStr = inStr.replace("\\\"", "\"");
		normalStr = normalStr.replace("\\[", "[");
		normalStr = normalStr.replace("\\]", "]");
		normalStr = normalStr.replace("\\.", ".");
		normalStr = normalStr.replace("\\*", "*");
		normalStr = normalStr.replace("\\(", ")");
		normalStr = normalStr.replace("\\)", ")");
		return normalStr;
	}

	/**
	 * @param regex Valid String object.
	 * @return A string with all double back slashes replaced by single ones or null, if the input string is null, empty or too large.
	 */
	public static String replaceDoubleBackslashes(String regex) {
		if(StringUtils.isBlank(regex) || regex.length() > Short.MAX_VALUE) {
			return null;
		}	
		return regex.replaceAll("\\\\\\\\", "\\\\");
	}

	/**
	 * @param inStr Valid String object.
	 * @return A string with all enclosing quotes removed or null, if the input string is null, empty or too large.
	 */
	public static String removeEnclosingQuotes(String inStr) {
		if(StringUtils.isBlank(inStr) || inStr.length() > Short.MAX_VALUE) {
			return null;
		}
		String clearedString = inStr.replaceAll("\\A[ ]*\"", ""); // remove opening quotes
		clearedString = clearedString.replaceAll("\"[ ]*\\Z", ""); // remove closing quotes
		return clearedString;
	}

}
