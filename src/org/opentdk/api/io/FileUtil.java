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
package org.opentdk.api.io;

import org.opentdk.api.logger.MLogger;
import org.opentdk.api.util.CommonUtil;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

/**
 * Class with static methods to perform several I/O operations like reading files, creating files or
 * checking files.
 * 
 * @author LK Test Solutions
 *
 */
public class FileUtil {

	/**
	 * Checks the existence of a directory and returns true (directory exists) or false (directory
	 * doesn't exist).
	 * 
	 * @param inDir String with the directory name and its full or relative path.
	 * @return <code>true</code> the defined directory exists; <code>false</code> the defined directory
	 *         doesn't exist.
	 */
	public static boolean checkDir(String inDir) {
		return checkDir(new File(inDir), false);
	}

	/**
	 * Checks the existence of a directory and returns true (directory exists) or false (directory
	 * doesn't exist).
	 * 
	 * @param inDir Object of type {@link java.io.File} of the directory that needs to be checked.
	 * @return <code>true</code> the defined directory exists; <code>false</code> the defined directory
	 *         doesn't exist.
	 * @see java.io.File
	 */
	public static boolean checkDir(File inDir) {
		return checkDir(inDir, false);
	}

	/**
	 * Checks for the existence of a defined path on the file system and creates all folders that don't
	 * exist.
	 *
	 * @param inDir         String with the directory name and its full or relative path.
	 * @param createFolders <code>true</code> = create missing folder and all its parents;
	 *                      <code>false</code> = don't create missing folders.
	 * @return <code>true</code> the defined directory exists; <code>false</code> the defined directory
	 *         doesn't exist.
	 */
	public static boolean checkDir(String inDir, boolean createFolders) {
		return checkDir(new File(inDir), createFolders);
	}

	/**
	 * Checks for the existence of a defined path on the file system and creates all folders that don't
	 * exist.
	 *
	 * @param inDir         Object of type {@link java.io.File} of the directory that needs to be
	 *                      checked.
	 * @param createFolders true = create missing folder and all its parents; false = don't create
	 *                      missing folders.
	 * @return <code>true</code> the defined directory exists; <code>false</code> the defined directory
	 *         doesn't exist.
	 * @see java.io.File
	 */
	public static boolean checkDir(File inDir, boolean createFolders) {
		boolean rval = false;
		if (inDir.exists()) {
			rval = true;
		} else {
			if (createFolders) {
				rval = inDir.mkdirs();
			}
		}
		return rval;
	}

	/**
	 * Checks the existence of a file and returns true (file exists) or false (file doesn't exist).
	 * 
	 * @param fileName Name of the file including its full or relative path
	 * @return true = file exists; false = file doesn't exist
	 * @see java.io.File#exists()
	 */
	public static boolean checkFile(String fileName) {
		File f = new File(fileName);
		return f.exists();
	}

	/**
	 * Creates a new empty file only if a file with this name does not yet exist. The method will return
	 * <code>false</code> in case that parent directories with in the files path are missing, or the
	 * file already exists.
	 * 
	 * @param fileName Name including the full or relative path of the file that needs to be created.
	 * @return <code>true</code> if the named file does not exist and was successfully created;
	 *         <code>false</code> if the named file already exists.
	 * @throws IOException If an I/O error occurred.
	 * @see java.io.File#createNewFile()
	 */
	public static boolean createFile(String fileName) throws IOException {
		return createFile(new File(fileName), false);
	}

	/**
	 * Creates a new empty file only if a file with this name does not yet exist. With the
	 * <code>createParentDir</code> argument it can be chosen, if missing parent directories in the path
	 * will automatically be created or not.
	 * 
	 * @param fileName        Name including the full or relative path of the file that needs to be
	 *                        created
	 * @param createParentDir <code>true</code> create missing parent directories; <code>false</code>
	 *                        don't create missing parent directories.
	 * @return <code>true</code> if the named file does not exist and was successfully created;
	 *         <code>false</code> if the named file already exists.
	 * @throws IOException If an I/O error occurred.
	 * @see java.io.File#createNewFile()
	 */
	public static boolean createFile(String fileName, boolean createParentDir) throws IOException {
		return createFile(new File(fileName), createParentDir);
	}

