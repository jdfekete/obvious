/*
* Copyright (c) 2009, INRIA
* All rights reserved.
* Redistribution and use in source and binary forms, with or without
* modification, are permitted provided that the following conditions are met:
*
*     * Redistributions of source code must retain the above copyright
*       notice, this list of conditions and the following disclaimer.
*     * Redistributions in binary form must reproduce the above copyright
*       notice, this list of conditions and the following disclaimer in the
*       documentation and/or other materials provided with the distribution.
*     * Neither the name of INRIA nor the names of its contributors may
*       be used to endorse or promote products derived from this software
*       without specific prior written permission.
*
* THIS SOFTWARE IS PROVIDED BY THE REGENTS AND CONTRIBUTORS ``AS IS'' AND ANY
* EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
* WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
* DISCLAIMED. IN NO EVENT SHALL THE REGENTS AND CONTRIBUTORS BE LIABLE FOR ANY
* DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
* (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
* LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
* ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
* (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
* SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/

package obviousx.util;

import java.text.FieldPosition;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.text.DecimalFormat;
import java.text.Format;
import java.util.BitSet;
import java.util.Date;

import obviousx.ObviousxRuntimeException;
import obviousx.text.TypedFormat;


/**
 * Concrete implementation for FormatFactory.
 * It is an example, not a definitive model.
 * @author Pierre-Luc Hemery
 *
 */
public final class FormatFactoryImpl implements FormatFactory {

  /**
   * Inner Class for String Format.
   * @author Pierre-Luc Hemery
   *
   */
  public class TypedStringFormat extends Format implements TypedFormat {

    /**
     * Serial UID.
     */
    private static final long serialVersionUID = 3429029058249142776L;

    /**
     * Constructor.
     */
    public TypedStringFormat() {
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

    /**
     * Returns String.class.
     * @return formatted Type.
     */
    public Class<?> getFormattedClass() {
      return String.class;
    }

    @Override
    public Format getFormat() {
      return this;
    }

  }

  /**
   * Inner class for Boolean Format.
   * @author Pierre-Luc Hemery
   *
   */
  public class TypedBooleanFormat extends Format implements TypedFormat {

    /**
     * Serial UID.
     */
    private static final long serialVersionUID = -4518959879827828248L;

    /**
     * Constructor.
     */
    public TypedBooleanFormat() {
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
      if (source != null && source.equals("true")) {
        return true;
      } else if (source != null && source.equals("false")) {
        return false;
      } else if (source == null || source.equals("null")) {
        return false;
      } else {
        throw new
          IllegalArgumentException("Cannot parsen given String as Boolean");
      }
    }

    /**
     * Returns Boolean.class.
     * @return formatted Type.
     */
    public Class<?> getFormattedClass() {
      return Boolean.class;
    }

    @Override
    public Format getFormat() {
      return this;
    }

  }

  /**
   * Inner class for Extended Decimal Format.
   * @author Pierre-Luc Hemery
   *
   */
  public class TypedDecimalFormat extends DecimalFormat implements TypedFormat {

    /**
     * UID.
     */
    private static final long serialVersionUID = 2140009303846156603L;

    /**
     * Type of the number.
     */
    private Class<?> type;

    /**
     * Constructor with accurate type parameter.
     * @param numberType accurate Type of the Number
     */
    public TypedDecimalFormat(String numberType) {
      super();
      String typeLow = numberType.toLowerCase();
      if (typeLow.equals("integer") || typeLow.equals("int")) {
        this.type = Integer.class;
      } else if (typeLow.equals("double")) {
        this.type = Double.class;
      } else if (typeLow.equals("float")) {
        this.type = Float.class;
      } else if (typeLow.equals("short")) {
        this.type = Short.class;
      } else if (typeLow.equals("long")) {
        this.type = Long.class;
      } else if (typeLow.equals("byte")) {
        this.type = Byte.class;
      } else if (typeLow.equals("number")) {
        this.type = Number.class;
      } else if (typeLow.equals("bigdecimal")) {
        this.type = java.math.BigDecimal.class;
      } else {
        throw new IllegalArgumentException(numberType
            + " unknown type for the constructor!");
      }
    }

    @Override
    public Number parse(String val, ParsePosition p) {
      try {
        if (val == "null" || val == "" || val == null) {
          return null;
        }
        if (type.equals(Integer.class) || type.equals(int.class)) {
          return Integer.parseInt(val);
        } else if (type.equals(Double.class) || type.equals(double.class)) {
          return Double.parseDouble(val);
        } else if (type.equals(Float.class) || type.equals(float.class)) {
          return Float.parseFloat(val);
        } else if (type.equals(Short.class) || type.equals(short.class)) {
          return Short.parseShort(val);
        } else if (type.equals(Long.class) || type.equals(long.class)) {
          return Long.parseLong(val);
        } else if (type.equals(Byte.class) || type.equals(byte.class)) {
          return Byte.parseByte(val);
        } else if (type.equals(Number.class)) {
          return super.parse(val);
        } else if (type.equals(java.math.BigDecimal.class)) {
          return new java.math.BigDecimal(val);
        } else {
          throw new ObviousxRuntimeException(
              "Can't parse " + val + " to a number");
        }
      } catch (Exception e) {
        throw new ObviousxRuntimeException(e);
      }
     }

