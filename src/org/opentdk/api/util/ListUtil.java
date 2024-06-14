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

import java.util.*;
import java.util.stream.Collectors;

/**
 * Helper utility class for lists that contains only static methods to call them directly like:<br>
 * <br>
 * <code>ListUtil.asString(new ArrayList(...), ";");</code>
 * 
 * @author LK Test Solutions
 */
public class ListUtil {

	/**
	 * Transform a list of generic type to a separated string. If an object of the list cannot be
	 * transformed to a string, the hash code gets added to the returning string.
	 * 
	 * @param list      A list of generic type like integer or string.
	 * @param separator The separator like semicolon.
	 * @return A string containing all the values of the committed list separated by the committed
	 *         <code>separator</code>.
	 */
	public static String asString(List<?> list, String separator) {
		StringBuffer retVal = new StringBuffer();
		ListIterator<?> iter = list.listIterator();

		while (iter.hasNext()) {
			if (iter.nextIndex() > 0) {
				retVal.append(separator);
			}
			retVal.append(iter.next());
		}
		return retVal.toString();
	}

	/**
	 * Append an array of strings to one string with the defined separator.
	 * 
	 * @param values    The array content.
	 * @param separator The separator for the values.
	 * @return the concatenated string or an empty string if the values are empty.
	 */
	public static String asString(String[] values, String separator) {
		return asString(values, separator, false);
	}
	
	/**
	 * Append an array of strings to one string with the defined separator.
	 * 
	 * @param values    The array content.
	 * @param separator The separator for the values.
	 * @param strip		Removes starting and ending white space from each value if set to true.
	 * @return the concatenated string or an empty string if the values are empty.
	 */
	public static String asString(String[] values, String separator, boolean strip) {
		StringBuilder retVal = new StringBuilder();

		for (int i = 0; i < values.length; i++) {			
			retVal.append(StringUtils.strip(values[i]));
			if (i < values.length - 1) {
				retVal.append(separator);
			}
		}
		return retVal.toString();
	}

	/**
	 * Transform an array of objects to to list of type string by using the valueOf method of the String
	 * class.
	 * 
	 * @param arr The object array to transform
	 * @return An array of type string with all the elements of the objects array, represented as
	 *         string.
	 */
	public static String[] asStringArr(Object[] arr) {
		String[] retVal = new String[arr.length];
		for (int i = 0; i < arr.length; i++) {
			retVal[i] = String.valueOf(arr[i]);
		}
		return retVal;
	}
	
	/**
	 * Transform a list of primitive data types to a string array.
	 * 
	 * @param list List of primitive data types (wild card)
	 * @return An array of type sting with all the elements of the committed list
	 */
	public static String[] asStringArr(List<?> list) {
		return list.toArray(new String[list.size()]);
	}

	/**
	 * Transform a list of unknown to to list of type string by using the valueOf method of the String
	 * class.
	 * 
	 * @param list The object list to transform
	 * @return A list of type string with all the elements of the objects list, represented as string.
	 */
	public static List<String> asList(List<?> list) {
		List<String> retVal = new ArrayList<>();
		for (Object val : list) {
			retVal.add(String.valueOf(val));
		}
		return retVal;
	}

	/**
	 * Transform a input string to a list of string by splitting it with the committed separator.
	 * 
	 * @param text      The committed string to transform as list.
	 * @param separator The separator to search for.
	 * @return A list of type string with the split values or null if the split is not possible because
	 *         the string does not contain the separator at least one time.
	 */
	public static List<String> asList(String text, String separator) {
		List<String> retVal = null;
		String[] temp = text.split(separator);

		if (temp != null) {
			retVal = new ArrayList<>(Arrays.asList(temp));
		}
		return retVal;
	}

	/**
	 * Add all elements of a string array to a string list return it.
	 * 
	 * @param arr The input string array.
	 * @return The resulting list of type string.
	 */
	public static List<String> asList(String[] arr) {
		List<String> retVal = new ArrayList<>();

		for (int i = 0; i < arr.length; i++) {
			retVal.add(arr[i]);
		}
		return retVal;
	}

