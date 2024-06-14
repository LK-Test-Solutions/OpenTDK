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

import org.opentdk.api.filter.Filter;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Used to initialize the right specific data container depending on the given
 * source type. It contains all methods that the {@link DataContainer}
 * class has to provide.
 * 
 * @author LK Test Solutions
 */
public interface SpecificContainer {
	
	/**
	 * Each specific container class has to have a method that provides the
	 * container content as string.
	 * 
	 * @return the container content as string for further operations.
	 */
	String asString();
	
	/**
	 * Used to provide the content of the container in another container format as string. Default
	 * method, because to every format is transformable.
	 * 
	 * @param exportAs see {@link EContainerFormat}
	 * @return the container content as string for further operations in the chosen format
	 */
	default String asString(EContainerFormat exportAs) {
		return asString();
	}

	/**
	 * Each specific container needs to implement a method that reads data from the
	 * source.
	 * 
	 * @param sourceFile The path and name of the file to read from
	 * @throws IOException if the reading failed the user can handle the cause
	 */
	void readData(File sourceFile) throws IOException;
	void readData(File sourceFile, Filter filter) throws IOException;
	void readData(InputStream stream) throws IOException;
	void readData(InputStream stream, Filter filter) throws IOException;

	/**
	 * Each specific container has to implement a method that writes the
	 * data from the runtime instance into a file.
	 * 
	 * @param outputFile The path and name of the file to write to
	 * @throws IOException if the writing failed the user can handle the cause
	 */
	void writeData(File outputFile) throws IOException;

}