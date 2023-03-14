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

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.opentdk.api.filter.Filter;
import org.opentdk.api.filter.FilterRule;
import org.opentdk.api.io.XMLEditor;
import org.opentdk.api.logger.MLogger;
import org.yaml.snakeyaml.Yaml;

/**
 * This class gets used to store data from different sources at runtime of an application. The class
 * provides methods to put data from different sources into an instance of a specific container
 * class, as well as getter and setter methods to read data from, and write data into the
 * object.<br>
 * <br>
 * This will allow to build applications with standardized data interface with minimum maintenance
 * effort in case that the data source changes.
 *
 * @author LK Test Solutions
 */
public class DataContainer implements SpecificContainer {

	/**
	 * The object that will be adapted to a specific <code>DataContainer</code> like
	 * {@link XMLDataContainer} or {@link PropertiesDataContainer}. The caller of the
	 * <code>DataContainer</code> does not need to know about the adaption process. In other words: The
	 * caller can use the <code>DataContainer</code> on all supported file formats without calling
	 * specific functions for the source file type.
	 */
	private SpecificContainer instance;

	/**
	 * Full path and name of the file for DataContainers with file based sources like
	 * {@link CSVDataContainer}, {@link PropertiesDataContainer} or {@link XMLDataContainer}. This file
	 * will be used as default for all read and write methods that are called without an explicit file
	 * attribute.
	 */
	private File inputFile = new File("");

	/**
	 * The filter object that does not filter the data in the <code>DataContainer</code> during runtime
	 * but when reading from and writing to the configuration file.
	 */
	private Filter filter = new Filter();

	/**
	 * Property that stores the container format for the instance of the {@link DataContainer} as an
	 * enumeration of type {@link EContainerFormat}.
	 */
	private EContainerFormat containerFormat = EContainerFormat.TEXT;

	/**
	 * This property is used to assign the data as an {@link java.io.InputStream} to the
	 * {@link DataContainer} instance in case that no source file exists.
	 */
	private InputStream inputStream;

	/**
	 * This property is used to assign the data as an {@link java.lang.String} to the
	 * {@link DataContainer} instance in case that no source file or stream exists.
	 */
//	private String inputString = "";
//	private List<String> lines = new ArrayList<>();

	/**
	 * Keeps the SQL result set of {@link org.opentdk.api.datastorage.RSDataContainer}.
	 */
	private ResultSet resultSet;

	/**
	 * Stores the root node of the {@link org.opentdk.api.datastorage.XMLDataContainer}.
	 */
	private String rootNode = "";

	/**
	 * This property is used for the adaption of different source formats, to make the data accessible
	 * in a similar way, as they were organized in tabular format.<br>
	 * E.g. source data of tree format will be transposed into a tree table with the parent path stored
	 * within an additional column for each node. This allows access to the node with the same methods
	 * as implemented for tabular formated data.
	 */
	private final Set<String> implicitHeaders = new HashSet<>();

	/**
	 * The non argument constructor get used to have an empty container instance without a connected
	 * file, stream or result set and without knowing the container format. In this case all the fields
	 * in the {@link org.opentdk.api.datastorage.DataContainer} class keep their default value.
	 * The container gets initialized as <code>CSVDataContainer</code> that stores text formats as well.
	 */
	public DataContainer() {
		adaptContainer();
	}

	/**
	 * Gets used if only the orientation is known. Format TREE points to XML and TEXT gets used
	 * otherwise.
	 *
	 * @param type {@link org.opentdk.api.datastorage.EHeader}
	 */
	public DataContainer(EHeader type) {
		switch (type) {
		case COLUMN:
			containerFormat = EContainerFormat.CSV;
			break;
		case ROW:
			containerFormat = EContainerFormat.PROPERTIES;
			break;
		case TREE:
			containerFormat = EContainerFormat.XML;
			break;
		default:
			containerFormat = EContainerFormat.TEXT;
			break;
		}
		adaptContainer();
	}

	/**
	 * Constructor that is called when no input file exists and the container for the specific data
	 * format also accepts {@link java.io.InputStream} for the data.<br>
	 * e.g.: An application dynamically receives data by calling RestAPI Service Requests and the XML
	 * responses will be passed to the {@link org.opentdk.api.dispatcher.BaseDispatcher} that creates an
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
		inputStream = inStream;
		adaptContainer();
	}

	/**
	 * Constructor that is called, when importing data from a database request to the container. No
	 * filter will be used and the column delimiter for the imported data is the semicolon.
	 *
	 * @param rs The result of the database request.
	 */
	public DataContainer(ResultSet rs) {
		this(rs, null);
	}

