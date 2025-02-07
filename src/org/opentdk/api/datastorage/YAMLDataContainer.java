package org.opentdk.api.datastorage;

import org.opentdk.api.exception.DataContainerException;
import org.opentdk.api.filter.Filter;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

/**
 * Specific data container class for the YAML format. The storage object for the source is the
 * {@link Yaml} class to read from an {@link InputStream} or a YAML file.
 * All read and write methods use the {@link JSONDataContainer}, because both the
 * {@link Yaml} and the {@link org.json.JSONObject} support a method to provide
 * the content as map.
 * 
 * @author FME (LK Test Solutions)
 * @see DataContainer
 */
public class YAMLDataContainer implements SpecificContainer {
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
	private Map<String, Object> content;

	public static YAMLDataContainer newInstance() {		
		return new YAMLDataContainer();
	}
	
	private YAMLDataContainer() {
		yaml = new Yaml();
		json = JSONDataContainer.newInstance();		
	}

	@Override
	public String asString() {
		return yaml.dump(yaml.dumpAsMap(json.getJsonAsMap()));
	}

	@Override
	public String asString(EContainerFormat format) {
		return switch (format) {
			case JSON -> json.asString();
			case YAML -> asString();
			default -> throw new IllegalArgumentException("No EContainerFormat detected");
		};
	}

	@Override
	public void readData(Path sourceFile) throws IOException {
		content = yaml.load(Files.readString(sourceFile));
		if (content == null) {
			throw new DataContainerException("YAML object is not initialized or empty ==> No YAML content to read");
		} else {
			json.setJsonWithMap(content);
		}
	}

	@Override
	public void readData(InputStream stream) throws IOException {
		content = yaml.load(stream);
		if (content == null) {
			throw new DataContainerException("YAML object is not initialized or empty ==> No YAML content to read");
		} else {
			json.setJsonWithMap(content);
		}
	}

	@Override
	public void writeData(Path outputFile) throws IOException {
		yaml.dump(yaml.dumpAsMap(json.getJsonAsMap()), new FileWriter(outputFile.toFile()));
	}

	// The following methods just link to the JSONDataContainer

	public void add(String name, String value) {
		json.add(name, value);
	}

	public void add(String name, String value, Filter filter) {
		json.add(name, value, filter);
	}

	public void delete(String name, Filter filter) {
		json.delete(name, filter);
	}

	public String[] get(String name) {
		return json.get(name);
	}

	public String[] get(String headerName, Filter fltr) {
		return json.get(headerName, fltr);
	}

	public void set(String name, String value) {
		json.set(name, value);
	}

	public void set(String name, String value, Filter filter) {
		json.set(name, value, filter);
	}

}
