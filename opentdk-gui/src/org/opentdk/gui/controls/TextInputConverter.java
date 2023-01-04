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