	/**
	 * Constructor that is called, when importing data from a database request to the container with the
	 * possibility to filter the data before it gets imported. The column delimiter of the imported data
	 * is the semicolon.
	 *
	 * @param rs   The result of the database request.
	 * @param fltr A {@link org.opentdk.api.filter.Filter} object to define which data should be
	 *             ignored. If it is null no filter gets used.
	 */
	public DataContainer(ResultSet rs, Filter fltr) {
		resultSet = rs;
		filter = fltr;
		adaptContainer();
	}

	/**
	 * Constructor that is called with the source file to store data from.
	 *
	 * @param srcFile Full path and name of the source file, from which the data will be loaded into the
	 *                <code>DataContainer</code>
	 */
	public DataContainer(File srcFile) {
		this(srcFile, null);
	}

	/**
	 * Constructor that is called with the source file to store data from and a filter to decide if part
	 * of the data should be ignored.
	 *
	 * @param srcFile Full path and name of the source file, from which the data will be loaded into the
	 *                <code>DataContainer</code>
	 * @param fltr    The {@link #filter} object to decide which data should be ignored
	 */
	public DataContainer(File srcFile, Filter fltr) {
		inputFile = srcFile;
		filter = fltr;
		adaptContainer();
	}

//	public DataContainer(String sourceString) {
//		inputString = sourceString;
//		adaptContainer();
//	}

	/**
	 * Constructor that is called when a DataContainer shall be copied. The copy holds only the columns
	 * with the headers specified in headerIndices.
	 *
	 * @param dc            Original DataContainer from which the columns shall be copied
	 * @param headerIndices Headers which shall be copied to the new DataContainer
	 *
	 */
	public DataContainer(DataContainer dc, int[] headerIndices) {
		if (dc.isTabular()) {
			instance = dc.instance;
			inputFile = dc.getInputFile();
			containerFormat = dc.getContainerFormat();
			filter = dc.getFilter();

			tabInstance().setColumnDelimiter(dc.tabInstance().getColumnDelimiter());
			tabInstance().setHeaderRowIndex(dc.tabInstance().getHeaderRowIndex());

			String[] headerNames = new String[headerIndices.length];
			for (int i = 0; i < headerIndices.length; i++) {
				headerNames[i] = dc.tabInstance().getHeaderName(headerIndices[i]);
			}
			tabInstance().setHeaders(headerNames);
			for (int i = 0; i < headerIndices.length; i++) {
				tabInstance().setColumn(headerNames[i], dc.tabInstance().getColumn(headerNames[i]));
			}
		} else {
			MLogger.getInstance().log(Level.WARNING, "Copy from other DataContainer only available for tabular formats");
		}
	}

	/**
	 * This method adapts the one {@link SpecificContainer} instance to a specific {@link DataContainer}
	 * depending on the source file ending or existence of a stream. It calls {@link #readData(Filter)}
	 * at the end if there is a source. In case of an empty container the read operation has to be
	 * triggered separately.
	 *
	 * @throws IOException to handle I/O methods when the {@link #readData(Filter)} method failed
	 */
	private final void adaptContainer() {
		detectDataFormat();
		switch (containerFormat) {
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
			break;
		case JSON:
			instance = new JSONDataContainer(this);
			break;
		case YAML:
			instance = new YAMLDataContainer(this);
			break;
		default:
			instance = new CSVDataContainer(this);
			return;
		}
		if ((inputFile != null && inputFile.exists()) || inputStream != null || resultSet != null) {
			try {
				instance.readData(filter);
			} catch (IOException e) {
				MLogger.getInstance().log(Level.SEVERE, e);
			}
		}
	}

