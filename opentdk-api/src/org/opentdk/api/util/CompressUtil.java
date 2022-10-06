package org.opentdk.api.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;

import org.apache.commons.lang3.StringUtils;
import org.opentdk.api.logger.MLogger;

/**
 * This class gets used to compress and extract directories by calling the 7 ZIP executable.<br><br>
 * Example:
 * <pre>
 * CompressUtil.getInstance().setFileNames("*.json *.yaml *.properties *.xml *.csv");
 * CompressUtil.getInstance().compress("testdata\\RegressionTestData", "C:\\Program Files\\7 Zip\\7z.exe", "RegressionTestData");
 * </pre>
 * 
 * This would compress all files with the defined extension in the folder 'RegressionTestData' as .7z archive with name 'RegressionTestData'.<br><br>
 * 
 * To get all available 7 ZIP settings go to the installation folder via command prompt and type 7z.
 * 
 * @author FME (LK Test Solutions)
 */
public class CompressUtil {
	/**
	 * The one and only instance of the {@code CompressUtil} class.
	 */
	private static CompressUtil instance;
	/**
	 * Possibility to include/exclude file types from the compression/extraction. Default is *.* which allows all
	 * file names and all file extensions. E.g. *.json *.yaml would only allow JSON and YAML files with
	 * any file name.
	 */
	private String fileNames = "*.*";
	/**
	 * Possibility to define all other options of the compression like archive type or compression rate. Default is {@literal -t7z} to set the 7z archive type.
	 */
	private String switches = "-t7z";

	/**
	 * Possibility to differentiate between the 7 ZIP compression commands like add, delete, list or
	 * update.
	 */
	public enum CompressCommand {
		Add("a"), Delete("d"), List("l"), Update("u"), Extract("e");

		private String shortcut;

		CompressCommand(String abrev) {
			shortcut = abrev;
		}

		public String getShortcut() {
			return shortcut;
		}
	}

	/**
	 * Invisible constructor that gets called when using {@link #getInstance()} for the first time in an
	 * application.
	 */
	private CompressUtil() {

	}

	/**
	 * When calling this method the fist time, a new instance of the CompressUtil class will be created
	 * and returned to the caller. For every further call, the already created instance will be
	 * returned. This construct allows access to all methods and properties of an instance of the
	 * CompressUtil class from any other class during runtime of an application. The usage of the
	 * methods is like it is in a static way, but with an instantiated class.<br>
	 * <br>
	 * 
	 * e.g.:<br>
	 * <code>CompressUtil.getInstance().compress(currentDirectory, zipExecutable, archiveName);</code>
	 * 
	 * @return The instance of the CompressUtil class
	 */
	public static CompressUtil getInstance() {
		if (instance == null) {
			instance = new CompressUtil();
		}
		return instance;
	}

	/**
	 * Compress a directory with the 7 ZIP application via command line. Only usable on windows.
	 * 
	 * @param currentDirectory The path to the folder with the files to compress (folder name included)
	 * @param zipExecutable    The full name (path + name) of the 7 ZIP executable
	 * @param archiveName      The name of the compressed folder (without extension)
	 * @return {@literal -1}: Invalid method call, 0: Command execution succeeded, 1: Command execution
	 *         failed
	 */
	public int compress(String currentDirectory, String zipExecutable, String archiveName) {
		return compress(currentDirectory, zipExecutable, archiveName, CompressCommand.Add);
	}

	/**
	 * Compress a directory with the 7 ZIP application via command line. Only usable on windows.
	 * 
	 * @param currentDirectory The path to the folder with the files to compress (folder name included)
	 * @param zipExecutable    The full name (path + name) of the 7 ZIP executable
	 * @param archiveName      The name of the compressed folder (without extension)
	 * @param command          One of the compress commands in {@link CompressCommand} with default ADD
	 *                         (create archive or add if already exists)
	 * @return {@literal -1}: Invalid method call, 0: Command execution succeeded, 1: Command execution
	 *         failed
	 */
	public int compress(String currentDirectory, String zipExecutable, String archiveName, CompressCommand command) {
		return compress(currentDirectory, zipExecutable, archiveName, command, false);
	}

	/**
	 * Compress a directory with the 7 ZIP application via command line. Only usable on windows.
	 * 
	 * @param currentDirectory The path to the folder with the files to compress (folder name included)
	 * @param zipExecutable    The full name (path + name) of the 7 ZIP executable
	 * @param archiveName      The name of the compressed folder (without extension)
	 * @param command          One of the compress commands in {@link CompressCommand} with default ADD
	 *                         (create archive or add if already exists)
	 * @param printDetails     If true the stream of the command line gets written to the console
	 * @return {@literal -1}: Invalid method call, 0: Command execution succeeded, 1: Command execution
	 *         failed
	 */
	public int compress(String currentDirectory, String zipExecutable, String archiveName, CompressCommand command, boolean printDetails) {
		int ret = -1;

		if (StringUtils.isBlank(currentDirectory) || StringUtils.isBlank(zipExecutable) || StringUtils.isBlank(archiveName)) {
			throw new IllegalArgumentException("CompressUtil.compress: Null or blank parameter committed");
		}
		File checkDir = new File(currentDirectory);
		if (!checkDir.exists() || checkDir.isFile()) {
			throw new IllegalArgumentException("CompressUtil.compress: Committed directory does not exist or is a file");
		}

		String cmd = "cmd /c cd " + currentDirectory + " && " + "\"" + zipExecutable + "\" " + command.getShortcut() + " \"" + archiveName + "\" " + fileNames + " " + switches;	
		System.out.println(cmd);
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
			throw new RuntimeException(e);
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
