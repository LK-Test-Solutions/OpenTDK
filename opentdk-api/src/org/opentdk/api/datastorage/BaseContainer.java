package org.opentdk.api.datastorage;

import java.io.InputStream;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.logging.Level;

import org.opentdk.api.logger.MLogger;
import org.opentdk.api.util.*;

/**
 * Super Class for {@link DataContainer} including common properties and their
 * getter and setter methods. In addition the BaseContainer class includes all
 * protected methods that will be used by 'unbound' methods of classes within
 * the <i>datastorage</i> package.
 *
 * @author LK Test Solutions GmbH
 */
public abstract class BaseContainer {

	/**
	 * Enumeration that defines all source types, supported by the
	 * {@link org.opentdk.api.datastorage} package with its header format.
	 *
	 */
	public enum EContainerFormat {
		CSV(EHeader.COLUMN), DEFAULT(EHeader.COLUMN), PROPERTIES(EHeader.ROW), RESULTSET(EHeader.COLUMN), XML(EHeader.TREE), JSON(EHeader.TREE), YAML(EHeader.TREE);

		private EHeader headerType;

		/**
		 * Default constructor of the enums
		 */
		EContainerFormat() {

		}

		/**
		 * Constructor of the enums that assigns the header type.
		 * 
		 * @param hType Value of type EHeader, defining the header format of the source
		 *              type
		 */
		EContainerFormat(EHeader hType) {
			headerType = hType;
		}

		/**
		 * Returns the header type of the enum
		 * 
		 * @return Value of type EHeader
		 */
		public EHeader getHeaderType() {
			return headerType;
		}
	}

	/**
	 * Enumeration that defines all header formats supported by the
	 * {@link org.opentdk.api.datastorage} package. In case of tabular formats, the
	 * header names can be defined in a row (e.g. CSV-files) or in a column (e.g.
	 * Properties-files) and for hierarchical formats like XML, HTML, JSON or JAML,
	 * the data will be located by the tree hierarchy by the xPath.
	 */
	public enum EHeader {
		/**
		 * The {@link DataContainer} is in tabular format and includes column headers
		 * which means that the elements added to the {@link #values()} ArrayList
		 * correspond to a row of the source.
		 */
		COLUMN,
		/**
		 * The data source is in tabular format and includes row headers which means
		 * that the elements added to the {@link #values()} ArrayList correspond to a
		 * column of the source.
		 */
		ROW,
		/**
		 * The data source is in tree format like XML. No header names will be assigned
		 * to the DataContainer.
		 */
		TREE,
		/**
		 * If UNKNOWN is used, the DataContainer assumes that the source includes
		 * semicolon separated columns without column- and row-headers.<br>
		 * TODO extend the DataContainer logic to automatically identify the source
		 * format in case it matches with one of the known header types.
		 */
		UNKNOWN
	}

	/**
	 * The character(s) that define the delimiter of columns within tabular files.
	 * This delimiter is used by the {@link DataContainer#readData()} methods to
	 * split the rows of the source file into a String Array and by the
	 * {@link DataContainer#exportContainer(String)} methods to write the elements
	 * of the {@link #values} ArrayList into the target file.
	 */
	protected String columnDelimiter = ";";

	/**
	 * Property that stores the container format for the instance of the
	 * {@link DataContainer} as an enum of type {@link EContainerFormat}.
	 */
	protected EContainerFormat containerFormat;

	/**
	 * Full path and name of the file for DataContainers with file based sources
	 * like {@link CSVDataContainer}, {@link PropertiesDataContainer} or
	 * {@link XMLDataContainer}. This file will be used as default for all read and
	 * write methods that are called without an explicit file attribute.
	 */
	protected String fileName = "";

