package org.opentdk.api.dispatcher;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.lang.reflect.Field;

import org.opentdk.api.application.EBaseSettings;
import org.opentdk.api.datastorage.DataContainer;
import org.opentdk.api.logger.MLogger;

/**
 * BaseDispatcher is build as a super class which can be inherited by subclasses to implement a list of variables,  
 * associated with any supported {@link org.opentdk.api.datastorage.DataContainer} classes.
 * This BaseDispatcher provides all methods to create the associations between a dispatcher class and a {@link org.opentdk.api.datastorage.DataContainer}.<br><br>
 * 
 * Settings class sample: <br>
 * <pre>
 * import org.opentdk.api.dispatcher.*;
 * public class ESampleSettings extends BaseDispatcher{
 * 	public static final BaseDispatchComponent MY_TITLE = new BaseDispatchComponent("Title", "/SampleSettings", "Sample Application");
 * 	public static final BaseDispatchComponent MY_HOMEPATH = new BaseDispatchComponent("HomePath", "/SampleSettings", "/temp/sample");
 * }
 * </pre>
 * 
 * @author LK Test Solutions
 *
 */
public class BaseDispatcher {

	/**
	 * Removes all the mappings from the DataContainer HashMap.<br>
	 * <br>
	 * {@link BaseDispatchComponent#clearDataContainer()}
	 */
	public static void clearDataContainer() {
		BaseDispatchComponent.clearDataContainer();
	}
	
	/**
	 * Returns a {@link org.opentdk.api.datastorage.DataContainer} that is assigned to the extended sub class of {@link BaseDispatcher},
	 * where the {@link BaseDispatchComponent} variables are declared.<br>
	 * <br>
	 * <pre>
	 * Usage samples:
	 * 1.) get DataContainer from dispatcher class which extends {@link BaseDispatcher} - no inherited {@link BaseDispatchComponent} declarations
	 * 	<code><b>ECustomSettings.getDataContainer(ECustomSettings.class.getSimpleName());</b></code>
	 * 2.) get DataContainer from dispatcher class which extends {@link EBaseSettings} - with inherited {@link BaseDispatchComponent} declarations
	 * 	<code><b>EChessAppSettings.getDataContainer(EBaseSettings.class.getSimpleName());</b></code>
	 * </pre>
	 * 
	 * @param dispatcherClassName The simple name of the dispatcher class where the {@link BaseDispatchComponent} variables are declared. In case the dispatcher class 
	 * 							  extends a super class with inherited declarations of {@link BaseDispatchComponent}, then the simple name of the super class needs to be used.
	 * 							  This will not effect the super class {@link #BaseDispatcher()}, because BaseDispatcher does not include declarations of {@link BaseDispatchComponent}.
	 * @return object of type {@link org.opentdk.api.datastorage.DataContainer} 
	 */
	public static DataContainer getDataContainer(String dispatcherClassName) {
		return BaseDispatchComponent.getDataContainer(dispatcherClassName);
	}

	/**
	 * Returns a {@link org.opentdk.api.datastorage.DataContainer} that is assigned to the extended subclass o {@link BaseDispatcher},
	 * where the {@link BaseDispatchComponent} variables are declared.<br>
	 * <br>
	 * <pre>
	 * Usage samples:
	 * 1.) get DataContainer from dispatcher class which extends {@link BaseDispatcher} - no inherited {@link BaseDispatchComponent} declarations
	 * 	<code><b>ECustomSettings.getDataContainer(ECustomSettings.class.getSimpleName());</b></code>
	 * 2.) get DataContainer from dispatcher class which extends {@link EBaseSettings} - with inherited {@link BaseDispatchComponent} declarations
	 * 	<code><b>EChessAppSettings.getDataContainer(EBaseSettings.class.getSimpleName());</b></code>
	 * </pre>
	 * 
	 * @param dispatcherClass - The dispatcher class where the {@link BaseDispatchComponent} variables are declared. In case the dispatcher class 
	 * 						  extends a super class with inherited declarations of {@link BaseDispatchComponent}, then the simple name of the super class needs to be used.
	 * 						  This will not effect the super class {@link #BaseDispatcher()}, because BaseDispatcher does not include declarations of {@link BaseDispatchComponent}.
	 * @return object of type {@link org.opentdk.api.datastorage.DataContainer} 
	 */
	public static DataContainer getDataContainer(Class<?> dispatcherClass) {
		return BaseDispatchComponent.getDataContainer(dispatcherClass.getSimpleName());
	}

	public static List<Field> getFields(Class<?> dispatcherClass){
		return Arrays.asList(dispatcherClass.getDeclaredFields());
	}
	
