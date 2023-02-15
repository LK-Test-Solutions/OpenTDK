package org.opentdk.api.datastorage;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.opentdk.api.filter.Filter;

public interface TabularContainer extends SpecificContainer {
	
	/**
	 * This method is designed for tabular data formats to add column names to existing
	 * <code>DataContainer</code>. In case the column name already exists, an suffix with the next
	 * available index will be appended to the column name.
	 * 
	 * @param col Name of the column that will be added to the {@link BaseContainer#headerNames}
	 */
	void addColumn(String col);

	/**
	 * This method is designed for tabular data formats to add column names to existing
	 * <code>DataContainer</code>. The useExisting argument specifies the behavior for existing columns
	 * with the same name. Either use the existing column or create a new column by appending a unique
	 * index to the name.
	 * 
	 * @param col         Name of the column that will be added to the {@link BaseContainer#headerNames}
	 * @param useExisting Boolean value - true = if column name exist, then use the existing column;
	 *                    false = if column name exists, then add column name with an index suffix
	 */
	void addColumn(String col, boolean useExisting);
	
	/**
	 * Adds an empty row to the values ArrayList that is used for tabular data formats.
	 */
	void addRow();

	/**
	 * Inserts a string array with the row content into the {@link BaseContainer#values} ArrayList at a
	 * specified position in this list. Shifts the row currently at that position (if any) and any
	 * subsequent row.
	 * 
	 * @param rowIndex  index at which the specified row is to be inserted
	 * @param rowValues String array with all row values to be inserted
	 */
	void addRow(int rowIndex, String[] rowValues);

	/**
	 * Adds a string array with the row content into the {@link BaseContainer#values} ArrayList.
	 *
	 * @param row String array with the content of the row to be added
	 */
	void addRow(String[] row);

	/**
	 * Adds one or more string arrays with the row content into the {@link BaseContainer#values}
	 * ArrayList.
	 * 
	 * @param rows List of string arrays with the content of the rows to be added
	 */
	void addRows(List<String[]> rows);

	/**
	 * This method reads data from a specified source file and appends it to the existing data within
	 * the container.
	 * 
	 * @param fileName Full path and name of the source file, from which the data will be added to the
	 *                 <code>DataContainer</code>
	 * @throws IOException
	 */
	void appendData(String fileName) throws IOException;

	/**
	 * This method reads data from a specified source file and appends it to the existing data within
	 * the container.
	 * 
	 * @param fileName        Full path and name of the source file, from which the data will be added
	 *                        to the <code>DataContainer</code>
	 * @param columnDelimiter The column separator to structure the data.
	 * @throws IOException
	 */
	void appendData(String fileName, String columnDelimiter) throws IOException;
	
	/**
	 * This method checks if the headers-of the assigned <code>DataContainer</code> match with the
	 * headers of the current instance and appends all data of the assigned <code>DataContainer</code>
	 * to the values List of the DataContaner instance which is calling the method, in case the headers
	 * of both <code>DataContainer</code> are the same.
	 *
	 * @param dc The DataContanier which content will be appended to the current DataContainer instance.
	 */
	void appendDataContainer(DataContainer dc);

	/**
	 * Compares the assigned <code>HashMap</code> with header names and indexes with the headers of the
	 * current <code>DataContainer</code> instance.
	 *
	 * @param compareHeaders <code>HashMap</code> with header names and indexes which will be compared
	 *                       against the headers of the current object instance.
	 * @return
	 * 
	 *         <pre>
	 * {@literal -}1 = mismatching headers
	 * 0 = header names and indexes are identical
	 * 1 = header names match, but with different index order
	 *         </pre>
	 */
	int checkHeader(HashMap<String, Integer> compareHeaders);

	/**
	 * Compares the header names and order of a <code>HashMap</code> with header names and order of an
	 * Arrays of type <code>String</code>.
	 *
	 * @param referenceHeaders Array of type <code>String</code> with header names and indexes which
	 *                         include reference values for comparison.
	 * @param compareHeaders   Array of type <code>String</code> with header names and indexes which
	 *                         include comparison values.
	 * @return
	 * 
	 *         <pre>
	 * -1 = mismatching headers
	 * 0 = header names and indexes are identical
	 * 1 = header names match, but with different index order
	 *         </pre>
	 */
	int checkHeader(HashMap<String, Integer> referenceHeaders, String[] compareHeaders);

