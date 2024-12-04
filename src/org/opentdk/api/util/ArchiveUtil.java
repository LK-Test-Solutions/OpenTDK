/* 
 * BSD 2-Clause License
 * 
 * Copyright (c) 2022, LK Test Solutions GmbH
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. 
 */
package org.opentdk.api.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.util.logging.Level;

import org.apache.commons.lang3.StringUtils;
import org.opentdk.api.logger.MLogger;

/**
 * This class gets used to compress and extract (or generally archive) directories by calling the 7
 * ZIP executable.<br>
 * <br>
 * Compress example:
 * 
 * <pre>
 * ArchiveUtil archive = ArchiveUtil.newInstance();
 * archive.setFileNames("*.json *.yaml *.properties *.xml *.csv");
 * archive.runProcess("testdata\\RegressionTestData", "C:\\Program Files\\7 Zip\\7z.exe", "..\\RegressionTestData.7z");
 * </pre>
 * 
 * This would compress all files with the defined extension in the folder 'RegressionTestData' as
 * .7z archive with name 'RegressionTestData'.<br>
 * <br>
 * 
 * Extract example:
 * 
 * <pre>
 * ArchiveUtil archive = ArchiveUtil.newInstance();
 * archive.setSwitches("{@literal -}o\"RegressionTestData_Extracted\" {@literal -}t7z {@literal -}y {@literal -}x!*.cmd {@literal -}x!config.txt {@literal -}x!*.html");
 * archive.runProcess("testdata", "C:\\Program Files\\7{@literal -}Zip\\7z.exe", "RegressionTestData.7z", ArchiveCommand.Extract, true);
 * </pre>
 * 
 * This would jump to the folder 'testdata' and extract the archive 'RegressionTestData.7z' as
 * 'RegressionTestData_Extracted'. Note that the output name has to be set as switch. The last
 * parameter 'true' triggers the 'print to console' option.<br>
 * <br>
 * 
 * To get all available 7 ZIP settings go to the installation folder via command prompt and type
 * 7z.<br>
 * <br>
 * 
 * @author FME (LK Test Solutions)
 */
public class ArchiveUtil {
	/**
	 * Possibility to include/exclude file types from the compression/extraction. Default is *.* which
	 * allows all file names and all file extensions. E.g. *.json *.yaml would only allow JSON and YAML
	 * files with any file name.
	 */
	private String fileNames = "*.*";
	/**
	 * Possibility to define all other options of the compression like archive type or compression rate.
	 * Default is only {@literal -t7z} to set the 7z archive type.
	 */
	private String switches = "-t7z";

	/**
	 * Possibility to differentiate between the 7 ZIP compression commands like add, delete, list or
	 * update.
	 */
	public enum ArchiveCommand {
		Add("a"), Delete("d"), List("l"), Update("u"), Extract("e");

		private String shortcut;

		ArchiveCommand(String abrev) {
			shortcut = abrev;
		}

		public String getShortcut() {
			return shortcut;
		}
	}

	/**
	 * Invisible constructor that gets called when using the {@link #newInstance()} method.
	 */
	private ArchiveUtil() {

	}

	/**
	 * @return a new instance of the ArchiveUtil class to work with
	 */
	public static ArchiveUtil newInstance() {
		return new ArchiveUtil();
	}

	/**
	 * Compress a directory with the 7 ZIP application via command line. Only usable on Windows.
	 * 
	 * @param currentDirectory The path to the folder with the files to compress (folder name included)
	 * @param zipExecutable    The full name (path + name) of the 7 ZIP executable
	 * @param archiveName      The name of the compressed folder (with extension)
	 * @return {@literal -}1: Return value of the process could not be determined, 0: Execution
	 *         succeeded, {@literal >}1: Execution failed
	 * @throws IllegalArgumentException in case of null or blank parameter or invalid directory
	 * @throws UncheckedIOException when the process execution failed
	 */
	public int runProcess(String currentDirectory, String zipExecutable, String archiveName) throws IOException, InterruptedException {
		return runProcess(currentDirectory, zipExecutable, archiveName, ArchiveCommand.Add);
	}

	/**
	 * Compress a directory with the 7 ZIP application via command line. Only usable on Windows.
	 * 
	 * @param currentDirectory The path to the folder with the files to compress (folder name included)
	 * @param zipExecutable    The full name (path + name) of the 7 ZIP executable
	 * @param archiveName      The name of the compressed folder (with extension)
	 * @param command          One of the compress commands in {@link ArchiveCommand} with default ADD
	 *                         (create archive or add if already exists)
	 * @return {@literal -}1: Return value of the process could not be determined, 0: Execution
	 *         succeeded, {@literal >}1: Execution failed
	 * @throws IllegalArgumentException in case of null or blank parameter or invalid directory
	 * @throws UncheckedIOException when the process execution failed
	 */
	public int runProcess(String currentDirectory, String zipExecutable, String archiveName, ArchiveCommand command) throws IOException, InterruptedException {
		return runProcess(currentDirectory, zipExecutable, archiveName, command, false);
	}

	/**
	 * Compress a directory with the 7 ZIP application via command line. Only usable on Windows.
	 * 
	 * @param currentDirectory The path to the folder with the files to compress (folder name included)
	 * @param zipExecutable    The full name (path + name) of the 7 ZIP executable
	 * @param archiveName      The name of the compressed folder (with extension)
	 * @param command          One of the compress commands in {@link ArchiveCommand} with default ADD
	 *                         (create archive or add if already exists)
	 * @param printDetails     If true the stream of the command line gets written to the console
	 * @return {@literal -}1: Return value of the process could not be determined, 0: Execution
	 *         succeeded, {@literal >}1: Execution failed
	 * @throws IllegalArgumentException in case of null or blank parameter or invalid directory
	 * @throws UncheckedIOException when the process execution failed
	 */
	public int runProcess(String currentDirectory, String zipExecutable, String archiveName, ArchiveCommand command, boolean printDetails) throws IOException, InterruptedException {
		if (StringUtils.isBlank(currentDirectory) || StringUtils.isBlank(zipExecutable) || StringUtils.isBlank(archiveName)) {
			throw new IllegalArgumentException("ArchiveUtil.runProcess: Null or blank parameter committed");
		}
		File checkDir = new File(currentDirectory);
		if (!checkDir.exists() || checkDir.isFile()) {
			throw new IllegalArgumentException("ArchiveUtil.runProcess: Committed directory does not exist or is a file");
		}

		int ret = -1;

		String cmd = "cmd /c cd " + currentDirectory + " && " + "\"" + zipExecutable + "\" " + command.getShortcut() + " \"" + archiveName + "\" " + fileNames + " " + switches;
		Process process = null;
		BufferedReader reader = null;
		try {
			process = Runtime.getRuntime().exec(cmd);

			if (printDetails) {
				reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
				String line = "";
				while ((line = reader.readLine()) != null) {
					System.out.println(line);
				}
			}
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		} finally {
			if (process != null) {
				ret = process.waitFor();
				process.destroy();
			}
			if (reader != null) {
				reader.close();
			}
		}
		return ret;
	}

	/**
	 * @return {@link #switches}
	 */
	public String getSwitches() {
		return switches;
	}

	/**
	 * @param switches {@link #switches}
	 */
	public void setSwitches(String switches) {
		this.switches = switches;
	}

	/**
	 * @return {@link #fileNames}
	 */
	public String getFileNames() {
		return fileNames;
	}

	/**
	 * @param fileNames {@link #fileNames}
	 */
	public void setFileNames(String fileNames) {
		this.fileNames = fileNames;
	}

}