	/**
	 * Assigns a {@link org.opentdk.api.datastorage.DataContainer} to the {@link BaseDispatchComponent} variables that are declared in an  
	 * extended sub class of {@link BaseDispatcher}. The DataContainer acts as a runtime storage for the values of the {@link BaseDispatchComponent} 
	 * variables and is linked to the defined file which acts as the permanent storage for the {@link org.opentdk.api.datastorage.DataContainer}.
	 * 
	 * @param dispatcherClass - the dispatcher class where the variables of type {@link BaseDispatchComponent} are declared
	 * @param dispatcherFile - full path and name of the file that stores the values of {@link BaseDispatchComponent} 
	 * 						 variables which are defined within the dispatcher class
	 */
	public static void setDataContainer(Class<?> dispatcherClass, String dispatcherFile) {
		BaseDispatchComponent.setDataContainer(dispatcherClass.getSimpleName(), dispatcherFile);
	}

	/**
	 * Assigns a {@link org.opentdk.api.datastorage.DataContainer} to the {@link BaseDispatchComponent} variables that are declared in an  
	 * extended sub class of {@link BaseDispatcher}. The DataContainer acts as a runtime storage for the values of the {@link BaseDispatchComponent} 
	 * variables that is linked to the defined file, which acts as the permanent storage for the {@link org.opentdk.api.datastorage.DataContainer}.
	 * This method will also check if the root node of tree formatted file matches with the root node defined within the corresponding dispatcher class.
	 * 
	 * @param dispatcherClass - the dispatcher class where the variables of type {@link BaseDispatchComponent} are declared
	 * @param dispatcherFile - full path and name of the file that stores the values of {@link BaseDispatchComponent} 
	 * 						 variables which are defined within the dispatcher class
	 * @param rootNode - name of the root node within the tree formated file, used to check if the content of the file matches to the dispatcher class
	 * @throws IOException TODO
	 */
	public static void setDataContainer(Class<?> dispatcherClass, String dispatcherFile, String rootNode) throws IOException {
		BaseDispatchComponent.checkDispatcherFile(dispatcherFile, rootNode);
		BaseDispatchComponent.setDataContainer(dispatcherClass.getSimpleName(), dispatcherFile);
	}
	
	/**
	 * Assigns a {@link org.opentdk.api.datastorage.DataContainer} to the {@link BaseDispatchComponent} variables that are declared in an 
	 * extended sub class of {@link BaseDispatcher}. The DataContainer acts as a runtime storage for the values of the {@link BaseDispatchComponent} 
	 * variables.
	 * 
	 * @param dispatcherClass - the dispatcher class where the variables of type {@link BaseDispatchComponent} are declared
	 * @param dc - {@link org.opentdk.api.datastorage.DataContainer} instance, representing the runtime storage for values of the {@link BaseDispatchComponent} variables
	 */
	public static void setDataContainer(Class<?>  dispatcherClass, DataContainer dc) {
		BaseDispatchComponent.setDataContainer(dispatcherClass.getSimpleName(), dc);
	}

	/**
	 * Assigns a {@link org.opentdk.api.datastorage.DataContainer} to the {@link BaseDispatchComponent} variables that are declared in an 
	 * extended sub class of {@link BaseDispatcher}. The DataContainer acts as a runtime storage for the values of the {@link BaseDispatchComponent} 
	 * variables.
	 * This method will also check if the root node of tree formatted DataContainer matches with the root node defined within the corresponding dispatcher class.
	 * 
	 * @param dispatcherClass - the dispatcher class where the variables of type {@link BaseDispatchComponent} are declared
	 * @param dc - {@link org.opentdk.api.datastorage.DataContainer} instance, representing the runtime storage for values of the {@link BaseDispatchComponent} variables
	 * @param rootNode - name of the root node within the dispatcher class, used to check if the content of the DataContainer matches to the dispatcher class
	 */
	public static void setDataContainer(Class<?>  dispatcherClass, DataContainer dc, String rootNode) {
		BaseDispatchComponent.checkDispatcherFile(dc, rootNode);
		BaseDispatchComponent.setDataContainer(dispatcherClass.getSimpleName(), dc);
	}

	/**
	 * Assigns a {@link org.opentdk.api.datastorage.DataContainer} to the {@link BaseDispatchComponent} variables that are declared in an 
	 * extended sub class of {@link BaseDispatcher}. The DataContainer will be initialized with the contend of an InputStream and acts as 
	 * a runtime storage for the values of the {@link BaseDispatchComponent} 
	 * variables.
	 * 
	 * @param dispatcherClass - the dispatcher class where the variables of type {@link BaseDispatchComponent} are declared
	 * @param inStream - InputStream with text content that will be linked to the {@link BaseDispatchComponent} variables
	 */
	public static void setDataContainer(Class<?>  dispatcherClass, InputStream inStream) {
		BaseDispatchComponent.setDataContainer(dispatcherClass.getSimpleName(), inStream);
	}	

}