	/**
	 * Compares an array of type <code>String</code> with defined header names with the header names and
	 * indexes of the current <code>DataContainer</code>.
	 *
	 * @param compareHeaders Array of type <code>String</code> with header names and indexes which will
	 *                       be compared against the headers of the current object instance.
	 * @return
	 * 
	 *         <pre>
	 * {@literal -}1 = mismatching headers
	 * 0 = header names and indexes are identical
	 * 1 = header names match, but with different index order
	 *         </pre>
	 */
	int checkHeader(String[] compareHeaders);

	/**
	 * Compares the names and order of two Arrays of type <code>String</code>.
	 *
	 * @param referenceHeaders Array of type <code>String</code> with header names and indexes which
	 *                         include reference values for comparison.
	 * @param compareHeaders   Array of type <code>String</code> with header names and indexes which
	 *                         include comparison values.
	 * @return
	 * 
	 *         <pre>
	 * -1 = mismatching headers
	 * 0 = header names and indexes are identical
	 * 1 = header names match, but with different index order
	 *         </pre>
	 */
	int checkHeader(String[] referenceHeaders, String[] compareHeaders);

	/**
	 * Removes the row of the container instance at the specified index.
	 * 
	 * @param index integer that has to be in the range of the values size.
	 */
	void deleteRow(int index);
	/**
	 * Deletes all rows of the container instance that match the defined filter criteria. If no rows
	 * could be detected the method returns. If there is a file defined for the container the changes
	 * get written to it afterwards.
	 * 
	 * @param fltr {@link org.opentdk.api.filter.Filter} object with the set filter rules.
	 */
	void deleteRows(Filter fltr);
	
	/**
	 * @param name column name
	 */
	void deleteValue(String name);

	/**
	 * This method writes data to a existing or newly created CSV file. Useful if the current state of
	 * the container should be saved. The semicolon gets used as column separator.
	 * 
	 * @param fileName Full path and name of the source file, from which the data will be loaded into
	 *                 DataContainer
	 * @throws IOException If any error occurred when writing to the file.
	 */
	void exportContainer(String fileName) throws IOException;
	
	/**
	 * This method writes data to a existing or newly created CSV file. Useful if the current state of
	 * the container should be saved. The column separator can be set.
	 *
	 * @param fileName        The file path of the file to write to or to create.
	 * @param columnDelimiter The column separator to structure the data.
	 * @throws IOException If any error occurred when writing to the file.
	 * 
	 */
	void exportContainer(String fileName, String columnDelimiter) throws IOException;

	/**
	 * Gets the content of a column from the <code>DataContainer</code> instance. In order to get the
	 * columns content, it is iterated through each row saved. The column is specified with index and
	 * the content is returned as <code>Array</code> of type String
	 *
	 * @param index The column's index, which content is searched.
	 * @return The content of the specified column as <code>Array</code> of type String.
	 */
	
	String[] getColumn(int index);
	/**
	 * Gets filtered content of a column from the <code>DataContainer</code> instance. Only values of a
	 * column that match to one or more conditions. In order to get the columns content, it is iterated
	 * through each row saved. The column is specified with index and the content is returned as
	 * <code>Array</code> of type <code>String</code>.
	 *
	 * @param index The column's index, which content is searched.
	 * @param fltr  A {@link org.opentdk.api.filter.Filter} object to define which data should be
	 *              ignored. If it is null no filter gets used.
	 * @return the content of the specified column as <code>Array</code> of type <code>String</code>.
	 */
	
	String[] getColumn(int index, Filter fltr);
	/**
	 * Gets the content of a column from the <code>DataContainer</code> instance. In order to get the
	 * columns content, it is iterated through each row saved. The column is specified with header name
	 * and the content is returned as <code>Array</code> of type String
	 *
	 * @param colName The header name of the column.
	 * @return The content of the specified column as <code>Array</code> of type String.
	 */
	String[] getColumn(String colName);

