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
package org.opentdk.api.meter;

import java.util.HashMap;
import java.util.Map;

/**
 * This class allows to store increasing or decreasing integer values. It is mainly used for
 * performance transactions but can be used for any value that should be stored during the runtime
 * of an application. Call {@link #increase(String)} to count up or {@link #decrease(String)} to
 * count down. This class can also be used via {@link EMeter} class where it is declared as
 * constant.
 * 
 * <pre>
 * EMeter.COUNTER.increase("MyTransaction");
 * ...
 * EMeter.COUNTER.increase("MyTransaction");
 * ..
 * Integer count = EMeter.COUNTER.increase("MyTransaction"); // 3
 * </pre>
 * 
 * @author LK Test Solutions
 *
 */
public class Counter {

	/**
	 * Stores pairs of transactions and counters.
	 */
	private static Map<String, Integer> storage;

	/**
	 * Initializes the {@link #storage}.
	 */
	public Counter() {
		storage = new HashMap<String, Integer>();
	}

	/**
	 * Count up by 1.
	 * 
	 * @param counterName The reference name to count for.
	 * @return The current count.
	 */
	public int decrease(String counterName) {
		return decrease(counterName, 1);
	}

	/**
	 * Count up.
	 * 
	 * @param counterName The reference name to count for.
	 * @param stepSize    Value thats gets added to the current count.
	 * @return The current count.
	 */
	public int decrease(String counterName, int stepSize) {
		return decrease(counterName, 0, stepSize);
	}

	/**
	 * Count up.
	 * 
	 * @param counterName  The reference name to count for.
	 * @param initialValue If the start value should differ from 0.
	 * @param stepSize     Value thats gets added to the current count.
	 * @return The current count.
	 */
	public int decrease(String counterName, int initialValue, int stepSize) {
		if (storage.containsKey(counterName)) {
			storage.put(counterName, storage.get(counterName) - stepSize);
		} else {
			storage.put(counterName, initialValue - stepSize);
		}
		return storage.get(counterName);
	}

	/**
	 * Count down by 1.
	 * 
	 * @param counterName The reference name to count for.
	 * @return The current count.
	 */
	public int increase(String counterName) {
		return increase(counterName, 1);
	}

	/**
	 * Count down.
	 * 
	 * @param counterName The reference name to count for.
	 * @param stepSize    Value thats gets subtracted to the current count.
	 * @return The current count.
	 */
	public int increase(String counterName, int stepSize) {
		return increase(counterName, 0, stepSize);
	}

	/**
	 * Count down.
	 * 
	 * @param counterName  The reference name to count for.
	 * @param initialValue If the start value should differ from 0.
	 * @param stepSize     Value thats gets subtracted to the current count.
	 * @return The current count.
	 */
	public int increase(String counterName, int initialValue, int stepSize) {
		if (storage.containsKey(counterName)) {
			storage.put(counterName, storage.get(counterName) + stepSize);
		} else {
			storage.put(counterName, initialValue + stepSize);
		}
		return storage.get(counterName);
	}
}
