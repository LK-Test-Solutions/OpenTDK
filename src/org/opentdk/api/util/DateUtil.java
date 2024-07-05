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

import org.opentdk.api.logger.MLogger;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.util.Random;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class with static utility methods to work with date, time and time stamp formats using the {@link java.time} package.
 * 
 * @author LK Test Solutions
 *
 */
public class DateUtil {

	/**
	 * The {@link java.time.ZoneId} that gets used in the date and time utility functions of the <code>DateUtil</code> class. The default is
	 * <code>ZoneId.systemDefault()</code> which is 'UTC+02:00' for Berlin (summer time).
	 */
	private static ZoneId zoneId = ZoneId.systemDefault();

	/**
	 * Set a new time zone by committing a string with the UTC value e.g. 'UTC+01:00'.
	 * 
	 * @param zone {@link #zoneId}
	 */
	public static void setZoneId(String zone) {
		zoneId = ZoneId.of(zone);
	}

	/**
	 * Compares to strings as date, time or time stamp.
	 * 
	 * @param dateTime        first comparison string.
	 * @param compareDateTime second comparison string.
	 * @return 0 = both instants are equal; -1 = first instant is before second; 1 = first instant is after second.
	 */
	public static int compare(String dateTime, String compareDateTime) {
		Instant first = getInstantInstance(dateTime);
		Instant second = getInstantInstance(compareDateTime);

		int ret = -999;
		if (first != null && second != null) {
			if (first.isBefore(second)) {
				ret = -1;
			} else if (first.isAfter(second)) {
				ret = 1;
			} else {
				ret = 0;
			}
		}
		return ret;
	}

	/**
	 * Get the day difference between two instants as integer value. Turn of the year will be taken into account as well. E.g. getDayDiff("20193112",
	 * "20200101") would return 1.
	 * 
	 * @param dateTime        The first comparison input string.
	 * @param compareDateTime The second comparison string.
	 * @param type            the unit that will be compared e.g. days or seconds.
	 * @return A positive integer value, or 0 if the instants are similar and -1 when no instant format could be detected.
	 */
	public static int diff(String dateTime, String compareDateTime, ChronoUnit type) {
		Instant first = getInstantInstance(dateTime);
		Instant second = getInstantInstance(compareDateTime);

		int ret = -1;
		if (first != null && second != null) {
			ret = (int) Math.abs(type.between(first, second));
		}
		return ret;
	}

	/**
	 * Retrieves the current date, time or time stamp.
	 * 
	 * @param format The preferred date/time format e.g. yyyyMMdd.
	 * @return The current instant in the committed format as string.
	 */
	public static String get(String format) {
		ZonedDateTime instant = getInstantInstance().atZone(zoneId);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
		return formatter.format(instant);
	}

	/**
	 * Retrieves the date, time or time stamp depending on the input instant.
	 * 
	 * @param dateTime A valid date, time or time stamp string in one of the formats defined in <code>EFormat</code>.
	 * @param format   The preferred date/time format e.g. yyyyMMdd.
	 * @return The formatted/transformed string or an empty string if no instant could be retrieved.
	 */
	public static String get(String dateTime, String format) {
		ZonedDateTime instant = getInstantInstance(dateTime).atZone(zoneId);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
		return formatter.format(instant);
	}

