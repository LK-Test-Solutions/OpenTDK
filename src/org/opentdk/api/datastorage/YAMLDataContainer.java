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

import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;

import org.opentdk.api.filter.Filter;
import org.opentdk.api.io.FileUtil;
import org.opentdk.api.logger.MLogger;
import org.yaml.snakeyaml.Yaml;

/**
 * Specific data container class for the YAML format. The storage object for the source is the
 * {@link org.yaml.snakeyaml.Yaml} class to read from an {@link java.io.InputStream} or a YAML file.
 * All read and write methods use the {@link JSONDataContainer}, because both the
 * {@link org.yaml.snakeyaml.Yaml} and the {@link org.json.JSONObject} support a method to provide
 * the content as map.
 * 
 * @author LK Test Solutions
 * @see org.opentdk.api.datastorage.DataContainer
 */
public class YAMLDataContainer implements TreeContainer {
	/**
	 * Container object for the YAML data. Supports several read and write methods. Gets initialized in
	 * the constructor.
	 */
	private final Yaml yaml;
	/**
	 * Gets used instead of programming own YAMLDataContainer methods because the formats are
	 * transformable.
	 */
	private final JSONDataContainer json;
	/**
	 * Stores the content of the YAML source. Gets used to initialize the {@link JSONDataContainer}.
	 */
	private final Map<String, Object> content;

	/**
	 * Construct a new specific container for YAML files. This gets done by the {@link DataContainer}
	 * class in its adaptContainer method. After initialization, it gets passed to this class. 
	 * This class has no {@link DataContainer} field because it points to the {@link JSONDataContainer}.
	 *
	 * @param dCont gets used to initialize the {@link JSONDataContainer}
	 */
	YAMLDataContainer(DataContainer dCont) {
		yaml = new Yaml();
		json = new JSONDataContainer(dCont);

		if (dCont.getInputFile().exists()) {
			content = yaml.load(FileUtil.getRowsAsString(dCont.getInputFile()));
		} else if (dCont.getInputStream() != null) {
			content = yaml.load(dCont.getInputStream());
		} else {
			content = null;
		}
	}

	@Override
	public void add(String name, String value) {
		json.add(name, value);
	}

	@Override
	public void add(String name, String value, Filter filter) {
		json.add(name, value, filter);
	}
	
	@Override
	public void add(String name, String fieldName, String newFieldValue, Filter filter) {
		json.add(name, fieldName, newFieldValue, filter);
	}

	@Override
	public void add(String name, String fieldName, String oldFieldValue, String newFieldValue, Filter filter) {
		json.add(name, fieldName, oldFieldValue, newFieldValue, filter);
	}

	@Override
	public String asString() {
		return yaml.dump(yaml.dumpAsMap(json.getJsonAsMap()));
	}

	@Override
	public String asString(EContainerFormat format) {
		switch(format) {
		case JSON:
			return json.asString();
		case YAML:
			MLogger.getInstance().log(Level.INFO, "Format already is YAML. Call asString", getClass().getSimpleName(), "asString(format)");
			return asString();
		default:
			MLogger.getInstance().log(Level.WARNING, "Format not supported to export from YAML", getClass().getSimpleName(), "asString(format)");
			return "";
		}
	}

	@Override
	public void createFile() throws IOException {	
		json.createFile();
	}

	@Override
	public void delete(String name, String value) {
		json.set(name, value);
	}

	@Override
	public void delete(String name, String value, Filter filter) {
		json.delete(name, value, filter);
	}

	@Override
	public void delete(String headerName, String fieldName, String fieldValue, Filter filter) {
		json.delete(headerName, fieldName, fieldValue, filter);
	}
	
	@Override
	public String[] get(String name) {
		return json.get(name);
	}

	@Override
	public String[] get(String headerName, Filter fltr) {
		return json.get(headerName, fltr);
	}

	@Override
	public String[] get(String name, String attr) {
		return json.get(name, attr);
	}

	@Override
	public void readData(Filter filter) {
		if (content == null) {
			MLogger.getInstance().log(Level.WARNING, "YAML object is not initialized or empty ==> No YAML content to read", getClass().getSimpleName(), "readData");
		} else {
			json.setJsonWithMap(content);
		}
	}

	@Override
	public void set(String name, String value) {
		json.set(name, value);
	}

	@Override
	public void set(String name, String value, Filter filter) {
		json.set(name, value, filter);
	}

	@Override
	public void set(String name, String value, Filter filter, boolean allOccurences) {
		json.set(name, value, filter, allOccurences);
	}

//	@Override
//	public void set(String headerName, int[] occurences, String value, Filter fltr) {
//		json.set(headerName, occurences, value, fltr);
//	}

	@Override
	public void set(String name, String attr, String value, String oldValue, Filter filter) {
		json.set(name, attr, value, oldValue, filter);
		
	}

	@Override
	public void writeData(String srcFileName) throws IOException {
		yaml.dump(yaml.dumpAsMap(json.getJsonAsMap()), new FileWriter(srcFileName));
	}
	
}
