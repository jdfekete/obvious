package obviousx.util;

import java.text.Format;

/**
 * Factory for format.
 * @author Pierre-Luc Hemery
 *
 */
public abstract class FormatFactory {

  protected static FormatFactory instance;
  
  private FormatFactory() {}
  
  public static FormatFactory getInstance() {
    return instance;
  }
  
  public abstract Format getFormat(Class<?> spottedClass);
}
