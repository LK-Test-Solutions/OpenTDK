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
package org.opentdk.api.dispatcher;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.lang.reflect.Field;

import org.apache.commons.lang3.StringUtils;
import org.opentdk.api.datastorage.DataContainer;
import org.opentdk.api.logger.MLogger;

/**
 * The BaseDispatcher provides all methods to create the associations between a dispatcher class and
 * a {@link org.opentdk.api.datastorage.DataContainer}.<br>
 * <br>
 * 
 * Dispatcher class sample: <br>
 * <br>
 * 
 * <pre>
 * import org.opentdk.api.dispatcher.*;
 * 
 * public class ESampleSettings {
 * 	public static final BaseDispatchComponent MY_TITLE = new BaseDispatchComponent("Title", "/SampleSettings", "Sample Application");
 * 	public static final BaseDispatchComponent MY_HOMEPATH = new BaseDispatchComponent("HomePath", "/SampleSettings", "/temp/sample");
 * }
 * </pre>
 * 
 * This way the ESampleSettings has the methods to get and set the DataContainer instance that
 * should be used for data storage in the {@link #dcMap}.<br>
 * <br>
 * 
 * @author LK Test Solutions
 *
 */
public abstract class BaseDispatcher {

	/**
	 * This HashMap stores the {@link org.opentdk.api.datastorage.DataContainer} instances of all
	 * settings used within an application during runtime. Once the setDataContainer method of a
	 * settings class is called, the DataContainer will be stored in the HashMap with the simple name of
	 * the settings class as key.
	 */
	static Map<String, DataContainer> dcMap = new HashMap<String, DataContainer>();

	/**
	 * @param dispatcherClass The dispatcher class where the {@link BaseDispatchComponent} variables are
	 *                        declared. In case the dispatcher class extends a super class with
	 *                        inherited declarations of {@link BaseDispatchComponent}, then the simple
	 *                        name of the super class needs to be used. This will not effect the super
	 *                        class {@link #BaseDispatcher()}, because BaseDispatcher does not include
	 *                        declarations of {@link BaseDispatchComponent}.
	 * @return a {@link org.opentdk.api.datastorage.DataContainer} that is assigned to the extended sub
	 *         class of {@link BaseDispatcher}, where the {@link BaseDispatchComponent} variables are
	 *         declared.
	 */
	public static DataContainer getDataContainer(Class<?> dispatcherClass) {
		return getDataContainer(dispatcherClass.getSimpleName());
	}

	/**
	 * @param dispatcherClassName The simple name of the dispatcher class where the
	 *                            {@link BaseDispatchComponent} variables are declared. In case the
	 *                            dispatcher class extends a super class with inherited declarations of
	 *                            {@link BaseDispatchComponent}, then the simple name of the super class
	 *                            needs to be used. This will not effect the super class
	 *                            {@link #BaseDispatcher()}, because BaseDispatcher does not include
	 *                            declarations of {@link BaseDispatchComponent}.
	 * @return a {@link org.opentdk.api.datastorage.DataContainer} that is assigned to the extended sub
	 *         class of {@link BaseDispatcher}, where the {@link BaseDispatchComponent} variables are
	 *         declared.
	 */
	public static DataContainer getDataContainer(String dispatcherClassName) {
		return dcMap.get(dispatcherClassName);
	}

	/**
	 * Assigns a {@link org.opentdk.api.datastorage.DataContainer} to the {@link BaseDispatchComponent}
	 * fields that are declared in an extended sub class of {@link BaseDispatcher}. The DataContainer
	 * acts as a runtime storage for the values of the {@link BaseDispatchComponent} variables.
	 * 
	 * @param dispatcherClass the dispatcher class where the variables of type
	 *                        {@link BaseDispatchComponent} are declared
	 * @param dc              {@link org.opentdk.api.datastorage.DataContainer} instance, representing
	 *                        the runtime storage for values of the {@link BaseDispatchComponent}
	 *                        variables
	 */
	public static void setDataContainer(Class<?> dispatcherClass, DataContainer dc) {
		setDataContainer(dispatcherClass, dc, false);
	}

