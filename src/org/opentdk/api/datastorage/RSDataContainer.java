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
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.opentdk.api.filter.Filter;
import org.opentdk.api.logger.MLogger;

public class RSDataContainer extends CSVDataContainer {

	RSDataContainer(DataContainer dCont) {
		super(dCont);
	}

	/**
     * Reads data from a {@link java.sql.ResultSet} and puts it into the DataContainer.
     * 
     * @param filter Filter parameter to accomplish interface requirement (has no
	 *               use here)	
     */
	@Override
	public void readData(Filter filter) throws IOException {
		if (dc.getResultSet() != null) {
			try {
				// read description of the ResultSet columns
				ResultSetMetaData rsmd = dc.getResultSet().getMetaData();
				int cols = rsmd.getColumnCount();
				List<String> columns = new ArrayList<String>();
				// fill columns List with the column names of the result set
				for (int i = 1; i <= cols; i++) {
					String col = rsmd.getColumnName(i);
					columns.add(col);
				}
				// transfer all column names from the colums list to the HashMap columns
				setHeaders(columns.toArray(new String[]{}));
				// transfer all data rows of the ResultSet to the ArrayList of this class
                do {
                    String[] row = new String[cols];
                    for (int i = 0; i < cols; i++) {
                    	row[i] = String.valueOf(dc.getResultSet().getObject(i + 1));
                    }
                   addRow(row);
                } while (dc.getResultSet().next());
                dc.getResultSet().close();
			} catch (SQLException e) {
				MLogger.getInstance().log(Level.SEVERE, e, "putResultSet");
				throw new RuntimeException(e);
			}
		}
	}

}
