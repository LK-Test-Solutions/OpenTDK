package org.opentdk.api.datastorage;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.json.JSONException;
import org.json.JSONObject;
import org.opentdk.api.datastorage.BaseContainer.EContainerFormat;
import org.opentdk.api.io.FileUtil;
import org.opentdk.api.logger.MLogger;

public class JSONDataContainer implements CustomContainer {

	/**
	 * An instance of the DataContainer that should be filled with the data from the connected source file. Task of the specific data containers.
	 */
	private final DataContainer dc;
	/**
	 * Container object for the JSON data.
	 */
	private JSONObject json;

	/**
	 * Construct a new specific <code>DataContainer</code> for JSON files.
	 *
	 * @param dCont the <code>DataContainer</code> instance to use it in the read and write methods of this specific data container
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
			List<String> headers = new ArrayList<>();
			for (String key : json.toMap().keySet()) {
				headers.add(key);
			}
			dc.setHeaders(headers);
			for (String header : dc.getHeaders().keySet()) {
				dc.setColumn(header, new String[] { json.get(header).toString() });
			}		
		}
	}

	@Override
	public void writeData(String srcFileName) {
		if(json == null || json.isEmpty()) {
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

}