	/**
	 * Gets filtered content of a column from the <code>DataContainer</code> instance. Only values of a
	 * column, that match to one or more conditions. In order to get the columns content, it is iterated
	 * through each row saved. The column is specified with column header name and the content is
	 * returned as <code>Array</code> of type <code>String</code>.
	 * 
	 * @param colName The header name of the column.
	 * @param fltr    A {@link org.opentdk.api.filter.Filter} object to define which data should be
	 *                ignored. If it is null no filter gets used.
	 * @return the content of the specified column as <code>Array</code> of type <code>String</code>.
	 */
	String[] getColumn(String colName, Filter fltr);

	/**
	 * Gets filtered content of a column from the <code>DataContainer</code> instance. Only values of a
	 * column, that match to one or more conditions. In order to get the columns content, it is iterated
	 * through each row saved. The column is specified with column header name and the content is
	 * returned as <code>Array</code> of type <code>String</code>, but only if the detected rows in the
	 * column match the committed row indexes.
	 * 
	 * @param colName    The header name of the column.
	 * @param rowIndexes An array of type integer that contains all row indexes that should be taken
	 *                   into account when searching in the column.
	 * @param fltr       A {@link org.opentdk.api.filter.Filter} object to define which data should be
	 *                   ignored. If it is null no filter gets used.
	 * @return the content of the specified column as <code>Array</code> of type <code>String</code>.
	 */
	String[] getColumn(String colName, int[] rowIndexes, Filter fltr);

	/**
	 * Returns the number of columns defined within the current instance of the
	 * <code>DataContainer</code>.
	 *
	 * @return number of columns
	 */
	int getColumnCount();

	/**
	 * Get the delimiter character(s) that separates the columns in the source file. The delimiter will
	 * be used for splitting the data rows and transfer them into a String Array.
	 *
	 * @return The character(s) used for column separation in the source file.
	 */
	String getColumnDelimiter();

	/**
	 * Gets a <code>List</code> of string arrays with all column values of the
	 * <code>DataContainer</code> instance.
	 *
	 * @return Object of type <code>List</code> with string arrays, including all matching values of a
	 *         column.
	 */
	List<String[]> getColumnsList();
	
	/**
	 * Gets a <code>List</code> of string arrays with all column values that match to the defined
	 * filter. The Filter defines the rules for matching rows. <br>
	 * <br>
	 * e.g. get string arrays for each column of table 'Customer' where the value of 'City' equals to
	 * 'Munich'.
	 *
	 * @param rowFilter Object of type {@link org.opentdk.api.filter.Filter} which defines rules for
	 *                  matching rows
	 * @return Object of type <code>List</code> with string arrays, including all matching values of a
	 *         column.
	 */
	List<String[]> getColumnsList(Filter rowFilter);

	/**
	 * Gets a <code>List</code> of string arrays with all column values that match to the defined
	 * filter. The Filter defines the rules for matching rows. <br>
	 * <br>
	 * e.g. get string arrays for each column of table 'Customer' where the value of 'City' equals to
	 * 'Munich'.
	 *
	 * @param columnHeaders Semicolon separated string with the column names of which the values will be
	 *                      returned.
	 * @param rowFilter     Object of type {@link org.opentdk.api.filter.Filter} which defines rules for
	 *                      matching rows.
	 * @return Object of type <code>List</code> with string arrays, including all matching values of a
	 *         column.
	 */
	List<String[]> getColumnsList(String columnHeaders, Filter rowFilter);

	/**
	 * Gets a <code>List</code> of string arrays with all column values that match to the defined
	 * filter. The Filter defines the rules for matching rows. Additionally it is possible to define the
	 * row indices that should be taken into account. <br>
	 * <br>
	 * e.g. get string arrays for each column of table 'Customer' where the value of 'City' equals to
	 * 'Munich'.
	 *
	 * @param columnHeaders Semicolon separated string with the column names of which the values will be
	 *                      returned.
	 * @param rowFilter     Object of type {@link org.opentdk.api.filter.Filter} which defines rules for
	 *                      matching rows.
	 * @param rowIndexes    The row numbers that should be used as search criteria.
	 * @return Object of type <code>List</code> with string arrays, including all matching values of a
	 *         column.
	 */
	List<String[]> getColumnsList(String[] columnHeaders, int[] rowIndexes, Filter rowFilter);