	/**
	 * Assigns a {@link org.opentdk.api.datastorage.DataContainer} to the {@link BaseDispatchComponent}
	 * fields that are declared in an extended sub class of {@link BaseDispatcher}. The DataContainer
	 * will be initialized with the contend of an InputStream and acts as a runtime storage for the
	 * values of the {@link BaseDispatchComponent} variables.
	 * 
	 * @param dispatcherClass the dispatcher class where the variables of type
	 *                        {@link BaseDispatchComponent} are declared
	 * @param inStream        InputStream with text content that will be linked to the
	 *                        {@link BaseDispatchComponent} variables
	 */
	public static void setDataContainer(Class<?> dispatcherClass, InputStream inStream) {
		setDataContainer(dispatcherClass, new DataContainer(inStream), false);
	}

	/**
	 * Assigns a {@link org.opentdk.api.datastorage.DataContainer} to the {@link BaseDispatchComponent}
	 * fields that are declared in an extended sub class of {@link BaseDispatcher}. The DataContainer
	 * acts as a runtime storage for the values of the {@link BaseDispatchComponent} variables and is
	 * linked to the defined file which acts as the permanent storage for the
	 * {@link org.opentdk.api.datastorage.DataContainer}.
	 * 
	 * @param dispatcherClass the dispatcher class where the variables of type
	 *                        {@link BaseDispatchComponent} are declared
	 * @param dispatcherFile  full path and name of the file that stores the values of
	 *                        {@link BaseDispatchComponent} variables which are defined within the
	 *                        dispatcher class
	 */
	public static void setDataContainer(Class<?> dispatcherClass, String dispatcherFile) {
		setDataContainer(dispatcherClass, new DataContainer(new File(dispatcherFile)), false);
	}

	/**
	 * Assigns a {@link org.opentdk.api.datastorage.DataContainer} to the {@link BaseDispatchComponent}
	 * fields that are declared in an extended sub class of {@link BaseDispatcher}. The DataContainer
	 * acts as a runtime storage for the values of the {@link BaseDispatchComponent} variables that is
	 * linked to the defined file, which acts as the permanent storage for the
	 * {@link org.opentdk.api.datastorage.DataContainer}. This method will also check if the root node
	 * of tree formatted file matches with the root node defined within the corresponding dispatcher
	 * class.
	 * 
	 * @param dispatcherClass the dispatcher class where the variables of type
	 *                        {@link BaseDispatchComponent} are declared
	 * @param dispatcherFile  full path and name of the file that stores the values of
	 *                        {@link BaseDispatchComponent} variables which are defined within the
	 *                        dispatcher class
	 * @param createFile      defines if the non existent dispatcherFile gets created
	 */
	public static void setDataContainer(Class<?> dispatcherClass, String dispatcherFile, boolean createFile) {
		setDataContainer(dispatcherClass, new DataContainer(new File(dispatcherFile)), createFile);
	}	

	/**
	 * Assigns a {@link org.opentdk.api.datastorage.DataContainer} to the {@link BaseDispatchComponent}
	 * fields that are declared in an extended sub class of {@link BaseDispatcher}. The DataContainer
	 * acts as a runtime storage for the values of the {@link BaseDispatchComponent} variables. This
	 * method will also check if the root node of tree formatted DataContainer matches with the root
	 * node defined within the corresponding dispatcher class.
	 * 
	 * @param dispatcherClass the dispatcher class where the variables of type
	 *                        {@link BaseDispatchComponent} are declared
	 * @param dc              {@link org.opentdk.api.datastorage.DataContainer} instance, representing
	 *                        the runtime storage for values of the {@link BaseDispatchComponent}
	 *                        variables
	 * @param createFile      defines if the non existent dispatcherFile gets created
	 */
	public static void setDataContainer(Class<?> dispatcherClass, DataContainer dc, boolean createFile) {
		Map<String, BaseDispatchComponent> dcomp = BaseDispatcher.getDeclaredComponents(dispatcherClass);
		String rn = "";
		for (String key : dcomp.keySet()) {
			rn = dcomp.get(key).getRootNode();
			if (!rn.isEmpty()) {
				break;
			}
		}
		if (createFile) {
			if (dc.isTree()) {
				if (StringUtils.isBlank(dc.getRootNode())) {
					dc.setRootNode(rn);
				} else {
					if (!dc.getRootNode().contentEquals(rn)) {
						throw new IllegalArgumentException("Root node of dispatcher class and data container are not equal");
					}
				}
			}
			try {
				if(dc.getInputFile().exists() == false) {
					dc.createFile();
				}
			} catch (IOException e) {
				MLogger.getInstance().log(Level.SEVERE, e);
			}
		}
		dcMap.put(dispatcherClass.getSimpleName(), dc);
	}