	/**
	 * The {@link #headerNames} property is used to assign header names to each
	 * index of the String Arrays, stored as elements of the ArrayList
	 * {@link #values}. This HashMap allows to locate values within the String
	 * Arrays by name instead of index.<br>
	 * e.g. if the first row of a CSV-file includes header names and all other rows
	 * include values, then the names of the first row will be stored in the
	 * {@link #headerNames} HashMap with their original index.
	 */
	protected final HashMap<String, Integer> headerNames = new HashMap<>();

	/**
	 * This property defines the record index of the source, where the header names
	 * are read from. All values of this record will be put into the HashMap
	 * {@link #headerNames}.<br>
	 * - If the headerType of the DataContainer is {@link EHeader#COLUMN}, then
	 * {@link #headerNamesIndex} defines the row index of the source that includes
	 * the header names.<br>
	 * - If the headerType of the DataContainer is {@link EHeader#ROW}, then
	 * {@link #headerNamesIndex} defines the column index of the source that
	 * includes the header names.
	 */
	protected int headerNamesIndex = 0;

	/**
	 * This property is used for the adaption of different source formats, to make
	 * the data accessible in a similar way, as they were organized in tabular
	 * format.<br>
	 * e.g. Source data of tree format will be transposed into a tree table with the
	 * parent path stored within an additional column for each node. This allows
	 * access to the node with the same methods as implemented for tabular formated
	 * data.
	 */
	protected Set<String> implicitHeaders = new HashSet<>();

	/**
	 * This property is used to assign the data as an {@link java.io.InputStream} to
	 * the {@link DataContainer} instance in case that no source file exists. This
	 * is valid for data formats like XML, JSON or JAML.
	 */
	protected InputStream inputStream;

	/**
	 * The HashMap {@link #metaData} is used to define fields and values that will
	 * be appended to each record added to the DataContainer by the
	 * {@link DataContainer#readData()}, {@link DataContainer#addRow()} and the
	 * {@link DataContainer#appendData(String)} methods.<br>
	 * e.g. add the name of the source file to each record, when putting the content
	 * of multiple files into one instance of the DataContainer.
	 * 
	 * <pre>
	 * <b>Code sample:</b>
	 * DataContainer dc = new DataContainer(";", EHeader.COLUMN);
	 * dc.putMetaData("Filename", "Attendees_FirstAid_Course_Q4-2020.csv"));
	 * dc.readData("./data/Attendees_FirstAid_Course_Q4-2020.csv");
	 * dc.putMetaData("Filename", "Attendees_FirstAid_Course_Q1-2021.csv"));
	 * dc.readData("./data/Attendees_FirstAid_Course_Q1-2021.csv");
	 * </pre>
	 */
	protected final HashMap<String, String> metaData = new HashMap<String, String>();

	/**
	 * Keeps the SQL result set of
	 * {@link org.opentdk.api.datastorage.RSDataContainer}
	 */
	protected ResultSet resultSet;
	
	protected String rootNode = "";

	/**
	 * ArrayList with an Array of Strings where content of tabular sources is stored
	 * at runtime of an application. <br>
	 * - If the associated source includes row-based records and column-based fields
	 * like SQL result sets, CSV-files etc., then each element of the ArrayList
	 * represents a row of the source and the header names are column headers of the
	 * source. <br>
	 * - If the associated source includes column-based records and row-based fields
	 * like Properties-files, then the data will be transposed while writing into
	 * the ArrayList. In this case each element of the ArrayList represents a column
	 * of the source and the header names are row headers of the source.<br>
	 * - If the associated source is not in tabular format, then the data will not
	 * be stored within the ArrayList. In this case the adapted DataContainer class
	 * needs to implement the logic how to store the data at runtime e.g.
	 * {@link org.w3c.dom.Document} for HTML- and XML-files.
	 */
	protected ArrayList<String[]> values = new ArrayList<String[]>();

	/**
	 * Adds the header names from HashMap {@link #metaData} to an array of strings.
	 * Header names are read from the key names of the entrySets of HashMap
	 * {@link #metaData}.
	 *
	 * @param inArray Array of strings with header names from HashMap
	 *                {@link #headerNames}
	 * @return concatenated array of strings with all key names of HashMap
	 *         {@link #metaData} and values of the array "inArray"
	 */
	protected String[] addMetaHeaders(String[] inArray) {
		return extendDataSet(inArray, "HEADER");
	}

