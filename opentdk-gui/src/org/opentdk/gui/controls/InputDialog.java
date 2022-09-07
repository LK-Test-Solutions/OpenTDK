package org.opentdk.gui.controls;

import java.util.Optional;

import javafx.scene.control.TextInputDialog;

/**
 * A dialog that waits for the users text input by using the {@link javafx.scene.control.TextInputDialog}.
 * 
 * @author LK Test Solutions GmbH
 *
 */
public class InputDialog {

	/**
	 * Call this method to show a dialog with a text field to type into.
	 * 
	 * @param title       The title of the window.
	 * @param headerText  The title of the message.
	 * @param contentText The message or instruction.
	 * @param value       The default value of the text field.
	 * @return The users input as string
	 */
	public Optional<String> showInputBox(String title, String headerText, String contentText, String value) {

		TextInputDialog dialog = new TextInputDialog(value);
		
		dialog.setTitle(title);
		dialog.setHeaderText(headerText);
		dialog.setContentText(contentText);

		return dialog.showAndWait();
	}
}
