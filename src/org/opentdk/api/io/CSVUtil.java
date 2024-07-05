package org.opentdk.api.io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

//import org.apache.commons.csv.CSVFormat;
//import org.apache.commons.csv.CSVRecord;
import org.opentdk.api.logger.MLogger;

public class CSVUtil {
	
//	public static List<CSVRecord> readFile(String filePath, Charset encoding) throws IOException {
//		FileReader reader = new FileReader(filePath, encoding);
//        return CSVFormat.DEFAULT.parse(reader).getRecords();
//    }

	public static List<String[]> readFile(String filePath, String delimiter, Charset encoding) {
		List<String[]> data = new ArrayList<>();

		try (BufferedReader br = new BufferedReader(new FileReader(filePath, encoding))) {
			String line;
			while ((line = br.readLine()) != null) {
				String[] values = line.split(delimiter);
				data.add(values);
			}
		} catch (IOException e) {
			MLogger.getInstance().log(Level.SEVERE, e);
		} 

		return data;
	}

	public static List<String[]> filterData(List<String[]> data, String columnName, String filterValue) {
		List<String[]> filteredData = new ArrayList<>();

		// Find the index in the column
		int columnIndex = -1;
		if (data.size() > 0) {
			String[] header = data.get(0);
			for (int i = 0; i < header.length; i++) {
				if (header[i].equals(columnName)) {
					columnIndex = i;
					break;
				}
			}
		}

		// Filter the data based on the value in the committed column
		if (columnIndex != -1) {
			for (int i = 1; i < data.size(); i++) { // Start index is 1 to jump over the header row
				String[] row = data.get(i);
				if (row.length > columnIndex && row[columnIndex].equals(filterValue)) {
					filteredData.add(row);
				}
			}
		}

		return filteredData;
	}

	public static List<String> getRow(List<String[]> data, int rowIndex) {
		List<String> rowData = new ArrayList<>();

		if (rowIndex >= 0 && rowIndex < data.size()) {
			String[] row = data.get(rowIndex);
			for (String value : row) {
				rowData.add(value);
			}
		}

		return rowData;
	}

	public static List<String> getColumn(List<String[]> data, int columnIndex) {
		List<String> columnData = new ArrayList<>();

		for (String[] row : data) {
			if (columnIndex >= 0 && columnIndex < row.length) {
				columnData.add(row[columnIndex]);
			}
		}

		return columnData;
	}

	public static List<String> getColumn(List<String[]> data, String columnName) {
		List<String> columnData = new ArrayList<>();

		int columnIndex = -1;
		if (data.size() > 0) {
			// Assuming the column names are in the first row
			String[] header = data.get(0);

			for (int i = 0; i < header.length; i++) {
				if (header[i].equalsIgnoreCase(columnName)) {
					columnIndex = i;
					break;
				}
			}
		}

		if (columnIndex != -1) {
			for (String[] row : data) {
				if (columnIndex < row.length) {
					columnData.add(row[columnIndex]);
				}
			}
		}

		return columnData;
	}
	
	public static void addColumn(List<String[]> data, Map<String, Integer> headerNames, String column) {
		addColumn(data, headerNames, column, false);
	}
	
	public static void addColumn(List<String[]> data, Map<String, Integer> headerNames, String column, boolean useExisting) {
		if (!headerNames.containsKey(column)) {
			headerNames.put(column, headerNames.size());
		} else if (!useExisting) {
			String col_tmp = column;
			int count = 2;
			while (headerNames.containsKey(col_tmp)) {
				col_tmp = column + "_" + count;
				count++;
			}
			headerNames.put(col_tmp, headerNames.size());
		} else {
			return;
		}
		for (int i = 0; i < data.size(); i++) {
			String[] newArr = Arrays.copyOf(data.get(i), data.get(i).length + 1);
			newArr[newArr.length - 1] = "";
			data.set(i, newArr);
		}
	}

	public static String getValue(List<String[]> data, int rowIndex, int columnIndex) {
		String value = "";

		if (rowIndex >= 0 && rowIndex < data.size()) {
			String[] row = data.get(rowIndex);
			if (columnIndex >= 0 && columnIndex < row.length) {
				value = row[columnIndex];
			}
		}

		return value;
	}

	public static void updateRow(List<String[]> data, String updateColumn, String oldValue, String newValue) {
		// Assume that the first row contains the headers
		int columnIndex = -1;
		for (int i = 0; i < data.get(0).length; i++) {
			if (data.get(0)[i].equalsIgnoreCase(updateColumn)) {
				columnIndex = i;
				break;
			}
		}

		// If a column could be detected, update the data
		if (columnIndex != -1) {
			for (int i = 1; i < data.size(); i++) { // // Start index is 1 to jump over the header row
				if (data.get(i)[columnIndex].equalsIgnoreCase(oldValue)) {
					// Update the value of the detected row 
					data.get(i)[columnIndex] = newValue;
					break; // Assume that there is only one hit, so jump out the loop
				}
			}
		}
	}

	public static String asString(List<String[]> data) {
		StringBuilder result = new StringBuilder();

		for (String[] row : data) {
			for (String cell : row) {
				result.append(cell).append("\t");
			}
			result.append("\n");
		}

		return result.toString();
	}

	public static void writeFile(List<String[]> data, String outputPath, String delimiter, Charset encoding) {
		File outputFile = new File(outputPath);
		if (!outputFile.exists()) {
			try {
				if (outputFile.createNewFile()) {
					MLogger.getInstance().log(Level.INFO, "New CSV file was created: " + outputPath, CSVUtil.class.getSimpleName(), "writeFile");
				} else {
					MLogger.getInstance().log(Level.SEVERE, "Error when creating the new CSV file.", CSVUtil.class.getSimpleName(), "writeFile");
					return;
				}
			} catch (IOException e) {
				MLogger.getInstance().log(Level.SEVERE, e);
				return;
			}

			try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath, encoding))) {
				for (String[] row : data) {
					StringBuilder line = new StringBuilder();
					for (String value : row) {
						line.append(value).append(delimiter);
					}
					line.deleteCharAt(line.length() - 1); // Remove last delimiter
					writer.write(line.toString());
					writer.newLine();
				}
			} catch (IOException e) {
				MLogger.getInstance().log(Level.SEVERE, e);
			}
		}
	}

}
