package org.opentdk.api.datastorage;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

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
		// ORG JSON
		
		String content = null;
		if (!dc.fileName.isEmpty()) {
			content = FileUtil.getContent(dc.fileName);
		} else if (dc.inputStream != null) {
			content = FileUtil.getContent(dc.inputStream);
		}
		if(content != null) {
			JSONObject json = new JSONObject(content);
			System.out.println(json.getJSONObject("address").get("city"));
		}
		
		// JACKSON Parser
//		JsonFactory factory = new JsonFactory();
//		JsonParser parser = null;
//		try {
//			if (!dc.getFileName().isEmpty()) {
//				parser = factory.createParser(new File(dc.fileName));
//			} else if (dc.getInputStream() != null) {
//				parser = factory.createParser(dc.getInputStream());
//			}
//			if (parser != null) {
//				while (!parser.isClosed()) {
//					JsonToken jsonToken;
//					jsonToken = parser.nextToken();
//					System.out.println("jsonToken = " + jsonToken);
//				}
//			}
//		} catch (IOException e) {
//			MLogger.getInstance().log(Level.SEVERE, e);
//			throw new RuntimeException(e);
//		} finally {
//			if (parser != null) {
//				try {
//					parser.close();
//				} catch (IOException e) {
//					MLogger.getInstance().log(Level.SEVERE, e);
//				}
//			}
//		}
	}

	@Override
	public void writeData(String srcFileName) {

	}

}
