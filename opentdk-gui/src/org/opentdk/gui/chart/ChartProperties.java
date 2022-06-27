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

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.opentdk.api.logger.MLogger;

import javafx.geometry.Insets;
import javafx.geometry.Side;
import javafx.scene.paint.Color;

/**
 * This class has all properties to trigger the chart creation.<br>
 * <br>
 * Sample usage:
 * 
 * <pre>
 * ChartProperties cp = new ChartProperties();
 * cp.setTitle("Title");
 * cp.setPlotAreaColor(Color.valueOf("rgb(188,222,255)"));
 * cp.setLegendColor(Color.valueOf("rgb(222,222,222)"));
 * cp.setTitleFont(ChartFont.font(18, FontWeight.BOLD));
 * cp.setHeight(741);
 * cp.setWidth(1286);
 * cp.setLegendSide(Side.BOTTOM);
 * ...
 * ChartCreatorPlugin chartPlugin = new ChartCreatorPlugin("LINE", "./output/LineChart.png", cp);
 * chartPlugin.run();
 * if (chartPlugin.isSuccess() == false) {
 * 	Assert.fail("Chart creation finished with error " + getClass().getSimpleName());
 * }
 * </pre>
 * 
 * @author FME (LK Test Solutions)
 *
 */
public class ChartProperties {

	private static final int maxChartSize = 10000;
	private static final int maxFontSize = 1638;
	private static final int maxChars = 10000;

	private int categoryGap = 10;
	private Color chartBackground = Color.WHITE;
	private int chartContentPadding = 10;
	private ChartFont chartFont = ChartFont.font(16);
	private List<ChartMarker> chartMarkers = new ArrayList<>();
	private int height = 435;
	private boolean horizontalGridLinesVisible = true;
	private boolean horizontalZeroLineVisible = true;
	private Color legendColor = Color.WHITE;
	private ChartFont legendFont = ChartFont.font(16);
	private Side legendSide = Side.BOTTOM;
	private boolean legendVisible = true;
	private Color plotAreaColor = Color.WHITE;
	private List<ChartSeries> seriesValues = new ArrayList<>();
	private String title = "";
	private ChartFont titleFont = ChartFont.font(16);
	private int titlePadding = 10;
	private Side titleSide = Side.TOP;
	private boolean verticalGridLinesVisible = true;
	private boolean verticalZeroLineVisible = true;
	private int width = 768;
	private boolean wrapTitleText = true;
	private boolean horizontalGridLinesToFront = false;
	private Color horizontalGridLinesColor = Color.GAINSBORO;
	private boolean verticalGridLinesToFront = true;
	private Color verticalGridLinesColor = Color.GAINSBORO;
	private Insets chartPadding = Insets.EMPTY;
	private NumberFormat numberAxisFormat = new DecimalFormat();

	private ChartAxis xAxis = new ChartAxis();
	private ChartAxis y1Axis = new ChartAxis();
	private ChartAxis y2Axis = new ChartAxis();

	private ChartBorder xAxisBorder = new ChartBorder(Color.BLACK, Color.TRANSPARENT, Color.TRANSPARENT, Color.TRANSPARENT);
	private ChartBorder y1AxisBorder = new ChartBorder(Color.TRANSPARENT, Color.BLACK, Color.TRANSPARENT, Color.TRANSPARENT);
	private ChartBorder y2AxisBorder = new ChartBorder(Color.TRANSPARENT, Color.TRANSPARENT, Color.TRANSPARENT, Color.BLACK);

	public final int getCategoryGap() {
		return categoryGap;
	}

	public final Color getChartBackground() {
		return chartBackground;
	}

	public final int getChartContentPadding() {
		return chartContentPadding;
	}

	public final ChartFont getChartFont() {
		return chartFont;
	}

	public final List<ChartMarker> getChartMarkers() {
		return chartMarkers;
	}

	public final int getHeight() {
		return height;
	}

	public final boolean isHorizontalGridLinesVisible() {
		return horizontalGridLinesVisible;
	}

	public final boolean isHorizontalZeroLineVisible() {
		return horizontalZeroLineVisible;
	}

	public final Color getLegendColor() {
		return legendColor;
	}

	public final ChartFont getLegendFont() {
		return legendFont;
	}

	public final Side getLegendSide() {
		return legendSide;
	}

	public final boolean isLegendVisible() {
		return legendVisible;
	}

