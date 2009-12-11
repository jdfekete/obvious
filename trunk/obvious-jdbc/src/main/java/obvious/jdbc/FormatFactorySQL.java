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

import obviousx.text.TypedFormat;
import obviousx.util.FormatFactory;

/**
 * Implementation of FormatFactory for database applications.
 * @author Pierre-Luc Hemery
 *
 */
public class FormatFactorySQL extends FormatFactory {

  @Override
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

}
