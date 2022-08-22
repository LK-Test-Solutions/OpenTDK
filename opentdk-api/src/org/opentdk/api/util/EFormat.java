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

/**
 * Enumeration of all formats (date, time, time stamp) that the {@link org.opentdk.api.util.DateUtil} class uses.
 * 
 * <br>
 * <br>
 * How to define a new format:<br>
 * <br>
 * 
 * 1. Create a new enumeration. Default usage is DATE_X for dates, TIME_X for times and TIMSTAMP_X for time stamps.<br>
 * 2. Add the date format to the enumeration e.g. yyyyMMdd.<br>
 * 3.<br>
 * Add the pattern to the enumeration e.g. [0-9]{4}[0-1][0-9][0-3][0-9]. This gets used to search for a format out of a given search string.<br>
 * 4. Add a delimiter e.g. the dash in yyyy-MM-dd or an empty string for yyyyMMdd.
 * 
 * @author LK Test Solutions
 *
 */
public enum EFormat {

	/** Used if no format could be retrieved. */
	NONE("", "", ""),

	/** Used if no format could be retrieved. */
	NOT_NULL("", "", ""),

	/** Defines the format yyyyMMdd. */
	DATE_1("yyyyMMdd", "[0-9]{4}[0-1][0-9][0-3][0-9]", " "),

	/** Defines the format yyyy-MM-dd. */
	DATE_2("yyyy-MM-dd", "[0-9]{4}-[0-1][0-9]-[0-3][0-9]", "-"),

	/** Defines the format dd-MM-yyyy. */
	DATE_3("dd-MM-yyyy", "[0-3][0-9]-[0-1][0-9]-[0-9]{4}", "-"),

	/** Defines the format dd.MM.yyyy. */
	DATE_4("dd.MM.yyyy", "[0-3][0-9].[0-1][0-9].[0-9]{4}", "."),

	/** Defines the format yyyy.MM.dd. */
	DATE_5("yyyy.MM.dd", "[0-9]{4}.[0-1][0-9].[0-3][0-9]", "."),

	/** Defines the format MM/dd/yyyy. */
	DATE_6("MM/dd/yyyy", "[0-1][0-9]/[0-3][0-9]/[0-9]{4}", "/"),

	/** Defines the format yyyy/MM/dd. */
	DATE_7("yyyy/MM/dd", "[0-9]{4}/[0-1][0-9]/[0-3][0-9]", "/"),

	/** Defines the format ddMM. */
	DATE_8("ddMM", "[1-3][0-9][0-1][0-9]", ""),

	/** Defines the format HH.mm.ss. */
	TIME_1("HH.mm.ss", "[0-9]{2}.[0-9]{2}.[0-9]{2}", "."),

	/** Defines the format HH:mm:ss. */
	TIME_2("HH:mm:ss", "[0-9]{2}.[0-9]{2}.[0-9]{2}", ":"),

	/** Defines the format HH.mm.ss.SSSSSS. */
	TIME_3("HH.mm.ss.SSSSSS", "[0-9]{2}.[0-9]{2}.[0-9]{2}.[0-9]{6}", "."),

	/** Defines the format HH:mm:ss.SSSSSS. */
	TIME_4("HH:mm:ss.SSSSSS", "[0-9]{2}.[0-9]{2}.[0-9]{2}.[0-9]{6}", "."),

	/** Defines the format HHmmss. */
	TIME_5("HHmmss", "[0-9]{2}[0-9]{2}[0-9]{2}", ""),

	/** Defines the format HH:mm:ss,SSS. */
	TIME_6("HH:mm:ss,SSS", "[0-9]{2}:[0-9]{2}:[0-9]{2},[0-9]{3}", ":", ","),

	/** Defines the format SSS. */
	TIME_7("SSS", "[0-9]{3}", ""),

	/** Defines the format HH:mm:ss.SSS. */
	TIME_8("HH:mm:ss.SSS", "[0-9]{2}:[0-9]{2}:[0-9]{2}.[0-9]{3}", " "),

	/** Defines the format HH:mm:ss.SSSSSS. */
	TIME_9("HH:mm:ss.SSSSSS", "[0-9]{2}:[0-9]{2}:[0-9]{2}.[0-9]{6}", " "),

	/** Defines the format HH:mm:ss.SSSSSSSSS. */
	TIME_10("HH:mm:ss.SSSSSSSSS", "[0-9]{2}:[0-9]{2}:[0-9]{2}.[0-9]{9}", " "),

	/** Defines the format yyyy-MM-dd-HH.mm.ss.SSSSSS. */
	TIMESTAMP_1(DATE_2, TIME_3, "-"),
	
