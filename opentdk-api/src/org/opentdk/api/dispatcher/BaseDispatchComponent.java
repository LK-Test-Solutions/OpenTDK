package org.opentdk.api.dispatcher;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.opentdk.api.application.EBaseSettings;
import org.opentdk.api.datastorage.BaseContainer.EHeader;
import org.opentdk.api.datastorage.DataContainer;
import org.opentdk.api.datastorage.EOperator;
import org.opentdk.api.datastorage.Filter;
import org.opentdk.api.io.FileUtil;
import org.opentdk.api.io.XMLEditor;
import org.opentdk.api.util.DateUtil;
import org.opentdk.api.util.EFormat;
import org.opentdk.api.util.ListUtil;

/**
 * Similar to the native <code><b>enum</b></code> type of java, the <code><b>BaseDispatchComponent</b></code> class is used 
 * to build a set of variables with the difference that their values are not static and extended access to the variables
 * values and their attributes will be provided.<br> 
 * Each <code><b>BaseDispatchComponent</b></code> instance will be declared within a dispatcher definition class like {@link EBaseSettings} 
 * and the instance is associated with a node or field, depending on the format of the underlying DataContainer (tree or table format).<br><br>
 * Main features of <code><b>BaseDispatchComponent</b></code>:<br>
 * 	- Values and attributes of defined nodes or fields can be read from and written to the structure of the underlying DataContainer.<br>
 * 	- In case the defined file file does not exist, it will be created once a setter method is called.<br>
 *  - In case that no file is associated with the dispatcher component, their values will be stored in runtime objects (DOM Document, Hashmap, Arraylist etc.) depending on the container format.<br>
 * 	- Default values can be assigned to nodes or fields. They will be returned in case that the node or field does not exist in the runtime objects or associated files.<br>
 *
 * XML sample:
 * <pre>
 * 	Declaration of a set of variables in class EBaseSettings.java:
 * 	<code><b>public static final BaseDispatchComponent APP_AUTO_POSITION = new BaseDispatchComponent(EBaseSettings.class.getSimpleName(), "AutoPosition", "/AppSettings", "true");
 * 	public static final BaseDispatchComponent APP_LANGUAGE = new BaseDispatchComponent(EBaseSettings.class.getSimpleName(), "Language", "/AppSettings", "en");
 * 	public static final BaseDispatchComponent APP_LOGFILE = new BaseDispatchComponent(EBaseSettings.class.getSimpleName(), "Logfile", "/AppSettings", "./logs/Application.log");
 * 	</b></code>
 * 
 * 	Assign XML file to the EBaseAppSettings class within the main class of the application where the settings are used:
 * 	<code><b>EBaseAppSettings.setDataContainer(EBaseSettings.class, "./conf/AppSettings.xml");</b></code>
 * 
 * 	Read a value from the settings by using getter method of the BaseDispatchComponent instance:
 * 	<code><b>System.out.println(EBaseSettings.APP_LANGUAGE.getValue());</b></code>
 * 	=&gt; if the tag &lt;Language&gt; exist in the XML file, than the value of the tag will be returned (fr)
 * 	=&gt; if the tag &lt;Language&gt; does not exist in the XML file, the default defined in the declaration will be returned (en)
 * 
 * 	(Over)Write the value of a tag into the associated XML file:
 * 	<code><b>EBaseSettings.APP_LANGUAGE.setValue("de");</b></code>
 * 
 * 	Sample content of an associated XML file:
 * 	<code><b>&lt;?xml version="1.0" encoding="UTF-8" standalone="no"?&gt;
 *	&lt;AppSettings&gt;
 *   	&lt;Language&gt;fr&lt;/Language&gt;
 *   	&lt;Logfile&gt;./logs/mp.log&lt;/Logfile&gt;
 *	&lt;/AppSettings&gt;</b></code>
 * </pre>
 * 
 * @author LK Test Solutions
 */
public class BaseDispatchComponent {

	/**
	 * This HashMap stores the DataContainers of all settings used within an application. Once the setDataContainer method of a settings class
	 * is called, the DataContainer will be stored in the HashMap with the simple name of the settings class as key.
	 */
	private static Map<String, DataContainer> dcMap = new HashMap<String, DataContainer>();
	
	/**
	 * This HashMap stores a list with all declared BaseDispatchComponent variables of each settings class used within an application. 
	 * Once the setDataContainer method of a settings class is called, the list is automatically created and stored in the HashMap with 
	 * the simple name of the settings class as key.
	 */
	private static Map<String, List<Field>> fieldsMap = new HashMap<String, List<Field>>();
	
	/**
	 * This property assigns the key name of the related DataContainer to each BaseDispatchComponent variable.
	 */
	private String settingsKey;
	
	/**
	 * This property is used to define the name of the node (tree formats) or field header (tabular formats) within the associated DataContainer. 
	 * Each BaseDispatchComponent variable has a storage location, defined by the node name and path (tree formats) or column and row (tabular formats).
	 */
	private String parameterName;

	/**
	 * This property is used to define the path within the tree format of the associated DataContainer, where the node of the BaseDispatchComponent
	 * variable is stored.<br><br>
	 * Sample XPath definition for XML structure: "/parserRules/rule[@name='{param_1}']/element[@name='{param_2}']"
	 */
	private String parentXPath;
	
	/**
	 * This property is used to assign a default value to the BaseDispatchComponent variable which will be returned by the getValue method
	 * in case that no settings file entry with the XPath and node name exists. 
	 */
	private String defaultValue;
	
	/**
	 * This property keeps the dispatcher class where the BaseDispatchComponent variable is declared. Since the BaseDispatchComponent classes are static,
	 * there is no reference to the parent class, where they are declared. This is why the parent class needs to be set for the runtime instance of 
	 * the BaseDispatchComponent class.
	 */
	private Class<?> cls;
	
