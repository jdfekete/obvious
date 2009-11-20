package test.obvious.data;

public interface TableTestData {

  public static final int NUMROW = 4;
  public static final int NUMCOL = 3;

  public static final String[]  HEADERS
    = {"string", "integer", "boolean"};

  public static final Class[]  TYPES
    = {String.class, Integer.class, Boolean.class};
  
  public static final Object[]  DEFAULTS
  = {"", 0, false};

  public static final String[] COLUMN1
    = {"Bonjour", "Hello", "Hallo", "Nihaho"};

  public static final Integer[] COLUMN2
    = {1, 2, 3, 4};

  public static final Boolean[] COLUMN3
  = {true, false, false, true};
}
