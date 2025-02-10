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
    public void readData(InputStream stream) throws IOException {
        rows = new ArrayList<>();
        List<String> content = null;
        if (stream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(stream);
            Stream<String> streamOfString = new BufferedReader(inputStreamReader).lines();
            content = streamOfString.collect(Collectors.toList());

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
        CSVUtil.writeFile(rows, outputFile.toFile(), delimiter, StandardCharsets.UTF_8);
    }

    public String[] getRow(int rowIndex) {
        if (rows.isEmpty()) {
            return null;
        }
        // Check if the row index is valid (header is at index 0, so start at 1)
        if (rowIndex >= 1 && rowIndex < rows.size()) {
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
        for(String[] row : rows) {
            if(checkValuesFilter(row, filter)) {
                ret.add(row);
            }
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
        // Check if the row index is valid (header is at index 0, so start at 1)
        if (rowIndex >= 1 && rowIndex < rows.size()) {
            return rows.get(rowIndex)[columnIndex];
        }
        throw new DataContainerException("Row index is out of range");
    }

    public String getValue(Filter filter) {
        return null;
    }

    public List<String> getValues(String columnHeader) {
        if (rows.isEmpty()) {
            return null;
        }
        return CSVUtil.getColumn(rows, columnHeader);
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

    /**
     * Checks if the filter rules match to the values of the given data set.
     *
     * @param values String array with all values of a defined data set (row).
     * @param fltr   Object of type {@link Filter}, which includes one or more filter rules
     * @return true = values match to the filter; false = values don't match to the filter
     */
    private boolean checkValuesFilter(String[] values, Filter fltr)  {
        boolean returnCode = false;
        if (fltr.getFilterRules().isEmpty()) {
            returnCode = true; // No filter defined
        } else {
            for (FilterRule fr : fltr.getFilterRules()) {
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