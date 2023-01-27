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
