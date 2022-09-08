package Tests.BaseApplication;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

import org.opentdk.gui.application.BaseApplication;
import org.opentdk.gui.controls.ChoiceBox.ApplyOption;
import org.opentdk.gui.controls.ChooserDialog;
import org.opentdk.gui.controls.ChooserDialog.DialogType;
import org.opentdk.gui.controls.ChooserDialog.ExtensionType;
import org.opentdk.gui.controls.MessageDialog.MessageType;

import RegressionTest.BaseRegression;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.Pair;

public class TST_BaseApplication_dialogs extends BaseApplication {
	
	public static void main(String[] args) {
		 launch();
	}
	
	@Override
	protected void showRootLayout() {
		Stage stage = super.getPrimaryStage();
		Scene scene = new Scene(new BorderPane());
		stage.setScene(scene);
		stage.setWidth(1000);
		stage.setHeight(600);
		stage.setOnCloseRequest(event -> {
			Platform.exit();
		});
		stage.show();
		
		super.setResourceBundle(ResourceBundle.getBundle("Tests.BaseApplication.Bundle", new Locale("en")));
		
		// CHOICE BOX
		List<String> options = new ArrayList<>();
		options.add("A");		
		String title = super.getBundle().getString("dialog.apply.title");
		String header = super.getBundle().getString("dialog.apply.header");
		Pair<ApplyOption, String> result = super.getChoiceBox().showChoiceBox(title, header, options);		
		BaseRegression.testResult(result.getValue(), "Selected item", "A");
		
		// INPUT BOX
		Optional<String> text = super.getInputDialog().showInputBox("Title", "Header", "Type text", "Text");
		BaseRegression.testResult(text.get(), "Input text", "Text");
		
		// MESSAGE DIALOG
		super.getMessageDialog().showMessageBox(MessageType.INFORMATION, "Header", "Content");
		
		// FILE CHOOSER
		List<File> selectedFiles = ChooserDialog.openFileChooser("Explorer", ExtensionType.FILE, DialogType.CHOOSE);
		System.out.println(selectedFiles.get(0));
		
		// FILE SAVER
		selectedFiles = ChooserDialog.openFileChooser("Explorer", ExtensionType.APP, DialogType.SAVE, "C:\\");
		System.out.println(selectedFiles.get(0));
		
		// DIR CHOOSER
		File dir = ChooserDialog.openDirectoryChooser("Explorer");
		System.out.println(dir);
	}

}
