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
 * The DataContainer class provides a standardized data container interface and supports multiple
 * data formats including CSV, XML, JSON, and YAML. It acts as a data structure primarily for
 * interacting with data from various sources like files, streams, or external inputs. The class
 * offers factory methods for creation and utility methods for data processing and manipulation.
 */
public class DataContainer implements SpecificContainer {

    /**
     * Represents an instance of a specific container implementation within the
     * {@link DataContainer}. Facilitates the handling of data in different formats by adapting to a
     * specific container type. This instance is used to delegate operations related to reading,
     * writing, and data manipulation to the appropriate container implementation.
     */
    private SpecificContainer instance;

    /**
     * Represents the file path of the input data source. This variable holds a {@link Path} object
     * pointing to the file that serves as input for reading or processing operations. Typically
     * used to load and manipulate data from the specified file location within the context of a
     * container.
     */
    @Getter
    @Setter
    private Path inputFile;

    /**
     * Represents an {@link InputStream} that serves as the source of data for the container. It is
     * used to read data into the associated {@code DataContainer}. This stream acts as the primary
     * input mechanism for creating or populating the container with data in various formats such as
     * CSV, XML, JSON, or YAML.
     * <p>
     * The {@code inputStream} can be utilized by methods to process and parse data from the stream
     * based
     */
    @Getter
    @Setter
    private InputStream inputStream;

    /**
     * Represents a filter object used for defining specific conditions or criteria that may be
     * applied to data operations within the DataContainer. The filter is commonly utilized in
     * scenarios where selective data retrieval, modification, or deletion is required.
     * <p>
     * This field is initialized with a new instance of the Filter class and is accessible or
     * modifiable through the provided getter and setter methods.
     */
    @Getter
    @Setter
    private Filter filter = new Filter();

    /**
     * Represents the format of the container used for data handling. The {@code containerFormat}
     * field is of type {@link EContainerFormat}, which enumerates supported data storage formats
     * such as CSV, XML, JSON, and YAML. This field determines the structure and behavior of the
     * data container.
     */
    @Getter
    private EContainerFormat containerFormat;

    /**
     * Creates and returns a new instance of the {@code DataContainer} class with default settings.
     *
     * @return A new {@code DataContainer} instance with default initialization.
     */
    public static DataContainer newContainer() {
        return new DataContainer();
    }


