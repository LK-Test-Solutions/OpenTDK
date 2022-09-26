package RegressionTest.IO;

import java.io.IOException;
import java.nio.file.FileSystemException;
import java.util.List;

import org.opentdk.api.datastorage.DataContainer;
import org.opentdk.api.io.FileUtil;

import RegressionTest.BaseRegression;

public class RT_FileUtil extends BaseRegression {
	
	public static void main(String[] args) {
		new RT_FileUtil();
	}

	@Override
	public void runTest() {
		
		String path = "conf/FileUtil.xml";
		String path_copy = "conf/FileUtilCopy.xml";
		String path_rename = "conf/FileUtilRenamed.xml";
		String path_txt = "conf/FileUtil.txt";
		
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
		
		boolean exists = FileUtil.checkDir(path, false);
		BaseRegression.testResult(String.valueOf(exists), "CHECK_DIR", "true");
		// CHECK EXISTING FILE
		boolean fileExists = FileUtil.checkFile(path);
		BaseRegression.testResult(String.valueOf(fileExists), "CHECK_FILE", "true");

		// COPY
		try {
			FileUtil.copyFile(path, path_copy);
			exists = FileUtil.checkDir(path_copy);
			BaseRegression.testResult(String.valueOf(exists), "CHECK_DIR_COPY", "true");
		} catch (IOException e) {
			System.err.println("CHECK_DIR_COPY failed.");
			return;
		}

		// RENAME 
		try {
			FileUtil.renameFile(path, path_rename);
			exists = FileUtil.checkDir(path_rename);
			BaseRegression.testResult(String.valueOf(exists), "CHECK_DIR_RENAME", "true");
		} catch (IOException e) {
			if (e instanceof FileSystemException) {
				System.err.println(path + " is blocked by another process and cannot be renamed at the moment. Continue.");
			}
		}

		// DELETE
		try {
			FileUtil.deleteFile(path_rename);
			exists = FileUtil.checkDir(path_rename);
			BaseRegression.testResult(String.valueOf(exists), "CHECK_DIR_DELETE", "false");
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
				BaseRegression.testResult(String.valueOf(isTextFile), "CHECK_FILE_TEXT", "true");
			} else {
				System.out.println("FileUtil.isTextFile: File is blocked and not readable");
			}
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}

		String content = FileUtil.getRowsAsString(path_txt, 3).trim();
		BaseRegression.testResult(content.length(), "READ_STRING_SIZE", 14);

		List<String> rows = FileUtil.getRowsAsList(path_txt);
		BaseRegression.testResult(String.valueOf(rows.size()), "READ_LIST_SIZE", "7");
		
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
