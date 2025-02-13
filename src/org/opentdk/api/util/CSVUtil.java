package org.opentdk.api.util;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class CSVUtil {

	public static List<String[]> readFile(File filePath, String delimiter, Charset encoding) throws IOException {
		List<String[]> data = new ArrayList<>();

		try (BufferedReader br = new BufferedReader(new FileReader(filePath, encoding))) {
			String line;
			while ((line = br.readLine()) != null) {
				String[] values = line.split(delimiter);
				data.add(values);
			}
		}
		return data;
	}

	public static List<String[]> filterData(List<String[]> data, String columnName, String filterValue) {
		List<String[]> filteredData = new ArrayList<>();

		// Find the index in the column
		int columnIndex = -1;
		if (!data.isEmpty()) {
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
            rowData.addAll(Arrays.asList(row));
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
		if (!data.isEmpty()) {
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
		addColumn(data, headerNames, column, false, 0);
	}
	
	public static void addColumn(List<String[]> data, Map<String, Integer> headerNames, String column, boolean useExisting, int startIndex) {
		String newColumn = column;
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
			newColumn = col_tmp;
		} else {
			return;
		}
		for (int i = startIndex; i < data.size(); i++) {
			String[] newArr = Arrays.copyOf(data.get(i), data.get(i).length + 1);
			if(i == 0) {
				newArr[newArr.length - 1] = newColumn;
			} else {
				newArr[newArr.length - 1] = "";
			}
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
		for (int i = 0; i < data.getFirst().length; i++) {
			if (data.getFirst()[i].equalsIgnoreCase(updateColumn)) {
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

	public static void writeFile(List<String[]> data, Path outputFile, String delimiter, Charset encoding) throws IOException {
		Files.createDirectories(outputFile.getParent());
		if(Files.notExists(outputFile)) {
			Files.createFile(outputFile);
		}
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile.toFile(), encoding))) {
			for (String[] row : data) {
				StringBuilder line = new StringBuilder();
				for (String value : row) {
					line.append(value).append(delimiter);
				}
				line.deleteCharAt(line.length() - 1); // Remove last delimiter
				writer.write(line.toString());
				writer.newLine();
			}
		}
	}

}
