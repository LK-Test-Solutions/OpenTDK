package org.opentdk.api.datastorage;

/**
 * Enumeration that defines all source types, supported by the data storage
 * package.
 */
public enum EContainerFormat {
	TEXT(EHeader.UNKNOWN), CSV(EHeader.COLUMN), PROPERTIES(EHeader.ROW), RESULTSET(EHeader.COLUMN), XML(EHeader.TREE), JSON(EHeader.TREE), YAML(EHeader.TREE);

	/**
	 * String that is the result of the Apache TIKA source detection e.g. text/CSV.
	 */
	private EHeader orientation;

	/**
	 * Constructor of the enumerations that assigns the header orientation.
	 * 
	 * @param headerOrientation {@link #orientation}
	 */
	EContainerFormat(EHeader headerOrientation) {
		orientation = headerOrientation;
	}

	/**
	 * @return {@link #orientation}
	 */
	public EHeader getOrientation() {
		return orientation;
	}
}