	public final Color getPlotAreaColor() {
		return plotAreaColor;
	}

	public final List<ChartSeries> getSeriesValues() {
		return seriesValues;
	}

	public final String getTitle() {
		return title;
	}

	public final ChartFont getTitleFont() {
		return titleFont;
	}

	public final int getTitlePadding() {
		return titlePadding;
	}

	public final Side getTitleSide() {
		return titleSide;
	}

	public final boolean isVerticalGridLinesVisible() {
		return verticalGridLinesVisible;
	}

	public final boolean isVerticalZeroLineVisible() {
		return verticalZeroLineVisible;
	}

	public final int getWidth() {
		return width;
	}

	public final boolean isWrapTitleText() {
		return wrapTitleText;
	}

	public final boolean isHorizontalGridLinesToFront() {
		return horizontalGridLinesToFront;
	}

	public final Color getHorizontalGridLinesColor() {
		return horizontalGridLinesColor;
	}

	public final boolean isVerticalGridLinesToFront() {
		return verticalGridLinesToFront;
	}

	public final Color getVerticalGridLinesColor() {
		return verticalGridLinesColor;
	}

	public final Insets getChartPadding() {
		return chartPadding;
	}

	public final ChartAxis getxAxis() {
		return xAxis;
	}

	public final ChartAxis getY1Axis() {
		return y1Axis;
	}

	public final ChartAxis getY2Axis() {
		return y2Axis;
	}

	public final ChartBorder getxAxisBorder() {
		return xAxisBorder;
	}

	public final ChartBorder getY1AxisBorder() {
		return y1AxisBorder;
	}

	public final ChartBorder getY2AxisBorder() {
		return y2AxisBorder;
	}
	
	public final NumberFormat getNumberAxisFormat() {
		return numberAxisFormat;
	}

	public final void setCategoryGap(int size) {
		if (size < 0 || size > maxFontSize) {
			MLogger.getInstance().log(Level.SEVERE, "Out of range ==> Allowed: 0 to " + maxFontSize, getClass().getSimpleName(), "setCategoryGap");
			return;
		}
		this.categoryGap = size;
	}

	public final void setChartBackground(Color color) {
		if (color == null) {
			MLogger.getInstance().log(Level.SEVERE, "Color is null ==> Use default " + chartBackground.toString(), getClass().getSimpleName(), "setChartBackground");
			return;
		}
		this.chartBackground = color;
	}

	public final void setChartContentPadding(int pixelSize) {
		if (pixelSize < 0 || pixelSize > maxFontSize) {
			MLogger.getInstance().log(Level.SEVERE, "Out of range ==> Allowed 0 to " + maxFontSize, getClass().getSimpleName(), "setChartContentPadding");
			return;
		}
		this.chartContentPadding = pixelSize;
	}

	public final void setChartFont(ChartFont font) {
		if (font == null) {
			MLogger.getInstance().log(Level.SEVERE, "Font is null ==> Use default " + chartFont.toString(), getClass().getSimpleName(), "setChartFont");
			return;
		}
		this.chartFont = font;
	}

	public final void setChartMarkers(List<ChartMarker> markers) {
		if (markers == null || markers.isEmpty() || markers.size() > Integer.MAX_VALUE) {
			MLogger.getInstance().log(Level.SEVERE, "Out of range", getClass().getSimpleName(), "setChartMarkers");
			return;
		}
		this.chartMarkers = markers;
	}

	public final void setHeight(int pixelSize) {
		if (pixelSize < 0 || pixelSize > maxChartSize) {
			MLogger.getInstance().log(Level.SEVERE, "Out of range ==> Allowed: 0 to " + maxChartSize, getClass().getSimpleName(), "setHeight");
			return;
		}
		this.height = pixelSize;
	}

	public final void setHorizontalGridLinesVisible(boolean isVisible) {
		this.horizontalGridLinesVisible = isVisible;
	}

	public final void setHorizontalZeroLineVisible(boolean isVisible) {
		this.horizontalZeroLineVisible = isVisible;
	}

	public final void setLegendColor(Color color) {
		if (color == null) {
			MLogger.getInstance().log(Level.SEVERE, "Color is null ==> Use default " + legendColor.toString(), getClass().getSimpleName(), "setLegendColor");
			return;
		}
		this.legendColor = color;
	}