    /**
     * Private constructor for the DataContainer class. This constructor initializes the singleton
     * instance by calling the adaptContainer method. The method catches any IOException that occurs
     * during this process and wraps it in an UncheckedIOException, which is then thrown.
     * <p>
     * This constructor enforces the singleton pattern by ensuring that the class cannot be
     * instantiated directly from outside the class.
     * <p>
     * Throws: UncheckedIOException - if an IOException occurs while adapting the container.
     */
    private DataContainer() {
        try {
            instance = adaptContainer();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    /**
     * Creates a new instance of a DataContainer configured with the CSV container format.
     *
     * @return A new DataContainer instance configured to work with the CSV format.
     */
    public static DataContainer newCSVContainer() {
        return new DataContainer(EContainerFormat.CSV);
    }

    /**
     * Creates a new instance of {@code DataContainer} configured to use the XML format.
     *
     * @return a {@code DataContainer} instance set to the {@code EContainerFormat.XML} format
     */
    public static DataContainer newXMLContainer() {
        return new DataContainer(EContainerFormat.XML);
    }

    /**
     * Creates a new DataContainer instance with the container format set to JSON.
     *
     * @return A new instance of DataContainer configured for JSON format.
     */
    public static DataContainer newJSONContainer() {
        return new DataContainer(EContainerFormat.JSON);
    }

    /**
     * Creates a new instance of the {@code DataContainer} class with the format set to YAML.
     *
     * @return A new {@code DataContainer} instance configured to handle YAML data format.
     */
    public static DataContainer newYAMLContainer() {
        return new DataContainer(EContainerFormat.YAML);
    }

    /**
     * Creates a new instance of DataContainer with the specified container format.
     *
     * @param type the container format type to initialize the DataContainer with. Supported types
     *             are defined in the EContainerFormat enumeration (e.g., CSV, XML, JSON, YAML).
     * @return a new instance of DataContainer initialized with the specified format type.
     */
    public static DataContainer newContainer(EContainerFormat type) {
        return new DataContainer(type);
    }

    /**
     * Constructor for the DataContainer class, used to create an instance based on the specified
     * container format. The format determines the underlying specific container implementation
     * (CSV, XML, JSON, YAML).
     *
     * @param type The desired container format, represented by the EContainerFormat enumeration.
     *             Supported formats include CSV, XML, JSON, and YAML. If an unsupported format is
     *             provided, an IllegalStateException is thrown.
     */
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
     * Creates a new instance of DataContainer using the specified source path.
     *
     * @param sourcePath the file system path to the source to be used for initializing the
     *                   DataContainer
     * @return a new DataContainer instance initialized with the specified source path
     */
    public static DataContainer newContainer(String sourcePath) {
        return new DataContainer(Paths.get(sourcePath));
    }

    /**
     * Creates a new instance of the DataContainer class based on the specified source file.
     *
     * @param sourceFile the path to the source file from which the DataContainer will be created
     * @return a new DataContainer instance associated with the provided source
     */
    public static DataContainer newContainer(Path sourceFile) {
        return new DataContainer(sourceFile);
    }

    /**
     * Constructs a DataContainer object by initializing it with the specified source file.
     * Reads and processes the file if it exists, is a regular file, and is non-empty.
     *
     * @param sourceFile the path to the source file that will be used to read and initialize data
     * @throws DataContainerException if an IOException occurs while accessing or processing the file
     */
    private DataContainer(Path sourceFile) {
        inputFile = sourceFile;
        try {
            instance = adaptContainer();
            if (Files.exists(sourceFile) && Files.isRegularFile(sourceFile) && Files.size(sourceFile) > 0) {
                instance.readData(sourceFile);
            }
        } catch (IOException e) {
            throw new DataContainerException(e);
        }
    }

    /**
     * Creates a new instance of DataContainer using the provided InputStream.
     *
     * @param inStream the InputStream to be used for creating the DataContainer
     * @return a new instance of DataContainer initialized with the given InputStream
     */
    public static DataContainer newContainer(InputStream inStream) {
        return new DataContainer(inStream);
    }

    /**
     * Constructs a new DataContainer that initializes an internal instance
     * based on the provided InputStream and reads data from it.
     *
     * @param inStream the InputStream from which the data is read.
     *                 Must not be null.
     * @throws DataContainerException if an I/O error occurs while reading data
     *                                 or initializing the container instance.
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
     * Adapts and returns a specific container instance based on the detected data format. The data
     * format is determined using the {@code detectDataFormat()} method.
     *
     * @return a specific container instance appropriate to the detected data format. This could be
     * an instance of CSVDataContainer, XMLDataContainer, JSONDataContainer, or YAMLDataContainer.
     * @throws IOException            if an I/O error occurs during data format detection or
     *                                container instantiation.
     * @throws DataContainerException if an error occurs while creating an XML container due to
     *                                issues such as parser configuration, invalid content, etc.
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
     * Detects the data format of an input source, which can either be an input stream or an input file.
     * The method analyzes the content of the input stream or the file extension to determine the data format.
     * Supported formats include XML, JSON, YAML, and CSV.
     *
     * @return The detected data format as an {@link EContainerFormat}.
     * @throws IOException If an I/O error occurs while reading the input stream or accessing the input file.
     */
    private EContainerFormat detectDataFormat() throws IOException {
        if (inputStream != null && inputStream.available() > 0) {
            try (Stream<String> streamOfString = new BufferedReader(new InputStreamReader(inputStream)).lines()) {
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
                if (inputStream.available() == 0) {
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

    /**
     * Retrieves the instance of the DataContainer as a CSVDataContainer if the underlying instance
     * is of type CSVDataContainer. If the instance is not initialized, a NullPointerException is
     * thrown.
     *
     * @return the instance of the DataContainer as a CSVDataContainer
     * @throws NullPointerException if the instance is not initialized or is not of type
     *                              CSVDataContainer
     */
    public CSVDataContainer tabInstance() {
        if (instance instanceof CSVDataContainer) {
            return (CSVDataContainer) instance;
        } else {
            throw new NullPointerException("TabularContainer not initialized");
        }
    }

    /**
     * Returns the current instance of XMLDataContainer if it is properly initialized.
     * If the instance is not initialized as an XMLDataContainer, a NullPointerException is thrown.
     *
     * @return the initialized instance of XMLDataContainer
     * @throws NullPointerException if the instance is not initialized as XMLDataContainer
     */
    public XMLDataContainer xmlInstance() {
        if (instance instanceof XMLDataContainer) {
            return (XMLDataContainer) instance;
        } else {
            throw new NullPointerException("XMLContainer not initialized");
        }
    }

    /**
     * Returns the instance as a {@code JSONDataContainer} if it has been initialized properly.
     * If the instance is not of type {@code JSONDataContainer}, an exception will be thrown.
     *
     * @return the current instance cast to {@code JSONDataContainer}
     * @throws NullPointerException if the instance is not properly initialized as {@code JSONDataContainer}
     */
    public JSONDataContainer jsonInstance() {
        if (instance instanceof JSONDataContainer) {
            return (JSONDataContainer) instance;
        } else {
            throw new NullPointerException("XMLContainer not initialized");
        }
    }

    /**
     * Provides an instance of the YAMLDataContainer if the existing instance
     * is of the correct type. Throws an exception if the instance is not initialized
     * or is of an unsupported type.
     *
     * @return the current instance cast as a YAMLDataContainer
     * @throws NullPointerException if the instance is not initialized properly
     */
    public YAMLDataContainer yamlInstance() {
        if (instance instanceof YAMLDataContainer) {
            return (YAMLDataContainer) instance;
        } else {
            throw new NullPointerException("XMLContainer not initialized");
        }
    }

    /**
     * Determines if the current instance represents a tabular data structure.
     *
     * @return true if the instance is of type CSVDataContainer, otherwise false
     */
    public boolean isTabular() {
        return instance instanceof CSVDataContainer;
    }

    /**
     * Determines if the instance variable is a tree-structured data container. Specifically, it
     * identifies the following container types as tree-structured: - XMLDataContainer -
     * JSONDataContainer - YAMLDataContainer
     *
     * @return true if the instance is of type XMLDataContainer, JSONDataContainer, or
     * YAMLDataContainer indicating a tree-structured format, otherwise false.
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
     * Determines if the current instance is of type XMLDataContainer.
     *
     * @return true if the instance is an XMLDataContainer, false otherwise.
     */
    public boolean isXML() {
        return instance instanceof XMLDataContainer;
    }

    /**
     * Checks if the current instance is an instance of JSONDataContainer.
     *
     * @return true if the instance is of type JSONDataContainer, false otherwise
     */
    public boolean isJSON() {
        return instance instanceof JSONDataContainer;
    }

    /**
     * Determines if the current data container instance is of type YAMLDataContainer.
     *
     * @return true if the instance is a YAMLDataContainer, otherwise false.
     */
    public boolean isYAML() {
        return instance instanceof YAMLDataContainer;
    }

    /**
     * Validates that the instance in the DataContainer is properly initialized. Throws a
     * RuntimeException if the instance does not represent a valid data structure.
     * <p>
     * The instance is considered valid if it is either of a tree format (XML, JSON, YAML) or a
     * tabular format (CSV).
     *
     * @throws RuntimeException if the instance is not initialized to a recognized format.
     */
    private void checkInstance() {
        if (!isTree() && !isTabular()) {
            throw new RuntimeException("DataContainer#instance not initialized");
        }
    }

    /**
     * Creates a new file in the filesystem at the location specified by the inputFile field.
     * <p>
     * If the inputFile field is not null, the method attempts to create a new file at the specified
     * path. If a file already exists at the path, an exception will be thrown.
     *
     * @throws IOException if an I/O error occurs or the file cannot be created.
     */
    public void createFile() throws IOException {
        if (inputFile != null) {
            Files.createFile(inputFile);
        }
    }

    /**
     * Validates whether the provided string is well-formed XML.
     *
     * @param inString the string to be validated as XML
     * @return true if the string is valid XML, false otherwise
     */
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

    /**
     * Provides the content of the current instance of the container as a string. Delegates the call
     * to the underlying instance implementation.
     *
     * @return the content of the container as a string.
     */
    @Override
    public String asString() {
        return instance.asString();
    }

    /**
     * Provides the content of the container as a string in the specified container format.
     *
     * @param format the container format in which the content should be converted. Supported
     *               formats are defined in {@code EContainerFormat}.
     * @return the content of the container as a string in the specified format.
     */
    @Override
    public String asString(EContainerFormat format) {
        return instance.asString(format);
    }

    /**
     * Reads data from the specified file path.
     *
     * @param inputFile the path to the input file from which data is to be read
     * @throws IOException if an I/O error occurs while reading the file
     */
    @Override
    public void readData(Path inputFile) throws IOException {
        instance.readData(inputFile);
    }

    /**
     * Reads data from the provided input stream and processes it.
     *
     * @param stream the input stream from which data is to be read
     * @throws IOException if an I/O error occurs during reading
     */
    @Override
    public void readData(InputStream stream) throws IOException {
        instance.readData(stream);
    }

    /**
     * Writes data from the current instance to the specified file path.
     *
     * @param outputFile the path to the output file where
     */
    @Override
    public void writeData(Path outputFile) throws IOException {
        instance.writeData(outputFile);
    }

    // --------------------------------------------------------------------
    // Facade methods that call either the tabular or tree format methods
    // --------------------------------------------------------------------

    /**
     * Adds a name-value pair with an associated filter to the current instance. This method
     * determines the type of the container (e.g., XML, JSON, YAML) and applies the addition
     * accordingly. Throws an exception if the container is tabular.
     *
     * @param name   the name of the element to be added
     * @param value  the value of the element to be added
     * @param filter the filter to be applied to the added element
     * @throws IllegalStateException if the container is tabular and does not support this method
     */
    public void add(String name, String value, Filter filter) {
        checkInstance();
        if (isTabular()) {
            throw new IllegalStateException("Facade methods not supported for tabular container");
        } else if (isTree()) {
            if (isXML()) {
                xmlInstance().add(name, value, filter);
            } else if (isJSON()) {
                jsonInstance().add(name, value, filter);
            } else if (isYAML()) {
                yamlInstance().add(name, value, filter);
            }
        }
    }

    /**
     * Deletes data based on the given parameters, attribute name, and attribute value, as well as
     * the specified filter. This method handles the deletion process based on the type of data
     * container (XML, JSON, YAML) but does not support tabular data containers.
     *
     * @param params    The parameters that determine the scope or context of the deletion.
     * @param attrName  The name of the attribute used to identify the data to be deleted.
     * @param attrValue The value of the attribute used to identify the data to be deleted.
     * @param fltr      The filter criteria to narrow down the deletion scope.
     * @throws IllegalStateException  If the method is invoked on a tabular container.
     * @throws DataContainerException If an error occurs during the deletion process.
     */
    public void delete(String params, String attrName, String attrValue, Filter fltr) {
        checkInstance();
        if (isTabular()) {
            throw new IllegalStateException("Facade methods not supported for tabular container");
        } else if (isTree()) {
            if (isXML()) {
                try {
                    xmlInstance().delete(params, attrName, attrValue, fltr);
                } catch (IOException | TransformerException | XPathExpressionException e) {
                    throw new DataContainerException(e);
                }
            } else if (isJSON()) {
                jsonInstance().delete(params, fltr);
            } else if (isYAML()) {
                yamlInstance().delete(params, fltr);
            }
        }
    }

    /**
     * Retrieves an array of strings based on the specified parameter name and a default filter.
     *
     * @param parameterName the name of the parameter for which the data is to be retrieved
     * @return an array of strings corresponding to the specified parameter name
     */
    public String[] get(String parameterName) {
        return get(parameterName, new Filter());
    }

    /**
     * Retrieves an array of strings based on the specified parameter name and filter. The method
     * processes the input based on the internal structure format (XML, JSON, or YAML).
     *
     * @param parameterName the name of the parameter to retrieve values for
     * @param fltr          the filter criteria to apply when fetching parameter values
     * @return a trimmed array of string values that match the parameter name and filter criteria
     * @throws IllegalStateException  if the container structure is tabular
     * @throws DataContainerException if an error occurs while processing an XML structure
     */
    public String[] get(String parameterName, Filter fltr) {
        String[] ret = new String[0];
        checkInstance();
        if (isTabular()) {
            throw new IllegalStateException("Facade methods not supported for tabular container");
        } else if (isTree()) {
            if (isXML()) {
                try {
                    ret = xmlInstance().get(parameterName, fltr);
                } catch (XPathExpressionException e) {
                    throw new DataContainerException(e);
                }
            } else if (isJSON()) {
                ret = jsonInstance().get(parameterName, fltr);
            } else if (isYAML()) {
                ret = yamlInstance().get(parameterName, fltr);
            }
            for (int i = 0; i < ret.length; i++) {
                ret[i] = ret[i].trim();
            }
        }
        return ret;
    }

    /**
     * Sets a parameter with the provided name and value. This method internally utilizes a default
     * filter and sets the flag to false.
     *
     * @param parameterName the name of the parameter to set
     * @param value         the value to assign to the specified parameter
     */
    public void set(String parameterName, String value) {
        set(parameterName, value, new Filter(), false);
    }

    /**
     * Sets the parameter with the specified name, value, and filter configuration.
     *
     * @param parameterName the name of the parameter to be set
     * @param value         the value to assign to the parameter
     * @param filter        the filter to apply when setting the value
     */
    public void set(String parameterName, String value, Filter filter) {
        set(parameterName, value, filter, false);
    }

    /**
     * Sets the value for the specified parameter based on the provided filter and configuration.
     * Handles different data structures such as XML, JSON, or YAML under specific conditions.
     *
     * @param parameterName the name of the parameter to set
     * @param value         the value to assign to the parameter
     * @param fltr          the filter used to locate the parameter
     * @param allOccurences a flag indicating whether to update all occurrences of the parameter
     * @throws IllegalStateException  if the method is invoked for a tabular container
     * @throws DataContainerException if an error occurs while setting the parameter in an XML
     *                                instance
     */
    public void set(String parameterName, String value, Filter fltr, boolean allOccurences) {
        checkInstance();
        if (isTabular()) {
            throw new IllegalStateException("Facade methods not supported for tabular container");
        } else if (isTree()) {
            if (isXML()) {
                try {
                    xmlInstance().set(parameterName, value, fltr, allOccurences);
                } catch (IOException | TransformerException | XPathExpressionException e) {
                    throw new DataContainerException(e);
                }
            } else if (isJSON()) {
                jsonInstance().set(parameterName, value, fltr);
            } else if (isYAML()) {
                yamlInstance().set(parameterName, value, fltr);
            }
        }
    }

}