	/**
	 * The default constructor if this class that is called when creating a new instance without attributes.
	 */
	public BaseDispatchComponent() {

	}
	
	/**
	 * Constructor used to declare variables for tabular formats like .properties or .csv Files
	 * 
	 * @param Key - Name of the dispatcher class where the BaseDispatchComponent variable is declared. If the class is an extended subclass, then the name of the superclass needs to be used. 
	 * @param paramName - Header name of the field. Tabular formats can either have column header or row headers.
	 * @param dVal - Default value returned by the variable in case the field doesn't exist in the associated DataContainer or file.
	 */
	public BaseDispatchComponent(String Key, String paramName, String dVal) {
		this(Key, paramName, "", dVal);
	}

	/**
	 * Constructor used for tabular formats like .properties or .csv Files
	 * 
	 * @param parentClass - The dispatcher class where the BaseDispatchComponent variable is declared. If the class is an extended subclass, then the superclass needs to be used. 
	 * @param paramName - Header name of the field. Tabular formats can either have column header or row headers.
	 * @param dVal - Default value returned by the variable in cases the field doesn't exist in the associated DataContainer or file.
	 */
	public BaseDispatchComponent(Class<?> parentClass, String paramName, String dVal) {
		this(parentClass, paramName, "", dVal);
	}

	/**
	 * This constructor is used to declare variables for tree formats like XML, JSON or JAML.
	 * 
	 * @param parentClass - The dispatcher class where the BaseDispatchComponent variable is declared. If the class is an extended subclass, then the superclass needs to be used.
	 * @param paramName - Name of a node, associated with the variable
	 * @param pxp - Path, where the tag is located within a tree structure 
	 * @param dVal - Default value returned by the variable in case the node doesn't exist in the associated DataContainer or file
	 */
	public BaseDispatchComponent(Class<?> parentClass, String paramName, String pxp, String dVal) {
		this(parentClass.getSimpleName(), paramName, pxp, dVal);
		cls = parentClass;

	}
	
	/**
	 * This constructor is used to declare variables for tree formats like XML, JSON or JAML.
	 * 
	 * @param setKey - Name of the dispatcher class where the BaseDispatchComponent variable is declared. If the class is an extended subclass, then the name of the superclass needs to be used.
	 * @param paramName - Name of a node, associated with the variable
	 * @param pxp - Path, where the tag is located within a tree structure 
	 * @param dVal - Default value returned by the variable in cases the node doesn't exist in the associated DataContainer or file
	 */
	public BaseDispatchComponent(String setKey, String paramName, String pxp, String dVal) {
		settingsKey = setKey;
		parameterName = paramName;
		parentXPath = pxp;
		defaultValue = dVal;
		if(!dcMap.containsKey(settingsKey)) {
			if(!pxp.isEmpty()) {
				String[] tags = pxp.split("/");
				String rootTag = "<" + tags[1] + ">" + "</" + tags[1] + ">";
				InputStream stream = new ByteArrayInputStream(rootTag.getBytes(StandardCharsets.UTF_8));
				setDataContainer(settingsKey, new DataContainer(stream));
			} else {
				setDataContainer(settingsKey, new DataContainer());
			}
		}
		if(!getDataContainer(settingsKey).getHeaders().containsKey(paramName)) {
			getDataContainer(settingsKey).getHeaders().put(paramName, getDataContainer(settingsKey).getHeaders().size());
		}
	}
	
	/**
	 * This method adds a node or field with the defined value into the DataContainer and file that is associated
	 * with the {@link org.opentdk.api.dispatcher.BaseDispatchComponent} variable.<br><br>
	 * <pre>
	 * <b>Sample BaseDispatchComponent declaration for XML format:</b>
	 * public static final BaseDispatchComponent PERMITTED_USER = new BaseDispatchComponent(EAppSettings.class, "UserName", "/AppSettings/PermittedUsers", "")
	 * <b>Method call:</b>
	 * EAppSettings.PERMOTTED_USER.addValue("admin");
	 * <b>Resulting entry within XML file:</b>
	 * &lt;AppSettings&gt;
	 * 	&lt;PermittedUsers&gt;
	 * 		&lt;UserName&gt;admin&lt;/UserName&gt;
	 * 	&lt;/PermittedUsers&gt;
	 * &lt;/AppSettings&gt;
	 * </pre>
	 * 
	 * @param value - The value which will be set to the node or field
	 */
	public void addValue(String value) {
		addValue("", value);	
	}

	/**
	 * This method adds a node or field with the defined value into the DataContainer and file that is associated
	 * with the {@link org.opentdk.api.dispatcher.BaseDispatchComponent} variable. <br><br>
	 * <pre>
	 * <b>Sample BaseDispatchComponent declaration for XML format:</b>
	 * public static final BaseDispatchComponent DATASET = new BaseDispatchComponent(EDataMapping.class, "set", "/Mappings/mapping[@name='{param_1}']", "")
	 * <b>Method call:</b>
	 * EDataMapping.DATASET.addValue("CountryCode", "France;FR");
	 * <b>Resulting entry within XML file:</b>
	 * &lt;Mappings&gt;
	 * 	&lt;mapping name="CountryCode"&gt;
	 * 		&lt;set&gt;Germany;D&lt;/UserName&gt;
	 * 		&lt;set&gt;France;FR&lt;/UserName&gt;
	 * 	&lt;/mapping&gt;
	 * &lt;/Mappings&gt;
	 * </pre>
	 * 
	 * @param params - Semicolon separated string with attribute values of the parent nodes that describe the path more uniquely.
	 * @param value - The value which will be set to the node or field
	 */
	public void addValue(String params, String value) {
		addValue(params, value, false);
	}
	