	/**
	 * Analyzes the file extension and/or structure of the data content and an enumeration of type
	 * {@link EContainerFormat} which defines the type of DataContaner that needs to be adapted.
	 *
	 * @return enumeration of type {@link EContainerFormat}
	 */
	private final void detectDataFormat() {
		if (resultSet != null) {
			containerFormat = EContainerFormat.RESULTSET;
		} else if (inputStream != null) {
			try {
				if (inputStream.available() > 0) {
					InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
					Stream<String> streamOfString = new BufferedReader(inputStreamReader).lines();
					String inputContent = streamOfString.collect(Collectors.joining());

					// Reader not needed anymore after stored to string
					inputStreamReader.close();
					streamOfString.close();

					// Every access consumes the stream so a reset is necessary
					inputStream.reset();

					if (inputContent.startsWith("<")) {
						// Own if clause due to performance reasons
						if (XMLEditor.validateXMLString(inputStream)) {
							inputStream.reset();
							containerFormat = EContainerFormat.XML;
						}
						inputStream.reset();
					} else if (inputContent.startsWith("{")) {
						if (StringUtils.isNotBlank(JSONObject.valueToString(inputContent))) {
							containerFormat = EContainerFormat.JSON;
						}
					} else {
						Map<String, Object> yamlContent = new Yaml().load(inputContent);
						if (!yamlContent.isEmpty()) {
							containerFormat = EContainerFormat.YAML;
						}
					}
				}
			} catch (IOException e) {
				MLogger.getInstance().log(Level.SEVERE, e);
			}
		} else if (inputFile != null) {
			String fileName = inputFile.getName();
			if (StringUtils.isNotBlank(fileName)) {
				if (fileName.endsWith(".txt")) {
					containerFormat = EContainerFormat.TEXT;
				} else if (fileName.endsWith(".properties")) {
					containerFormat = EContainerFormat.PROPERTIES;
				} else if (fileName.endsWith(".csv")) {
					containerFormat = EContainerFormat.CSV;
				} else if (fileName.endsWith(".xml")) {
					containerFormat = EContainerFormat.XML;
				} else if (fileName.endsWith(".json")) {
					containerFormat = EContainerFormat.JSON;
				} else if (fileName.endsWith(".yaml")) {
					containerFormat = EContainerFormat.YAML;
				}
			}
		}
//		else if (StringUtils.isNotBlank(inputString)) {
//			
//		}
	}

	/**
	 * Gets the value of the property {@link #containerFormat} which keeps information about the format
	 * of the source that is associated to the DataContainer.
	 *
	 * @return ENUM element of type {@link EContainerFormat}
	 */
	public EContainerFormat getContainerFormat() {
		return containerFormat;
	}

	/**
	 * @return the {@link #instance} if it was initialized as tabular format
	 * @throws NullPointerException is there is no tabular format available
	 */
	public TabularContainer tabInstance() {
		if (instance instanceof CSVDataContainer) {
			return (CSVDataContainer) instance;
		} else if (instance instanceof PropertiesDataContainer) {
			return (PropertiesDataContainer) instance;
		} else if (instance instanceof RSDataContainer) {
			return (RSDataContainer) instance;
		} else {
			throw new NullPointerException("TabularContainer not intialized");
		}
	}

	/**
	 * @return the {@link #instance} if it was initialized as tree format
	 * @throws NullPointerException is there is no tree format available
	 */
	public TreeContainer treeInstance() {
		if (instance instanceof XMLDataContainer) {
			return (XMLDataContainer) instance;
		} else if (instance instanceof JSONDataContainer) {
			return (JSONDataContainer) instance;
		} else if (instance instanceof YAMLDataContainer) {
			return (YAMLDataContainer) instance;
		} else {
			throw new NullPointerException("TreeContainer not intialized");
		}
	}

	/**
	 * Checks if the {@link #instance} was initialized as one of the tabular format e.g. CSV or
	 * RESULSET. This can be done before calling {@link #tabInstance()} to be sure that a tabular format
	 * is available.
	 *
	 * @return true: is tree format, false otherwise
	 */
	public boolean isTabular() {
		boolean ret = false;
		if (instance instanceof CSVDataContainer) {
			ret = true;
		} else if (instance instanceof PropertiesDataContainer) {
			ret = true;
		} else if (instance instanceof RSDataContainer) {
			ret = true;
		}
		return ret;
	}

	/**
	 * Checks if the {@link #instance} was initialized as one of the tree format e.g. XML, JSON or YAML.
	 * This can be done before calling {@link #treeInstance()} to be sure that a tree format is
	 * available.
	 *
	 * @return true: is tree format, false otherwise
	 */
	public boolean isTree() {
		boolean ret = false;
		if (instance instanceof XMLDataContainer) {
			ret = true;
		} else if (instance instanceof JSONDataContainer) {
			ret = true;
		} else if (instance instanceof YAMLDataContainer) {
			ret = true;
		}
		return ret;
	}

	/**
	 * @return {@link #inputFile}
	 */
	public File getInputFile() {
		return inputFile;
	}

	/**
	 * @return {@link #filter}
	 */
	public Filter getFilter() {
		return filter;
	}

	/**
	 * Returns the filter rules which belong to an implicit header column. This allows to use same
	 * column and row based getter and setter methods for tree formatted sources like XML, as they are
	 * used for tabular formatted sources. e.g. the {@link #getImplFilterRules(Filter)} method is used
	 * to limit the data records for the search to specified xPath(s).
	 *
	 * @param fltr Filter object with the complete filter rules for a data search
	 * @return List object with all FilterRules that belong to implicit columns
	 */
	List<FilterRule> getImplFilterRules(Filter fltr) {
		List<FilterRule> frList = new ArrayList<FilterRule>();
		for (FilterRule fr : fltr.getFilterRules()) {
			if (implicitHeaders.contains(fr.getHeaderName())) {
				frList.add(fr);
			}
		}
		return frList;
	}

