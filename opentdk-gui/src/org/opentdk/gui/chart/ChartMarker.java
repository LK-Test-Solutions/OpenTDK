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

import javafx.scene.layout.Pane;
import javafx.scene.shape.Shape;

/**
 * This class gets used to store marker information for the chart creation. The
 * <code>ChartMarker.marker(..)</code> methods simply create a new marker to use
 * it for further operations. A marker can be every JavaFX Shape or a Pane with
 * Shape children to display additional information in the chart that is not
 * connected to the chart data.
 * 
 * @author FME (LK Test Solutions)
 *
 */
public final class ChartMarker {

	private final Pane container;
	private final Shape[] children;
	private final Shape marker;
	private final DataPoint dataPoint;
	private final double viewOrder;
	private final int belongingAxis;

	private ChartMarker(Shape marker, DataPoint dataPoint) {
		this(marker, dataPoint, 0.0);
	}

	private ChartMarker(Shape marker, DataPoint dataPoint, double viewOrder) {
		this(marker, dataPoint, viewOrder, 0);
	}

	private ChartMarker(Shape marker, DataPoint dataPoint, int belongingAxis) {
		this(marker, dataPoint, 0.0, belongingAxis);
	}

	private ChartMarker(Shape marker, DataPoint dataPoint, double viewOrder, int belongingAxis) {
		if (marker == null) {
			throw new NullPointerException("Shape object is null when constructing the chart marker");
		}
		if (dataPoint == null) {
			throw new NullPointerException("DataPoint object is null when constructing the chart marker");
		}
		if (Double.isNaN(viewOrder) || Double.isInfinite(viewOrder)) {
			throw new IllegalArgumentException("Value overflow (viewOrder) when constructing the chart marker");
		}
		if (belongingAxis < Integer.MIN_VALUE || belongingAxis > Integer.MAX_VALUE) {
			throw new IllegalArgumentException("Value overflow (belongingAxis) when constructing the chart marker");
		}
		this.container = null;
		this.children = new Shape[0];
		this.marker = marker;
		this.dataPoint = dataPoint;
		this.viewOrder = viewOrder;
		this.belongingAxis = belongingAxis;
	}

	private ChartMarker(Pane container, Shape[] children, DataPoint dataPoint) {
		this(container, children, dataPoint, 0.0);
	}

	private ChartMarker(Pane container, Shape[] children, DataPoint dataPoint, int belongingAxis) {
		this(container, children, dataPoint, 0.0, belongingAxis);
	}

	private ChartMarker(Pane container, Shape[] children, DataPoint dataPoint, double viewOrder) {
		this(container, children, dataPoint, viewOrder, 0);
	}

	private ChartMarker(Pane container, Shape[] children, DataPoint dataPoint, double viewOrder, int belongingAxis) {
		if (container == null) {
			throw new NullPointerException("Pane object is null when constructing the chart marker");
		}
		if (children == null) {
			throw new NullPointerException("Shape object is null when constructing the chart marker");
		}
		if (dataPoint == null) {
			throw new NullPointerException("DataPoint object is null when constructing the chart marker");
		}
		if (Double.isNaN(viewOrder) || Double.isInfinite(viewOrder)) {
			throw new IllegalArgumentException("Value overflow (viewOrder) when constructing the chart marker");
		}
		if (belongingAxis < Integer.MIN_VALUE || belongingAxis > Integer.MAX_VALUE) {
			throw new IllegalArgumentException("Value overflow (belongingAxis) when constructing the chart marker");
		}
		this.container = container;
		this.children = children;
		this.marker = null;
		this.dataPoint = dataPoint;
		this.container.getChildren().addAll(children);
		this.viewOrder = viewOrder;
		this.belongingAxis = belongingAxis;
	}

	public static ChartMarker marker(Shape marker, DataPoint dataPoint) {
		return new ChartMarker(marker, dataPoint);
	}

	public static ChartMarker marker(Shape marker, DataPoint dataPoint, double viewOrder) {
		return new ChartMarker(marker, dataPoint, viewOrder);
	}

	public static ChartMarker marker(Shape marker, DataPoint dataPoint, int belongingAxis) {
		return new ChartMarker(marker, dataPoint, belongingAxis);
	}