	/**
	 * This method adds a node or field with the defined value into the DataContainer and file that is associated 
	 * with the {@link org.opentdk.api.dispatcher.BaseDispatchComponent} variable.<br><br>
	 * <pre>
	 * <b>Sample BaseDispatchComponent declaration for XML format:</b>
	 * public static final BaseDispatchComponent PERMITTED_USER = new BaseDispatchComponent(EAppSettings.class, "UserName", "/AppSettings/PermittedUsers", "")
	 * <b>Method call:</b>
	 * EAppSettings.PERMOTTED_USER.addValue("admin");
	 * <b>Resulting entry within XML file:</b>
	 * &lt;AppSettings&gt;
	 * 	&lt;PermittedUsers&gt;
	 * 		&lt;UserName&gt;admin&lt;/UserName&gt;
	 * 	&lt;/PermittedUsers&gt;
	 * &lt;/AppSettings&gt;
	 * </pre>
	 * 
	 * @param value - The value which will be set to the node or field
	 * @param noDuplicates - true = do not create duplicate nodes or fields; false = create duplicate nodes or fields
	 */
	public void addValue(String value, boolean noDuplicates) {
		addValue("", value, noDuplicates);
	}

	/**
	 * This method adds a node or tag with the defined value into the DataContainer and file that is associated
	 * with the {@link org.opentdk.api.dispatcher.BaseDispatchComponent} variable.<br><br>
	 * <pre>
	 * <b>Sample BaseDispatchComponent declaration for XML format:</b>
	 * public static final BaseDispatchComponent DATASET = new BaseDispatchComponent(EDataMapping.class, "set", "/Mappings/mapping[@name='{param_1}']", "")
	 * <b>Method call:</b>
	 * EDataMapping.DATASET.addValue("CountryCode", "France;FR");
	 * <b>Resulting entry within XML file:</b>
	 * &lt;Mappings&gt;
	 * 	&lt;mapping name="CountryCode"&gt;
	 * 		&lt;set&gt;Germany;D&lt;/UserName&gt;
	 * 		&lt;set&gt;France;FR&lt;/UserName&gt;
	 * 	&lt;/mapping&gt;
	 * &lt;/Mappings&gt;
	 * </pre>
	 * 
	 * @param params - Semicolon separated string with attribute values of the parent nodes that describe the path more uniquely.
	 * @param value - The value which will be set to the new node
	 * @param noDuplicates - true = do not create duplicate nodes; false = create duplicate nodes
	 */
	//ToDo: Filter not allowed for tabular formats!
	public void addValue(String params, String value, boolean noDuplicates) {
		String pxp = resolveXPath(params);
		Filter fltr = new Filter();
		if (!parentXPath.isEmpty()) {
			fltr.addFilterRule("XPath", pxp, EOperator.EQUALS);
		}
		if (noDuplicates && ListUtil.asList(getValues()).contains(value)) {
			return;
		}
		dcMap.get(settingsKey).addField(parameterName, value, fltr);
	}
	
	/**
	 * Checks if the assigned file matches with the expected format of the {@link org.opentdk.api.dispatcher.BaseDispatchComponent}.
	 * 
	 * @param fileName - Full name of the file to check.
	 * @param rootNode - The value of this parameter defines the expected name of the root node within tree formated file.
	 * @return true = rootNode matches with the first node within the file ; false = rootNode does not match to the first node within the file
	 */
	public static boolean checkDispatcherFile(String fileName, String rootNode) {
		return checkDispatcherFile(fileName, rootNode, false);
	}

	/**
	 * Checks if the file, associated with the DataContainer <code>dc</code>, matches with the expected format of the {@link org.opentdk.api.dispatcher.BaseDispatchComponent}.
	 * 
	 * @param dc - DataContainer instance with the associated file, that needs to be checked
	 * @param rootNode - The value of this parameter defines the expected name of the root node within tree formated file.
	 * @return true = rootNode matches with the first node within the file ; false = rootNode does not match to the first node within the file
	 */
	public static boolean checkDispatcherFile(DataContainer dc, String rootNode) {
		return checkDispatcherFile(dc.getFileName(), rootNode, false);
	}

	/**
	 * Checks if the file, associated with the DataContainer <code>dc</code>, matches with the expected format of the {@link org.opentdk.api.dispatcher.BaseDispatchComponent}.
	 * 
	 * @param fileName - Full name of the file to check.
	 * @param rootNode - The value of this parameter defines the expected name of the root node within tree formated file.
	 * @param createNew - defines the behavior if the file does not exist.<br> true = create new file with first node and return true<br> false = do nothing and return false
	 * @return true = rootNode matches with the first node within the file ; false = rootNode does not match to the first node within the file
	 */
	public static boolean checkDispatcherFile(String fileName, String rootNode, boolean createNew) {
		boolean res = false;
		if (createNew || !new File(fileName).exists()) {
			try {
				FileUtil.deleteFile(fileName);
			} catch (IOException e) {
				e.printStackTrace();
			}
			XMLEditor xEdit = new XMLEditor(fileName, rootNode);
			xEdit.save(fileName);
		}
		return res;
	}

	/**
	 * Removes all the mappings from the DataContainer HashMap.
	 * 
	 * @see {@link java.util.Map#clear()}
	 */
	public static void clearDataContainer() {
		dcMap.clear();
	}
	