	/**
	 * Adds the values from HashMap {@link #metaData} to an array of strings. Values
	 * are read from the values of the entrySets of HashMap {@link #metaData}.
	 *
	 * @param inArray Array of strings with values from ArrayList {@link #values}
	 * @return concatenated array of strings with all values of HashMap
	 *         {@link #metaData} and the array "inArray"
	 */
	protected String[] addMetaValues(String[] inArray) {
		return extendDataSet(inArray, "VALUE");
	}

	/**
	 * Compares an array of type <code>String</code> with defined header names with
	 * the header names and indexes of the current <code>DataContainer</code>.
	 *
	 * @param compareHeaders Array of type <code>String</code> with header names and
	 *                       indexes which will be compared against the headers of
	 *                       the current object instance.
	 * @return
	 * 
	 *         <pre>
	 * -1 = mismatching headers
	 * 0 = header names and indexes are identical
	 * 1 = header names match, but with different index order
	 *         </pre>
	 */
	public int checkHeader(String[] compareHeaders) {
		return checkHeader(getHeaders(), addMetaHeaders(compareHeaders));
	}

	/**
	 * Compares the assigned <code>HashMap</code> with header names and indexes with
	 * the headers of the current <code>DataContainer</code> instance.
	 *
	 * @param compareHeaders <code>HashMap</code> with header names and indexes
	 *                       which will be compared against the headers of the
	 *                       current object instance.
	 * @return
	 * 
	 *         <pre>
	 * -1 = mismatching headers
	 * 0 = header names and indexes are identical
	 * 1 = header names match, but with different index order
	 *         </pre>
	 */
	public int checkHeader(HashMap<String, Integer> compareHeaders) {
		// transfer compareHeaders HashMap into String Array
		String[] hd = new String[compareHeaders.size()];
		for (Entry<String, Integer> e : compareHeaders.entrySet()) {
			if (e.getValue() >= hd.length) {
				return -1;
			} else {
				hd[e.getValue()] = e.getKey();
			}
		}
		// check if headers match with existing instance headers and return the
		// check-result
		return checkHeader(getHeaders(), hd);
	}

	/**
	 * Compares the names and order of two Arrays of type <code>String</code>.
	 *
	 * @param referenceHeaders Array of type <code>String</code> with header names
	 *                         and indexes which include reference values for
	 *                         comparison.
	 * @param compareHeaders   Array of type <code>String</code> with header names
	 *                         and indexes which include comparison values.
	 * @return
	 * 
	 *         <pre>
	 * -1 = mismatching headers
	 * 0 = header names and indexes are identical
	 * 1 = header names match, but with different index order
	 *         </pre>
	 */
	public int checkHeader(String[] referenceHeaders, String[] compareHeaders) {
		HashMap<String, Integer> refH = new HashMap<String, Integer>();
		for (int i = 0; i < referenceHeaders.length; i++) {
			refH.put(referenceHeaders[i], i);
		}
		return checkHeader(refH, compareHeaders);
	}

	/**
	 * Compares the header names and order of a <code>HashMap</code> with header
	 * names and order of an Arrays of type <code>String</code>.
	 *
	 * @param referenceHeaders Array of type <code>String</code> with header names
	 *                         and indexes which include reference values for
	 *                         comparison.
	 * @param compareHeaders   Array of type <code>String</code> with header names
	 *                         and indexes which include comparison values.
	 * @return
	 * 
	 *         <pre>
	 * -1 = mismatching headers
	 * 0 = header names and indexes are identical
	 * 1 = header names match, but with different index order
	 *         </pre>
	 */
	public int checkHeader(HashMap<String, Integer> referenceHeaders, String[] compareHeaders) {
		int rc = 0;
		for (int i = 0; i < compareHeaders.length; i++) {
			if (referenceHeaders.containsKey(compareHeaders[i])) {
				if (referenceHeaders.get(compareHeaders[i]) != i) {
					rc = 1;
				}
			} else {
				return -1;
			}
		}
		return rc;
	}

