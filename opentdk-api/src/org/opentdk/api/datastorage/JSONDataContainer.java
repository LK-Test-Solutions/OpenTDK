package org.opentdk.api.datastorage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.stream.Collectors;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.opentdk.api.datastorage.BaseContainer.EContainerFormat;
import org.opentdk.api.io.FileUtil;
import org.opentdk.api.logger.MLogger;
import org.opentdk.api.util.ListUtil;
import org.w3c.dom.Element;

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
			MLogger.getInstance().log(Level.WARNING,
					"JSON object is not initialized or empty ==> No JSON content to write", getClass().getSimpleName(),
					"writeData");
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
					//
					filteredValues.add(result.toString());
				}
			}
			ret = ListUtil.asStringArr(filteredValues);
		} else {
			/*
			 * Simply returns the whole content of the column header at top level.
			 */
			ret = new String[] { json.get(headerName).toString() };
		}
		return ret;
	}

	@Override
	public void addField(String headerName, String attributeName, String oldAttributeValue, String attributeValue,
			Filter fltr) {

	}

	@Override
	public void deleteField(String headerName, String attributeName, String attributeValue, Filter fltr) {

	}

	@Override
	public void setFieldValues(String headerName, int[] occurences, String value, Filter fltr) {
		setFieldValues(headerName, occurences, value, fltr, false);
	}

	public void setFieldValues(String headerName, int[] occurences, String value, Filter fltr, boolean newField) {
		// Initialize occurrences array with 0 item, in case it is empty
		if (occurences.length < 1) {
			occurences = new int[] { 0 };
		}
		for (FilterRule fltrRule : fltr.getFilterRules()) {
			if (fltrRule.getHeaderName().equalsIgnoreCase("XPath")) {			
				if (!fltrRule.getValue().strip().isEmpty()) {
					
//					String searchExp = "/" + fltrRule.getValue().replace(";", "/") + "/" + headerName;
//					Object result = json.query(searchExp);
//					
//					Object field = null;
//					if(result.equals(JSONObject.NULL)) {
//						break;
//					} else if(result instanceof JSONArray) {
//						field = new JSONArray(result.toString());
//					} else if(result instanceof JSONObject) {
//						field = new JSONObject(result.toString());
//					} else if(result instanceof String) {
//						
//					}
//					if (newField) {
//						((JSONObject) result).append(headerName, value);
//					} else {
//						((JSONObject) result).put(headerName, value);
//					}
				} else {
					if(newField) {
						json.append(headerName, value);
					} else {
						json.put(headerName, value);
					}
				}
				break;
			}
		}
	}

}