	/**
	 * Creates a new empty file only if a file with this name does not yet exist. The method will return
	 * <code>false</code> in case that parent directories with in the files path are missing, or the
	 * file already exists.
	 * 
	 * @param file Object of type {@link java.io.File} with the file that needs to be created.
	 * @return <code>true</code> if the named file does not exist and was successfully created;
	 *         <code>false</code> if the named file already exists.
	 * @throws IOException If an I/O error occurred.
	 * @see java.io.File#createNewFile()
	 */
	public static boolean createFile(File file) throws IOException {
		return createFile(file, false);
	}

	/**
	 * Creates a new empty file only if a file with this name does not already exist. With the
	 * <code>createParentDir</code> argument missing parent directories in the path will automatically
	 * be created or not.
	 * 
	 * @param file            Object of type {@link java.io.File} with the file that needs to be created
	 * @param createParentDir <code>true</code> create missing parent directories; <code>false</code>
	 *                        don't create missing parent directories.
	 * @return <code>true</code> if the named file does not exist and was successfully created;
	 *         <code>false</code> if the named file already exists.
	 * @throws IOException If an I/O error occurred.
	 * @see java.io.File#createNewFile()
	 */
	public static boolean createFile(File file, boolean createParentDir) throws IOException {
		if (checkDir(file.getParent(), createParentDir)) {
			return file.createNewFile();
		}
		return false;
	}

	/**
	 * Creates a copy of the input source file to the chosen target file by using {@link FileUtils}. 
	 * 
	 * @param sourceFile Full source file name (path + name) that should be copied.
	 * @param targetFile Full target file name (path + name) for the copy operation.
	 * @throws IOException If an I/O error occurred.
	 */
	public static void copyFile(String sourceFile, String targetFile) throws IOException {
		FileUtils.copyFile(new File(sourceFile), new File(targetFile));
//		OutputStream os = new FileOutputStream(targetFile);
//		return Files.copy(Paths.get(sourceFile), os);
	}

	/**
	 * Deletes the input file if it does exist.
	 * 
	 * @param fileName Full name (path + name) of the file to remove.
	 * @return {@code true} if the file was deleted by this method; {@code
	 *          false} if the file could not be deleted because it did not exist.
	 * @throws IOException If an I/O error occurred.
	 */
	public static boolean deleteFile(String fileName) throws IOException {
		File f = new File(fileName);
		return Files.deleteIfExists(f.toPath());
	}

	/**
	 * Rename a file when the full names are available. If the file to rename already exists, it gets
	 * replaced.
	 * 
	 * @param oldFile The full name of the current file.
	 * @param newFile The full name of the file that should be the replacement of the old file.
	 * @throws FileNotFoundException If one of the files does not exist.
	 * @throws SecurityException     If the file/directory creation failed in the current context.
	 * @throws IOException           If any I/O error occurs on the file system.
	 */
	public static void renameFile(String oldFile, String newFile) throws SecurityException, IOException {
		renameFile(new File(oldFile), new File(newFile));
	}

	/**
	 * Rename a file when the file objects are available. If the file to rename already exists, it gets
	 * replaced. File.renameTo() is not recommended to use while it returns false with no exception when
	 * something went wrong. The cause is hard to analyze.
	 * 
	 * @param oldFile The full name of the current file.
	 * @param newFile The full name of the file that should be the replacement of the old file.
	 * @throws FileNotFoundException If one of the files does not exist.
	 * @throws SecurityException     If the file/directory creation failed in the current context.
	 * @throws IOException           If any I/O error occurs on the file system.
	 */
	public static void renameFile(File oldFile, File newFile) throws SecurityException, IOException {
		if (checkDir(oldFile)) {
			Path oldPath = Paths.get(oldFile.getPath());
			Path newPath = Paths.get(newFile.getPath());
			if (oldFile.getParent().contentEquals(newFile.getParent())) {
				Files.move(oldPath, oldPath.resolveSibling(newFile.getName()), StandardCopyOption.REPLACE_EXISTING);
			} else {
				Files.move(oldPath, newPath, StandardCopyOption.REPLACE_EXISTING);
			}
		}
	}

