package org.opentdk.gui.controls;

import javafx.scene.control.cell.TextFieldListCell;
import javafx.util.StringConverter;
import javafx.scene.control.Cell;

/**
 * Use this class to convert between the text input of a
 * {@link javafx.scene.control.Cell} and any other type supported by
 * {@link org.opentdk.gui.controls.ObjectPropertyWrapper}. This has to be done
 * once at initialization of the cell factory of the object that contains
 * cells.<br>
 * <br>
 * 
 * Example for a list cell with text fields:<br>
 * <br>
 * lstv.setCellFactory(factory {@literal ->} {<br>
 * TextFieldListCell{@literal <ObjectPropertyWrapper<String>>} cell = new<br>
 * TextFieldListCell{@literal <>}(); cell.setConverter(new TextInputConverter(cell));<br>
 * return cell; <br>
 * });
 * 
 * 
 * @author FME (LK Test Solutions GmbH)
 *
 */
public class TextInputConverter extends StringConverter<ObjectPropertyWrapper<String>> {

	/**
	 * The cell object where the text input should be converted.
	 */
	private Cell<ObjectPropertyWrapper<String>> cell;

	/**
	 * Entry point to use this class.
	 * 
	 * @param cell The cell object where the text input should be converted.
	 */
	public TextInputConverter(TextFieldListCell<ObjectPropertyWrapper<String>> cell) {
		this.cell = cell;
	}

	@Override
	public String toString(ObjectPropertyWrapper<String> object) {
		return object.toString();
	}

	@Override
	public ObjectPropertyWrapper<String> fromString(String string) {
		ObjectPropertyWrapper<String> object = cell.getItem();
		object.setObject(string);
		return object;
	}
}