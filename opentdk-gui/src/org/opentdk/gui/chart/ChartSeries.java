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

import com.kostikiadis.charts.MultiAxisChart;
import org.opentdk.api.logger.MLogger;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

/**
 * This class has all properties to define a series (chart data) object for the
 * chart creation.<br>
 * <br>
 * Sample usage:
 * 
 * <pre>
 * ChartProperties props = new ChartProperties();
 * List seriesValues = new ArrayList();
 * ChartSeries series = new ChartSeries();
 * series.setSeriesName("Name");
 * series.setSeriesColor(Color.STEELBLUE);
 * series.setSeriesLabelVisible(false);
 * series.setSeriesFontSize(16);
 * ...
 * seriesValues.add(series);
 * cp.setSeriesValues(seriesValues);
 * </pre>
 * 
 * @author FME (LK Test Solutions)
 *
 */
public class ChartSeries {

	private String seriesName = "";
	private List<String> seriesValues = new ArrayList<>();
	private Shape seriesSymbol = new Circle(5, Color.STEELBLUE);
	private boolean valuesConnected = true;
	private Color seriesColor = Color.STEELBLUE;
	private int seriesFontSize = 12;
	private int seriesSymbolSize = 5;
	private boolean seriesLabelVisible = true;
	private Shape seriesLegendNode = new Rectangle(10, 10, 10, 10);
	private int belongingAxis = MultiAxisChart.Y1_AXIS;

	public final String getSeriesName() {
		return seriesName;
	}

	public final void setSeriesName(String name) {
		if (name == null || name.length() > Integer.MAX_VALUE) {
			MLogger.getInstance().log(Level.SEVERE, "Value is null or too large ==> Use default " + this.seriesName, getClass().getSimpleName(), "setSeriesName");
			return;
		}
		this.seriesName = name;
	}

	public final List<String> getSeriesValues() {
		return seriesValues;
	}

	public final void setSeriesValues(List<String> values) {
		if (values == null || values.size() > Integer.MAX_VALUE) {
			MLogger.getInstance().log(Level.SEVERE, "Values null or too large ==> No chart series to add", getClass().getSimpleName(), "setSeriesValues");
			return;
		}
		this.seriesValues = values;
	}

	public final Shape getSeriesSymbol() {
		return seriesSymbol;
	}

	public final void setSeriesSymbol(Shape symbol) {
		if (symbol == null) {
			MLogger.getInstance().log(Level.SEVERE, "Symbol is null ==> Use default " + this.seriesSymbol, getClass().getSimpleName(), "setSeriesSymbol");
			return;
		}
		this.seriesSymbol = symbol;
	}

	public final boolean isValuesConnected() {
		return valuesConnected;
	}

	public final void setValuesConnected(boolean valuesConnected) {
		this.valuesConnected = valuesConnected;
	}

	public final Color getSeriesColor() {
		return seriesColor;
	}

	public final void setSeriesColor(Color color) {
		if (color == null) {
			MLogger.getInstance().log(Level.SEVERE, "Color is null ==> Use default " + this.seriesColor, getClass().getSimpleName(), "setSeriesColor");
			return;
		}
		this.seriesColor = color;
	}

	public final int getSeriesFontSize() {
		return seriesFontSize;
	}

	public final void setSeriesFontSize(int size) {
		if (size < Integer.MIN_VALUE || size > Integer.MAX_VALUE) {
			MLogger.getInstance().log(Level.SEVERE, "Size out of bounds ==> Use default " + this.seriesFontSize, getClass().getSimpleName(), "setSeriesFontSize");
			return;
		}
		this.seriesFontSize = size;
	}

	public final int getSeriesSymbolSize() {
		return seriesSymbolSize;
	}

	public final void setSeriesSymbolSize(int size) {
		if (size < Integer.MIN_VALUE || size > Integer.MAX_VALUE) {
			MLogger.getInstance().log(Level.SEVERE, "Size out of bounds ==> Use default " + this.seriesSymbolSize, getClass().getSimpleName(), "setSeriesSymbolSize");
			return;
		}
		this.seriesSymbolSize = size;
	}

	public final boolean isSeriesLabelVisible() {
		return seriesLabelVisible;
	}

	public final void setSeriesLabelVisible(boolean seriesLabelVisible) {
		this.seriesLabelVisible = seriesLabelVisible;
	}

	public final Shape getSeriesLegendNode() {
		return seriesLegendNode;
	}

	public final void setSeriesLegendNode(Shape node) {
		if (node == null) {
			MLogger.getInstance().log(Level.SEVERE, "Shape is null ==> Use default " + this.seriesLegendNode, getClass().getSimpleName(), "setSeriesLegendNode");
			return;
		}
		this.seriesLegendNode = node;
	}

	public final int getBelongingAxis() {
		return belongingAxis;
	}

	public final void setBelongingAxis(int type) {
		if (type < Integer.MIN_VALUE || type > Integer.MAX_VALUE) {
			MLogger.getInstance().log(Level.SEVERE, "Size out of bounds ==> Use default " + this.belongingAxis, getClass().getSimpleName(), "setBelongingAxis");
			return;
		}
		this.belongingAxis = type;
	}

}
