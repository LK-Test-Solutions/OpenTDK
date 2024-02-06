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

import org.apache.commons.lang3.StringUtils;
import org.opentdk.api.application.EBaseSettings;
import org.opentdk.api.datastorage.DataContainer;
import org.opentdk.api.datastorage.EContainerFormat;
import org.opentdk.api.filter.Filter;
import org.opentdk.api.mapping.EOperator;
import org.opentdk.api.util.ListUtil;

/**
 * Similar to the native <code><b>enum</b></code> type of java, the
 * <code><b>BaseDispatchComponent</b></code> class is used to build a set of variables with the
 * difference that their values are not static and extended access to the variables values and their
 * attributes is provided.<br>
 * Each <code><b>BaseDispatchComponent</b></code> instance will be declared within a dispatcher
 * definition class like {@link EBaseSettings} and the instance is associated with a node or field,
 * depending on the format of the underlying DataContainer (tree or table format).<br>
 * <br>
 * Main features of <code><b>BaseDispatchComponent</b></code>:<br>
 * {@literal -} Values and attributes of defined nodes or fields can be read from and written to the
 * structure of the underlying DataContainer.<br>
 * {@literal -} In case the defined file file does not exist, it will be created once a setter
 * method is called.<br>
 * {@literal -} In case that no file is associated with the dispatcher component, their values will
 * be stored in runtime objects (DOM Document, HashMap, ArrayList etc.) depending on the container
 * format.<br>
 * {@literal -} Default values can be assigned to nodes or fields. They will be returned in case
 * that the node or field does not exist in the runtime objects or associated files.<br>
 * <br>
 * XML sample:
 * 
 * <pre>
 * 	Declare a set of variables in class EBaseSettings.java:
 * 	<code><b>public static final BaseDispatchComponent APP_AUTO_POSITION = new BaseDispatchComponent(EBaseSettings.class.getSimpleName(), "AutoPosition", "/AppSettings", "true");
 * 	public static final BaseDispatchComponent APP_LANGUAGE = new BaseDispatchComponent(EBaseSettings.class.getSimpleName(), "Language", "/AppSettings", "en");
 * 	public static final BaseDispatchComponent APP_LOGFILE = new BaseDispatchComponent(EBaseSettings.class.getSimpleName(), "Logfile", "/AppSettings", "./logs/Application.log");
 * 	</b></code>
 * 
 * 	Assign a XML file to the EBaseSettings class within the main class of the application where the settings are used:
 * 	<code><b>BaseDispatcher.setDataContainer(EBaseSettings.class, "./conf/AppSettings.xml");</b></code>
 * 
 * 	Read a value from the settings by using getter method of the BaseDispatchComponent instance:
 * 	<code><b>System.out.println(EBaseSettings.APP_LANGUAGE.getValue());</b></code>
 * 	=&gt; if the tag &lt;Language&gt; exist in the XML file, than the value of the tag will be returned (fr)
 * 	=&gt; if the tag &lt;Language&gt; does not exist in the XML file, the default defined in the declaration will be returned (en)
 * 
 * 	Write the value of a tag into the associated XML file:
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
	 * This property assigns the key name of the related DataContainer to each BaseDispatchComponent
	 * variable.
	 */
	private final String settingsKey;

	/**
	 * This property is used to define the name of the node (tree formats) or field header (tabular
	 * formats) within the associated DataContainer. Each BaseDispatchComponent variable has a storage
	 * location, defined by the node name and path (tree formats) or column and row (tabular formats).
	 */
	private final String parameterName;

	/**
	 * This property is used to define the path within the tree format of the associated DataContainer,
	 * where the node of the BaseDispatchComponent variable is stored.<br>
	 * <br>
	 * Sample XPath definition for XML structure:
	 * "/parserRules/rule[@name='{param_1}']/element[@name='{param_2}']"
	 */
	private final String parentXPath;

	/**
	 * This property is used to assign a default value to the BaseDispatchComponent variable which will
	 * be returned by the getValue method in case that no settings file entry with the XPath and node
	 * name exists.
	 */
	private final String defaultValue;

	/**
	 * Hide no argument constructor because a BaseDispatchComponent without parameter cannot be used.
	 */
	@SuppressWarnings("unused")
	private BaseDispatchComponent() {
		settingsKey = null;
		parameterName = null;
		parentXPath = null;
		defaultValue = null;
	}

	/**
	 * Constructor with the settings class and the field name. The XPath and the default value are
	 * unused (set to blank). Can be used for tabular formats or tree formats (if no XPath gets used).
	 * 
	 * @param parentClass The dispatcher class where the BaseDispatchComponent variable is declared. If
	 *                    the class is an extended subclass, then the superclass needs to be used.
	 * @param paramName   Name of a node, associated with the variable
	 */
	public BaseDispatchComponent(Class<?> parentClass, String paramName) {
		this(parentClass, paramName, "", "");
	}

	/**
	 * Constructor used for tabular formats like .properties or .csv Files or tree formats (if no XPath
	 * gets used).
	 * 
	 * @param parentClass The dispatcher class where the BaseDispatchComponent variable is declared. If
	 *                    the class is an extended subclass, then the superclass needs to be used.
	 * @param paramName   Header name of the field. Tabular formats can either have column header or row
	 *                    headers.
	 * @param dVal        Default value returned by the variable in cases the field doesn't exist in the
	 *                    associated DataContainer or file.
	 */
	public BaseDispatchComponent(Class<?> parentClass, String paramName, String dVal) {
		this(parentClass, paramName, "", dVal);
	}

	/**
	 * This constructor is used to declare variables for tree formats like XML, JSON or YAML.
	 * 
	 * @param parentClass The dispatcher class where the BaseDispatchComponent variable is declared. If
	 *                    the class is an extended subclass, then the superclass needs to be used.
	 * @param paramName   Name of a node, associated with the variable
	 * @param pxp         Path, where the tag is located within a tree structure
	 * @param dVal        Default value returned by the variable in case the node doesn't exist in the
	 *                    associated DataContainer or file
	 */
	public BaseDispatchComponent(Class<?> parentClass, String paramName, String pxp, String dVal) {
		settingsKey = parentClass.getSimpleName();
		parameterName = paramName;
		parentXPath = pxp;
		defaultValue = dVal;

		if (!BaseDispatcher.dcMap.containsKey(settingsKey)) {
			DataContainer dc = DataContainer.newContainer(EContainerFormat.CSV);
			if (StringUtils.isNotBlank(parentXPath)) {	
				dc = DataContainer.newContainer(EContainerFormat.XML);
				dc.xmlInstance().setRootNode(getRootNode()); 			
			} 
			BaseDispatcher.setDataContainer(parentClass, dc);
		}
	}

	/**
	 * This method adds a node or field with the defined value into the DataContainer and file that is
	 * associated with the {@link org.opentdk.api.dispatcher.BaseDispatchComponent} variable.<br>
	 * <br>
	 * 
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
	 * @param value The value which will be set to the node or field
	 */
	public void addValue(String value) {
		addValue("", value);
	}

	/**
	 * This method adds a node or field with the defined value into the DataContainer and file that is
	 * associated with the {@link org.opentdk.api.dispatcher.BaseDispatchComponent} variable. <br>
	 * <br>
	 * 
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
	 * @param params - Semicolon separated string with attribute values of the parent nodes that
	 *               describe the path more uniquely.
	 * @param value  - The value which will be set to the node or field
	 */
	public void addValue(String params, String value) {
		addValue(params, value, false);
	}

	/**
	 * This method adds a node or field with the defined value into the DataContainer and file that is
	 * associated with the {@link org.opentdk.api.dispatcher.BaseDispatchComponent} variable.<br>
	 * <br>
	 * 
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
	 * @param value        The value which will be set to the node or field
	 * @param noDuplicates true = do not create duplicate nodes or fields; false = create duplicate
	 *                     nodes or fields
	 */
	public void addValue(String value, boolean noDuplicates) {
		addValue("", value, noDuplicates);
	}

	/**
	 * This method adds a node or tag with the defined value into the DataContainer and file that is
	 * associated with the {@link org.opentdk.api.dispatcher.BaseDispatchComponent} variable.<br>
	 * <br>
	 * 
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
	 * @param params       Semicolon separated string with attribute values of the parent nodes that
	 *                     describe the path more uniquely.
	 * @param value        The value which will be set to the new node
	 * @param noDuplicates true = do not create duplicate nodes; false = create duplicate nodes
	 */
	// ToDo: Filter not allowed for tabular formats!
	public void addValue(String params, String value, boolean noDuplicates) {
		String pxp = resolveXPath(params);
		Filter fltr = new Filter();
		if (!parentXPath.isEmpty()) {
			fltr.addFilterRule("XPath", pxp, EOperator.EQUALS);
		}
		if (noDuplicates && ListUtil.asList(getValues()).contains(value)) {
			return;
		}
		BaseDispatcher.getDataContainer(settingsKey).add(parameterName, value, fltr);
	}

	/**
	 * Deletes all nodes or fields within the associated DataContainer and file that are representing
	 * entries for this {@link org.opentdk.api.dispatcher.BaseDispatchComponent}.<br>
	 * <br>
	 * 
	 * XML Sample<br>
	 * The method call:
	 * 
	 * <pre>
	 * EBaseAppSettings.APP_TRACE_LEVEL.delete();
	 * </pre>
	 * 
	 * performed on the following BaseDispatchComponent variable, defined within the settings class
	 * EBaseAppSettings:
	 * 
	 * <pre>
	 * public static final BaseDispatchComponent APP_TRACE_LEVEL = new BaseDispatchComponent(EBaseAppSettings.class, "TraceLevel", "/AppSettings", "1");
	 * </pre>
	 * 
	 * will delete the tag TraceLevel of the following XML sample:
	 * 
	 * <pre>
	 * 	&lt;AppSettings&gt;
	 * 		&lt;TraceLevel&gt;1&lt;/TraveLevel&gt;
	 * 	&lt;/AppSettings&gt;
	 * </pre>
	 * 
	 * If multiple tags with the same name and XPath exist, then all matching tags will be deleted.
	 */
	public void delete() {
		delete("", "", "");
	}

	/**
	 * Deletes all matching nodes or fields with an assigned attribute within the associated
	 * DataContainer and file that are representing entries for this
	 * {@link org.opentdk.api.dispatcher.BaseDispatchComponent}.<br>
	 * <br>
	 * 
	 * XML Sample<br>
	 * The method call:
	 * 
	 * <pre>
	 * EBaseAppSettings.APP_TRACE_LEVEL.delete("name", "Security");
	 * </pre>
	 * 
	 * performed on the following BaseDispatchComponent variable, defined within the settings class
	 * EBaseAppSettings:
	 * 
	 * <pre>
	 * public static final BaseDispatchComponent APP_TRACE_LEVEL = new BaseDispatchComponent(EBaseAppSettings.class, "TraceLevel", "/AppSettings", "1");
	 * </pre>
	 * 
	 * will delete the tag TraceLevel of the following XML sample:
	 * 
	 * <pre>
	 * 	&lt;AppSettings&gt;
	 * 		&lt;TraceLevel name="Security"&gt;1&lt;/TraveLevel&gt;
	 * 	&lt;/AppSettings&gt;
	 * </pre>
	 * 
	 * If multiple tags with the same name and XPath exist, then all matching tags will be deleted.
	 * 
	 * @param attrName  Attribute name of the node or field that will be deleted
	 * @param attrValue Attribute value of the node or field that will be deleted
	 */
	public void delete(String attrName, String attrValue) {
		delete("", attrName, attrValue);
	}

	/**
	 * Deletes all matching nodes or fields with an assigned attribute within the associated
	 * DataContainer and file that are representing entries for this
	 * {@link org.opentdk.api.dispatcher.BaseDispatchComponent}. In addition the nodes within the path
	 * hierarchy can be identified by an attribute<br>
	 * <br>
	 * 
	 * XML Sample<br>
	 * The method call:
	 * 
	 * <pre>
	 * ECollectionRules.QUERY_FILTERVALUE.delete("Search LK Test;COMPANY_NAME", "", "");
	 * </pre>
	 * 
	 * performed on the following BaseDispatchComponent variable, defined within the settings class
	 * ECollectionRules:
	 * 
	 * <pre>
	 * public static final BaseDispatchComponent QUERY_FILTERVALUE = new BaseDispatchComponent(ECollectionRules.class.getSimpleName(), "value", "/Rules/rule[@name='{param_1}']/Query/filter[@column='{param_2}']", "");
	 * </pre>
	 * 
	 * will delete the tag <b>value</b> with the value <b>LK Test Solutions GmbH</b> of the following
	 * XML sample:
	 * 
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
	 * 
	 * If multiple tags with the same name and XPath exist, then all matching tags will be deleted.
	 * 
	 * @param params    Semicolon separated string with attribute values of parent nodes within the path
	 *                  hierarchy.
	 * @param attrName  Attribute name of the node or field that will be deleted
	 * @param attrValue Attribute value of the node or field that will be deleted
	 */
	public void delete(String params, String attrName, String attrValue) {
		String xPath = resolveXPath(params);
		Filter fltr = new Filter();
		if (!parentXPath.isEmpty()) {
			fltr.addFilterRule("XPath", xPath, EOperator.EQUALS);
		}
		BaseDispatcher.getDataContainer(settingsKey).delete(parameterName, attrName, attrValue, fltr);
	}

	/**
	 * This method returns the attribute value of the of the first matching node that is associated with
	 * this BaseDispatchComponent instance.
	 * 
	 * <pre>
	 * XML Sample:
	 * <b>EAppSettings.POS.getAttribute("axis");</b>
	 * executed on the following XML content:
	 * <b>&lt;AppSettings&gt;&lt;Geometry&gt;&lt;Pos axis="X"&gt;50&lt;/Pos&gt;&lt;/Geometry&gt;&lt;/AppSettings&gt;</b>
	 * returns the value "X"
	 * </pre>
	 * 
	 * @param attrName the name of the nodes attribute to search for
	 * @return the value of the defined attribute
	 */
	public String getAttribute(String attrName) {
		String expr = parameterName;
		if (!parentXPath.isEmpty()) {
			expr = parentXPath + "/" + parameterName;
		}
		DataContainer dc = BaseDispatcher.getDataContainer(settingsKey);
		String[] attributes = new String[0];
		if (dc.isTree() && dc.isXML()) {
			attributes = dc.xmlInstance().get(expr, attrName);
		}
		if (attributes.length == 0) {
			return "";
		}
		return attributes[0];
	}

	/**
	 * This method returns the attribute value of the first matching node that is associated with this
	 * BaseDispatchComponent instance and where the nodes within the hierarchical path include the
	 * attribute values that are defined by the params argument.
	 * 
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
	 * @param params   a semicolon separated string with attribute values within the path
	 * @param attrName the name of the node attribute to search for
	 * @return the value of the matching attribute
	 */
	public String getAttribute(String params, String attrName) {
		String expr = parameterName;
		if (!parentXPath.isEmpty()) {
			expr = resolveXPath(params) + "/" + parameterName;
		}
		DataContainer dc = BaseDispatcher.getDataContainer(settingsKey);
		String[] attributes = new String[0];
		if (dc.isTree() && dc.isXML()) {
			attributes = dc.xmlInstance().get(expr, attrName);
		}
		if (attributes.length == 0) {
			return "";
		}
		return attributes[0];
	}

	/**
	 * This method returns the attribute values of all associated nodes of this BaseDispatchComponent
	 * instance.
	 * 
	 * <pre>
	 * XML sample:
	 * <b>EAppSettings.POS.getAttributes("axis");</b>
	 * executed on the following XML content:
	 * <b>&lt;AppSettings&gt;&lt;Geometry&gt;&lt;Pos axis="X"&gt;50&lt;/Pos&gt;&lt;/Geometry&gt;&lt;/AppSettings&gt;</b>
	 * <b>&lt;AppSettings&gt;&lt;Geometry&gt;&lt;Pos axis="Y"&gt;100&lt;/Pos&gt;&lt;/Geometry&gt;&lt;/AppSettings&gt;</b>
	 * returns the values ["X", "Y"]
	 * </pre>
	 * 
	 * @param attrName the name of the nodes attribute to search for
	 * @return the values of all matching attributes as array of type String
	 */
	public String[] getAttributes(String attrName) {
		String expr = parameterName;
		if (!parentXPath.isEmpty()) {
			expr = parentXPath + "/" + parameterName;
		}
		DataContainer dc = BaseDispatcher.getDataContainer(settingsKey);
		String[] attributes = new String[0];
		if (dc.isTree() && dc.isXML()) {
			attributes = dc.xmlInstance().get(expr, attrName);
		}
		if (attributes.length == 0) {
			return new String[0];
		}
		return attributes;
	}

	/**
	 * This method returns the attribute values of all matching nodes that are associated with this
	 * BaseDispatchComponent instance and where the nodes within the hierarchical path include the
	 * attribute values that are defined by the params argument.
	 * 
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
	 * @param params   a semicolon separated string with attribute values within the path
	 * @param attrName the name of the nodes attribute to search for
	 * @return the values of all matching attribute as array of type String
	 */
	public String[] getAttributes(String params, String attrName) {
		String expr = parameterName;
		if (!parentXPath.isEmpty()) {
			expr = resolveXPath(params) + "/" + parameterName;
		}
		DataContainer dc = BaseDispatcher.getDataContainer(settingsKey);
		String[] attributes = new String[0];
		if (dc.isTree() && dc.isXML()) {
			attributes = dc.xmlInstance().get(expr, attrName);
		}
		if (attributes.length == 0) {
			return new String[0];
		}
		return attributes;
	}

	/**
	 * Returns the parameterName property of the
	 * {@link org.opentdk.api.dispatcher.BaseDispatchComponent}.
	 * 
	 * @return The value of the parameterName property as string
	 * @see org.opentdk.api.dispatcher.BaseDispatchComponent#parameterName
	 */
	public String getName() {
		return parameterName;
	}

	public String getRootNode() {
		String rootNode = "";
		String[] nodeArray = parentXPath.split("/");
		if (nodeArray.length > 0) {
			if (StringUtils.isBlank(nodeArray[0])) {
				if (nodeArray.length > 1) {
					rootNode = nodeArray[1].replace("/", "");
				}
			} else {
				rootNode = nodeArray[0].replace("/", "");
			}
			if(rootNode.contains("[")) {
				rootNode = rootNode.split("\\[")[0];
			}
		}
		return rootNode;
	}

	/**
	 * This method retrieves the value that is assigned to the
	 * {@link org.opentdk.api.dispatcher.BaseDispatchComponent} variable in the following way:<br>
	 * {@literal -} Search the node or field, defined by property {@link #parameterName}. For tree
	 * formats the node will be searched within the XPath, defined by property {@link #parentXPath}<br>
	 * {@literal -} If the node or field does not exist within the associated DataContainer, the default
	 * value defined by property {@link #defaultValue} will be retrieved.<br>
	 * {@literal -} If multiple nodes or fields exist, then the first value will be retrieved
	 * 
	 * @return the value, assigned to the {@link org.opentdk.api.dispatcher.BaseDispatchComponent}
	 *         variable as String
	 */
	public String getValue() {
		return getValue(0);
	}

	public String getValue(int valueIndex) {
		if ((parentXPath != null) && (!parentXPath.isEmpty())) {
			return getValue("");
		}
		if (BaseDispatcher.dcMap.containsKey(settingsKey)) {
			DataContainer dc = BaseDispatcher.getDataContainer(settingsKey);
			String[] ret = dc.get(parameterName);
			if(ret != null && ret.length > 0 && ret[valueIndex] != null) {
				return ret[valueIndex];
			}
		}
		return defaultValue;
	}

	/**
	 * This method retrieves the value that is assigned to the
	 * {@link org.opentdk.api.dispatcher.BaseDispatchComponent} variable in the following way:<br>
	 * {@literal -} Search the node or field, defined by property {@link #parameterName}. For tree
	 * formats the node will be searched within the XPath, defined by property {@link #parentXPath}<br>
	 * {@literal -} If the node or field does not exist within the associated DataContainer, the default
	 * value defined by property {@link #defaultValue} will be retrieved.<br>
	 * {@literal -} If multiple nodes or fields exist, then the first value will be retrieved
	 * 
	 * @param params Semicolon separated string with the values of node attributes within the
	 *               parentXPath
	 * @return the value, assigned to the {@link org.opentdk.api.dispatcher.BaseDispatchComponent}
	 *         variable as String
	 */
	public String getValue(String params) {
		String outVal = defaultValue;
		if (BaseDispatcher.dcMap.containsKey(settingsKey)) {
			String[] values = getValues(params);
			if (values.length > 0) {
				outVal = values[0];
			}
		}
		return outVal;
	}

	/**
	 * This method retrieves the values that are assigned to the
	 * {@link org.opentdk.api.dispatcher.BaseDispatchComponent} variable in the following way:<br>
	 * {@literal -} Search the nodes or fields, defined by property {@link #parameterName}. For tree
	 * formats the nodes will be searched within the XPath, defined by property {@link #parentXPath}<br>
	 * {@literal -} If the nodes or fields do not exist within the associated DataContainer, the default
	 * value defined by property {@link #defaultValue} will be retrieved.<br>
	 * {@literal -} Otherwise the values of all matching nodes or fields will be retrieved.
	 * 
	 * @return the values, assigned to the {@link org.opentdk.api.dispatcher.BaseDispatchComponent}
	 *         variable as Array of type String
	 */
	public String[] getValues() {
		return getValues("");
	}

	/**
	 * This method retrieves the values that are assigned to the
	 * {@link org.opentdk.api.dispatcher.BaseDispatchComponent} variable in the following way:<br>
	 * {@literal -} Search the nodes or fields, defined by property {@link #parameterName}. For tree
	 * formats the nodes will be searched within the XPath, defined by property {@link #parentXPath}<br>
	 * {@literal -} If the nodes or fields do not exist within the associated DataContainer, the default
	 * value defined by property {@link #defaultValue} will be retrieved.<br>
	 * {@literal -} Otherwise the values of all matching nodes will be retrieved
	 * 
	 * @param params - Semicolon separated string with the values of tag attributes within the
	 *               parentXPath
	 * @return the values, assigned to the {@link org.opentdk.api.dispatcher.BaseDispatchComponent}
	 *         variable as Array of type String
	 */
	public String[] getValues(String params) {
		Filter fltr = new Filter();
		if ((parentXPath != null) && (!parentXPath.isEmpty())) {
			String pxp = resolveXPath(params);
			fltr.addFilterRule("XPath", pxp, EOperator.EQUALS);
		}
		DataContainer dc = BaseDispatcher.getDataContainer(settingsKey);
		String[] ret = dc.get(parameterName, fltr);
		if (ret.length > 0) {
			return ret;
		}
		return new String[0];
	}

	/**
	 * This method replaces all parameter wild cards of the parentXPath of a BaseDispatchComponent
	 * instance by the values defined in the committed parameter string.
	 * 
	 * <pre>
	 * E.g. 
	 * <b>resolveXPath("CUSTOMERS;LASTNAME");</b>
	 * executed by the following declared BaseDispatchComponent:
	 * <b>public static final BaseDispatchComponent OUTPUT_COLUMNNAME = new BaseDispatchComponent(ECollectionRules.class.getSimpleName(), "column", "/Rules/rule[@name='{param_1}']/Output/ResultTypes/resultType[@name='{param_2}']", "");</b>
	 * will transform the XPath 
	 * <b>"/Rules/rule[@name='{param_1}']/Output/ResultTypes/resultType[@name='{param_2}']"</b>
	 * into
	 * <b>"/Rules/rule[@name='CUSTOMERS']/Output/ResultTypes/resultType[@name='LASTNAME']"</b>
	 * </pre>
	 * 
	 * In case that one parameter contains a '=' the left string gets used as variable attribute and the
	 * right string as value.
	 * 
	 * <pre>
	 * E.g. 
	 * <b>resolveXPath("name=CUSTOMERS;name=LASTNAME");</b>
	 * executed by the following declared BaseDispatchComponent:
	 * <b>public static final BaseDispatchComponent OUTPUT_COLUMNNAME = new BaseDispatchComponent(ECollectionRules.class.getSimpleName(), "column", "/Rules/rule[@{attribute_1}='{param_1}']/Output/ResultTypes/resultType[@{attribute_2}='{param_2}']", "");</b>
	 * will transform the XPath 
	 * <b>"/Rules/rule[@{attribute_1}='{param_1}']/Output/ResultTypes/resultType[@{attribute_2}='{param_2}']"</b>
	 * into
	 * <b>"/Rules/rule[@name='CUSTOMERS']/Output/ResultTypes/resultType[@name='LASTNAME']"</b>
	 * </pre>
	 * 
	 * In case that multiple attributes should be taken into account, another expression in square
	 * brackets has to be added. Make sure to have a unique index in the parameter declaration.
	 * 
	 * <pre>
	 * E.g. 
	 * <b>resolveXPath("name=CUSTOMERS;id=1;name=LASTNAME");</b>
	 * executed by the following declared BaseDispatchComponent:
	 * <b>public static final BaseDispatchComponent OUTPUT_COLUMNNAME = new BaseDispatchComponent(ECollectionRules.class.getSimpleName(), "column", "/Rules/rule[@{attribute_1}='{param_1}'][@{attribute_2}='{param_2}']/Output/ResultTypes/resultType[@{attribute_3}='{param_3}']", "");</b>
	 * will transform the XPath 
	 * <b>"/Rules/rule[@{attribute_1}='{param_1}'][@{attribute_2}='{param_2}']/Output/ResultTypes/resultType[@{attribute_3}='{param_3}']"</b>
	 * into
	 * <b>"/Rules/rule[@name='CUSTOMERS'][@id='1']/Output/ResultTypes/resultType[@name='LASTNAME']"</b>
	 * </pre>
	 * 
	 * Note that the variable attribute is not quoted and the index always has to be the same than the
	 * value index.<br>
	 * <br>
	 * 
	 * @param params Semicolon separated string with attribute values of parent nodes within the XPath
	 *               hierarchy
	 * @return transformed XPath with substituted attribute values of the parent nodes within the XPath
	 *         hierarchy
	 */
	private String resolveXPath(String params) {
		String pxp = parentXPath;
		if (StringUtils.isBlank(params)) {
			// remove all attributes and parameter placeholder like "[@name='{param_1}']"
			pxp = pxp.replaceAll("param_[0-9]*", "");
			pxp = pxp.replaceAll("attribute_[0-9]*", "");
			// Replace the brackets in two steps to avoid the replacement of the whole string in the xPath in case of multiple attributes
			pxp = pxp.replaceAll("\\[@.*\\='\\{\\}'\\]/", "/");
			pxp = pxp.replaceAll("\\[@.*\\='\\{\\}'\\]", "");
		} else {
			String[] paramsToSet = params.split(";");
			// replace parameter placeholders by parameter values
			for (int i = 0; i < paramsToSet.length; i++) {
				// syntax attribute=value allows variable attribute names
				if (paramsToSet[i].contains("=")) {
					String attributeToSet = paramsToSet[i].split("=")[0];
					String valueToSet = paramsToSet[i].split("=")[1];
					pxp = pxp.replace("{attribute_" + String.valueOf(i + 1) + "}", attributeToSet);
					pxp = pxp.replace("{param_" + String.valueOf(i + 1) + "}", valueToSet);
				} else {
					pxp = pxp.replace("{param_" + String.valueOf(i + 1) + "}", paramsToSet[i]);
				}
			}
		}
		return pxp;
	}

	/**
	 * Sets the attribute of a node. The method overwrites identical node with this attribute, in case
	 * the existing attributes value is empty. In any other case the tag will be added to the
	 * parendXPath.
	 * 
	 * @param attrName  Attribute name of the node
	 * @param attrValue Attribute value of the node that will be set
	 */
	public void setAttribute(String attrName, String attrValue) {
		setAttribute("", attrName, attrValue);
	}

	/**
	 * Sets the attribute of a node. The method overwrites identical node with this attribute, in case
	 * the existing attributes value is empty. In any other case the node will be added to the
	 * parendXPath.
	 * 
	 * @param params    Semicolon separated string with attribute values of parent tags within the XPath
	 *                  hierarchy.
	 * @param attrName  Attribute name of the node
	 * @param attrValue Attribute value of the node that will be set
	 */
	public void setAttribute(String params, String attrName, String attrValue) {
		setAttribute("", params, attrName, attrValue);
	}

	/**
	 * Sets the attribute of a node. The method overwrites identical node with this attribute, in case
	 * the existing attributes value is empty. In any other case the node will be added to the
	 * parendXPath.
	 * 
	 * @param oldValue  Attribute value of the node that will be searched and overwritten
	 * @param params    Semicolon separated string with attribute values of parent nodes within the
	 *                  XPath hierarchy.
	 * @param attrName  Attribute name of the node
	 * @param attrValue Attribute value of the node that will be set
	 */
	public void setAttribute(String oldValue, String params, String attrName, String attrValue) {
		String xPath = resolveXPath(params);
		Filter fltr = new Filter();
		fltr.addFilterRule("XPath", xPath, EOperator.EQUALS);
		DataContainer dc = BaseDispatcher.getDataContainer(settingsKey);
		if(dc.isTree() && dc.isXML()) {
			dc.xmlInstance().set(parameterName, attrName, oldValue, attrValue, fltr);
		}
	}

	/**
	 * This method searches within the associated document for the first tree node or field, that
	 * matches the name and path (for tree formats) of this {@link BaseDispatchComponent} instance and
	 * assigns the value to this node/field. If no node/field is found, then a new node/field will be
	 * created. If one or more nodes/fields exist, then the value of the first matching node/field will
	 * be overwritten.
	 * 
	 * <pre>
	 * XML Sample:
	 * <b>EAppSettings.POSX.setValue("100");</b>
	 * Replaces the value 50 of the following XML tag by 100:
	 * <b>&lt;AppSettings&gt;&lt;Geometry&gt;&lt;PosX&gt;50&lt;/PosX&gt;&lt;/Geometry&gt;&lt;/AppSettings&gt;</b>
	 * </pre>
	 * 
	 * @param value the value that will be written into the node or field
	 */
	public void setValue(String value) {
		if ((parentXPath == null) || (parentXPath.isEmpty())) {
			BaseDispatcher.getDataContainer(settingsKey).set(parameterName, value);
		} else {
			setValue("", value);
		}
	}

	public void setValue(int valueIndex, String value) {
		if ((parentXPath == null) || (parentXPath.isEmpty())) {
			if(valueIndex >= BaseDispatcher.getDataContainer(settingsKey).tabInstance().getRowCount()) {
				BaseDispatcher.getDataContainer(settingsKey).tabInstance().addRow();
			}
			BaseDispatcher.getDataContainer(settingsKey).tabInstance().setValue(parameterName, valueIndex, value);
		} else {
			// ToDo: implementation for tree format, if required
		}
	}

	/**
	 * This method searches within the associated document for the first node that matches the name and
	 * path of this {@link BaseDispatchComponent} and assigns the value to this field. As additional
	 * search criteria, the params argument can be used to enclose the search to path nodes with
	 * specific attribute values. If no node is found, then a new node will be created. If one or more
	 * nodes exist, then the value of the first matching node will be overwritten.
	 * 
	 * <pre>
	 * XML sample:
	 * <b>EAppSettings.POSX.setValue("Help", "100");</b>
	 * Replaces the value 50 of the following XML tag by 100:
	 * <b>&lt;AppSettings&gt;&lt;Geometry window="Help"&gt;&lt;PosX&gt;50&lt;/PosX&gt;&lt;/Geometry&gt;&lt;/AppSettings&gt;</b>
	 * </pre>
	 * 
	 * @param params Semicolon separated string with the values of node attributes within the nodes path
	 * @param value  the value that will be written into the node
	 */
	public void setValue(String params, String value) {
		setValue(params, "", value);
	}

	/**
	 * This method searches within the associated document for the first node that matches the node
	 * name, path and the defined value (oldValue argument) of this {@link BaseDispatchComponent} and
	 * replaces the value of this node by newValue. As additional search criteria, the params argument
	 * can be used to enclose the search to path nodes with specific attribute values. Use an empty
	 * string for the params argument to search for all path nodes by name only. If no node is found,
	 * then a new node will be created. If one or more nodes exist, then the value of the first matching
	 * node will be overwritten.
	 * 
	 * <pre>
	 * XML sample:
	 * <b>EAppSettings.POSX.setValue("Help", "50", "100");</b>
	 * Assigns the value 100 to the PosX tag, only if the tag already exists within the XML document and only when the current value is 50.
	 * Replaces the value 50 of the following XML tag by 100:
	 * <b>&lt;AppSettings&gt;&lt;Geometry window="Help"&gt;&lt;PosX&gt;50&lt;/PosX&gt;&lt;/Geometry&gt;&lt;/AppSettings&gt;</b>
	 * </pre>
	 * 
	 * @param params   Semicolon separated string with the values of tag attributes within the parent
	 *                 path
	 * @param oldValue the node value that will be replaced
	 * @param newValue the value that will overwrite the oldValue
	 */
	public void setValue(String params, String oldValue, String newValue) {
		String xPath = resolveXPath(params);
		Filter fltr = new Filter();
		fltr.addFilterRule("XPath", xPath, EOperator.EQUALS);
		if (StringUtils.isNotBlank(oldValue)) {
			fltr.addFilterRule(parameterName, oldValue, EOperator.EQUALS);
		}
		BaseDispatcher.getDataContainer(settingsKey).set(parameterName, newValue, fltr);		
	}

	/**
	 * This method will replace the values of all nodes or fields within the associated document that
	 * match with the name and path (for tree formats) of this {@link BaseDispatchComponent} and where
	 * the current value is equal to the value defined by oldValue argument. The argument allOccurences
	 * defines if all matching node or field values will be replaced or only the first one.
	 * 
	 * @param oldValue      The value of a node or field that will be searched for
	 * @param newValue      The value that will be used to overwrite the found nodes or fields
	 * @param allOccurences true = overwrite all matching node or field values; false = overwrite only
	 *                      the first matching node or field value
	 */
	public void setValues(String oldValue, String newValue, boolean allOccurences) {
		String xPath = "";
		Filter fltr = new Filter();
		if ((parentXPath != null) || (!parentXPath.isEmpty())) {
			xPath = resolveXPath("");
			fltr.addFilterRule("XPath", xPath, EOperator.EQUALS);
		}
		fltr.addFilterRule(parameterName, oldValue, EOperator.EQUALS);
		BaseDispatcher.getDataContainer(settingsKey).set(parameterName, newValue, fltr, allOccurences);	
	}

}
