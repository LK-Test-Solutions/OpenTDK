package org.opentdk.api.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;

import org.apache.commons.lang3.StringUtils;
import org.opentdk.api.logger.MLogger;

public class CompressUtil {
	/**
	 * TODO variable command arguments and compress extension to not have a 7 ZIP dependent code
	 */
	private static String commandArguments = "";

	/**
	 * Compress a directory with the 7 ZIP application via command line.
	 * 
	 * @param currentDirectory The path to the folder with the files to compress (folder name included)
	 * @param zipExecutable    The full name (path + name) of the 7 ZIP executable
	 * @param outputFileName   The name of the compressed folder (without extension)
	 * @return {@literal -1}: Invalid method call, 0: Command execution succeeded, 1: Command execution
	 *         failed
	 */
	public static int compressSevenZip(String currentDirectory, String zipExecutable, String outputFileName) {
		return CompressUtil.compressSevenZip(currentDirectory, zipExecutable, outputFileName, false);
	}

	/**
	 * Compress a directory with the 7 ZIP application via command line.
	 * 
	 * @param currentDirectory The path to the folder with the files to compress (folder name included)
	 * @param zipExecutable    The full name (path + name) of the 7 ZIP executable
	 * @param outputFileName   The name of the compressed folder (without extension)
	 * @param printDetails If true the stream of the command line gets written to the console
	 * @return {@literal -1}: Invalid method call, 0: Command execution succeeded, 1: Command execution
	 *         failed
	 */
	public static int compressSevenZip(String currentDirectory, String zipExecutable, String outputFileName, boolean printDetails) {
		int ret = -1;

		if (StringUtils.isBlank(currentDirectory) || StringUtils.isBlank(zipExecutable) || StringUtils.isBlank(outputFileName)) {
			throw new IllegalArgumentException("CompressUtil.compress: Null or blank parameter committed");
		}
		File checkDir = new File(currentDirectory);
		if (!checkDir.exists() || checkDir.isFile()) {
			throw new IllegalArgumentException("CompressUtil.compress: Committed directory does not exist or is a file");
		}

		String command = "cmd /c cd " + currentDirectory + " && " + "\"" + zipExecutable + "\"" + " a -t7z " + "\"..\\" + outputFileName + ".7z\" *.* -m0=BCJ2 -m1=LZMA:d25:fb255 -m2=LZMA:d19 -m3=LZMA:d19 -mb0:1 -mb0s1:2 -mb0s2:3 -mx";
		Process process = null;
		BufferedReader reader = null;
		try {
			process = Runtime.getRuntime().exec(command);
			ret = process.onExit().get().exitValue();

			if (printDetails) {
				if (ret == 0) {
					reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
					String line = "";
					while ((line = reader.readLine()) != null) {
						System.out.println(line);
					}
				} else if (ret == 1) {
					MLogger.getInstance().log(Level.SEVERE, "compressSevenZip failed for ==> " + currentDirectory);
				}
			}
		} catch (IOException | InterruptedException | ExecutionException e) {
			MLogger.getInstance().log(Level.SEVERE, e);
		} finally {
			if (process != null) {
				process.destroy();
			}
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					MLogger.getInstance().log(Level.SEVERE, e);
				}
			}
		}
		return ret;
	}

	/**
	 * @return {@link #commandArguments}
	 */
	public static String getCommandArguments() {
		return commandArguments;
	}

	/**
	 * @param commandArguments {@link #commandArguments}
	 */
	public static void setCommandArguments(String commandArguments) {
		CompressUtil.commandArguments = commandArguments;
	}
}
