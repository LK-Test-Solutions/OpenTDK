package org.opentdk.gui.controls;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 * Whenever the items of a control of javafx.scene.controls is in editing mode this class helps to 
 * display the items correctly. This is a typical JavaFX bean object.<br>
 * <br>
 * E.g. if a list view should be editable it gets initialized like this:
 * <pre>
 * {@literal ListView<ObjectPropertyWrapper<String>>} view = ...
 * </pre>
 * 
 * @author FME (LK Test Solutions)
 *
 * @param <T>
 */
public class ObjectPropertyWrapper<T> {

	private final ObjectProperty<T> object = new SimpleObjectProperty<>();
	private final BooleanProperty filtered = new SimpleBooleanProperty();

	public ObjectPropertyWrapper(T object) {
		setObject(object);
	}

	private ObjectProperty<T> objectProperty() {
		return this.object;
	}

	public T getObject() {
		return this.objectProperty().get();
	}

	public void setObject(T object) {
		this.objectProperty().set(object);
	}

	BooleanProperty filterProperty() {
		return this.filtered;
	}

	public boolean isFiltered() {
		return this.filterProperty().get();
	}

	public void setFiltered(boolean filter) {
		this.filterProperty().set(filter);
	}

	@Override
	public String toString() {
		return getObject() == null ? null : getObject().toString();
	}
}
