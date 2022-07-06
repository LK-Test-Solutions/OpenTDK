package Tests.ChartCreation;

import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

public class TST_ChartCreation_getFont {

	public static void main(String[] args) {
		Font font = Font.getDefault();
		System.out.println("Default font: " + font.toString());
		
		font = Font.font(20);
		System.out.println("Only size set: " + font.toString());
		
		font = Font.font("Verdana", FontWeight.BOLD, 20);
		System.out.println("Familiy, weight and size set: " + font.toString());
		
		font = Font.font("Arial", FontWeight.BOLD, FontPosture.ITALIC, 16);
		System.out.println("All set: " + font.toString());
//		System.out.println("Family: " + font.getFamily());
//		System.out.println("Size: " + font.getSize());
//		System.out.println("Style: " + font.getStyle());
//		System.out.println("Weight: " + "==> Bold, too, but there is only the getStyle() - FontWeight + FontPosture");
		
		System.out.println("Available font names: " + Font.getFontNames());
		System.out.println("Available family names: " + Font.getFamilies());
	}

}
