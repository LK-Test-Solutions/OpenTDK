package org.opentdk.api.datastorage;

/**
 * Enumeration that defines all header formats supported by the data storage
 * package. In case of tabular formats, the header names can be defined in a row (e.g. CSV files) or
 * in a column (e.g. Properties files) and for hierarchical formats like XML, HTML, JSON or JAML,
 * the data will be located by the tree hierarchy by the xPath.
 */
public enum EHeader {
	/**
	 * The {@link DataContainer} is in tabular format and includes column headers which means that the
	 * elements added to the {@link #values()} ArrayList correspond to a row of the source.
	 */
	COLUMN,
	/**
	 * The data source is in tabular format and includes row headers which means that the elements added
	 * to the {@link #values()} ArrayList correspond to a column of the source.
	 */
	ROW,
	/**
	 * The data source is in tree format like XML. No header names will be assigned to the
	 * {@link DataContainer}.
	 */
	TREE,
	/**
	 * If UNKNOWN is used, the DataContainer assumes that the source includes semicolon separated
	 * columns without column and row headers.
	 */
	UNKNOWN;
}
