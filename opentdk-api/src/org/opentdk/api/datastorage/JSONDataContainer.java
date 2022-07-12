package org.opentdk.api.datastorage;

import java.io.IOException;
import java.util.logging.Level;

import org.json.JSONException;
import org.json.JSONObject;
import org.opentdk.api.datastorage.BaseContainer.EContainerFormat;
import org.opentdk.api.io.FileUtil;
import org.opentdk.api.logger.MLogger;

public class JSONDataContainer implements CustomContainer {

	/**
	 * An instance of the DataContainer that should be filled with the data from the connected source
	 * file. Task of the specific data containers.
	 */
	private final DataContainer dc;

	/**
	 * Container object for the JSON data. Supports several methods for read and write operations.
	 */
	private JSONObject json;

	/**
	 * Construct a new specific <code>DataContainer</code> for JSON files.
	 *
	 * @param dCont the <code>DataContainer</code> instance to use it in the read and write methods of
	 *              this specific data container
	 */
	JSONDataContainer(DataContainer dCont) {
		dc = dCont;
		dc.containerFormat = EContainerFormat.JSON;
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
	 * Retrieves the value of the attribute 'attrName' with the specified search criteria 'expr' that is
	 * a semicolon separated string with the keys that point to the result. Does not work for objects
	 * enumerations that are listed in a JSON array.
	 */
	@Override
	public String[] getAttributes(String expr, String attrName) {
		String searchExp = "/" + expr.replace(";", "/") + "/" + attrName;
		Object result = json.query(searchExp);
		return new String[] { result.toString() };
	}

	/**
	 * Retrieves the content as string that is stored under the specified 'headerName'. The headers are
	 * the key of the JSON format at top level. The column content is the whole string that is defined
	 * as value. Can be an array or object as well.
	 */
	@Override
	public Object[] getColumn(String headerName, Filter fltr) {
		MLogger.getInstance().log(Level.INFO, "Filter not used in JSON format", getClass().getSimpleName(), "getColumn");
		return new String[] { json.get(headerName).toString() };
	}

}