	/**
	 * Checks, if the filter rules match to the values of the given data set.
	 *
	 * @param values String Array with all values of a defined data set (row).
	 * @param fltr   Object of type Filter, which includes one or more filter rules
	 * @return true = values match to the filter; false = values don't match to the
	 * @throws NoSuchHeaderException If the container does not have a header that is defined in the filter 
	 */
	protected boolean checkValuesFilter(String[] values, Filter fltr) throws NoSuchHeaderException {
		boolean returnCode = false;
		for (FilterRule rule : fltr.getFilterRules()) {
			if ((!this.headerNames.containsKey(rule.getHeaderName())) && (!implicitHeaders.contains(rule.getHeaderName()))) {
				throw new NoSuchHeaderException("Header " + rule.getHeaderName() + " doesn't comply to DataContainer!");
			}
		}
		// return true, if no filter rule is defined
		if (fltr.getFilterRules().isEmpty()) {
			returnCode = true;
		} else {
			for (FilterRule fr : fltr.getFilterRules()) {
				// Wildcards * and % will accept any value
				if (fr.getValue() != null) {
					if ((fr.getValue().equals("*")) || (fr.getValue().equals("%"))) {
						returnCode = true;
						break;
					}
				}
				// check values against the filter rules
				returnCode = fr.checkValue(values[headerNames.get(fr.getHeaderName())]);
				if (!returnCode) {
					// skip check and return false, in case that one of the rules fails
					break;
				}
			}
		}
		return returnCode;
	}

	/**
	 * This method is used to prepare an Array of strings before inserting the
	 * values into the ArrayList {@link #values}. <br>
	 * <br>
	 * e.g. remove enclosing quotes for each value in the array<br>
	 * inArray = "val1", "val2", "val3", "val4"<br>
	 * return = val1, val2, val3, val4
	 *
	 * @param inArray an Array of strings whose values are cleaned up
	 * @return an Array of strings with cleaned values
	 */
	protected String[] cleanValues(String[] inArray) {
		ArrayList<String> valList = new ArrayList<String>(Arrays.asList(inArray));
		String[] outArray = new String[inArray.length];
		for (int i = 0; i < inArray.length; i++) {
			outArray[i] = StringUtil.removeEnclosingQuotes(valList.get(i));
		}
		return outArray;
	}

	/**
	 * This method is used to add the values stored in HashMap {@link #metaData} to
	 * an array of strings and returns the extended array.
	 *
	 * @param inArray array of strings with all values from ArrayList
	 *                {@link #values} or header names from HashMap
	 *                {@link #headerNames}
	 * @param target  valid values are "HEADER" and "VALUE" - this defines where to
	 *                get the value value from HashMap {@link #metaData}
	 *                (HEADER=getKey(); VALUE=getValue();)
	 * @return the extended array.
	 */
	protected String[] extendDataSet(String[] inArray, String target) {
		// copy array of strings into ArrayList
		ArrayList<String> valList = new ArrayList<String>(Arrays.asList(inArray));
		// check if metaData is defined for this instance of DataContainer
		if (!getMetaData().isEmpty()) {
			// if metaData is defined, loop through each entrySet of the HashMap "metaData"
			for (Map.Entry<String, String> entry : getMetaData().entrySet()) {
				if (target.equals("HEADER")) {
					// if inArray includes headerNames from the HashMap "columnHeaders", then extend
					// the ArrayList with the key name of the entrySet
					valList.add(entry.getKey());
				} else {
					// else extend the ArrayList with the value of the entrySet
					valList.add(entry.getValue());
				}
			}
		}
		return valList.toArray(new String[valList.size()]);
	}