	/**
	 * Gets the index value of a defined headerName.
	 *
	 * @param headerName Name of the header for which to retrieve the index.
	 * @return Index value of type integer.
	 */
	int getHeaderIndex(String headerName);

	/**
	 * @param headerIndex The key of the header in the headers map.
	 * @return The header name at the committed position as string value.
	 */
	String getHeaderName(int headerIndex);

	/**
	 * Returns the header names from the {@link headerNames} HashMap as string array, ordered by their
	 * index.
	 * 
	 * @return String array with header names
	 */
	String[] getHeaderNamesIndexed();

	/**
	 * Gets the number of headers that match a given regular expression.
	 *
	 * @param toMatch regular expression to search for in header names
	 * @return number of header-names that match the regex
	 */
	int getHeaderOccurances(String toMatch);

	/**
	 * Gets the index of the header row within the source file.
	 * 
	 * @return row index
	 */
	int getHeaderRowIndex();

	/**
	 * Gets the <code>HashMap</code> {@link #headerNames} that includes the names and indexes of column
	 * headers within the instance of the DataContainer.
	 *
	 * @return {@link BaseContainer#headerNames} <code>HashMap</code>
	 */
	HashMap<String, Integer> getHeaders();

	/**
	 * Gets the HashMap columnHeaders with reversed key/value assignment. Whereas the original
	 * columnHeaders # HashMap uses the header names as key and their index as value, this method
	 * returns a HashMap with the index as key and the header name as value.
	 *
	 * @return HashMap with indexes and names of the columnHeaders HashMap
	 */
	HashMap<Integer, String> getHeadersIndexed();

	/**
	 * This method returns an array of type integer with the indexes of defined header names. This can
	 * either be a single header, a semicolon separated string with multiple headers, or an empty string
	 * that returns the indexes of all headers.
	 *
	 * @param headerName
	 * 
	 *                   <pre>
	 *                   - single headername (e.g.
	 *                   getColumnHeadersIndexes("Firstname");) - semicolon
	 *                   separated value with multiple headernames (e.g.
	 *                   getColumnHeadersIndexes("Firstname;Surname;Email;Phone");)
	 *                   - empty string for all available headernames
	 *                   (getColumnHeadersIndexes("");)
	 *
	 *                   </pre>
	 *
	 * @return Array of type integer with header indexes in the order as defined by the headerName
	 *         parameter
	 */
	int[] getHeadersIndexes(String headerName);

	/**
	 * Gets the maximum length of the values corresponding to the headerName.
	 *
	 * @param headerName The name of the header, which can be column or row, depending on the
	 *                   orientation
	 * @return Length of the longest String corresponding to headerName
	 */
	int getMaxLen(String headerName);

	/**
	 * Gets the HashMap with MetaData
	 *
	 * @return the metaData HashMap of this object instance
	 */
	HashMap<String, String> getMetaData();

	/**
	 * Gets the content of a row of the container instance. The row is specified by index. If the given
	 * index is bigger than the list's size, it will return <code>null</code>.
	 *
	 * @param rowIndex The row's index, which content is searched.
	 * @return The content of the specified row.
	 */
	String[] getRow(int rowIndex);

	/**
	 * Gets the content of a row of the container instance. The row is specified by index. If the given
	 * index is bigger than the list's size, it will return <code>null</code>.
	 *
	 * @param rowIndex      The row's index, which content is searched.
	 * @param columnHeaders The column headers that should be taken into account, separated by
	 *                      semicolon. If only one header should be specified, no separation is needed
	 *                      of course.
	 * @return The content of the specified row.
	 */
	String[] getRow(int rowIndex, String columnHeaders);

