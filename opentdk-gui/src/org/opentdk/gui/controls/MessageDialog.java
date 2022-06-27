package org.opentdk.gui.controls;

import java.util.ResourceBundle;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;

/**
 * Standard messages boxes for information or warning issues. Prerequisites: An application specific bundle.properties file with the used strings e.g. "messagebox.type.Information = Information"
 *
 * Usage example: MessageDialog message = new MessageDialog(mainApp.getBundle()); message.showMessageBox(MessageType.WARNING, mainApp.getBundle().getString("messagebox.RequiredField.title"),
 * mainApp.getBundle().getString("messagebox.your.message"));
 *
 * @author LK Test Solutions GmbH
 *
 */
public class MessageDialog {
	private ResourceBundle rscBundle;

//	TODO Do not use TGR specific code and the preferences are not necessary to use this class
//	static Preferences prefs = Preferences.userRoot().node("/com/lk/tgr/settings/dialog");

	public MessageDialog(ResourceBundle bundle) {
		rscBundle = bundle;
	}

	public enum MessageType {
		INFORMATION, CONFIRMATION, CONFIRMATION_YES_NO, CONFIRMATION_SAVE_SAVEAS, CONFIRMATION_SAVE_NEW, CONFIRMATION_CHECK, WARNING, ERROR, NONE
	}

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
//				buttonTypeFour = new ButtonType(rscBundle.getString("dict.Cancel"), ButtonData.CANCEL_CLOSE);
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
//				alert.getDialogPane().set(selection);
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
			if (mt == MessageType.CONFIRMATION_CHECK) {
				@SuppressWarnings("unused")
				boolean sel = false;
				if (selection.isSelected())
					sel = true;
//				prefs.putBoolean("CheckBoxSelection", sel);
			}
			bt = ButtonType.CANCEL;
		}
		return bt;
	}

}
