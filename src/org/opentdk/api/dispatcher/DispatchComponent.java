package org.opentdk.api.dispatcher;

import org.opentdk.api.datastorage.DataContainer;
import org.opentdk.api.datastorage.EContainerFormat;
import org.opentdk.api.exception.DataContainerException;
import org.opentdk.api.filter.Filter;
import org.opentdk.api.filter.EOperator;
import org.apache.commons.lang3.StringUtils;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;
import java.util.List;

/**
 * Similar to the native <code><b>enum</b></code> type of Java, the {@link DispatchComponent} class is used to build a set of variables with the difference that their values are not static and
 * extended access to the variables values and their attributes is provided.<br> Each {@link DispatchComponent} instance will be declared within a dispatcher definition class like EBaseSettings and
 * the instance is associated with a node or field, depending on the format of the underlying {@link DataContainer} (tree or table format).<br>
 * <br>
 * Main features of {@link DispatchComponent}:<br> {@literal -} Values and attributes of defined nodes or fields can be read from and written to the structure of the underlying
 * {@link DataContainer}.<br> {@literal -} In case the defined file does not exist, it will be created once a setter method is called.<br> {@literal -} In case that no file is associated with the
 * dispatcher component, their values will be stored in runtime objects (DOM Document, HashMap, ArrayList etc.) depending on the container format.<br> {@literal -} Default values can be assigned to
 * nodes or fields. They will be returned in case that the node or field does not exist in the runtime objects or associated files.<br>
 * <br>
 * XML sample:
 * <br><br>
 * Declaration as described in {@link MainDispatcher} or like this:
 *
 * <pre>
 * public class EBaseSettings extends MainDispatcher {
 *
 *     public EBaseSettings(DataContainer dc, boolean createFile, String root) {
 *         super(dc, createFile, root);
 *     }
 *
 *     public final DispatchComponent APP_LANGUAGE = new DispatchComponent(getDataContainer(), "Language", "/AppSettings", "en");
 * }
 * </pre>
 *
 * 	Assign an XML file to the EBaseSettings class within the main class of the application where the settings are used:
 * 	<pre>
 * 	<code><b>DataContainer dc = DataContainer.newContainer("~tmp/test.xml");</b></code>
 * 	<code><b>EBaseSettings settings = new EBaseSettings(dc, true, "AppSettings");</b></code>
 *  </pre>
 *
 * 	Read a value from the settings by using getter method of the {@link DispatchComponent} instance:
 * 	<pre>
 * 	<code><b>System.out.println(settings.APP_LANGUAGE.getValue());</b></code>
 * 	</pre>
 * 	=&gt; if the tag &lt;Language&gt; exist in the XML file, then the value of the tag will be returned (fr)<br>
 * 	=&gt; if the tag &lt;Language&gt; does not exist in the XML file, the default defined in the declaration will be returned (en)<br><br>
 *
 * 	Write the value of a tag into the associated XML file:
 * 	<pre>
 * 	<code><b>settings.APP_LANGUAGE.setValue("de");</b></code>
 *  </pre>
 *
 * 	Sample content of an associated XML file:
 * 	<pre>
 * 	<code><b>&lt;?xml version="1.0" encoding="UTF-8" standalone="no"?&gt;
 * 	&lt;AppSettings&gt;
 *   	&lt;Language&gt;fr&lt;/Language&gt;
 *   	&lt;Logfile&gt;./logs/mp.log&lt;/Logfile&gt;
 * 	&lt;/AppSettings&gt;</b></code>
 * </pre>
 *
 * @author FME (LK Test Solutions)
 */
public class DispatchComponent {

    private final DataContainer container;

    /**
     * This property is used to define the name of the node (tree formats) or field header (tabular formats) within the associated DataContainer. Each BaseDispatchComponent variable has a storage
     * location, defined by the node name and path (tree formats) or column and row (tabular formats).
     */
    private final String parameterName;

    /**
     * This property is used to define the path within the tree format of the associated DataContainer, where the node of the BaseDispatchComponent variable is stored.<br>
     * <br>
     * Sample XPath definition for XML structure: "/parserRules/rule[@name='{param_1}']/element[@name='{param_2}']"
     */
    private final String parentXPath;

    /**
     * This property is used to assign a default value to the BaseDispatchComponent variable which will be returned by the getValue method in case that no settings file entry with the XPath and node
     * name exists.
     */
    private final String defaultValue;

    /**
     * Hide no argument constructor because a DispatchComponent without parameter cannot be used.
     */
    @SuppressWarnings("unused")
    private DispatchComponent() {
        container = null;
        parameterName = null;
        parentXPath = null;
        defaultValue = null;
    }