	/**
	 * Sets the name of columns that will be populated with processed data by implemented logic of the
	 * individual DataContainer. e.g. The implicitHeaders will be used by the {@link XMLDataContainer}
	 * to add the column XPath, where the complete path of each tag will be inserted. This allows to
	 * transpose data from tree format into tabular format.
	 *
	 * @return object of type java.util.set with column names
	 */
	public Set<String> getImplicitHeaders() {
		return implicitHeaders;
	}

	/**
	 * @return {@link #inputStream}
	 */
	public InputStream getInputStream() {
		return inputStream;
	}

	/**
	 * Returns the {@link #resultSet} property with SQL results of type {@link java.sql.ResultSet}. This
	 * property is used to store the data of {@link RSDataContainer}.
	 *
	 * @return {@link #resultSet}
	 */
	public ResultSet getResultSet() {
		return resultSet;
	}

	/**
	 * @return {@link #rootNode}
	 */
	public String getRootNode() {
		return rootNode;
	}

	/**
	 * Sets the format information of the source used for this instance of DataContainer.
	 *
	 * @param cFormat ENUM value of type EContainerFormat
	 */
	public void setContainerFormat(EContainerFormat cFormat) {
		containerFormat = cFormat;
	}

	/**
	 * @param file {@link #inputFile}
	 */
	public void setInputFile(File file) {
		inputFile = file;
	}

	/**
	 * @param fltr {@link #filter}
	 */
	public void setFilter(Filter fltr) {
		filter = fltr;
	}

	/**
	 * Sets content of type {@link java.io.InputStream} to the {@link #inputStream} property that is
	 * used by DataContainers that have document type data connected like {@link XMLDataContainer} using
	 * {@link org.w3c.dom.Document} for XML and HTML formats. The {@link #inputStream} property can be
	 * used in case that the data source is not a file.
	 *
	 * @param inStream {@link #inputStream}
	 */
	public void setInputStream(InputStream inStream) {
		inputStream = inStream;
	}

	/**
	 * Assigns an object of type {@link java.sql.ResultSet} with SQL results to the property
	 * {@link #resultSet}. This property is used by the {@link RSDataContainer}
	 *
	 * @param rs Object of type {@link java.sql.ResultSet}
	 */
	public void setResultSet(ResultSet rs) {
		resultSet = rs;
	}

	/**
	 * @param rn {@link #rootNode}
	 */
	public void setRootNode(String rn) {
		rootNode = rn;
	}

	@Override
	public String asString() {
		return instance.asString();
	}

	@Override
	public void createFile() throws IOException {
		if (StringUtils.isNotBlank(inputFile.getPath())) {
			instance.createFile();
		}

	}

	@Override
	public void readData(File srcFile) throws IOException {
		instance.readData(srcFile);
	}

	@Override
	public void readData(Filter filter) throws IOException {
		instance.readData(filter);
	}

	@Override
	public void writeData(String srcFile) throws IOException {
		instance.writeData(srcFile);
	}

	// --------------------------------------------------------------------
	// Facading methods that call either the tabular or tree format methods
	// --------------------------------------------------------------------

	public void add(String name, String value, Filter filter) {
		if (isTabular()) {
			tabInstance().addColumn(name);
		} else if (isTree()) {
			treeInstance().add(name, value, filter);
		}
	}

	public void delete(String params, String attrName, String attrValue, Filter fltr) {
		if (isTabular()) {
			tabInstance().deleteValue(params);
		} else if (isTree()) {
			treeInstance().delete(params, attrName, attrValue, fltr);
		}
	}

	public String[] get(String parameterName) {
		return get(parameterName, new Filter());
	}

	public String[] get(String parameterName, Filter fltr) {
		String[] ret = new String[0];
		if (isTabular()) {
			ret = tabInstance().getColumn(parameterName, fltr);
		} else if (isTree()) {
			ret = treeInstance().get(parameterName, fltr);
		}
		return ret;
	}

	public void set(String parameterName, String value) {
		set(parameterName, value, new Filter(), false);
	}

	public void set(String parameterName, String value, Filter filter) {
		set(parameterName, value, filter, false);
	}

	public void set(String parameterName, String value, Filter fltr, boolean allOccurences) {
		if (isTabular()) {
			tabInstance().setValues(parameterName, value, fltr, allOccurences);
		} else if (isTree()) {
			treeInstance().set(parameterName, value, fltr, allOccurences);
		}
	}
}
