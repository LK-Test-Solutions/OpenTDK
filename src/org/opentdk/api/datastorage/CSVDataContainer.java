package org.opentdk.api.datastorage;

import lombok.Getter;
import lombok.Setter;

import org.opentdk.api.exception.DataContainerException;
import org.opentdk.api.filter.Filter;
import org.opentdk.api.filter.FilterRule;
import org.opentdk.api.util.CSVUtil;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    @Getter @Setter
    private String delimiter = ";";

    public static CSVDataContainer newInstance() {
        return new CSVDataContainer();
    }

    protected CSVDataContainer() {
        rows = new ArrayList<>();
        headers = new ArrayList<>();
        headerMap = new HashMap<>();
    }

    @Override
    public String asString() {
        StringBuilder result = new StringBuilder();
        for (String[] row : rows) {
            for (String cell : row) {
                result.append(cell).append("\t");
            }
            result.append("\n");
        }
        return result.toString();
    }

    @Override
    public void readData(Path sourceFile) throws IOException {
        rows = CSVUtil.readFile(sourceFile.toFile(), delimiter, StandardCharsets.UTF_8);
        headers = Arrays.asList(rows.getFirst());
//        rows.removeFirst(); // Headers are stored and can be removed from the rows list
        for(int i = 0; i < headers.size(); i++) {
            headerMap.put(headers.get(i), i);
        }
    }

    @Override
    public void readData(InputStream stream) throws IOException {
        rows = new ArrayList<>();
        List<String> content = null;
        if (stream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(stream);
            Stream<String> streamOfString = new BufferedReader(inputStreamReader).lines();
            content = streamOfString.toList();

            streamOfString.close();
            inputStreamReader.close();
        }
        if(content != null) {
            for(String row : content) {
                rows.add(row.split(delimiter));
            }
        }
    }

    @Override
    public void writeData(Path outputFile) throws IOException {
        CSVUtil.writeFile(rows, outputFile, delimiter, StandardCharsets.UTF_8);
    }

    // GET

    public String[] getRow(int rowIndex) {
        if (rows.isEmpty()) {
            return null;
        }
        if (rowIndex >= 0 && rowIndex < rows.size()) {
            return rows.get(rowIndex);
        }
        throw new DataContainerException("Row index is out of range");
    }

    public String[] getRow(Filter filter) {
        if (rows.isEmpty()) {
            return null;
        }
        for(String[] row : rows) {
            if(checkValuesFilter(row, filter)) {
                return row;
            }
        }
        return null;
    }

    public List<String[]> getRows(Filter filter) {
        if (rows.isEmpty()) {
            return null;
        }
        List<String[]> ret = new ArrayList<>();
        int rowIndex = 0;
        for(String[] row : rows) {
            // Column header row needs no check
            if(rowIndex > 0 && checkValuesFilter(row, filter)) {
                ret.add(row);
            }
            rowIndex++;
        }
        return ret;
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
        if (rowIndex >= 0 && rowIndex < rows.size()) {
            return rows.get(rowIndex)[columnIndex];
        }
        throw new DataContainerException("Row index is out of range");
    }

    public List<String> getColumn(String columnHeader) {
        if (rows.isEmpty()) {
            return null;
        }
        List<String> ret = CSVUtil.getColumn(rows, columnHeader);
        ret.removeFirst(); // Remove column header
        return ret;
    }

    public List<String> getColumn(String columnHeader, Filter filter) {
        List<String[]> filteredRows = getRows(filter);
        filteredRows.addFirst(headers.toArray(String[]::new));
        List<String> ret = CSVUtil.getColumn(filteredRows, columnHeader);
        ret.removeFirst();
        return ret;
    }

    // ADD

    public void addRow(List<String> row) throws IOException {
        rows.add(row.toArray(String[]::new));
    }

    public void addRow(String[] row) throws IOException {
        rows.add(row);
    }

    public void addColumn(String column) {
        addColumn(column, false);
    }

    public void addColumn(String column, boolean useExisting) {
        CSVUtil.addColumn(rows, headerMap, column, useExisting, 0);
    }

    // SET

    public void setRow(String updateColumn, String oldValue, String newValue) {
        CSVUtil.updateRow(rows, updateColumn, oldValue, newValue);
    }

    // DELETE

    public void deleteValue(String headerName) {
        int headerIndex = headerMap.get(headerName);
        rows.getFirst()[headerIndex] = null;
    }

    public void deleteRow(int index) {
        rows.remove(index);
    }

    public void deleteRows(Filter filter) {
        int[] indexes = getRowsIndexes(filter);
        for (int index : indexes) {
            rows.remove(index);
        }
    }

    // HELPER

    private int[] getRowsIndexes(Filter filter) {
        StringBuilder indexBuffer = new StringBuilder();
        for (int i = 0; i < rows.size(); i++) {
            if (checkValuesFilter(rows.get(i), filter)) {
                if (!indexBuffer.isEmpty()) {
                    indexBuffer.append(";");
                }
                indexBuffer.append(i);
            }
        }
        if (!indexBuffer.isEmpty()) {
            return Arrays.stream(indexBuffer.toString().split(";")).mapToInt(Integer::parseInt).toArray();
        } else {
            return new int[] {};
        }
    }

    /**
     * Checks if the filter rules match to the values of the given data set.
     *
     * @param values String array with all values of a defined data set (row).
     * @param filter   Object of type {@link Filter}, which includes one or more filter rules
     * @return true = values match to the filter; false = values don't match to the filter
     */
    private boolean checkValuesFilter(String[] values, Filter filter)  {
        boolean returnCode = false;
        if (filter.getFilterRules().isEmpty()) {
            returnCode = true; // No filter defined
        } else {
            for (FilterRule fr : filter.getFilterRules()) {
                // Wild cards * and % will accept any value
                if (fr.getValue() != null) {
                    if ((fr.getValue().equals("*")) || (fr.getValue().equals("%"))) {
                        returnCode = true;
                        break;
                    }
                }
                // check values against the filter rules
                returnCode = fr.checkValue(values[headerMap.get(fr.getHeaderName())]);
                if (!returnCode) {
                    // skip check and return false, in case that one of the rules fails
                    break;
                }
            }
        }
        return returnCode;
    }
}