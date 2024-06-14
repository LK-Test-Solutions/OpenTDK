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
import org.opentdk.api.io.FileUtil;
import org.opentdk.api.io.XMLEditor;
import org.opentdk.api.logger.MLogger;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.nio.file.Files;
import java.sql.ResultSet;
import java.util.Map;
import java.util.logging.Level;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * This class gets used to store data from different sources at runtime of an
 * application. The class provides methods to put data from different sources
 * into an instance of a specific container class, as well as getter and setter
 * methods to read data from, and write data into the object.<br>
 * <br>
 * This will allow to build applications with standardized data interface with
 * minimum maintenance effort in case that the data source changes.
 *
 * @author LK Test Solutions
 */
public class DataContainer implements SpecificContainer {

	/**
	 * The object that will be adapted to a specific data container like
	 * {@link XMLDataContainer} or {@link PropertiesDataContainer}. The caller does
	 * not need to know about the adaption process.
	 */
	private final SpecificContainer instance;

	/**
	 * This property is used to assign the data as an {@link java.io.File} to the
	 * {@link DataContainer} instance.
	 */
	private File inputFile;

	/**
	 * This property is used to assign the data as an {@link java.io.InputStream} to
	 * the {@link DataContainer} instance in case that no source file exists.
	 */
	private InputStream inputStream;

	/**
	 * This property is used to assign the data as an {@link java.sql.ResultSet} to
	 * the {@link DataContainer} instance in case that no source file exists.
	 */
	private ResultSet resultSet;

	/**
	 * The filter object that does not filter the data in the {@link DataContainer}
	 * during runtime but when reading from and writing to the source file.
	 */
	private Filter filter = new Filter();

	/**
	 * Property that stores the container format for the instance of the
	 * {@link DataContainer} as an enumeration of type {@link EContainerFormat}.
	 */
	private EContainerFormat containerFormat = EContainerFormat.TEXT;

	/**
	 * The non argument constructor get used to have an empty container instance
	 * without a connected file, stream or result set and without knowing the
	 * container format. In this case all the fields keep their default value and
	 * the container gets initialized as {@link TextDataContainer}.
	 */
	public static DataContainer newContainer() {
		return new DataContainer();
	}

	private DataContainer() {
		instance = adaptContainer();
	}
	
	/**
	 * Gets used if the exact type is known but without any present data. 
	 * In this case no adaption process is necessary, because the format gets explicitly set.
	 *
	 * @param type {@link EHeader}
	 */
	public static DataContainer newContainer(EContainerFormat type) {
		return new DataContainer(type);
	}

	private DataContainer(EContainerFormat type) {
		containerFormat = type;
		switch (type) {
		case CSV:
			instance = CSVDataContainer.newInstance();
			break;
		case PROPERTIES:
			instance = PropertiesDataContainer.newInstance();
			break;
		case RESULTSET:
			instance = RSDataContainer.newInstance();
			break;
		case XML:
			instance = XMLDataContainer.newInstance();
			break;
		case JSON:
			instance = JSONDataContainer.newInstance();
			break;
		case YAML:
			instance = YAMLDataContainer.newInstance();
			break;
		default:
			instance = TextDataContainer.newInstance();	
		}		
	}

	/**
	 * Gets used to initialize from a source path string that gets committed to a
	 * file instance.
	 * 
	 * @param sourcePath {@link #inputFile}
	 * @return Data container instance depending on the file type
	 */
	public static DataContainer newContainer(String sourcePath) {
		return new DataContainer(new File(sourcePath));
	}

	/**
	 * Constructor that is called with the source file object to store data from.
	 * 
	 * @param sourceFile valid java.io.File object
	 * @return Data container instance depending on the file type
	 */
	public static DataContainer newContainer(File sourceFile) {
		return new DataContainer(sourceFile);
	}

	private DataContainer(File sourceFile) {
		inputFile = sourceFile;
		instance = adaptContainer();
		try {
			if(sourceFile.exists() && sourceFile.isFile() && Files.size(sourceFile.toPath()) > 0) {
				instance.readData(sourceFile);
			}
		} catch (IOException e) {
			MLogger.getInstance().log(Level.SEVERE, e);
		}
	}

	public static DataContainer newContainer(InputStream inStream) {
		return new DataContainer(inStream);
	}

	/**
	 * Constructor that is called when no input file exists and the container for
	 * the specific data format also accepts {@link java.io.InputStream} for the
	 * data.<br>
	 * e.g.: An application dynamically receives data by calling RestAPI Service
	 * Requests and the XML responses will be passed to the
	 * BaseDispatcher that creates an
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
	private DataContainer(InputStream inStream) {
		inputStream = inStream;
		instance = adaptContainer();
		try {
			instance.readData(inStream);
		} catch (IOException e) {
			MLogger.getInstance().log(Level.SEVERE, e);
		}
	}

	/**
	 * Constructor that is called, when importing data from a database request to
	 * the container. No filter will be used and the column delimiter for the
	 * imported data is the semicolon.
	 *
	 * @param rs The result of the database request.
	 */
	public static DataContainer newContainer(ResultSet rs) {
		return new DataContainer(rs);
	}

