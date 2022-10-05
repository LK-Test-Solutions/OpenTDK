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
	
	private static String fileNames = "*.*";
	private static String switches = " -t7z -m0=BCJ2 -m1=LZMA:d25:fb255 -m2=LZMA:d19 -m3=LZMA:d19 -mb0:1 -mb0s1:2 -mb0s2:3 -mx";
	
	public enum CompressCommand {
		Add("a"),
		Delete("d"),
		List("l"),
		Update("u");
		
		private String shortcut;
		
		CompressCommand(String abrev) {
			shortcut = abrev;
		}

		public String getShortcut() {
			return shortcut;
		}		
	}

	/**
	 * Compress a directory with the 7 ZIP application via command line. Only usable on windows.
	 * 
	 * @param currentDirectory The path to the folder with the files to compress (folder name included)
	 * @param zipExecutable    The full name (path + name) of the 7 ZIP executable
	 * @param archiveName   The name of the compressed folder (without extension)
	 * @return {@literal -1}: Invalid method call, 0: Command execution succeeded, 1: Command execution
	 *         failed
	 */
	public static int compress(String currentDirectory, String zipExecutable, String archiveName) {
		return CompressUtil.compress(currentDirectory, zipExecutable, archiveName, CompressCommand.Add);
	}
	
	public static int compress(String currentDirectory, String zipExecutable, String archiveName, CompressCommand command) {
		return CompressUtil.compress(currentDirectory, zipExecutable, archiveName, command, false);
	}

	/**
	 * Compress a directory with the 7 ZIP application via command line. Only usable on windows.
	 * 
	 * @param currentDirectory The path to the folder with the files to compress (folder name included)
	 * @param zipExecutable    The full name (path + name) of the 7 ZIP executable
	 * @param archiveName   The name of the compressed folder (without extension)
	 * @param printDetails If true the stream of the command line gets written to the console
	 * @return {@literal -1}: Invalid method call, 0: Command execution succeeded, 1: Command execution
	 *         failed
	 */
	public static int compress(String currentDirectory, String zipExecutable, String archiveName, CompressCommand command, boolean printDetails) {
		int ret = -1;

		if (StringUtils.isBlank(currentDirectory) || StringUtils.isBlank(zipExecutable) || StringUtils.isBlank(archiveName)) {
			throw new IllegalArgumentException("CompressUtil.compress: Null or blank parameter committed");
		}
		File checkDir = new File(currentDirectory);
		if (!checkDir.exists() || checkDir.isFile()) {
			throw new IllegalArgumentException("CompressUtil.compress: Committed directory does not exist or is a file");
		}

		// a = Add to archive, -t7z = type of archive
		String cmd = "cmd /c cd " + currentDirectory + " && " + "\"" + zipExecutable + "\" " + command.getShortcut() + " \"..\\" + archiveName + ".7z\" " + fileNames + switches;
		Process process = null;
		BufferedReader reader = null;
		try {
			process = Runtime.getRuntime().exec(cmd);
			ret = process.onExit().get().exitValue();

			if (printDetails) {
				reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
				String line = "";
				while ((line = reader.readLine()) != null) {
					System.out.println(line);
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

	public static String getSwitches() {
		return switches;
	}

	public static void setSwitches(String switches) {
		CompressUtil.switches = switches;
	}

	public static String getFileNames() {
		return fileNames;
	}

	public static void setFileNames(String fileNames) {
		CompressUtil.fileNames = fileNames;
	}

}
