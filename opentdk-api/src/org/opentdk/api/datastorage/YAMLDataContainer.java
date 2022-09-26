package org.opentdk.api.datastorage;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;

import org.opentdk.api.datastorage.BaseContainer.EContainerFormat;
import org.opentdk.api.io.FileUtil;
import org.opentdk.api.logger.MLogger;
import org.yaml.snakeyaml.Yaml;

/**
 * Specific data container class for the YAML format. The storage object for the source is the
 * {@link org.yaml.snakeyaml.Yaml} class to read from an {@link java.io.InputStream} or a YAML file.
 * All read and write methods use the {@link JSONDataContainer}, because both the
 * {@link org.yaml.snakeyaml.Yaml} and the {@link org.json.JSONObject} support a method to provide
 * the content as map.
 * 
 * @author LK Test Solutions
 * @see org.opentdk.api.datastorage.DataContainer
 */
public class YAMLDataContainer implements CustomContainer {
	/**
	 * Container object for the YAML data. Supports several read and write methods. Gets initialized in
	 * the constructor.
	 */
	private final Yaml yaml;
	/**
	 * Gets used instead of programming own YAMLDataContainer methods because the formats are
	 * transformable.
	 */
	private final JSONDataContainer json;
	/**
	 * Stores the content of the YAML source. Gets used to initialize the {@link JSONDataContainer}.
	 */
	private final Map<String, Object> content;

	/**
	 * Construct a new specific container for YAML files. This gets done by the {@link DataContainer}
	 * class in its adaptContainer method. After initialization, it gets passed to this class. 
	 * This class has no {@link DataContainer} field because it points to the {@link JSONDataContainer}.
	 *
	 * @param dCont gets used to initialize the {@link JSONDataContainer}
	 */
	YAMLDataContainer(DataContainer dCont) {
		yaml = new Yaml();
		json = new JSONDataContainer(dCont);

		if (!dCont.getFileName().isEmpty()) {
			content = yaml.load(FileUtil.getContent(dCont.getFileName()));
		} else if (dCont.getInputStream() != null) {
			content = yaml.load(dCont.getInputStream());
		} else {
			content = null;
		}
	}

	@Override
	public void readData(Filter filter) {
		if (content == null) {
			MLogger.getInstance().log(Level.WARNING, "YAML object is not initialized or empty ==> No YAML content to read", getClass().getSimpleName(), "readData");
		} else {
			json.setJsonWithMap(content);
		}
	}

	@Override
	public void writeData(String srcFileName) throws IOException {
		yaml.dump(yaml.dumpAsMap(json.getJsonAsMap()), new FileWriter(srcFileName));
	}

	@Override
	public Object[] getColumn(String headerName, Filter fltr) {
		return json.getColumn(headerName, fltr);
	}

	@Override
	public void addField(String headerName, String value, Filter fltr) {
		this.addField(headerName, "", value, fltr);
	}

	@Override
	public void addField(String headerName, String fieldName, String newFieldValue, Filter fltr) {
		this.addField(headerName, fieldName, "", newFieldValue, fltr);
	}

	@Override
	public void addField(String headerName, String fieldName, String oldFieldValue, String newFieldValue, Filter fltr) {
		json.addField(headerName, newFieldValue, fltr);
	}

	@Override
	public void deleteField(String headerName, String fieldName, String fieldValue, Filter fltr) {
		json.deleteField(headerName, fieldName, fieldValue, fltr);
	}

	@Override
	public void setFieldValues(String headerName, int[] occurences, String value, Filter fltr) {
		json.setFieldValues(headerName, occurences, value, fltr);
	}

	@Override
	public String asString() {
		return yaml.dump(yaml.dumpAsMap(json.getJsonAsMap()));
	}
	
	@Override
	public String asString(EContainerFormat format) {
		switch(format) {
		case JSON:
			return json.asString();
		case YAML:
			MLogger.getInstance().log(Level.INFO, "Format already is YAML. Call asString", getClass().getSimpleName(), "asString(format)");
			return asString();
		default:
			MLogger.getInstance().log(Level.WARNING, "Format not supported to export from YAML", getClass().getSimpleName(), "asString(format)");
			return "";
		}
	}
	
}