    /**
     * Constructor with the settings class and the field name. The XPath and the default value are unused (set to blank). Can be used for tabular formats or tree formats (if no XPath gets used).
     *
     * @param dc        {@link #container}
     * @param paramName Name of a node, associated with the variable
     */
    public DispatchComponent(DataContainer dc, String paramName) {
        this(dc, paramName, "", "");
    }

    /**
     * Constructor used for tabular formats like .properties or .csv Files or tree formats (if no XPath gets used).
     *
     * @param dc        {@link #container}
     * @param paramName Header name of the field. Tabular formats can either have column header or row headers.
     * @param dVal      Default value returned by the variable in cases the field doesn't exist in the associated DataContainer or file.
     */
    public DispatchComponent(DataContainer dc, String paramName, String dVal) {
        this(dc, paramName, "", dVal);
    }

    /**
     * This constructor is used to declare variables for tree formats like XML, JSON or YAML.
     *
     * @param dc        {@link #container}
     * @param paramName Name of a node, associated with the variable
     * @param pxp       Path, where the tag is located within a tree structure
     * @param dVal      Default value returned by the variable in case the node doesn't exist in the associated DataContainer or file
     */
    public DispatchComponent(DataContainer dc, String paramName, String pxp, String dVal) {
        container = dc;
        parameterName = paramName;
        parentXPath = pxp;
        defaultValue = dVal;
        if (StringUtils.isNotBlank(parentXPath)) {
            dc = DataContainer.newContainer(EContainerFormat.XML);
            try {
                dc.xmlInstance().initXmlEditor(retrieveRootNode());
            } catch (ParserConfigurationException | IOException | SAXException e) {
                throw new DataContainerException(e);
            }
        }
    }


    /**
     * Returns the parameterName property of the {@link DispatchComponent}.
     *
     * @return The value of the parameterName property as string
     * @see DispatchComponent#parameterName
     */
    public String getName() {
        return parameterName;
    }

    /**
     * Returns the parentXPath property of the {@link DispatchComponent}.
     *
     * @return The value of the parentXPath property as string
     * @see DispatchComponent#parentXPath
     */
    public String getParentXPath() {
        return parentXPath;
    }

    /**
     * Returns the defaultValue property of the {@link DispatchComponent}.
     *
     * @return The value of the defaultValue property as string
     * @see DispatchComponent#defaultValue
     */
    public String getDefaultValue() {
        return defaultValue;
    }

    public void addValue(String value) {
        addValue("", value);
    }

    public void addValue(String params, String value) {
        addValue(params, value, false);
    }

    public void addValue(String value, boolean noDuplicates) {
        addValue("", value, noDuplicates);
    }

    public void addValue(String params, String value, boolean noDuplicates) {
        String pxp = resolveXPath(params);
        Filter fltr = new Filter();
        if (!parentXPath.isEmpty()) {
            fltr.addFilterRule("XPath", pxp, EOperator.EQUALS);
        }
        if (noDuplicates && List.of(getValues()).contains(value)) {
            return;
        }
        container.add(parameterName, value, fltr);
    }

    public void delete() {
        delete("", "", "");
    }

    public void delete(String attrName, String attrValue) {
        delete("", attrName, attrValue);
    }

    public void delete(String params, String attrName, String attrValue) {
        String xPath = resolveXPath(params);
        Filter fltr = new Filter();
        if (!parentXPath.isEmpty()) {
            fltr.addFilterRule("XPath", xPath, EOperator.EQUALS);
        }
        container.delete(parameterName, attrName, attrValue, fltr);
    }

    public String getAttribute(String attrName) {
        String expr = parameterName;
        if (!parentXPath.isEmpty()) {
            expr = parentXPath + "/" + parameterName;
        }
        String[] attributes = new String[0];
        if (container.isTree() && container.isXML()) {
            try {
                attributes = container.xmlInstance().get(expr, attrName);
            } catch (XPathExpressionException e) {
                throw new DataContainerException(e);
            }
        }
        if (attributes.length == 0) {
            return "";
        }
        return attributes[0];
    }

    public String getAttribute(String params, String attrName) {
        String expr = parameterName;
        if (!parentXPath.isEmpty()) {
            expr = resolveXPath(params) + "/" + parameterName;
        }
        String[] attributes = new String[0];
        if (container.isTree() && container.isXML()) {
            try {
                attributes = container.xmlInstance().get(expr, attrName);
            } catch (XPathExpressionException e) {
                throw new DataContainerException(e);
            }
        }
        if (attributes.length == 0) {
            return "";
        }
        return attributes[0];
    }

