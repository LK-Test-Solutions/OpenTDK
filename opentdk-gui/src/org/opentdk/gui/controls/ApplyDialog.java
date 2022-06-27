package org.opentdk.gui.controls;

import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;

/*
 * Dialog which allows to apply or skip an action, described by the content of the dialog.
 * 
 */
public class ApplyDialog {
	private ResourceBundle rscBundle;
	private ApplyOption applyRes;

	public ApplyDialog(ResourceBundle bundle) {
		rscBundle = bundle;
	}

	public enum ApplyType {
		NO_OPTIONS, ALL_OPTIONS,
	}

	public enum ApplyOption {
		ALL_SIMILAR, ALL_SELECTED, CURRENT, UNKNOWN
	}

	public Pair<ApplyOption, String> showApplyDialog(ApplyType at, String objName) {
		applyRes = ApplyOption.CURRENT;

		/* create instance of custom dialog */
		Dialog<Pair<ApplyOption, String>> dialog = new Dialog<>();
		dialog.setTitle(rscBundle.getString("dialog.apply.sourceVersion.title"));
		dialog.setHeaderText(rscBundle.getString("dialog.apply.sourceVersion.HeaderText") + ": \r\n" + objName);

		/* set icon */

		/* Set button types */
		ButtonType applyButtonType = new ButtonType(rscBundle.getString("dict.Apply"), ButtonData.OK_DONE);
		ButtonType skipButtonType = new ButtonType(rscBundle.getString("dict.Skip"), ButtonData.CANCEL_CLOSE);
		dialog.getDialogPane().getButtonTypes().addAll(skipButtonType, applyButtonType);

		/* Create version label and field */
		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(20, 150, 10, 10));

		TextField version = new TextField();
//		version.setPromptText(rscBundle.getString("dialog.apply.sourceVersion.label"));
		grid.add(new Label(rscBundle.getString("dict.Version") + ":"), 0, 0);
		grid.add(version, 1, 0);

		ToggleGroup tgr_Remember = new ToggleGroup();
		RadioButton rbtn_RememberAllSelected = new RadioButton();
		rbtn_RememberAllSelected.setText(rscBundle.getString("dialog.apply.sourceVersion.OptionSelected"));
		rbtn_RememberAllSelected.setToggleGroup(tgr_Remember);
		RadioButton rbtn_RememberAllSimilar = new RadioButton();
		rbtn_RememberAllSimilar.setText(rscBundle.getString("dialog.apply.sourceVersion.OptionAllDir"));
		rbtn_RememberAllSimilar.setToggleGroup(tgr_Remember);
		RadioButton rbtn_DontRemember = new RadioButton();
		rbtn_DontRemember.setText(rscBundle.getString("dialog.apply.sourceVersion.OptionCurrent"));
		rbtn_DontRemember.setToggleGroup(tgr_Remember);
		tgr_Remember.selectToggle(rbtn_DontRemember);

		if (at == ApplyType.ALL_OPTIONS) {
			grid.add(rbtn_RememberAllSimilar, 1, 1);
			grid.add(rbtn_RememberAllSelected, 1, 2);
			grid.add(rbtn_DontRemember, 1, 3);
		}

		/* Enable/Disable apply button depending on whether a version was entered. */
		Node applyButton = dialog.getDialogPane().lookupButton(applyButtonType);
		applyButton.setDisable(true);

		version.textProperty().addListener((observable, oldValue, newValue) -> {
			applyButton.setDisable(newValue.trim().isEmpty());
		});

		tgr_Remember.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
			@Override
			public void changed(ObservableValue<? extends Toggle> ov, Toggle toggle, Toggle new_toggle) {
				if (new_toggle == rbtn_DontRemember) {
					applyRes = ApplyOption.CURRENT;
				} else if (new_toggle == rbtn_RememberAllSelected) {
					applyRes = ApplyOption.ALL_SELECTED;
				} else if (new_toggle == rbtn_RememberAllSimilar) {
					applyRes = ApplyOption.ALL_SIMILAR;
				}
			}
		});

		dialog.getDialogPane().setContent(grid);

		/* Request focus on the version field by default. */
		Platform.runLater(() -> version.requestFocus());

		dialog.setResultConverter(dialogButton -> {
			if (dialogButton == applyButtonType) {
				return new Pair<>(applyRes, version.getText());
			}
			return null;
		});

		Optional<Pair<ApplyOption, String>> result = dialog.showAndWait();

		if (result.isPresent()) {
			return result.get();
		}
		return null;
	}

	public Pair<ApplyOption, String> showApplyDialog(ApplyType at, List<String> options, String message) {
		applyRes = ApplyOption.CURRENT;

		/* create instance of custom dialog */
		Dialog<Pair<ApplyOption, String>> dialog = new Dialog<>();
//		dialog.setTitle(rscBundle.getString("dialog.apply.sourceVersion.title"));
		dialog.setHeaderText(message);

		ButtonType applyButtonType = new ButtonType(rscBundle.getString("dict.Apply"), ButtonData.OK_DONE);
		ButtonType skipButtonType = new ButtonType(rscBundle.getString("dict.Skip"), ButtonData.CANCEL_CLOSE);
		ButtonType thirdOption = new ButtonType(rscBundle.getString("dict.CreateNew"), ButtonData.APPLY);
		dialog.getDialogPane().getButtonTypes().addAll(skipButtonType, applyButtonType, thirdOption);

		/* Create version label and field */
		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(20, 150, 10, 10));

		ComboBox<String> cbo = new ComboBox<>();
		cbo.getItems().addAll(options);
		cbo.setValue(options.get(0));
		grid.add(cbo, 1, 1);

		dialog.getDialogPane().setContent(grid);

		dialog.setResultConverter(dialogButton -> {
			if (dialogButton == applyButtonType)
				applyRes = ApplyOption.CURRENT;
			else if (dialogButton == skipButtonType)
				applyRes = ApplyOption.UNKNOWN;
			if (dialogButton == thirdOption)
				applyRes = ApplyOption.ALL_SIMILAR;

			return new Pair<>(applyRes, cbo.getSelectionModel().getSelectedItem());
		});

		Optional<Pair<ApplyOption, String>> result = dialog.showAndWait();

		if (result.isPresent()) {
			return result.get();
		}
		return null;
	}
}
