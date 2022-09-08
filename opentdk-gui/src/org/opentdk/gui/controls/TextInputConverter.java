
package org.opentdk.gui.controls;

import javafx.scene.control.cell.TextFieldListCell;
import javafx.util.StringConverter;
import javafx.scene.control.Cell;

/**
 * Whenever the items of a control of javafx.scene.controls have editable
 * {@link javafx.scene.control.Cell} this class helps to display the text input
 * of the cells correctly.<br>
 * <br>
 * 
 * Example for a list cell of type text field:
 * 
 * <pre>
 * listview.setCellFactory(factory {@literal ->} {
 * 	TextFieldListCell{@literal <ObjectPropertyWrapper<String>>} cell = new TextFieldListCell{@literal <>}(); 
 * 	cell.setConverter(new TextInputConverter(cell));
 * 	return cell; 
 * });
 * </pre>
 * 
 * @author FME (LK Test Solutions)
 *
 */
public class TextInputConverter extends StringConverter<ObjectPropertyWrapper<String>> {

	/**
	 * The cell object where the text input should be converted.
	 */
	private final Cell<ObjectPropertyWrapper<String>> cell;

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