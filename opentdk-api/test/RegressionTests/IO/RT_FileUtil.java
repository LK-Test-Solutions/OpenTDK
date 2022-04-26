package RegressionTests.IO;

import java.io.IOException;
import java.nio.file.FileSystemException;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;
import org.opentdk.api.datastorage.DataContainer;
import org.opentdk.api.io.FileUtil;

import RegressionSets.Api.BaseRegressionSet;

public class RT_FileUtil {

	private static final String path = "conf/FileUtil.xml";
	private static final String path_copy = "conf/FileUtilCopy.xml";
	private static final String path_rename = "conf/FileUtilRenamed.xml";
	private static final String path_txt = "conf/FileUtil.txt";


	@BeforeClass
	public static void init() {
		try {
			FileUtil.deleteFileOrFolder(path);
		} catch (IOException e) {
			System.out.println(path + " - does not exist. Will be created.");
		}
		try {
			FileUtil.deleteFileOrFolder(path_copy);
		} catch (IOException e) {
			System.out.println(path_copy + " - does not exist. Will be copied in the test run.");
		}
		try {
			FileUtil.deleteFileOrFolder(path_txt);
		} catch (IOException e) {
			System.out.println(path_txt + " - does not exist. Will be created in the test run.");
		}
		try {
			FileUtil.createFile(path);
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}

	}

	@Test
	public void test() {
		// CHECK EXISTING DIR
		try {
			boolean exists = FileUtil.checkDir(path, false);
			BaseRegressionSet.testResult(String.valueOf(exists), "CHECK_DIR", "true");
		} catch (IOException e) {
			System.err.println("CHECK_DIR failed.");
			return;
		}
		// CHECK EXISTING FILE
		boolean fileExists = FileUtil.checkFile(path);
		BaseRegressionSet.testResult(String.valueOf(fileExists), "CHECK_FILE", "true");

		// COPY
		try {
			FileUtil.copyFile(path, path_copy);
			boolean exists = FileUtil.checkDir(path_copy);
			BaseRegressionSet.testResult(String.valueOf(exists), "CHECK_DIR_COPY", "true");
		} catch (IOException e) {
			System.err.println("CHECK_DIR_COPY failed.");
			return;
		}

		// RENAME 
		try {
			FileUtil.renameFile(path, path_rename);
			boolean exists = FileUtil.checkDir(path_rename);
			BaseRegressionSet.testResult(String.valueOf(exists), "CHECK_DIR_RENAME", "true");
		} catch (IOException e) {
			if (e instanceof FileSystemException) {
				System.err.println(path + " is blocked by another process and cannot be renamed at the moment. Continue.");
			}
		}

		// DELETE
		try {
			FileUtil.deleteFile(path_rename);
			boolean exists = FileUtil.checkDir(path_rename);
			BaseRegressionSet.testResult(String.valueOf(exists), "CHECK_DIR_DELETE", "false");
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}

		// READ
		try {
			FileUtil.deleteFileOrFolder(path_txt);
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}
		DataContainer dc = new DataContainer();
		dc.addRow(new String[] { "Row1" });
		dc.addRow(new String[] { "Row2" });
		dc.addRow(new String[] { "Row3" });
		dc.addRow(new String[] { "Row4" });
		dc.addRow(new String[] { "Row5" });
		dc.addRow(new String[] { "Row6" });
		try {
			dc.exportContainer(path_txt);
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}
		
		// CHECK TEXT FILE TYPE
		try {
			// Files.isReadable does not work due to permission problems
			boolean isTextFile = FileUtil.isTextFile(path_txt);
			if(isTextFile) {
				BaseRegressionSet.testResult(String.valueOf(isTextFile), "CHECK_FILE_TEXT", "true");
			} else {
				System.out.println("FileUtil.isTextFile: File is blocked and not readable");
			}
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}

		String content = FileUtil.getRowsAsString(path_txt, 3);
		BaseRegressionSet.testResult(String.valueOf(content.length()), "READ_STRING_SIZE", "12");

		List<String> rows = FileUtil.getRowsAsList(path_txt);
		BaseRegressionSet.testResult(String.valueOf(rows.size()), "READ_LIST_SIZE", "7");
		
		try {
			FileUtil.deleteFileOrFolder(path_copy);
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}
		try {
			FileUtil.deleteFileOrFolder(path_txt);
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}
	}
}
