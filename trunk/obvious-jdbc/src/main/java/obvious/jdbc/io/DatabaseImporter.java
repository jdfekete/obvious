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

package obvious.jdbc.io;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import obvious.data.DataFactory;
import obvious.data.Table;
import obviousx.util.FormatFactory;

/**
 * Class that loads data contained in a database into
 * an obvious table.
 * @author Pierre-Luc Hemery
 *
 */
public class DatabaseImporter {

  /**
   * JDBC connection for the spotted database.
   */
  private Connection con;

  /**
   * Database metadata.
   */
  private DatabaseMetaData metadata;

  /**
   * Table(s) contained in the DataBase.
   */
  private Map<String, Table> table;

  /**
   * DataFactory to create table.
   */
  private DataFactory dataFactory;

  /**
   * FormatFactory to create Format.
   */
  private FormatFactory formatFactory;

  /**
   * Constructor.
   * @param driver JDBC driver to load.
   * @param url URL to connect to the database.
   * @param username login to access to the database.
   * @param password password to access to the database.
   * @param dFactory DataFactory to use to create tables.
   * @param fFactory FormatFactory to use to create formats.
   */
  public DatabaseImporter(String driver, String url, String username,
        String password, DataFactory dFactory, FormatFactory fFactory) {
    this.table = new HashMap<String, Table>();
    this.dataFactory = dFactory;
    this.formatFactory = fFactory;
    try {
      Class.forName(driver);
      this.con = DriverManager.getConnection(url, username, password);
      this.metadata = con.getMetaData();
    } catch (ClassNotFoundException e) {
      System.err.println("Can't load " + driver + " as a jdbc driver!");
    } catch (SQLException e) {
      System.err.println("Bad URL or invalid username/password!");
    }
  }

  /**
   * Getter for the connection attribute.
   * @return connection attribute
   */
  public Connection getConnection() {
    return this.con;
  }

  /**
   * Getter for table attribute.
   * @return table map attribute
   */
  public Map<String, Table> getTableMap() {
    return this.table;
  }

  /**
   * Tables importer.
   * @throws SQLException if bad thing happen.
   */
  public void importTables() throws SQLException {
    ResultSet setTable = metadata.getTables(null, null, "%", null);
    while (setTable.next()) {
      TableImporter importer = new TableImporter(setTable
           .getString("TABLE_NAME"), metadata, con, dataFactory, formatFactory);
      importer.createTable();
      Table tableImported = importer.getTable();
      table.put(setTable.getString("TABLE_NAME"), tableImported);
    }
  }

}