	/**
	 * Get the delimiter character(s) that separates the columns in the source file.
	 * The delimiter will be used for splitting the data rows and transfer them into
	 * a String Array.
	 *
	 * @return The character(s) used for column separation in the source file.
	 */
	public String getColumnDelimiter() {
		return columnDelimiter;
	}

	/**
	 * Gets the value of the property {@link #containerFormat} which keeps
	 * information about the format of the source that is associated to the
	 * DataContainer.
	 *
	 * @return ENUM element of type {@link EContainerFormat}
	 */
	public EContainerFormat getContainerFormat() {
		return containerFormat;
	}

	/**
	 * Gets the full path and name of the source file from which the data will be
	 * loaded into the DataContainer.
	 *
	 * @return full path and name of the source file
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * Gets the index value of a defined headerName.
	 *
	 * @param headerName Name of the header for which to retrieve the index.
	 * @return Index value of type integer.
	 */
	public int getHeaderIndex(String headerName) {
		int retVal = -1;
		if (headerNames.containsKey(headerName)) {
			retVal = headerNames.get(headerName);
		} else {
			MLogger.getInstance().log(Level.INFO, "Invalid Column Header Name '" + headerName + "'", this.getClass().getSimpleName(), this.getClass().getName(), "getHeaderIndex");
		}
		return retVal;
	}

	/**
	 * @param headerIndex The key of the header in the headers map.
	 * @return The header name at the committed position as string value.
	 */
	public String getHeaderName(int headerIndex) {
		return getHeadersIndexed().get(headerIndex);
	}

	/**
	 * Returns the header names from the {@link headerNames} HashMap as string
	 * array, ordered by their index.
	 * 
	 * @return String array with header names
	 */
	public String[] getHeaderNamesIndexed() {
		HashMap<Integer, String> hm = getHeadersIndexed();
		List<String> headerNamesIndexed = new ArrayList<String>();
		for (int i = 0; i < hm.size(); i++) {
			if (hm.containsKey(i))
				headerNamesIndexed.add(hm.get(i));
		}
		return (headerNamesIndexed.toArray(new String[headerNamesIndexed.size()]));
	}

	/**
	 * Gets the number of headers that match a given regular expression.
	 *
	 * @param toMatch regular expression to search for in header names
	 * @return number of header-names that match the regex
	 */
	public int getHeaderOccurances(String toMatch) {
		int retVal = 0;
		for (String value : headerNames.keySet()) {
			if (value.matches(toMatch)) {
				retVal++;
			}
		}
		return retVal;
	}

	/**
	 * Gets the index of the header row within the source file.
	 * 
	 * @return row index
	 */
	public int getHeaderRowIndex() {
		return headerNamesIndex;
	}

	/**
	 * Gets the <code>HashMap</code> {@link #headerNames} that includes the names
	 * and indexes of column headers within the instance of the DataContainer.
	 *
	 * @return {@link BaseContainer#headerNames} <code>HashMap</code>
	 */
	public HashMap<String, Integer> getHeaders() {
		return headerNames;
	}

	/**
	 * Gets the HashMap columnHeaders with reversed key/value assignment. Whereas
	 * the original columnHeaders # HashMap uses the header names as key and their
	 * index as value, this method returns a HashMap with the index as key and the
	 * header name as value.
	 *
	 * @return HashMap with indexes and names of the columnHeaders HashMap
	 */
	public HashMap<Integer, String> getHeadersIndexed() {
		HashMap<Integer, String> indexedHeaders = new HashMap<>();
		for (String k : headerNames.keySet()) {
			indexedHeaders.put(headerNames.get(k), k);
		}
		return indexedHeaders;
	}

