package RegressionTests.IO;

import java.io.IOException;
import java.nio.file.NoSuchFileException;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opentdk.api.io.FileUtil;
import org.opentdk.api.io.XFileWriter;

public class RT_XFileWiter {

	private static final String path = "testdata/XFileWriter.txt";
	private XFileWriter writer;

	@BeforeClass
	public static void init() throws IOException {
		try {
			FileUtil.deleteFileOrFolder(path);
		} catch (NoSuchFileException e) {
			System.out.println(path + " - does not exist. Will be created.");
		}
		FileUtil.createFile(path);
	}

	private void testSettingsField(String actual, String fieldName, String expected) {
		if (actual.contentEquals(expected)) {
			System.out.println("Success: " + fieldName + " == " + actual);
		} else {
			System.out.println("Failure: " + fieldName + " is \"" + actual + "\" but should be \"" + expected + "\"");
			Assert.fail();
		}
	}

	@Test
	public void test() {		
		String content = "DATE;DURMILLIS;PERCENTILE;COUNT";
		try {
			writer = new XFileWriter(path);
			writer.writeLine(new String[] { content });
			testSettingsField(FileUtil.getRowsAsString(path).strip(), "File content", content);
		} catch (IOException e) {
			Assert.fail();
			e.printStackTrace();
		} finally {
			try {
				writer.close();
			} catch (IOException e) {
				Assert.fail();
				e.printStackTrace();
			}
		}	
	}
}