	/**
	 * Delete a file or folder.
	 * 
	 * @param path The path of the file (path + name) or folder that should be deleted as string.
	 * @throws IOException If an I/O error occurred.
	 */
	public static void deleteFileOrFolder(final String path) throws IOException {
		deleteFileOrFolder(new File(path).toPath());
	}

	/**
	 * Delete a file or folder.
	 * 
	 * @param path The path of the file (path + name) or folder that should be deleted.
	 * @throws IOException If an I/O error occurred.
	 */
	public static void deleteFileOrFolder(final Path path) throws IOException {
		Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
			@Override
			public FileVisitResult visitFile(final Path file, final BasicFileAttributes attrs) throws IOException {
				Files.delete(file);
				return FileVisitResult.CONTINUE;
			}

			@Override
			public FileVisitResult visitFileFailed(final Path file, final IOException e) {
				return handleException(e);
			}

			private FileVisitResult handleException(final IOException e) {
				return FileVisitResult.TERMINATE;
			}

			@Override
			public FileVisitResult postVisitDirectory(final Path dir, final IOException e) throws IOException {
				if (e != null) {
					return handleException(e);
				}
				Files.delete(dir);
				return FileVisitResult.CONTINUE;
			}
		});
	}

	/**
	 * Checks if the committed file instance is a text file. In general every file is of type binary and
	 * is composed of bytes. The way it gets interpreted by a program makes it possible to group the
	 * file type. The most common group are text files that represent reasonable text and can be opened
	 * by any text editor program.<br>
	 * The first step in this check routine is to check if the file is readable. Afterwards the guess
	 * methods of {@link java.net.URLConnection} get used to determine the file type. If this is not
	 * successful the file extension will be compared with a set of most common text file endings. If
	 * none of the listed extension hits the first lines of the file get checked character by character.
	 * If the characters are all below 0x7F (which is the common range of valid ASCCI characters for
	 * text files), the file gets treated as text. Afterwards a binary check gets passed to include a
	 * negative check as well and avoid mistakes. If all these operations fail, an IOException gets
	 * thrown.
	 * 
	 * @param fullName full path and name of the file to check.
	 * @return true: file is of type text, false: otherwise.
	 * @throws IOException If the filed type could not be determined.
	 */
	public static boolean isTextFile(String fullName) throws IOException {
		return isTextFile(new File(fullName));
	}

	/**
	 * Checks if the committed file instance is a text file. In general every file is of type binary and
	 * is composed of bytes. The way it gets interpreted by a program makes it possible to group the
	 * file type. The most common group are text files that represent reasonable text and can be opened
	 * by any text editor program.<br>
	 * The first step in this check routine is to check if the file is readable. Afterwards the guess
	 * methods of {@link java.net.URLConnection} get used to determine the file type. If this is not
	 * successful the file extension will be compared with a set of most common text file endings. If
	 * none of the listed extension hits the first lines of the file get checked character by character.
	 * If the characters are all below 0x7F (which is the common range of valid ASCCI characters for
	 * text files), the file gets treated as text. Afterwards a binary check gets passed to include a
	 * negative check as well and avoid mistakes. If all these operations fail, an IOException gets
	 * thrown.
	 * 
	 * @param file the file instance with the full path and name of the file to check.
	 * @return true: file is of type text, false: otherwise.
	 * @throws IOException If the filed type could not be determined.
	 */
	public static boolean isTextFile(File file) throws IOException {
		boolean isText = false;
		String name = file.getName();
		String path = file.getAbsolutePath();

		if (Files.isReadable(file.toPath())) {

			InputStream is = new BufferedInputStream(new FileInputStream(path));
			String type = URLConnection.guessContentTypeFromStream(is);
			is.close();

			if (type == null) {
				type = URLConnection.guessContentTypeFromName(name);
			}

			if (type == null) {
				if (checkAsciiFileExtension(name)) {
					isText = true;
				} else if (isAllASCII(getRowsAsString(path, 5))) {
					isText = true;
				}
			}

			if (type != null) {
				if (type.endsWith("/pdf") || type.startsWith("image/") || type.startsWith("application/")) {
					isText = false;
				} else if (checkBinaryFileExtension(name)) {
					isText = false;
				} else if (!isAllASCII(getRowsAsString(path, 5))) {
					isText = false;
				} else {
					isText = true;
				}
			} else {
				throw new IOException("FileUtil.isTextFile: File type could not be detected.");
			}
		}
		return isText;
	}

	private static boolean checkAsciiFileExtension(String fileName) {
		boolean isAsciiExtension = false;
		String[] extensions = { ".html", ".xml", ".css", ".svg", ".json", ".c", ".cpp", ".h", ".cs", ".js", ".py", ".java", ".rb", ".pl", ".php", ".sh", ".txt", ".tex", ".markdown", ".asciidoc", ".rtf", ".ps", ".ini", ".cfg", ".rc", ".reg", ".csv", ".tsv", ".properties" };
		for (String suffix : extensions) {
			if (fileName.endsWith(suffix)) {
				isAsciiExtension = true;
			}
		}
		return isAsciiExtension;
	}

	private static boolean checkBinaryFileExtension(String fileName) {
		boolean isBinaryExtension = false;
		String[] extensions = { ".jpg", ".png", ".gif", ".bmp", ".tiff", ".psd", ".mp4", ".mkv", ".avi", ".mov", ".mpg", ".vob", ".mp3", ".aac", ".wav", ".flac", ".ogg", ".mka", ".wma", ".pdf", ".doc", ".xls", ".ppt", ".docx", ".odt", ".zip", ".rar", ".7z", ".tar", ".iso", ".mdb", ".db", ".accde", ".frm", ".sqlite", ".exe", ".dll", ".so", ".class" };
		for (String suffix : extensions) {
			if (fileName.endsWith(suffix)) {
				isBinaryExtension = true;
			}
		}
		return isBinaryExtension;
	}

	private static boolean isAllASCII(String input) {
		boolean isASCII = true;
		for (int i = 0; i < input.length(); i++) {
			int c = input.charAt(i);
			if (c > 0x7F) {
				isASCII = false;
				break;
			}
		}
		return isASCII;
	}

	/**
	 * Checks if a file is hidden. On UNIX operating systems the file is hidden if it starts with dot or
	 * slash + dot. On Windows the <code>File.isHidden</code> method gets used.
	 * 
	 * @param path path object to check.
	 * @return true: file is hidden, false otherwise.
	 */
	public static final boolean isHidden(Path path) {
		boolean ret = false;
		if (CommonUtil.isUnix()) {
			if (path.toFile().toString().startsWith("/.") || path.getFileName().toString().startsWith(".")) {
				ret = true;
			}
		} else if (CommonUtil.isWindows()) {
			if (path.toFile().isHidden()) {
				ret = true;
			}
		}
		return ret;
	}

	/**
	 * Read the lines of a ASCII file to a String by using the {@link java.io.BufferedReader}. This is a
	 * very fast type of reading. Especially for large files. Line breaks (\n) are included, too.
	 * 
	 * @param file The {@link java.io.File} instance of the file to work with.
	 * @return A list of type String with all lines of the file as elements.
	 */
	public static String getRowsAsString(File file) {
		return getRowsAsString(file.getPath());
	}

	/**
	 * Read the lines of a ASCII file to a String by using the {@link java.io.BufferedReader}. This is a
	 * very fast type of reading. Especially for large files. Line breaks (\n) are included, too.
	 * 
	 * @param path Full name (path + name) of the file to work with.
	 * @return A list of type String with all lines of the file as elements.
	 */
	public static String getRowsAsString(String path) {
		return getRowsAsString(path, -1);
	}
	
	/**
	 * Read the lines of a ASCII file to a String by using the {@link java.io.BufferedReader}. This is a
	 * very fast type of reading. Especially for large files. Line breaks (\n) are included, too.
	 * 
	 * @param path      Full name (path + name) of the file to work with.
	 * @param lineCount Possibility to determine the number of iterations/lines. Use {@literal -} 1 to switch off.
	 * @return A list of type string with all lines of the file as elements.
	 */
	public static String getRowsAsString(String path, int lineCount) {
		return getRowsAsString(path, lineCount, StandardCharsets.UTF_8);
	}

	/**
	 * Read the lines of a ASCII file to a String by using the {@link java.io.BufferedReader}. This is a
	 * very fast type of reading. Especially for large files. Line breaks (\n) are included, too.
	 * 
	 * @param path      Full name (path + name) of the file to work with.
	 * @param lineCount Possibility to determine the number of iterations/lines. Use {@literal -} 1 to switch off.
	 * @param encoding Character encoding, see {@link java.nio.charset.StandardCharsets}
	 * @return A list of type string with all lines of the file as elements.
	 */
	public static String getRowsAsString(String path, int lineCount, Charset encoding) {
		BufferedReader br = null;
		StringBuilder sb = new StringBuilder();
		try {
			br = new BufferedReader(new FileReader(path, encoding));

			String line = null;
			int i = 0;
			while ((line = br.readLine()) != null) {
				sb.append(line).append('\n');
				if (i == lineCount) {
					break;
				}
				i++;
			}		
		} catch (IOException e) {
			MLogger.getInstance().log(Level.SEVERE, e);
			throw new RuntimeException(e);
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return new String(sb);
	}

	/**
	 * Read the lines of a ASCII file to a {@link java.util.List} by using the
	 * {@link java.io.BufferedReader}. This is a very fast type of reading. Especially for large files.
	 * 
	 * @param file The {@link java.io.File} instance of the file to work with.
	 * @return A list of type string with all lines of the file as elements.
	 */
	public static List<String> getRowsAsList(File file) {
		return FileUtil.getRowsAsList(file.getPath());
	}

	/**
	 * Read the lines of a ASCII file to a {@link java.util.List} by using the
	 * {@link java.io.BufferedReader}. This is a very fast type of reading. Especially for large files.
	 * Default character encoding is UTF8.
	 * 
	 * @param path Full name (path + name) of the file to work with.
	 * @return A list of type string with all lines of the file as elements.
	 */
	public static List<String> getRowsAsList(String path) {		
		return getRowsAsList(path, StandardCharsets.UTF_8);
	}
	
	/**
	 * Read the lines of a ASCII file to a {@link java.util.List} by using the
	 * {@link java.io.BufferedReader}. This is a very fast type of reading. Especially for large files.
	 * 
	 * @param path Full name (path + name) of the file to work with.
	 * @param encoding Character encoding, see {@link java.nio.charset.StandardCharsets}
	 * @return A list of type string with all lines of the file as elements.
	 */
	public static List<String> getRowsAsList(String path, Charset encoding) {
		BufferedReader br = null;
		List<String> rows = new ArrayList<>();
		try {
			br = new BufferedReader(new FileReader(path, encoding));

			String line = null;
			while ((line = br.readLine()) != null) {
				rows.add(line);
			}		
		} catch (IOException e) {
			MLogger.getInstance().log(Level.SEVERE, e);
			throw new RuntimeException(e);
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return rows;
	}

	/**
	 * Reads content of a file, closes the file and returns the content as String. This method will
	 * return the content with UTF-8 {@link java.nio.charset.Charset} by default. Please use the 
	 * method {@link #getContent(String, Charset)} to return the content with other charsets.
	 * 
	 * @param path Full name (path + name) of the file to read the content from.
	 * @return The file content as string.
	 */
	public static String getContent(String path) {
		return getContent(path, StandardCharsets.UTF_8);
	}
	
	/**
	 * Reads content of a file, closes the file and returns the content as String. This method will
	 * return the content with the {@link java.nio.charset.Charset}, defined by the Charset attribute.
	 * 
	 * @param path Full name (path + name) of the file to read the content from.
	 * @param cs The {@link java.nio.charset.Charset}, used to transfer the file content into the returned string.
	 * @return The file content as string.
	 */
	public static String getContent(String path, Charset cs) {
		InputStream stream = null;
		try {
			stream = new FileInputStream(path);
		} catch (FileNotFoundException e) {
			MLogger.getInstance().log(Level.SEVERE, e);
			throw new RuntimeException(e);
		}
		return getContent(stream, cs);
	}

	/**
	 * Reads content of an InputStream, closes the InputStream and returns the content as String. This 
	 * method will return the content with UTF-8 {@link java.nio.charset.Charset} by default. Please use 
	 * the method {@link #getContent(InputStream, Charset)} to return the content with other charsets.<br>
	 * <br>
	 * Usage:
	 * 
	 * <pre>
	 * InputStream stream = null;
	 * try {
	 *   stream = new FileInputStream(path);
	 * } catch (FileNotFoundException e) {
	 *   e.printStackTrace();
	 * }
	 * FileUtil.getContent(stream);
	 * </pre>
	 * 
	 * @param is An object of type {@link java.io.InputStream} with the content
	 * @return The content of the {@link java.io.InputStream} object as string.
	 */
	public static String getContent(InputStream is) {
		return getContent(is, StandardCharsets.UTF_8);
	}
	
	/**
	 * Reads content of an InputStream, closes the InputStream and returns the content as String. This 
	 * method will return the content with the {@link java.nio.charset.Charset}, defined by the Charset 
	 * attribute.<br>
	 * <br>
	 * Usage:
	 * 
	 * <pre>
	 * InputStream stream = null;
	 * try {
	 *   stream = new FileInputStream(path);
	 * } catch (FileNotFoundException e) {
	 *   e.printStackTrace();
	 * }
	 * FileUtil.getContent(stream, StandardCharsets.UFT_16);
	 * </pre>
	 * 
	 * @param is An object of type {@link java.io.InputStream} with the content
	 * @param cs {@link java.nio.charset.StandardCharsets}
	 * @return The content of the {@link java.io.InputStream} object as string.
	 */
	public static String getContent(InputStream is, Charset cs) {
		StringBuilder sb = new StringBuilder();
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(is, cs));
			String line;
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
		} catch (IOException e) {
			MLogger.getInstance().log(Level.SEVERE, e);
			throw new RuntimeException(e);
		} finally {
			try {
				if (is != null) {
					is.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return new String(sb);
	}

	/**
	 * Transfers the content of a string list into a defined file. Existing files with the same name
	 * will be overwritten.
	 * 
	 * @param list     Object of type List(String) which content will be written into the output file.
	 * @param fileName Full path and name of the file, where content of the list will be written.
	 */
	public static void writeOutputFile(List<String> list, String fileName) {
		FileWriter writer = null;
		try {
			FileUtil.createFile(fileName, true);
			writer = new FileWriter(fileName);
			for (String row : list) {
				writer.append(row);
				writer.append(System.getProperty("line.separator"));
			}
		} catch (IOException e) {
			MLogger.getInstance().log(Level.SEVERE, e);
			throw new RuntimeException(e);
		} finally {
			try {
				if (writer != null) {
					writer.flush();
					writer.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}
	
	/**
	 * Transfers the content of a string into a defined file. Existing files with the same name
	 * will be overwritten.
	 * 
	 * @param content     Object of type String which content will be written into the output file.
	 * @param fileName Full path and name of the file, where content of the list will be written.
	 */
	public static void writeOutputFile(String content, String fileName) {
		FileWriter writer = null;
		try {
			FileUtil.createFile(fileName, true);
			writer = new FileWriter(fileName);
			writer.append(content);
		} catch (IOException e) {
			MLogger.getInstance().log(Level.SEVERE, e);
			throw new RuntimeException(e);
		} finally {
			try {
				if (writer != null) {
					writer.flush();
					writer.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}

}
