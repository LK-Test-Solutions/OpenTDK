package org.opentdk.api.datastorage;


import org.opentdk.api.filter.Filter;

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
	 * This method gets used to retrieve values from the data source.
	 * 
	 * @param name Name of the element that will be added into the data source
	 */
	String[] get(String name);

	/**
	 * This method gets used to retrieve values from the data source.
	 * 
	 * @param name   Name of the element that will be added into the data source
	 * @param filter Filter condition for more precise localization of the element within the data
	 *               structure
	 */
	String[] get(String name, Filter filter);

	/**
	 * This method gets used to retrieve values from the data source.
	 * 
	 * @param name Name of the element that will be added into the data source
	 * @param attr If the name has some attribute
	 */
	String[] get(String name, String attr);

	/**
	 * This method gets used to replace a value in the data source.
	 * 
	 * @param name  Name of the element that will be added into the data source
	 * @param value Value that will be assigned to the name
	 */
	void set(String name, String value);

	/**
	 * This method gets used to replace a value in the data source.
	 * 
	 * @param name   Name of the element that will be added into the data source
	 * @param value  Value that will be assigned to the name
	 * @param filter Filter condition for more precise localization of the element within the data
	 *               structure
	 */
	void set(String name, String value, Filter filter);

	/**
	 * This method gets used to replace a value in the data source.
	 * 
	 * @param name          Name of the element that will be added into the data source
	 * @param value         Value that will be assigned to the name
	 * @param filter        Filter condition for more precise localization of the element within the
	 *                      data structure
	 * @param allOccurences If not only the first hit should be replaced
	 */
	void set(String name, String value, Filter filter, boolean allOccurences);

	/**
	 * This method gets used to replace a value in the data source.
	 * 
	 * @param name       Name of the element that will be added into the data source
	 * @param value      Value that will be assigned to the name
	 * @param filter     Filter condition for more precise localization of the element within the data
	 *                   structure
	 * @param occurences if there are more hits this parameter allows to decide which ones get replaced
	 */
	void set(String name, String value, Filter filter, int[] occurences);

	/**
	 * This method gets used to replace a value in the data source.
	 * 
	 * @param name     Name of the element that will be added into the data source
	 * @param attr     If the name has some attribute
	 * @param value    Value that will be assigned to the name
	 * @param oldValue To identify the value to replace
	 * @param filter   Filter condition for more precise localization of the element within the data
	 *                 structure
	 */
	void set(String name, String attr, String value, String oldValue, Filter filter);

}