	/**
	 * Deletes all nodes or fields within the associated DataContainer and file that are representing entries for this {@link org.opentdk.api.dispatcher.BaseDispatchComponent}.<br><br>
	 * 
	 * XML Sample<br>
	 * The method call:
	 * <pre>
	 * 	EBaseAppSettings.APP_TRACE_LEVEL.delete();
	 * </pre>
	 * performed on the following BaseDispatchComponent variable, defined within the settings class EBaseAppSettings:
	 * <pre>
	 * 	public static final BaseDispatchComponent APP_TRACE_LEVEL = new BaseDispatchComponent(EBaseAppSettings.class, "TraceLevel", "/AppSettings", "1"); 
	 * </pre>
	 * will delete the tag TraceLevel of the following XML sample:
	 * <pre>
	 * 	&lt;AppSettings&gt;
	 * 		&lt;TraceLevel&gt;1&lt;/TraveLevel&gt;
	 * 	&lt;/AppSettings&gt;
	 * </pre>
	 * If multiple tags with the same name and XPath exist, then all matching tags will be deleted.
	 */
	public void delete() {
		delete("", "", "");
	}

	/**
	 * Deletes all matching nodes or fields with an assigned attribute within the associated DataContainer and file that are 
	 * representing entries for this {@link org.opentdk.api.dispatcher.BaseDispatchComponent}.<br><br>
	 * 
	 * XML Sample<br>  
	 * The method call:
	 * <pre>
	 * 	EBaseAppSettings.APP_TRACE_LEVEL.delete("name", "Security");
	 * </pre>
	 * performed on the following BaseDispatchComponent variable, defined within the settings class EBaseAppSettings:
	 * <pre>
	 * 	public static final BaseDispatchComponent APP_TRACE_LEVEL = new BaseDispatchComponent(EBaseAppSettings.class, "TraceLevel", "/AppSettings", "1"); 
	 * </pre>
	 * will delete the tag TraceLevel of the following XML sample:
	 * <pre>
	 * 	&lt;AppSettings&gt;
	 * 		&lt;TraceLevel name="Security"&gt;1&lt;/TraveLevel&gt;
	 * 	&lt;/AppSettings&gt;
	 * </pre>
	 * If multiple tags with the same name and XPath exist, then all matching tags will be deleted.
	 * 
	 * @param attrName - Attribute name of the node or field that will be deleted
	 * @param attrValue - Attribute value of the node or field that will be deleted
	 */
	public void delete(String attrName, String attrValue) {
		delete("", attrName, attrValue);
	}

	/**
	 * Deletes all matching nodes or fields with an assigned attribute within the associated DataContainer and file that are 
	 * representing entries for this {@link org.opentdk.api.dispatcher.BaseDispatchComponent}. In addition the nodes within 
	 * the path hierarchy can be identified by an attribute<br><br>
	 * 
	 * XML Sample<br>
	 * The method call:
	 * <pre>
	 * 	ECollectionRules.QUERY_FILTERVALUE.delete("Search LK Test;COMPANY_NAME", "", "");
	 * </pre>
	 * performed on the following BaseDispatchComponent variable, defined within the settings class ECollectionRules:
	 * <pre>
	 * 	public static final BaseDispatchComponent QUERY_FILTERVALUE = new BaseDispatchComponent(ECollectionRules.class.getSimpleName(), "value", "/Rules/rule[@name='{param_1}']/Query/filter[@column='{param_2}']", ""); 
	 * </pre>
	 * will delete the tag <b>value</b> with the value <b>LK Test Solutions GmbH</b> of the following XML sample:
	 * <pre>
	 * 	&lt;Rules&gt;
     * 		&lt;rule name="Search LK Test"&gt;
     *  		&lt;Query&gt;
     * 				&lt;filter column="COMPANY_NAME"&gt;
     *					&lt;value&gt;LK Test Solutions GmbH&lt;/value&gt;
     *				&lt;/filter&gt;
     * 				&lt;filter column="CITY"&gt;
     *					&lt;value&gt;Munich&lt;/value&gt;
     *				&lt;/filter&gt;
     *			&lt;/Query&gt;
     *		&lt;/rule&gt;
     *	&lt;/Rules&gt;
	 * </pre>
	 * If multiple tags with the same name and XPath exist, then all matching tags will be deleted.
	 * 
	 * @param params - Semicolon separated string with attribute values of parent nodes within the path hierarchy. 
	 * @param attrName - Attribute name of the node or field that will be deleted
	 * @param attrValue - Attribute value of the node or field that will be deleted
	 */
	public void delete(String params, String attrName, String attrValue) {
		String xPath = resolveXPath(params);
		Filter fltr = new Filter();
		if (!parentXPath.isEmpty()) {
			fltr.addFilterRule("XPath", xPath, EOperator.EQUALS);
		}
		dcMap.get(settingsKey).deleteField(parameterName, attrName, attrValue, fltr);
	}
	
	/**
	 * This method returns the attribute value of the of the first matching node that is associated with this BaseDispatchComponent instance.
	 * <pre>
	 * XML Sample:
	 * <b>EAppSettings.POS.getAttribute("axis");</b>
	 * executed on the following XML content:
	 * <b>&lt;AppSettings&gt;&lt;Geometry&gt;&lt;Pos axis="X"&gt;50&lt;/Pos&gt;&lt;/Geometry&gt;&lt;/AppSettings&gt;</b>
	 * returns the value "X"
	 * </pre>
	 * 
	 * @param attrName - the name of the nodes attribute to search for
	 * @return the value of the defined attribute
	 */
	public String getAttribute(String attrName) {
		String expr = parameterName;
		if (!parentXPath.isEmpty()) {
			expr = parentXPath + "/" + parameterName;
		}
		DataContainer dc = dcMap.get(settingsKey);
		String[] attributes = dc.getAttributes(expr, attrName);
		if(attributes.length == 0) {
			return null;
		}
		return attributes[0];
	}

