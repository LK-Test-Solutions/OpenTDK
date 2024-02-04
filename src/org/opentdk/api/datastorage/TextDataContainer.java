package org.opentdk.api.datastorage;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.opentdk.api.io.FileUtil;

public class TextDataContainer implements SpecificContainer {
	
	private final StringBuilder content;

	public static TextDataContainer newInstance() {		
		return new TextDataContainer();
	}
	
	private TextDataContainer() {
		content = new StringBuilder();
	}

	@Override
	public String asString() {
		return content.toString();
	}

	@Override
	public void readData(File sourceFile) throws IOException {
		content.append(FileUtil.getRowsAsString(sourceFile));	
	}

	@Override
	public void readData(InputStream stream) throws IOException {
		content.append(FileUtil.getContent(stream));
	}

	@Override
	public void writeData(File outputFile) throws IOException {
		FileUtil.writeOutputFile(content.toString(), outputFile.getPath());		
	}
}
