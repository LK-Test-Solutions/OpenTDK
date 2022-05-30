/*
 * Copyright (c) 2011, 2013, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */
package org.opentdk.api.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
import java.util.List;

/**
 * This class uses {@link java.io.FileWriter} for write operations on ASCII
 * files with additional functionality. Instances of this class can be created
 * in append mode which appends data to existing content, or in write mode which
 * overwrites existing content of a file. Various writing methods allow to write
 * data in different ways into a file.<br>
 * <br>
 * E.g.:<br>
 * 1. Single line, with or without column separation<br>
 * 2. List of multiple lines, with or without column separation<br>
 * 3. Insert a line at a specific line id<br>
 * etc.
 * 
 * @author LK Test Solutions
 *
 */
public class XFileWriter {
	/**
	 * File object that the {@link #XFileWriter(File)} uses as target for the write
	 * operations.
	 */
	private File file;
	/**
	 * Full name (path + name) that the {@link #XFileWriter(File)} uses as target
	 * for the write operations.
	 */
	private String fileName;
	/**
	 * Full name (path + name) that the {@link #insertLine(int, String)} method
	 * needs to perform the insertion operation.
	 */
	private String tmpFileName;
	/**
	 * The object that the {@link #XFileWriter(File)} uses to perform the write
	 * operations itself.
	 */
	private Writer fw;
	/**
	 * <code>true</code> append to existing file; <code>false</code> overwrite
	 * existing file.
	 */
	private boolean append;
	/**
	 * The character(s) used as delimiter for writing column separated lines.
	 */
	private String columnDelimiter = ";";

	/**
	 * Constructor to create a new instance with a given filename string. The
	 * filename must include the relative or absolute path of the file.
	 * 
	 * @param fileName {@link #fileName}
	 * @throws IOException If any IO error occurs when creating the instance with
	 *                     the committed file.
	 */
	public XFileWriter(String fileName) throws IOException {
		this(new File(fileName));
	}

	/**
	 * Constructor to create a new instance with a given file object.
	 * 
	 * @param file {@link #file}
	 * @throws IOException If any IO error occurs when creating the XFileWriter
	 *                     instance with the committed file.
	 */
	public XFileWriter(File file) throws IOException {
		this(file, false);
	}

	/**
	 * Constructor to create a new instance with a given full name (path + name) and
	 * the possibility to append data.
	 * 
	 * @param fileName {@link #fileName}
	 * @param append     {@link #append}
	 * @throws IOException If any IO error occurs when creating the XFileWriter
	 *                     instance with the committed file.
	 */
	public XFileWriter(String fileName, boolean append) throws IOException {
		this(new File(fileName), append);
	}

	/**
	 * Constructor to create a new instance with a given file object and the
	 * possibility to append data.
	 * 
	 * @param fileObj {@link #file}
	 * @param apnd    {@link #append}
	 * @throws IOException If any IO error occurs when creating the XFileWriter
	 *                     instance with the committed file.
	 */
	public XFileWriter(File fileObj, boolean apnd) throws IOException {
		file = fileObj;
		append = apnd;
		/*
		 * FileWriter constructor contains security checks and invalidation so no more
		 * necessary at this point
		 */
		fw = new FileWriter(file, append); // F
		fileName = file.getName();
		tmpFileName = file.getParent() + "\\~" + fileName.substring(0, fileName.lastIndexOf(".")) + ".tmp";
	}

	/**
	 * Flush and close the stream of the XFileWriter.
	 * 
	 * @throws IOException If any IO error occurs when closing the XFileWriter
	 *                     instance.
	 */
	public final void close() throws IOException {
		fw.close();
	}

	/**
	 * Retrieves the character(s) that will be used as column delimiter for the
	 * current instance of FileWriter. Default value for the delimiter is ";".
	 * 
	 * @return {@link #columnDelimiter}
	 */
	public final String getColumnDelimiter() {
		return columnDelimiter;
	}

	/**
	 * Sets the columDelimiter property which defines the character(s) used as
	 * delimiter to write column separated lines. Default value for the delimiter is
	 * ";".
	 * 
	 * @param cDelim {@link #columnDelimiter}
	 */
	public final void setColumnDelimiter(String cDelim) {
		columnDelimiter = cDelim;
	}

	/**
	 * Retrieves the file property as object of type {@link java.io.File}. The file
	 * property will be used as the target for write operations.
	 * 
	 * @return {@link #file}
	 */
	public final File getFile() {
		return file;
	}

