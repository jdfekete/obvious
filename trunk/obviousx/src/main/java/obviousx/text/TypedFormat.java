package obviousx.text;

import java.text.FieldPosition;
import java.text.ParsePosition;

/**
 * Defines an extended Java Format supporting type attribute.
 * @author Pierre-Luc Hemery
 *
 */
public interface TypedFormat {

  /**
   * Formats an object and appends the resulting text to a given string buffer.
   * @param obj The object to format
   * @param toAppendTo where the text is to be appended
   * @param pos A FieldPosition identifying a field in the formatted text
   * @return the string buffer with formatted text appended
   */
  StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos);

  /**
   * Parses text from a string to produce an object.
   * @param source A String, part of which should be parsed.
   * @param pos A ParsePosition object with index and error index information.
   * @return An Object parsed from the string. In case of error, returns null.
   */
  Object parseObject(String source, ParsePosition pos);

  /**
   * Getter for the associated type with the defined format.
   * @return formatted Type.
   */
  Class<?> getFormattedClass();

}