	private DataContainer(ResultSet rs) {
		resultSet = rs;
		instance = adaptContainer();
		try {
			rsInstance().readData(rs);
		} catch (IOException e) {
			MLogger.getInstance().log(Level.SEVERE, e);
		}
	}

	/**
	 * This method adapts the one {@link SpecificContainer} instance to a specific
	 * {@link DataContainer} depending on the source file ending or existence of a
	 * stream. It calls {@link #readData(File)} at the end if there is a source.
	 * In case of an empty container the read operation has to be triggered
	 * separately.
	 *
	 * @throws IOException to handle I/O methods when the {@link #readData(File)}
	 *                     method failed
	 */
	private SpecificContainer adaptContainer() {
		switch (detectDataFormat()) {
		case CSV:
			return CSVDataContainer.newInstance();
		case PROPERTIES:
			return PropertiesDataContainer.newInstance();
		case RESULTSET:
			return RSDataContainer.newInstance();
		case XML:
			return XMLDataContainer.newInstance(); 
		case JSON:
			return JSONDataContainer.newInstance();
		case YAML:
			return YAMLDataContainer.newInstance();
		default:
			return TextDataContainer.newInstance();
		}
	}

	/**
	 * Analyzes the file extension and/or structure of the data content and an
	 * enumeration of type {@link EContainerFormat} which defines the type of
	 * container that needs to be adapted.
	 *
	 * @return enumeration of type {@link EContainerFormat}
	 */
	private EContainerFormat detectDataFormat() {
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
		return containerFormat;
	}

	public CSVDataContainer tabInstance() {
		if (instance instanceof PropertiesDataContainer) {
			return (PropertiesDataContainer) instance;
		} else if (instance instanceof RSDataContainer) {
			return (RSDataContainer) instance;
		} else if (instance instanceof CSVDataContainer) {
			return (CSVDataContainer) instance;
		} else {
			throw new NullPointerException("TabularContainer not intialized");
		}
	}
	
	public RSDataContainer rsInstance() {
		if (instance instanceof RSDataContainer) {
			return (RSDataContainer) instance;
		} else {
			throw new NullPointerException("RSDataContainer not intialized");
		}
	} 
	
	public XMLDataContainer xmlInstance() {
		if (instance instanceof XMLDataContainer) {
			return (XMLDataContainer) instance;
		} else {
			throw new NullPointerException("XMLContainer not intialized");
		}
	} 
	
	public JSONDataContainer jsonInstance() {
		if (instance instanceof JSONDataContainer) {
			return (JSONDataContainer) instance;
		} else {
			throw new NullPointerException("XMLContainer not intialized");
		}
	} 
	
	public YAMLDataContainer yamlInstance() {
		if (instance instanceof YAMLDataContainer) {
			return (YAMLDataContainer) instance;
		} else {
			throw new NullPointerException("XMLContainer not intialized");
		}
	} 
	
	public TextDataContainer textInstance() {
		if (instance instanceof TextDataContainer) {
			return (TextDataContainer) instance;
		} else {
			throw new NullPointerException("TextContainer not intialized");
		}
	}

	/**
	 * Checks if the {@link #instance} was initialized as one of the tabular format
	 * e.g. CSV or RESULSET. This can be done before calling {@link #tabInstance()}
	 * to be sure that a tabular format is available. CSV format is default for
	 * TabularContainer and gets checked at the end.
	 *
	 * @return true: is tree format, false otherwise
	 */
	public boolean isTabular() {
		boolean ret = false;
		if (instance instanceof PropertiesDataContainer) {
			ret = true;
		} else if (instance instanceof RSDataContainer) {
			ret = true;
		} else if (instance instanceof CSVDataContainer) {
			ret = true;
		}
		return ret;
	}

