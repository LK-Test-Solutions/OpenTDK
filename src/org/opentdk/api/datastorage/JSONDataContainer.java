
package org.opentdk.api.datastorage;

import org.opentdk.api.exception.DataContainerException;
import org.opentdk.api.filter.Filter;
import org.opentdk.api.filter.FilterRule;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Specific data container class for the JSON format. In its read and write methods the
 * {@link JSONObject} class gets used to handle sources from an {@link InputStream}
 * or a JSON file.
 * 
 * @author FME (LK Test Solutions)
 * @see DataContainer
 */
public class JSONDataContainer implements SpecificContainer {
	/**
	 * Container object for the JSON data. Supports several read and write methods. Gets initialized in
	 * {@link #readData(Path)}.
	 */
	private JSONObject json;
	
	public static JSONDataContainer newInstance() {		
		return new JSONDataContainer();
	}
	
	private JSONDataContainer() {
		
	}
	
	@Override
	public String asString() {
		return json.toString(1);
	}

	@Override
	public String asString(EContainerFormat format) {
		switch(format) {
		case JSON:
			return asString();
		case YAML:
			Yaml yaml = new Yaml();
			return yaml.dump(yaml.dumpAsMap(getJsonAsMap()));
		default:
			throw new UnsupportedOperationException("Format not supported to export from YAML");
		}
	}
	
	@Override
	public void readData(InputStream stream) throws IOException {
		String content = null;
		if (stream != null) {
			try(InputStreamReader inputStreamReader = new InputStreamReader(stream); Stream<String> streamOfString = new BufferedReader(inputStreamReader).lines();) {
				content = streamOfString.collect(Collectors.joining());
			}
		}
		if (content != null) {
			json = new JSONObject(content);
		}
	}
	
	@Override
	public void readData(Path srcFile) throws IOException {
		String content = Files.readString(srcFile);
		if (!content.isBlank()) {
			json = new JSONObject(content);
		}
	}
	
	@Override
	public void writeData(Path srcFile) throws IOException {
		if (json == null || json.isEmpty()) {
			throw new DataContainerException("JSON object is not initialized or empty ==> No JSON content to write");
		} else {
			try {
				Files.createFile(srcFile);
				Files.writeString(srcFile, json.toString(1));
			} catch (JSONException e) {
				throw new DataContainerException(e);
			}
		}
	}
	
	public void add(String name, String value) {
		add(name, value, new Filter());
	}
	
	public void add(String name, String value, Filter filter) {
		if (filter.getFilterRules().isEmpty()) {
			Object newValue = this.getDataType(value);
			json.append(name, newValue);
		}
		for (FilterRule fltrRule : filter.getFilterRules()) {
			if (fltrRule.getHeaderName().equalsIgnoreCase("XPath") && StringUtils.isNotBlank(fltrRule.getValue())) {
				String searchExp = fltrRule.getValue().replace(";", "/") + "/" + name;
				Object newValue = this.getDataType(value);
				Object result = null;

				List<String> keyList = List.of(searchExp.split("/"));
				int i = 0;
				for (String key : keyList) {
					if (i == 0) {
						result = json.get(key);
					} else {
						if (result instanceof JSONObject && i < keyList.size() - 1) {
							result = ((JSONObject) result).get(key);
						} else if (result instanceof JSONArray && i < keyList.size() - 1) {
							result = ((JSONArray) result).get(Integer.parseInt(key));
						} else {
							break;
						}
					}
					i++;
				}
				assert result != null;
				((JSONObject) result).append(name, newValue);
				break;
			}
		}
	}

	public void delete(String name, Filter filter) {
		if (filter.getFilterRules().isEmpty()) {
			json.remove(name);
		}
		for (FilterRule fltrRule : filter.getFilterRules()) {
			if (fltrRule.getHeaderName().equalsIgnoreCase("XPath") && StringUtils.isNotBlank(fltrRule.getValue())) {
				String searchExp = fltrRule.getValue().replace(";", "/") + "/" + name;
				Object result = null;

				List<String> keyList = List.of(searchExp.split("/"));
				int i = 0;
				for (String key : keyList) {
					if (i == 0) {
						result = json.get(key);
					} else {
						if (result instanceof JSONObject && i < keyList.size() - 1) {
							result = ((JSONObject) result).get(key);
						} else if (result instanceof JSONArray && i < keyList.size() - 1) {
							result = ((JSONArray) result).get(Integer.parseInt(key));
						} else {
							break;
						}
					}
					i++;
				}
				assert result != null;
				((JSONObject) result).remove(name);
				break;
			}
		}
	}

