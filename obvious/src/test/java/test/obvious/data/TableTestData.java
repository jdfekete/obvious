package test.obvious.data;

/**
 * Interface to provide some data for testing Table class.
 *
 * @author Pierre-Luc Hemery
 *
 */
public interface TableTestData {

  /**
   * Number of rows of the table.
   */
  int NUMROW = 4;
  /**
   * Number of columns of the table.
   */
  int NUMCOL = 3;

  /**
   * Headers of the columns.
   */
  String[]  HEADERS
    = {"string", "integer", "boolean"};

  /**
   * Types for the columns.
   */
  Class<?>[]  TYPES
    = {String.class, Integer.class, Boolean.class};

  /**
   * Default values for columns.
   */
  Object[]  DEFAULTS
  = {"", 0, false};

  /**
   * Data set for the first column.
   */
  String[] COLUMN1
    = {"Bonjour", "Hello", "Hallo", "Nihaho"};

  /**
   * Data set for the second column.
   */
  Integer[] COLUMN2
    = {1, 2, 3, 4};

  /**
   * Data set for the first column.
   */
  Boolean[] COLUMN3
  = {true, false, false, true};

  /**
   * To string method for the data set.
   * @return string for the list of columns
   */
  String toString();
}
