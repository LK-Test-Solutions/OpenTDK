package org.opentdk.api.datastorage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;
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
			content = FileUtil.getContent(dc.getInputStream());
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

	/**
	 * Retrieves the value of the attribute 'attrName' with the specified search
	 * criteria 'expr' that is a semicolon separated string with the keys that point
	 * to the result. Does not work for objects enumerations that are listed in a
	 * JSON array.
	 */
//	@Override
//	public String[] getAttributes(String expr, String attrName) {
//		String searchExp = "/" + expr.replace(";", "/") + "/" + attrName;
//		Object result = json.query(searchExp);
//		return new String[] { result.toString() };
//	}

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
					String sResult = result.toString();

					String[] saResults = null;
					if (result instanceof JSONArray) {
						sResult = this.splitJSONArray(sResult);
						if (sResult.contains(",")) {
							saResults = sResult.split(",");
						}
					}
					if (saResults == null) {
						filteredValues.add(sResult);
					} else {
						for (String res : saResults) {
							filteredValues.add(res);
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
			ret = new String[] { sResult };
			if (result instanceof JSONArray) {
				sResult = this.splitJSONArray(sResult);
				if (sResult.contains(",")) {
					ret = sResult.split(",");
				}
			}

		}
		return ret;
	}

	@Override
	public void addField(String headerName, String attributeName, String oldAttributeValue, String attributeValue, Filter fltr) {

	}

	@Override
	public void deleteField(String headerName, String attributeName, String attributeValue, Filter fltr) {

	}

	@Override
	public void setFieldValues(String headerName, int[] occurences, String value, Filter fltr) {
		setFieldValues(headerName, occurences, value, fltr, false);
	}

	/**
	 * Called when a value, array or object should be changed or added.
	 * 
	 * @param headerName JSON key
	 * @param occurences unused
	 * @param value      date to add or set
	 * @param fltr       filter object to find the correct field
	 * @param newField   flag to indicate if the value should be added or set
	 */
	public void setFieldValues(String headerName, int[] occurences, String value, Filter fltr, boolean newField) {
		occurences = null;
		// No filter ==> top level adjustments
		if (fltr.getFilterRules().isEmpty()) {
			Object newValue = this.getDataType(value);
			if (newField && newValue instanceof JSONArray) {
				json.append(headerName, newValue);
			} else {
				json.put(headerName, newValue);
			}
		}
		for (FilterRule fltrRule : fltr.getFilterRules()) {
			if (fltrRule.getHeaderName().equalsIgnoreCase("XPath")) {
				if (!fltrRule.getValue().strip().isEmpty()) {

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
							}
						}
						i++;
					}
					if (newField && newValue instanceof JSONArray) {
						((JSONObject) result).append(headerName, newValue);
					} else {
						((JSONObject) result).put(headerName, newValue);
					}
				}
				break;
			}
		}
	}

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

	private String splitJSONArray(String input) {
		String ret = input;
		if (ret.startsWith("[") && ret.endsWith("]")) {
			ret = ret.substring(ret.indexOf("[") + 1, ret.indexOf("]"));
		}
		return ret;
	}

}