	/** Defines the format yyyy-MM-dd-HH:mm:ss.SSSSSS. */
	TIMESTAMP_2(DATE_2, TIME_4, "-"),
	
	/** Defines the format yyyy-MM-dd HH:mm:ss.SSSSSS. */
	TIMESTAMP_3(DATE_2, TIME_4, " "),
	
	/** Defines the format yyyy-MM-dd-HH:mm:ss,SSS. */
	TIMESTAMP_4(DATE_2, TIME_6, "-"),

	/** Defines the format yyyy-MM-dd HH:mm:ss.SSS. */
	TIMESTAMP_5(DATE_2, TIME_8, " "),
	
	/** Defines the format yyyyMMdd HH:mm:ss. */
	TIMESTAMP_6(DATE_1, TIME_2, " "),
	
	/** Defines the format yyyyMMdd-HHmmss. */
	TIMESTAMP_7(DATE_1, TIME_5, "-"),

	/** Defines the format yyyy.MM.dd HH:mm:ss. */
	TIMESTAMP_8(DATE_5, TIME_2, " "),
	
	/** Defines the format yyyy.MM.dd.HH:mm:ss. */
	TIMESTAMP_9(DATE_5, TIME_2, "."),
	
	/** Defines the format yyyy.MM.dd.HH:mm:ss.SSS. */
	TIMESTAMP_10(DATE_5, TIME_8, "."),

	/** Defines the format yyyy.MM.dd HH:mm:ss.SSSSSSSSS. */
	TIMESTAMP_11(DATE_5, TIME_10, " ");

		
	/**
	 * The format string of the date or time to the <code>EFormat</code>. E.g. yyyyMMdd.
	 */
	private final String dateFormat;

	/**
	 * The pattern string of the date or time to the <code>EFormat</code>. E.g. [0-9]{4}[0-1][0-9][0-3][0-9].
	 */
	private final String formatPattern;

	/**
	 * Delimiter for the different parts in the date e.g. the dash or dot.
	 */
	private final String dateDelimiter;

	/**
	 * Delimiter for the date and time string. Used to build time stamps.
	 */
	private final String dateTimeDelimiter;

	/**
	 * Delimiter for the different parts in the time e.g. the dash or dot.
	 */
	private final String timeDelimiter;

	/**
	 * The delimiter that separates the milliseconds, microseconds and nanoseconds from the rest of the date or time string e.g. comma or dot.
	 */
	private final String decimalDelimiter;

	/**
	 * @param format    {@link #dateFormat}
	 * @param pattern   {@link #formatPattern}
	 * @param delimiter {@link #dateDelimiter}
	 */
	private EFormat(String format, String pattern, String delimiter) {
		this(format, pattern, delimiter, "");
	}

	/**
	 * @param format       {@link #dateFormat}
	 * @param pattern      {@link #formatPattern}
	 * @param delimiter    {@link #dateDelimiter}
	 * @param decDelimiter {@link #decimalDelimiter}
	 */
	private EFormat(String format, String pattern, String delimiter, String decDelimiter) {
		this(format, pattern, delimiter, decDelimiter, "", "");
	}

	/**
	 * @param format            {@link #dateFormat}
	 * @param pattern           {@link #formatPattern}
	 * @param dateDelimiter     {@link #dateDelimiter}
	 * @param dateTimeDelimiter {@link #dateTimeDelimiter}
	 * @param timeDelimiter     {@link #timeDelimiter}
	 * @param decDelimiter      {@link #decimalDelimiter}
	 */
	private EFormat(String format, String pattern, String dateDelimiter, String dateTimeDelimiter, String timeDelimiter, String decDelimiter) {
		this.dateFormat = format;
		this.formatPattern = pattern;
		this.dateDelimiter = dateDelimiter;
		this.dateTimeDelimiter = dateTimeDelimiter;
		this.timeDelimiter = timeDelimiter;
		this.decimalDelimiter = decDelimiter;
	}

	/**
	 * Used to combine to formats e.g. a time stamp out of a date and a time format.
	 * 
	 * @param date      The date format.
	 * @param time      The time format.
	 * @param separator The delimiter of the date and time.
	 */
	private EFormat(EFormat date, EFormat time, String separator) {
		this.dateFormat = date.getDateFormat() + separator + time.getDateFormat();
		this.formatPattern = date.getFormatPattern() + separator + time.getFormatPattern();
		this.dateDelimiter = date.getDateDelimiter();
		this.dateTimeDelimiter = separator;
		this.timeDelimiter = time.getTimeDelimiter();
		this.decimalDelimiter = time.getDecimalDelimiter();
	}