	/**
	 * Remove all elements from a list that contain only spaces.
	 * 
	 * @param list A list of type string.
	 * @return A list of type string without space elements.
	 */
	public static List<String> removeSpaceElements(List<String> list) {
		List<String> retList = new ArrayList<>();
		for (String item : list) {
			if (!item.trim().isEmpty()) {
				retList.add(item);
			}
		}
		return retList;
	}

	/**
	 * Get a list with all duplicate values of the committed collection.
	 * 
	 * This works like this: Add all elements of the committed collection to a set that cannot have
	 * duplicate values. When an item gets added the overridden add() method of the set gets called that
	 * adds the value to the returning list only if it is duplicate.
	 * 
	 * @param <T>  The placeholder for every type of collection.
	 * @param list The collection of any type.
	 * @return A list with all duplicate values.
	 */
	public static <T> List<T> getDuplicate(Collection<T> list) {
		List<T> retVal = new ArrayList<T>();
		Set<T> set = new HashSet<T>() {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean add(T e) {
				if (contains(e)) {
					retVal.add(e);
				}
				return super.add(e);
			}
		};

		for (T t : list) {
			set.add(t);
		}
		return retVal;
	}

	/**
	 * Get a list with all non duplicate values of the committed collection.
	 * 
	 * This works like this: Add all elements of the committed collection to a set that cannot have
	 * duplicate values. When an item gets added the overridden add() method of the set gets called that
	 * adds the value to the returning list.
	 * 
	 * @param <T>  The placeholder for every type of collection.
	 * @param list The collection of any type.
	 * @return A list with no duplicate values.
	 */
	public static <T> List<T> getNonDuplicate(Collection<T> list) {
		List<T> retVal = new ArrayList<T>();
		Set<T> set = new HashSet<T>() {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean add(T e) {
				if (!contains(e)) {
					retVal.add(e);
				}
				return super.add(e);
			}
		};

		for (T t : list) {
			set.add(t);
		}
		return retVal;
	}

	/**
	 * Check if a committed collection has duplicates.
	 * 
	 * @param <T>  The placeholder for every type of collection.
	 * @param list The collection of any type.
	 * @return True: There are duplicates. False: There are NO duplcates.
	 */
	public static <T> boolean hasDuplicate(Collection<T> list) {
		if (getDuplicate(list).isEmpty()) {
			return false;
		}
		return true;
	}

	/**
	 * Sort a list of any type by integer using the <code>Collections.sort()</code> method.
	 * 
	 * @param list A list of any type.
	 * @return The list of type integer sorted by integer from lowest to highest value.
	 */
	public static List<Integer> sortByInteger(List<?> list){
		List<Integer> intList = ListUtil.asIntList(list);
		List<Integer> result = intList.stream().sorted((o1, o2)->o1.
                compareTo(o2)).
                collect(Collectors.toList());
		return result;
	}

	/**
	 * Transform a list of any type to a list of integer values. If a list value cannot be casted to
	 * string to parse the value as integer afterwards, the value gets NaN(Not a number). If the list
	 * value could be casted to string but not parsed as integer, the value gets NaN as well.
	 * 
	 * @param list The input list of any type.
	 * @return A list of integer.
	 */
	public static List<Integer> asIntList(List<?> list) {
		List<Integer> retVal = new ArrayList<>();
		for (int i = 0; i < list.size(); i++) {
			int next;
			try {
				String nextAsString;
				try {
					nextAsString = (String) list.get(i);
				} catch (ClassCastException e) {
					nextAsString = "NaN";
				}
				next = Integer.parseInt(nextAsString);
			} catch (NumberFormatException e) {
				continue;
			}
			retVal.add(next);
		}
		return retVal;
	}

	/**
	 * Transform a list of any type to a list of string values. If a list value cannot be parsed as
	 * string, null gets added to the returning list.
	 * 
	 * @param list The input list of any type.
	 * @return A list of strings.
	 */
	public static List<String> asStringList(List<?> list) {
		List<String> retVal = new ArrayList<>();
		for (int i = 0; i < list.size(); i++) {
			String next;
			try {
				next = String.valueOf(list.get(i));
			} catch (NumberFormatException e) {
				next = null;
			}
			retVal.add(next);
		}
		return retVal;
	}
}