	/**
	 * Retrieves the current date.
	 * 
	 * @param millis The milliseconds from the epoch of 1970-01-01T00:00:00Z.
	 * @param format The preferred date/time format e.g. yyyyMMdd.
	 * @return The detected date as string in the default time zone.
	 */
	public static String get(long millis, String format) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
		return formatter.format(ZonedDateTime.ofInstant(Instant.ofEpochMilli(millis), zoneId));
	}

	/**
	 * Retrieves the current date, time or time stamp shifted to the past or future.
	 * 
	 * @param diff   The amount to travel. This depends on the committed unit.
	 * @param format The preferred date/time format e.g. yyyyMMdd.
	 * @param unit   The travel unit e.g. <code>ChronoUnit.DAYS</code>.
	 * @return The resulting date string in the committed format.
	 */
	public static String get(int diff, String format, ChronoUnit unit) {
		ZonedDateTime instant = getInstantInstance().atZone(zoneId);
		if (diff > 0) {
			instant = instant.plus(diff, unit);
		} else if (diff < 0) {
			instant = instant.minus(-diff, unit);
		}
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
		return formatter.format(instant);
	}

	/**
	 * Retrieves the committed date, time or time stamp shifted to the past or future.
	 * 
	 * @param dateTime A valid date, time or time stamp string in one of the formats defined in <code>EFormat</code>.
	 * @param diff     The amount to travel. This depends on the committed unit.
	 * @param format   The preferred date/time format e.g. yyyyMMdd.
	 * @param unit     The travel unit e.g. <code>ChronoUnit.DAYS</code>.
	 * @return The resulting date/time string in the committed format.
	 */
	public static String get(String dateTime, int diff, String format, ChronoUnit unit) {
		Instant instant = getInstantInstance(dateTime);
		if (instant == null) {
			MLogger.getInstance().log(Level.WARNING, "DateUtil.getDateTravel ==> Could not create an instant for the committed date: " + dateTime);
			return "";
		}
		ZonedDateTime zonedInstant = instant.atZone(zoneId);
		if (diff > 0) {
			zonedInstant = zonedInstant.plus(diff, unit);
		} else if (diff < 0) {
			zonedInstant = zonedInstant.minus(-diff, unit);
		}
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
		return formatter.format(zonedInstant);
	}

	/**
	 * Retrieves the first date or time or time stamp depending on the committed type.
	 * 
	 * @param type   The field to use e.g. <code>ChronoField.DAY_OF_WEEK</code> gets the first day of the week.
	 * @param format The preferred date/time format e.g. yyyyMMdd.
	 * @return The resulting date/time string in the committed format.
	 */
	public static String getFirstOf(ChronoField type, String format) {
		ZonedDateTime instant = getInstantInstance().atZone(zoneId);
		instant = instant.with(type, instant.range(type).getMinimum());
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
		return formatter.format(instant);
	}

	/**
	 * Retrieves the first date or time or time stamp depending on the committed type.
	 * 
	 * @param dateTime A valid date, time or time stamp string in one of the formats defined in <code>EFormat</code>.
	 * @param type     The field to use e.g. <code>ChronoField.DAY_OF_WEEK</code> gets the first day of the week.
	 * @param format   The preferred date/time format e.g. yyyyMMdd.
	 * @return The resulting date/time string in the committed format.
	 */
	public static String getFirstOf(String dateTime, ChronoField type, String format) {
		ZonedDateTime instant = getInstantInstance(dateTime).atZone(zoneId);
		instant = instant.with(type, instant.range(type).getMinimum());
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
		return formatter.format(instant);
	}

	/**
	 * Retrieves the first date or time or time stamp depending on the committed type with the possibility to travel to the past or future.
	 * 
	 * @param type   The field to use e.g. <code>ChronoField.DAY_OF_WEEK</code> gets the first day of the week.
	 * @param format The preferred date/time format e.g. yyyyMMdd.
	 * @param unit   The travel unit e.g. <code>ChronoUnit.DAYS</code>.
	 * @param diff   The amount to travel. This depends on the committed unit.
	 * @return The resulting date/time string in the committed format.
	 */
	public static String getFirstOf(ChronoField type, String format, ChronoUnit unit, int diff) {
		return getFirstOf(get(diff, format, unit), type, format);
	}

	/**
	 * Retrieves the first date or time or time stamp depending on the committed type with the possibility to travel to the past or future.
	 * 
	 * @param dateTime A valid date, time or time stamp string in one of the formats defined in <code>EFormat</code>.
	 * @param type     The field to use e.g. <code>ChronoField.DAY_OF_WEEK</code> gets the first day of the week.
	 * @param format   The preferred date/time format e.g. yyyyMMdd.
	 * @param unit     The travel unit e.g. <code>ChronoUnit.DAYS</code>.
	 * @param diff     The amount to travel. This depends on the committed unit.
	 * @return The resulting date/time string in the committed format.
	 */
	public static String getFirstOf(String dateTime, ChronoField type, String format, ChronoUnit unit, int diff) {
		return getFirstOf(get(dateTime, diff, format, unit), type, format);
	}

	/**
	 * Retrieves the last date or time or time stamp depending on the committed type.
	 * 
	 * @param type   The field to use e.g. <code>ChronoField.DAY_OF_WEEK</code> gets the first day of the week.
	 * @param format The preferred date/time format e.g. yyyyMMdd.
	 * @return The resulting date/time string in the committed format.
	 */
	public static String getLastOf(ChronoField type, String format) {
		ZonedDateTime instant = getInstantInstance().atZone(zoneId);
		instant = instant.with(type, instant.range(type).getMaximum());
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
		return formatter.format(instant);
	}

	/**
	 * Retrieves the last date or time or time stamp depending on the committed type.
	 * 
	 * @param dateTime A valid date, time or time stamp string in one of the formats defined in <code>EFormat</code>.
	 * @param type     The field to use e.g. <code>ChronoField.DAY_OF_WEEK</code> gets the first day of the week.
	 * @param format  The preferred date/time format e.g. yyyyMMdd.
	 * @return The resulting date/time string in the committed format.
	 */
	public static String getLastOf(String dateTime, ChronoField type, String format) {
		ZonedDateTime instant = getInstantInstance(dateTime).atZone(zoneId);
		instant = instant.with(type, instant.range(type).getMaximum());
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
		return formatter.format(instant);
	}

	/**
	 * Retrieves the last date or time or time stamp depending on the committed type with the possibility to travel to the past or future.
	 * 
	 * @param type   The field to use e.g. <code>ChronoField.DAY_OF_WEEK</code> gets the first day of the week.
	 * @param format The preferred date/time format e.g. yyyyMMdd.
	 * @param unit   The travel unit e.g. <code>ChronoUnit.DAYS</code>.
	 * @param diff   The amount to travel. This depends on the committed unit.
	 * @return The resulting date/time string in the committed format.
	 */
	public static String getLastOf(ChronoField type, String format, ChronoUnit unit, int diff) {
		return getLastOf(get(diff, format, unit), type, format);
	}

	/**
	 * Retrieves the last date or time or time stamp depending on the committed type with the possibility to travel to the past or future.
	 * 
	 * @param dateTime A valid date, time or time stamp string in one of the formats defined in <code>EFormat</code>.
	 * @param type     The field to use e.g. <code>ChronoField.DAY_OF_WEEK</code> gets the first day of the week.
	 * @param format   The preferred date/time format e.g. yyyyMMdd.
	 * @param unit     The travel unit e.g. <code>ChronoUnit.DAYS</code>.
	 * @param diff     The amount to travel. This depends on the committed unit.
	 * @return The resulting date/time string in the committed format.
	 */
	public static String getLastOf(String dateTime, ChronoField type, String format, ChronoUnit unit, int diff) {
		return getLastOf(get(dateTime, diff, format, unit), type, format);
	}

	/**
	 * Retrieves the number of the part of the committed date, time or time stamp depending on the committed type.
	 * 
	 * @param dateTime A valid date, time or time stamp string in one of the formats defined in <code>EFormat</code>.
	 * @param type     The field to use e.g. <code>ChronoField.DAY_OF_WEEK</code> gets the first day of the week.
	 * @return The number as integer value.
	 */
	public static int getNumber(String dateTime, ChronoField type) {
		ZonedDateTime instant = getInstantInstance(dateTime).atZone(zoneId);
		return instant.get(type);
	}

	/**
	 * This method gets a date string out of the committed string (exact match or containment).
	 * 
	 * @param inStr   The input string that is a date string or contains one.
	 * @param pFormat One of the date formats in {@link EFormat}.
	 * @return The detected date string or an empty string, if nothing could be found.
	 */
	public static String parse(String inStr, String pFormat) {
		String parsedDate = "";
		Pattern p = Pattern.compile(pFormat);
		Matcher mDate = p.matcher(inStr);
		if (mDate.find()) {
			parsedDate = inStr.substring(mDate.start(), mDate.end());
		}
		return parsedDate;
	}

	/**
	 * Method that gets the current milliseconds, counted from 1st Jan. 1970 and returns the length. The logarithm to the base 10 returns e.g. 3 when the
	 * value 1000 is passed, 4 when the value 10000 is passed etc. So one digit has to be added. Another example: The value log10(111) is a bit higher
	 * than 2 and the value log10(999) is a bit lower than 3. But the integer cast always returns 2 in this case (round down) and the result of this
	 * method is 3, which is correct for both numbers.
	 * 
	 * @return an integer value with the length of the milliseconds.
	 */
	public static int getLengthMilliseconds() {
		long millis = System.currentTimeMillis();
		return (int) (Math.log10(millis) + 1);
	}

	/**
	 * Gets the date from a given age. If the age is given as in interval separated with a minus, the function get a random date between the given ages.
	 * 
	 * @param age    the age to be found as a date. To give an interval of ages, the ages must be separated with a minus within the string.
	 * @param format one of the date formats in {@link EFormat}.
	 * @return the date of the given age or a random age between the given age interval.
	 */
	public static String getDateFromAge(String age, String format) {
		String outDate = "";
		Random random = new Random();
		String[] a_age = { age };
		if (age.contains("-")) {
			a_age = age.split("-");
		}
		switch (a_age.length) {
		case 1:
			outDate = "'" + get(-(Integer.parseInt(a_age[0]) * 365), format, ChronoUnit.DAYS) + "'";
			break;
		case 2:
			outDate = "'" + get(-(random.nextInt(Integer.parseInt(a_age[1]) * 365 - Integer.parseInt(a_age[0]) * 365) + Integer.parseInt(a_age[0]) * 365), format, ChronoUnit.DAYS) + "'";
			break;
		case 3:
			outDate = "'" + age + "'";
			break;
		}
		return outDate;
	}

	/**
	 * Gets an age in years from a given date.
	 * 
	 * @param date the date where the age is calculated from in one of the formats defined in {@link EFormat}.
	 * @return the age in years as integer value.
	 */
	public int getAgeFromDate(String date) {
		return diff(get(date, EFormat.getDateEFormat(date).getDateFormat()), get("yyyy-MM-dd"), ChronoUnit.YEARS);
	}

	/**
	 * Retrieves a <code>java.time.LocalDate</code> instance out a given date string. This is done by comparing the date string with all formats that are
	 * available in the <code>EFormat</code> enumeration.
	 * 
	 * @param inDate The date string in valid format <code>EFormat</code>.
	 * @return The date instance for further operations or null if no format matches to the committed date string.
	 */
	private static LocalDate getDateInstance(String inDate) {
		LocalDate ret = null;
		EFormat format = EFormat.getDateEFormat(inDate);
		if (format != EFormat.NONE) {
//			try {
				int year = format.getYear(inDate);
				int month = format.getMonth(inDate);
				int day = format.getDay(inDate);

				if (year == -1) {
					year = 0;
				}
				if (month != -1 && day != -1) {
					ret = LocalDate.of(year, month, day);
				}
//			} catch (DateTimeException e) {
//				MLogger.getInstance().log(Level.SEVERE, DateUtil.class.getName() + ".getDateInstance ==> Error while retrieving date: " + inDate);
//			}
		}
		return ret;
	}

	/**
	 * Retrieves a <code>java.time.LocalTime</code> instance out a given time string. This is done by comparing the time string with all formats that are
	 * available in the <code>EFormat</code> enumeration.
	 * 
	 * @param inTime The time string in valid format <code>EFormat</code>.
	 * @return The time instance for further operations or null if no format matches to the committed time string.
	 */
	private static LocalTime getTimeInstance(String inTime) {
		LocalTime ret = null;
		EFormat format = EFormat.getDateEFormat(inTime);
		if (format != EFormat.NONE) {
//			try {
				int hours = format.getHour(inTime);
				int minutes = format.getMinute(inTime);
				int seconds = format.getSecond(inTime);

				if (hours != -1 && minutes != -1 && seconds != -1) {
					int millis = format.getMilliSecond(inTime);
					int micros = format.getMicroSecond(inTime);
					int nanos = format.getNanoSecond(inTime);

					int rest = (millis * 1000 * 1000) + (micros * 1000) + nanos;
					ret = LocalTime.of(hours, minutes, seconds, rest);
				}
//			} catch (DateTimeException e) {
//				MLogger.getInstance().log(Level.SEVERE, DateUtil.class.getName() + ".getTimeInstance ==> Error while retrieving time: " + inTime);
//			}
		}
		return ret;
	}

	/**
	 * Retrieves a <code>java.time.LocalDateTime</code> instance out a given time stamp string. This is done by comparing the time stamp string with all
	 * formats that are available in the <code>EFormat</code> enumeration.
	 * 
	 * @param inTimestamp The time stamp string in valid format <code>EFormat</code>.
	 * @return The time stamp instance for further operations or null if no format matches to the committed time stamp string.
	 */
	private static LocalDateTime getTimestampInstance(String inTimestamp) {
		LocalDateTime ret = null;
		EFormat format = EFormat.getDateEFormat(inTimestamp);
		if (format != EFormat.NONE) {
//			try {
				int year = format.getYear(inTimestamp);
				int month = format.getMonth(inTimestamp);
				int day = format.getDay(inTimestamp);

				int hours = format.getHour(inTimestamp);
				int minutes = format.getMinute(inTimestamp);
				int seconds = format.getSecond(inTimestamp);

				if (year != -1 && month != -1 && day != -1 && hours != -1 && minutes != -1 && seconds != -1) {

					int millis = format.getMilliSecond(inTimestamp);
					int micros = format.getMicroSecond(inTimestamp);
					int nanos = format.getNanoSecond(inTimestamp);

					int rest = (millis * 1000 * 1000) + (micros * 1000) + nanos;
					ret = LocalDateTime.of(year, month, day, hours, minutes, seconds, rest);
				}
//			} catch (DateTimeException e) {
//				MLogger.getInstance().log(Level.SEVERE, DateUtil.class.getName() + ".getTimestampInstance ==> Error while retrieving timestamp: " + inTimestamp);
//			}
		}
		return ret;
	}

	/**
	 * Retrieves an {@link java.time.Instant} object to the current date, time or time stamp string. This represents an object on the time line and can be
	 * build out of a {@link java.time.LocalDate}, {@link java.time.LocalTime} or {@link java.time.LocalDateTime} by using the
	 * {@link java.time.ZonedDateTime} class which is a combination of {@link java.time.LocalDateTime} and {@link java.time.ZoneId} to handle any type on
	 * the time line.
	 * 
	 * @return The instance for further operations.
	 */
	private static Instant getInstantInstance() {
		LocalDate date = LocalDate.now(zoneId);
		LocalTime time = LocalTime.now(zoneId);
		LocalDateTime timestamp = LocalDateTime.now(zoneId);

		Instant ret = null;
		if (date != null) {
			ret = date.atStartOfDay(zoneId).toInstant();
		}
		if (time != null) {
			ret = time.atDate(LocalDate.now(zoneId)).atZone(zoneId).toInstant();
		}
		if (timestamp != null) {
			ret = timestamp.atZone(zoneId).toInstant();
		}
		return ret;
	}

	/**
	 * Retrieves an {@link java.time.Instant} object to the committed date, time or time stamp string. This represents an object on the time line and can
	 * be build out of a {@link java.time.LocalDate}, {@link java.time.LocalTime} or {@link java.time.LocalDateTime} by using the
	 * {@link java.time.ZonedDateTime} class which is a combination of {@link java.time.LocalDateTime} and {@link java.time.ZoneId} to handle any type on
	 * the time line.
	 * 
	 * @param dateTime a valid input string in one of the formats defined in <code>EFormat</code>.
	 * @return The instance for further operations.
	 */
	private static Instant getInstantInstance(String dateTime) {
		LocalDate date = getDateInstance(dateTime);
		LocalTime time = getTimeInstance(dateTime);
		LocalDateTime timestamp = getTimestampInstance(dateTime);

		Instant ret = null;
		if (date != null) {
			ret = date.atStartOfDay(zoneId).toInstant();
		}
		if (time != null) {
			ret = time.atDate(LocalDate.now()).atZone(zoneId).toInstant();
		}
		if (timestamp != null) {
			ret = timestamp.atZone(zoneId).toInstant();
		}
		return ret;
	}
}