	/**
	 * Gets the content of a row of the container instance. The row is specified by index. If the given
	 * index is bigger than the list's size, it will return <code>null</code>.
	 *
	 * @param rowIndex      The row's index, which content is searched.
	 * @param columnHeaders The column headers that should be taken into account, separated by
	 *                      semicolon. If only one header should be specified, no separation is needed
	 *                      of course.
	 * @param fltr          Object of type {@link org.opentdk.api.filter.Filter} which defines rules for
	 *                      matching rows.
	 * @return The content of the specified row.
	 */
	String[] getRow(int rowIndex, String columnHeaders, Filter fltr);

	/**
	 * Gets the content of a row of the container instance. The row is specified by index. If the given
	 * index is bigger than the list's size, it will return <code>null</code>.
	 *
	 * @param rowIndex      The row's index, which content is searched.
	 * @param columnHeaders The column headers that should be taken into account, separated by
	 *                      semicolon. If only one header should be specified, no separation is needed
	 *                      of course.
	 * @return The content of the specified row.
	 */
	String[] getRow(int rowIndex, String[] columnHeaders);

	/**
	 * @param rowIndex
	 * @return map with the column index as key and the row value as value
	 */
	Map<String, String> getRowAsMap(int rowIndex);

	/**
	 * Returns the number of rows of the current <code>DataContainer</code> instance.
	 *
	 * @return Number of rows e.g number of lines in case of a CSV format or number of key value pairs
	 *         in case of a properties file.
	 */
	int getRowCount();

	/**
	 * Possibility to search for integer values of the <code>DataContainer</code> rows.
	 * 
	 * @param filter Object of type {@link org.opentdk.api.filter.Filter} which defines rules for
	 *               matching rows.
	 * @return The numbers of the matching rows as integer array.
	 */
	int[] getRowsIndexes(Filter filter);

	/**
	 * Returns a list with all rows stored in the <code>DataContainer</code> instance.
	 *
	 * @return list object with stored data rows.
	 */
	List<String[]> getRowsList();

	/**
	 * Returns a list with all rows, that match to the defined rowFilter.
	 *
	 * @param rowFilter Object of type {@link org.opentdk.api.filter.Filter} which defines rules for
	 *                  matching rows.
	 * @return A list with all rows. Each row is stored in one string array.
	 */
	List<String[]> getRowsList(Filter rowFilter);

	/**
	 * Returns a list of row values for defined columns that match to a defined rowFilter.
	 *
	 * @param rowIndexes    The numbers of the rows that should be taken into account.
	 * @param columnHeaders The header name of the column to search in.
	 * @param rowFilter     Object of type {@link org.opentdk.api.filter.Filter} which defines rules for
	 *                      matching rows.
	 * 
	 * @return A list with all rows. Each row is stored in one string array.
	 */
	List<String[]> getRowsList(int[] rowIndexes, String[] columnHeaders, Filter rowFilter);
	
	/**
	 * Returns a list of row values for defined columns that match to a defined rowFilter.
	 *
	 * @param columnHeader The header name of the column to search in.
	 * @param rowFilter    Object of type {@link org.opentdk.api.filter.Filter} which defines rules for
	 *                     matching rows.
	 * @return A list with all rows. Each row is stored in one string array.
	 */
	List<String[]> getRowsList(String columnHeader, Filter rowFilter);

	/**
	 * Use the <code>getValue</code> method to return a value from a field addressed by headerName of
	 * the column and index of the row.
	 *
	 * @param headerIndex The name of the column header
	 * @param rowIndex    row-index of the field
	 * @return the value as String
	 */
	String getValue(int headerIndex, int rowIndex);

	/**
	 * Use the <code>getValue</code> method to return the first value in column addressed by headerName.
	 *
	 * @param headerName The name of the column header
	 * @return the first value in column addressed by headerName.
	 */
	String getValue(String headerName);

	/**
	 * Use the <code>getValue</code> method to return a value from a field addressed by headerName of
	 * the column and prepared filter.
	 *
	 * @param headerName The name of the column header
	 * @param fltr       A prepared Filter instance to categorize the result
	 * @return the value as String
	 */
	String getValue(String headerName, Filter fltr);

