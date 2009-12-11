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
import java.util.Date;

import obviousx.text.TypedFormat;


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
      if (source.equals("true")) {
        return true;
      } else if (source.equals("false")) {
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
     * Constructor with accurate type paramater.
     * @param numberType accurate Type of the Number
     */
    public TypedDecimalFormat(String numberType) {
      super();
      String typeLow = numberType.toLowerCase();
      if (typeLow.equals("integer")) {
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
      } else {
        throw new IllegalArgumentException(numberType
            + " unknown type for the constructor!");
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
  }

  @Override
  public TypedFormat getFormat(String type) {
    String typeLow = type.toLowerCase();
    if (typeLow.equals("integer") || typeLow.equals("double")
        || typeLow.equals("float") || typeLow.equals("short")
        || typeLow.equals("byte") || typeLow.equals("long")
        || typeLow.equals("number")) {
      return new TypedDecimalFormat(type);
    } else if (typeLow.equals("date")) {
      return new TypedDateFormat("dd/MM/yyyy");
    } else if (typeLow.equals("boolean")) {
      return new TypedBooleanFormat();
    } else if (typeLow.equals("string")) {
      return new TypedStringFormat();
    } else {
      throw new
        IllegalArgumentException("Cannot return a corresponding Format for : "
            + type);
    }

  }

}