	/**
	 * This method returns an array of type integer with the indexes of defined
	 * header names. This can either be a single header, a semicolon separated
	 * string with multiple headers, or an empty string that returns the indexes of
	 * all headers.
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
	 * @return Array of type integer with header indexes in the order as defined by
	 *         the headerName parameter
	 */
	public int[] getHeadersIndexes(String headerName) {
		String[] hArray = new String[0];
		if (headerName != null && headerName.length() > 0) {
			hArray = new String[headerName.split(";").length];
			hArray = headerName.split(";");
		} else {
			hArray = new String[getHeaders().size()];
			hArray = getHeaders().keySet().toArray(new String[getHeaders().size()]);
		}

		int[] hi = new int[hArray.length];
		for (int i = 0; i < hArray.length; i++) {
			hi[i] = getHeaderIndex(hArray[i]);
		}
		return hi;
	}

	/**
	 * Returns the filter rules which belong to an implicit header column. This
	 * allows to use same column and row based getter and setter methods for tree
	 * formatted sources like XML, as they are used for tabular formatted sources.
	 * e.g. the {@link #getImplFilterRules(Filter)} method is used within
	 * {@link XMLDataContainer#getColumn} to limit the data records for the search
	 * to specified xPath(s).
	 * 
	 * @param fltr Filter object with the complete filter rules for a data search
	 * @return List object with all FilterRules that belong to implicit columns
	 */
	protected List<FilterRule> getImplFilterRules(Filter fltr) {
		List<FilterRule> frList = new ArrayList<FilterRule>();
		for (FilterRule fr : fltr.getFilterRules()) {
			if (implicitHeaders.contains(fr.getHeaderName())) {
				frList.add(fr);
			}
		}
		return frList;
	}

	/**
	 * Sets the name of columns that will be populated with processed data by
	 * implemented logic of the individual DataContainer. e.g. The implicitHeaders
	 * will be used by the {@link XMLDataContainer} to add the column XPath, where
	 * the complete path of each tag will be inserted. This allows to transpose data
	 * from tree format into tabular format.
	 * 
	 * @return object of type java.util.set with column names
	 */
	public Set<String> getImplicitHeaders() {
		return implicitHeaders;
	}

	public InputStream getInputStream() {
		return inputStream;
	}

	/**
	 * Gets the HashMap with MetaData
	 *
	 * @return the metaData HashMap of this object instance
	 */
	public HashMap<String, String> getMetaData() {
		return metaData;
	}

	/**
	 * Returns the {@link #resultSet} property with SQL results of type
	 * {@link java.sql.ResultSet}. This property is used to store the data of
	 * {@link RSDataContainer}.
	 * 
	 * @return {@link #resultSet}
	 */
	public ResultSet getResultSet() {
		return resultSet;
	}
	
	public String getRootNode() {
		return rootNode;
	}

	/**
	 * Inserts additional headers and values into the DataContanier that will added
	 * to the DataSets, transmitted from the sources. e.g. If you create an instance
	 * of {@link CSVDataContainer} with a csv.file as source, you can put the file
	 * content into the {@link CSVDataContainer} and add the filename to each
	 * DataSet.<br>
	 * The following sample reads contact data from CSV file and adds the column
	 * Company with the company name for each data set while putting the data into
	 * the {@link CSVDataContainer}.
	 *
	 * <pre>
	 * DataContainer dc = new DataContainer(";", EHeader.COLUMN); // creates a {@link CSVDataContainer}
	 * dc.putMetaData("Company", "happy inc.");
	 * dc.readData("c:\\data\\happy-inc.csv");
	 * </pre>
	 * <p>
	 * The readData method of the appropriate DataContainer instance will
	 * automatically extend all DataSets with all headers and values, added to the
	 * metaData HashMap.<br>
	 * <br>
	 *
	 * @param headerName Header name of DataSet within the DataContainer
	 * @param value      Value as String that will be added to each DataSet into the
	 *                   column of headerName
	 */
	public void putMetaData(String headerName, String value) {
		metaData.put(headerName, value);
	}

	/**
	 * Sets the <code>columnDelimiter</code> property which is used for splitting
	 * the data rows of the source file into a String Array.
	 *
	 * @param cDel The character(s) used for column separation in the source file.
	 */
	public void setColumnDelimiter(String cDel) {
		columnDelimiter = cDel;
	}

