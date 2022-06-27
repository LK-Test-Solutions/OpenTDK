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
package org.opentdk.gui.chart;

import java.util.logging.Level;

import org.opentdk.api.logger.MLogger;

import javafx.scene.text.FontWeight;

/**
 * This class gets used to store font information for the chart creation. The
 * <code>ChartFont.font(..)</code> methods simply create a new font to use it
 * for further operations. 
 * 
 * @author FME (LK Test Solutions)
 *
 */
public final class ChartFont {

	private int size = 16;
	private FontWeight weight = FontWeight.BOLD;
	private String family = "System";

	private ChartFont(int size) {
		this.checkSize(size);
	}

	private ChartFont(int size, FontWeight weight) {
		this.checkSize(size);
		this.checkWeight(weight);
	}

	private ChartFont(int size, FontWeight weight, String family) {
		this.checkSize(size);
		this.checkWeight(weight);
		this.checkFamily(family);
	}

	private final void checkSize(int size) {
		if (size < 0 || size > Integer.MAX_VALUE) {
			MLogger.getInstance().log(Level.SEVERE, "Size is negative or out of integer bounds ==> Use default " + this.size, getClass().getSimpleName(), "setSize");
			return;
		}
		this.size = size;
	}

	private final void checkWeight(FontWeight weight) {
		this.weight = weight;
	}

	private final void checkFamily(String family) {
		if (family == null || family.isBlank() || family.length() > Integer.MAX_VALUE) {
			MLogger.getInstance().log(Level.SEVERE, "Family is null, blank or too large ==> Use default " + this.family, getClass().getSimpleName(), "setFamily");
			return;
		}
		this.family = family;
	}

	public static ChartFont font(int size) {
		return new ChartFont(size);
	}

	public static ChartFont font(int size, FontWeight weight) {
		return new ChartFont(size, weight);
	}

	public static ChartFont font(int size, FontWeight weight, String family) {
		return new ChartFont(size, weight, family);
	}

	public final String getFamily() {
		return family;
	}

	public final FontWeight getWeight() {
		return weight;
	}

	public final int getSize() {
		return size;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "[Size: " + size + ", Weight: " + weight.name() + ", Family: " + family + "]";
	}

}
