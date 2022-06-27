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

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.opentdk.api.logger.MLogger;

import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 * This class has all properties to define an axis object for the chart creation.<br>
 * <br>
 * Sample usage:
 * 
 * <pre>
 * ChartProperties props = new ChartProperties();
 * ChartAxis xAxis = new ChartAxis();
 * xAxis.setLabel(..);
 * ..
 * props.setxAxis(xAxis);
 * 
 * ChartAxis y1Axis = new ChartAxis();
 * y1Axis.setLabel(..);
 * ..
 * props.setY1Axis(y1Axis);
 *
 * </pre>
 * 
 * @author FME (LK Test Solutions)
 *
 */
public class ChartAxis {

	private static final int maxChars = 10000;
	private static final int maxFontSize = 1638;
	private static final int maxMargin = 10000;
	private static final int maxMinorTickCount = 1000;
	private static final int maxTickLength = 10000;

	/** Axis values if the axis is categorical and not numeric. */
	private List<String> categories = new ArrayList<>();
	/** Space between the axis and the plot area end. For categorical axis only. */
	private int endMargin = 30;
	/** Displays the zero on the axis even if it is not part of the values. For numerical axis only. */
	private boolean forceZeroInRange = false;
	/** Label text of the axis. */
	private String label = "";
	/** Object to define font, font size and family of the axis label. */
	private ChartFont labelFont = ChartFont.font(16);
	/** Padding left, right, top and bottom of the axis label. */
	private int labelPadding = 10;
	/** First value to display on the axis. */
	private double lowestValue = 0;
	/** Number of ticks between two axis values. */
	private int minorTickCount = 0;
	/** Size in pixel of the minor ticks. */
	private double minorTickLength = 10;
	/** Show or disable the minor tick marks. */
	private boolean minorTickMarksVisible = true;
	/** Space between the axis and the plot area start. For categorical axis only. */
	private int startMargin = 10;
	/** Define the color of the axis tick label (e.g. <code>Color.BLACK</code>). <code>Color.valueOf(..)</code> takes color strings (like 'white'), RGB values (like 'rgb(255,255,255)') or HEX strings. */
	private Color tickLabelColor = Color.BLACK;
	/** Object to define the font of the axis tick label. Use <code>Font.font(..)</code> to define one. */
	private Font tickLabelFont = Font.getDefault();
	/** The space between to tick labels in pixel. */
	private int tickLabelGap = 5;
	/** Degree value to define the axis label rotation. Default value is 0 (horizontal). */
	private int tickLabelRotation = 0;
	/** Possibility to show or disable the tick label. */
	private boolean tickLabelsVisible = true;
	/** Size in pixel of the axis tick marks. */
	private double tickLength = 10;
	/** Define the color of the axis tick mark (e.g. <code>Color.BLACK</code>). <code>Color.valueOf(..)</code> takes color strings (like 'white'), RGB values (like 'rgb(255,255,255)') or HEX strings. */
	private Color tickMarkColor = Color.BLACK;
	/** Define the color of the minor tick mark (e.g. <code>Color.BLACK</code>). <code>Color.valueOf(..)</code> takes color strings (like 'white'), RGB values (like 'rgb(255,255,255)') or HEX strings. */
	private Color minorTickMarkColor = Color.BLACK;
	/** Possibility to show or disable the tick marks. */
	private boolean tickMarksVisible = true;
	/** Maximum/highest value of the axis. */
	private double valueRange = 0;
	/** Step size of each tick mark. This value should be a divisor of {@link #valueRange} to avoid the last tick to be smaller than the rest. */
	private double valueStep = 0;
	/** If true one additional tick mark will be shown. */
	private boolean leaveOneStepSpace = false;

	/**
	 * @return {@link #categories}
	 */
	public final List<String> getCategories() {
		return categories;
	}

	/**
	 * @return {@link #endMargin}
	 */
	public final int getEndMargin() {
		return endMargin;
	}

	/**
	 * @return {@link #forceZeroInRange}
	 */
	public final boolean getForceZeroInRange() {
		return forceZeroInRange;
	}

	/**
	 * @return {@link #label}
	 */
	public final String getLabel() {
		return label;
	}

	/**
	 * @return {@link #labelFont}
	 */
	public final ChartFont getLabelFont() {
		return labelFont;
	}

	/**
	 * @return {@link #labelPadding}
	 */
	public final int getLabelPadding() {
		return labelPadding;
	}

	/**
	 * @return {@link #lowestValue}
	 */
	public final double getLowestValue() {
		return lowestValue;
	}

