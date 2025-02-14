package org.opentdk.api.logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Utility class to handle log file archiving based on specific criteria such as file size,
 * modification date, and maximum number of archives allowed.
 * The class provides mechanisms to automatically archive log files and remove old archives
 * to ensure storage efficiency while maintaining record retention policies.
 * <p>
 * This class is final and cannot be extended.
 * It is designed with static methods and cannot be instantiated.
 */
public final class LogArchiver {

	/**
	 * Archives the given log file if it meets specific criteria such as exceeding a specified size
	 * and being older than a specified number of days. The archived file is moved to a parent directory,
	 * and excess archives are automatically removed based on the specified archive size limit.
	 *
	 * @param logFile the path to the log file that needs to be checked for archiving
	 * @param logSize the maximum allowed size (in bytes) of the log file before it needs to be archived
	 * @param keepAge the maximum age (in days) a log file can remain unarchived if the size threshold is exceeded
	 * @param archiveSize the maximum number of archived files allowed in the archive directory
	 * @throws IOException if the log file does not exist or an I/O error occurs during the archiving process
	 */
	public static void archiveIfNecessary(Path logFile, long logSize, int keepAge, int archiveSize) throws IOException {
		if (!Files.exists(logFile)) {
			throw new IOException("No log file present to archive");
		}

		// Get file properties
		BasicFileAttributes attributes = Files.readAttributes(logFile, BasicFileAttributes.class);
		long fileSize = attributes.size();
		Instant lastModified = attributes.lastModifiedTime().toInstant();
		Instant now = Instant.now();

		// Check if archiving is required
		if (fileSize > logSize && lastModified.isBefore(now.minus(keepAge, ChronoUnit.DAYS))) {
			archiveLog(logFile, logFile.getParent().getParent(), archiveSize);
		}
	}

	/**
	 * Archives a specified log file by moving it to an archive directory with a timestamped name,
	 * creating a new log file in its place, and cleaning up old archives to maintain a maximum
	 * allowed number of archived files.
	 *
	 * @param logFile the path to the log file to be archived
	 * @param archiveDir the directory where the archived log files will be stored
	 * @param archiveSize the maximum number of archived files allowed in the archive directory
	 * @throws IOException if there is an error during the process of archiving, moving, or creating files
	 */
	private static void archiveLog(Path logFile, Path archiveDir, int archiveSize) throws IOException {
		if (!Files.exists(archiveDir)) {
			Files.createDirectories(archiveDir);
		}

		// Generate archive name
		String timestamp = LocalDateTime.now().toString().replace(":", "-");
		Path archiveFile = archiveDir.resolve(logFile.getFileName() + "_" + timestamp);

		// Move log file
		Files.move(logFile, archiveFile, StandardCopyOption.REPLACE_EXISTING);

		// Create new log file
		Files.createFile(logFile);

		// Remove old files when the number exceeds the archive size
		cleanupOldArchives(archiveDir, archiveSize);
	}

	/**
	 * Removes old archive files from the specified archive directory to ensure that the
	 * maximum number of allowed archives is not exceeded. The oldest files are prioritized
	 * for deletion when the limit is surpassed.
	 *
	 * @param archiveDir the directory containing the archived files
	 * @param maxArchives the maximum allowed number of archive files to retain
	 * @throws IOException if an I/O error occurs while accessing or deleting files
	 */
	private static void cleanupOldArchives(Path archiveDir, int maxArchives) throws IOException {
		try (Stream<Path> files = Files.list(archiveDir)) {
			List<Path> archiveFiles = files.filter(Files::isRegularFile)
				.sorted(Comparator.comparingLong(path -> path.toFile().lastModified()))
				.collect(Collectors.toList());

			while (archiveFiles.size() > maxArchives) {
				Path oldestFile = archiveFiles.remove(0);
				Files.delete(oldestFile);
			}
		}
	}

}
