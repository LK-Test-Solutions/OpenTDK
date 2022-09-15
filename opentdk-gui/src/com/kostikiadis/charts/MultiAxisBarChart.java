/*
 * Copyright (c) 2010, 2014, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */
package com.kostikiadis.charts;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javafx.application.Platform;
import javafx.beans.NamedArg;
import javafx.beans.property.DoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.css.CssMetaData;
import javafx.css.PseudoClass;
import javafx.css.Styleable;
import javafx.css.StyleableDoubleProperty;
import javafx.geometry.Orientation;
import javafx.scene.AccessibleRole;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import javafx.scene.chart.Axis;
import javafx.scene.chart.ValueAxis;
import javafx.scene.chart.CategoryAxis;

/**
 * A chart that plots bars indicating data values for a category. The bars can be vertical or horizontal depending on which axis is a category axis.
 * 
 * @param <X> the data type of the first axis.
 * @param <Y> the data type of the second axis.
 * 
 * @since JavaFX 2.0
 */
public class MultiAxisBarChart<X, Y> extends MultiAxisChart<X, Y> {

	// -------------- PRIVATE FIELDS -------------------------------------------

	private final Map<Series<X, Y>, Map<String, Data<X, Y>>> seriesCategoryMap = new HashMap<>();
	private final Legend legend = new Legend();
	private final Orientation orientation;
	private final javafx.scene.chart.CategoryAxis categoryAxis;
	private final ValueAxis<? super Number> valueAxis;
	private static String NEGATIVE_STYLE = "negative";

	// -------------- PUBLIC PROPERTIES ----------------------------------------

	/** The gap to leave between bars in the same category */
	private DoubleProperty barGap = new StyleableDoubleProperty(0) {
		@Override
		protected void invalidated() {
			get();
			layout();
		}

		@Override
		public Object getBean() {
			return MultiAxisBarChart.this;
		}

		@Override
		public String getName() {
			return "barGap";
		}

		@Override
		public CssMetaData<MultiAxisBarChart<?, ?>, Number> getCssMetaData() {
			return null;
		}
	};

	public final double getBarGap() {
		return barGap.getValue();
	}

	public final void setBarGap(double value) {
		barGap.setValue(value);
	}

	public final DoubleProperty barGapProperty() {
		return barGap;
	}

	/** The gap to leave between bars in separate categories */
	private DoubleProperty categoryGap = new StyleableDoubleProperty(0) {
		@Override
		protected void invalidated() {
			get();
			layout();
		}

		@Override
		public Object getBean() {
			return MultiAxisBarChart.this;
		}

		@Override
		public String getName() {
			return "categoryGap";
		}

		@Override
		public CssMetaData<MultiAxisBarChart<?, ?>, Number> getCssMetaData() {
			return null;
		}
	};

	public final double getCategoryGap() {
		return categoryGap.getValue();
	}

	public final void setCategoryGap(double value) {
		categoryGap.setValue(value);
	}

	public final DoubleProperty categoryGapProperty() {
		return categoryGap;
	}

	// -------------- CONSTRUCTOR ----------------------------------------------

	/**
	 * Construct a new MultiAxisBarChart with the given axis. The two axis should be a ValueAxis/NumberAxis (Y1, Y2) and a CategoryAxis ( X-Axis), they
	 * can be in either order depending on if you want a horizontal or vertical bar chart.
	 *
	 * @param xAxis  The x axis to use
	 * @param yAxis  The y1 axis to use
	 * @param y2Axis The y2 axis to use
	 *
	 */
	public MultiAxisBarChart(Axis<X> xAxis, Axis<Y> yAxis, Axis<Y> y2Axis) {
		this(xAxis, yAxis, y2Axis, FXCollections.<Series<X, Y>>observableArrayList());
	}

