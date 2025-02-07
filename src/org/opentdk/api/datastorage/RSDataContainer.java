package org.opentdk.api.datastorage;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RSDataContainer extends CSVDataContainer {

	public static CSVDataContainer newInstance() {
		return CSVDataContainer.newInstance();
	}

	private RSDataContainer() {
        super();
    }

	public void readData(ResultSet rs) throws IOException, SQLException {
		readData(rs, "Label");
	}

	public void readData(ResultSet rs, String headerOrigin) throws IOException, SQLException {
//		if (rs != null) {
//			try (rs) {
//				// read description of the ResultSet columns
//				ResultSetMetaData rsmd = rs.getMetaData();
//				int cols = rsmd.getColumnCount();
//				List<String> columns = new ArrayList<String>();
//				// fill columns List with the column names of the result set
//				for (int i = 1; i <= cols; i++) {
//					String col;
//					if (headerOrigin.equalsIgnoreCase("LABEL")) {
//						col = rsmd.getColumnLabel(i);
//					} else {
//						col = rsmd.getColumnName(i);
//					}
//					columns.add(col);
//				}
//				// transfer all column names from the columns list to the HashMap columns
//				setHeaders(columns.toArray(String[]::new));
//				// transfer all data rows of the ResultSet to the ArrayList of this class
//				if (rs.isBeforeFirst()) {
//					rs.first();
//				}
//				do {
//					String[] row = new String[cols];
//					for (int i = 0; i < cols; i++) {
//						row[i] = String.valueOf(rs.getObject(i + 1));
//					}
//					addRow(row);
//				} while (rs.next());
//			}
//		}
	}

}
