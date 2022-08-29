package org.opentdk.api.datastorage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.stream.Collectors;

import org.json.JSONObject;
import org.opentdk.api.io.FileUtil;
import org.opentdk.api.io.XFileWriter;
import org.opentdk.api.io.XMLEditor;
import org.opentdk.api.logger.*;

/**
 * Class used to store data from different sources at runtime of an application
 * in an <code>ArrayList</code>. The source data needs to be in a structured
 * format, and keys can be assigned to the coordinates of the fields. In
 * general, the keys are column- or row-headers, that will be assigned to a
 * column- or row-index within the <code>ArrayList</code>. <br>
 * The class provides methods for putting data from different sources into an
 * instance of DataContainer, as well as getter and setter methods, for reading
 * data from, and writing data into the object.<br>
 * This will allow to build applications with standardized data interface with
 * minimum maintenance effort in case that the data source changes.
 *
 * @author LK Test Solutions GmbH
 */
public class DataContainer extends BaseContainer {

	/**
	 * The filter object that does not filter the data in the
	 * <code>DataContainer</code> during runtime but when reading from and writing
	 * to the configuration file.
	 */
	private Filter filter;

	/**
	 * The object that will be adapted to a specific <code>DataContainer</code> like
	 * {@link XMLDataContainer} or {@link PropertiesDataContainer}. The caller of
	 * the <code>DataContainer</code> does not need to know about the adaption
	 * process. In other words: The caller can use the <code>DataContainer</code> on
	 * all supported file formats without calling specific functions for the source
	 * file type.
	 */
	private CustomContainer instance;

	/**
	 * Default constructor for the specific <code>DataContainer</code>.
	 */
	public DataContainer() {
		columnDelimiter = "";
		fileName = "";
		containerFormat = EContainerFormat.DEFAULT;
		adaptContainer();
	}

	/**
	 * Constructor that is called, when creating a new instance by just committing
	 * the data container column headers. Used when the data gets loaded into the
	 * container afterwards, but the structure is known. The semicolon is used as
	 * column separator.
	 * 
	 * @param headers The column headers as array of type string.
	 */
	public DataContainer(String[] headers) {
		this("", ";", -1, headers, new Filter());
	}

	/**
	 * Constructor that is called, when creating a new instance with an ASCII File
	 * of structured data.
	 *
	 * @param srcFile Full path and name of the source file, from which the data
	 *                will be loaded into DataContainer
	 */
	public DataContainer(String srcFile) {
		this(srcFile, ";", 0, new String[0], new Filter());
	}

	/**
	 * Constructor that is called with the source file to store data from and a
	 * filter to decide if part of the data should be ignored. The file string must
	 * be valid, the filter may be null.
	 *
	 * @param srcFile Full path and name of the source file, from which the data
	 *                will be loaded into DataContainer
	 * @param fltr    The {@link #filter} object to decide which data should be
	 *                ignored.
	 */
	public DataContainer(String srcFile, Filter fltr) {
		this(srcFile, ";", 0, new String[0], fltr);
	}

	/**
	 * Constructor that is called when the source file, the column delimiter and the
	 * filter is known.
	 * 
	 * @param srcFile   Full path and name of the source file, from which the data
	 *                  will be loaded into DataContainer
	 * @param delimiter The delimiter to separate the stored data.
	 * @param fltr      The {@link #filter} object to decide which data should be
	 *                  ignored.
	 */
	public DataContainer(String srcFile, String delimiter, Filter fltr) {
		this(srcFile, delimiter, 0, new String[0], fltr);
	}

	/**
	 * Constructor that is called when the source file, the column delimiter and the
	 * filter is known.
	 * 
	 * @param srcFile   Full path and name of the source file, from which the data
	 *                  will be loaded into DataContainer
	 * @param delimiter The delimiter to separate the stored data.
	 */
	public DataContainer(String srcFile, String delimiter) {
		this(srcFile, delimiter, 0, new String[0], new Filter());
	}

	/**
	 * Constructor that is called when the source file, the column delimiter, the
	 * header index and the filter is known.
	 * 
	 * @param srcFile     Full path and name of the source file, from which the data
	 *                    will be loaded into DataContainer
	 * @param delimiter   The delimiter to separate the stored data.
	 * @param headerIndex TODO description
	 * @param fltr        A {@link org.opentdk.api.datastorage.Filter} object to
	 *                    define which data should be ignored. If it is null no
	 *                    filter gets used.
	 */
	public DataContainer(String srcFile, String delimiter, int headerIndex, Filter fltr) {
		this(srcFile, delimiter, headerIndex, new String[0], fltr);
	}

	/**
	 * Constructor that is called when creating a new instance with column separated
	 * file.
	 *
	 * @param srcFile     Full path and name of the source file, from which the data
	 *                    will be loaded into DataContainer
	 * @param delimiter   Character(s) that are used as delimiter within the file
	 * @param headerIndex TODO description
	 */
	public DataContainer(String srcFile, String delimiter, int headerIndex) {
		this(srcFile, delimiter, headerIndex, new String[0], new Filter());
	}

	/**
	 * Constructor that is called when the source file, the data delimiter and the
	 * column header are known.
	 * 
	 * @param srcFile   Full path and name of the source file, from which the data
	 *                  will be loaded into DataContainer
	 * @param delimiter Character(s) that are used as delimiter within the file
	 * @param headers   The column header as string array.
	 */
	public DataContainer(String srcFile, String delimiter, String[] headers) {
		this(srcFile, delimiter, -1, headers, new Filter());
	}

	/**
	 * Constructor that is called when the source file, the data delimiter and the
	 * header orientation are known.
	 *
	 * @param srcFile   Full path and name of the source file, from which the data
	 *                  will be loaded into DataContainer
	 * @param delimiter Character(s) that are used as delimiter within the file
	 * @param srcHeader The header orientation (column or row header)
	 */
	public DataContainer(String srcFile, String delimiter, EHeader srcHeader) {
		this(srcFile, delimiter, 0, new String[0], new Filter());
	}

	/**
	 * Constructor that is called when the source file, the data delimiter, the
	 * header orientation, the header index, the header names and the filter are
	 * known.
	 * 
	 * @param srcFile     Full path and name of the source file, from which the data
	 *                    will be loaded into DataContainer
	 * @param delimiter   Character(s) that are used as delimiter within the file
	 * @param headerIndex TODO description
	 * @param headers     The column header as string array.
	 * @param fltr        A {@link org.opentdk.api.datastorage.Filter} object to
	 *                    define which data should be ignored. If it is null no
	 *                    filter gets used.
	 */
	public DataContainer(String srcFile, String delimiter, int headerIndex, String[] headers, Filter fltr) {
		fileName = srcFile;
		columnDelimiter = delimiter;
		headerNamesIndex = headerIndex;
		filter = fltr;
		containerFormat = EContainerFormat.DEFAULT;
		if (headers.length > 0) {
			setHeaders(headers);
		}
		adaptContainer();
	}

