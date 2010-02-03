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
    = {"col1", "col2", "col3"};

  /**
   * Types for the columns.
   */
  Class<?>[]  TYPES
    = {String.class, Integer.class, Boolean.class};

  /**
   * Default values for columns.
   */
  Object[]  DEFAULTS
  = {"Hi", 0, false};

  /**
   * Data Set for the first row.
   */
  Object[] ROW1 =
    {"Bonjour", 1, true};

  /**
   * Data Set for the second row.
   */
  Object[] ROW2 =
    {"Hello", 2, false};

  /**
   * Data Set for the third row.
   */
  Object[] ROW3 =
    {"Hallo", 3, false};

  /**
   * Data Set for the fourth row.
   */
  Object[] ROW4 =
    {"Nihaho", 4, true};

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
