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
package org.opentdk.api.datastorage;

import org.opentdk.api.filter.Filter;
import org.opentdk.api.filter.FilterRule;
import org.opentdk.api.io.FileUtil;
import org.opentdk.api.io.XFileWriter;
import org.opentdk.api.logger.MLogger;
import org.opentdk.api.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class TabularContainer implements SpecificContainer {

	/**
	 * The character(s) that define the delimiter of columns within tabular files. This delimiter is
	 * used by the {@link SpecificContainer#readData(File)} methods to split the rows of the source file into a
	 * String Array and by the {@link TabularContainer#exportContainer(String)} methods to write the
	 * elements of the {@link #values} ArrayList into the target file.
	 */
	private String columnDelimiter = ";";

	/**
	 * This property defines the record index of the source, where the header names are read from. All
	 * values of this record will be put into the HashMap {@link #headerNames}.<br>
	 * If the headerType of the {@link DataContainer} is {@link EHeader#COLUMN}, then
	 * this index defines the row index of the source that includes the header names.<br>
	 * If the headerType of the {@link DataContainer} is {@link EHeader#ROW}, then
	 * this index defines the column index of the source that includes the header names.
	 */
	private int headerNamesIndex = 0;

	/**
	 * The headerNames property is used to assign header names to each index of the string
	 * arrays, stored as elements of the ArrayList {@link #values}. This HashMap allows to locate values
	 * within the string arrays by name instead of index.<br>
	 * E.g. if the first row of a CSV file includes header names and all other rows include values, then
	 * the names of the first row will be stored in the headerNames HashMap with their original
	 * index.
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
	private final ArrayList<String[]> values = new ArrayList<>();

	/**
	 * This HashMap is used to define fields and values that will be appended to each
	 * record added to the {@link DataContainer} by the {@link SpecificContainer#readData(File)},
	 * {@link TabularContainer#addRow()} and the {@link TabularContainer#appendData(String)} methods.<br>
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
	
	private EHeader headerOrientation = EHeader.COLUMN;

	public static TabularContainer newInstance() {
		return new TabularContainer();
	}
	
	protected TabularContainer() {
		super();
	}
	
	// INHERTIED METHODS
	
	@Override
	public String asString() {
		return getValuesAsString();
	}

	@Override
	public String asString(EContainerFormat format) {
		return asString(format, 0, false);
	}

	public String asString(EContainerFormat format, int depth) {
		return asString(format, depth, false);
	}

	public String asString(EContainerFormat format, int depth, boolean skipParent) {
		switch (format) {
			case CSV:
				MLogger.getInstance().log(Level.INFO, "Format already is CSV. Call asString instead.", getClass().getSimpleName(), "asString(format)");
				return asString();
			case JSON:
				if(skipParent) {
					StringBuilder ret = new StringBuilder();
					ArrayList<String> jsonObjectList = new ArrayList<>();
					for (String[] row : values) {
						JSONObject rowObject = new JSONObject();
						for (int i = 0; i < row.length; i++) {
							rowObject.put(getHeaderName(i), row[i]);
						}
						jsonObjectList.add(rowObject.toString(depth));
					}
					int rowIndex = 0;
					for (String obj : jsonObjectList) {
						ret.append(obj);
						if(rowIndex < jsonObjectList.size() - 1) {
							ret.append(",\n");
						}
						rowIndex++;
					}
					return ret.toString();
				} else {
					JSONArray jsonArray = new JSONArray();
					for (String[] row : values) {
						JSONObject rowObject = new JSONObject();
						for (int i = 0; i < row.length; i++) {
							rowObject.put(getHeaderName(i), row[i]);
						}
						jsonArray.put(rowObject);
					}
					return jsonArray.toString(depth);
				}
			default:
				MLogger.getInstance().log(Level.WARNING, "Format not supported to export from CSV", getClass().getSimpleName(), "asString(format)");
				return "";
		}
	}

	/**
	 * @return CSV content as JSONObject with leading and trailing '{}' and as array of objects, where one object has the column headers
	 * as keys and the values of the row as values.
	 */
	public JSONObject toJson() {
		JSONObject jsonObject = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		for (String[] row : values) {
			JSONObject rowObject = new JSONObject();
			for (int i = 0; i < row.length; i++) {
				rowObject.put(getHeaderName(i), row[i]);
			}
			jsonArray.put(rowObject);
		}
		jsonObject.put("data", jsonArray);
		return jsonObject;
	}

	@Override
	public void readData(File sourceFile) throws IOException {
		if (sourceFile != null && sourceFile.exists()) {
			if (StringUtils.isNotBlank(sourceFile.getPath())) {
				putFile(sourceFile.getPath(), getColumnDelimiter());
			}
		}	
	}
	
	public void setOrientation(EHeader orientation) {
		headerOrientation = orientation;
	}
	
	private void putFile(String fileName, String columnDelimiter) {
		switch (headerOrientation) {
			case COLUMN:
				putDatasetRows(fileName, columnDelimiter);
				break;
			case ROW:
				putDatasetColumns(fileName, columnDelimiter);
				break;
			default:
				MLogger.getInstance().log(Level.WARNING, "Source header '" + headerOrientation + "' is not valid for this method. Either EHeader.COLUMN or EHeader.ROW.", getClass().getSimpleName(), "putFile");
				break;
		}
	}
	
	/**
	 * This method reads data from a CSV file, converts it to a row orientation and puts it row by row
	 * in the DataContainer's value property. Every column corresponds to one data set, so the header
	 * orientation has to be row wise.
	 *
	 * @param fileName        The filename of the file to read from
	 * @param columnDelimiter The column delimiter used to separate columns in the CSV file
	 */
	private void putDatasetColumns(String fileName, String columnDelimiter) {
		List<List<String>> tmpRowsList = new ArrayList<>();
		int rowIndex = -1;

		List<String> rows = FileUtil.getRowsAsList(fileName);
		for (String row : rows) {
			rowIndex++;
			String[] valArray = cleanValues(row.split(columnDelimiter));
			int firstValueIndex = 0;
			for (int i = 0; i < valArray.length; i++) {
				// if withHeaders = true, add values in the first column to the headers HashMap,
				// and start values at index 1
				if (i == 0) {
					if (!getHeaders().containsValue(rowIndex)) {
						getHeaders().put(valArray[0], rowIndex);
					}
					firstValueIndex = 1;
				}
				// initialize one tmpRowsList for each value in valArray
				while (tmpRowsList.size() < valArray.length - firstValueIndex) {
					tmpRowsList.add(new ArrayList<>());
				}
				// write all values into temporary lists
				if (i >= firstValueIndex) {
					tmpRowsList.get(i - firstValueIndex).add(valArray[i]);
				}
			}
		}

		for (int i = 0; i < tmpRowsList.size(); i++) {
			// get existing values from 'values' List Object
			String[] currentValues = new String[0];
			if ((!values.isEmpty()) && (rowIndex <= values.size() - 1)) {
				currentValues = Arrays.copyOf(values.get(rowIndex), values.get(rowIndex).length);
			}
			String[] newValues = tmpRowsList.get(i).toArray(new String[tmpRowsList.get(i).size()]);
			List<String> both = new ArrayList<>(currentValues.length + newValues.length);
			Collections.addAll(both, currentValues);
			Collections.addAll(both, newValues);
			if (rowIndex <= values.size() - 1) {
				setRow(rowIndex, both.toArray(new String[both.size()]));
			} else {
				addRow(both.toArray(new String[both.size()]));
			}
		}
	}

	/**
	 * This method reads data from a CSV file and puts it row by row in the DataContainer's value
	 * property. Every row corresponds to one data set, so the header orientation has to be column wise.
	 *
	 * @param fileName        The filename of the file to read from
	 * @param columnDelimiter The column delimiter used to separate columns in the CSV file
	 */
	private void putDatasetRows(String fileName, String columnDelimiter) {
		int rowIndex = -1;
		int headerState = 0;
		HashMap<Integer, Integer> sortMap = null;

		List<String> rows = FileUtil.getRowsAsList(fileName);
		for (String row : rows) {
			rowIndex++;
			String[] valArray = row.split(columnDelimiter, -1);

//			String[] valArray = dc.cleanValues(row.split(columnDelimiter, -1));
			if (rowIndex < getHeaderRowIndex()) {
				MLogger.getInstance().log(Level.INFO, "Skipping row with index " + rowIndex + "! Just rows after the headerRowIndex will be loaded into DataContainer.", this.getClass().getSimpleName(), "putDataSetRows");
			} else if (rowIndex == getHeaderRowIndex()) {
				if (getHeaders().isEmpty()) {
					setHeaders(valArray);
				}
				headerState = checkHeader(valArray);
				if (headerState < 0) {
					for (String h : valArray) {
						if (!getHeaders().containsKey(h)) {
							addColumn(h);
						}
					}
				}
				if (headerState != 0)
					sortMap = sortHeadersIndexes(valArray);
			} else {
				// FIXME This condition fails when metadata columns get added to the data set
				if (addMetaValues(valArray).length == (getHeaders().size() + metaData.size())) {
					if (headerState == 0) {
						addRow(valArray);
					} else {
						addRow(sortValues(sortMap, valArray));
					}
				} else {
					MLogger.getInstance().log(Level.WARNING, "The number of values doesn't match to the number of headers! Values will not be added to DataContainer.", this.getClass().getSimpleName(), "putDataSetRows");
				}
			}
		}
	}

	@Override
	public void readData(File sourceFile, Filter filter) throws IOException {
		// TODO Auto-generated method stub		
	}

	@Override
	public void readData(InputStream stream) throws IOException {
		// TODO Auto-generated method stub	
	}

	@Override
	public void readData(InputStream stream, Filter filter) throws IOException {
		// TODO Auto-generated method stub		
	}

	@Override
	public void writeData(File outputFile) throws IOException {
		List<String[]> writeable = new ArrayList<>();
		writeable.add(getHeaderNamesIndexed());
		for (String[] dataArr : getRowsList()) {
			writeable.add(dataArr);
		}
		FileUtil.checkDir(outputFile.getParent(), true);
		XFileWriter writer = new XFileWriter(outputFile);
		writer.writeLines(writeable);
		writer.close();		
	}
	
	// ADD
	
	public void addColumn(String col) {
		addColumn(col, false);
	}

	
	public void addColumn(String col, boolean useExisting) {
		if (!this.headerNames.containsKey(col)) {
			this.headerNames.put(col, headerNames.size());
		} else if (!useExisting) {
			String col_tmp = col;
			int count = 2;
			while (this.headerNames.containsKey(col_tmp)) {
				col_tmp = col + "_" + count;
				count++;
			}
			this.headerNames.put(col_tmp, headerNames.size());
		} else {
			return;
		}
		for (int i = 0; i < values.size(); i++) {
			String[] newArr = Arrays.copyOf(values.get(i), values.get(i).length + 1);
			newArr[newArr.length - 1] = "";
			values.set(i, newArr);
		}
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
	 * Adds a new row to the container with empty values. The size of the row is equal to the size of the header without
	 * headers that were added as metadata.
	 */
	public void addRow() {
		int rowSize = getHeaders().size() - getMetaData().size();
		addRow(new String[rowSize]);
	}
	
	public void addRow(int rowIndex, String[] rowValues) {
		values.add(rowIndex, addMetaValues(rowValues));
	}
	
	public void addRow(String[] row) {
		// TODO when readData with filter was called, the data is already filtered, what's the purpose here
//		if ((getFilter() != null) && (!getFilter().getFilterRules().isEmpty())) {
//			try {
//				if (!checkValuesFilter(row, getFilter())) {
//					return;
//				}
//			} catch (NoSuchHeaderException e) {
//				MLogger.getInstance().log(Level.SEVERE, e);
//				throw new RuntimeException(e);
//			}
//		}
		values.add(addMetaValues(row));
	}

	
	public void addRows(List<String[]> rows) {
		for (String[] row : rows) {
			addRow(row);
		}
	}

	// APPEND
	
	public void appendData(String fileName) throws IOException {
		if (getColumnDelimiter() == null) {
			setColumnDelimiter(";");
		}
		appendData(fileName, getColumnDelimiter());
	}

	
	public void appendData(String fileName, String columnDelimiter) throws IOException {
		setColumnDelimiter(columnDelimiter);
		// TODO when readData with filter was called, the data is already filtered, what's the purpose here
//		if (getFilter() == null) {
//			setFilter(new Filter());
//		}
		readData(new File(fileName));
	}

	public void appendDataContainer(DataContainer dc) {
		if (checkHeader(dc.tabInstance().getHeaders()) == 0) {
			values.addAll(dc.tabInstance().getValues());
		} else if (checkHeader(dc.tabInstance().getHeaders()) == 1) {
			int i = 0;
			while (i < dc.tabInstance().getRowCount()) {
				String[] row = new String[getColumnCount()];
				for (int j = 0; j < getColumnCount(); j++) {
					try {
						row[j] = dc.tabInstance().getValuesAsList(getHeadersIndexed().get(j)).get(i);
					} catch (RuntimeException e) {
						row[j] = null;
					}
				}
				i++;
				values.add(row);
			}
		} else {
			MLogger.getInstance().log(Level.WARNING, "Headers of appending DataContainer don't match to the headers of the current instance. DataContainer will not be appended!", getClass().getSimpleName(), getClass().getName(), "appendDataContainer");
		}
	}

	// CHECK
	
	public int checkHeader(HashMap<String, Integer> compareHeaders) {
		// transfer compareHeaders HashMap into String Array
		String[] hd = new String[compareHeaders.size()];
		for (Map.Entry<String, Integer> e : compareHeaders.entrySet()) {
			if (e.getValue() >= hd.length) {
				return -1;
			} else {
				hd[e.getValue()] = e.getKey();
			}
		}
		// check if headers match with existing instance headers and return the check result
		return checkHeader(getHeaders(), hd);
	}

	
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

	
	public int checkHeader(String[] compareHeaders) {
		return checkHeader(getHeaders(), addMetaHeaders(compareHeaders));
	}

	
	public int checkHeader(String[] referenceHeaders, String[] compareHeaders) {
		HashMap<String, Integer> refH = new HashMap<String, Integer>();
		for (int i = 0; i < referenceHeaders.length; i++) {
			refH.put(referenceHeaders[i], i);
		}
		return checkHeader(refH, compareHeaders);
	}

	/**
	 * Checks, if the filter rules match to the values of the given data set.
	 *
	 * @param values String Array with all values of a defined data set (row).
	 * @param fltr   Object of type Filter, which includes one or more filter rules
	 * @return true = values match to the filter; false = values don't match to the
	 * @throws NoSuchHeaderException If the container does not have a header that is defined in the
	 *                               filter
	 */
	private boolean checkValuesFilter(String[] values, Filter fltr) throws NoSuchHeaderException {
		boolean returnCode = false;
		for (FilterRule rule : fltr.getFilterRules()) {
//			if ((!this.headerNames.containsKey(rule.getHeaderName())) && (!getImplicitHeaders().contains(rule.getHeaderName()))) {

			if (!headerNames.containsKey(rule.getHeaderName())) {
				throw new NoSuchHeaderException("Header " + rule.getHeaderName() + " doesn't comply to DataContainer!");
			}
		}
		// return true, if no filter rule is defined
		if (fltr.getFilterRules().isEmpty()) {
			returnCode = true;
		} else {
			for (FilterRule fr : fltr.getFilterRules()) {
				// Wild cards * and % will accept any value
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
	
	// PREPARE / CLEAN

	/**
	 * This method is used to prepare an Array of strings before inserting the values into the ArrayList
	 * {@link #values}. <br>
	 * <br>
	 * e.g. remove enclosing quotes for each value in the array<br>
	 * inArray = "val1", "val2", "val3", "val4"<br>
	 * return = val1, val2, val3, val4
	 *
	 * @param inArray an Array of strings whose values are cleaned up
	 * @return an Array of strings with cleaned values
	 */
	private String[] cleanValues(String[] inArray) {
		ArrayList<String> valList = new ArrayList<String>(Arrays.asList(inArray));
		String[] outArray = new String[inArray.length];
		for (int i = 0; i < inArray.length; i++) {
			outArray[i] = StringUtil.removeEnclosingQuotes(valList.get(i));
		}
		return outArray;
	}

	public String[] createPreparedRow(){
		return createPreparedRow(new String[0]);
	}

	
	public String[] createPreparedRow(String[] valueSets){
		String[] preparedRow = new String[getHeaderNamesIndexed().length];
		for(int i=0; i< preparedRow.length; i++){
			preparedRow[i] = "";
		}
		for(String valueSet:valueSets){
			String[] setItems = valueSet.split("=");
			preparedRow[getHeaderIndex(setItems[0])] = setItems[1];
		}
		return preparedRow;
	}

	// DELETE
	
	public void deleteValue(String headerName) {
		int headerIndex = getHeaderIndex(headerName);
		values.get(0)[headerIndex] = null;
		// TODO why does deleteValue have to write into file instantly ?
//		if (!dc.getInputFile().getPath().isEmpty()) {
//			try {
//				writeData(dc.getInputFile().getPath());
//			} catch (IOException e) {
//				MLogger.getInstance().log(Level.SEVERE, e);
//			}
//		}
	}

	
	public void deleteRow(int index) {
		values.remove(index);
	}

	
	public void deleteRows(Filter fltr) {
		int[] indexes = getRowsIndexes(fltr);
		if (indexes.length == 0) {
			MLogger.getInstance().log(Level.WARNING, "No row indexes detected for the filter criteria", getClass().getSimpleName(), "deleteRows");
		} else {
			for (int index : indexes) {
				getValues().remove(index);
			}
			// TODO why does deleteRows have to write into file instantly ?
//			if (!dc.getInputFile().getName().isEmpty()) {
//				try {
//					writeData(dc.getInputFile().getPath());
//				} catch (IOException e) {
//					MLogger.getInstance().log(Level.SEVERE, e);
//				}
//			}
		}
	}

	// EXPORT
	
	public void exportContainer(String fileName) throws IOException {
		exportContainer(fileName, ";");
	}

	
	public void exportContainer(String fileName, String columnDelimiter) throws IOException {
		HashMap<Integer, String> hm = getHeadersIndexed();
		XFileWriter writer = new XFileWriter(new File(fileName));
		if (writer != null) {
			switch (headerOrientation) {
				case COLUMN:
					writer.writeLine(hm.values().toArray(new String[hm.values().size()]), columnDelimiter);
					for (String[] row : getRowsList()) {
						writer.writeLine(row, columnDelimiter);
					}
					break;
				case ROW:
					List<String[]> colList = getColumnsList();
					for (int i = 0; i < colList.size(); i++) {
						writer.writeLine(hm.get(i) + columnDelimiter + (String.join(columnDelimiter, colList.get(i))));
					}
					break;
				default:
					MLogger.getInstance().log(Level.WARNING, "Header Type '" + headerOrientation.toString() + "' not supported by for export!", getClass().getSimpleName(), getClass().getName(), "exportContainer");
					return;
			}
			writer.close();
		}
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

	// GET
	
	public String[] getColumn(int index) {
		return getColumn(getHeaderName(index), new int[0], new Filter());
	}

	
	public String[] getColumn(int index, Filter fltr) {
		return getColumn(getHeaderName(index), new int[0], fltr);
	}

	
	public String[] getColumn(String colName) {
		return getColumn(colName, new int[0], new Filter());
	}

	
	public String[] getColumn(String colName, Filter fltr) {
		return getColumn(colName, new int[0], fltr);
	}

	
	public String[] getColumn(String colName, int[] rowIndexes, Filter fltr) {
		List<String[]> outLst = getColumnsList(colName.split(";"), rowIndexes, fltr);
		if (outLst.size() > 0) {
			return outLst.get(0);
		}
		return new String[0];
	}

	
	public int getColumnCount() {
		return getHeaders().size();
	}

	
	public String getColumnDelimiter() {
		return columnDelimiter;
	}

	
	public List<String[]> getColumnsList() {
		return getColumnsList(new String[0], new int[0], new Filter());
	}

	
	public List<String[]> getColumnsList(Filter rowFilter) {
		return getColumnsList(new String[0], new int[0], rowFilter);
	}

	
	public List<String[]> getColumnsList(String columnHeaders, Filter rowFilter) {
		return getColumnsList(columnHeaders.split(";"), new int[0], rowFilter);
	}

	
	public List<String[]> getColumnsList(String[] columnHeaders, int[] rowIndexes, Filter rowFilter) {
		List<String[]> colList = new ArrayList<String[]>();
		List<String[]> rowValues = getRowsList(rowIndexes, columnHeaders, rowFilter);
		List<List<String>> colsTmp = new ArrayList<List<String>>();
		for (String[] rowArray : rowValues) {
			for (int i = 0; i < rowArray.length; i++) {
				if (colsTmp.size() <= i) {
					colsTmp.add(new ArrayList<String>());
				}
				colsTmp.get(i).add(rowArray[i]);
			}
		}
		for (List<String> col : colsTmp) {
			colList.add(col.toArray(new String[col.size()]));
		}
		return colList;
	}

	public List<?> getColumnsList(Filter rowFilter, Class<?> storageObject) {
		List<?> ret = new ArrayList<>();
		List<String[]> rows = getColumnsList(new String[0], new int[0], rowFilter);
		for(String[] row : rows) {
			for(int i = 0; i < row.length; i++) {
				// .. TODO
			}
		}
		return ret;
	}
	
	public int getHeaderIndex(String headerName) {
		int retVal = -1;
		if (headerNames.containsKey(headerName)) {
			retVal = headerNames.get(headerName);
		}
		return retVal;
	}

	
	public String getHeaderName(int headerIndex) {
		return getHeadersIndexed().get(headerIndex);
	}

	
	public String[] getHeaderNamesIndexed() {
		HashMap<Integer, String> hm = getHeadersIndexed();
		List<String> headerNamesIndexed = new ArrayList<String>();
		for (int i = 0; i < hm.size(); i++) {
			if (hm.containsKey(i))
				headerNamesIndexed.add(hm.get(i));
		}
		return (headerNamesIndexed.toArray(new String[headerNamesIndexed.size()]));
	}

	
	public int getHeaderOccurances(String toMatch) {
		int retVal = 0;
		for (String value : headerNames.keySet()) {
			if (value.matches(toMatch)) {
				retVal++;
			}
		}
		return retVal;
	}

	
	public int getHeaderRowIndex() {
		return headerNamesIndex;
	}

	
	public HashMap<String, Integer> getHeaders() {
		return headerNames;
	}

	
	public HashMap<Integer, String> getHeadersIndexed() {
		HashMap<Integer, String> indexedHeaders = new HashMap<>();
		for (String k : headerNames.keySet()) {
			indexedHeaders.put(headerNames.get(k), k);
		}
		return indexedHeaders;
	}

	
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

	
	public int getMaxLen(String headerName) {
		int ret = 0;
		List<String> valList = getValuesAsList(headerName);
		for (String s : valList) {
			if (s.length() > ret)
				ret = s.length();
		}
		return ret;
	}

	
	public HashMap<String, String> getMetaData() {
		return metaData;
	}

	
	public String[] getRow(int rowIndex) {
		return getRow(rowIndex, null, new Filter());
	}

	
	public String[] getRow(int rowIndex, String columnHeaders) {
		return getRow(rowIndex, columnHeaders, new Filter());
	}

	
	public String[] getRow(int rowIndex, String columnHeaders, Filter fltr) {
		String[] colHeaders = null;
		if (columnHeaders == null) {
			colHeaders = new String[0]; // TODO why not using getHeaders() here to get all if no specific column is passed ?
		} else {
			colHeaders = columnHeaders.split(";");
		}
		List<String[]> outLst = getRowsList(new int[] { rowIndex }, colHeaders, fltr);
		if (outLst.size() > 0) {
			return outLst.get(0);
		}
		return new String[] {};
	}

	
	public String[] getRow(int rowIndex, String[] columnHeaders) {
		return getRow(rowIndex, String.join(";", columnHeaders), new Filter());
	}

	
	public Map<String, String> getRowAsMap(int rowIndex) {
		Map<String, String> outRow = new HashMap<String, String>();
		String[] columnHeaders = getHeaderNamesIndexed();
		for (String header : columnHeaders) {
			outRow.put(header, this.getValue(header, rowIndex));
		}
		return outRow;
	}

	
	public int getRowCount() {
		return values.size();
	}

	
	public int[] getRowsIndexes(Filter filter) {
		StringBuilder indexBuffer = new StringBuilder();
		for (int i = 0; i < values.size(); i++) {
			try {
				if (checkValuesFilter(values.get(i), filter)) {
					if (indexBuffer.length() > 0) {
						indexBuffer.append(";");
					}
					indexBuffer.append(String.valueOf(i));
				}
			} catch (NoSuchHeaderException e) {
				MLogger.getInstance().log(Level.SEVERE, e, "getRowsIndexes");
				throw new RuntimeException(e);
			}
		}
		if (indexBuffer.length() > 0) {
			return Arrays.stream(indexBuffer.toString().split(";")).mapToInt(Integer::parseInt).toArray();
		} else {
			return new int[] {};
		}
	}

	
	public List<String[]> getRowsList() {
		return getRowsList(new int[0], new String[0], new Filter());
	}

	
	public List<String[]> getRowsList(Filter rowFilter) {
		return getRowsList(new int[0], new String[0], rowFilter);
	}

	
	public List<String[]> getRowsList(int[] rowIndexes, String[] columnHeaders, Filter rowFilter) {
		List<String[]> outValues = new ArrayList<String[]>();
		if (rowIndexes.length == 0) {
			rowIndexes = new int[values.size()];
			if (values.size() > 0) {
				int i = 0;
				do {
					rowIndexes[i] = i++;
				} while (i < values.size());
			}
		}
		for (int i = 0; i < rowIndexes.length; i++) {
			if (rowIndexes[i] >= values.size()) {
				MLogger.getInstance().log(Level.INFO, "Row-index " + rowIndexes[i] + " is out of range. Maximum number of rows in container is " + values.size(), this.getClass().getSimpleName(), this.getClass().getName(), "getRowsList");
				break;
			}
			String[] row = values.get(rowIndexes[i]);
			if (row.length > 0) {
				try {
					if (checkValuesFilter(row, rowFilter)) {
						if (columnHeaders.length == 0) {
							// if no headerNames are defined, return the complete row
							outValues.add(row);
						} else {
							// else return the values of all defined columns
							String[] rowArray = new String[columnHeaders.length];
							for (int j = 0; j < columnHeaders.length; j++) {
								int headerIndex = getHeaderIndex(columnHeaders[j]);
								if (headerIndex > -1) {
									rowArray[j] = row[headerIndex];
								}
							}
							outValues.add(rowArray);
						}
					}
				} catch (NoSuchHeaderException e) {
					MLogger.getInstance().log(Level.SEVERE, e, "getRowsList");
					throw new RuntimeException(e);
				}
			}
		}
		return outValues;
	}

	
	public List<String[]> getRowsList(String columnHeader, Filter rowFilter) {
		return getRowsList(new int[0], columnHeader.split(";"), rowFilter);
	}

	
	public String getValue(int headerIndex, int rowIndex) {
		return getValue(getHeaderName(headerIndex), rowIndex, new Filter());
	}

	
	public String getValue(String headerName) {
		return getValue(headerName, -1, new Filter());
	}

	
	public String getValue(String headerName, Filter fltr) {
		return getValue(headerName, -1, fltr);
	}

	
	public String getValue(String headerName, int rowIndex) {
		return getValue(headerName, rowIndex, new Filter());
	}

	
	public String getValue(String headerName, int rowIndex, Filter fltr) {
		int[] ri = null;
		if (rowIndex < 0) {
			ri = new int[0];
		} else {
			ri = new int[] { rowIndex };
		}
		List<String> outLst = this.getValuesAsList(headerName, ri, fltr);
		if (outLst.size() > 0) {
			return outLst.get(0);
		}
		return null;
	}

	
	public List<String[]> getValues() {
		return values;
	}

	
	public List<String> getValuesAsDistinctedList(String headerName) {
		List<String> values = getValuesAsList(headerName, new int[0], new Filter());
		Set<String> uniqueValues = new HashSet<>(values);
		return Arrays.asList(uniqueValues.toArray(new String[uniqueValues.size()]));
	}

	
	public List<Double> getValuesAsDoubleList(String headerName) {
		return this.getValuesAsDoubleList(headerName, new Filter());
	}

	
	public List<Double> getValuesAsDoubleList(String headerName, Filter rowFilter) {
		List<Double> doubleList = new ArrayList<Double>();
		List<String> valList = getValuesAsList(headerName, rowFilter);
		for (String val : valList) {
			if (!val.isEmpty()) {
				doubleList.add(Double.valueOf(val));
			}
		}
		return doubleList;
	}

	
	public List<Integer> getValuesAsIntList(String headerName) {
		return this.getValuesAsIntList(headerName, new Filter());
	}

	
	public List<Integer> getValuesAsIntList(String headerName, Filter rowFilter) {
		List<Integer> intList = new ArrayList<Integer>();
		List<String> valList = getValuesAsList(headerName, rowFilter);
		for (String val : valList) {
			intList.add(Integer.valueOf(val));
		}
		return intList;
	}

	
	public List<String> getValuesAsList(String headerName) {
		return getValuesAsList(headerName, new int[0], new Filter());
	}

	
	public List<String> getValuesAsList(String headerName, Filter rowFilter) {
		return getValuesAsList(headerName, new int[0], rowFilter);
	}

	
	public List<String> getValuesAsList(String headerName, int[] rowIndexes, Filter fltr) {
		List<String[]> colLst = getColumnsList(headerName.split(";"), rowIndexes, fltr);
		List<String> outList = new ArrayList<String>();
		if (colLst.size() > 0) {
			outList = Arrays.asList(colLst.get(0));
		}
		return outList;
	}

	
	public String getValuesAsString() {
		StringBuilder ret = new StringBuilder();
		for (String[] row : values) {
			ret.append(String.join(columnDelimiter, row));
			ret.append("\n");
		}
		return ret.toString();
	}

	
	public void mergeRows(int rowIndex, String[] newValues) {
		String[] valArr = this.getRow(rowIndex);
		for (int i = 0; i < newValues.length; i++) {
			if (newValues[i] != null) {
				if ((getValue(i, rowIndex) == null) || (getValue(i, rowIndex).isEmpty())) {
					valArr[i] = newValues[i];
				}
			}
		}
		values.set(rowIndex, valArr);
	}

	public void putMetaData(String headerName, String value) {
		metaData.put(headerName, value);
	}
	
	// SET

	public void setColumn(String headerName, List<String> columnValues) {
		setColumn(headerName, columnValues.toArray(new String[columnValues.size()]));
	}

	
	public void setColumn(String headerName, String[] columnValues) {
		String[] oldCol = new String[values.size()];
		int j = 0;
		int headerIndex = getHeaderIndex(headerName);
		Iterator<String[]> it = values.iterator();
		while (it.hasNext() && j < values.size()) {
			oldCol[j] = it.next()[headerIndex];
		}
		if (oldCol.length < columnValues.length) {
			for (int i = 0; i < this.getRowCount(); i++) {
				String[] newRow = values.listIterator(i).next();
				newRow[headerIndex] = columnValues[i];
				this.setRow(i, newRow);
			}
			for (int i = this.getRowCount(); i < columnValues.length; i++) {
				String[] newRow = new String[this.getColumnCount()];
				newRow[headerIndex] = columnValues[i];
				this.addRow(newRow);
			}
		} else {
			if (oldCol.length > columnValues.length)
				columnValues = Arrays.copyOf(columnValues, oldCol.length);
			for (int i = 0; i < this.getRowCount(); i++) {
				String[] newRow = values.listIterator(i).next();
				newRow[headerIndex] = columnValues[i];
				this.setRow(i, newRow);
			}
		}
	}

	
	public void setColumnDelimiter(String cDel) {
		columnDelimiter = cDel;
	}

	/**
	 * Sub method that finally edits the values object e.g. when a setValue call occurs.
	 *
	 * @param headerName Name of the sequence header.
	 * @param index      Index of the field within the DataSet.
	 * @param val      Value as String that will be set to the field.
	 */
	private void setFieldValue(String headerName, int index, String val) {
		if (!getHeaders().containsKey(headerName)) {
			setHeaders(new String[] { headerName });
			for (int i = 0; i < values.size(); i++) {
				values.set(i, Arrays.copyOf(values.get(i), values.get(i).length + 1));
			}
		}
		String[] valArr = null;
		if (values.size() > 0) {
			valArr = getRow(index);
		}
		if (valArr == null) {
			valArr = new String[getHeaders().size()];
		}
		valArr[getHeaderIndex(headerName)] = val;
		if (values.size() == 0) {
			values.add(index, valArr);
		} else {
			values.set(index, valArr);
		}
	}

	
	public void setHeaderRowIndex(int headerIndex) {
		headerNamesIndex = headerIndex;
	}

	
	public void setHeaders(List<String> in_headers) {
		setHeaders(in_headers.toArray(new String[in_headers.size()]), false);
	}

	
	public void setHeaders(String[] in_headers) {
		setHeaders(in_headers, false);
	}

	
	public void setHeaders(String[] in_headers, boolean overwrite) {
		if(overwrite) {
			headerNames.clear();
		}
		int actualPos = headerNames.size(); // Last index of the old headers
		for (String header : addMetaHeaders(in_headers)) {
			if (!headerNames.containsKey(header)) {
				headerNames.put(header, actualPos);
			} else {
				String headers_tmp = header; // To not change the origin header
				int count = 2;
				// Increase the index suffix until it is unique
				while (headerNames.containsKey(headers_tmp)) {
					headers_tmp = header + "_" + count;
					count++;
				}
				headerNames.put(headers_tmp, actualPos);
			}
			actualPos++;
		}
	}

	
	public void setMetaData(String headerName, String value) {
		int i = 0;
		if (metaData.containsValue(headerName)) {
			metaData.replace(headerName, value);
		} else {
			metaData.put(headerName, value);

			// Add a new array element to each existing array in ArrayList values with an empty string as value
			for (String[] element : values) {
				ArrayList<String> valList = new ArrayList<String>(Arrays.asList(element));
				valList.add("");
				values.set(i, valList.toArray(new String[valList.size()]));
				i++;
			}
		}
	}

	
	public void setRow(int rowIndex, String[] rowValues) {
		values.set(rowIndex, addMetaValues(rowValues));
	}

	
	public void setValue(String headerName, String value) {
		setValue(headerName, 0, value, new Filter());
	}

	
	public void setValue(String headerName, int index, String value) {
		setValue(headerName, index, value, new Filter());
	}

	
	public void setValue(String headerName, String value, Filter fltr) {
		setValue(headerName, 0, value, fltr);
	}

	
	public void setValue(String headerName, int index, String value, Filter fltr) {
		setValues(headerName, new int[] { index }, value, fltr);
	}

	
	public void setValues(String headerName, int[] indexes, String value, Filter fltr) {
		if (values.isEmpty()) {
			setFieldValue(headerName, 0, value);
		} else {
			List<Integer> occ = new ArrayList<>();
			int[] rowIDs = getRowsIndexes(fltr);
			if (indexes.length != 0 && indexes[0] == -1) {
				occ = Arrays.stream(rowIDs).boxed().collect(Collectors.toList());
			} else {
				occ = Arrays.stream(indexes).boxed().collect(Collectors.toList());
			}
			for (int i = 0; i < rowIDs.length; i++) {
				if ((occ.size() > 0) && (!occ.contains(i))) {
					continue;
				} else {
					setFieldValue(headerName, rowIDs[i], value);
				}
			}
		}
		// TODO why does setValues have to write to file ?
//		if (!dc.getInputFile().getName().isEmpty()) {
//			try {
//				writeData(dc.getInputFile().getPath());
//			} catch (IOException e) {
//				MLogger.getInstance().log(Level.SEVERE, e);
//			}
//		}
	}

	
	public void setValues(String headerName, String value, Filter fltr) {
		setValues(headerName, new int[0], value, fltr);
	}

	
	public void setValues(String headerName, String value, Filter fltr, Boolean allOccurences) {
		int indexes = 0;
		if (allOccurences) {
			indexes = -1;
		}
		setValues(headerName, new int[] { indexes }, value, fltr);
	}

	/**
	 * This method creates a HashMap, mapping the indexes of the passed header-array to the correct ones
	 * from the underlying DataContainer. New headers will be mapped to the index -1.
	 *
	 * @param sortableHeaders String array with headers of the new DataContainer in a wrong order.
	 *
	 * @return HashMap<Integer, Integer> with index in the original DataContainer mapped to index from
	 *         the passed String array.
	 *
	 */
	private HashMap<Integer, Integer> sortHeadersIndexes(String[] sortableHeaders) {
		HashMap<Integer, Integer> ret = new HashMap<>();
		for (int i = 0; i < sortableHeaders.length; i++) {
			ret.put(getHeaderIndex(sortableHeaders[i]), i);
		}
		return ret;
	}

	/**
	 * This method uses the {@link #sortHeadersIndexes(String[])} created HashMap to bring an array
	 * of String values to the right order of the current DataContainer's headers.
	 *
	 * @param sortMap  HashMap created by {@link #sortHeadersIndexes(String[])} with the corresponding
	 *                 header array.
	 * @param sortable String array with the String values to sort. It's header array must have been
	 *                 used to create the sortMap with {@link #sortHeadersIndexes(String[])}.
	 *
	 * @return String[] with values in the right order corresponding to the current DataContainer's
	 *         headers.
	 *
	 */
	private String[] sortValues(HashMap<Integer, Integer> sortMap, String[] sortable) {
		String[] ret = new String[sortable.length];
		for (int i = 0; i < sortable.length; i++) {
			ret[i] = sortable[sortMap.get(i)];
		}
		return ret;
	}

}