	/**
	 * Constructor that is called when no input file exists and the container for
	 * the specific data format also accepts {@link java.io.InputStream} for the
	 * data.<br>
	 * e.g.: An application dynamically receives data by calling RestAPI Service
	 * Requests and the XML responses will be passed to the
	 * {@link org.opentdk.api.dispatcher.BaseDispatcher} that creates an
	 * {@link XMLDataContainer} for parsing the XML response.<br>
	 * <br>
	 * 
	 * <pre>
	 * InputStream stream = new ByteArrayInputStream(QCDesignStep_Response.getBytes(StandardCharsets.UTF_8));
	 * EQCStepField.setDataContainer(EQCStepField.class, stream);
	 * String[] stepNames = EDesignSteps.STEP_NAME.getValues();
	 * String[] stepDescriptions = EDesignSteps.STEP_DESCRIPTION.getValues();
	 * String[] stepExpected = EDesignSteps.STEP_EXPECTED.getValues();
	 * </pre>
	 * 
	 * @param inStream Object of type {@link java.io.InputStream}
	 */
	public DataContainer(InputStream inStream) {
		filter = new Filter();
		inputStream = inStream;
		containerFormat = EContainerFormat.DEFAULT;
		adaptContainer();
	}
	
//	public DataContainer(EContainerFormat containerFormat) {
//		String input = "";
//		InputStream stream = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
//	}

	/**
	 * Constructor that is called, when importing data from a database request to
	 * the container. No filter will be used and the column delimiter for the
	 * imported data is the semicolon.
	 * 
	 * @param rs The result of the database request.
	 */
	public DataContainer(ResultSet rs) {
		this(rs, null);
	}

	/**
	 * Constructor that is called, when importing data from a database request to
	 * the container with the possibility to filter the data before it gets
	 * imported. The column delimiter of the imported data is the semicolon.
	 * 
	 * @param rs   The result of the database request.
	 * @param fltr A {@link org.opentdk.api.datastorage.Filter} object to define
	 *             which data should be ignored. If it is null no filter gets used.
	 */
	public DataContainer(ResultSet rs, Filter fltr) {
		this(rs, fltr, ";");
	}

	/**
	 * Constructor that is called, when importing data from a database request to
	 * the container with the possibility to filter the data before it gets imported
	 * and define a column delimiter for the imported data.
	 * 
	 * @param rs           The result of the database request.
	 * @param fltr         A {@link org.opentdk.api.datastorage.Filter} object to
	 *                     define which data should be ignored. If it is null no
	 *                     filter gets used.
	 * @param colDelimiter Possibility to define a column delimiter for the imported
	 *                     table data like semicolon.
	 */
	public DataContainer(ResultSet rs, Filter fltr, String colDelimiter) {
		resultSet = rs;
		filter = fltr;
		columnDelimiter = colDelimiter;
		adaptContainer();
	}

	/**
	 * Constructor that is called, when a DataContainer shall be copied. The copy
	 * holds only the columns with the headers specified in headerIndices.
	 * 
	 * @param dc            Original DataContainer from which the columns shall be
	 *                      copied.
	 * @param headerIndices Headers which shall be copied to the new DataContainer
	 * 
	 */
	public DataContainer(DataContainer dc, int[] headerIndices) {
		instance = dc.instance.getClass().cast(dc.instance);
		fileName = dc.getFileName();
		columnDelimiter = dc.getColumnDelimiter();
		headerNamesIndex = dc.getHeaderRowIndex();
		filter = dc.filter;
		String[] headerNames = new String[headerIndices.length];
		for (int i = 0; i < headerIndices.length; i++) {
			headerNames[i] = dc.getHeaderName(headerIndices[i]);
		}
		setHeaders(headerNames);
		for (int i = 0; i < headerIndices.length; i++) {
			setColumn(headerNames[i], dc.getColumn(headerNames[i]));
		}
	}

	/**
	 * This method adapts the one {@link CustomContainer} instance to a specific
	 * <code>DataContainer</code> depending on the source file ending. The needed
	 * objects like header or filter will be initialized with default values if they
	 * were not explicitly set or the information could not be read from the source
	 * file.
	 * 
	 * @throws FileNotFoundException If the file name is null or empty or the file
	 *                               does not exist.
	 */
	private final void adaptContainer() {
		if (filter == null) {
			filter = new Filter();
		}
		switch (detectDataFormat()) {
		case CSV:
			instance = new CSVDataContainer(this);
			break;
		case PROPERTIES:
			instance = new PropertiesDataContainer(this);
			break;
		case RESULTSET:
			instance = new RSDataContainer(this);
			break;
		case XML:
			instance = new XMLDataContainer(this);
			values = new ArrayList<String[]>() {
				private static final long serialVersionUID = 1L;

				@Override
				public String[] get(int index) {
					return ((XMLDataContainer) instance).getDecoded(index);
				}
			};
			break;
		case JSON:
			instance = new JSONDataContainer(this);
			break;
		default:
			instance = new CSVDataContainer(this);
			return;
		}
		instance.readData(filter);
	}
	
	/**
	 * This method is designed for tabular data formats to add column names to
	 * existing <code>DataContainer</code>. In case the column name already exists,
	 * an suffix with the next available index will be appended to the column name.
	 * 
	 * @param col Name of the column that will be added to the
	 *            {@link BaseContainer#headerNames}
	 */
	public void addColumn(String col) {
		addColumn(col, false);
	}

