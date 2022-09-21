package org.opentdk.api.datastorage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;

import org.opentdk.api.datastorage.BaseContainer.EContainerFormat;
import org.opentdk.api.io.FileUtil;
import org.opentdk.api.io.XFileWriter;
import org.opentdk.api.logger.MLogger;

/**
 * Sub class of {@link DataContainer} which provides all methods for reading and writing from or to
 * ASCII files that are column separated, and store the data at runtime within the DataContainer.
 * 
 * @author LK Test Solutions
 * @see org.opentdk.api.datastorage.DataContainer
 *
 */
public class CSVDataContainer implements CustomContainer {

	/**
	 * An instance of the DataContainer that should be filled with the data from the connected source
	 * file. Task of the specific data containers.
	 */
	private final DataContainer dc;

	/**
	 * Construct a new specific <code>DataContainer</code> for CSV files.
	 *
	 * @param dCont the <code>DataContainer</code> instance to use it in the read and write methods of
	 *              this specific data container
	 */
	CSVDataContainer(DataContainer dCont) {
		dc = dCont;
		dc.containerFormat = EContainerFormat.CSV;
	}

	/**
	 * Empties a field (set value to null) within the {@link BaseContainer#values} ArrayList.<br>
	 * Limitation: Implemented for dispatcher of CSV format. Only fields in the first row can be
	 * deleted!
	 * 
	 * @param headerName     Name of the header that defines the column of the field
	 * @param attributeName  Not used by this container. Defined by the interface class
	 *                       {@link CustomContainer}
	 * @param attributeValue Not used by this container. Defined by the interface class
	 *                       {@link CustomContainer}
	 * @param fltr           Not used by this container. Defined by the interface class
	 *                       {@link CustomContainer}
	 */
	@Override
	public void deleteField(String headerName, String attributeName, String attributeValue, Filter fltr) {
		int headerIndex = dc.getHeaderIndex(headerName);
		dc.values.get(0)[headerIndex] = null;
		if (!dc.getFileName().isEmpty()) {
			writeData(dc.getFileName());
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
			String[] valArray = dc.cleanValues(row.split(columnDelimiter));
			int firstValueIndex = 0;
			for (int i = 0; i < valArray.length; i++) {
				// if withHeaders = true, add values in the first column to the headers HashMap,
				// and start values at index 1
				if (i == 0) {
					if (!dc.getHeaders().containsValue(rowIndex)) {
						dc.getHeaders().put(valArray[0], rowIndex);
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
			if ((!dc.values.isEmpty()) && (rowIndex <= dc.values.size() - 1)) {
				currentValues = Arrays.copyOf(dc.values.get(rowIndex), dc.values.get(rowIndex).length);
			}
			String[] newValues = tmpRowsList.get(i).toArray(new String[tmpRowsList.get(i).size()]);
			List<String> both = new ArrayList<>(currentValues.length + newValues.length);
			Collections.addAll(both, currentValues);
			Collections.addAll(both, newValues);
			if (rowIndex <= dc.values.size() - 1) {
				dc.setRow(rowIndex, both.toArray(new String[both.size()]));
			} else {
				dc.addRow(both.toArray(new String[both.size()]));
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

			// 20220817 HWA: disabled cleanValues, because stepInterpreter for Finanz Informatik cannot handle
			// null fields
			// and removed quotes.
			// TODO The concept of cleanValues needs to be reviewed! It may be more useful to store the values
			// unchanged in the
			// DataContainer and let the consumer handle modifications.
			String[] valArray = row.split(columnDelimiter, -1);

//			String[] valArray = dc.cleanValues(row.split(columnDelimiter, -1));
			if (rowIndex < dc.getHeaderRowIndex()) {
				MLogger.getInstance().log(Level.INFO, "Skipping row with index " + rowIndex + "! Just rows after the headerRowIndex will be loaded into DataContainer.", this.getClass().getSimpleName(), "putDataSetRows");
			} else if (rowIndex == dc.getHeaderRowIndex()) {
				if (dc.getHeaders().isEmpty()) {
					dc.setHeaders(valArray);
				}
				headerState = dc.checkHeader(valArray);
				if (headerState < 0) {
					for (String h : valArray) {
						if (!dc.getHeaders().containsKey(h)) {
							dc.addColumn(h);
						}
					}
				}
				if (headerState != 0)
					sortMap = sortHeadersIndexes(valArray);
			} else {
				if (dc.addMetaValues(valArray).length == dc.getHeaders().size()) {
					if (headerState == 0) {
						dc.addRow(valArray);
					} else {
						dc.addRow(sortValues(sortMap, valArray));
					}
				} else {
					MLogger.getInstance().log(Level.WARNING, "The number of values doesn't match to the number of headers! Values will not be added to DataContainer.", this.getClass().getSimpleName(), "putDataSetRows");
				}
			}
		}
	}

	/**
	 * This method is used to put data from a CSV file to the DataContainer. Depending on the header
	 * orientation, the correct underlying method will be called.
	 *
	 * @param fileName        The filename of the file to read from
	 * @param srcHeader       The source Header depending on the orientation of the data in the CSV file
	 * @param columnDelimiter The column delimiter used to separate columns in the CSV file
	 * 
	 */
	private void putFile(String fileName, String columnDelimiter) {
		switch (dc.getContainerFormat().getHeaderType()) {
		case COLUMN:
			putDatasetRows(fileName, columnDelimiter);
			break;
		case ROW:
			putDatasetColumns(fileName, columnDelimiter);
			break;
		default:
			MLogger.getInstance().log(Level.WARNING, "Source header '" + dc.containerFormat.getHeaderType().toString() + "' is not valid for this method. Please set the property sourceHeader with a valid option of EHeader.COLUMN or EHeader.ROW.", this.getClass().getSimpleName(), "putFile");
			break;
		}
	}

	/**
	 * This method is used to read data from a CSV-file to the data container. If the DataContainer is
	 * empty, new headers will be created and the values-list will be filled. Otherwise, data from
	 * existing headers will be appended. In any case, new headers will be added to the DataContainer by
	 * setting all previous row values of the new header to null.
	 *
	 * @param filter Filter parameter to accomplish interface requirement (has no use here)
	 */
	@Override
	public void readData(Filter filter) {
		putFile(dc.fileName, dc.columnDelimiter);
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
			ret.put(dc.getHeaderIndex(sortableHeaders[i]), i);
		}
		return ret;
	}

	/**
	 * This method uses the from {@link #sortHeadersIndexes(String[])} created HashMap to bring an array
	 * of String values to the right order of the current DataContainer's headers.
	 *
	 * @param sortMap  HashMap created by {@link #sortHeadersIndexes(String[])} with the corresponding
	 *                 header array.
	 * @param sortable String array with the String values to sort. It's header array must have been
	 *                 used to created the sortMap with {@link #sortHeadersIndexes(String[])}.
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

	/**
	 * This method writes data to a existing or newly created CSV-file when called.
	 *
	 * @param fileName The filename of the file to write to or to create.
	 * 
	 */
	@Override
	public void writeData(String fileName) {
		List<String[]> writeable = new ArrayList<>();
		if (dc.getContainerFormat().getHeaderType().equals(BaseContainer.EHeader.COLUMN)) {
			// for Column Header oriented output
			writeable.add(dc.getHeaderNamesIndexed());
			for (String[] dataArr : dc.getRowsList()) {
				writeable.add(dataArr);
			}
		} else if (dc.getContainerFormat().getHeaderType().equals(BaseContainer.EHeader.ROW)) {
			// for Row Header oriented output
			HashMap<Integer, String> hm = dc.getHeadersIndexed();
			List<String[]> colList;
			colList = dc.getColumnsList();
			for (int i = 0; i < colList.size(); i++) {
				List<String> row = new ArrayList<>();
				row.add(hm.get(i));
				row.addAll(Arrays.asList(colList.get(i)));
				writeable.add(row.toArray(new String[row.size()]));
			}
		}
		// Write content of List writeable to output-file
		try {
			File f = new File(fileName);
			FileUtil.checkDir(f.getParent(), true);
			XFileWriter writer = new XFileWriter(f);
			writer.writeLines(writeable);
			writer.close();
		} catch (IOException e) {
			MLogger.getInstance().log(Level.SEVERE, e, "writeData");
			throw new RuntimeException(e);
		}
	}

	@Override
	public String asString() {
		return dc.getValuesAsString();
	}
}
