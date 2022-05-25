package RegressionTest.IO;

import java.io.IOException;
import java.nio.file.NoSuchFileException;

import org.opentdk.api.io.FileUtil;
import org.opentdk.api.io.XFileWriter;

import RegressionTest.BaseRegression;

public class RT_XFileWiter extends BaseRegression {

	public static void main(String[] args) {
		new RT_XFileWiter();
	}

	@Override
	public void runTest() {
		String path = "testdata/XFileWriter.txt";
		XFileWriter writer = null;

		try {
			FileUtil.deleteFileOrFolder(path);
		} catch (NoSuchFileException e) {
			System.out.println(path + " - does not exist. Will be created.");
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			FileUtil.createFile(path);
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		String content = "DATE;DURMILLIS;PERCENTILE;COUNT";
		try {
			writer = new XFileWriter(path);
			writer.writeLine(new String[] { content });
			testResult(FileUtil.getRowsAsString(path).strip(), "File content", content);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (writer != null) {
					writer.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
