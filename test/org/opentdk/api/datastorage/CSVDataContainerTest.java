package org.opentdk.api.datastorage;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CSVDataContainerTest {

    private static final String content = "ID,Name,Alter,Land\n" + "1,Emma,42,Deutschland\n" + "2,Chris,29,Schweiz\n" + "3,Hannah,35,Österreich\n" + "4,Ben,19,Frankreich\n" + "5,Greta,51,Spanien\n" + "6,Felix,27,Italien\n" + "7,Julia,22,Deutschland\n" + "8,David,41,Österreich\n" + "9,Ivan,60,Schweiz\n" + "10,Anna,31,Frankreich\n";

    @BeforeTest
    public void prepare() {
        // Prepare
        try {
            Files.createDirectory(Paths.get("tmp"));
        } catch(IOException ignored) {}
    }

    @Test
    public void getRow() throws IOException {
        String filePath = "tmp/CSVDataContainerTest_getRow.csv";
        Path tempFile = Paths.get(filePath);
        try {
            Files.createFile(tempFile);
        } catch(IOException ignored) {}
        Files.writeString(tempFile, content);

        String delimiter = ",";
        DataContainer dc = DataContainer.newContainer(EContainerFormat.CSV);
        dc.tabInstance().setDelimiter(delimiter);
        dc.readData(tempFile);
        String actual = dc.tabInstance().getRow(1, "Land");
        String expected = "Schweiz";
        Assert.assertEquals(actual, expected, "Read incorrect value from csv");


    }


}
