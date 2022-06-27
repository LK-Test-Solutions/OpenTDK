package org.opentdk.gui.controls;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;

@SuppressWarnings("javadoc")
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
