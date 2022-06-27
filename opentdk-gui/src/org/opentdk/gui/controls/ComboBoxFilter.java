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
 * edited text
 * 
 * Call the create() method in another class like this: NAME_OF_COMBOBOX =
 * ComboBoxFilter.create(LIST_WITH_ALL_ELEMENTS, NAME_OF_COMBOBOX);
 * 
 * @author FME
 *
 */
public class ComboBoxFilter {
	/**
	 * When the create() method is called anywhere in the application the specific
	 * type will be set in ObjectPropertyConvert
	 */
	@SuppressWarnings("rawtypes")
//	public static <T> ComboBox<ObjectPropertyWrapper<T>> create(List<T> allItems, final ComboBox<ObjectPropertyWrapper<T>> cbo)
	public static <T> ComboBox<ObjectPropertyWrapper<T>> create(List<T> allItems,
			final ComboBox<ObjectPropertyWrapper<T>> cbo) {
		ObservableList<ObjectPropertyWrapper<T>> items = FXCollections
				.observableArrayList(item -> new Observable[] { item.filterProperty() });

		/*
		 * Make sure all elements of the items list have the specified object type. E.g.
		 * the COnstruktor gets called with type String - the raw type T will be set to
		 * string
		 */
		allItems.forEach(item -> {
			ObjectPropertyWrapper<T> cbof = new ObjectPropertyWrapper<>(item);
			items.add(cbof);
		});

		/*
		 * Filtered list for the items list that adds all items that fit to the filter
		 * criteria
		 */
		FilteredList<ObjectPropertyWrapper<T>> filterItems = new FilteredList<>(items, t -> !t.isFiltered());

		cbo.setItems(filterItems);

		cbo.setOnHidden(event -> items.forEach(item -> item.setFiltered(false)));

		/* The significant lines */
		cbo.getEditor().textProperty().addListener((obs, oldValue, newValue) -> {
			/* Make sure the items appear when editing a value into combo box text field */
//            if(!cbo.isShowing()) 
			if (!cbo.isShowing() && cbo.getEditor().isFocused()) {
				cbo.show();
				return;
			}

			Platform.runLater(() -> {
				/* Make sure that no item is selected when searching for other items */
				if (cbo.getSelectionModel().getSelectedItem() == null) {
					/*
					 * filter criteria -> if the value in the combo box text field is part of a
					 * value in the items list - the item will be added to the filtered list
					 */
					items.forEach(item -> item
							.setFiltered(!item.getObject().toString().toLowerCase().contains(newValue.toLowerCase())));
				} else {
					boolean validText = false;

					for (ObjectPropertyWrapper hideableItem : items) {
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
