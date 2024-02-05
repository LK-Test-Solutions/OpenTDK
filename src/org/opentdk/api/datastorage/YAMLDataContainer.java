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

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
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
 * @author FME (LK Test Solutions)
 * @see org.opentdk.api.datastorage.DataContainer
 */
public class YAMLDataContainer implements SpecificContainer {
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
	private Map<String, Object> content;

	public static YAMLDataContainer newInstance() {		
		return new YAMLDataContainer();
	}
	
	private YAMLDataContainer() {
		yaml = new Yaml();
		json = JSONDataContainer.newInstance();
		if (content == null) {
			MLogger.getInstance().log(Level.WARNING, "YAML object is not initialized or empty ==> No YAML content to read", getClass().getSimpleName(), "constructor");
		} else {
			json.setJsonWithMap(content);
		}
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
	public void readData(File sourceFile) throws IOException {
		content = yaml.load(FileUtil.getRowsAsString(sourceFile));		
	}

	@Override
	public void readData(InputStream stream) throws IOException {
		content = yaml.load(stream);		
	}
	

	@Override
	public void readData(File sourceFile, Filter filter) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void readData(InputStream stream, Filter filter) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void writeData(File outputFile) throws IOException {
		yaml.dump(yaml.dumpAsMap(json.getJsonAsMap()), new FileWriter(outputFile.getPath()));	
	}

	public void add(String name, String value) {
		json.add(name, value);
	}

	public void add(String name, String value, Filter filter) {
		json.add(name, value, filter);
	}
	
	public void add(String name, String fieldName, String newFieldValue, Filter filter) {
		json.add(name, fieldName, newFieldValue, filter);
	}

	public void add(String name, String fieldName, String oldFieldValue, String newFieldValue, Filter filter) {
		json.add(name, fieldName, oldFieldValue, newFieldValue, filter);
	}

	public void delete(String name, String value) {
		json.set(name, value);
	}

	public void delete(String name, String value, Filter filter) {
		json.delete(name, value, filter);
	}

	public void delete(String headerName, String fieldName, String fieldValue, Filter filter) {
		json.delete(headerName, fieldName, fieldValue, filter);
	}
	
	public String[] get(String name) {
		return json.get(name);
	}

	public String[] get(String headerName, Filter fltr) {
		return json.get(headerName, fltr);
	}

	public void set(String name, String value) {
		json.set(name, value);
	}

	public void set(String name, String value, Filter filter) {
		json.set(name, value, filter);
	}

	public void set(String name, String value, Filter filter, boolean allOccurences) {
		json.set(name, value, filter, allOccurences);
	}

	public void set(String name, String attr, String value, String oldValue, Filter filter) {
		json.set(name, attr, value, oldValue, filter);
		
	}

}
