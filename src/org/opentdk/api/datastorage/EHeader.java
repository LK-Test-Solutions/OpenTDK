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

/**
 * Enumeration that defines all header formats supported by the data storage
 * package. In case of tabular formats, the header names can be defined in a row (e.g. CSV files) or
 * in a column (e.g. Properties files) and for hierarchical formats like XML, HTML, JSON or JAML,
 * the data will be located by the tree hierarchy by the xPath.
 */
public enum EHeader {
	/**
	 * The {@link DataContainer} is in tabular format and includes column headers which means that the
	 * elements added to the {@link #values()} ArrayList correspond to a row of the source.
	 */
	COLUMN,
	/**
	 * The data source is in tabular format and includes row headers which means that the elements added
	 * to the {@link #values()} ArrayList correspond to a column of the source.
	 */
	ROW,
	/**
	 * The data source is in tree format like XML. No header names will be assigned to the
	 * {@link DataContainer}.
	 */
	TREE,
	/**
	 * If UNKNOWN is used, the DataContainer assumes that the source includes semicolon separated
	 * columns without column and row headers.
	 */
	UNKNOWN;
}
