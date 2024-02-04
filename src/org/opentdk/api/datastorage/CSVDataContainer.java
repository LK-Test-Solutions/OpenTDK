package org.opentdk.api.datastorage;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;

import org.opentdk.api.filter.Filter;
import org.opentdk.api.io.CSVUtil;
import org.opentdk.api.io.FileUtil;
import org.opentdk.api.io.XMLEditor;

public class CSVDataContainer implements SpecificContainer {

	/**
	 * The character(s) that define the delimiter of columns within tabular files. This delimiter is
	 * used by the {@link DataContainer#readData()} methods to split the rows of the source file into a
	 * String Array and by the {@link CSVDataContainer#exportContainer(String)} methods to write the
	 * elements of the {@link #values} ArrayList into the target file.
	 */
	private String delimiter = ";";

	/**
	 * This property defines the record index of the source, where the header names are read from. All
	 * values of this record will be put into the HashMap {@link #headerNames}.<br>
	 * If the headerType of the {@link DataContainer} is {@link EHeader#COLUMN}, then this index defines
	 * the row index of the source that includes the header names.<br>
	 * If the headerType of the {@link DataContainer} is {@link EHeader#ROW}, then this index defines
	 * the column index of the source that includes the header names.
	 */
	private int headerNamesIndex = 0;

	/**
	 * When the first row of a CSV file includes header names and all other rows include values, then
	 * the names of the first row will be stored in this object with their original index.
	 */
	private final HashMap<String, Integer> headerNames = new HashMap<>();

	/**
	 * ArrayList with an array of strings where content of tabular sources is stored at runtime of an
	 * application. <br>
	 * If the associated source includes row based records and column based fields like SQL result sets,
	 * CSV files etc., then each element of the ArrayList represents a row of the source and the header
	 * names are column headers of the source. <br>
	 * If the associated source includes column based records and row based fields like Properties
	 * files, then the data will be transposed while writing into the ArrayList. In this case each
	 * element of the ArrayList represents a column of the source and the header names are row headers
	 * of the source.<br>
	 * If the associated source is not in tabular format, then the data will not be stored within the
	 * ArrayList. In this case the adapted DataContainer class needs to implement the logic how to store
	 * the data at runtime e.g. {@link org.w3c.dom.Document} for HTML and XML files.
	 */
	private List<String[]> values = new ArrayList<>();

	/**
	 * This HashMap is used to define fields and values that will be appended to each record added to
	 * the {@link DataContainer} by the {@link DataContainer#readData()},
	 * {@link CSVDataContainer#addRow()} and the {@link CSVDataContainer#appendData(String)}
	 * methods.<br>
	 * E.g. add the name of the source file to each record, when putting the content of multiple files
	 * into one instance of the {@link DataContainer}.
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
	private final HashMap<String, String> metaData = new HashMap<>();
	
	public static CSVDataContainer newInstance() {		
		return new CSVDataContainer();
	}
	
	protected CSVDataContainer() {
		
	}

	// INHERITED METHODS
	
	@Override
	public String asString() {
		return CSVUtil.asString(values);
	}

	@Override
	public void readData(File srcFile) throws IOException {
		values = CSVUtil.readFile(srcFile.getPath(), delimiter, StandardCharsets.UTF_8);
	}
	

	@Override
	public void readData(InputStream stream) throws IOException {
		// TODO Auto-generated method stub		
	}

	@Override
	public void writeData(File outputPath) throws IOException {
		CSVUtil.writeFile(values, outputPath.getPath(), delimiter, StandardCharsets.UTF_8);
	}
	
	// ROW
	
	public List<String> getRow(int rowIndex) {
		return CSVUtil.getRow(values, rowIndex);
	}
	
	public void addRow() {
		CSVUtil.addRow(values, values.size() - getMetaData().size(), delimiter);
	}
	
	public void addRow(int rowIndex) {
		CSVUtil.addRow(values, rowIndex, delimiter);
	}
	
	public void addRow(int rowIndex, String[] rowValues) {
		CSVUtil.addRow(values, rowIndex, addMetaValues(rowValues), delimiter);
	}

	// COLUMN 
	
	public List<String> getColumn(int columnIndex) {
		return CSVUtil.getColumn(values, columnIndex);
	}

	public List<String> getColumn(String columnName) {
		return CSVUtil.getColumn(values, columnName);
	}
	
	public void addColumn(String column) {
		CSVUtil.addColumn(values, headerNames, column);
	}
	
	public void addColumn(String column, boolean useExisting) {
		CSVUtil.addColumn(values, headerNames, column, useExisting);
	}

	public String getValue(int rowIndex, int columnIndex) {
		return CSVUtil.getValue(values, rowIndex, columnIndex);
	}
	
	// VALUE

	public void setValue(String updateColumn, String oldValue, String newValue) {
		CSVUtil.updateRow(values, updateColumn, oldValue, newValue);
	}

	public void setValue(String updateColumn, int valueIndex, String newValue) {
//		CSVUtil.updateRow(values, updateColumn, oldValue, newValue);
	}
	
	/**
	 * Adds the header names from HashMap {@link #metaData} to an array of strings. Header names are
	 * read from the key names of the entrySets of HashMap {@link #metaData}.
	 *
	 * @param inArray Array of strings with header names from HashMap {@link #headerNames}
	 * @return concatenated array of strings with all key names of HashMap {@link #metaData} and values
	 *         of the array <b>inArray</b>
	 */
	protected String[] addMetaHeaders(String[] inArray) {
		return extendDataSet(inArray, "HEADER");
	}

	/**
	 * Adds the values from HashMap {@link #metaData} to an array of strings. Values are read from the
	 * values of the entrySets of HashMap {@link #metaData}.
	 *
	 * @param inArray Array of strings with values from ArrayList {@link #values}
	 * @return concatenated array of strings with all values of HashMap {@link #metaData} and the array
	 *         <b>inArray</b>
	 */
	protected String[] addMetaValues(String[] inArray) {
		return extendDataSet(inArray, "VALUE");
	}
	
	/**
	 * This method is used to add the values stored in HashMap {@link #metaData} to an array of strings
	 * and returns the extended array.
	 *
	 * @param inArray array of strings with all values from ArrayList {@link #values} or header names
	 *                from HashMap {@link #headerNames}
	 * @param target  valid values are "HEADER" and "VALUE" - this defines where to get the value
	 *                from HashMap {@link #metaData} (HEADER=getKey(); VALUE=getValue();)
	 * @return the extended array.
	 */
	private String[] extendDataSet(String[] inArray, String target) {
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
	 * @return Length of the {@link #values} list
	 */
	public int getRowCount() {
		return values.size();
	}

	/**
	 * @return {@link #values}
	 */
	public List<String[]> getValues() {
		return values;
	}

	/**
	 * @return {@link #delimiter}
	 */
	public String getDelimiter() {
		return delimiter;
	}

	/**
	 * @param colDelimiter {@link #delimiter}
	 */
	public void setDelimiter(String colDelimiter) {
		delimiter = colDelimiter;
	}

	/** 
	 * @return {@link #headerNamesIndex}
	 */
	public int getHeaderNamesIndex() {
		return headerNamesIndex;
	}

	/**
	 * @param headerIndex {@link #headerNamesIndex}
	 */
	public void setHeaderNamesIndex(int headerIndex) {
		headerNamesIndex = headerIndex;
	}

	/**
	 * @return {@link #headerNames}
	 */
	public HashMap<String, Integer> getHeaderNames() {
		return headerNames;
	}

	/**
	 * @return {@link #metaData}
	 */
	public HashMap<String, String> getMetaData() {
		return metaData;
	}
}