	/**
	 * @return {@link #minorTickCount}
	 */
	public final int getMinorTickCount() {
		return minorTickCount;
	}

	/**
	 * @return {@link #minorTickLength}
	 */
	public final double getMinorTickLength() {
		return minorTickLength;
	}

	/**
	 * @return {@link #minorTickMarksVisible}
	 */
	public final boolean getMinorTickMarksVisible() {
		return minorTickMarksVisible;
	}

	/**
	 * @return {@link #startMargin}
	 */
	public final int getStartMargin() {
		return startMargin;
	}

	/**
	 * @return {@link #tickLabelColor}
	 */
	public final Color getTickLabelColor() {
		return tickLabelColor;
	}

	/**
	 * @return {@link #tickLabelFont}
	 */
	public final Font getTickLabelFont() {
		return tickLabelFont;
	}

	/**
	 * @return {@link #tickLabelGap}
	 */
	public final int getTickLabelGap() {
		return tickLabelGap;
	}

	/**
	 * @return {@link #tickLabelRotation}
	 */
	public final int getTickLabelRotation() {
		return tickLabelRotation;
	}

	/**
	 * @return {@link #tickLabelsVisible}
	 */
	public final boolean getTickLabelsVisible() {
		return tickLabelsVisible;
	}

	/**
	 * @return {@link #tickLength}
	 */
	public final double getTickLength() {
		return tickLength;
	}

	/**
	 * @return {@link #tickMarkColor}
	 */
	public final Color getTickMarkColor() {
		return tickMarkColor;
	}

	/**
	 * @return {@link #minorTickMarkColor}
	 */
	public final Color getMinorTickMarkColor() {
		return minorTickMarkColor;
	}

	/**
	 * @return {@link #tickMarksVisible}
	 */
	public final boolean getTickMarksVisible() {
		return tickMarksVisible;
	}

	/**
	 * @return {@link #valueRange}
	 */
	public final double getValueRange() {
		return valueRange;
	}

	/**
	 * @return {@link #valueStep}
	 */
	public final double getValueStep() {
		return valueStep;
	}

	/**
	 * @return {@link #leaveOneStepSpace}
	 */
	public final boolean isLeaveOneStepSpace() {
		return leaveOneStepSpace;
	}

	public final void setCategories(List<String> values) {
		if (values == null || values.isEmpty() || values.size() > Integer.MAX_VALUE) {
			MLogger.getInstance().log(Level.SEVERE, "Values are null, empty or out of range ==> No values to add", getClass().getSimpleName(), "setCategories");
			return;
		}
		this.categories = values;
	}

	public final void setEndMargin(int size) {
		if (size < 0 || size > maxMargin) {
			MLogger.getInstance().log(Level.SEVERE, "Out of range ==> Allowed: 0 to " + maxMargin, getClass().getSimpleName(), "setEndMargin");
			return;
		}
		this.endMargin = size;
	}

	public final void setForceZeroInRange(boolean forceZeroInRange) {
		this.forceZeroInRange = forceZeroInRange;
	}

	public final void setLabel(String name) {
		if (name == null || name.isBlank() || name.length() > maxChars) {
			MLogger.getInstance().log(Level.SEVERE, "Label is null, blank or too large ==> Use default " + this.label, getClass().getSimpleName(), "setLabel");
			return;
		}
		this.label = name;
	}

	public final void setLabelFont(ChartFont font) {
		if (font == null) {
			MLogger.getInstance().log(Level.SEVERE, "Font is null ==> Use default " + this.labelFont.toString(), getClass().getSimpleName(), "setLabelFont");
			return;
		}
		this.labelFont = font;
	}

	public final void setLabelPadding(int pixelSize) {
		if (pixelSize < 0 || pixelSize > maxFontSize) {
			MLogger.getInstance().log(Level.SEVERE, "Pixel size is out of range (< 0 or > " + maxFontSize + ") ==> Use default " + this.labelPadding, getClass().getSimpleName(), "setLabelPadding");
			return;
		}
		this.labelPadding = pixelSize;
	}

	public final void setLowestValue(double value) {
		if (Double.isNaN(value) || Double.isInfinite(value)) {
			MLogger.getInstance().log(Level.SEVERE, "Value out of range ==> Use default " + this.lowestValue, getClass().getSimpleName(), "setLowestValue");
			return;
		}
		this.lowestValue = value;
	}

