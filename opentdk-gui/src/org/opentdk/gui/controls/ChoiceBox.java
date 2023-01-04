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
import java.util.Optional;

import javafx.geometry.Insets;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;

/**
 * Dialog which allows to apply or skip an action, described by the content of
 * the dialog. The user has the possibility to choose a value of a combo box and
 * the result gets returned.<br>
 * <br>
 * 
 * Example:
 * 
 * <pre>
 * ChoiceBox box = new ChoiceBox(new Insets(20, 150, 10, 10), "OK", "Cancel");
 * List{@literal <}String{@literal >} options = new ArrayList<>();
 * options.add("A");
 * options.add("B");
 * Pair{@literal <}ApplyOption, String{@literal >} result = box.showChoiceBox("Title", "Info", options);
 * System.out.println(result.get());
 * </pre>
 * 
 * @author LK Test Solutions
 */
public class ChoiceBox {
	/**
	 * Size of the choice box dialog.
	 */
	private final Insets dialogSize;
	/**
	 * Text of the button that triggers the users OK action.
	 */
	private final String applyButtonText;
	/**
	 * Text of the button that triggers the users CANCEL action.
	 */
	private final String cancelButtonText;

	/**
	 * Create a new choice box instance.
	 * 
	 * @param size       {@link #dialogSize}
	 * @param applyText  {@link #applyButtonText}
	 * @param cancelText {@link #cancelButtonText}
	 */
	public ChoiceBox(Insets size, String applyText, String cancelText) {
		if (size == null || size == Insets.EMPTY) {
			throw new IllegalArgumentException("ChoiceBox: Insets object is null or empty");
		}
		if (applyText == null || applyText.strip().isEmpty()) {
			throw new IllegalArgumentException("ChoiceBox: Apply button text is null or blank");
		}
		if (cancelText == null || cancelText.strip().isEmpty()) {
			throw new IllegalArgumentException("ChoiceBox: Cancel button text is null or blank");
		}
		this.dialogSize = size;
		this.applyButtonText = applyText;
		this.cancelButtonText = cancelText;
	}

	/**
	 * The possibilities that the user has to continue in the apply dialog.
	 */
	public enum ApplyOption {
		APPLY, CANCEL;
	}

	/**
	 * Show a new apply dialog window. It gets closed after the user selection and
	 * button click.
	 * 
	 * @param title   title text of the dialog window
	 * @param header  content text of the dialog window
	 * @param options possibilities that the user has in the dialog in a combo box
	 * @param apply   text of the apply button e.g. 'OK'
	 * @param skip    text of the cancel button e.g. 'Cancel'
	 * @return javafx.util.Pair with the selected {@link #ApplyDialog()} as key and
	 *         the selected value as string
	 */
	public Pair<ApplyOption, String> showChoiceBox(String title, String header, List<String> options) {
		if (options == null || options.isEmpty()) {
			throw new IllegalArgumentException("showApplyDialog: No apply options defined");
		}
		if (title == null || title.strip().isEmpty()) {
			throw new IllegalArgumentException("showApplyDialog: Title is null or blank");
		}
		if (header == null || header.strip().isEmpty()) {
			throw new IllegalArgumentException("showApplyDialog: Header text is null or blank");
		}

		// Create dialog instance and add title and header description
		Dialog<Pair<ApplyOption, String>> dialog = new Dialog<>();
		dialog.setTitle(title);
		dialog.setHeaderText(header);

		// Add apply and cancel button
		ButtonType applyButtonType = new ButtonType(this.applyButtonText, ButtonData.OK_DONE);
		ButtonType skipButtonType = new ButtonType(this.cancelButtonText, ButtonData.CANCEL_CLOSE);
		dialog.getDialogPane().getButtonTypes().addAll(skipButtonType, applyButtonType);

		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(this.dialogSize);

		ComboBox<String> cbo = new ComboBox<>();
		cbo.getItems().addAll(options);
		cbo.setValue(options.get(0));
		cbo.prefWidthProperty().set(this.dialogSize.getRight());
		cbo.maxWidthProperty().set(this.dialogSize.getRight());
		grid.add(cbo, 1, 0);

		dialog.getDialogPane().setContent(grid);

		dialog.setResultConverter(dialogButton -> {
			ApplyOption applyRes = null;
			if (dialogButton == applyButtonType) {
				applyRes = ApplyOption.APPLY;
			} else if (dialogButton == skipButtonType) {
				applyRes = ApplyOption.CANCEL;
			}
			return new Pair<>(applyRes, cbo.getSelectionModel().getSelectedItem());
		});

		Optional<Pair<ApplyOption, String>> result = dialog.showAndWait();

		if (result.isPresent()) {
			return result.get();
		}
		return null;
	}
}
