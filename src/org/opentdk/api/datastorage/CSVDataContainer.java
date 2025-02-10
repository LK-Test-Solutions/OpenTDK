package org.opentdk.api.datastorage;

import lombok.Getter;
import lombok.Setter;

import org.opentdk.api.exception.DataContainerException;
import org.opentdk.api.filter.Filter;
import org.opentdk.api.util.CSVUtil;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author FME (LK Test Solutions)
 */
public class CSVDataContainer implements SpecificContainer {

    @Getter
    private List<String[]> rows;

    @Getter @Setter
    private List<String> headers;

    @Getter
    private Map<String, Integer> headerMap;

    @Getter
    @Setter
    private String delimiter = ";";

    public static CSVDataContainer newInstance() {
        return new CSVDataContainer();
    }

    protected CSVDataContainer() {
        rows = new ArrayList<>();
    }

    @Override
    public String asString() {
        return null;
    }

    @Override
    public void readData(Path sourceFile) throws IOException {
        rows = CSVUtil.readFile(sourceFile.toFile(), delimiter, StandardCharsets.UTF_8);
        headers = Arrays.asList(rows.get(0));
        headerMap = new HashMap<>();
        for(int i = 0; i < headers.size(); i++) {
            headerMap.put(headers.get(i), i);
        }
    }

    @Override
    public void readData(InputStream stream) {

    }

    @Override
    public void writeData(Path outputFile) throws IOException {
        CSVUtil.writeFile(rows, outputFile.toFile(), delimiter, StandardCharsets.UTF_8);
    }

    public List<String> getRow(int rowIndex) {
        if (rows.isEmpty()) {
            return null;
        }
        // Check if the row index is valid (header is at index 0, so start at 1)
        if (rowIndex >= 1 && rowIndex < rows.size()) {
            return Arrays.asList(rows.get(rowIndex));
        }
        throw new DataContainerException("Row index is out of range");
    }

    public String getValue(int rowIndex, String columnHeader) {
        if (rows.isEmpty()) {
            return null;
        }
        int columnIndex;
        try {
            columnIndex = headerMap.get(columnHeader);
        } catch (NullPointerException e) {
            throw new DataContainerException("Column '" + columnHeader + "' not found.");
        }
        // Check if the row index is valid (header is at index 0, so start at 1)
        if (rowIndex >= 1 && rowIndex < rows.size()) {
            return rows.get(rowIndex)[columnIndex];
        }
        throw new DataContainerException("Row index is out of range");
    }

    public void addRow(List<String> row) throws IOException {
        rows.add(row.toArray(String[]::new));
    }

    public void addRow(String[] row) throws IOException {
        rows.add(row);
    }

    public void deleteRow(String params) {
    }

    public void setRow(String parameterName, String value, Filter fltr, boolean allOccurences) {
    }
}