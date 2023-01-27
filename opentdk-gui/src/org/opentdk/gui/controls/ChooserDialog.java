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
package org.opentdk.gui.controls;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

import org.opentdk.api.logger.MLogger;

import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

/**
 * Class to open the explorer/finder or similar in different modes (file
 * chooser, directory chooser, save dialog).
 * 
 * Call like this:
 * 
 * <pre>
 * List{@literal <File>} files = ChooserDialog.openFileChooser(TITLE, ExtensionType.FILE, DialogType.CHOOSE, LATESTPATH); 
 * </pre>
 * 
 * The latest path can stored in a properties file or set to null to open the
 * default path (user home).<br>
 * <br>
 * 
 * @author LK Test Solutions
 *
 */
public final class ChooserDialog {
	private ChooserDialog() {

	}

	/**
	 * Available extension filters in the chooser dialog.
	 */
	public enum ExtensionType {
		FILE(Arrays.asList(new FileChooser.ExtensionFilter("All Files", "*.*"), new FileChooser.ExtensionFilter("java", "*.java"), new FileChooser.ExtensionFilter("txt", "*.txt"), new FileChooser.ExtensionFilter("csv", "*.csv"), new FileChooser.ExtensionFilter("xml", "*.xml"))), APP(Arrays.asList(new FileChooser.ExtensionFilter("exe", "*.exe"), new FileChooser.ExtensionFilter("dmg", "*.dmg"), new FileChooser.ExtensionFilter("app", "*.app"), new FileChooser.ExtensionFilter("rpm", "*.rpm")));

		/**
		 * File ending filter list that get added to the FileChooser instance. Not used
		 * for DiectoryChooser, of course.
		 */
		private List<FileChooser.ExtensionFilter> filters;

		/**
		 * @param extFilters {@link #filters}
		 */
		ExtensionType(List<FileChooser.ExtensionFilter> extFilters) {
			this.filters = extFilters;
		}

		/**
		 * @return {@link #filters}
		 */
		public List<FileChooser.ExtensionFilter> getFilters() {
			return filters;
		}
	}

	public enum DialogType {
		CHOOSE, SAVE;
	}

	/**
	 * Show the OS depending explorer to open or save files. The latest path is
	 * default user.home.
	 * 
	 * @param title the title of the chooser dialog
	 * @param ext   the extension of the files that should be displayed by default
	 * @param type  One of the DialogType e.g. CHOOSE or SAVE
	 * @return the chosen file as <code>File</code> list instance
	 */
	public static List<File> openFileChooser(String title, ExtensionType ext, DialogType type) {
		return openFileChooser(title, ext, type, null);
	}

	/**
	 * Show the OS depending explorer to open or save files.
	 * 
	 * @param title      the title of the chooser dialog
	 * @param ext        the extension of the files that should be displayed by
	 *                   default
	 * @param type choose or save dialog           
	 * @param latestPath if the latest path was stored, it can be used to open the
	 *                   chooser at this location again
	 * @return the chosen file as <code>File</code> list instance
	 */
	public static List<File> openFileChooser(String title, ExtensionType ext, DialogType type, String latestPath) {
		String chooserTitle = null;
		if (title == null || title.isBlank()) {
			MLogger.getInstance().log(Level.WARNING, "Title is null or blank", ChooserDialog.class.getSimpleName(), "openFileChooserDialog");
			chooserTitle = "";
		} else {
			chooserTitle = title;
		}

		String pathToLoad = null;
		if (latestPath == null || latestPath.isBlank()) {
			pathToLoad = System.getProperty("user.home");
		} else {
			pathToLoad = latestPath;
		}

		final FileChooser fileChooser = new FileChooser();

		File file = new File(pathToLoad);
		fileChooser.setTitle(chooserTitle);
		fileChooser.setInitialFileName(file.getName());
		fileChooser.getExtensionFilters().addAll(ext.getFilters());

		switch (type) {
		case CHOOSE:
			return fileChooser.showOpenMultipleDialog(null);
		case SAVE:
			return Arrays.asList(fileChooser.showSaveDialog(null));
		default:
			throw new RuntimeException("No DialogType detected in openFileDialog");
		}
	}

	/**
	 * Show the OS depending explorer to open directories. The latest path is
	 * default user.home.
	 * 
	 * @param title title of the window
	 * @return chosen directory as <code>File</code> object
	 */
	public static File openDirectoryChooser(String title) {
		return openDirectoryChooser(title, null);
	}

	/**
	 * Show the OS depending explorer to open directories.
	 * 
	 * @param title  title of the window
	 * @param latest if the latest path was stored, it can be used to open the
	 *               chooser at this location again
	 * @return chosen directory as <code>File</code> object
	 */
	public static File openDirectoryChooser(String title, String latest) {
		String chooserTitle = null;
		if (title == null || title.isBlank()) {
			MLogger.getInstance().log(Level.WARNING, "Title is null or blank", ChooserDialog.class.getSimpleName(), "openDirectoryChooser");
			chooserTitle = "";
		} else {
			chooserTitle = title;
		}

		String pathToLoad = null;
		if (latest == null || latest.isBlank() || !new File(latest).exists()) {
			pathToLoad = System.getProperty("user.home");
		} else {
			pathToLoad = latest;
		}

		final DirectoryChooser dirChooser = new DirectoryChooser();
		dirChooser.setTitle(chooserTitle);
		dirChooser.setInitialDirectory(new File(pathToLoad));

		return dirChooser.showDialog(null);
	}
}
