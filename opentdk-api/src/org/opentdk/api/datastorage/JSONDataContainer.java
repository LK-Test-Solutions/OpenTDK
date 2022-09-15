package org.opentdk.api.datastorage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.opentdk.api.datastorage.BaseContainer.EContainerFormat;
import org.opentdk.api.io.FileUtil;
import org.opentdk.api.logger.MLogger;
import org.opentdk.api.util.ListUtil;

public class JSONDataContainer implements CustomContainer {

	/**
	 * An instance of the DataContainer that should be filled with the data from the
	 * connected source file. Task of the specific data containers.
	 */
	private final DataContainer dc;

	/**
	 * Container object for the JSON data. Supports several methods for read and
	 * write operations.
	 */
	private JSONObject json;

	/**
	 * Construct a new specific <code>DataContainer</code> for JSON files.
	 *
	 * @param dCont the <code>DataContainer</code> instance to use it in the read
	 *              and write methods of this specific data container
	 */
	JSONDataContainer(DataContainer dCont) {
		dc = dCont;
		dc.containerFormat = EContainerFormat.JSON;
		dc.getImplicitHeaders().add("XPath");
	}

	@Override
	public void readData(Filter filter) {
		String content = null;
		if (!dc.getFileName().isEmpty()) {
			content = FileUtil.getContent(dc.getFileName());
		} else if (dc.getInputStream() != null) {
			try {
				content = new String(dc.getInputStream().readAllBytes());
			} catch (IOException e) {
				MLogger.getInstance().log(Level.SEVERE, e);
				content = null;
			}
		}
		if (content != null) {
			json = new JSONObject(content);

			String[] names = JSONObject.getNames(json);
			dc.setHeaders(names);
			for (String header : names) {
				dc.setColumn(header, new String[] { json.get(header).toString() });
			}
		}
	}

	@Override
	public void writeData(String srcFileName) {
		if (json == null || json.isEmpty()) {
			MLogger.getInstance().log(Level.WARNING, "JSON object is not initialized or empty ==> No JSON content to write", getClass().getSimpleName(), "writeData");
		} else {
			try {
				FileUtil.createFile(srcFileName, true);
				FileUtil.writeOutputFile(json.toString(1), srcFileName);
			} catch (JSONException | IOException e) {
				MLogger.getInstance().log(Level.SEVERE, e);
				throw new RuntimeException(e);
			}
		}

	}

	@Override
	public Object[] getColumn(String headerName, Filter fltr) {
		String[] ret = null;
		List<FilterRule> implFilterRules = dc.getImplFilterRules(fltr);
		List<String> filteredValues = new ArrayList<>();

		if (implFilterRules.size() > 0) {
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
			/*
			 * Simply returns the whole content of the column header at top level.
			 */
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
		Object[] exisitingNode = this.getColumn(headerName, fltr);
		if (exisitingNode.length == 0) {
			// Field does not exist ==> create it
			this.setFieldValues(headerName, new int[0], newFieldValue, fltr);
		} else {
			if (fltr.getFilterRules().isEmpty()) {
				Object newValue = this.getDataType(newFieldValue);
				json.append(headerName, newValue);
			}
			for (FilterRule fltrRule : fltr.getFilterRules()) {
				// Filter used ==> XPath usage
				if (fltrRule.getHeaderName().equalsIgnoreCase("XPath") && !fltrRule.getValue().strip().isEmpty()) {
					String searchExp = fltrRule.getValue().replace(";", "/") + "/" + headerName;
					Object newValue = this.getDataType(newFieldValue);
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
					((JSONObject) result).append(headerName, newValue);
					break;
				}
			}
		}
	}

	@Override
	public void deleteField(String headerName, String fieldName, String fieldValue, Filter fltr) {
		// No filter ==> top level adjustments
		if (fltr.getFilterRules().isEmpty()) {
			json.remove(headerName);
		}
		for (FilterRule fltrRule : fltr.getFilterRules()) {
			// Filter used ==> XPath usage
			if (fltrRule.getHeaderName().equalsIgnoreCase("XPath") && !fltrRule.getValue().strip().isEmpty()) {
				String searchExp = fltrRule.getValue().replace(";", "/") + "/" + headerName;
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
				((JSONObject) result).remove(headerName);
				break;
			}
		}
	}

	/**
	 * Called when a value, array or object should be changed or created.
	 * 
	 * @param headerName JSON key
	 * @param occurences unused
	 * @param value      data to set {@literal -} the JSON data type gets parsed e.g. 'true'
	 *                   becomes a boolean or '"Test"' becomes a string or
	 *                   '[value1,value2]' an array
	 * @param fltr       filter object to find the correct field
	 */
	public void setFieldValues(String headerName, int[] occurences, String value, Filter fltr) {
		occurences = null;
		// No filter ==> top level adjustments
		if (fltr.getFilterRules().isEmpty()) {
			Object newValue = this.getDataType(value);
			json.put(headerName, newValue);
		}
		for (FilterRule fltrRule : fltr.getFilterRules()) {
			// Filter used ==> XPath usage
			if (fltrRule.getHeaderName().equalsIgnoreCase("XPath") && !fltrRule.getValue().strip().isEmpty()) {
				String searchExp = fltrRule.getValue().replace(";", "/") + "/" + headerName;
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
					((JSONObject) result).put(headerName, newValue);
				}
				break;
			}
		}
	}

	/**
	 * Retrieves the data type out of the committed value. This can be a JSONObject,
	 * JSONArray or any other primitive data type like string, integer or boolean.
	 * E.g. if the input string has leading and trailing '"' it gets interpreted as
	 * JSONString. If not it gets wrapped into a number or boolean. If it has the
	 * syntax '[value1,value2]' it gets wrapped as JSONarray. And if it has the
	 * syntax '{...}' it gets wrapped as JSONObject. A 'null' value is allowed, too,
	 * and gets a JSON.NULL object.
	 * 
	 * @param newValue the value to add or put that should be checked
	 * @return the detected object type depending on the syntax of the committed
	 *         string
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
				// All other data types like boolean or number
				if (ret == null) {
					ret = JSONObject.stringToValue(newValue);
				}
			}
		}
		return ret;
	}

//	private String splitJSONArray(String input) {
//		String ret = input;
//		if (ret.startsWith("[") && ret.endsWith("]")) {
//			ret = ret.substring(ret.indexOf("[") + 1, ret.indexOf("]"));
//		}
//		return ret;
//	}

}