	/**
	 * This method returns the attribute value of the first matching node that is associated with this BaseDispatchComponent instance
	 * and where the nodes within the hierarchical path include the attribute values that are defined by the params argument.
	 * <pre>
	 * XML sample:f
	 * <b>EAppSettings.POS.getAttributes("Settings", "axis");</b>
	 * executed on the following XML content:
	 * <b>&lt;AppSettings&gt;&lt;Geometry window="Help"&gt;&lt;Pos axis="X"&gt;80&lt;/Pos&gt;&lt;/Geometry&gt;&lt;/AppSettings&gt;</b>
	 * <b>&lt;AppSettings&gt;&lt;Geometry window="Help"&gt;&lt;Pos axis="Y"&gt;120&lt;/Pos&gt;&lt;/Geometry&gt;&lt;/AppSettings&gt;</b>
	 * <b>&lt;AppSettings&gt;&lt;Geometry window="Settings"&gt;&lt;Pos axis="X1"&gt;100&lt;/Pos&gt;&lt;/Geometry&gt;&lt;/AppSettings&gt;</b>
	 * <b>&lt;AppSettings&gt;&lt;Geometry window="Settings"&gt;&lt;Pos axis="Y1"&gt;150&lt;/Pos&gt;&lt;/Geometry&gt;&lt;/AppSettings&gt;</b>
	 * returns the value "X1"
	 * </pre>
	 * 
	 * @param params - a semicolon separated string with attribute values within the path
	 * @param attrName - the name of the node attribute to search for
	 * @return the value of the matching attribute
	 */
	public String getAttribute(String params, String attrName) {
		String expr = parameterName;
		if (!parentXPath.isEmpty()) {
			expr = resolveXPath(params) + "/" + parameterName;
		}
		DataContainer dc = dcMap.get(settingsKey);
		String[] attributes = dc.getAttributes(expr, attrName);
		if(attributes.length == 0) {
			return null;
		}
		return attributes[0];
	}

	/**
	 * This method returns the attribute values of all associated nodes of this BaseDispatchComponent instance. 
	 * <pre>
	 * XML sample:
	 * <b>EAppSettings.POS.getAttributes("axis");</b>
	 * executed on the following XML content:
	 * <b>&lt;AppSettings&gt;&lt;Geometry&gt;&lt;Pos axis="X"&gt;50&lt;/Pos&gt;&lt;/Geometry&gt;&lt;/AppSettings&gt;</b>
	 * <b>&lt;AppSettings&gt;&lt;Geometry&gt;&lt;Pos axis="Y"&gt;100&lt;/Pos&gt;&lt;/Geometry&gt;&lt;/AppSettings&gt;</b>
	 * returns the values ["X", "Y"]
	 * </pre>
	 * 
	 * @param attrName - the name of the nodes attribute to search for
	 * @return the values of all matching attributes as array of type String
	 */
	public String[] getAttributes(String attrName) {
		String expr = parameterName;
		if (!parentXPath.isEmpty()) {
			expr = parentXPath + "/" + parameterName;
		}
		return dcMap.get(settingsKey).getAttributes(expr, attrName);
	}
	
	/**
	 * This method returns the attribute values of all matching nodes that are associated with this BaseDispatchComponent instance
	 * and where the nodes within the hierarchical path include the attribute values that are defined by the params argument.
	 * <pre>
	 * XML sample:
	 * <b>EAppSettings.POS.getAttributes("Settings", "axis");</b>
	 * executed on the following XML content:
	 * <b>&lt;AppSettings&gt;&lt;Geometry window="Help"&gt;&lt;Pos axis="X"&gt;80&lt;/Pos&gt;&lt;/Geometry&gt;&lt;/AppSettings&gt;</b>
	 * <b>&lt;AppSettings&gt;&lt;Geometry window="Help"&gt;&lt;Pos axis="Y"&gt;120&lt;/Pos&gt;&lt;/Geometry&gt;&lt;/AppSettings&gt;</b>
	 * <b>&lt;AppSettings&gt;&lt;Geometry window="Settings"&gt;&lt;Pos axis="X1"&gt;100&lt;/Pos&gt;&lt;/Geometry&gt;&lt;/AppSettings&gt;</b>
	 * <b>&lt;AppSettings&gt;&lt;Geometry window="Settings"&gt;&lt;Pos axis="Y1"&gt;150&lt;/Pos&gt;&lt;/Geometry&gt;&lt;/AppSettings&gt;</b>
	 * returns the values ["X1", "Y1"]
	 * </pre>
	 * 
	 * @param params - a semicolon separated string with attribute values within the path
	 * @param attrName - the name of the nodes attribute to search for
	 * @return the values of all matching attribute as array of type String
	 */
	public String[] getAttributes(String params, String attrName) {
		String expr = parameterName;
		if (!parentXPath.isEmpty()) {
			expr = resolveXPath(params) + "/" + parameterName;
		}
		return dcMap.get(settingsKey).getAttributes(expr, attrName);
	}

	
	/**
	 * Returns the DataContainer that acts as runtime storage for the values linked to the {@link org.opentdk.api.dispatcher.BaseDispatchComponent} instances.
	 * 
	 * @param keyName - Name of the key where the DataContainer is stored within the HashMap. This should be the simple name of the dispatcher class. If the dispatcher class is an extended class, then the simple name of its superclass needs to be defined.
	 * @return The DataContainer object, representing the runtime storage for the dispatcher
	 */
	public static DataContainer getDataContainer(String keyName) {
		return dcMap.get(keyName);
	}
	
	/**
	 * Retrieves the fieldsMap which includes the names of all {@link org.opentdk.api.dispatcher.BaseDispatchComponent} variables, used by the application at runtime.
	 * 
	 * @return fielsMap property of type HashMap
	 */
	public static Map<String, List<Field>> getFieldsMap() {
		return fieldsMap;
	}