	public static ChartMarker marker(Shape marker, DataPoint dataPoint, double viewOrder, int belongingAxis) {
		return new ChartMarker(marker, dataPoint, viewOrder, belongingAxis);
	}

	public static ChartMarker marker(Pane container, Shape[] children, DataPoint dataPoint) {
		return new ChartMarker(container, children, dataPoint);
	}

	public static ChartMarker marker(Pane container, Shape[] children, DataPoint dataPoint, int belongingAxis) {
		return new ChartMarker(container, children, dataPoint, belongingAxis);
	}

	public static ChartMarker marker(Pane container, Shape[] children, DataPoint dataPoint, double viewOrder) {
		return new ChartMarker(container, children, dataPoint, viewOrder);
	}

	public static ChartMarker marker(Pane container, Shape[] children, DataPoint dataPoint, double viewOrder, int belongingAxis) {
		return new ChartMarker(container, children, dataPoint, viewOrder, belongingAxis);
	}

	public final Pane getContainer() {
		return container;
	}

	public final Shape[] getChildren() {
		return children;
	}

	public final Shape getMarker() {
		return marker;
	}

	public final DataPoint getDataPoint() {
		return dataPoint;
	}

	public final double getViewOrder() {
		return viewOrder;
	}

	public final int getBelongingAxis() {
		return belongingAxis;
	}

	public static final class DataPoint {

		private final String xsValue;
		private final String ysValue;
		private final Number xnValue;
		private final Number ynValue;

		private DataPoint(String x, String y) {
			if (x == null || y == null) {
				throw new NullPointerException("DataPoint x or y value is null");
			}
			if (x.length() > Integer.MAX_VALUE || y.length() > Integer.MAX_VALUE) {
				throw new IllegalArgumentException("DataPoint x or y value is out of range");
			}
			xsValue = x;
			ysValue = y;
			xnValue = -1;
			ynValue = -1;
		}

		private DataPoint(String x, Number y) {
			if (x == null || y == null) {
				throw new NullPointerException("DataPoint x or y value is null");
			}
			if (x.length() > Integer.MAX_VALUE || y.longValue() > Long.MAX_VALUE || y.longValue() < Long.MIN_VALUE || Double.isNaN(y.doubleValue()) || Double.isInfinite(y.doubleValue())) {
				throw new IllegalArgumentException("DataPoint x or y value is out of range");
			}
			xsValue = x;
			ysValue = null;
			xnValue = -1;
			ynValue = y;
		}

		private DataPoint(Number x, String y) {
			if (x == null || y == null) {
				throw new NullPointerException("DataPoint x or y value is null");
			}
			if (y.length() > Integer.MAX_VALUE || x.longValue() > Long.MAX_VALUE || x.longValue() < Long.MIN_VALUE || Double.isNaN(x.doubleValue()) || Double.isInfinite(x.doubleValue())) {
				throw new IllegalArgumentException("DataPoint x or y value is out of range");
			}
			xsValue = null;
			ysValue = y;
			xnValue = -1;
			ynValue = -1;
		}

		private DataPoint(Number x, Number y) {
			if (x == null || y == null) {
				throw new NullPointerException("DataPoint x or y value is null");
			}
			if (x.longValue() > Long.MAX_VALUE || x.longValue() < Long.MIN_VALUE || y.longValue() > Long.MAX_VALUE || y.longValue() < Long.MIN_VALUE || Double.isNaN(x.doubleValue()) || Double.isInfinite(x.doubleValue()) || Double.isNaN(y.doubleValue()) || Double.isInfinite(y.doubleValue())) {
				throw new IllegalArgumentException("DataPoint x or y value is out of range");
			}
			xsValue = null;
			ysValue = null;
			xnValue = x;
			ynValue = y;
		}

		public static DataPoint point(String x, String y) {
			return new DataPoint(x, y);
		}

		public static DataPoint point(String x, Number y) {
			return new DataPoint(x, y);
		}

		public static DataPoint point(Number x, String y) {
			return new DataPoint(x, y);
		}

		public static DataPoint point(Number x, Number y) {
			return new DataPoint(x, y);
		}

		public final String getXsValue() {
			return xsValue;
		}

		public final String getYsValue() {
			return ysValue;
		}

		public final Number getXnValue() {
			return xnValue;
		}

		public final Number getYnValue() {
			return ynValue;
		}

	}

}
