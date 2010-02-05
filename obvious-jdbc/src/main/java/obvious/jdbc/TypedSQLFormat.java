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

package obvious.jdbc;

import java.text.DecimalFormat;
import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;

import obviousx.text.TypedFormat;

/**
 * Implements all typed format classes for SQL types.
 * @author Pierre-Luc Hemery
 *
 */
public class TypedSQLFormat {


  /**
   * Inner class for Char SQL formats.
   * @author Pierre-Luc Hemery
   *
   */
  public class TypedSQLCharFormat extends Format implements TypedFormat {

    /**
     * Serial UID.
     */
    private static final long serialVersionUID = -4106389999073711738L;
    /**
     * Accurate Java type associated to the format.
     */
    private Class<?> type;

    /**
     * Constructor.
     */
    public TypedSQLCharFormat() {
      super();
      this.type = String.class;
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
      toAppendTo.append("'" + obj + "'");
      return toAppendTo;
    }

    @Override
    public Object parseObject(String source, ParsePosition pos) {
      pos.setIndex(source.length() - 1);
      return source;
    }

    /**
     * Returns the type associated to the format.
     * @return java type
     */
    public Class<?> getFormattedClass() {
      return this.type;
    }

  }

  /**
   * Inner class for SQL number formats.
   * @author Pierre-Luc Hemery
   *
   */
  public class TypedSQLNumFormat extends DecimalFormat implements TypedFormat {

    /**
     * Serial UID.
     */
    private static final long serialVersionUID = -6343458577027057690L;
    /**
     * Accurate Java type associated to the format.
     */
    private Class<?> type;

    /**
     * Constructor.
     * @param typeString name of the java type.
     */
    public TypedSQLNumFormat(String typeString) {
      super();
      String typeLow = typeString.toLowerCase();
      if (typeLow.equals("double") || typeLow.equals("float")) {
        this.type = Double.class;
      } else if (typeLow.equals("real")) {
        this.type = Float.class;
      } else if (typeLow.equals("bigint")) {
        this.type = Long.class;
      } else if (typeLow.equals("decimal") || typeLow.equals("numeric")) {
        this.type = java.math.BigDecimal.class;
      } else if (typeLow.equals("integer") || typeLow.equals("int")) {
        this.type = Integer.class;
      } else if (typeLow.equals("smallint")) {
        this.type = Short.class;
      } /* else if (typeLow.equals("smallint")) {
        this.type = Byte.class;
      } */ else {
        throw new IllegalArgumentException(typeString
            + " unknown type for the constructor!");
      }
    }

    /**
     * Returns the type associated to the format.
     * @return java type
     */
    public Class<?> getFormattedClass() {
      return this.type;
    }

  }

  /**
   * Inner class for SQL time/date formats.
   * @author Pierre-Luc Hemery
   *
   */
  public class TypedSQLTimeFormat extends Format implements TypedFormat {

    /**
     * Serial UID.
     */
    private static final long serialVersionUID = 1L;
    /**
     * Accurate Java type associated to the format.
     */
    private Class<?> type;

    /**
     * Constructor.
     * @param timeType name of the java type.
     */
    public TypedSQLTimeFormat(String timeType) {
      super();
      String typeLow = timeType.toLowerCase();
      if (typeLow.equals("date")) {
        this.type = java.sql.Date.class;
      } else if (typeLow.equals("time")) {
        this.type = java.sql.Time.class;
      } else if (typeLow.equals("timestamp")) {
        this.type = java.sql.Timestamp.class;
      } else {
        throw new IllegalArgumentException(timeType
            + " unknown type for the constructor!");
      }
    }

    @Override
    public StringBuffer format(Object obj, StringBuffer toAppendTo,
        FieldPosition pos) {
      if (this.type.equals(java.sql.Date.class)) {
        return new StringBuffer(((java.sql.Date) obj).toString());
      } else if (this.type.equals(java.sql.Time.class)) {
        return new StringBuffer(((java.sql.Time) obj).toString());
      } else if (this.type.equals(java.sql.Timestamp.class)) {
        return new StringBuffer(((java.sql.Timestamp) obj).toString());
      } else {
        throw new
        IllegalArgumentException("Cannot parsen given String as SQL Time/Date");
      }
    }

    @Override
    public Object parseObject(String source, ParsePosition pos) {
      if (this.type.equals(java.sql.Date.class)) {
        return java.sql.Date.valueOf(source);
      } else if (this.type.equals(java.sql.Time.class)) {
        return java.sql.Time.valueOf(source);
      } else if (this.type.equals(java.sql.Timestamp.class)) {
        return java.sql.Timestamp.valueOf(source);
      } else {
        throw new
        IllegalArgumentException("Cannot parsen given String as SQL Time/Date");
      }
    }

    /**
     * Returns the type associated to the format.
     * @return java type
     */
    public Class<?> getFormattedClass() {
      return this.type;
    }

  }

  /**
   * Inner class for SQL time/date formats.
   * @author Pierre-Luc Hemery
   *
   */
  public class TypedSQLBitFormat extends Format implements TypedFormat {

    /**
     * Serial UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Accurate Java type associated to the format.
     */
    private Class<?> type;

    /**
     * Constructor.
     */
    public TypedSQLBitFormat() {
      super();
      this.type = Boolean.class;
    }

    @Override
    public StringBuffer format(Object obj, StringBuffer toAppendTo,
        FieldPosition pos) {
      if ((Boolean) obj) {
        return new StringBuffer("1");
      } else {
        return new StringBuffer("0");
      }
    }

    @Override
    public Object parseObject(String source, ParsePosition pos) {
      return source.equals("1");
    }

    /**
     * Returns the type associated to the format.
     * @return java type
     */
    public Class<?> getFormattedClass() {
      return this.type;
    }

  }

  /**
   * Inner class for SQL time/date formats.
   * @author Pierre-Luc Hemery
   *
   */
  public class TypedSQLBasicFormat extends Format implements TypedFormat {

    /**
     * Serial UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Accurate Java type associated to the format.
     */
    private Class<?> type;

    /**
     * Constructor.
     * @param typeString string name of the java type.
     */
    public TypedSQLBasicFormat(String typeString) {
      super();
      String typeLow = typeString.toLowerCase();
      if (typeLow.equals("object") || typeLow.equals("others")
          || typeLow.equals("distinct")) {
        this.type = Object.class;
      } else if (typeLow.equals("ref")) {
        this.type = java.sql.Ref.class;
      } else if (typeLow.equals("clob")) {
        this.type = java.sql.Clob.class;
      } else if (typeLow.equals("blob")) {
        this.type = java.sql.Blob.class;
      } else if (typeLow.equals("array")) {
        this.type = java.sql.Array.class;
      } else if (typeLow.equals("binary") || typeLow.equals("varbinary")
            || typeLow.equals("longvarbinary")) {
        this.type = Byte[].class;
      } else {
        throw new IllegalArgumentException(typeString
            + " unknown type for the constructor!");
      }
    }

    @Override
    public StringBuffer format(Object obj, StringBuffer toAppendTo,
        FieldPosition pos) {
      // TODO Auto-generated method stub
      return null;
    }

    @Override
    public Object parseObject(String source, ParsePosition pos) {
      // TODO Auto-generated method stub
      return null;
    }

    /**
     * Returns the type associated to the format.
     * @return java type
     */
    public Class<?> getFormattedClass() {
      return this.type;
    }

  }


}