	/**
	 * Checks if the {@link #instance} was initialized as one of the tree formats
	 * e.g. XML, JSON or YAML. 
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
	
	public boolean isText() {
		boolean ret = false;
		if (instance instanceof TextDataContainer) {
			ret = true;
		} 
		return ret;
	}
	
	public boolean isXML() {
		boolean ret = false;
		if (instance instanceof XMLDataContainer) {
			ret = true;
		} 
		return ret;
	}
	
	public boolean isJSON() {
		boolean ret = false;
		if (instance instanceof JSONDataContainer) {
			ret = true;
		} 
		return ret;
	}
	
	public boolean isYAML() {
		boolean ret = false;
		if (instance instanceof YAMLDataContainer) {
			ret = true;
		} 
		return ret;
	}
	
	private void checkInstance() {
		if(!isTree() && !isTabular()) {
			throw new RuntimeException("DataContainer#instance not initialized");
		} 
	}
	
	public void createFile() throws IOException {
		if (inputFile != null && StringUtils.isNotBlank(inputFile.getPath())) {
			if(instance instanceof XMLDataContainer) {
				xmlInstance().readData(inputFile);
			} else {
				FileUtil.createFile(inputFile, true);
			}
		} else {
			MLogger.getInstance().log(Level.WARNING, "DataContainer.createFile() was called, but inputFile is null.");
		}
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
	 * @return {@link #inputStream}
	 */
	public InputStream getInputStream() {
		return inputStream;
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
	 * Sets content of type {@link java.io.InputStream} to the {@link #inputStream}
	 * property that is used by DataContainers that have document type data
	 * connected like {@link XMLDataContainer} using {@link org.w3c.dom.Document}
	 * for XML and HTML formats. The {@link #inputStream} property can be used in
	 * case that the data source is not a file.
	 *
	 * @param inStream {@link #inputStream}
	 */
	public void setInputStream(InputStream inStream) {
		inputStream = inStream;
	}

	/**
	 * Assigns an object of type {@link java.sql.ResultSet} with SQL results to the
	 * property {@link #resultSet}. This property is used by the {@link RSDataContainer}.
	 *
	 * @param rs {@link #resultSet}
	 */
	public void setResultSet(ResultSet rs) {
		resultSet = rs;
	}
	
	/**
	 * @return {@link #containerFormat}
	 */
	public EContainerFormat getContainerFormat() {
		return containerFormat;
	}
	
	// --------------------------------------------------------------------
	// Inherited methods that just link to the instance
	// --------------------------------------------------------------------

	@Override
	public String asString() {
		return instance.asString();
	}

	@Override
	public String asString(EContainerFormat format) {
		return instance.asString(format);
	}

	@Override
	public void readData(File srcFile) throws IOException {
		instance.readData(srcFile);
	}

	@Override
	public void readData(InputStream stream) throws IOException {
		instance.readData(stream);
	}
	
	@Override
	public void readData(File sourceFile, Filter filter) throws IOException {
		instance.readData(sourceFile, filter);
	}

	@Override
	public void readData(InputStream stream, Filter filter) throws IOException {
		instance.readData(stream, filter);	
	}

	@Override
	public void writeData(File outputFile) throws IOException {
		instance.writeData(outputFile);
	}

	// --------------------------------------------------------------------
	// Facade methods that call either the tabular or tree format methods
	// --------------------------------------------------------------------

	public void add(String name, String value, Filter filter) {
		checkInstance();
		if (isTabular()) {
			tabInstance().addColumn(name);
		} else if (isTree()) {
			if(isXML()) {
				xmlInstance().add(name, value, filter);
			} else if (isJSON()) {
				jsonInstance().add(name, value, filter);
			} else if(isYAML()) {
				yamlInstance().add(name, value, filter);
			}
		} 
	}

	public void delete(String params, String attrName, String attrValue, Filter fltr) {
		checkInstance();
		if (isTabular()) {
			tabInstance().deleteValue(params);
		} else if (isTree()) {
			if(isXML()) {
				xmlInstance().delete(params, attrName, attrValue, fltr);
			} else if (isJSON()) {
				jsonInstance().delete(params, fltr);
			} else if(isYAML()) {
				yamlInstance().delete(params, fltr);
			}
		} 
	}

	public String[] get(String parameterName) {
		return get(parameterName, new Filter());
	}

	public String[] get(String parameterName, Filter fltr) {
		String[] ret = new String[0];
		checkInstance();
		if (isTabular()) {
			ret = tabInstance().getColumn(parameterName, fltr);
		} else if (isTree()) {
			if(isXML()) {
				ret = xmlInstance().get(parameterName, fltr);
			} else if (isJSON()) {
				ret = jsonInstance().get(parameterName, fltr);
			} else if(isYAML()) {
				ret = yamlInstance().get(parameterName, fltr);
			}		
			for (int i = 0; i < ret.length; i++) {
				ret[i] = ret[i].trim();
			}
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
		checkInstance();
		if (isTabular()) {
			tabInstance().setValues(parameterName, value, fltr, allOccurences);
		} else if (isTree()) {
			if(isXML()) {
				xmlInstance().set(parameterName, value, fltr, allOccurences);
			} else if (isJSON()) {
				jsonInstance().set(parameterName, value, fltr);
			} else if(isYAML()) {
				yamlInstance().set(parameterName, value, fltr);
			}	
		}
	}

}