	/**
	 * Returns the parameterName property of the {@link org.opentdk.api.dispatcher.BaseDispatchComponent}.
	 * 
	 * @return The value of the parameterName property as string
	 * @see {@link org.opentdk.api.dispatcher.BaseDispatchComponent#parameterName}
	 */
	public String getName() {
		return parameterName;
	}
	
	/**
	 * This method retrieves the value that is assigned to the {@link org.opentdk.api.dispatcher.BaseDispatchComponent} variable in the following way:<br>
	 * - Search the node or field, defined by property {@link #parameterName}. For tree formats the node will be searched within the XPath, defined by property {@link #parentXPath}<br>
	 * - If the node or field does not exist within the associated DataContainer, the default value defined by property {@link #defaultValue} will be retrieved.<br>
	 * - If multiple nodes or fields exist, then the first value will be retrieved
	 * 
	 * @return the value, assigned to the {@link org.opentdk.api.dispatcher.BaseDispatchComponent} variable as String
	 */
	public String getValue() {
		if((parentXPath != null) && (!parentXPath.isEmpty())) {
			return getValue("");
		}
		if(dcMap.containsKey(settingsKey)) {
			if((dcMap.get(settingsKey).getHeaderIndex(parameterName) >= 0) && (dcMap.get(settingsKey).getRowCount() > 0)) {
				if(dcMap.get(settingsKey).getValue(parameterName) != null) {
					return dcMap.get(settingsKey).getValue(parameterName);
				}
			}
		}
		return defaultValue;
	}
	
	/**
	 * This method retrieves the value that is assigned to the {@link org.opentdk.api.dispatcher.BaseDispatchComponent} variable in the following way:<br>
	 * - Search the node or field, defined by property {@link #parameterName}. For tree formats the node will be searched within the XPath, defined by property {@link #parentXPath}<br>
	 * - If the node or field does not exist within the associated DataContainer, the default value defined by property {@link #defaultValue} will be retrieved.<br>
	 * - If multiple nodes or fields exist, then the first value will be retrieved
	 * 
	 * @param params - Semicolon separated string with the values of node attributes within the parentXPath
	 * @return the value, assigned to the {@link org.opentdk.api.dispatcher.BaseDispatchComponent} variable as String
	 */
	public String getValue(String params) {
		String outVal = defaultValue;
		if(dcMap.containsKey(settingsKey)) {
			String[] values = getValues(params);
			if (values.length > 0) {
				outVal = values[0];
			}
		}
		return outVal;
	}

	/**
	 * This method retrieves the values that are assigned to the {@link org.opentdk.api.dispatcher.BaseDispatchComponent} variable in the following way:<br>
	 * - Search the nodes or fields, defined by property {@link #parameterName}. For tree formats the nodes will be searched within the XPath, defined by property {@link #parentXPath}<br>
	 * - If the nodes or fields do not exist within the associated DataContainer, the default value defined by property {@link #defaultValue} will be retrieved.<br>
	 * - Otherwise the values of all matching nodes or fields will be retrieved.
	 * 
	 * @return the values, assigned to the {@link org.opentdk.api.dispatcher.BaseDispatchComponent} variable as Array of type String
	 */
	public String[] getValues() {
		return getValues("");
	}

	/**
	 * This method retrieves the values that are assigned to the {@link org.opentdk.api.dispatcher.BaseDispatchComponent} variable in the following way:<br>
	 * - Search the nodes or fields, defined by property {@link #parameterName}. For tree formats the nodes will be searched within the XPath, defined by property {@link #parentXPath}<br>
	 * - If the nodes or fields do not exist within the associated DataContainer, the default value defined by property {@link #defaultValue} will be retrieved.<br>
	 * - Otherwise the values of all matching nodes will be retrieved
	 * 
	 * @param params - Semicolon separated string with the values of tag attributes within the parentXPath
	 * @return the values, assigned to the {@link org.opentdk.api.dispatcher.BaseDispatchComponent} variable as Array of type String
	 */
	public String[] getValues(String params) {
		Filter fltr = new Filter();
		if((parentXPath != null) && (!parentXPath.isEmpty())) {
			String pxp = resolveXPath(params);
			fltr.addFilterRule("XPath", pxp, EOperator.EQUALS);
		}
		if(dcMap.get(settingsKey).getValuesAsList(parameterName, fltr).size() > 0) {
			return dcMap.get(settingsKey).getValuesAsList(parameterName, fltr).toArray(new String[dcMap.get(settingsKey).getValuesAsList(parameterName, fltr).size()]);
		}
		return new String[0];
	}
	
	/**
	 * This method replaces all parameter wildcards of the parentXPath of a BaseDispatchComponent instance by the values
	 * defined in the params attribute.
	 * <pre>
	 * e.g. 
	 * <b>resolveXPath("CUSTOMERS;LASTNAME");</b>
	 * executed by the following declared BaseDispatchComponent:
	 * <b>public static final BaseDispatchComponent OUTPUT_COLUMNNAME = new BaseDispatchComponent(ECollectionRules.class.getSimpleName(), "column", "/Rules/rule[@name='{param_1}']/Output/ResultTypes/resultType[@name='{param_2}']", "");</b>
	 * will transform the XPath 
	 * <b>"/Rules/rule[@name='{param_1}']/Output/ResultTypes/resultType[@name='{param_2}']"</b>
	 * into
	 * <b>"/Rules/rule[@name='CUSTOMERS']/Output/ResultTypes/resultType[@name='LASTNAME']"</b>
	 * </pre>
	 *  
	 * @param params 	Semicolon separated string with attribute values of parent nodes within the XPath hierarchy. 
	 * @return transformed XPath with substituted attribute values of the parent nodes within the XPath hierarchy.
	 */
	private String resolveXPath(String params) {
		String pxp = parentXPath;
		if (params.isBlank()) {
			// remove all attributes placeholder like "[@name='{param_1}']"
			pxp = pxp.replaceAll("param_[0-9]*", "");
			pxp = pxp.replaceAll("\\[@.*\\='\\{\\}'\\]", "");
		} else {
			String[] p = params.split(";");
			// replace parameter place holders by parameter values
			for (int i = 0; i < p.length; i++) {
				pxp = pxp.replace("{param_" + String.valueOf(i + 1) + "}", p[i]);
			}
		}
		return pxp;
	}

