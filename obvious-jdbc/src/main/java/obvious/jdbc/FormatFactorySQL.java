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

import java.util.Date;

import obviousx.text.TypedFormat;
import obviousx.util.FormatFactory;

/**
 * Implementation of FormatFactory for database applications.
 * @author Pierre-Luc Hemery
 *
 */
public class FormatFactorySQL implements FormatFactory {

  /**
   * Gives the associated TypedFormat object associated to a type entered as a
   * String. This method in this implementation recognizes classic java types :
   * String, boolean, numbers and date. This method is made to be overriden.
   * @param type The type as a String.
   * @return a TypedFormat object
   *
   */
  public TypedFormat getFormat(String type) {
    TypedSQLFormat format = new TypedSQLFormat();
    String typeLow = type.toLowerCase();
    if (typeLow.equals("char") || typeLow.equals("longvarchar")
          || typeLow.equals("varchar")) {
      return format.new TypedSQLCharFormat();
    } else if (typeLow.equals("blob") || typeLow.equals("clob")
          || typeLow.equals("array") || typeLow.equals("ref")
          || typeLow.equals("java_object") || typeLow.equals("other")
          || typeLow.equals("binary") || type.equals("varbinary")
          || typeLow.equals("longvarbinary")) {
      return format.new TypedSQLBasicFormat(type);
    } else if (typeLow.equals("bigint") || typeLow.equals("decimal")
          || typeLow.equals("double") || typeLow.equals("float")
          || typeLow.equals("numeric") || typeLow.equals("real")
          || typeLow.equals("int") || typeLow.equals("integer")
          || typeLow.equals("smallint") || typeLow.equals("tinyint")) {
      return format.new TypedSQLNumFormat(type);
    } else if (typeLow.equals("date") || typeLow.equals("time")
          || typeLow.equals("timestamp")) {
      return format.new TypedSQLTimeFormat(type);
    } else if (typeLow.equals("bit")) {
      return format.new TypedSQLBitFormat();
    } else {
      throw new
      IllegalArgumentException("Cannot return a corresponding Format for : "
          + type);
    }
  }


  /**
   * Gives the corresponding SQL format for a Java class.
   * This method should and could be overridden for particular JDBC / Obvious
   * implementations.
   * @param c Java class to "convert" in a SQL type
   * @return a corresponding SQL type in String format
   */
  public String getSQLType(Class<?> c) {
    if (c.equals(Integer.class)) {
      return "INT";
    } else if (c.equals(Long.class)) {
      return "BIGINT";
    } else if (c.equals(Double.class)) {
      return "DOUBLE";
    } else if (c.equals(Float.class)) {
      return "FLOAT";
    } else if (c.equals(Short.class)) {
      return "SMALLINT";
    } else if (c.equals(java.math.BigDecimal.class)) {
      return "DECIMAL";
    } else if (c.equals(Number.class)) {
      return "NUMERIC";
    } else if (c.equals(java.sql.Date.class) || c.equals(Date.class)) {
      return "DATE";
    } else if (c.equals(java.sql.Time.class)) {
      return "TIME";
    } else if (c.equals(java.sql.Timestamp.class)) {
      return "TIMESTAMP";
    } else if (c.equals(Boolean.class)) {
      return "BIT";
    } else if (c.equals(java.sql.Blob.class)) {
      return "BLOB";
    } else if (c.equals(java.sql.Clob.class)) {
      return "CLOB";
    } else if (c.equals(java.sql.Array.class)) {
      return "ARRAY";
    } else if (c.equals(java.sql.Ref.class)) {
      return "REF";
    } else if (c.equals(Byte[].class)) {
      return "LONGVARBINARY";
    } else if (c.equals(String.class)) {
      return "TEXT";
    } else {
      return "OTHERS";
    }
  }

}
