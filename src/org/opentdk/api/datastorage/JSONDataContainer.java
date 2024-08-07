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
import org.opentdk.api.logger.MLogger;
import org.opentdk.api.util.ListUtil;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Specific data container class for the JSON format. In its read and write methods the
 * {@link org.json.JSONObject} class gets used to handle sources from an {@link java.io.InputStream}
 * or a JSON file.
 * 
 * @author FME (LK Test Solutions)
 * @see org.opentdk.api.datastorage.DataContainer
 */
public class JSONDataContainer implements SpecificContainer {

	/**
	 * Container object for the JSON data. Supports several read and write methods. Gets initialized in
	 * {@link #readData(File)}.
	 */
	private JSONObject json;
	
	public static JSONDataContainer newInstance() {		
		return new JSONDataContainer();
	}
	
	private JSONDataContainer() {
		
	}
	
	@Override
	public String asString() {
		String ret = "";
		if (json == null || json.isEmpty()) {
			MLogger.getInstance().log(Level.WARNING, "JSON object is not initialized or empty ==> No JSON content to return", getClass().getSimpleName(), "asString");
		} else {
			ret = json.toString(1);
		}
		return ret;
	}

	@Override
	public String asString(EContainerFormat format) {
		switch(format) {
		case JSON:
			MLogger.getInstance().log(Level.INFO, "Format already is JSON. Call asString", getClass().getSimpleName(), "asString(format)");
			return asString();
		case YAML:
			Yaml yaml = new Yaml();
			return yaml.dump(yaml.dumpAsMap(getJsonAsMap()));
		default:
			MLogger.getInstance().log(Level.WARNING, "Format not supported to export from YAML", getClass().getSimpleName(), "asString(format)");
			return "";
		}
	}
	
	@Override
	public void readData(InputStream stream) {
		String content = null;
		if (stream != null) {
			InputStreamReader inputStreamReader = new InputStreamReader(stream);
			Stream<String> streamOfString = new BufferedReader(inputStreamReader).lines();
			content = streamOfString.collect(Collectors.joining());

			streamOfString.close();
			try {
				inputStreamReader.close();
			} catch (IOException e) {
				MLogger.getInstance().log(Level.SEVERE, e);
			}
		}
		if (content != null) {
			json = new JSONObject(content);
		}
	}
	
	@Override
	public void readData(File srcFile) throws IOException {
		if (!srcFile.getName().isEmpty()) {
			String content = FileUtil.getContent(srcFile.getPath());
			if (content != null) {
				json = new JSONObject(content);
			}
		} 	
	}
	
	@Override
	public void writeData(File srcFile) throws IOException {
		if (json == null || json.isEmpty()) {
			MLogger.getInstance().log(Level.WARNING, "JSON object is not initialized or empty ==> No JSON content to write", getClass().getSimpleName(), "writeData");
		} else {
			try {
				FileUtil.createFile(srcFile, true);
				FileUtil.writeOutputFile(json.toString(1), srcFile.getPath());
			} catch (JSONException e) {
				MLogger.getInstance().log(Level.SEVERE, e);
				throw new RuntimeException(e);
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

				List<String> keyList = ListUtil.asList(searchExp.split("/"));
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
				((JSONObject) result).append(name, newValue);
				break;
			}
		}
	}
	
	public void add(String name, String fieldName, String newValue, Filter filter) {
		MLogger.getInstance().log(Level.INFO, "Method not used", getClass().getSimpleName(), "add");
	}

	public void add(String headerName, String fieldName, String oldFieldValue, String newFieldValue, Filter filter) {
		MLogger.getInstance().log(Level.INFO, "Method not used", getClass().getSimpleName(), "add");
	}

	public void delete(String name, Filter filter) {
		if (filter.getFilterRules().isEmpty()) {
			json.remove(name);
		}
		for (FilterRule fltrRule : filter.getFilterRules()) {
			if (fltrRule.getHeaderName().equalsIgnoreCase("XPath") && StringUtils.isNotBlank(fltrRule.getValue())) {
				String searchExp = fltrRule.getValue().replace(";", "/") + "/" + name;
				Object result = null;

				List<String> keyList = ListUtil.asList(searchExp.split("/"));
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
				((JSONObject) result).remove(name);
				break;
			}
		}
	}

	public String[] get(String name) {
		return get(name, new Filter());
	}

	public String[] get(String headerName, Filter fltr) {
		String[] ret = null;
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
						if (result instanceof JSONArray) {
							JSONArray values = (JSONArray) result;
							for (int i = 0; i < values.length(); i++) {
								filteredValues.add(values.get(i).toString());
							}
						} else {
							filteredValues.add(sResult);
						}
					}
				}
			}
			ret = ListUtil.asStringArr(filteredValues);
		} else {
			Object result = json.get(headerName);
			String sResult = result.toString();

			if (result instanceof JSONArray) {
				JSONArray values = (JSONArray) result;
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

		Object ret = null;
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

				List<String> keyList = ListUtil.asList(searchExp.split("/"));
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
			MLogger.getInstance().log(Level.WARNING, "Map object is not initialized ==> No JSON content to read", getClass().getSimpleName(), "setJsonWithMap");
		} else {
			json = new JSONObject(content);
		}
	}


}