	/**
	 * Sets the attribute of a node. The method overwrites identical node with this attribute, in case the existing attributes value is empty.
	 * In any other case the tag will be added to the parendXPath.
	 * 
	 * @param attrName - Attribute name of the node 
	 * @param attrValue - Attribute value of the node that will be set
	 */
	public void setAttribute(String attrName, String attrValue) {
		setAttribute("", attrName, attrValue);
	}

	/**
	 * Sets the attribute of a node. The method overwrites identical node with this attribute, in case the existing attributes value is empty.
	 * In any other case the node will be added to the parendXPath.
	 * 
	 * @param params - Semicolon separated string with attribute values of parent tags within the XPath hierarchy. 
	 * @param attrName - Attribute name of the node 
	 * @param attrValue - Attribute value of the node that will be set
	 */
	public void setAttribute(String params, String attrName, String attrValue) {
		setAttribute("", params, attrName, attrValue);
	}
	
	/**
	 * Sets the attribute of a node. The method overwrites identical node with this attribute, in case the existing attributes value is empty.
	 * In any other case the node will be added to the parendXPath.
	 * 
	 * @param oldValue	Attribute value of the node that will be searched and overwritten
	 * @param params	Semicolon separated string with attribute values of parent nodes within the XPath hierarchy. 
	 * @param attrName	Attribute name of the node 
	 * @param attrValue	Attribute value of the node that will be set
	 */
	public void setAttribute(String oldValue, String params, String attrName, String attrValue) {
		String xPath = resolveXPath(params);
		Filter fltr = new Filter();
		fltr.addFilterRule("XPath", xPath, EOperator.EQUALS);
		dcMap.get(settingsKey).addField(parameterName, attrName, oldValue, attrValue, fltr);
	}
	
	/**
	 * Creates a DataContainer instance from a given file and adds the instance to the {@link #dcMap} HashMap of {@link org.opentdk.api.dispatcher.BaseDispatchComponent}.
	 * The keyName within the HashMap should be the simple name of the dispatcher class, where the BaseDispatchComponent variables are declared. In case the
	 * dispatcher class is a subclass of a superclass with predefined BaseDispatchComponent variables like EBaseSettings, then the simple name 
	 * of the superclass needs to be used.<br><br>
	 * 
	 * <pre>
	 * <b>Sample usage:</b>
	 * 	ECollectorSettings.setDataContainer(ECollectorSettings.class, "c:\\Appdata\\MetricsCollector\\conf\\msettings.xml");
	 * </pre>
	 * 
	 * @param keyName - Name of the key where the DataContainer is stored within the HashMap. This should be the simplename of the settings class.
	 * @param settingsFile - Full name of the XML file that will be associated with the settings class where the BaseDispatchComponent variable is declared.
	 */
	public static void setDataContainer(String keyName, String settingsFile) {
		dcMap.put(keyName, new DataContainer(settingsFile));
	}

	/**
	 * Adds an DataContainer instance to the {@link #dcMap} HashMap of {@link org.opentdk.api.dispatcher.BaseDispatchComponent}.
	 * The keyName within the HashMap should be the simple name of the dispatcher class, where the BaseDispatchComponent variables are declared. In case the
	 * dispatcher class is a subclass of a superclass with predefined {@link org.opentdk.api.dispatcher.BaseDispatchComponent} variables like EBaseSettings, 
	 * then the simple name of the superclass needs to be used.<br><br>
	 * 
	 * <pre>
	 * <b>Sample usage:</b>
	 * 	DataContainer dc = new DataContainer("c:\\Appdata\\MetricsCollector\\conf\\msettings.xml");
	 * 	ECollectorSettings.setDataContainer(ECollectorSettings.class, dc);
	 * </pre>
	 * 
	 * @param setKey - Name of the key where the DataContainer is stored within the HashMap. This should be the simple name of the settings class.
	 * @param dc - A DataContainer instance which is associated to a file in a format, described by the dispatcher class.
	 */
	public static void setDataContainer(String setKey, DataContainer dc) {
		dcMap.put(setKey, dc);
	}

	/**
	 * Creates a DataContainer instance from an InputStream and adds the instance to the {@link #dcMap} HashMap of {@link BaseDispatchComponent}.
	 * The keyName within the HashMap should be the simple name of the dispatcher class, where the BaseDispatchComponent variables are declared. In case the
	 * dispatcher class is a subclass of a superclass with predefined BaseDispatchComponent variables like EBaseSettings, then the simple name 
	 * of the superclass needs to be used.<br><br>
	 * 
	 * <pre>
	 * <b>XML sample:</b>
	 * String xmlContent = "&lt;?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?&gt;&lt;rootTag&gt;...&lt;/rootTag&gt;"
	 * InputStream stream = new ByteArrayInputStream(xmlContent.getBytes(StandardCharsets.UTF_8));
	 * ECollectorSettings.setDataContainer(ECollectorSettings.class, stream);
	 * </pre>
	 * 
	 * @param setKey - Name of the key where the DataContainer is stored within the HashMap.
	 * @param inStream - A InputStream with content in a format of any supported {@link org.opentdk.api.datastorage.DataContainer}
	 * @see {@link org.opentdk.api.datastorage.DataContainer#DataContainer(InputStream)}
	 */
	public static void setDataContainer(String keyName, InputStream inStream) {
		dcMap.put(keyName, new DataContainer(inStream));
	}

