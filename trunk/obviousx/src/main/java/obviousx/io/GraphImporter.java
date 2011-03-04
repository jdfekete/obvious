package obviousx.io;

import obvious.data.Data;
import obviousx.ObviousxException;
import obviousx.util.FormatFactory;

public interface GraphImporter {
	/**
	 * Get the FormatFactory associated to this Importer.
	 * @return the FormatFactory of this Importer
	 */
	FormatFactory getFormatFactory();

	/**
	 * Sets for the Importer the format factory.
	 * @param formatFactory the factory to set
	 */
	void setFormatFactory(FormatFactory formatFactory);

	/**
	 * Reads the schema of an external medium.
	 * @throws ObviousxException when a bad schema structure is used in the file.
	 */
	void readSchema() throws ObviousxException;

	/**
	 * Load the content of an external medium (file, db) into an Obvious Table.
	 * @throws ObviousxException when an exception occurs.
	 */
	void loadTable() throws ObviousxException;

	// TODO: needs to be removed, loadTable renamed to loadGraph and should
	// return whatever appropriate.
	Data getData();
}
