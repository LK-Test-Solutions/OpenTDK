package org.opentdk.api.datastorage;

import org.opentdk.api.filter.EOperator;
import org.opentdk.api.filter.Filter;
import org.opentdk.api.filter.FilterRule;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class CSVDataContainerTest {
    private static final String content = "ID,Name,Alter,Land\n" + "1,Emma,42,Deutschland\n" + "2,Chris,29,Schweiz\n" + "3,Hannah,35,Österreich\n" + "4,Ben,19,Frankreich\n" + "5,Greta,51,Spanien\n" + "6,Felix,27,Italien\n" + "7,Julia,null,Deutschland\n" + "8,David,41,Österreich\n" + "9,'Ivan',60,Schweiz\n" + "10,Anna,31,Frankreich\n";

    @BeforeTest
    public void prepare() {
        // Prepare
        try {
            Files.createDirectory(Paths.get("tmp"));
        } catch(IOException ignored) {}
    }

    @Test
    public void getHeaders() throws IOException {
        DataContainer dc = prepareFile();
        List<String> actual = dc.tabInstance().getHeaders();
        List<String> expected = List.of("ID", "Name", "Alter", "Land");
        Assert.assertEquals(actual, expected);
        System.out.println("Success: Headers are " + actual);
    }

    @Test
    public void getRow() throws IOException {
        DataContainer dc = prepareFile();
        List<String> actual = Arrays.asList(dc.tabInstance().getRow(3));
        List<String> expected = List.of("3", "Hannah" , "35", "Österreich");
        Assert.assertEquals(actual, expected);
        System.out.println("Success: Row is " + actual);
        // Filter
        Filter filter = new Filter();
        filter.addFilterRule("Name", "anna", EOperator.CONTAINS);
        actual = Arrays.asList(dc.tabInstance().getRow(filter));
        Assert.assertEquals(actual, expected);
        System.out.println("Success: Row is " + actual);

    }

    @Test
    public void getRows() throws IOException {
        DataContainer dc = prepareFile();
        Filter filter = new Filter();
        filter.addFilterRule("Name", "Ben", EOperator.EQUALS);
        List<String> actual = List.of(dc.tabInstance().getRows(filter).getFirst());
        List<String> expected = List.of("4", "Ben" , "19", "Frankreich");
        Assert.assertEquals(actual, expected);
        System.out.println("Success: Row is " + actual);
    }

    @Test
    public void getValue() throws IOException {
        DataContainer dc = prepareFile();
        String actual = dc.tabInstance().getValue(2, "Land");
        String expected = "Schweiz";
        Assert.assertEquals(actual, expected);
        System.out.println("Success: Country is " + actual);
    }

    @Test
    public void getColumn() throws IOException {
        DataContainer dc = prepareFile();
        List<String> actual = dc.tabInstance().getColumn("Alter");
        List<String> expected = List.of("42", "29" , "35", "19", "51", "27", "null", "41", "60", "31");
        Assert.assertEquals(actual, expected);
        System.out.println("Success: Ages are " + actual);
        // Filter
        Filter filter = new Filter();
        filter.addFilterRule("Alter", "50", EOperator.GREATER_OR_EQUAL_THAN);
        actual = dc.tabInstance().getColumn("Name", filter);
        expected = List.of("Greta", "'Ivan'");
        Assert.assertEquals(actual, expected);
        System.out.println("Success: Country is " + actual);
    }

    @Test
    public void addRow() throws IOException {
        DataContainer dc = prepareFile();
        dc.tabInstance().addRow(List.of("x", "Fabian", "29", "Bayern"));
        writeFile(dc);
        String actual = dc.tabInstance().getValue(dc.tabInstance().getRows().size() - 1, "Land");
        String expected = "Bayern";
        Assert.assertEquals(actual, expected);
        System.out.println("Success: Country is " + actual);
    }

    @Test
    public void addColumn() throws IOException {
        DataContainer dc = prepareFile();
        dc.tabInstance().addColumn("Geschlecht");
        dc.tabInstance().addColumn("Name");
        writeFile(dc);
        List<String> actual = dc.tabInstance().getColumn("Name_2");
        List<String> expected = List.of("", "", "", "", "", "", "", "", "", "");
        Assert.assertEquals(actual, expected);
        System.out.println("Success: column created " + actual);
    }

    // TODO setRow

    // TODO delete 3x

    private DataContainer prepareFile() throws IOException {
        String filePath = "tmp/CSVDataContainerTest.csv";
        Path tempFile = Paths.get(filePath);
        try {
            Files.createFile(tempFile);
        } catch(IOException ignored) {}

        Files.writeString(tempFile, content);
        String delimiter = ",";
        DataContainer dc = DataContainer.newContainer(EContainerFormat.CSV);
        dc.tabInstance().setDelimiter(delimiter);
        dc.readData(tempFile);
        return dc;
    }

    private void writeFile(DataContainer dc) throws IOException {
        String filePath = "tmp/CSVDataContainerTest.csv";
        Path tempFile = Paths.get(filePath);
        dc.writeData(tempFile);
    }


}