	/**
	 * Sets the file property with an object of type {@link java.io.File}. The file
	 * property will be used as the target for write operations.
	 * 
	 * @param fileObj {@link #file}
	 */
	public final void setFile(File fileObj) {
		file = fileObj;
	}

	/**
	 * Writes a string as single line into a file. Before writing the string, a
	 * {@link System#lineSeparator()} will be added to the end of the string.
	 * 
	 * @param line String value which will be written to a file as line
	 * @throws IOException If any IO error occurs when the new line gets written to
	 *                     the file.
	 */
	public void writeLine(String line) throws IOException {
		writeLine(new String[] { line }, "");
	}

	/**
	 * Joins all values of an array with a semicolon as column separator. between
	 * each value and writes the result as single line into a file. Before writing
	 * the string, a {@link System#lineSeparator()} will be added to the end of the
	 * string.
	 * 
	 * @param lines String array that will be written as column separated line into
	 *              a file.
	 * @throws IOException If any IO error occurs when the new line gets written to
	 *                     the file.
	 */
	public void writeLine(String[] lines) throws IOException {
		writeLine(lines, columnDelimiter);
	}

	/**
	 * Joins all values of an array with one or more character(s) as column
	 * separator between each value and writes the result as single line into a
	 * file. Before writing the string, a {@link System#lineSeparator()} will be
	 * added to the end of the string.
	 * 
	 * @param line         String array that will be written as column separated
	 *                     line into a file.
	 * @param colDelimiter The character(s) that are added as column delimiter
	 *                     between each array value.
	 * @throws IOException If any IO error occurs when the new line gets written to
	 *                     the file.
	 */
	public void writeLine(String[] line, String colDelimiter) throws IOException {
		String output = String.format("%s", String.join(colDelimiter, line) + System.lineSeparator());
		fw.write(output);
		fw.flush();
	}

	/**
	 * Writes lines of any object type into a file. If the object cannot be
	 * represented as string the {@link #toString()} method gets used.
	 * 
	 * @param <I>   Custom type (placeholder).
	 * @param lines List of objects with the data to write into a file as lines.
	 * @throws IOException If any IO error occurs when the new line gets written to
	 *                     the file.
	 */
	public <I> void writeLines(List<I> lines) throws IOException {
		String output;
		Iterator<I> rowIt = lines.iterator();
		I next = null;
		while (rowIt.hasNext()) {
			next = rowIt.next();
			if (next.getClass().isArray()) {
				output = String.format("%s", String.join(columnDelimiter, (String[]) next));
			} else {
				output = next.toString();
			}
			output = output + System.lineSeparator();
			fw.write(output);
		}
		fw.flush();
	}

	/**
	 * Inserts a line into a file at the position, defined by the line index. While
	 * the standard methods only append lines to the end of a file, this method
	 * copies the file content line by line from a temporary copy of the file. At
	 * the specified line position, the new line will be inserted.
	 *
	 * @param lineIndex 0 based index that defines the position where to add the
	 *                  line.
	 * @param lineText  Text of the line that will be inserted.
	 * @throws IOException              If any IO error occurs when inserting the
	 *                                  new line into the file.
	 * @throws IllegalArgumentException If there are invalid input parameters.
	 */
	public void insertLine(int lineIndex, String lineText) throws IllegalArgumentException, IOException {
		if (lineIndex < 0) {
			throw new IllegalArgumentException("The value of lineIndex argument is not valid. lineIndex must be >= 0");
		} else if (append == false) {
			throw new IllegalArgumentException(
					"Method insertLine can only be executed in append mode. Please create instance of XFileWriter with append = true defined in the constructor.");
		} else {
			// create a temporary copy of the file
			FileUtil.copyFile(file.getPath(), tmpFileName);
			// create file object of temporary copy and read content into BufferedReader
			File tmpFile = new File(tmpFileName);
			BufferedReader fRead = new BufferedReader(new FileReader(tmpFile));
			// Reopen/Recreate an empty FileWriter object for the source file
			fw.close();
			fw = new FileWriter(file);
			int i = 0;
			String s;
			// read all lines from temporary file
			while ((s = fRead.readLine()) != null) {
				/* insert specified line at the given row index */
				if (i == lineIndex) {
					writeLine(lineText);
				}
				// write current line from temporary file back to source file
				writeLine(s);
				i++;
			}
			// Insert line with index 0 or after last line
			if (((i == 0) && (lineIndex == 0)) || (lineIndex == i)) {
				writeLine(lineText);
			}
			fRead.close();
			fw.flush();
			// Recreate FileWriter instance in append mode for further actions
			if (append) {
				fw.close();
				fw = new FileWriter(file, append);
			}
		}
	}
}