	/**
	 * Construct a new MultiAxisBarChart with the given axis and data. The two axis should be a ValueAxis/NumberAxis and a CategoryAxis, they can be in
	 * either order depending on if you want a horizontal or vertical bar chart.
	 *
	 * @param xAxis  The x axis to use
	 * @param y1Axis The y axis to use
	 * @param y2Axis The second y axis to use
	 * @param data   The data to use, this is the actual list used so any changes to it will be reflected in the chart
	 */
	@SuppressWarnings("unchecked")
	public MultiAxisBarChart(Axis<X> xAxis, Axis<Y> y1Axis, Axis<Y> y2Axis, ObservableList<Series<X, Y>> data) {
		super(xAxis, y1Axis, y2Axis);
		getStyleClass().add("bar-chart");
		setLegend(legend);

		categoryAxis = (CategoryAxis) xAxis;

		valueAxis = (ValueAxis<? super Number>) y1Axis;
		orientation = Orientation.VERTICAL;

		// update CSS
		pseudoClassStateChanged(HORIZONTAL_PSEUDOCLASS_STATE, orientation == Orientation.HORIZONTAL);
		pseudoClassStateChanged(VERTICAL_PSEUDOCLASS_STATE, orientation == Orientation.VERTICAL);
		setData(data);
	}

	/**
	 * Construct a new MultiAxisBarChart with the given axis and data. The two axis should be a ValueAxis/NumberAxis and a CategoryAxis, they can be in
	 * either order depending on if you want a horizontal or vertical bar chart.
	 *
	 * @param xAxis       The x axis to use
	 * @param y1Axis      The y axis to use
	 * @param y2Axis      The second y axis to use
	 * @param data        The data to use, this is the actual list used so any changes to it will be reflected in the chart
	 * @param categoryGap The gap to leave between bars in separate categories
	 */
	public MultiAxisBarChart(Axis<X> xAxis, Axis<Y> y1Axis, Axis<Y> y2Axis, ObservableList<Series<X, Y>> data, @NamedArg("categoryGap") double categoryGap) {
		this(xAxis, y1Axis, y2Axis);
		setData(data);
		setCategoryGap(categoryGap);
	}

	// -------------- PROTECTED METHODS ----------------------------------------

	@Override
	protected void seriesAdded(Series<X, Y> series, int seriesIndex) {
		Map<String, Data<X, Y>> categoryMap = new HashMap<String, Data<X, Y>>();
		for (int j = 0; j < series.getData().size(); j++) {
			Data<X, Y> item = series.getData().get(j);
			Node bar = createBar(series, seriesIndex, item, j);
			String category;
			if (orientation == Orientation.VERTICAL) {
				category = (String) item.getXValue();
			} else {
				category = (String) item.getYValue();
			}
			categoryMap.put(category, item);

			double barVal = (orientation == Orientation.VERTICAL) ? ((Number) item.getYValue()).doubleValue() : ((Number) item.getXValue()).doubleValue();
			if (barVal < 0) {
				bar.getStyleClass().add(NEGATIVE_STYLE);
			}
			getPlotChildren().add(bar);

		}
		if (categoryMap.size() > 0) {
			seriesCategoryMap.put(series, categoryMap);
		}
	}

	@Override
	protected void layoutPlotChildren() {
//		double catSpace = this.categoryAxis.getCategorySpacing();
		int catCount =  this.categoryAxis.getCategories().size();
		if(catCount == 0) {
			catCount = 1;
		}
		double catSpace = this.categoryAxis.getWidth() / catCount; // Each catSpace has the same proportion on the abscissa depending on the number of categories
		double avilableBarSpace = catSpace - getCategoryGap() + getBarGap(); 
		double barWidth = avilableBarSpace / getSeriesSize() - getBarGap(); // Divide with seriesSize to split the avilableBarSpace by the number of bars per category
		double barOffset = -((catSpace - getCategoryGap()) / 2.0D);
		double zeroPos = (this.valueAxis.getLowerBound() > 0.0D) ? this.valueAxis.getDisplayPosition(Double.valueOf(this.valueAxis.getLowerBound())) : this.valueAxis.getZeroPosition();
		if (barWidth <= 0.0D)
			barWidth = 1.0D;
		int catIndex = 0;
		for (String category : this.categoryAxis.getCategories()) {
			int index = 0;
			for (Iterator<MultiAxisChart.Series<X, Y>> sit = getDisplayedSeriesIterator(); sit.hasNext();) {
				MultiAxisChart.Series<X, Y> series = sit.next();
				MultiAxisChart.Data<X, Y> item = getDataItem(series, index, catIndex, category);
				if (item != null) {
					double valPos;
					Node bar = item.getNode();
					double categoryPos = getXAxis().getDisplayPosition(item.getCurrentX());
					if (item.getExtraValue() == null || ((Integer) item.getExtraValue()).intValue() == 0) {
						valPos = getY1Axis().getDisplayPosition(item.getCurrentY());
					} else if (getY2Axis() != null) {
						if (getY2Axis().isVisible()) {
							valPos = getY2Axis().getDisplayPosition(item.getCurrentY());
						} else {
							continue;
						}
					} else {
						throw new NullPointerException("Y2 axis is not defined.");
					}
					if (Double.isNaN(categoryPos) || Double.isNaN(valPos))
						continue;
					double bottom = Math.min(valPos, zeroPos);
					double top = Math.max(valPos, zeroPos);
//					this.bottomPos = bottom;
					if (this.orientation == Orientation.VERTICAL) {
						bar.resizeRelocate(categoryPos + barOffset + (barWidth + getBarGap()) * index, bottom, barWidth, top - bottom);
					} else {
						bar.resizeRelocate(bottom, categoryPos + barOffset + (barWidth + getBarGap()) * index, top - bottom, barWidth);
					}
					index++;
				}
			}
			catIndex++;
		}
	}

