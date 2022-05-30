package org.opentdk.api.datastorage;

/**
 * The <code>CustomContainer</code> interface groups related methods with empty bodies for every
 * specific <code>DataContainer</code>, like {@link PropertiesDataContainer}, {@link XMLDataContainer} 
 * or {@link CSVDataContainer}. The abstract methods defined within <code>CustomContainer</code> need
 * to be implemented in all classes that implement the <code>CustomContainer</code> interface. 
 * The abstract default methods do not need to be implemented within these classes, but they can.
 * 
 * The <code>CustomContainer</code> is also used by the <code>DataContainer</code> to initialize the 
 * right specific data container depending on the given source file type.
 */
interface CustomContainer {
	
    /**
     * Each specific <code>DataContainer</code> that implements the <code>CustomContainer</code>, needs 
     * to implement a method that reads data from the source and store the data into tabular format 
     * within the <code>DataContainer</code>. In case the source data is not in tabular format, the
     * specific method needs to implement also the logic to parse, filter and/or transpose the source data
     * into the format that is supported by the <code>DataContainer</code>.
     * 
     * @param filter    Possibility to filter some of the source data
     */
    void readData(Filter filter);

    /**
     * Each specific <code>DataContainer</code> that implements the <code>CustomContainer</code>, needs 
     * to implement a method that writes the data from the runtime instance of the <code>DataContainer</code>
     * into a file.
     * 
     * @param srcFileName   The name of the source file to write to
     */
    void writeData(String srcFileName);
    
    /*
     * The default methods within this interface can be implemented in the specific classes, but they do not need to.
     */
    
    /**
     * The {@link #addField(String, String, Filter)} method can be implemented within the specific <code>DataContainer</code>
     * to add a field and value into the data source. Primarily the method is designed for data formats, where single nodes
     * or similar elements can be addressed, instead of columns or rows with multiple values. e.g. the implemented method
     * {@link XMLDataContainer#addField(String, String, Filter)} adds a tag into the XML document.
     * 
     * @param headerName	Name of the element that will be added into the data source. 
     * @param value			Value that will be assigned to the element.
     * @param fltr			Filter condition for more precise localization of the element within the data structure.
     */
	default void addField(String headerName, String value, Filter fltr) {
    	
    }

    /**
     * The {@link #addField(String, String, String, Filter)} method can be implemented within the specific <code>DataContainer</code>
     * to add an attribute name and value to a field within the data source. Primarily the method is designed for data formats, 
     * where single nodes or similar elements can be addressed, instead of columns or rows with multiple values. e.g. the 
     * implemented method {@link XMLDataContainer#addField(String, String, String, Filter)} adds an attribute name and value 
     * to a tag within the XML document.
     * 
     * @param headerName		Name of the element where to add an attribute within the data source.
     * @param attributeName	 	Name of the attribute that will be assigned to the element.
     * @param attributeValue	Value that will be assigned to the attribute.
     * @param fltr				Filter condition for more precise localization of the element within the data structure.
     */
    default void addField(String headerName, String attributeName, String attributeValue, Filter fltr) {
    	
    }

    /**
     * The {@link #addField(String, String, String, String, Filter)} method can be implemented within the specific <code>DataContainer</code>
     * to add an attribute name and value to a field within the data source. Primarily the method is designed for data formats, 
     * where single nodes or similar elements can be addressed, instead of columns or rows with multiple values. e.g. the 
     * implemented method {@link XMLDataContainer#addField(String, String, String, String, Filter)} adds an attribute name and value 
     * to a tag within the XML document.
     * 
     * @param headerName		Name of the element where to add an attribute within the data source.
     * @param attributeName	 	Name of the attribute that will be assigned to the element.
     * @param oldAttrValue		Value of existing attribute to search for and overwrite, if exists.
     * @param attributeValue	Value that will be assigned to the attribute.
     * @param fltr				Filter condition for more precise localization of the element within the data structure.
     */
    default void addField(String headerName, String attributeName, String oldAttrValue, String attributeValue, Filter fltr) {
    	
    }

    /**
     * The {@link #deleteField(String, String, String, Filter)} method can be implemented within the specific <code>DataContainer</code>
     * to delete an attribute name and value from a field within the data source. Primarily the method is designed for data formats, 
     * where single nodes or similar elements can be addressed, instead of columns or rows with multiple values. e.g. the 
     * implemented method {@link XMLDataContainer#deleteField(String, String, String, Filter)} deletes an attribute name and value 
     * from a tag within the XML document.
     * 
     * @param headerName		Name of the element where to delete an attribute from within the data source.
     * @param attributeName	 	Name of the attribute that will be deleted from the element.
     * @param attributeValue	Attribute value that will be deleted with the attribute.
     * @param fltr				Filter condition for more precise localization of the element within the data structure.
     */
    default void deleteField(String headerName, String attributeName, String attributeValue, Filter fltr) {
    	return;
    }
    
    /**
     * The {@link #getAttributes(String, String)} method can be implemented within the specific <code>DataContainer</code> 
     * in case that the fields of the data source have additional attributes attached. 
     * e.g.: The {@link XMLDataContainer#getAttributes(String, String)} searches for all tags with specific attributes
     * like <code>&lt;CountryCode name="Germany"&gt;+49&lt;/CountryCode&gt;</code>
     *  
     * @param expr		Element to search for {@literal ->} the individual purpose of this parameter will be specified within the implementing classes
     * @param attrName	Name of the attribute to search for {@literal ->} the individual purpose of this parameter will be specified within the implementing classes
     * @return			String Array with all attribute values found by the search expression and attribute name
     */
    default String[] getAttributes(String expr, String attrName) {
    	return new String[0];
    }

    /**
     * The {@link #getColumn(String, Filter)} method can be implemented within the specific <code>DataContainer</code>
     * to retrieve all or a filtered amount of field values from a column, if the data is in tabular format.
     * 
     * @param headerName	Name of the column header, where the method will get the values from.
     * @param rowFilter		The row filter defines the conditions for values found in the column. Only values that match the conditions will be retrieved. 
     * @return				Array with all resulting values.
     */
	default Object[] getColumn(String headerName, Filter rowFilter) throws Exception {
		return new String[0];
	}
 
    /**
     * The {@link #setFieldValues(String, int[], String, Filter)} method can be implemented within the specific <code>DataContainer</code>
     * to assign a value to one or multiple elements that match a filter condition. This can be convenient for tree and tabular formats.
     * 
     * @param headerName	Name of the element where to add an attribute within the data source.
     * @param occurences	Integer array with the index numbers of occurences, where to set the value.
     * @param value			The value that will be assigned to the elements.
     * @param fltr			Filter condition of the elements to search for.
     */
	default void setFieldValues(String headerName, int[] occurences, String value, Filter fltr) {

	}
	
}