	public final void setLegendFont(ChartFont font) {
		if (font == null) {
			MLogger.getInstance().log(Level.SEVERE, "Font is null ==> Use default " + legendFont.toString(), getClass().getSimpleName(), "setLegendFont");
			return;
		}
		this.legendFont = font;
	}

	public final void setLegendSide(Side side) {
		if (side == null) {
			MLogger.getInstance().log(Level.SEVERE, "Side is null ==> Use default " + legendSide.name(), getClass().getSimpleName(), "setLegendSide");
		}
		this.legendSide = side;
	}

	public final void setLegendVisible(boolean isVisible) {
		this.legendVisible = isVisible;
	}

	public final void setPlotAreaColor(Color color) {
		if (color == null) {
			MLogger.getInstance().log(Level.SEVERE, "Color is null ==> Use default " + plotAreaColor.toString(), getClass().getSimpleName(), "setPlotAreaColor");
			return;
		}
		this.plotAreaColor = color;
	}

	public final void setSeriesValues(List<ChartSeries> values) {
		if (values == null || values.isEmpty() || values.size() > Integer.MAX_VALUE) {
			MLogger.getInstance().log(Level.SEVERE, "Out of range", getClass().getSimpleName(), "setSeriesValues");
			return;
		}
		this.seriesValues = values;
	}

	public final void setTitle(String title) {
		if (title == null || title.isBlank() || title.length() > maxChars) {
			MLogger.getInstance().log(Level.SEVERE, "Null, blank or too large", getClass().getSimpleName(), "setTitle");
			return;
		}
		this.title = title;
	}

	public final void setTitleFont(ChartFont font) {
		if (font == null) {
			MLogger.getInstance().log(Level.SEVERE, "Font is null ==> Use default " + titleFont.toString(), getClass().getSimpleName(), "setTitleFont");
			return;
		}
		this.titleFont = font;
	}

	public final void setTitlePadding(int pixelSize) {
		if (pixelSize < 0 || pixelSize > maxFontSize) {
			MLogger.getInstance().log(Level.SEVERE, "Out of range ==> Allowed: 0 to " + maxFontSize, getClass().getSimpleName(), "setTitlePadding");
			return;
		}
		this.titlePadding = pixelSize;
	}

	public final void setTitleSide(Side side) {
		if (side == null) {
			MLogger.getInstance().log(Level.SEVERE, "Side is null ==> Use default " + titleSide.name(), getClass().getSimpleName(), "setTitleSide");
		}
		this.titleSide = side;
	}

	public final void setVerticalGridLinesVisible(boolean isVisible) {
		this.verticalGridLinesVisible = isVisible;
	}

	public final void setVerticalZeroLineVisible(boolean isVisible) {
		this.verticalZeroLineVisible = isVisible;
	}

	public final void setWidth(int pixelSize) {
		if (pixelSize < 0 || pixelSize > maxChartSize) {
			MLogger.getInstance().log(Level.SEVERE, "Out of range ==> Allowed: 0 to " + maxChartSize, getClass().getSimpleName(), "setWidth");
			return;
		}
		this.width = pixelSize;
	}

	public final void setWrapTitleText(boolean wrapTitleText) {
		this.wrapTitleText = wrapTitleText;
	}

	public final void setxAxis(ChartAxis axis) {
		if (axis == null) {
			MLogger.getInstance().log(Level.SEVERE, "Axis object null ==> use default properties", getClass().getSimpleName(), "setxAxis");
			return;
		}
		this.xAxis = axis;
	}

	public final void setY1Axis(ChartAxis axis) {
		if (axis == null) {
			MLogger.getInstance().log(Level.SEVERE, "Axis object null ==> use default properties", getClass().getSimpleName(), "sety1Axis");
			return;
		}
		this.y1Axis = axis;
	}

	public final void setY2Axis(ChartAxis axis) {
		if (axis == null) {
			MLogger.getInstance().log(Level.SEVERE, "Axis object null ==> use default properties", getClass().getSimpleName(), "sety2Axis");
			return;
		}
		this.y2Axis = axis;
	}

	public final void setxAxisBorder(ChartBorder border) {
		if (border == null) {
			MLogger.getInstance().log(Level.SEVERE, "Border object null ==> use default properties", getClass().getSimpleName(), "setxAxisBorder");
			return;
		}
		this.xAxisBorder = border;
	}

