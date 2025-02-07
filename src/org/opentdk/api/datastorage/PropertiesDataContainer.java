package org.opentdk.api.datastorage;

import java.io.*;
import java.nio.file.Path;

public class PropertiesDataContainer implements SpecificContainer {


	public static SpecificContainer newInstance() {
		return new PropertiesDataContainer();
	}

	@Override
	public String asString() {
		return "";
	}

	@Override
	public void readData(Path sourceFile) throws IOException {

	}

	@Override
	public void readData(InputStream stream) throws IOException {

	}

	@Override
	public void writeData(Path outputFile) throws IOException {

	}
}