	/**
	 * Sets the format information of the source used for this instance of
	 * DataContainer.
	 *
	 * @param cFormat ENUM value of type EContainerFormat
	 */
	public void setContainerFormat(EContainerFormat cFormat) {
		containerFormat = cFormat;
	}

	/**
	 * Sets the property <code>fileName</code> with the full path and name of the
	 * source file.
	 *
	 * @param file Full path and name of the source file as String
	 */
	public void setFileName(String file) {
		fileName = file;
	}

	/**
	 * Sets the index of the header row within the source file.
	 * 
	 * @param headerIndex Zero based index where the names of header are placed.
	 */
	public void setHeaderRowIndex(int headerIndex) {
		headerNamesIndex = headerIndex;
	}

	/**
	 * Adds header names into the <code>HashMap</code> {@link #headerNames}. It's
	 * proofed if the name already exists in the <code>HashMap</code>. In case of
	 * duplicated names, an index suffix will be added to the name (i.e GF#,GF#_2
	 * ...). Furthermore, the name of the header is added with an index
	 * (<code>i</code>) as "helper" for the assignment of headers to the values.
	 *
	 * @param in_headers List with the name of the headers to be added
	 */
	public void setHeaders(List<String> in_headers) {
		this.setHeaders(in_headers.toArray(new String[in_headers.size()]));
	}

	/**
	 * Adds header names into the <code>HashMap</code> {@link #headerNames}. It's
	 * proofed if the name already exists in the <code>HashMap</code>. In case of
	 * duplicated names, an index suffix will be added to the name (i.e GF#,GF#_2
	 * ...). Furthermore, the name of the header is added with an index
	 * (<code>i</code>) as "helper" for the assignment of headers to the values.
	 *
	 * @param in_headers Array with the name of the headers to be added
	 */
	public void setHeaders(String[] in_headers) {
		int i = this.headerNames.size();
		for (String col : addMetaHeaders(in_headers)) {
			if (!this.headerNames.containsKey(col)) {
				this.headerNames.put(col, i);
			} else {
				String col_tmp = col;
				int count = 2;
				while (this.headerNames.containsKey(col_tmp)) {
					col_tmp = col + "_" + count;
					count++;
				}
				this.headerNames.put(col_tmp, i);
			}
			i++;
		}
	}

	/**
	 * Sets content of type {@link java.io.InputStream} to the {@link #inputStream}
	 * property that is used by DataContainers that have document type data
	 * connected like {@link XMLDataContainer} using {@link org.w3c.dom.Document}
	 * for XML and HTML formats. The {@link #inputStream} property can be used in
	 * case that the data source is not a file.
	 * 
	 * @param inStream Data of type {@link java.io.InputStream}
	 */
	public void setInputStream(InputStream inStream) {
		inputStream = inStream;
	}

	/**
	 * This method is similar to the {@link #putMetaData} method with the
	 * difference, that existing metadata with the same headerName will be
	 * overwritten. This method should be used preferably to avoid duplicated column
	 * headers.
	 * 
	 * @param headerName Header name of DataSet within the DataContainer
	 * @param value      Value as String that will be added to each DataSet into the
	 *                   column of headerName
	 */
	public void setMetaData(String headerName, String value) {
		if (metaData.containsValue(headerName)) {
			metaData.replace(headerName, value);
		} else {
			metaData.put(headerName, value);
		}
	}

	/**
	 * Assigns an object of type {@link java.sql.ResultSet} with SQL results to the
	 * property {@link #resultSet}. This property is used by the
	 * {@link RSDataContainer}
	 * 
	 * @param rs Object of type {@link java.sql.ResultSet}
	 */
	public void setResultSet(ResultSet rs) {
		resultSet = rs;
	}
	
	public void setRootNode(String rn) {
		rootNode = rn;
	}

}