	public final void setY1AxisBorder(ChartBorder border) {
		if (border == null) {
			MLogger.getInstance().log(Level.SEVERE, "Border object null ==> use default properties", getClass().getSimpleName(), "setY1AxisBorder");
			return;
		}
		this.y1AxisBorder = border;
	}

	public final void setY2AxisBorder(ChartBorder border) {
		if (border == null) {
			MLogger.getInstance().log(Level.SEVERE, "Border object null ==> use default properties", getClass().getSimpleName(), "setY2AxisBorder");
			return;
		}
		this.y2AxisBorder = border;
	}

	public final void setHorizontalGridLinesToFront(boolean horizontalGridLinesToFront) {
		this.horizontalGridLinesToFront = horizontalGridLinesToFront;
	}

	public final void setHorizontalGridLinesColor(Color horizontalGridLinesColor) {
		this.horizontalGridLinesColor = horizontalGridLinesColor;
	}

	public final void setVerticalGridLinesToFront(boolean verticalGridLinesToFront) {
		this.verticalGridLinesToFront = verticalGridLinesToFront;
	}

	public final void setVerticalGridLinesColor(Color color) {
		if (color == null) {
			MLogger.getInstance().log(Level.SEVERE, "Color is null ==> Use default " + this.verticalGridLinesColor, getClass().getSimpleName(), "setVerticalGridLinesColor");
			return;
		}
		this.verticalGridLinesColor = color;
	}

	public final void setChartPadding(Insets chartPadding) {
		if (chartPadding == null) {
			MLogger.getInstance().log(Level.SEVERE, "Insets is null ==> Use default " + this.chartPadding, getClass().getSimpleName(), "setChartPadding");
			return;
		}
		this.chartPadding = chartPadding;
	}

	public final void setNumberAxisFormat(NumberFormat format) {
		if(format == null) {
			MLogger.getInstance().log(Level.SEVERE, "Format is null ==> Use default " + this.numberAxisFormat, getClass().getSimpleName(), "setNumberAxisFormat");
			return;
		}
		this.numberAxisFormat = format;
	}

//	public ChartProperties(ChartProperties cp) {	
//		this.setCategoryGap(cp.getCategoryGap());
//		this.setChartBackground(cp.getChartBackground());
//		this.setChartContentPadding(cp.getChartContentPadding());
//		this.setChartFont(cp.getChartFont());
//		this.setChartMarkers(cp.getChartMarkers());
//		this.setHeight(cp.getHeight());
//		this.setHorizontalGridLinesVisible(cp.isHorizontalGridLinesVisible());
//		this.setHorizontalZeroLineVisible(cp.isHorizontalZeroLineVisible());
//		this.setLegendColor(cp.getLegendColor());
//		this.setLegendFont(cp.getLegendFont());
//		this.setLegendSide(cp.getLegendSide());
//		this.setLegendVisible(cp.isLegendVisible());
//		this.setPlotAreaColor(cp.getPlotAreaColor());
//		this.setSeriesValues(cp.getSeriesValues());
//		this.setTitle(cp.getTitle());
//		this.setTitleFont(cp.getTitleFont());
//		this.setTitlePadding(cp.getTitlePadding());
//		this.setTitleSide(cp.getTitleSide());
//		this.setVerticalGridLinesVisible(cp.isVerticalGridLinesVisible());
//		this.setVerticalZeroLineVisible(cp.isVerticalZeroLineVisible());
//		this.setWidth(cp.getWidth());
//		this.setWrapTitleText(cp.isWrapTitleText());
//		this.setHorizontalGridLinesToFront(cp.isHorizontalGridLinesToFront());
//		this.setHorizontalGridLinesColor(cp.getHorizontalGridLinesColor());
//		this.setVerticalGridLinesToFront(cp.isVerticalGridLinesToFront());
//		this.setVerticalGridLinesColor(cp.getVerticalGridLinesColor());
//		this.setChartPadding(cp.getChartPadding());
//		this.setNumberAxisFormat(cp.getNumberAxisFormat());
//		this.setxAxis(cp.getxAxis());
//		this.setY1Axis(cp.getY1Axis());
//		this.setY2Axis(cp.getY2Axis());
//		this.setxAxisBorder(cp.getxAxisBorder());
//		this.setY1AxisBorder(cp.getY1AxisBorder());
//		this.setY2AxisBorder(cp.getY2AxisBorder());
//	}
}