    public String[] getAttributes(String attrName) {
        String expr = parameterName;
        if (!parentXPath.isEmpty()) {
            expr = parentXPath + "/" + parameterName;
        }
        String[] attributes = new String[0];
        if (container.isTree() && container.isXML()) {
            try {
                attributes = container.xmlInstance().get(expr, attrName);
            } catch (XPathExpressionException e) {
                throw new DataContainerException(e);
            }
        }
        if (attributes.length == 0) {
            return new String[0];
        }
        return attributes;
    }

    public String[] getAttributes(String params, String attrName) {
        String expr = parameterName;
        if (!parentXPath.isEmpty()) {
            expr = resolveXPath(params);
            if (StringUtils.isNotBlank(parameterName)) {
                expr = expr + "/" + parameterName;
            }
        }
        String[] attributes = new String[0];
        if (container.isTree() && container.isXML()) {
            try {
                attributes = container.xmlInstance().get(expr, attrName);
            } catch (XPathExpressionException e) {
                throw new DataContainerException(e);
            }
        }
        if (attributes.length == 0) {
            return new String[0];
        }
        return attributes;
    }

    public String retrieveRootNode() {
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
            if (rootNode.contains("[")) {
                rootNode = rootNode.split("\\[")[0];
            }
        }
        return rootNode;
    }

    public String getValue() {
        return getValue(0);
    }

    public String getValue(int valueIndex) {
        if ((parentXPath != null) && (!parentXPath.isEmpty())) {
            return getValue("");
        }
        String[] ret = container.get(parameterName);
        if (ret != null && ret.length > 0 && ret[valueIndex] != null) {
            return ret[valueIndex];
        }
        return defaultValue;
    }

    public String getValue(String params) {
        String outVal = defaultValue;
        String[] values = getValues(params);
        if (values.length > 0) {
            outVal = values[0];
        }
        return outVal;
    }

    public String[] getValues() {
        return getValues("");
    }

    public String[] getValues(String params) {
        Filter fltr = new Filter();
        if ((parentXPath != null) && (!parentXPath.isEmpty())) {
            String pxp = resolveXPath(params);
            fltr.addFilterRule("XPath", pxp, EOperator.EQUALS);
        }
        String[] ret = container.get(parameterName, fltr);
        if (ret.length > 0) {
            return ret;
        }
        return new String[0];
    }

    public void setAttribute(String attrName, String attrValue) {
        setAttribute("", attrName, attrValue);
    }

    public void setAttribute(String params, String attrName, String attrValue) {
        setAttribute("", params, attrName, attrValue);
    }

    public void setAttribute(String oldValue, String params, String attrName, String attrValue) {
        String xPath = resolveXPath(params);
        Filter fltr = new Filter();
        fltr.addFilterRule("XPath", xPath, EOperator.EQUALS);
        if (container.isTree() && container.isXML()) {
            try {
                container.xmlInstance().set(parameterName, attrName, oldValue, attrValue, fltr);
            } catch (IOException | TransformerException e) {
                throw new DataContainerException(e);
            }
        }
    }

    public void setValue(String value) {
        if ((parentXPath == null) || (parentXPath.isEmpty())) {
            container.set(parameterName, value);
        } else {
            setValue("", value);
        }
    }

//    public void setValue(int valueIndex, String value) {
//        if ((parentXPath == null) || (parentXPath.isEmpty())) {
//            if (valueIndex >= container.tabInstance().getRowCount()) {
//                //dc.tabInstance().addRow(new String[dc.tabInstance().getHeaders().size()]);
//                container.tabInstance().addRow();
//            }
//            container.tabInstance().setValue(parameterName, valueIndex, value);
//        } else {
//            // ToDo: implementation for tree format, if required
//        }
//    }

    public void setValue(String params, String value) {
        setValue(params, "", value);
    }

    public void setValue(String params, String oldValue, String newValue) {
        String xPath = resolveXPath(params);
        Filter fltr = new Filter();
        fltr.addFilterRule("XPath", xPath, EOperator.EQUALS);
        if (StringUtils.isNotBlank(oldValue)) {
            fltr.addFilterRule(parameterName, oldValue, EOperator.EQUALS);
        }
        container.set(parameterName, newValue, fltr);
    }

    public void setValues(String oldValue, String newValue, boolean allOccurences) {
        String xPath = "";
        Filter fltr = new Filter();
        if ((parentXPath != null) || (!parentXPath.isEmpty())) {
            xPath = resolveXPath("");
            fltr.addFilterRule("XPath", xPath, EOperator.EQUALS);
        }
        fltr.addFilterRule(parameterName, oldValue, EOperator.EQUALS);
        container.set(parameterName, newValue, fltr, allOccurences);
    }

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

}
