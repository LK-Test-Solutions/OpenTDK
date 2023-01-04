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

import java.util.ResourceBundle;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;

/**
 * Standard message box for information or warning issues. Prerequisites: An
 * application specific bundle.properties file with the used strings e.g.
 * "messagebox.type.Information = Information".
 * 
 * @author LK Test Solutions
 *
 */
public class MessageDialog {
	/**
	 * Object that stores all internationalized strings that are used in the
	 * application.
	 */
	private ResourceBundle rscBundle;

	/**
	 * Create a new <code>MessageDialog</code>.
	 * 
	 * @param bundle {@link #rscBundle}
	 */
	public MessageDialog(ResourceBundle bundle) {
		rscBundle = bundle;
	}

	/**
	 * All available designs of the <code>MessageDialog</code>.
	 */
	public enum MessageType {
		INFORMATION, CONFIRMATION, CONFIRMATION_YES_NO, CONFIRMATION_SAVE_SAVEAS, CONFIRMATION_SAVE_NEW, CONFIRMATION_CHECK, WARNING, ERROR, NONE
	}

	/**
	 * Let the message box appear.
	 * 
	 * @param mt          one of the message types defined in
	 *                    <code>MessageType</code>
	 * @param headerText  text in the header area of the dialog
	 * @param contentText text in the body area of the dialog
	 * @return selected button if the user selection has to be analyzed
	 */
	public ButtonType showMessageBox(MessageType mt, String headerText, String contentText) {
		Alert alert;
		ButtonType bt = new ButtonType("");
		ButtonType buttonTypeOne = null;
		ButtonType buttonTypeTwo = null;
		ButtonType buttonTypeThree = null;
		ButtonType buttonTypeFour = null;
		CheckBox selection = new CheckBox();

		switch (mt) {
		case INFORMATION:
			alert = new Alert(AlertType.INFORMATION);
			alert.setTitle(rscBundle.getString("dict.Information"));
			break;
		case CONFIRMATION:
			alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle(rscBundle.getString("dict.Confirmation"));
			break;
		case CONFIRMATION_YES_NO:
			alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle(rscBundle.getString("dict.Confirmation"));
			buttonTypeOne = new ButtonType(rscBundle.getString("dict.Yes"));
			buttonTypeTwo = new ButtonType(rscBundle.getString("dict.No"));
			alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo);
			break;
		case CONFIRMATION_SAVE_SAVEAS:
			alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle(rscBundle.getString("dict.Confirmation"));
			buttonTypeOne = new ButtonType(rscBundle.getString("dict.Save"));
			buttonTypeTwo = new ButtonType(rscBundle.getString("text.SaveAs"));
			buttonTypeFour = new ButtonType(rscBundle.getString("dict.Cancel"), ButtonData.CANCEL_CLOSE);
			alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo, buttonTypeThree);
			break;
		case CONFIRMATION_SAVE_NEW:
			alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle(rscBundle.getString("dict.Confirmation"));
			buttonTypeOne = new ButtonType(rscBundle.getString("dict.Save"));
			buttonTypeTwo = new ButtonType(rscBundle.getString("text.CreateNew"));
			buttonTypeThree = new ButtonType(rscBundle.getString("dict.Dismiss"));
			buttonTypeFour = new ButtonType(rscBundle.getString("dict.Cancel"), ButtonData.CANCEL_CLOSE);
			alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo, buttonTypeThree, buttonTypeFour);
			break;
		case CONFIRMATION_CHECK:
			alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle(rscBundle.getString("dict.Confirmation"));
			selection.setText(rscBundle.getString("dialog.apply.checkbox.text"));
			alert.getDialogPane().setContent(selection);
			buttonTypeFour = new ButtonType(rscBundle.getString("dict.Apply"), ButtonData.OK_DONE);
			alert.getButtonTypes().setAll(buttonTypeFour);
			break;
		case ERROR:
			alert = new Alert(AlertType.ERROR);
			alert.setTitle(rscBundle.getString("dict.Error"));
			break;
		case WARNING:
			alert = new Alert(AlertType.WARNING);
			alert.setTitle(rscBundle.getString("dict.Warning"));
			break;
		case NONE:
			alert = new Alert(AlertType.NONE);
			alert.setTitle(rscBundle.getString("dict.None"));
			break;
		default:
			alert = new Alert(AlertType.NONE);
			alert.setTitle(rscBundle.getString("dict.Unknown"));
			break;
		}

		alert.setHeaderText(headerText);
		alert.setContentText(contentText);
		alert.showAndWait();

		bt = alert.getResult();
		if (alert.getResult() == buttonTypeOne) {
			bt = ButtonType.YES;
		} else if (alert.getResult() == buttonTypeTwo) {
			bt = ButtonType.NO;
		} else if (alert.getResult() == buttonTypeThree) {
			bt = ButtonType.FINISH;
		} else if (alert.getResult() == buttonTypeFour) {
			bt = ButtonType.CANCEL;
		}
		return bt;
	}

}