	/**
	 * Use the <code>getValue</code> method to return a value from a field addressed by headerName of
	 * the column and index of the row.
	 *
	 * @param headerName The name of the column header
	 * @param rowIndex   row index of the field
	 * @return the value as String
	 */
	String getValue(String headerName, int rowIndex);

	/**
	 * Use the getValuesAsList() method to search for a value by header name, row index and prepared
	 * filter.
	 * 
	 * @param headerName The name of the column header
	 * @param rowIndex   The row index. If it is unknown or not used, commit -1 to search for header
	 *                   name and filter
	 * @param fltr       A prepared Filter instance to categorize the result.
	 * @return the value as String or an empty string if no result could be found
	 */
	String getValue(String headerName, int rowIndex, Filter fltr);
	
	/**
	 * Gets all values from the DataContainer instance directly.
	 *
	 * @return List of type String with all values
	 */
	List<String[]> getValues();

	/**
	 * Gets a sequence of distincted values from the DataContainer instance. Depending on the header
	 * orientation, this method will retrieve the values of one row, or one column, which is defined by
	 * headerName.
	 *
	 * @param headerName The name of the header, which can be column or row, depending on the
	 *                   orientation
	 * @return distincted list of type String with the selected values
	 */
	List<String> getValuesAsDistinctedList(String headerName);

	/**
	 * Gets a sequence of numeric values from the DataContainer instance and returns them as List of
	 * Double. Depending on the header orientation, this method will retrieve the values of one row, or
	 * one column, which is defined by headerName.
	 *
	 * @param headerName The name of the header, which can be column or row, depending on the
	 *                   orientation
	 * @return List of type Double with all selected values
	 */
	List<Double> getValuesAsDoubleList(String headerName);

	/**
	 * Gets a sequence of numeric values from the current DataContainer instance, that matches to a
	 * defined filter and returns them as a List of Double. Depending on the header orientation, this
	 * method will retrieve the values of one row, or one column, which is defined by headerName.
	 *
	 * @param headerName The name of the header
	 * @param rowFilter  Object of type {@link org.opentdk.api.filter.Filter} which defines rules for
	 *                   matching rows
	 * @return List of type Double with all selected values
	 */
	List<Double> getValuesAsDoubleList(String headerName, Filter rowFilter);

	/**
	 * Gets a sequence of numeric values from the DataContainer instance and returns them as List of
	 * integer. Depending on the header orientation, this method will retrieve the values of one row, or
	 * one column, which is defined by headerName.
	 *
	 * @param headerName The name of the header, which can be column or row, depending on the
	 *                   orientation
	 * @return List of type Integer with all selected values
	 */
	List<Integer> getValuesAsIntList(String headerName);

	/**
	 * Gets a sequence of numeric values from the current DataContainer instance, that matches to a
	 * defined filter and returns them as a List of Integer. Depending on the header orientation, this
	 * method will retrieve the values of one row, or one column, which is defined by headerName.
	 *
	 * @param headerName The name of the header
	 * @param rowFilter  Object of type {@link org.opentdk.api.filter.Filter} which defines rules for
	 *                   matching rows
	 * @return List of type integer with all selected values
	 */
	List<Integer> getValuesAsIntList(String headerName, Filter rowFilter);

	/**
	 * Gets a sequence of values from the DataContainer instance. Depending on the header orientation,
	 * this method will retrieve the values of one row, or one column, which is defined by headerName.
	 *
	 * @param headerName The name of the header, which can be column or row, depending on the
	 *                   orientation
	 * @return List of type String with all selected values
	 */
	List<String> getValuesAsList(String headerName);

	/**
	 * Gets a sequence of values from the current DataContainer instance, that matches to a defined
	 * filter. Depending on the header orientation, this method will retrieve the values of one row, or
	 * one column, which is defined by headerName.
	 *
	 * @param headerName The name of the header
	 * @param rowFilter  Object of type {@link org.opentdk.api.filter.Filter} which defines rules for
	 *                   matching rows
	 * @return List of type String with all selected values
	 */
	List<String> getValuesAsList(String headerName, Filter rowFilter);

