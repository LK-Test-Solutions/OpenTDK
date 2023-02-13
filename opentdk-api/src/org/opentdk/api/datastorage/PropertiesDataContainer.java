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

import java.io.Reader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.logging.Level;

import org.apache.commons.lang3.StringUtils;
import org.opentdk.api.filter.Filter;
import org.opentdk.api.io.FileUtil;
import org.opentdk.api.logger.MLogger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * 
 * @author LK Test Solutions GmbH
 *
 */
public class PropertiesDataContainer implements CustomContainer {

	/**
	 * An instance of the DataContainer that should be filled with the data from the
	 * connected source file. -> Task of the specific data containers.
	 */
	private final DataContainer dc;

	/**
	 * Construct a new specific <code>DataContainer</code> for PROPERTIES files.
	 *
	 * @param dCont the <code>DataContainer</code> instance to use it in the read
	 *              and write methods of this specific data container
	 */
	public PropertiesDataContainer(DataContainer dCont) {
		dc = dCont;
	}
	
	public void deleteField(String headerName, String attributeName, String attributeValue, Filter fltr) {
		int headerIndex = dc.getHeaderIndex(headerName);
		dc.values.get(0)[headerIndex] = null;
		if(!dc.getFileName().isEmpty()) {
			try {
				writeData(dc.getFileName());
			} catch (IOException e) {
				MLogger.getInstance().log(Level.SEVERE, e, "getRowsIndexes");
				throw new RuntimeException(e);
			}
		}
	}

	/**
	 * Reads data from a file of type {@link java.util.Properties} and puts its
	 * property names and values into the DataContainer.
	 *
	 * @param filter Unused in the <code>PropertiesDataContainer</code>
	 */
	@Override
	public void readData(Filter filter) {
		SortedProperties props = readProps(dc.getFileName());
		// get all property names and add them to a List
		if ((props != null) && (!props.isEmpty())) {
			Enumeration<?> e = props.propertyNames();
			List<String> pNames = new ArrayList<String>();
			while (e.hasMoreElements()) {
				pNames.add(String.valueOf(e.nextElement()));
			}
			// set property names as headers
			dc.setHeaders(pNames.toArray(new String[pNames.size()]));
			// get values of the properties
			String[] row = new String[pNames.size()];
			for (int i = 0; i < pNames.size(); i++) {
				row[i] = String.valueOf(props.get(pNames.get(i)));
//				addRow(new String[] { String.valueOf(props.get(key)) });
			}
			dc.addRow(row);
		}
	}

	private SortedProperties readProps(String fileName) {
		SortedProperties props = new SortedProperties();
		Reader reader;
		try {
			reader = new FileReader(fileName);
			props.load(reader);
			reader.close();
		} catch (Exception e) {
			props = null;
		}
		return props;
	}

	/**
	 * Writes data to a new or existing .properties file. If the file already
	 * exists, all comments and unknown keys remain unchanged. New data will be
	 * added to the file. Manipulated data will also change in the file.
	 *
	 * @param srcFile Name and full path of the properties file
	 * @throws IOException If any I/O error occurred
	 */
	public void writeData(String srcFile) throws IOException {
		File f = new File(srcFile);
		FileUtil.checkDir(f.getParent(), true);
		FileWriter fw = null;
		fw = new FileWriter(dc.getFileName());
		SortedProperties existingProps = readProps(dc.getFileName());
		SortedProperties outputProps = new SortedProperties();
		if ((existingProps != null) && (!existingProps.isEmpty())) {
			List<String> doc = FileUtil.getRowsAsList(dc.getFileName());

			List<String> usedKeys = new ArrayList<String>();
			for (String line : doc) {
				if (dc.getHeaders().containsKey(line.split("=")[0].trim())) {
					fw.write(line.split("=")[0].trim() + "=" + dc.getValue(line.split("=")[0].trim(), 0) + "\n");
					usedKeys.add(line.split("=")[0].trim());
				} else {
					fw.write(line + "\n");
				}
			}
			for (String k : dc.getHeaders().keySet()) {
				if (!usedKeys.contains(k))
					fw.write(k + "=" + dc.getValue(k, 0) + "\n");
			}

			fw.close();

		} else {
			String[] ds = dc.getRow(0);
			for (String s : dc.getHeaders().keySet()) {
				if(ds[dc.getHeaders().get(s)] != null) {
					outputProps.put(s, ds[dc.getHeaders().get(s)]);
				}
			}
			FileOutputStream fos = new FileOutputStream(dc.getFileName());
			outputProps.store(fos, "Store properties");
			
		}
	}
	

	@Override
	public void createFile(String srcFile) throws IOException {
		FileUtil.createFile(srcFile, true);
	}

	@Override
	public String asString() {
		StringBuilder ret = new StringBuilder();
		String[] values = dc.getRow(0);
		for (String key : dc.getHeaders().keySet()) {
			String value = values[dc.getHeaders().get(key)];
			if(value != null) {
				ret.append(StringUtils.strip(key)).append(" = ").append(StringUtils.strip(value)).append("\n");
			}
		}
		return ret.toString();
	}
}
