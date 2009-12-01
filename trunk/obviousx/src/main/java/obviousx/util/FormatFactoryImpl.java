package obviousx.util;

import java.text.FieldPosition;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.text.DecimalFormat;
import java.text.Format;
import java.util.Date;


/**
 * Concrete implementation for FormatFactory.
 * It is an example, not a definitive model.
 * @author Pierre-Luc Hemery
 *
 */
public final class FormatFactoryImpl extends FormatFactory {

  /**
   * Inner Class for String Format.
   * @author Pierre-Luc Hemery
   *
   */
  public class StringFormat extends Format {

    /**
     * Serial UID.
     */
    private static final long serialVersionUID = 3429029058249142776L;

    /**
     * Constructor.
     */
    public StringFormat() {
      super();
    }

    @Override
    public StringBuffer format(Object obj, StringBuffer toAppendTo,
        FieldPosition pos) {
        if (obj instanceof String) {
          return format((String) obj, toAppendTo);
        } else {
          throw new
            IllegalArgumentException("Cannot format given Object as String");
        }
    }

    /**
     * Returns a StringBuffer.
     * @param obj String to convert
     * @param toAppendTo base StringBuffer.
     * @return StringBuffer appended with the original string.
     */
    public StringBuffer format(String obj, StringBuffer toAppendTo) {
      toAppendTo.append(obj);
      return toAppendTo;
    }

    @Override
    public Object parseObject(String source, ParsePosition pos) {
      pos.setIndex(source.length() - 1);
      return source;
    }

  }

  /**
   * Inner class for Boolean Format.
   * @author Pierre-Luc Hemery
   *
   */
  public class BooleanFormat extends Format {

    /**
     * Serial UID.
     */
    private static final long serialVersionUID = -4518959879827828248L;

    /**
     * Constructor.
     */
    public BooleanFormat() {
      super();
    }

    @Override
    public StringBuffer format(Object obj, StringBuffer toAppendTo,
        FieldPosition pos) {
      if (obj instanceof Boolean) {
        return format((Boolean) obj);
      } else {
        throw new
          IllegalArgumentException("Cannot format given Boolean as String");
      }
    }

    /**
     * Format an input Boolean as a string.
     * @param obj Input boolean
     * @return string corresponding to the boolean
     */
    public StringBuffer format(Boolean obj) {
      if (obj) {
        return new StringBuffer("true");
      } else {
        return new StringBuffer("false");
      }
    }

    @Override
    public Object parseObject(String source, ParsePosition pos) {
      if (source.equals("true")) {
        return true;
      } else if (source.equals("false")) {
        return false;
      } else {
        throw new
          IllegalArgumentException("Cannot parsen given String as Boolean");
      }
    }

  }

  @Override
  public Format getFormat(Class<?> type) {
    if (type.equals(Integer.class) || type.equals(Double.class)
        || type.equals(Float.class) || type.equals(Short.class)
        || type.equals(Byte.class) || type.equals(Long.class)) {
      return new DecimalFormat();
    } else if (type.equals(Date.class)) {
      return new SimpleDateFormat();
    } else if (type.equals(Boolean.class)) {
      return new BooleanFormat();
    } else if (type.equals(String.class)) {
      return new StringFormat();
    } else {
      throw new
        IllegalArgumentException("Cannot return a corresponding Format.");
    }

  }

}