	/**
	 * @param dispatcherClass the dispatcher class where the variables of type
	 *                        {@link BaseDispatchComponent} are declared
	 * @return the field instances as list of the committed class via reflection
	 */
	public static List<Field> getFields(Class<?> dispatcherClass) {
		return Arrays.asList(dispatcherClass.getDeclaredFields());
	}

	/**
	 * @param dispatcherClass the dispatcher class where the variables of type
	 *                        {@link BaseDispatchComponent} are declared
	 * @return the {@link BaseDispatchComponent} instances as map by using the {@link #getFields(Class)}
	 *         method
	 */
	public static Map<String, BaseDispatchComponent> getDeclaredComponents(Class<?> dispatcherClass) {
		Map<String, BaseDispatchComponent> componentMap = new HashMap<>();
		for (Field fld : getFields(dispatcherClass)) {
			// Get the runtime object to execute the method with
			Object fieldInstance = null;
			try {
				fieldInstance = fld.get(dispatcherClass);
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
			if (fieldInstance != null) {
				if (fieldInstance instanceof BaseDispatchComponent) {
					componentMap.put(fld.getName(), ((BaseDispatchComponent) fieldInstance));
				}
			}
		}
		return componentMap;
	}

	/**
	 * Checks if the assigned file matches with the expected format of the
	 * {@link BaseDispatchComponent}.
	 * 
	 * @param fileName Full name of the file to check
	 * @param rootNode The value of this parameter defines the expected name of the root node within
	 *                 tree formated file
	 * @return true = rootNode matches with the first node within the file ; false = rootNode does not
	 *         match to the first node within the file
	 * @throws IOException
	 */
//	static boolean checkDispatcherFile(String fileName, String rootNode) throws IOException {
//		return checkDispatcherFile(new DataContainer(new File(fileName)), rootNode, false);
//	}

	/**
	 * Checks if the file, associated with the DataContainer <code>dc</code>, matches with the expected
	 * format of the {@link BaseDispatchComponent}.
	 * 
	 * @param dc       DataContainer instance with the associated file, that needs to be checked
	 * @param rootNode The value of this parameter defines the expected name of the root node within
	 *                 tree formated file.
	 * @return true = rootNode matches with the first node within the file ; false = rootNode does not
	 *         match to the first node within the file
	 * @throws IOException
	 */
//	static boolean checkDispatcherFile(DataContainer dc, String rootNode) throws IOException {
//		return checkDispatcherFile(dc, rootNode, false);
//	}

	/**
	 * Checks if the file, associated with the DataContainer <code>dc</code>, matches with the expected
	 * format of the {@link BaseDispatchComponent}.
	 * 
	 * @param dc        DataContainer instance with the associated file, that needs to be checked
	 * @param rootNode  The value of this parameter defines the expected name of the root node within
	 *                  tree formated file
	 * @param createNew defines the behavior if the file does not exist.<br>
	 *                  true = create new file with first node and return true<br>
	 *                  false = do nothing and return false
	 * @return true = rootNode matches with the first node within the file ; false = rootNode does not
	 *         match to the first node within the file
	 * @throws IOException
	 */
//	static boolean checkDispatcherFile(DataContainer dc, String rootNode, boolean createNew) throws IOException {
//		File file = new File(dc.getInputFile().getPath());		
//		if (createNew && !file.exists()) {	
//			String filePath = file.getPath();
//			if(StringUtils.isBlank(rootNode)) {
//				dc.createFile(filePath);
//			} else {
//				dc.createFile(filePath, rootNode);
//			}
//		}
//		return file.exists();
//	}

	/**
	 * Removes all the mappings from the {@link #dcMap}.
	 */
	public static void clearDataContainer() {
		dcMap.clear();
	}

}