	/**
	 * Gets a sequence of values from the current DataContainer instance, that matches to a defined
	 * filter and defined row indexes. Depending on the header orientation, this method will retrieve
	 * the values of one row, or one column, which is defined by headerName.
	 *
	 * @param headerName The name of the header
	 * @param rowIndexes int array with index numbers of the rows that will be retrieved
	 * @param fltr       Object of type {@link org.opentdk.api.filter.Filter} which defines rules for
	 *                   matching rows
	 * @return List of type String with all selected values
	 */
	List<String> getValuesAsList(String headerName, int[] rowIndexes, Filter fltr);

	/**
	 * Gets a representation of all values within the DataContainer. Columns will be separated by the
	 * associated or default column delimiter, rows by a new line.
	 *
	 * @return String with the complete content of the DataContainer
	 */
	String getValuesAsString();

	/**
	 * Compares the row at committed index with the committed string array. If one of the still existing
	 * row values is null or empty, it gets replaced by the value from the new array.
	 * 
	 * @param rowIndex  row number to identify the row that should be merged
	 * @param newValues the new data as string array
	 */
	void mergeRows(int rowIndex, String[] newValues);

	/**
	 * Inserts additional headers and values into the DataContanier that will added to the DataSets,
	 * transmitted from the sources. e.g. If you create an instance of {@link CSVDataContainer} with a
	 * csv.file as source, you can put the file content into the {@link CSVDataContainer} and add the
	 * filename to each DataSet.<br>
	 * The following sample reads contact data from CSV file and adds the column Company with the
	 * company name for each data set while putting the data into the {@link CSVDataContainer}.
	 *
	 * <pre>
	 * DataContainer dc = new DataContainer(";", EHeader.COLUMN); // creates a {@link CSVDataContainer}
	 * dc.putMetaData("Company", "happy inc.");
	 * dc.readData("c:\\data\\happy-inc.csv");
	 * </pre>
	 * <p>
	 * The readData method of the appropriate DataContainer instance will automatically extend all
	 * DataSets with all headers and values, added to the metaData HashMap.<br>
	 * <br>
	 *
	 * @param headerName Header name of DataSet within the DataContainer
	 * @param value      Value as String that will be added to each DataSet into the column of
	 *                   headerName
	 */
	void putMetaData(String headerName, String value);
	
	/**
	 * Sets a column in the <code>DataContainer</code> instance with the specified new content. If the
	 * new column is bigger than the old one, new rows with empty values will be added.
	 *
	 * @param headerName   The name of the column. Existing column will be overwritten and non-existing
	 *                     column will be created.
	 * @param columnValues The values for the column as List of strings.
	 */
	void setColumn(String headerName, List<String> columnValues);

	/**
	 * Sets a column in the <code>DataContainer</code> instance with the specified new content. If the
	 * new column is bigger than the old one, new rows with empty values will be added.
	 *
	 * @param headerName   The name of the column. Existing column will be overwritten and non-existing
	 *                     column will be created.
	 * @param columnValues The values for the column as string array.
	 */
	void setColumn(String headerName, String[] columnValues);

	/**
	 * Sets the <code>columnDelimiter</code> property which is used for splitting the data rows of the
	 * source file into a String Array.
	 *
	 * @param cDel The character(s) used for column separation in the source file.
	 */
	void setColumnDelimiter(String cDel);

	/**
	 * Sets the index of the header row within the source file.
	 * 
	 * @param headerIndex Zero based index where the names of header are placed.
	 */
	void setHeaderRowIndex(int headerIndex);

	/**
	 * Adds header names into the <code>HashMap</code> {@link #headerNames}. It's proofed if the name
	 * already exists in the <code>HashMap</code>. In case of duplicated names, an index suffix will be
	 * added to the name (i.e GF#,GF#_2 ...). Furthermore, the name of the header is added with an index
	 * (<code>i</code>) as "helper" for the assignment of headers to the values.
	 *
	 * @param in_headers List with the name of the headers to be added
	 */
	void setHeaders(List<String> in_headers);

