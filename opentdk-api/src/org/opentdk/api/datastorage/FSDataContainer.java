package org.opentdk.api.datastorage;

import java.io.File;
import java.util.logging.Level;

import org.opentdk.api.datastorage.BaseContainer.EContainerFormat;
import org.opentdk.api.logger.MLogger;

/**
 * Custom container to store file system structures.
 * 
 * @author FME (LK Test Solutions)
 *
 */
class FSDataContainer implements CustomContainer {
	
	/**
	 * An instance of the DataContainer that should be filled with the data from the
	 * connected source file. -> Task of the specific data containers.
	 */
	private final DataContainer dc;
	
	/**
	 * Construct a new specific <code>DataContainer</code> for FileSystem structures.
	 *
	 * @param dCont the <code>DataContainer</code> instance to use it in the read
	 *              and write methods of this specific data container
	 */
	public FSDataContainer(DataContainer dCont) {
		dc = dCont;
		dc.containerFormat = EContainerFormat.CSV;
	}

	@Override
	public void readData(Filter filter) {
		// TODO Auto-generated method stub

	}

	@Override
	public void writeData(String destFileName) {
		File fDestination = new File(destFileName);
		if (fDestination.exists()) {
			// TODO
		} else {
			MLogger.getInstance().log(Level.SEVERE,
					"The location where the file structure should be placed does not exist anymore.");
		}
	}

}
