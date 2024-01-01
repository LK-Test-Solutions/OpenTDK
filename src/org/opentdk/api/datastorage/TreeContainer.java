package org.opentdk.api.datastorage;

import org.opentdk.api.filter.Filter;
import org.w3c.dom.Element;

public interface TreeContainer extends SpecificContainer {

	/**
	 * This method gets used to add a value into the data source.
	 * 
	 * @param name  Name of the element that will be added into the data source
	 * @param value Value that will be assigned to the name
	 */
	void add(String name, String value);

	/**
	 * This method gets used to add a value into the data source.
	 * 
	 * @param name   Name of the element that will be added into the data source
	 * @param value  Value that will be assigned to the name
	 * @param filter Filter condition for more precise localization of the element within the data
	 *               structure
	 */
	void add(String name, String value, Filter filter);

	/**
	 * This method gets used to add a value into the data source.
	 * 
	 * @param name   Name of the element that will be added into the data source
	 * @param attr   If the value has some attribute
	 * @param value  Value that will be assigned to the name
	 * @param filter Filter condition for more precise localization of the element within the data
	 *               structure
	 */
	void add(String name, String attr, String value, Filter filter);

	/**
	 * This method gets used to add a value into the data source.
	 * 
	 * @param name     Name of the element that will be added into the data source
	 * @param attr     If the value has some attribute
	 * @param value    Value that will be assigned to the name
	 * @param oldValue If an existing value has to be checked
	 * @param filter   Filter condition for more precise localization of the element within the data
	 *                 structure
	 */
	void add(String name, String attr, String value, String oldValue, Filter filter);

	/**
	 * This method gets used to delete a value from the data source.
	 * 
	 * @param name  Name of the element that will be added into the data source
	 * @param value Value that will be assigned to the name
	 */
	void delete(String name, String value);

	/**
	 * This method gets used to delete a value from the data source.
	 * 
	 * @param name   Name of the element that will be added into the data source
	 * @param value  Value that will be assigned to the name
	 * @param filter Filter condition for more precise localization of the element within the data
	 *               structure
	 */
	void delete(String name, String value, Filter filter);

	/**
	 * This method gets used to delete a value from the data source.
	 * 
	 * @param name   Name of the element that will be added into the data source
	 * @param attr   If the values has some attribute
	 * @param value  Value that will be assigned to the name
	 * @param filter Filter condition for more precise localization of the element within the data
	 *               structure
	 */
	void delete(String name, String attr, String value, Filter filter);

	/**
	 * Method used to retrieve a string array with the text content of all tree nodes specified by their name.
	 * This method will search on all levels of the tree hierarchy. The get method needs to be implemented for
	 * any tree container to support the functionality for the specific node types.<br>
	 * For {@link XMLDataContainer} a node is a tag-element and the retrieved value will be the content of the
	 * tag. <br>
	 * e.g. <code>get("Birthdate")</code> will retrieve the value <code>01.01.1970</code> from the following tag:<br>
	 * <code>&lt;Birthdate&gt;01.01.1970&lt;/Birthdate&gt;</code>
	 *
	 * @param nodeName Name of the tree node that the value will be read from
	 */
	String[] get(String nodeName);

	/**
	 * Method used to retrieve a string array with the text content of all tree nodes specified by their name.
	 * This method will search only within the path, defined by the filter.<br>
	 * Sample usage for XML-format:
	 * <pre>
	 *     Filter fltr = new Filter();
	 *     fltr.addFilterRule("XPath", "/Contacts/Business/Management", EOperator.EQUALS);
	 *     dataContainer.treeInstance.get("firstname", fltr);
	 * </pre>
	 *
	 * @param nodeName Name of the tag(s) to search for
	 * @param filter Filter condition for more precise localization of the element within the data structure
	 * @return String array with the text content of all found tags
	 */
	String[] get(String nodeName, Filter filter);

	/**
	 * This method gets used to retrieve values from the data source.
	 * 
	 * @param nodeName Name of the element that will be added into the data source
	 * @param attributeName If the name has some attribute
	 */
	String[] get(String nodeName, String attributeName);

	Object get(String nodeName, String attributeName, String attributeValue);

	Object[] get(String nodeName, Filter filter, String returnType);

	Object getRootElement();

	/**
	 * This method gets used to replace a value in the data source.
	 * 
	 * @param name  name of the element that will be added into the data source
	 * @param value value that will be assigned to the name
	 */
	void set(String name, String value);

	/**
	 * This method gets used to replace a value in the data source.
	 * 
	 * @param name   name of the element that will be added into the data source
	 * @param value  value that will be assigned to the name
	 * @param filter filter condition for more precise localization of the element within the data
	 *               structure
	 */
	void set(String name, String value, Filter filter);

	/**
	 * This method gets used to replace a value in the data source.
	 * 
	 * @param name          name of the element that will be added into the data source
	 * @param value         value that will be assigned to the name
	 * @param filter        filter condition for more precise localization of the element within the
	 *                      data structure
	 * @param allOccurences true: replace all hits, false: replace first hit
	 */
	void set(String name, String value, Filter filter, boolean allOccurences);

	/**
	 * This method gets used to replace a value in the data source.
	 * 
	 * @param name       name of the element that will be added into the data source
	 * @param value      value that will be assigned to the name
	 * @param filter     filter condition for more precise localization of the element within the data
	 *                   structure
	 * @param occurences if only part of the hits should be replaced
	 */
//	void set(String name, String value, Filter filter, int[] occurences);

	/**
	 * This method gets used to replace a value in the data source.
	 * 
	 * @param name     name of the element that will be added into the data source
	 * @param attr     if the name has some attribute
	 * @param oldValue value that will be assigned to the name
	 * @param newValue to identify the value to replace
	 * @param filter   filter condition for more precise localization of the element within the data
	 *                 structure
	 */
	void set(String name, String attr, String oldValue, String newValue, Filter filter);

}