    /**
     * Constructor.
     */
    public TypedDecimalFormat() {
      this("Number");
    }


    /**
     * Returns Number.class.
     * @return formatted Type.
     */
    public Class<?> getFormattedClass() {
      return this.type;
    }

    @Override
    public Format getFormat() {
      return this;
    }

  }

  /**
   * Inner class for Extended SimpleDateFormat class.
   * @author Pierre-Luc Hemery
   *
   */
  public class TypedDateFormat extends SimpleDateFormat implements TypedFormat {

    /**
     * UID.
     */
    private static final long serialVersionUID = 7811185128843436214L;

    /**
     * Constructor.
     * @param format format of the date.
     */
    public TypedDateFormat(String format) {
      super(format);
    }

    /**
     * Returns Date.class.
     * @return formatted Type.
     */
    public Class<?> getFormattedClass() {
      return Date.class;
    }

    @Override
    public Format getFormat() {
      return this;
    }
  }

  /**
   * Inner class for BitSet format class.
   * @author Hemery
   *
   */
  public class TypedBitSetFormat extends Format implements TypedFormat {

    /**
     * Serial ID.
     */
    private static final long serialVersionUID = 523447675781107765L;

    /**
     * Constructor.
     */
    public TypedBitSetFormat() {
      super();
    }

    @Override
    public StringBuffer format(Object obj, StringBuffer toAppendTo,
        FieldPosition pos) {
      if (obj instanceof BitSet) {
        return format((BitSet) obj);
      } else {
        throw new
          IllegalArgumentException("Cannot format given Boolean as String");
      }
    }

    /**
     * Format the given BitSet instance.
     * @param set BitSet instance to format.
     * @return BitSet as a String.
     */
    public StringBuffer format(BitSet set) {
      return new StringBuffer(set.toString());
    }

    @Override
    public Class<?> getFormattedClass() {
      return java.util.BitSet.class;
    }

    @Override
    public Object parseObject(String source, ParsePosition pos) {
      if (source == null) {
        return null;
      } else {
        try {
          if (!source.endsWith("{") || !source.startsWith("{")) {
            return null;
          }
          String trueString = source.substring(0, source.length() - 1);
          String[] trueBits = trueString.split(",");
          BitSet set = new BitSet(Integer.parseInt(
              trueBits[trueBits.length - 1]));
          for (int i = 0; i < trueBits.length; i++) {
            set.set(Integer.parseInt(trueBits[i]));
          }
          return set;
        } catch (Exception e) {
          throw new IllegalArgumentException(e);
        }
      }
    }

    @Override
    public Format getFormat() {
      return this;
    }

  }

  /**
   * Gives the associated TypedFormat object associated to a type entered as a
   * String. This method in this implementation recognizes classic java types :
   * String, boolean, numbers and date. This method is made to be overriden.
   * @param type The type as a String.
   * @return a TypedFormat object
   *
   */
  public TypedFormat getFormat(String type) {
    String typeLow = type.toLowerCase();
    if (typeLow.equals("integer") || typeLow.equals("double")
        || typeLow.equals("float") || typeLow.equals("short")
        || typeLow.equals("byte") || typeLow.equals("long")
        || typeLow.equals("number") || typeLow.equals("int")
        || typeLow.equals("bigdecimal")) {
      return new TypedDecimalFormat(type);
    } else if (typeLow.equals("date")) {
      return new TypedDateFormat("dd/MM/yyyy");
    } else if (typeLow.equals("boolean")) {
      return new TypedBooleanFormat();
    } else if (typeLow.equals("string")) {
      return new TypedStringFormat();
    } else if (typeLow.equals("bitset")) {
      return new TypedBitSetFormat();
    } else {
      throw new
        IllegalArgumentException("Cannot return a corresponding Format for : "
            + type);
    }

  }

  /**
   * Gives the corresponding TypedFormat for a given Format.
   * @param format a Format instance
   * @return corresponding TypedFormat instance
   */
  public TypedFormat getFormat(Format format) {
    if (format.getClass().equals(DecimalFormat.class)) {
      return new  TypedDecimalFormat("number");
    } else if (format.getClass().equals(SimpleDateFormat.class)) {
      return new TypedDateFormat("dd/MM/yyyy");
    } else if (format.getClass().equals(TypedBooleanFormat.class)) {
      return new TypedBooleanFormat();
    } else if (format.getClass().equals(TypedStringFormat.class)) {
      return new TypedStringFormat();
    } else if (format.getClass().equals(TypedBitSetFormat.class)) {
      return new TypedBitSetFormat();
    } else {
      throw new
      IllegalArgumentException("Cannot return a corresponding TypedFormat"
          + "for : " + format);
    }
  }
}