	/**
	 * This method is designed for tabular data formats to add column names to
	 * existing <code>DataContainer</code>. The useExisting argument specifies the
	 * behavior for existing columns with the same name. Either use the existing
	 * column or create a new column by appending a unique index to the name.
	 * 
	 * @param col         Name of the column that will be added to the
	 *                    {@link BaseContainer#headerNames}
	 * @param useExisting Boolean value - true = if column name exist, then use the
	 *                    existing column; false = if column name exists, then add
	 *                    column name with an index suffix
	 */
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
	 * This method is designed for data formats, where single fields like tree nodes
	 * or XML tags can be addressed, instead of columns or rows with multiple
	 * values. The field will be added with name and value into parent node, which
	 * path needs to be defined by filter conditions.<br>
	 * Usage sample with {@link XMLDataContainer#addField(String, String, Filter)}:
	 * 
	 * <pre>
	 * Filter fltr = new Filter();
	 * fltr.addFilterRule("XPath", "/parserRules/rule[@name='Orbit']/element[@name='SELECT']", EOperator.EQUALS);
	 * return dataContainer.addField("StartsWith", "select[\s]*", fltr);
	 * </pre>
	 * 
	 * @param headerName Name of the field or node that will be added.
	 * @param value      Value that will be assigned to the field or node.
	 * @param fltr       The filter object to insert the new node at the right
	 *                   position as demonstrated above.
	 */
	public void addField(String headerName, String value, Filter fltr) {
		switch (getContainerFormat().getHeaderType()) {
		case TREE:
			instance.addField(headerName, value, fltr);
			break;
		default:
			MLogger.getInstance().log(Level.WARNING, "The method is only supported for tree formats!", getClass().getSimpleName(), getClass().getName(), "addField");
			break;
		}
	}

	/**
	 * This method is designed for data formats, where single fields like tree nodes
	 * or XML tags can be addressed, instead of columns or rows with multiple
	 * values. The field will be added with name and an attribute into parent node,
	 * which path needs to be defined by filter conditions.<br>
	 * Usage sample with
	 * {@link XMLDataContainer#addField(String, String, String, Filter)}:
	 * 
	 * <pre>
	 * Filter fltr = new Filter();
	 * fltr.addFilterRule("XPath", "/parserRules/rule[@name='Orbit']", EOperator.EQUALS);
	 * return dataContainer.addField("element", "name", "INSERT", fltr);
	 * </pre>
	 * 
	 * @param headerName     Name of the field or node that will be added.
	 * @param attributeName  Name of the attribute that will be assigned to the
	 *                       field or node.
	 * @param attributeValue Value that will be assigned to the attribute of the
	 *                       field or node.
	 * @param fltr           The filter object to insert the new node at the right
	 *                       position as demonstrated above.
	 */
	public void addField(String headerName, String attributeName, String attributeValue, Filter fltr) {
		switch (getContainerFormat().getHeaderType()) {
		case TREE:
			instance.addField(headerName, attributeName, attributeValue, fltr);
			break;
		default:
			MLogger.getInstance().log(Level.WARNING, "The method is only supported for tree formats!", getClass().getSimpleName(), getClass().getName(), "addField");
			break;
		}
	}

	/**
	 * This method is designed for data formats, where single fields like tree nodes
	 * or XML tags can be addressed, instead of columns or rows with multiple
	 * values. The method is reserved to add or overwrite elements within the data
	 * source. These elements can be e.g. nodes, tags or attributes of existing
	 * nodes or tags, depending on the implementation for the specific
	 * <code>Data Container</code>.<br>
	 * <br>
	 * 
	 * Usage sample with
	 * {@link XMLDataContainer#addField(String, String, String, String, Filter)}:
	 * 
	 * <pre>
	 * Filter fltr = new Filter();
	 * fltr.addFilterRule("XPath", "/parserRules/rule[@name='Orbit']", EOperator.EQUALS);
	 * return dataContainer.addField("element", "name", "INSERT", "INSERT INTO", fltr);
	 * </pre>
	 * 
	 * @param headerName     Name of the field or node that will be added.
	 * @param attributeName  Name of the attribute that will be assigned to the
	 *                       field or node.
	 * @param oldAttrValue   The attribute value that will be replaced, in case it
	 *                       exists.
	 * @param attributeValue Value that will be assigned to the attribute of the
	 *                       field or node.
	 * @param fltr           The filter object to insert the new node at the right
	 *                       position as demonstrated above.
	 */
	public void addField(String headerName, String attributeName, String oldAttrValue, String attributeValue, Filter fltr) {
		switch (getContainerFormat().getHeaderType()) {
		case TREE:
			instance.addField(headerName, attributeName, oldAttrValue, attributeValue, fltr);
			break;
		default:
			MLogger.getInstance().log(Level.WARNING, "The method is only supported for tree formats!", getClass().getSimpleName(), getClass().getName(), "addField");
			break;
		}
	}

	/**
	 * Adds an empty row to the values ArrayList that is used for tabular data
	 * formats.
	 */
	public void addRow() {
		int rowSize = values.size() - getMetaData().size();
		addRow(new String[rowSize]);
	}

	/**
	 * Adds a string array with the row content into the
	 * {@link BaseContainer#values} ArrayList.
	 *
	 * @param row String array with the content of the row to be added
	 */
	public void addRow(String[] row) {
		if ((filter != null) && (!filter.getFilterRules().isEmpty())) {
			try {
				if (!checkValuesFilter(row, filter)) {
					return;
				}
			} catch (NoSuchHeaderException e) {
				MLogger.getInstance().log(Level.SEVERE, e);
				throw new RuntimeException(e);
			}
		}
		values.add(addMetaValues(row));
	}

	/**
	 * Inserts a string array with the row content into the
	 * {@link BaseContainer#values} ArrayList at a specified position in this list.
	 * Shifts the row currently at that position (if any) and any subsequent row.
	 * 
	 * @param rowIndex  index at which the specified row is to be inserted
	 * @param rowValues String array with all row values to be inserted
	 */
	public void addRow(int rowIndex, String[] rowValues) {
		values.add(rowIndex, addMetaValues(rowValues));
	}

	/**
	 * Adds one or more string arrays with the row content into the
	 * {@link BaseContainer#values} ArrayList.
	 * 
	 * @param rows List of string arrays with the content of the rows to be added
	 */
	public void addRows(List<String[]> rows) {
		for (String[] row : rows) {
			addRow(row);
		}
	}

	/**
	 * This method reads data from a specified source file and appends it to the
	 * existing data within the container.
	 * 
	 * @param fileName        Full path and name of the source file, from which the
	 *                        data will be added to the <code>DataContainer</code>
	 * @param columnDelimiter The column separator to structure the data.
	 * @throws FileNotFoundException If the committed file does not exist.
	 */
	public void appendData(String fileName, String columnDelimiter) throws FileNotFoundException {
		setFileName(fileName);
		setColumnDelimiter(columnDelimiter);

		if (filter == null) {
			filter = new Filter();
		}
		if (instance == null) {
			adaptContainer();
		}

		instance.readData(filter);
	}

	/**
	 * This method reads data from a specified source file and appends it to the
	 * existing data within the container.
	 * 
	 * @param fileName Full path and name of the source file, from which the data
	 *                 will be added to the <code>DataContainer</code>
	 * @throws FileNotFoundException If the committed file does not exist.
	 */
	public void appendData(String fileName) throws FileNotFoundException {
		if (getColumnDelimiter() == null) {
			setColumnDelimiter(";");
		}
		appendData(fileName, getColumnDelimiter());
	}

	/**
	 * This method checks if the headers-of the assigned <code>DataContainer</code>
	 * match with the headers of the current instance and appends all data of the
	 * assigned <code>DataContainer</code> to the values List of the DataContaner
	 * instance which is calling the method, in case the headers of both
	 * <code>DataContainer</code> are the same.
	 *
	 * @param dc The DataContanier which content will be appended to the current
	 *           DataContainer instance.
	 */
	public void appendDataContainer(DataContainer dc) {
		if (checkHeader(dc.getHeaders()) == 0) {
			values.addAll(dc.values);
		} else if (checkHeader(dc.getHeaders()) == 1) {
			int i = 0;
			while (i < dc.getRowCount()) {
				String[] row = new String[getColumnCount()];
				for (int j = 0; j < getColumnCount(); j++) {
					try {
						row[j] = dc.getValuesAsList(getHeadersIndexed().get(j)).get(i);
					} catch (RuntimeException e) {
						row[j] = null;
					}
				}
				i++;
				values.add(row);
			}
		} else {
			MLogger.getInstance().log(Level.WARNING, "Headers of appending DataContainer don't match to the headers of the current instance. DataContainer will not be appended!",
					getClass().getSimpleName(), getClass().getName(), "appendDataContainer");
		}
	}

	/**
	 * This method is designed for data formats, where single fields like tree nodes
	 * or XML tags can be addressed, instead of columns or rows with multiple
	 * values. The field matching the defined arguments will be deleted from the
	 * data source.<br>
	 * Usage sample with
	 * {@link XMLDataContainer#deleteField(String, String, String, Filter)}
	 * 
	 * <pre>
	 * Filter fltr = new Filter();
	 * fltr.addFilterRule("XPath", "/parserRules/rule[@name='Orbit']", EOperator.EQUALS);
	 * dataContainer.deleteField("element", "name", "INSERT", fltr);
	 * </pre>
	 * 
	 * @param headerName     Name of the field or node that will be deleted.
	 * @param attributeName  Name of the attribute that is part of the field or node
	 *                       description.
	 * @param attributeValue Value of the attribute that is part of the field or
	 *                       node description.
	 * @param fltr           The filter object to find the field or node at the
	 *                       right position as demonstrated above.
	 */
	public void deleteField(String headerName, String attributeName, String attributeValue, Filter fltr) {

		switch (containerFormat) {
		case XML:
		case PROPERTIES:
		case CSV:
		case DEFAULT:
			instance.deleteField(headerName, attributeName, attributeValue, fltr);
			break;
		default:
			MLogger.getInstance().log(Level.WARNING, "The method is only supported for DataContainer format " + containerFormat.toString() + "!", getClass().getSimpleName(), getClass().getName(),
					"deleteField");
			break;
		}
	}

	/**
	 * Analyzes the file extension and/or structure of the data content and an enum
	 * of type {@link EContainerFormat} which defines the type of DataContaner that
	 * needs to be adapted.
	 * 
	 * @return enum of type {@link EContainerFormat}
	 */
	private final EContainerFormat detectDataFormat() {
		if (resultSet != null) {
			return EContainerFormat.RESULTSET;
		} else if (inputStream != null) {
			try {
				if (inputStream.available() > 0) {
					if (XMLEditor.validateXMLString(inputStream)) {
						return EContainerFormat.XML;
					} else if(FileUtil.getContent(inputStream).startsWith("{")) {
						return EContainerFormat.JSON;
					} else {
						return EContainerFormat.DEFAULT;
					}
				}
			} catch (IOException e) {
				MLogger.getInstance().log(Level.SEVERE, e);
				return EContainerFormat.CSV;
			}
		} else if ((fileName != null) && (!fileName.isBlank())) {
			if (fileName.endsWith(".properties")) {
				return EContainerFormat.PROPERTIES;
			} else if (fileName.endsWith(".xml")) {
				return EContainerFormat.XML;
				// ToDo: Validation of XML file need to be re-defined. The current
				// implementation fails when the file is empty.
//				if(FileUtil.checkFile(fileName)) {
//					if(XMLEditor.validateXMLFile(new File(fileName))) {
//						return EContainerFormat.XML;
//					}
//				} else {
//					return EContainerFormat.XML;
//				}
			} else if (fileName.endsWith(".json")) {
				return EContainerFormat.JSON;
			} else {
				return EContainerFormat.CSV;
			}
		}
		return EContainerFormat.DEFAULT;
	}

	/**
	 * This method writes data to a existing or newly created CSV file. Useful if
	 * the current state of the container should be saved. The semicolon gets used
	 * as column separator.
	 * 
	 * @param fileName Full path and name of the source file, from which the data
	 *                 will be loaded into DataContainer
	 * @throws IOException If any error occurred when writing to the file.
	 */
	public void exportContainer(String fileName) throws IOException {
		exportContainer(fileName, ";");
	}

	/**
	 * This method writes data to a existing or newly created CSV file. Useful if
	 * the current state of the container should be saved. The column separator can
	 * be set.
	 *
	 * @param fileName        The file path of the file to write to or to create.
	 * @param columnDelimiter The column separator to structure the data.
	 * @throws IOException If any error occurred when writing to the file.
	 * 
	 */
	public void exportContainer(String fileName, String columnDelimiter) throws IOException {
		HashMap<Integer, String> hm = getHeadersIndexed();
		XFileWriter writer = null;
//		try {
			writer = new XFileWriter(new File(fileName));
//		} catch (FileNotFoundException e) {
//			MLogger.getInstance().log(Level.SEVERE, e);
//		}
		if (writer != null) {
			switch (getContainerFormat().getHeaderType()) {
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
				MLogger.getInstance().log(Level.WARNING, "Header Type '" + getContainerFormat().getHeaderType().toString() + "' not supported by for export!", getClass().getSimpleName(),
						getClass().getName(), "exportContainer");
				return;
			}
			writer.close();
		}
	}

	/**
	 * A specific method for type {@link XMLDataContainer} to get the attributes of
	 * a XML tag.
	 * 
	 * @param tag      The name of the XML tag to refer to.
	 * @param attrName The name of the XML attribute to refer to.
	 * @return An array of type string with all attributes to the committed tag.
	 */
	public String[] getAttributes(String tag, String attrName) {
		switch (getContainerFormat().getHeaderType()) {
		case TREE:
			return instance.getAttributes(tag, attrName);
		default:
			return new String[0];
		}

	}

	/**
	 * Gets the content of a column from the <code>DataContainer</code> instance. In
	 * order to get the columns content, it is iterated through each row saved. The
	 * column is specified with index and the content is returned as
	 * <code>Array</code> of type String
	 *
	 * @param index The column's index, which content is searched.
	 * @return The content of the specified column as <code>Array</code> of type
	 *         String.
	 */
	public String[] getColumn(int index) {
		return getColumn(getHeaderName(index), new int[0], new Filter());
	}

	/**
	 * Gets the content of a column from the <code>DataContainer</code> instance. In
	 * order to get the columns content, it is iterated through each row saved. The
	 * column is specified with header name and the content is returned as
	 * <code>Array</code> of type String
	 *
	 * @param colName The header name of the column.
	 * @return The content of the specified column as <code>Array</code> of type
	 *         String.
	 */
	public String[] getColumn(String colName) {
		return getColumn(colName, new int[0], new Filter());
	}

	/**
	 * Gets filtered content of a column from the <code>DataContainer</code>
	 * instance. Only values of a column that match to one or more conditions. In
	 * order to get the columns content, it is iterated through each row saved. The
	 * column is specified with index and the content is returned as
	 * <code>Array</code> of type <code>String</code>.
	 *
	 * @param index The column's index, which content is searched.
	 * @param fltr  A {@link org.opentdk.api.datastorage.Filter} object to define
	 *              which data should be ignored. If it is null no filter gets used.
	 * @return the content of the specified column as <code>Array</code> of type
	 *         <code>String</code>.
	 */
	public String[] getColumn(int index, Filter fltr) {
		return getColumn(getHeaderName(index), new int[0], fltr);
	}

	/**
	 * Gets filtered content of a column from the <code>DataContainer</code>
	 * instance. Only values of a column, that match to one or more conditions. In
	 * order to get the columns content, it is iterated through each row saved. The
	 * column is specified with column header name and the content is returned as
	 * <code>Array</code> of type <code>String</code>.
	 * 
	 * @param colName The header name of the column.
	 * @param fltr    A {@link org.opentdk.api.datastorage.Filter} object to define
	 *                which data should be ignored. If it is null no filter gets
	 *                used.
	 * @return the content of the specified column as <code>Array</code> of type
	 *         <code>String</code>.
	 */
	public String[] getColumn(String colName, Filter fltr) {
		return getColumn(colName, new int[0], fltr);
	}

	/**
	 * Gets filtered content of a column from the <code>DataContainer</code>
	 * instance. Only values of a column, that match to one or more conditions. In
	 * order to get the columns content, it is iterated through each row saved. The
	 * column is specified with column header name and the content is returned as
	 * <code>Array</code> of type <code>String</code>, but only if the detected rows
	 * in the column match the committed row indexes.
	 * 
	 * @param colName    The header name of the column.
	 * @param rowIndexes An array of type integer that contains all row indexes that
	 *                   should be taken into account when searching in the column.
	 * @param fltr       A {@link org.opentdk.api.datastorage.Filter} object to
	 *                   define which data should be ignored. If it is null no
	 *                   filter gets used.
	 * @return the content of the specified column as <code>Array</code> of type
	 *         <code>String</code>.
	 */
	public String[] getColumn(String colName, int[] rowIndexes, Filter fltr) {
		List<String[]> outLst = getColumnsList(colName.split(";"), rowIndexes, fltr);
		if (outLst.size() > 0) {
			return outLst.get(0);
		}
		return new String[] {};
	}

	/**
	 * Returns the number of columns defined within the current instance of the
	 * <code>DataContainer</code>.
	 *
	 * @return number of columns
	 */
	public int getColumnCount() {
		return getHeaders().size();
	}

	/**
	 * Gets a <code>List</code> of string arrays with all column values of the
	 * <code>DataContainer</code> instance.
	 *
	 * @return Object of type <code>List</code> with string arrays, including all
	 *         matching values of a column.
	 */
	public List<String[]> getColumnsList() {
		return getColumnsList(new String[0], new int[0], new Filter());
	}

	/**
	 * Gets a <code>List</code> of string arrays with all column values that match
	 * to the defined filter. The Filter defines the rules for matching rows. <br>
	 * <br>
	 * e.g. get string arrays for each column of table 'Customer' where the value of
	 * 'City' equals to 'Munich'.
	 *
	 * @param rowFilter Object of type {@link org.opentdk.api.datastorage.Filter}
	 *                  which defines rules for matching rows
	 * @return Object of type <code>List</code> with string arrays, including all
	 *         matching values of a column.
	 */
	public List<String[]> getColumnsList(Filter rowFilter) {
		return getColumnsList(new String[0], new int[0], rowFilter);
	}

	/**
	 * Gets a <code>List</code> of string arrays with all column values that match
	 * to the defined filter. The Filter defines the rules for matching rows. <br>
	 * <br>
	 * e.g. get string arrays for each column of table 'Customer' where the value of
	 * 'City' equals to 'Munich'.
	 *
	 * @param columnHeaders Semicolon separated string with the column names of
	 *                      which the values will be returned.
	 * @param rowFilter     Object of type
	 *                      {@link org.opentdk.api.datastorage.Filter} which defines
	 *                      rules for matching rows.
	 * @return Object of type <code>List</code> with string arrays, including all
	 *         matching values of a column.
	 */
	public List<String[]> getColumnsList(String columnHeaders, Filter rowFilter) {
		return getColumnsList(columnHeaders.split(";"), new int[0], rowFilter);
	}

	/**
	 * Gets a <code>List</code> of string arrays with all column values that match
	 * to the defined filter. The Filter defines the rules for matching rows.
	 * Additionally it is possible to define the row indices that should be taken
	 * into account. <br>
	 * <br>
	 * e.g. get string arrays for each column of table 'Customer' where the value of
	 * 'City' equals to 'Munich'.
	 *
	 * @param columnHeaders Semicolon separated string with the column names of
	 *                      which the values will be returned.
	 * @param rowFilter     Object of type
	 *                      {@link org.opentdk.api.datastorage.Filter} which defines
	 *                      rules for matching rows.
	 * @param rowIndexes    The row numbers that should be used as search criteria.
	 * @return Object of type <code>List</code> with string arrays, including all
	 *         matching values of a column.
	 */
	public List<String[]> getColumnsList(String[] columnHeaders, int[] rowIndexes, Filter rowFilter) {
		List<String[]> colList = new ArrayList<String[]>();
		switch (getContainerFormat().getHeaderType()) {
		case TREE:
			for (String headerName : columnHeaders) {
				try {
					colList.add((String[]) instance.getColumn(headerName, rowFilter));
				} catch (Exception e) {
					MLogger.getInstance().log(Level.SEVERE, e, "getColumnsList");
					throw new RuntimeException(e);
				}
			}
			break;
		default:
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
			break;
		}
		return colList;
	}

	/**
	 * Gets the maximum length of the values corresponding to the headerName.
	 *
	 * @param headerName The name of the header, which can be column or row,
	 *                   depending on the orientation
	 * @return Length of the longest String corresponding to headerName
	 */
	public int getMaxLen(String headerName) {
		int ret = 0;
		List<String> valList = getValuesAsList(headerName);
		for (String s : valList) {
			if (s.length() > ret)
				ret = s.length();
		}
		return ret;
	}

	/**
	 * Gets the content of a row of the container instance. The row is specified by
	 * index. If the given index is bigger than the list's size, it will return
	 * <code>null</code>.
	 *
	 * @param rowIndex The row's index, which content is searched.
	 * @return The content of the specified row.
	 */
	public String[] getRow(int rowIndex) {
		return getRow(rowIndex, null, new Filter());
	}

	/**
	 * Gets the content of a row of the container instance. The row is specified by
	 * index. If the given index is bigger than the list's size, it will return
	 * <code>null</code>.
	 *
	 * @param rowIndex      The row's index, which content is searched.
	 * @param columnHeaders The column headers that should be taken into account,
	 *                      separated by semicolon. If only one header should be
	 *                      specified, no separation is needed of course.
	 * @return The content of the specified row.
	 */
	public String[] getRow(int rowIndex, String columnHeaders) {
		return getRow(rowIndex, columnHeaders, new Filter());
	}

	/**
	 * Gets the content of a row of the container instance. The row is specified by
	 * index. If the given index is bigger than the list's size, it will return
	 * <code>null</code>.
	 *
	 * @param rowIndex      The row's index, which content is searched.
	 * @param columnHeaders The column headers that should be taken into account,
	 *                      separated by semicolon. If only one header should be
	 *                      specified, no separation is needed of course.
	 * @return The content of the specified row.
	 */
	public String[] getRow(int rowIndex, String[] columnHeaders) {
		return getRow(rowIndex, String.join(";", columnHeaders), new Filter());
	}

	/**
	 * Gets the content of a row of the container instance. The row is specified by
	 * index. If the given index is bigger than the list's size, it will return
	 * <code>null</code>.
	 *
	 * @param rowIndex      The row's index, which content is searched.
	 * @param columnHeaders The column headers that should be taken into account,
	 *                      separated by semicolon. If only one header should be
	 *                      specified, no separation is needed of course.
	 * @param fltr          Object of type
	 *                      {@link org.opentdk.api.datastorage.Filter} which defines
	 *                      rules for matching rows.
	 * @return The content of the specified row.
	 */
	public String[] getRow(int rowIndex, String columnHeaders, Filter fltr) {
		String[] colHeaders = null;
		if (columnHeaders == null) {
			colHeaders = new String[0];
		} else {
			colHeaders = columnHeaders.split(";");
		}
		List<String[]> outLst = getRowsList(new int[] { rowIndex }, colHeaders, fltr);
		if (outLst.size() > 0) {
			return outLst.get(0);
		}
		return new String[] {};
	}

	public Map<String, String> getRowAsMap(int rowIndex){
		Map<String, String> outRow = new HashMap<String, String>();
		String[] columnHeaders = getHeaderNamesIndexed();
		for(String header:columnHeaders) {
			outRow.put(header, this.getValue(header, rowIndex));
		}
		return outRow;
	}
	
	/**
	 * Returns the number of rows of the current <code>DataContainer</code>
	 * instance.
	 *
	 * @return Number of rows e.g number of lines in case of a CSV format or number
	 *         of key value pairs in case of a properties file.
	 */
	public int getRowCount() {
		return values.size();
	}

	/**
	 * Possibility to search for integer values of the <code>DataContainer</code>
	 * rows.
	 * 
	 * @param filter Object of type {@link org.opentdk.api.datastorage.Filter} which
	 *               defines rules for matching rows.
	 * @return The numbers of the matching rows as integer array.
	 */
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

	/**
	 * Returns a list with all rows stored in the <code>DataContainer</code>
	 * instance.
	 *
	 * @return list object with stored data rows.
	 */
	public List<String[]> getRowsList() {
		return getRowsList(new int[0], new String[0], new Filter());
	}

	/**
	 * Returns a list with all rows, that match to the defined rowFilter.
	 *
	 * @param rowFilter Object of type {@link org.opentdk.api.datastorage.Filter}
	 *                  which defines rules for matching rows.
	 * @return A list with all rows. Each row is stored in one string array.
	 */
	public List<String[]> getRowsList(Filter rowFilter) {
		return getRowsList(new int[0], new String[0], rowFilter);
	}

	/**
	 * Returns a list of row values for defined columns that match to a defined
	 * rowFilter.
	 *
	 * @param columnHeader The header name of the column to search in.
	 * @param rowFilter    Object of type {@link org.opentdk.api.datastorage.Filter}
	 *                     which defines rules for matching rows.
	 * @return A list with all rows. Each row is stored in one string array.
	 */
	public List<String[]> getRowsList(String columnHeader, Filter rowFilter) {
		return getRowsList(new int[0], columnHeader.split(";"), rowFilter);
	}

	/**
	 * Returns a list of row values for defined columns that match to a defined
	 * rowFilter.
	 *
	 * @param rowIndexes    The numbers of the rows that should be taken into
	 *                      account.
	 * @param columnHeaders The header name of the column to search in.
	 * @param rowFilter     Object of type
	 *                      {@link org.opentdk.api.datastorage.Filter} which defines
	 *                      rules for matching rows.
	 * 
	 * @return A list with all rows. Each row is stored in one string array.
	 */
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
				MLogger.getInstance().log(Level.INFO, "Row-index " + rowIndexes[i] + " is out of range. Maximum number of rows in container is " + values.size(), this.getClass().getSimpleName(),
						this.getClass().getName(), "getRowsList");
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

	/**
	 * Removes the row of the container instance at the specified index.
	 * 
	 * @param index integer that has to be in the range of the values size.
	 */
	public void deleteRow(int index) {
		values.remove(index);
	}

	/**
	 * Deletes all rows of the container instance that match the defined filter
	 * criteria. If no rows could be detected the method returns. If there is a file
	 * defined for the container the changes get written to it afterwards.
	 * 
	 * @param fltr {@link org.opentdk.api.datastorage.Filter} object with the set
	 *             filter rules.
	 */
	public void deleteRows(Filter fltr) {
		int[] indexes = getRowsIndexes(fltr);
		if (indexes.length == 0) {
			MLogger.getInstance().log(Level.WARNING, "No row indexes detected for the filter criteria", getClass().getSimpleName(), "deleteRows");
		} else {
			for (int index : indexes) {
				values.remove(index);
			}
			if (!fileName.isEmpty()) {
				writeData(fileName);
			}
		}
	}

	/**
	 * Use the <code>getValue</code> method to return the first value in column
	 * addressed by headerName.
	 *
	 * @param headerName The name of the column header
	 * @return the first value in column addressed by headerName.
	 */
	public String getValue(String headerName) {
		return getValue(headerName, -1, new Filter());
	}

	/**
	 * Use the <code>getValue</code> method to return a value from a field addressed
	 * by headerName of the column and index of the row.
	 *
	 * @param headerName The name of the column header
	 * @param rowIndex   row index of the field
	 * @return the value as String
	 */
	public String getValue(String headerName, int rowIndex) {
		return getValue(headerName, rowIndex, new Filter());
	}

	/**
	 * Use the <code>getValue</code> method to return a value from a field addressed
	 * by headerName of the column and index of the row.
	 *
	 * @param headerIndex The name of the column header
	 * @param rowIndex    row-index of the field
	 * @return the value as String
	 */
	public String getValue(int headerIndex, int rowIndex) {
		return getValue(getHeaderName(headerIndex), rowIndex, new Filter());
	}

	/**
	 * Use the <code>getValue</code> method to return a value from a field addressed
	 * by headerName of the column and prepared filter.
	 *
	 * @param headerName The name of the column header
	 * @param fltr       A prepared Filter instance to categorize the result
	 * @return the value as String
	 */
	public String getValue(String headerName, Filter fltr) {
		return getValue(headerName, -1, fltr);
	}

	/**
	 * Use the getValuesAsList() method to search for a value by header name, row
	 * index and prepared filter.
	 * 
	 * @param headerName The name of the column header
	 * @param rowIndex   The row index. If it is unknown or not used, commit -1 to
	 *                   search for header name and filter
	 * @param fltr       A prepared Filter instance to categorize the result.
	 * @return the value as String or an empty string if no result could be found
	 */
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

	/**
	 * Gets all values from the DataContainer instance directly.
	 *
	 * @return List of type String with all values
	 */
	public List<String[]> getValues() {
		return values;
	}

	/**
	 * Gets a sequence of distincted values from the DataContainer instance.
	 * Depending on the header orientation, this method will retrieve the values of
	 * one row, or one column, which is defined by headerName.
	 *
	 * @param headerName The name of the header, which can be column or row,
	 *                   depending on the orientation
	 * @return distincted list of type String with the selected values
	 */
	public List<String> getValuesAsDistinctedList(String headerName) {
		List<String> values = getValuesAsList(headerName, new int[0], new Filter());
		Set<String> uniqueValues = new HashSet<>(values);
		return Arrays.asList(uniqueValues.toArray(new String[uniqueValues.size()]));
	}

	/**
	 * Gets a sequence of numeric values from the DataContainer instance and returns
	 * them as List of Double. Depending on the header orientation, this method will
	 * retrieve the values of one row, or one column, which is defined by
	 * headerName.
	 *
	 * @param headerName The name of the header, which can be column or row,
	 *                   depending on the orientation
	 * @return List of type Double with all selected values
	 */
	public List<Double> getValuesAsDoubleList(String headerName) {
		return this.getValuesAsDoubleList(headerName, new Filter());
	}

	/**
	 * Gets a sequence of numeric values from the current DataContainer instance,
	 * that matches to a defined filter and returns them as a List of Double.
	 * Depending on the header orientation, this method will retrieve the values of
	 * one row, or one column, which is defined by headerName.
	 *
	 * @param headerName The name of the header
	 * @param rowFilter  Object of type {@link org.opentdk.api.datastorage.Filter}
	 *                   which defines rules for matching rows
	 * @return List of type Double with all selected values
	 */
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

	/**
	 * Gets a sequence of numeric values from the DataContainer instance and returns
	 * them as List of integer. Depending on the header orientation, this method
	 * will retrieve the values of one row, or one column, which is defined by
	 * headerName.
	 *
	 * @param headerName The name of the header, which can be column or row,
	 *                   depending on the orientation
	 * @return List of type Integer with all selected values
	 */
	public List<Integer> getValuesAsIntList(String headerName) {
		return this.getValuesAsIntList(headerName, new Filter());
	}

	/**
	 * Gets a sequence of numeric values from the current DataContainer instance,
	 * that matches to a defined filter and returns them as a List of Integer.
	 * Depending on the header orientation, this method will retrieve the values of
	 * one row, or one column, which is defined by headerName.
	 *
	 * @param headerName The name of the header
	 * @param rowFilter  Object of type {@link org.opentdk.api.datastorage.Filter}
	 *                   which defines rules for matching rows
	 * @return List of type integer with all selected values
	 */
	public List<Integer> getValuesAsIntList(String headerName, Filter rowFilter) {
		List<Integer> intList = new ArrayList<Integer>();
		List<String> valList = getValuesAsList(headerName, rowFilter);
		for (String val : valList) {
			intList.add(Integer.valueOf(val));
		}
		return intList;
	}

	/**
	 * Gets a sequence of values from the DataContainer instance. Depending on the
	 * header orientation, this method will retrieve the values of one row, or one
	 * column, which is defined by headerName.
	 *
	 * @param headerName The name of the header, which can be column or row,
	 *                   depending on the orientation
	 * @return List of type String with all selected values
	 */
	public List<String> getValuesAsList(String headerName) {
		return getValuesAsList(headerName, new int[0], new Filter());
	}

	/**
	 * Gets a sequence of values from the current DataContainer instance, that
	 * matches to a defined filter. Depending on the header orientation, this method
	 * will retrieve the values of one row, or one column, which is defined by
	 * headerName.
	 *
	 * @param headerName The name of the header
	 * @param rowFilter  Object of type {@link org.opentdk.api.datastorage.Filter}
	 *                   which defines rules for matching rows
	 * @return List of type String with all selected values
	 */
	public List<String> getValuesAsList(String headerName, Filter rowFilter) {
		return getValuesAsList(headerName, new int[0], rowFilter);
	}

	/**
	 * Gets a sequence of values from the current DataContainer instance, that
	 * matches to a defined filter and defined row indexes. Depending on the header
	 * orientation, this method will retrieve the values of one row, or one column,
	 * which is defined by headerName.
	 *
	 * @param headerName The name of the header
	 * @param rowIndexes int array with index numbers of the rows that will be
	 *                   retrieved
	 * @param fltr       Object of type {@link org.opentdk.api.datastorage.Filter}
	 *                   which defines rules for matching rows
	 * @return List of type String with all selected values
	 */
	public List<String> getValuesAsList(String headerName, int[] rowIndexes, Filter fltr) {
		List<String[]> colLst = getColumnsList(headerName.split(";"), rowIndexes, fltr);
		List<String> outList = new ArrayList<String>();
		if (colLst.size() > 0) {
			outList = Arrays.asList(colLst.get(0));
		}
		return outList;
	}

	/**
	 * Gets a representation of all values within the DataContainer. Columns will be
	 * separated by the associated or default column delimiter, rows by a new line.
	 *
	 * @return String with the complete content of the DataContainer
	 */
	public String getValuesAsString() {
		StringBuilder ret = new StringBuilder();
		for (String[] row : values) {
			ret.append(String.join(columnDelimiter, row));
			ret.append("\n");
		}
		return ret.toString();
	}

	/**
	 * Compares the row at committed index with the committed string array. If one
	 * of the still existing row values is null or empty, it gets replaced by the
	 * value from the new array.
	 * 
	 * @param rowIndex  row number to identify the row that should be merged
	 * @param newValues the new data as string array
	 */
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

	/**
	 * If the <code>DataContainer</code> initialization and the data import needs to
	 * be done separately, this method can be used. This method requires that the
	 * data source is known by the specific <code>DataContainer</code>, e.g. a valid
	 * source file is assigned to the {@link BaseContainer#fileName} property.
	 */
	public void readData() {
		instance.readData(new Filter());
	}

	/**
	 * If the <code>DataContainer</code> initialization and the data import needs to
	 * be done separately, this method can be used.
	 * 
	 * @param fileName Full path and name of the source file, from which the data
	 *                 will be loaded into DataContainer
	 */
	public void readData(String fileName) {
		this.setFileName(fileName);
		instance.readData(new Filter());
	}

	/**
	 * If the <code>DataContainer</code> initialization and the data import needs to
	 * be done separately, this method can be used. This method requires that the
	 * data source is known by the specific <code>DataContainer</code>, e.g. a valid
	 * source file is assigned to the {@link BaseContainer#fileName} property.
	 * 
	 * @param fltr A {@link org.opentdk.api.datastorage.Filter} object to define
	 *             which data should be ignored. If it is null no filter gets used.
	 */
	public void readData(Filter fltr) {
		instance.readData(fltr);
	}

	/**
	 * If the <code>DataContainer</code> initialization and the data import needs to
	 * be done separately, this method can be used.
	 * 
	 * @param fileName Full path and name of the source file, from which the data
	 *                 will be loaded into DataContainer
	 * 
	 * @param fltr     A {@link org.opentdk.api.datastorage.Filter} object to define
	 *                 which data should be ignored. If it is null no filter gets
	 *                 used.
	 */
	public void readData(String fileName, Filter fltr) {
		this.setFileName(fileName);
		instance.readData(fltr);
	}

	/**
	 * Sets a column in the <code>DataContainer</code> instance with the specified
	 * new content. If the new column is bigger than the old one, new rows with
	 * empty values will be added.
	 *
	 * @param headerName   The name of the column. Existing column will be
	 *                     overwritten and non-existing column will be created.
	 * @param columnValues The values for the column as string array.
	 */
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

	/**
	 * Sets a column in the <code>DataContainer</code> instance with the specified
	 * new content. If the new column is bigger than the old one, new rows with
	 * empty values will be added.
	 *
	 * @param headerName   The name of the column. Existing column will be
	 *                     overwritten and non-existing column will be created.
	 * @param columnValues The values for the column as List of strings.
	 */
	public void setColumn(String headerName, List<String> columnValues) {
		setColumn(headerName, columnValues.toArray(new String[columnValues.size()]));
	}

	/**
	 * Sub method that finally edits the values object e.g. when a setValue call
	 * occurs.
	 * 
	 * @param headerName Name of the sequence header.
	 * @param index      Index of the field within the DataSet.
	 * @param value      Value as String that will be set to the field.
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
			valArr = this.getRow(index);
		}
		if (valArr == null) {
			valArr = new String[this.getHeaders().size()];
		}
		valArr[this.getHeaderIndex(headerName)] = val;
		if (values.size() <= 0) {
			values.add(index, valArr);
		} else {
			values.set(index, valArr);
		}
	}

	/**
	 * Replaces the row at the committed index with the data of the committed string
	 * array.
	 * 
	 * @param rowIndex  row number to identify the row that should be replaced
	 * @param rowValues the new data as string array
	 */
	public void setRow(int rowIndex, String[] rowValues) {
		values.set(rowIndex, addMetaValues(rowValues));
	}

	/**
	 * Sets content to the first field of a sequence defined by header name with a
	 * specified value. The existing content of that field will be overwritten.
	 *
	 * @param headerName Name of the sequence header
	 * @param value      Value as String that will be set to the field
	 */
	public void setValue(String headerName, String value) {
		setValue(headerName, 0, value, new Filter());
	}

	/**
	 * Sets content of a defined field by header name and index with a specified
	 * value. The existing content of that field will be overwritten. If a header
	 * does not exist, it will be created by shifting all existing values one
	 * position backward and setting the new one at the first position.
	 *
	 * @param headerName Name of the sequence header
	 * @param index      Index of the field within the DataSet
	 * @param value      Value as String that will be set to the field
	 */
	public void setValue(String headerName, int index, String value) {
		setValue(headerName, index, value, new Filter());
	}

	/**
	 * Sets content of a defined field by header name and filter with a specified
	 * value. The existing content of the field within the first matching dataset
	 * will be overwritten. If a header does not exist, it will be created by
	 * shifting all existing values one position backward and setting the new one at
	 * the first position.
	 *
	 * @param headerName Name of the sequence header
	 * @param value      Value as String that will be set to the field
	 * @param fltr       Filter for selection of the target dataset (row or column)
	 */
	public void setValue(String headerName, String value, Filter fltr) {
		setValue(headerName, 0, value, fltr);
	}

	/**
	 * Sets content of a defined field by header name, index and filter with a
	 * specified value. The existing content of the field within the dataset will be
	 * overwritten. If a header does not exist, it will be created by shifting all
	 * existing values one position backward and setting the new one at the first
	 * position.
	 *
	 * @param headerName Name of the sequence header
	 * @param index      zero based index that defines the position of the field in
	 *                   case that multiple fields were found
	 * @param value      Value as String that will be set to the field
	 * @param fltr       Filter for selection of the target dataset (row or column)
	 */
	public void setValue(String headerName, int index, String value, Filter fltr) {
		setValues(headerName, new int[] { index }, value, fltr);
	}

	/**
	 * Sets content of the first matching field defined by header name and filter
	 * with a specified value. The existing content of the field within the dataset
	 * will be overwritten. If a header does not exist, it will be created by
	 * shifting all existing values one position backward and setting the new one at
	 * the first position.
	 *
	 * @param headerName Name of the sequence header
	 * @param value      Value as String that will be set to the field
	 * @param fltr       Filter for selection of the target dataset (row or column)
	 */
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
	 * Sets content of multiple fields by header name, index and filter with a
	 * specified value. The existing content of the fields within the dataset will
	 * be overwritten. If a header does not exist, it will be created by shifting
	 * all existing values one position backward and setting the new one at the
	 * first position.
	 *
	 * @param headerName Name of the sequence header
	 * @param indexes    Array with indexes, defining the position of all fields
	 *                   which will be overwritten
	 * @param value      Value as String that will be set to the field
	 * @param fltr       Filter for selection of the target dataset (row or column)
	 */
	public void setValues(String headerName, int[] indexes, String value, Filter fltr) {
		switch (getContainerFormat().getHeaderType()) {
		case TREE:
			instance.setFieldValues(headerName, indexes, value, fltr);
			break;
		default:
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
						MLogger.getInstance().log(Level.INFO, "Occurence index not in occurences array.", this.getClass().getSimpleName(),
								"setValues(String headerName, int[] occurences, String value, Filter fltr)");
					} else {
						setFieldValue(headerName, rowIDs[i], value);
					}
				}
			}
			if (!fileName.isEmpty()) {
				writeData(fileName);
			}
			break;
		}
	}

	/**
	 * Adapt to the specific data container when the caller of the
	 * <code>DataContainer</code> wants to save the changes to the configuration
	 * output file.
	 *
	 * @param srcFile The path to the source file.
	 */
	public void writeData(String srcFile) {
		try {
			instance.writeData(srcFile);
		} catch (IOException e) {
			MLogger.getInstance().log(Level.SEVERE, e, "setValues");
			throw new RuntimeException(e);
		}
	}

}
