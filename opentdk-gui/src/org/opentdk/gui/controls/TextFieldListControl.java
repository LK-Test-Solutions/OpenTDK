package org.opentdk.gui.controls;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextField;

/**
 * Get access to the list cell of a text field.
 */
@Deprecated
public class TextFieldListControl extends ListCell<String> {
	
	private TextField textField;

	@Override
	public void startEdit() {
		if (!isEditable() || !getListView().isEditable()) {
			return;
		}

		super.startEdit();

		if (isEditing()) {
			if (textField == null) {
				textField = new TextField(getItem());
			}

			textField.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					commitEdit(textField.getText());
				}
			});
		}

		textField.setPrefHeight(0);
		textField.setPrefWidth(0);
		textField.setPadding(new Insets(3.5));
		textField.setText(getItem());
		setText(null);
		setGraphic(textField);

	}

	@Override
	public void updateItem(String item, boolean empty) {
		super.updateItem(item, empty);

		if (isEmpty()) {
			setText(null);
			setGraphic(null);
		} else {
			if (!isEditing()) {
				if (textField != null) {
					setText(textField.getText());
				} else {
					setText(item);
				}
				setGraphic(null);
			}
		}
	}
}
