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

import java.io.IOException;

import org.opentdk.api.filter.Filter;

/**
 * The <code>BaseContainer</code> is used by the <code>DataContainer</code> to initialize the
 * right specific data container depending on the given source type. It contains all methods that
 * the <code>DataContainer</code> has to provide.
 * 
 * @author LK Test Solutions
 */
interface SpecificContainer {
	/**
	 * Each specific container class has to have a method that provides the container content as string.
	 * 
	 * @return the container content as string for further operations.
	 */
	String asString();

	/**
	 * Used to provide the content of the container in another container format as string. Default
	 * method, because to every format is transformable.
	 * 
	 * @param exportAs see {@link org.opentdk.api.datastorage.BaseContainer.EContainerFormat}
	 * @return the container content as string for further operations in the chosen format
	 */
	default String asString(EContainerFormat exportAs) {
		return asString();
	}

	/**
	 * Each specific <code>DataContainer</code> has to implement a method that checks if the connected
	 * file exists and create it if required.
	 * 
	 * @throws IOException if the creation failed the user can handle the cause
	 */
	void createFile() throws IOException;

	/**
	 * Each specific <code>DataContainer</code> has to implement a method that checks if the connected
	 * file exists and create it if required.
	 * 
	 * @param srcFile  The name of the source file to write to
	 * @param rootNode if the format has a root node that has to be checked
	 * @throws IOException if the creation failed the user can handle the cause
	 */
//	default void createFile(String srcFile, String rootNode) throws IOException {
//		return;
//	}

	/**
	 * Each specific <code>DataContainer</code> that implements the <code>CustomContainer</code>, needs
	 * to implement a method that reads data from the source and store the data into tabular format
	 * within the <code>DataContainer</code>. In case the source data is not in tabular format, the
	 * specific method needs to implement also the logic to parse, filter and/or transpose the source
	 * data into the format that is supported by the <code>DataContainer</code>.
	 * 
	 * @param filter Possibility to filter some of the source data
	 */
	void readData(Filter filter) throws IOException;

	/**
	 * Each specific <code>DataContainer</code> that implements the <code>CustomContainer</code>, needs
	 * to implement a method that writes the data from the runtime instance of the
	 * <code>DataContainer</code> into a file.
	 * 
	 * @param srcFile The name of the source file to write to
	 * @throws IOException Throws an exception if file is missing
	 */
	void writeData(String srcFile) throws IOException;
	
}