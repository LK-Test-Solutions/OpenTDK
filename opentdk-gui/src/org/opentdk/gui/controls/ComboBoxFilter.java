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

import java.util.List;

import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.ComboBox;

/**
 * JavaFX helper class to allow to filter the combo box items depending on the
 * edited text.
 * 
 * Call the create() method in another class like this: NAME_OF_COMBOBOX =
 * ComboBoxFilter.create(LIST_WITH_ALL_ELEMENTS, NAME_OF_COMBOBOX);
 * 
 * @author FME (LK Test Solutions)
 *
 */
public class ComboBoxFilter {
	/**
	 * Call this method when combo box items need to be filtered in the application.
	 * @param allItems The current combo box items without any filter
	 * @param cbo the combo box object
	 * @return the combo box with filtered values
	 */
	public static <T> ComboBox<ObjectPropertyWrapper<T>> create(List<T> allItems, final ComboBox<ObjectPropertyWrapper<T>> cbo) {
		ObservableList<ObjectPropertyWrapper<T>> items = FXCollections.observableArrayList(item -> new Observable[] { item.filterProperty() });

		// Every raw type in the allItems list get wrapped to a valid type
		allItems.forEach(item -> {
			ObjectPropertyWrapper<T> cbof = new ObjectPropertyWrapper<>(item);
			items.add(cbof);
		});

		// Show the items that match the filter criteria
		FilteredList<ObjectPropertyWrapper<T>> filterItems = new FilteredList<>(items, t -> !t.isFiltered());
		cbo.setItems(filterItems);
		
		// Hide the items that do not match the filter criteria
		cbo.setOnHidden(event -> items.forEach(item -> item.setFiltered(false)));

		// Listener that calls the setFiltered method for every item that matches the filter criteria
		cbo.getEditor().textProperty().addListener((obs, oldValue, newValue) -> {
			// Make sure the items appear when editing a value into combo box text field 
			if (!cbo.isShowing() && cbo.getEditor().isFocused()) {
				cbo.show();
				return;
			}

			Platform.runLater(() -> {
				// Make sure that no item is selected when searching for other items 
				if (cbo.getSelectionModel().getSelectedItem() == null) {

					// Set every item to filtered if it matches the filter criteria ==> isFiltered can then detect it above
					items.forEach(item -> item.setFiltered(!item.getObject().toString().toLowerCase().contains(newValue.toLowerCase())));
				} else {
					boolean validText = false;

					for (ObjectPropertyWrapper<T> hideableItem : items) {
						if (hideableItem.getObject().toString().equals(newValue)) {
							validText = true;
							break;
						}
					}
					if (!validText) {
						cbo.getSelectionModel().select(null);
					}
				}
			});
		});
		return cbo;
	}
}
