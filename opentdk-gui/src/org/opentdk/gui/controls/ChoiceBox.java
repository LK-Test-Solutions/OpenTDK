package org.opentdk.gui.controls;

import java.util.Optional;

import javafx.collections.ObservableList;
import javafx.scene.control.ChoiceDialog;

/**
 * A dialog that waits for the users choice by using the {@link javafx.scene.control.ChoiceDialog}.
 * 
 * @author FME
 *
 */
public class ChoiceBox {

	/**
	 * Call this method to show a dialog with a combo box to choose a value from.
	 * 
	 * @param title        The title of the window.
	 * @param headerText   The title of the message.
	 * @param contentText  The message or instruction.
	 * @param choices      A list with the choice that should be shown.
	 * @param latestChoice The possibility to set the latest choice as default value if it occurs multiple times.
	 * @return The users input.
	 */
	public Optional<String> showChoiceBox(String title, String headerText, String contentText, ObservableList<String> choices, String latestChoice) {

		ChoiceDialog<String> dialog = new ChoiceDialog<String>(latestChoice, choices);

		dialog.setTitle(title);
		dialog.setHeaderText(headerText);
		dialog.setContentText(contentText);

		Optional<String> result = dialog.showAndWait();

		return result;
	}
}
