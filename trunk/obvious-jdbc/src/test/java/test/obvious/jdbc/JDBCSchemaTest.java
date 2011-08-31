/*
* Copyright (c) 2010, INRIA
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

package test.obvious.jdbc;

/*import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import obvious.data.Schema;
import obvious.jdbc.data.JDBCObviousSchema;
import test.obvious.data.SchemaTest;*/

/**
 * Implementation of  Schema  test-case for JDBCObviousSchema implementation.
 * @author Pierre-Luc Hemery
 *
 */
public class JDBCSchemaTest /*extends SchemaTest*/ {

/*  *//**
   * Driver for the database.
   *//*
  public static final String DRIVER = "com.mysql.jdbc.Driver";

  *//**
   * URL for the test database.
   *//*
  public static final String URL = "jdbc:mysql:///test";

  *//**
   * Login for the test database.
   *//*
  public static final String LOGIN = "root";

  *//**
   * Database for the test database.
   *//*
  public static final String PASSWORD = "";

  *//**
   * Name of the test table.
   *//*
  public static final String TABLE_NAME = "TEST_CASE_TABLE";

  @Override
  public Schema newInstance() {
    return new JDBCObviousSchema(DRIVER, URL, LOGIN, PASSWORD, TABLE_NAME);
  }

  @Override
  public void tearDown() {
    try {
      Class.forName(DRIVER);
    } catch (ClassNotFoundException e) {
      System.err.print("ClassNotFoundException: ");
      System.err.println(e.getMessage());
    }
    Connection con = null;
    PreparedStatement pStatement = null;
    if (((JDBCObviousSchema) this.getSchema()).tableExist()) {
      try {
        con = DriverManager.getConnection(URL, LOGIN, PASSWORD);
        String request = "DROP TABLE " + TABLE_NAME;
        pStatement = con.prepareStatement(request);
        pStatement.execute(request);
      } catch (SQLException e) {
        System.err.println("SQLException: " + e.getMessage());
        if (con != null) {
          try {
            System.err.print("Transaction is being rolled back");
            con.rollback();
          } catch (SQLException ex) {
            System.err.print("SQLException: ");
            System.err.println(ex.getMessage());
          }
        }
      } finally {
        try { pStatement.close(); } catch (Exception e) { e.printStackTrace(); }
        try { con.close(); } catch (Exception e) { e.printStackTrace(); }
      }
    }
    setSchema(null);
  }*/

}
