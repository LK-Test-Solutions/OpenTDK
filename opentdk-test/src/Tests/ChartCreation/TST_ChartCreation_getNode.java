package Tests.ChartCreation;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class TST_ChartCreation_getNode {

	public static void main(String[] args) {
		Rectangle rectangle = new Rectangle(10, 10, 10, 10);
		rectangle.setStroke(Color.TRANSPARENT);
		rectangle.setFill(Color.WHITE.deriveColor(1, 1, 1, 1));
		
		double xpref = rectangle.prefWidth(Double.NaN);
		double ypref = rectangle.prefHeight(Double.NaN);
		double xmin = rectangle.minWidth(Double.NaN);
		double ymin = rectangle.minHeight(Double.NaN);
		double xmax = rectangle.maxWidth(Double.NaN);
		double ymax = rectangle.maxHeight(Double.NaN);
		System.out.println("Rectangle x pref value: " + xpref);
		System.out.println("Rectangle y pref value: " + ypref);
		System.out.println("Rectangle x min value: " + xmin);
		System.out.println("Rectangle y min value: " + ymin);
		System.out.println("Rectangle x max value: " + xmax);
		System.out.println("Rectangle y max value: " + ymax);
	}
}