	public String[] get(String name) {
		return get(name, new Filter());
	}

	public String[] get(String headerName, Filter fltr) {
		String[] ret;
		List<String> filteredValues = new ArrayList<>();

		if (fltr.getFilterRules().size() > 0) {
			for (FilterRule frImpl : fltr.getFilterRules()) {
				if (frImpl.getHeaderName().equalsIgnoreCase("XPath")) {

					String searchExp = "/" + frImpl.getValue().replace(";", "/") + "/" + headerName;
					Object result = json.query(searchExp);
					JSONObject.testValidity(result);

					if (result == null) {
						throw new JSONException("JSON key not present");
					} else {
						String sResult = result.toString();
						if (result instanceof JSONArray values) {
							for (int i = 0; i < values.length(); i++) {
								filteredValues.add(values.get(i).toString());
							}
						} else {
							filteredValues.add(sResult);
						}
					}
				}
			}
			ret = (String[]) filteredValues.toArray();
		} else {
			Object result = json.get(headerName);
			String sResult = result.toString();

			if (result instanceof JSONArray values) {
				ret = new String[values.length()];
				for (int i = 0; i < values.length(); i++) {
					ret[i] = values.get(i).toString();
				}
			} else {
				ret = new String[] { sResult };
			}
		}
		return ret;
	}

	/**
	 * Retrieves the data type out of the committed value. This can be a JSONObject, JSONArray or any
	 * other primitive data type like string, integer or boolean. E.g. if the input string has leading
	 * and trailing '"' it gets interpreted as JSONString. If not it gets wrapped into a number or
	 * boolean. If it has the syntax '[value1,value2]' it gets wrapped as JSONarray. And if it has the
	 * syntax '{...}' it gets wrapped as JSONObject. A 'null' value is allowed, too, and gets a
	 * JSON.NULL object.
	 * 
	 * @param newValue the value to add or put that should be checked
	 * @return the detected object type depending on the syntax of the committed string
	 */
	private Object getDataType(String newValue) {
		JSONObject.testValidity(newValue);

		Object ret;
		if (newValue.startsWith("\"") && newValue.endsWith("\"")) {
			ret = newValue.substring(newValue.indexOf("\"") + 1, newValue.indexOf("\""));
		} else {
			try {
				ret = new JSONArray(newValue);
			} catch (JSONException e) {
				ret = null;
			}
			if (ret == null) {
				try {
					ret = new JSONObject(newValue);
				} catch (JSONException e) {
					ret = null;
				}
				if (ret == null) {
					ret = JSONObject.stringToValue(newValue);
				}
			}
		}
		return ret;
	}

	/**
	 * @return {@link #json} object as map to access from other containers.
	 */
	Map<String, Object> getJsonAsMap() {
		return json.toMap();
	}
	
	public void set(String name, String value) {
		set(name, value, new Filter());
	}
	
	public void set(String name, String value, Filter filter) {
		if (filter.getFilterRules().isEmpty()) {
			Object newValue = this.getDataType(value);
			json.put(name, newValue);
		}
		for (FilterRule fltrRule : filter.getFilterRules()) {
			if (fltrRule.getHeaderName().equalsIgnoreCase("XPath") && StringUtils.isNotBlank(fltrRule.getValue())) {
				String searchExp = fltrRule.getValue().replace(";", "/") + "/" + name;
				Object newValue = this.getDataType(value);
				Object result = null;

				List<String> keyList = List.of(searchExp.split("/"));
				int i = 0;
				for (String key : keyList) {
					if (i == 0) {
						result = json.get(key);
					} else {
						if (result instanceof JSONObject && i < keyList.size() - 1) {
							result = ((JSONObject) result).get(key);
						} else if (result instanceof JSONArray && i < keyList.size() - 1) {
							result = ((JSONArray) result).get(Integer.parseInt(key));
						} else {
							break;
						}
					}
					i++;
				}
				if (result instanceof JSONArray) {
					((JSONArray) result).put(newValue);
				} else {
					assert result != null;
					((JSONObject) result).put(name, newValue);
				}
				break;
			}
		}
	}

	/**
	 * Possibility to set the content of the {@link #json} object from other containers.
	 * 
	 * @param content map that has the whole content of the JSON source
	 */
	void setJsonWithMap(Map<String, Object> content) {
		if(content == null) {
			throw new DataContainerException("Map object is not initialized ==> No JSON content to read");
		} else {
			json = new JSONObject(content);
		}
	}


}
