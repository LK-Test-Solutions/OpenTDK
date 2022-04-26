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

import java.util.Random;

/**
 * Class to create random floating point test data.<br>
 * <br>
 * Usage:
 * 
 * <pre>
 * 
 * </pre>
 */
public class DataProvider {

	/**
	 * Left border for the random number generator {@link #randgen}.
	 */
	private final Number min;
	/**
	 * Right border for the random number generator {@link #randgen}.
	 */
	private final Number max;
	/**
	 * Random test data generator. Used for numbers in this case.
	 */
	private final Random randgen;

	/**
	 * If {@link #min} and {@link #max} are of type Integer this value gets true.
	 */
	private boolean bothInteger = false;
	/**
	 * If {@link #min} and {@link #max} are of type Long this value gets true.
	 */
	private boolean bothLong = false;
	/**
	 * If {@link #min} and {@link #max} are of type Float this value gets true.
	 */
	private boolean bothFloat = false;
	/**
	 * If {@link #min} and {@link #max} are of type Double this value gets true.
	 */
	private boolean bothDouble = false;

	/**
	 * Initialize a new test data generator for numbers. After initialization the {@link #data()} gets
	 * used to get the next random value in the defined range.
	 * 
	 * @param min {@link #min}
	 * @param max {@link #max}
	 */
	public DataProvider(Number min, Number max) {
		bothInteger = min instanceof Integer && max instanceof Integer;
		bothLong = min instanceof Long && max instanceof Long;
		bothFloat = min instanceof Float && max instanceof Float;
		bothDouble = min instanceof Double && max instanceof Double;

		if (bothInteger || bothLong || bothFloat || bothDouble) {
			this.min = min;
			this.max = max;
			this.randgen = new Random();
		} else {
			throw new IllegalArgumentException("DataProvider: Minimum and maximum value are not of the same number type. E.g. integer and integer is allowed.");
		}
	}

	/**
	 * @return The next random number value in the range defined in
	 *         {@link #DataProvider(Number, Number)}.
	 */
	public final Number data() {
		if (bothInteger) {
			return min.intValue() + randgen.nextInt(max.intValue() - min.intValue());
		} else if (bothLong) {
			return min.longValue() + (long) (Math.random() * (max.longValue() - min.longValue()));
		} else if (bothFloat) {
			return min.floatValue() + randgen.nextFloat() * (max.floatValue() - min.floatValue());
		} else if (bothDouble) {
			return min.doubleValue() + randgen.nextDouble() * (max.doubleValue() - min.doubleValue());
		} else {
			throw new RuntimeException("Initialization of 'DataProvider' went wrong and now there is no correct usage of the 'data' method possible.");
		}
	}

	/**
	 * @return {@link #min}
	 */
	public final Number getMin() {
		return min;
	}

	/**
	 * @return {@link #max}
	 */
	public final Number getMax() {
		return max;
	}
}
