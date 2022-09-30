package org.opentdk.api.datastorage;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.opentdk.api.datastorage.BaseContainer.EContainerFormat;
import org.opentdk.api.filter.Filter;
import org.opentdk.api.logger.MLogger;

public class RSDataContainer implements CustomContainer {

    /**
     * An instance of the DataContainer that should be filled with the data from the connected source file.
     * -> Task of the specific data containers.
     */
    private final DataContainer dc;

    /**
	 * Construct a new specific <code>DataContainer</code> for database requests.
	 *
	 * @param dCont the <code>DataContainer</code> instance to use it in the read
	 *              and write methods of this specific data container
	 */
    public RSDataContainer(DataContainer dCont) {
        dc = dCont;
        dc.containerFormat = EContainerFormat.RESULTSET;
    }
	
    /**
     * Reads data from a {@link java.sql.ResultSet} and puts it into the DataContainer.
     * 
     * @param filter Filter parameter to accomplish interface requirement (has no
	 *               use here)	
     */
	@Override
	public void readData(Filter filter) {
		if (dc.resultSet != null) {
			try {
				// read description of the ResultSet columns
				ResultSetMetaData rsmd = dc.resultSet.getMetaData();
				int cols = rsmd.getColumnCount();
				List<String> columns = new ArrayList<String>();
				// fill columns List with the column names of the result set
				for (int i = 1; i <= cols; i++) {
					String col = rsmd.getColumnName(i);
					columns.add(col);
				}
				// transfer all column names from the colums list to the HashMap columns
				dc.setHeaders(columns.toArray(new String[]{}));
				// transfer all data rows of the ResultSet to the ArrayList of this class
                do {
                    String[] row = new String[cols];
                    for (int i = 0; i < cols; i++) {
                    	row[i] = String.valueOf(dc.resultSet.getObject(i + 1));
                    }
                   dc.addRow(row);
                } while (dc.resultSet.next());
                dc.resultSet.close();
			} catch (SQLException e) {
				MLogger.getInstance().log(Level.SEVERE, e, "putResultSet");
				throw new RuntimeException(e);
			}
		}
	}

	@Override
	public void writeData(String srcFile) {

	}

	@Override
	public String asString() {
		return dc.getValuesAsString();
	}

}
