package org.opentdk.gui.application;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;

import org.opentdk.gui.controls.ChoiceBox;
import org.opentdk.gui.controls.InputDialog;
import org.opentdk.gui.controls.MessageDialog;

import org.opentdk.api.logger.MLogger;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * This class gets used as extend for the main classes of JavaFX GUI
 * applications. It provides basic methods and properties that are required to
 * create and show GUI dialogues and components.
 * <p>
 * 
 * Sample usage:<br>
 * 
 * <pre>
 * public class MainApp extends BaseApplication { 
 *	
 *	private SampleController controller;
 *
 *	public static void main(String[] args) {
 *		launch();
 * 	}
 *
 *	&#64;Override
 *	protected void showRootLayout() {
 *		try {
 * 			controller = (SampleController) super.showStage("Sample.fxml", "Test", super.getPrimaryStage());
 *			controller.reload();
 *		} catch (IOException e) {
 *			MLogger.getInstance().log(Level.SEVERE, e);
 *			Platform.exit();
 *		}
 * 	}
 * </pre>
 * 
 * This would launch a simple window on the screen. The look is defined in the
 * Sample.fxml file that can be designed with the SceneBuilder application. The
 * controller class is in the same package than the FXML file and the sample
 * controller skeleton can be generated with SceneBuilder as well. The title of
 * the window is 'Test'.<br>
 * <br>
 * Advanced usage:<br>
 * <br>
 * 
 * @author LK Test Solutions
 *
 * @see javafx.application.Application
 */
public abstract class BaseApplication extends Application {
	/**
	 * Width of the applications main window in pixel.
	 */
	private double width = 1000;
	/**
	 * Height of the applications main window in pixel.
	 */
	private double height = 800;
	/**
	 * Horizontal position of the application in pixel.
	 */
	private double posX = 0;
	/**
	 * Vertical position of the application in pixel.
	 */
	private double posY = 0;
	/**
	 * CSS file to style the look of the application.
	 */
	private String styleSheet;
	/**
	 * Bundle object to access all internationalized string of the application.
	 */
	private ResourceBundle resourceBundle;
	/**
	 * Stage instance of the main layout of the application that gets initialized
	 * when the start method gets called. It is available as getter for the sub
	 * class and gets used to initialize the main controller class.
	 */
	private Stage primaryStage;
	/**
	 * Default choice box size that can be overwritten via
	 */
	private Insets choiceBoxSize = new Insets(20, 150, 10, 10);

	/**
	 * Force the sub class to load a root layout with the {@link #primaryStage} to
	 * have a main window appearing with the look of the FXML file that is assigned
	 * to the main controller class. <br>
	 * <br>
	 * Example content of the method:
	 * 
	 * <pre>
	 * private SampleController controller;
	 *
	 * public static void main(String[] arguments) {
	 *	launch();
	 * }
	 * 
	 * try {
	 * 	controller = (SampleController) super.showStage("Sample.fxml", "Test", super.getPrimaryStage());
	 * 	controller.reload();
	 * } catch (IOException e) {
	 * 	MLogger.getInstance().log(Level.SEVERE, e);
	 * 	Platform.exit();
	 * }
	 * </pre>
	 * 
	 * In this case the FXML file is in the same package (recommended) than the
	 * controller class and the title of the stage (window) is 'Test'.
	 */
	protected abstract void showRootLayout();

	/**
	 * Gets called after {@link #launch(String...)} was called by the sub class in
	 * the main method. The method is empty by default and is used here to
	 * initialize the <code>MLogger</code> class.
	 */
	public void init() throws Exception {
//		MLogger.getInstance().setLogFile(EBaseAppSettings.APP_LOGFILE.getValue());
//		MLogger.getInstance().setTraceLevel(Integer.valueOf(EBaseAppSettings.APP_TRACE_LEVEL.getValue()));
	}

	/**
	 * Gets called after {@link #init()} and is used to set the primary stage (with
	 * size properties) and implicitly call {@link #showRootLayout()}.
	 */
	public void start(Stage primaryStage) throws Exception {
		this.primaryStage = primaryStage;
		this.setStageProperties();
		this.primaryStage.setOnCloseRequest(event -> {
			this.close();
		});
		this.showRootLayout();
	}

	/**
	 * Call this method to close the primary stage, which closes the application and
	 * stops the JavaFX application thread.
	 */
	public void close() {
		this.primaryStage.close();
		Platform.exit();
	}

	/**
	 * Creates a new <code>ChoiceBox</code> instance with the apply button text
	 * 'dict.Apply' and the cancel button text 'dict.Cancel' that are defined in the
	 * properties file connected to the <code>ResourceBundle</code>. The size of the
	 * dialog has the default values of {@link #choiceBoxSize}.
	 * 
	 * @return new <code>ChoiceBox</code> instance
	 */
	public ChoiceBox getChoiceBox() {
		final String apply = this.resourceBundle.getString("dict.Apply");
		final String cancel = this.resourceBundle.getString("dict.Cancel");
		return new ChoiceBox(this.choiceBoxSize, apply, cancel);
	}

	/**
	 * Define the size of the four sides of the choice box.
	 * 
	 * @param size {@link #choiceBoxSize}
	 */
	public final void setChoiceBoxSize(Insets size) {
		if (size == null || size == Insets.EMPTY) {
			MLogger.getInstance().log(Level.SEVERE, "Insets are null or empty", this.getClass().getSimpleName(), "setChoiceBoxSize");
			return;
		}
		this.choiceBoxSize = size;
	}

	/**
	 * Returns an instance of the InputDialog object. InputDialog can be
	 * used to pass user input into the application.
	 */
	public InputDialog getInputDialog() {
		return new InputDialog();
	}

	/**
	 * Returns the instance of the MessageDialog object. MessageDialog is
	 * used to display any application message like error, warning, information etc.
	 * in a pop up dialog.
	 */
	public MessageDialog getMessageDialog() {
		return new MessageDialog(this.resourceBundle);
	}

	/**
	 * Returns the name and path of the style sheet (CSS file) which is used for the
	 * GUI design like colors, fonts
	 * 
	 * @return Instance of the rootLayoutController object
	 */
	public String getStyleSheet() {
		return styleSheet;
	}

	/**
	 * Assigns the full path and filename of the Cascaded Style Sheet with the GUI
	 * theme (colors, fonts etc.) to the styleSheet property. This CSS file controls
	 * representation of the GUI elements of this application.
	 * 
	 * @param css Full path and name of the CSS file
	 */
	public void setStyleSheet(String css) {
		if (css == null || css.isBlank() || css.length() > Short.MAX_VALUE || !css.endsWith(".css")) {
			MLogger.getInstance().log(Level.SEVERE, "Invalid parameter 'css'. No style sheet gets used", getClass().getSimpleName(), "setStyleSheet");
			return;
		}
		this.styleSheet = css;
	}

	/**
	 * @return {@link #resourceBundle}
	 */
	public ResourceBundle getBundle() {
		return this.resourceBundle;
	}

	/**
	 * Use this method to define which resource bundle should be used to store
	 * internationalized strings.<br>
	 * <br>
	 * Example:
	 * 
	 * <pre>
	 * setResourceBundle(ResourceBundle.getBundle("application.test.Bundle", new Locale("en")));
	 * </pre>
	 * 
	 * Points to a file named Bundle_en.properties that is located in the package
	 * application.test.<br>
	 * <br>
	 * 
	 * @param bundle {@link #resourceBundle}
	 */
	public void setResourceBundle(ResourceBundle bundle) {
		this.resourceBundle = bundle;
	}

	/**
	 * @return {@link #primaryStage}
	 */
	public Stage getPrimaryStage() {
		return primaryStage;
	}

	/**
	 * @return {@link #width}
	 */
	public double getWidth() {
		return width;
	}

	/**
	 * Set the width of the {@link #primaryStage}.
	 * 
	 * @param width A valid double value between 1 and Integer.MAX_VALUE.
	 */
	public void setWidth(double width) {
		if (width <= 0 || width > Integer.MAX_VALUE) {
			MLogger.getInstance().log(Level.SEVERE, "The comitted width is not in the range 1 to Integer.MAXVALUE ==> Use default " + this.width);
			return;
		}
		this.width = width;
	}

	/**
	 * @return {@link #height}
	 */
	public double getHeight() {
		return height;
	}

	/**
	 * Set the height of the {@link #primaryStage}.
	 * 
	 * @param height A valid double value between 1 and Integer.MAX_VALUE.
	 */
	public void setHeight(double height) {
		if (height <= 0 || height > Integer.MAX_VALUE) {
			MLogger.getInstance().log(Level.SEVERE, "The comitted height is not in the range 1 to Integer.MAXVALUE ==> Use default " + this.height);
			return;
		}
		this.height = height;
	}

	/**
	 * @return {@link #posX}
	 */
	public double getPosX() {
		return posX;
	}

	/**
	 * Set the horizontal position of the {@link #primaryStage}.
	 * 
	 * @param posX A valid double value between 1 and Integer.MAX_VALUE.
	 */
	public void setPosX(double posX) {
		if (posX <= 0 || posX > Integer.MAX_VALUE) {
			MLogger.getInstance().log(Level.SEVERE, "The comitted posX is not in the range 1 to Integer.MAXVALUE ==> Use default " + this.posX);
			return;
		}
		this.posX = posX;
	}

	/**
	 * @return {@link #posY}
	 */
	public double getPosY() {
		return posY;
	}

	/**
	 * Set the vertical position of the {@link #primaryStage}.
	 * 
	 * @param posY A valid double value between 1 and Integer.MAX_VALUE.
	 */
	public void setPosY(double posY) {
		if (posY <= 0 || posY > Integer.MAX_VALUE) {
			MLogger.getInstance().log(Level.SEVERE, "The comitted posY is not in the range 1 to Integer.MAXVALUE ==> Use default " + this.posY);
			return;
		}
		this.posY = posY;
	}

	/**
	 * {@link #showStage(String, String)}
	 */
	public BaseController showStage(String fxmlFile) throws IOException {
		return showStage(fxmlFile, "Application");
	}

	/**
	 * {@link #showStage(String, String, boolean)}
	 */
	public BaseController showStage(String fxmlFile, String title) throws IOException {
		return this.showStage(fxmlFile, title, false);
	}

	/**
	 * {@link #showStage(String, String, Stage)} Use the model option to define if
	 * the window can be used parallel to the origin window (true).
	 */
	public BaseController showStage(String fxmlFile, String title, boolean modal) throws IOException {
		Stage stage = new Stage();
		if (modal) {
			stage.initModality(Modality.APPLICATION_MODAL);
		}
		return this.showStage(fxmlFile, title, stage);
	}

	/**
	 * Show a new stage on the screen. For an example usage see
	 * {@link #BaseApplication()}.
	 * 
	 * @param fxmlFile The FXML file that defines the looks of the stage.
	 * @param title    Text on the top of the window.
	 * @param stage    The javafx.application.Stage object that should be used. If
	 *                 unknown use {@link #showStage(String, String)} to simply pass
	 *                 <code>new Stage()</code>.
	 * @return <code>BaseController</code> instance that controls the FXMl file.
	 * @throws IOException
	 */
	public BaseController showStage(String fxmlFile, String title, Stage stage) throws IOException {
		BaseController bc = null;
		Parent parent = null;
		FXMLLoader loader = new FXMLLoader();

		URL fxmlLocation = this.getClass().getResource(fxmlFile);
		if (fxmlLocation == null) {
			throw new MalformedURLException("URL instance for FXML is null. Either it does not exist or the reference to the controller class is wrong. The easiest way is to have the controller and the FXML in the same package and just pass the FXML file name ==> no stage will be loaded!");
		}
		loader.setLocation(fxmlLocation);

		if (this.resourceBundle != null) {
			loader.setResources(this.resourceBundle);
		}
		parent = loader.load();

		Scene scene = new Scene(parent);
		if (this.styleSheet != null) {
			URL cssLocation = this.getClass().getResource(this.styleSheet);
			if (cssLocation == null) {
				MLogger.getInstance().log(Level.WARNING, "Style sheet CSS file not found.");
			} else {
				scene.getStylesheets().add(this.getClass().getResource(this.styleSheet).toExternalForm());
			}
		}
		scene.setUserData(loader);
		scene.setFill(Color.TRANSPARENT);

		bc = loader.getController();

		stage.setScene(scene);
		stage.setTitle(title);
		stage.initStyle(StageStyle.DECORATED);
		bc.setStage(stage);
		stage.show();

		return bc;
	}

	/**
	 * Similar to {@link #showStage(String, String, Stage)} but used to embed
	 * container into an existing parent stage.
	 * 
	 * @param fxmlFile The FXML file that defines the looks of the container.
	 * @return <code>BaseController</code> instance that controls the FXMl file.
	 * @throws IOException
	 */
	public BaseController createComponent(String fxmlFile) throws IOException {
		BaseController bc = null;
		Parent parent = null;
		FXMLLoader loader = new FXMLLoader();

		URL fxmlLocation = this.getClass().getResource(fxmlFile);
		if (fxmlLocation == null) {
			throw new MalformedURLException("URL to FXML file could not be created ==> no stage will be loaded!");
		}
		loader.setLocation(fxmlLocation);

		if (this.resourceBundle != null) {
			loader.setResources(this.resourceBundle);
		}
		parent = loader.load();
		loader.setRoot(parent);
		bc = loader.getController();
		bc.setParent(loader.getRoot());
		return bc;
	}

	/**
	 * Set the following stage properties:
	 * 
	 * <pre>
	 * {@link #width}
	 * {@link #height}
	 * {@link #posX}
	 * {@link #posY}
	 * </pre>
	 * 
	 * There are default values for every property if it was not set in the
	 * {@link #init()} method.
	 */
	private void setStageProperties() {

		if (this.width < this.primaryStage.getMinWidth()) {
			this.width = this.primaryStage.getMinWidth();
		}
		this.primaryStage.setWidth(this.width);

		if (this.height < this.primaryStage.getMinHeight()) {
			this.height = this.primaryStage.getMinHeight();
		}
		this.primaryStage.setHeight(this.height);

		if (this.posX <= 0 && this.posY <= 0) {
			this.primaryStage.centerOnScreen();
		} else {
			this.primaryStage.setX(this.posX);
			this.primaryStage.setY(this.posY);
		}
	}

}