	/**
	 * Adds header names into the <code>HashMap</code> {@link #headerNames}. It's proofed if the name
	 * already exists in the <code>HashMap</code>. In case of duplicated names, an index suffix will be
	 * added to the name (i.e GF#,GF#_2 ...). Furthermore, the name of the header is added with an index
	 * (<code>i</code>) as "helper" for the assignment of headers to the values.
	 *
	 * @param in_headers Array with the name of the headers to be added
	 */
	void setHeaders(String[] in_headers);

	/**
	 * This method is similar to the {@link #putMetaData} method with the difference, that existing
	 * metadata with the same headerName will be overwritten. This method should be used preferably to
	 * avoid duplicated column headers.
	 * 
	 * In case a new header/value set is added to the DataContainer an empty string will be added as an
	 * additional element to the existing data arrays.
	 * 
	 * @param headerName Header name of DataSet within the DataContainer
	 * @param value      Value as String that will be added to each DataSet into the column of
	 *                   headerName
	 */
	void setMetaData(String headerName, String value);	

	/**
	 * Replaces the row at the committed index with the data of the committed string array.
	 * 
	 * @param rowIndex  row number to identify the row that should be replaced
	 * @param rowValues the new data as string array
	 */
	void setRow(int rowIndex, String[] rowValues);

	/**
	 * Sets content of a defined field by header name and index with a specified value. The existing
	 * content of that field will be overwritten. If a header does not exist, it will be created by
	 * shifting all existing values one position backward and setting the new one at the first position.
	 *
	 * @param headerName Name of the sequence header
	 * @param index      Index of the field within the DataSet
	 * @param value      Value as String that will be set to the field
	 */
	void setValue(String headerName, int index, String value);

	/**
	 * Sets content of a defined field by header name, index and filter with a specified value. The
	 * existing content of the field within the dataset will be overwritten. If a header does not exist,
	 * it will be created by shifting all existing values one position backward and setting the new one
	 * at the first position.
	 *
	 * @param headerName Name of the sequence header
	 * @param index      zero based index that defines the position of the field in case that multiple
	 *                   fields were found
	 * @param value      Value as String that will be set to the field
	 * @param fltr       Filter for selection of the target dataset (row or column)
	 */
	void setValue(String headerName, int index, String value, Filter fltr);

	/**
	 * Sets content to the first field of a sequence defined by header name with a specified value. The
	 * existing content of that field will be overwritten.
	 *
	 * @param headerName Name of the sequence header
	 * @param value      Value as String that will be set to the field
	 */
	void setValue(String headerName, String value);

	/**
	 * Sets content of a defined field by header name and filter with a specified value. The existing
	 * content of the field within the first matching dataset will be overwritten. If a header does not
	 * exist, it will be created by shifting all existing values one position backward and setting the
	 * new one at the first position.
	 *
	 * @param headerName Name of the sequence header
	 * @param value      Value as String that will be set to the field
	 * @param fltr       Filter for selection of the target dataset (row or column)
	 */
	void setValue(String headerName, String value, Filter fltr);

	/**
	 * Sets content of multiple fields by header name, index and filter with a specified value. The
	 * existing content of the fields within the dataset will be overwritten. If a header does not
	 * exist, it will be created by shifting all existing values one position backward and setting the
	 * new one at the first position.
	 *
	 * @param headerName Name of the sequence header
	 * @param indexes    Array with indexes, defining the position of all fields which will be
	 *                   overwritten
	 * @param value      Value as String that will be set to the field
	 * @param fltr       Filter for selection of the target dataset (row or column)
	 */
	void setValues(String headerName, int[] indexes, String value, Filter fltr);
	
	/**
	 * Sets content of the first matching field defined by header name and filter with a specified
	 * value. The existing content of the field within the dataset will be overwritten. If a header does
	 * not exist, it will be created by shifting all existing values one position backward and setting
	 * the new one at the first position.
	 *
	 * @param headerName Name of the sequence header
	 * @param value      Value as String that will be set to the field
	 * @param fltr       Filter for selection of the target dataset (row or column)
	 */
	void setValues(String headerName, String value, Filter fltr);
	
	void setValues(String headerName, String value, Filter fltr, Boolean allOccurences);
}
