package Tests.BaseApplication;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

import org.opentdk.gui.application.BaseApplication;
import org.opentdk.gui.controls.ChoiceBox.ApplyOption;
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
		List<String> options = new ArrayList<>();
		options.add("A");		
		String title = super.getBundle().getString("dialog.apply.title");
		String header = super.getBundle().getString("dialog.apply.header");
		Pair<ApplyOption, String> result = super.getChoiceBox().showChoiceBox(title, header, options);		
		BaseRegression.testResult(result.getValue(), "Selected item", "A");
		
		Optional<String> text = super.getInputDialog().showInputBox(title, header, "Type text", "Text");
		BaseRegression.testResult(text.get(), "Input text", "Text");
		
		super.getMessageDialog().showMessageBox(MessageType.INFORMATION, header, "Content");
	}

}