	/**
	 * This is called whenever a series is added or removed and the legend needs to be updated
	 */
	@Override
	protected void updateLegend() {
		legend.getItems().clear();
		if (getData() != null) {
			for (int seriesIndex = 0; seriesIndex < getData().size(); seriesIndex++) {
				Series<X, Y> series = getData().get(seriesIndex);
				Legend.LegendItem legenditem = new Legend.LegendItem(series.getName());
				legenditem.getSymbol().getStyleClass().addAll("chart-bar", "series" + seriesIndex, "bar-legend-symbol", series.defaultColorStyleClass);
				legend.getItems().add(legenditem);
			}
		}
		if (legend.getItems().size() > 0) {
			if (getLegend() == null) {
				setLegend(legend);
			}
		} else {
			setLegend(null);
		}
	}

	// -------------- PRIVATE METHODS ------------------------------------------

	private Node createBar(Series<X, Y> series, int seriesIndex, final Data<X, Y> item, int itemIndex) {
		Node bar = item.getNode();
		if (bar == null) {
			bar = new StackPane();
			bar.setAccessibleRole(AccessibleRole.TEXT);
			bar.setAccessibleRoleDescription("Bar");
			bar.focusTraversableProperty().bind(Platform.accessibilityActiveProperty());
			item.setNode(bar);
		}
		bar.getStyleClass().addAll("chart-bar", "series" + seriesIndex, "data" + itemIndex, series.defaultColorStyleClass);
		return bar;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @since JavaFX 8.0
	 */
	@Override
	public List<CssMetaData<? extends Styleable, ?>> getCssMetaData() {
		return getClassCssMetaData();
	}

	private MultiAxisChart.Data<X, Y> getDataItem(MultiAxisChart.Series<X, Y> series, int seriesIndex, int itemIndex, String category) {
		Map<String, MultiAxisChart.Data<X, Y>> catmap = this.seriesCategoryMap.get(series);
		return (catmap != null) ? catmap.get(category) : null;
	}

	/** Pseudo class indicating this is a vertical chart. */
	private static final PseudoClass VERTICAL_PSEUDOCLASS_STATE = PseudoClass.getPseudoClass("vertical");

	/** Pseudo class indicating this is a horizontal chart. */
	private static final PseudoClass HORIZONTAL_PSEUDOCLASS_STATE = PseudoClass.getPseudoClass("horizontal");

	// -------------- UNUSED METHODS ----------------------------------------

	@Override
	protected void dataItemAdded(Series<X, Y> series, int itemIndex, Data<X, Y> item) {

	}

	@Override
	protected void dataItemRemoved(final Data<X, Y> item, final Series<X, Y> series) {

	}

	@Override
	protected void dataItemChanged(Data<X, Y> item) {

	}

	@Override
	protected void seriesRemoved(final Series<X, Y> series) {

	}

}