	public final void setMinorTickCount(int value) {
		if (value < 0 || value > maxMinorTickCount) {
			MLogger.getInstance().log(Level.SEVERE, "Value out of long range ==> Use default " + this.minorTickCount, getClass().getSimpleName(), "setMinorTickCount");
			return;
		}
		this.minorTickCount = value;
	}

	public final void setMinorTickLength(double value) {
		if (Double.isNaN(value) || Double.isInfinite(value) || value < 0 || value > maxTickLength) {
			MLogger.getInstance().log(Level.SEVERE, "Value out of long range ==> Use default " + this.minorTickLength, getClass().getSimpleName(), "setMinorTickLength");
			return;
		}
		this.minorTickLength = value;
	}

	public final void setMinorTickMarksVisible(boolean isVisible) {
		this.minorTickMarksVisible = isVisible;
	}

	public final void setStartMargin(int size) {
		if (size < 0 || size > maxMargin) {
			MLogger.getInstance().log(Level.SEVERE, "Out of range ==> Allowed: 0 to " + maxMargin, getClass().getSimpleName(), "setStartMargin");
			return;
		}
		this.startMargin = size;
	}

	public final void setTickLabelColor(Color color) {
		if (color == null) {
			MLogger.getInstance().log(Level.SEVERE, "Color is null ==> Use default " + this.tickLabelColor.toString(), getClass().getSimpleName(), "setTickLabelColor");
			return;
		}
		this.tickLabelColor = color;
	}

	public final void setTickLabelFont(Font font) {
		if (font == null) {
			MLogger.getInstance().log(Level.SEVERE, "Font is null ==> Use default " + this.tickLabelFont.toString(), getClass().getSimpleName(), "setTickLabelFont");
			return;
		}
		this.tickLabelFont = font;
	}

	public final void setTickLabelGap(int size) {
		if (size < 0 || size > maxFontSize) {
			MLogger.getInstance().log(Level.SEVERE, "Size is out of range (< 0 or > " + maxFontSize + ") ==> Use default " + this.tickLabelGap, getClass().getSimpleName(), "setTickLabelGap");
			return;
		}
		this.tickLabelGap = size;
	}

	public final void setTickLabelRotation(int degree) {
		if (degree < 0 || degree > 360) {
			MLogger.getInstance().log(Level.SEVERE, "Degree value is out of range (< 0 or > 360) ==> Use default " + this.tickLabelRotation, getClass().getSimpleName(), "setTickLabelRotation");
			return;
		}
		this.tickLabelRotation = degree;
	}

	public final void setTickLabelsVisible(boolean isVisible) {
		this.tickLabelsVisible = isVisible;
	}

	public final void setTickLength(double value) {
		if (Double.isNaN(value) || Double.isInfinite(value) || value < 0 || value > maxTickLength) {
			MLogger.getInstance().log(Level.SEVERE, "Value out of long range ==> Use default " + this.tickLength, getClass().getSimpleName(), "setTickLength");
			return;
		}
		this.tickLength = value;
	}

	public final void setTickMarkColor(Color color) {
		if (color == null) {
			MLogger.getInstance().log(Level.SEVERE, "Color is null ==> Use default " + this.tickMarkColor.toString(), getClass().getSimpleName(), "setTickMarkColor");
			return;
		}
		this.tickMarkColor = color;
	}

	public final void setMinorTickMarkColor(Color color) {
		if (color == null) {
			MLogger.getInstance().log(Level.SEVERE, "Color is null ==> Use default " + this.minorTickMarkColor.toString(), getClass().getSimpleName(), "setMinorTickMarkColor");
			return;
		}
		this.minorTickMarkColor = color;
	}

	public final void setTickMarksVisible(boolean isVisible) {
		this.tickMarksVisible = isVisible;
	}

	public final void setValueRange(double value) {
		if (Double.isNaN(value) || Double.isInfinite(value)) {
			MLogger.getInstance().log(Level.SEVERE, "Value out of long range ==> Use default " + this.valueRange, getClass().getSimpleName(), "setValueRange");
			return;
		}
		this.valueRange = value;
	}

	public final void setValueStep(double value) {
		if (Double.isNaN(value) || Double.isInfinite(value)) {
			MLogger.getInstance().log(Level.SEVERE, "Value out of long range ==> Use default " + this.valueStep, getClass().getSimpleName(), "setValueStep");
			return;
		}
		this.valueStep = value;
	}

	public final void setLeaveOneStepSpace(boolean leaveOneStepSpace) {
		this.leaveOneStepSpace = leaveOneStepSpace;
	}

}
