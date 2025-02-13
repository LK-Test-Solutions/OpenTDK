package org.opentdk.api.datastorage;

import lombok.Getter;
import lombok.Setter;
import org.opentdk.api.exception.DataContainerException;
import org.opentdk.api.filter.Filter;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.yaml.snakeyaml.Yaml;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPathExpressionException;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
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
 * @author FME (LK Test Solutions)
 */
public class DataContainer implements SpecificContainer {

	/**
	 * The object that will be adapted to a specific data container like
	 * {@link XMLDataContainer} or {@link CSVDataContainer}. The caller does
	 * not need to know about the adaption process.
	 */
	private SpecificContainer instance;

	@Getter
    @Setter
    private Path inputFile;

	@Getter
    @Setter
    private InputStream inputStream;

	@Getter
    @Setter
    private Filter filter = new Filter();

	/**
	 * Property that stores the container format for the instance of the
	 * {@link DataContainer} as an enumeration of type {@link EContainerFormat}.
     */
	@Getter
    private EContainerFormat containerFormat;

	/**
	 * The non argument constructor get used to have an empty container instance
	 * without a connected file, stream or result set and without knowing the
	 * container format.
	 */
	public static DataContainer newContainer() {
		return new DataContainer();
	}

	private DataContainer() {
		try {
			instance = adaptContainer();
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}

	/**
	 * @return CSV instance directly
	 */
	public static DataContainer newCSVContainer() {
		return new DataContainer(EContainerFormat.CSV);
	}

	/**
	 * @return XML instance directly
	 */
	public static DataContainer newXMLContainer() {
		return new DataContainer(EContainerFormat.XML);
	}

	/**
	 * @return JSON instance directly
	 */
	public static DataContainer newJSONContainer() {
		return new DataContainer(EContainerFormat.JSON);
	}

	/**
	 * @return YAML instance directly
	 */
	public static DataContainer newYAMLContainer() {
		return new DataContainer(EContainerFormat.YAML);
	}

	/**
	 * Gets used if the exact type is known but without any present data. 
	 * In this case no adaption process is necessary, because the format gets explicitly set.
	 *
	 * @param type {@link EContainerFormat}
	 */
	public static DataContainer newContainer(EContainerFormat type) {
		return new DataContainer(type);
	}

	private DataContainer(EContainerFormat type) {
		containerFormat = type;
		switch (type) {
			case CSV -> instance = CSVDataContainer.newInstance();
			case XML -> {
				try {
					instance = XMLDataContainer.newInstance();
				} catch (ParserConfigurationException | IOException | SAXException e) {
					throw new DataContainerException(e);
				}
			}
			case JSON -> instance = JSONDataContainer.newInstance();
			case YAML -> instance = YAMLDataContainer.newInstance();
			default -> throw new IllegalStateException("Unexpected value: " + type);
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
		return new DataContainer(Paths.get(sourcePath));
	}

	/**
	 * Constructor that is called with the source file object to store data from.
	 * 
	 * @param sourceFile valid java.io.File object
	 * @return Data container instance depending on the file type
	 */
	public static DataContainer newContainer(Path sourceFile) {
		return new DataContainer(sourceFile);
	}

	private DataContainer(Path sourceFile) {
		inputFile = sourceFile;
		try {
			instance = adaptContainer();
			if(Files.exists(sourceFile) && Files.isRegularFile(sourceFile) && Files.size(sourceFile) > 0) {
				instance.readData(sourceFile);
			}
		} catch (IOException e) {
			throw new DataContainerException(e);
		}
	}

	public static DataContainer newContainer(InputStream inStream) {
		return new DataContainer(inStream);
	}

	/**
	 * Constructor that is called when no input file exists and the container for
	 * the specific data format also accepts {@link InputStream} for the
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
	 * @param inStream Object of type {@link InputStream}
	 */
	private DataContainer(InputStream inStream) {
		inputStream = inStream;
		try {
			instance = adaptContainer();
			instance.readData(inputStream);
		} catch (IOException e) {
			throw new DataContainerException(e);
		}
	}

	/**
	 * This method adapts the one {@link SpecificContainer} instance to a specific
	 * {@link DataContainer} depending on the source file ending or existence of a
	 * stream. It calls {@link #readData(Path)} at the end if there is a source.
	 * In case of an empty container the read operation has to be triggered
	 * separately.
	 */
	private SpecificContainer adaptContainer() throws IOException {
		return switch (detectDataFormat()) {
			case CSV -> CSVDataContainer.newInstance();
			case XML -> {
				try {
					yield XMLDataContainer.newInstance();
				} catch (ParserConfigurationException | IOException | SAXException e) {
					throw new DataContainerException(e);
				}
			}
			case JSON -> JSONDataContainer.newInstance();
			case YAML -> YAMLDataContainer.newInstance();
        };
	}

	/**
	 * Analyzes the file extension and/or structure of the data content and an
	 * enumeration of type {@link EContainerFormat} which defines the type of
	 * container that needs to be adapted.
	 *
	 * @return enumeration of type {@link EContainerFormat}
	 */
	private EContainerFormat detectDataFormat() throws IOException {
		if (inputStream != null && inputStream.available() > 0) {
			try(Stream<String> streamOfString = new BufferedReader(new InputStreamReader(inputStream)).lines()) {
				String inputContent = streamOfString.collect(Collectors.joining());

				if (inputContent.startsWith("<")) {
					// Check due to performance reasons
					if (validateXMLString(inputContent)) {
						containerFormat = EContainerFormat.XML;
					}
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
				// Stream gets passed to specific container after this operation and has to be reset
				if(inputStream.available() == 0) {
					inputStream.reset();
				}
			}
		} else if (inputFile != null) {
			String fileName = inputFile.toFile().getName();
			if (StringUtils.isNotBlank(fileName)) {
				if (fileName.endsWith(".txt")) {
					containerFormat = EContainerFormat.CSV;
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
		if (instance instanceof CSVDataContainer) {
			return (CSVDataContainer) instance;
		} else {
			throw new NullPointerException("TabularContainer not initialized");
		}
	}
	
	public XMLDataContainer xmlInstance() {
		if (instance instanceof XMLDataContainer) {
			return (XMLDataContainer) instance;
		} else {
			throw new NullPointerException("XMLContainer not initialized");
		}
	} 
	
	public JSONDataContainer jsonInstance() {
		if (instance instanceof JSONDataContainer) {
			return (JSONDataContainer) instance;
		} else {
			throw new NullPointerException("XMLContainer not initialized");
		}
	} 
	
	public YAMLDataContainer yamlInstance() {
		if (instance instanceof YAMLDataContainer) {
			return (YAMLDataContainer) instance;
		} else {
			throw new NullPointerException("XMLContainer not initialized");
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
        return instance instanceof CSVDataContainer;
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

	public boolean isXML() {
		return instance instanceof XMLDataContainer;
	}
	
	public boolean isJSON() {
		return instance instanceof JSONDataContainer;
	}
	
	public boolean isYAML() {
		return instance instanceof YAMLDataContainer;
	}
	
	private void checkInstance() {
		if(!isTree() && !isTabular()) {
			throw new RuntimeException("DataContainer#instance not initialized");
		} 
	}
	
	public void createFile() throws IOException {
		if (inputFile != null) {
			Files.createFile(inputFile);
		}
	}

	@Deprecated
	public Boolean validateXMLString(String inString) {
		try {
			InputStream inStream = new ByteArrayInputStream(inString.getBytes(StandardCharsets.UTF_8));
			SAXParserFactory.newInstance().newSAXParser().getXMLReader().parse(new InputSource(inStream));
		} catch (IOException e) {
			// log.warn("Invalid input used for XML parser");
			return false;
		} catch (ParserConfigurationException e) {
			// log.warn("Probably non-supported feature used for the XML processor");
			return false;
		} catch (SAXException e) {
			// log.warn("A DOCTYPE was passed into the XML document");
			return false;
		}
		return true;
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
	public void readData(Path inputFile) throws IOException {
		instance.readData(inputFile);
	}

	@Override
	public void readData(InputStream stream) throws IOException {
		instance.readData(stream);
	}

	@Override
	public void writeData(Path outputFile) throws IOException {
		instance.writeData(outputFile);
	}

	// --------------------------------------------------------------------
	// Facade methods that call either the tabular or tree format methods
	// --------------------------------------------------------------------

	public void add(String name, String value, Filter filter) {
		checkInstance();
		if (isTabular()) {
			throw new IllegalStateException("Facade methods not supported for tabular container");
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
			throw new IllegalStateException("Facade methods not supported for tabular container");
		} else if (isTree()) {
			if(isXML()) {
				try {
					xmlInstance().delete(params, attrName, attrValue, fltr);
				} catch (IOException | TransformerException | XPathExpressionException e) {
					throw new DataContainerException(e);
				}
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
			throw new IllegalStateException("Facade methods not supported for tabular container");
		} else if (isTree()) {
			if(isXML()) {
				try {
					ret = xmlInstance().get(parameterName, fltr);
				} catch (XPathExpressionException e) {
					throw new DataContainerException(e);
				}
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
			throw new IllegalStateException("Facade methods not supported for tabular container");
		} else if (isTree()) {
			if(isXML()) {
				try {
					xmlInstance().set(parameterName, value, fltr, allOccurences);
				} catch (IOException | TransformerException | XPathExpressionException e) {
					throw new DataContainerException(e);
				}
			} else if (isJSON()) {
				jsonInstance().set(parameterName, value, fltr);
			} else if(isYAML()) {
				yamlInstance().set(parameterName, value, fltr);
			}	
		}
	}

}