	/**
	 * @param inDate the date to retrieve the year from.
	 * @return year part of the date e.g. "2021" in 2021.01.02.
	 */
	public int getYear(String inDate) {
		return getPart(inDate, "y");
	}

	/**
	 * @param inDate the date to retrieve the month from.
	 * @return month part of the date e.g. "01" in 2021.01.02.
	 */
	public int getMonth(String inDate) {
		return getPart(inDate, "M");
	}

	/**
	 * @param inDate the date to retrieve the day from.
	 * @return day part of the date e.g. "02" in 2021.01.02.
	 */
	public int getDay(String inDate) {
		return getPart(inDate, "d");
	}

	/**
	 * @param inDate the date to retrieve the hour from.
	 * @return hour part of the date or time e.g. "12" in 2021.01.02 12:15:00.123456789.
	 */
	public int getHour(String inDate) {
		return getPart(inDate, "H");
	}

	/**
	 * @param inDate the date to retrieve the minute from.
	 * @return minute part of the date or time e.g. "15" in 2021.01.02 12:15:00.123456789.
	 */
	public int getMinute(String inDate) {
		return getPart(inDate, "m");
	}

	/**
	 * @param inDate the date to retrieve the second from.
	 * @return second part of the date or time e.g. "00" in 2021.01.02 12:15:00.123456789.
	 */
	public int getSecond(String inDate) {
		return getPart(inDate, "s");
	}

	/**
	 * @param inDate the date to retrieve the millisecond from.
	 * @return millisecond part of the date or time e.g. "123" in 2021.01.02 12:15:00.123456789.
	 */
	public int getMilliSecond(String inDate) {
		return getSecondPart(inDate, 0, 3);
	}

	/**
	 * @param inDate the date to retrieve the microsecond from.
	 * @return microsecond part of the date or time e.g. "456" in 2021.01.02 12:15:00.123456789.
	 */
	public int getMicroSecond(String inDate) {
		return getSecondPart(inDate, 3, 6);
	}

	/**
	 * @param inDate the date to retrieve the nanosecond from.
	 * @return nanosecond part of the date or time e.g. "789" in 2021.01.02 12:15:00.123456789.
	 */
	public int getNanoSecond(String inDate) {
		return getSecondPart(inDate, 6, 9);
	}

	private final int getSecondPart(String inDate, int from, int to) {
		int start = dateFormat.indexOf("S");
		int end = dateFormat.lastIndexOf("S") + 1;
		if (start == -1 || end == -1) {
			return 0;
		}
		String part = "";
		try {
			part = inDate.substring(start, end);
			part = part.substring(from, to);
		} catch (IndexOutOfBoundsException e) {
			return 0;
		}
		try {
			if (part.startsWith("0")) {
				part = part.substring(1);
			}
			if (part.startsWith("00")) {
				part = part.substring(2);
			}
			return Integer.parseInt(part);
		} catch (NumberFormatException e) {
			return 0;
		}
	}

	private final int getPart(String inDate, String type) {
		int start = dateFormat.indexOf(type);
		int end = dateFormat.lastIndexOf(type) + 1;
		if (start == -1 || end == -1) {
			return -1;
		}
		String part = inDate.substring(start, end);
		if (part.startsWith("0")) {
			part = part.substring(1);
		}
		try {
			return Integer.parseInt(part);
		} catch (NumberFormatException e) {
			return -1;
		}
	}

	/**
	 * @param srcDate The date string to retrieve a format for.
	 * @return The detected format or NONE if there is no format for the committed date.
	 */
	public static final EFormat getDateEFormat(String srcDate) {
		for (EFormat f : EFormat.values()) {
			if (f != EFormat.NONE) {
				if (f.getDateFormat().length() == srcDate.length()) {
					if (srcDate.matches(f.getFormatPattern())) {
						return f;
					}
				}
			}
		}
		return EFormat.NONE;
	}

	/**
	 * @return {@link #dateFormat}
	 */
	public String getDateFormat() {
		return dateFormat;
	}

	/**
	 * @return {@link #formatPattern}
	 */
	public String getFormatPattern() {
		return formatPattern;
	}

	/**
	 * @return {@link #dateDelimiter}
	 */
	public String getDateDelimiter() {
		return dateDelimiter;
	}

	/**
	 * @return {@link #dateTimeDelimiter}
	 */
	public String getDateTimeDelimiter() {
		return dateTimeDelimiter;
	}

	/**
	 * @return {@link #timeDelimiter}
	 */
	public String getTimeDelimiter() {
		return timeDelimiter;
	}

	/**
	 * @return {@link #decimalDelimiter}
	 */
	public String getDecimalDelimiter() {
		return decimalDelimiter;
	}
}