	/**
	 * This method is called within the {@link BaseDispatcher#setDataContainer} methods to assign a list with all declared
	 * {@link BaseDispatchComponent} variables, declared within a dispatcher class.  
	 * 
	 * @param setKey - The key under which the fields list will be stored within the HashMap. This should be the simple name of the dispatcher class, where the {@link BaseDispatchComponent} variables are declared.
	 * @param fields - A List object with the names of all {@link BaseDispatchComponent} variables of a dispatcher class. 
	 */
	public static void setFields(String setKey, List<Field> fields) {
		fieldsMap.put(setKey, fields);
	}

	/**
	 * This method searches within the associated document for the first tree node or field, that matches the name and path (for tree formats)
	 * of this {@link BaseDispatchComponent} instance and assigns the value to this node/field.
	 * If no node/field is found, then a new node/field will be created. If one or more nodes/fields exist, then the value of the first matching 
	 * node/field will be overwritten.
	 *  
	 * <pre>
	 * XML Sample:
	 * <b>EAppSettings.POSX.setValue("100");</b>
	 * Replaces the value 50 of the following XML tag by 100:
	 * <b>&lt;AppSettings&gt;&lt;Geometry&gt;&lt;PosX&gt;50&lt;/PosX&gt;&lt;/Geometry&gt;&lt;/AppSettings&gt;</b>
	 * </pre>
	 * 
	 * @param value - the value that will be written into the node or field
	 */
	public void setValue(String value) {
		if((parentXPath == null) || (parentXPath.isEmpty())) {
			dcMap.get(settingsKey).setValue(parameterName, value);
		}else {
			setValue("", value);
		}
	}

	/**
	 * This method searches within the associated document for the first node that matches the name and path of this 
	 * {@link BaseDispatchComponent} and assigns the value to this field. As additional search criteria, the params argument can be used 
	 * to enclose the search to path nodes with specific attribute values.
	 * If no node is found, then a new node will be created. If one or more nodes exist, then the value of the first matching node
	 * will be overwritten.
	 * 
	 * <pre>
	 * XML sample:
	 * <b>EAppSettings.POSX.setValue("Help", "100");</b>
	 * Replaces the value 50 of the following XML tag by 100:
	 * <b>&lt;AppSettings&gt;&lt;Geometry window="Help"&gt;&lt;PosX&gt;50&lt;/PosX&gt;&lt;/Geometry&gt;&lt;/AppSettings&gt;</b>
	 * </pre>
	 * 
	 * @param params - Semicolon separated string with the values of node attributes within the nodes path
	 * @param value - the value that will be written into the node
	 */
	public void setValue(String params, String value) {
		String xPath = resolveXPath(params);
		Filter fltr = new Filter();
		fltr.addFilterRule("XPath", xPath, EOperator.EQUALS);
		dcMap.get(settingsKey).setValue(parameterName, value, fltr);
	}
	
	/**
	 * This method searches within the associated document for the first node that matches the node name, path and the defined
	 * value (oldValue argument) of this {@link BaseDispatchComponent} and replaces the value of this node by newValue. 
	 * As additional search criteria, the params argument can be used to enclose the search to path nodes with specific attribute 
	 * values. Use an empty string for the params argument to search for all path nodes by name only.
	 * If no node is found, then a new node will be created. If one or more nodes exist, then the value of the first matching node 
	 * will be overwritten.
	 * 
	 * <pre>
	 * XML sample:
	 * <b>EAppSettings.POSX.setValue("Help", "50", "100");</b>
	 * Assigns the value 100 to the PosX tag, only if the tag already exists within the XML document and only when the current value is 50.
	 * Replaces the value 50 of the following XML tag by 100:
	 * <b>&lt;AppSettings&gt;&lt;Geometry window="Help"&gt;&lt;PosX&gt;50&lt;/PosX&gt;&lt;/Geometry&gt;&lt;/AppSettings&gt;</b>
	 * </pre>
	 * 
	 * @param params - Semicolon separated string with the values of tag attributes within the parent path
	 * @param oldValue - the node value that will be replaced
	 * @param newValue - the value that will overwrite the oldValue
	 */
	public void setValue(String params, String oldValue, String newValue) {
		String xPath = resolveXPath(params);
		Filter fltr = new Filter();
		fltr.addFilterRule("XPath", xPath, EOperator.EQUALS);
		fltr.addFilterRule(parameterName, oldValue, EOperator.EQUALS);
		dcMap.get(settingsKey).setValue(parameterName, newValue, fltr);
	}

	/**
	 * This method will replace the values of all nodes or fields within the associated document that match with the name and path (for tree formats) 
	 * of this {@link BaseDispatchComponent} and where the current value is equal to the value defined by oldValue argument. 
	 * The argument allOccurences defines if all matching node or field values will be replaced or only the first one.
	 * 
	 * @param oldValue - The value of a node or field that will be searched for
	 * @param newValue - The value that will be used to overwrite the found nodes or fields
	 * @param allOccurences	- true = overwrite all matching node or field values; false = overwrite only the first matching node or field value
	 */
	public void setValues(String oldValue, String newValue, boolean allOccurences) {
		String xPath = "";
		Filter fltr = new Filter();
		if((parentXPath != null) || (!parentXPath.isEmpty())) {
			xPath = resolveXPath("");
			fltr.addFilterRule("XPath", xPath, EOperator.EQUALS);
		}
		fltr.addFilterRule(parameterName, oldValue, EOperator.EQUALS);
		dcMap.get(settingsKey).setValues(parameterName, newValue, fltr, allOccurences);
	}
	
}
