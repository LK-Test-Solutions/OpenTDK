package org.opentdk.api.datastorage;


import com.opencsv.exceptions.CsvException;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;


public class GeneratedCSVDataContainer implements SpecificContainer {

    private List<String[]> csvData; // In-memory storage for CSV data

    public GeneratedCSVDataContainer() {
        csvData = new ArrayList<>();
    }

    @Override
    public String asString() {
        StringBuilder builder = new StringBuilder();
        for (String[] row : csvData) {
            builder.append(String.join(",", row)).append("\n");
        }
        return builder.toString().trim();
    }

    @Override
    public void readData(Path sourceFile) throws IOException {
        try (var reader = java.nio.file.Files.newBufferedReader(sourceFile); var csvReader = new com.opencsv.CSVReader(reader)) {
            csvData.clear();
            try {
                csvData.addAll(csvReader.readAll());
            } catch (CsvException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void readData(InputStream stream) throws IOException {
        try (var reader = new java.io.InputStreamReader(stream); var csvReader = new com.opencsv.CSVReader(reader)) {
            csvData.clear();
            try {
                csvData.addAll(csvReader.readAll());
            } catch (CsvException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void writeData(Path outputFile) throws IOException {
        try (var writer = java.nio.file.Files.newBufferedWriter(outputFile); var csvWriter = new com.opencsv.CSVWriter(writer)) {
            csvWriter.writeAll(csvData);
        }
    }

    public List<String[]> getRows(String filter) {
        List<String[]> filteredRows = new ArrayList<>();
        for (String[] row : csvData) {
            for (String cell : row) {
                if (cell.contains(filter)) {
                    filteredRows.add(row);
                    break;
                }
            }
        }
        return filteredRows;
    }

    public void setValues(int row, int column, String value) {
        if (row < csvData.size() && column < csvData.get(row).length) {
            csvData.get(row)[column] = value;
        }
    }

    public void deleteRows(String filter) {
        csvData.removeIf(row -> {
            for (String cell : row) {
                if (cell.contains(filter)) {
                    return true;
                }
            }
            return false;
        });
    }

    public void addRow(String[] rowData) {
        csvData.add(rowData);
    }
}
