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
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RSDataContainer extends TabularContainer {

	public static RSDataContainer newInstance() {
		return new RSDataContainer();
	}

	private RSDataContainer() {
		super();
	}

	public void readData(ResultSet rs) throws IOException, SQLException {
		readData(rs, "Label");
	}

	public void readData(ResultSet rs, String headerOrigin) throws IOException, SQLException {
		if (rs != null) {
			try {
				// read description of the ResultSet columns
				ResultSetMetaData rsmd = rs.getMetaData();
				int cols = rsmd.getColumnCount();
				List<String> columns = new ArrayList<String>();
				// fill columns List with the column names of the result set
				for (int i = 1; i <= cols; i++) {
					String col;
					if (headerOrigin.equalsIgnoreCase("LABEL")) {
						col = rsmd.getColumnLabel(i);
					} else {
						col = rsmd.getColumnName(i);
					}
					columns.add(col);
				}
				// transfer all column names from the columns list to the HashMap columns
				setHeaders(columns.toArray(new String[] {}));
				// transfer all data rows of the ResultSet to the ArrayList of this class
				if (rs.isBeforeFirst()) {
					rs.first();
				}
				do {
					String[] row = new String[cols];
					for (int i = 0; i < cols; i++) {
						row[i] = String.valueOf(rs.getObject(i + 1));
					}
					addRow(row);
				} while (rs.next());
			} finally {
				rs.close();
			}
		}
	}

}
