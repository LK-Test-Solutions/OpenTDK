package org.opentdk.api.datastorage;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.opentdk.api.exception.DataContainerException;
import org.opentdk.api.filter.Filter;

import java.io.*;
import java.nio.file.Path;
import java.util.*;

/**
 * @author FME (LK Test Solutions)
 */
public class CSVDataContainer implements SpecificContainer {

    @Getter
    private List<CSVRecord> rows;

    @Getter
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
        try (Reader reader = new FileReader(sourceFile.toFile()); CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader())) {
			rows = csvParser.getRecords();
            headers = csvParser.getHeaderNames();
            headerMap = csvParser.getHeaderMap();
        }
    }

    @Override
    public void readData(InputStream stream) {

    }

    @Override
    public void writeData(Path outputFile) throws IOException {

    }

    public String getRow(String columnHeader, Filter filer) {
        return null;
    }

    public String getRow(int rowIndex, String columnHeader) {
        if (rows.isEmpty()) {
            return null;
        }
        int columnIndex;
        try {
            columnIndex = headerMap.get(columnHeader);
        } catch(NullPointerException e) {
            throw new DataContainerException("Column '" + columnHeader + "' not found.");
        }
        // Check if the row index is valid (header is at index 0, so start at 1
        if (rowIndex >= 1 && rowIndex < rows.size()) {
            return rows.get(rowIndex).get(columnIndex);
        }
        throw new DataContainerException("Row index is out of range");
    }

    public void addRow(String name) {
    }

    public void deleteRow(String params) {
    }

    public void setRow(String parameterName, String value, Filter fltr, boolean allOccurences) {
    }
}