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

import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

import org.opentdk.api.util.DateUtil;
import org.opentdk.api.util.EFormat;

/**
 * This class calculates and stores the time between calling its {@link Transaction#start} method
 * and {@link Transaction#end} method and returns the elapsed time. This allows to implement
 * performance measurements for the execution of code between a {@link Transaction#start} and
 * {@link Transaction#end}. This class can also be used via {@link EMeter} class where it is
 * declared as constant.
 * 
 * <pre>
 * EMeter.TRANSACTION.start("MyTransaction");
 * ...
 * double responseTime = EMeter.TRANSACTION.end("MyTransaction");
 * </pre>
 * 
 * @author LK Test Solutions
 *
 */
public class Transaction {

	/**
	 * Stores pairs of transactions and time stamps.
	 */
	private static Map<String, String> storage;

	/**
	 * Initializes the {@link #storage}.
	 */
	public Transaction() {
		storage = new HashMap<String, String>();
	}

	/**
	 * Gets called to set the start measurement point.
	 * 
	 * @param tn The unique name of the transaction to have a reference when calling the
	 *           {@link #end(String)} method.
	 */
	public void start(String tn) {
		storage.put(tn, DateUtil.get(EFormat.TIMESTAMP_1.getDateFormat()));
	}

	/**
	 * Gets called to set the end measurement point.
	 * 
	 * @param tn The unique name of the transaction that were used when calling {@link #start(String)}.
	 * @return The response time (difference) in seconds.
	 */
	public double end(String tn) {
		if (storage.containsKey(tn)) {
			String startTime = storage.get(tn);
			String endTime = DateUtil.get(EFormat.TIMESTAMP_1.getDateFormat());
			Double diffMillis = Double.valueOf(DateUtil.diff(startTime, endTime, ChronoUnit.MILLIS));
			return diffMillis / 1000;
		} else {
			return -1;
		}
	}
}
