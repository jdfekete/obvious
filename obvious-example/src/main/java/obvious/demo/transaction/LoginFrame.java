/*
* Copyright (c) 2011, INRIA
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

package obvious.demo.transaction;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import obvious.ObviousException;

/**
 * A login frame.
 * @author Hemery
 *
 */
@SuppressWarnings("serial")
public class LoginFrame extends JFrame implements ActionListener {

  /**
   * Labels.
   */
  private JLabel userLabel, urlLabel, pswdLabel, tableLabel, tableKeyLabel,
      driverLabel;

  /**
   * Fields.
   */
  private JTextField userField, urlField, pswdField, tableField, tableKeyField,
      driverField;

  /**
   * Button of the form.
   */
  private JButton submit;

  /**
   * Params of the connexion.
   */
  private String url, user, pswd, tableName, tableKey, driver;

  /**
   * JPanel.
   */
  private JPanel panel;

  /**
   * Constructor.
   */
  public LoginFrame() {
    userLabel = new JLabel();
    userLabel.setText("User name");
    userField = new JTextField(15);

    pswdLabel = new JLabel();
    pswdLabel.setText("User password");
    pswdField = new JPasswordField(15);

    tableLabel = new JLabel();
    tableLabel.setText("Table name");
    tableField = new JTextField(15);
    tableField.setText("person");

    tableKeyLabel = new JLabel();
    tableKeyLabel.setText("Table primary key");
    tableKeyField = new JTextField(15);
    tableKeyField.setText("name");

    urlLabel = new JLabel();
    urlLabel.setText("Database url");
    urlField = new JTextField(50);
    urlField.setText("jdbc:mysql://localhost/test");

    driverLabel = new JLabel();
    driverLabel.setText("Driver classpath");
    driverField = new JTextField(50);
    driverField.setText("com.mysql.jdbc.Driver");

    submit = new JButton("SUBMIT");
    submit.addActionListener(this);

    panel = new JPanel(new GridLayout(7, 1));
    panel.add(driverLabel);
    panel.add(driverField);
    panel.add(urlLabel);
    panel.add(urlField);
    panel.add(userLabel);
    panel.add(userField);
    panel.add(pswdLabel);
    panel.add(pswdField);
    panel.add(tableLabel);
    panel.add(tableField);
    panel.add(tableKeyLabel);
    panel.add(tableKeyField);
    panel.add(submit);

    setContentPane(panel);
    setTitle("Login Form");
  }

  /**
   * Gets password.
   * @return password
   */
  private String getPswd() {
    return pswd;
  }

  /**
   * Sets password.
   * @param inPswd password to set
   */
  private void setPswd(String inPswd) {
    this.pswd = inPswd;
  }

  /**
   * Gets user.
   * @return user
   */
  private String getUser() {
    return user;
  }

  /**
   * Sets user.
   * @param inUser user to set
   */
  private void setUser(String inUser) {
    this.user = inUser;
  }

  /**
   * Gets url.
   * @return url
   */
  private String getUrl() {
    return url;
  }

  /**
   * Sets url.
   * @param inUrl url to set
   */
  private void setUrl(String inUrl) {
    this.url = inUrl;
  }

  /**
   * Gets driver.
   * @return driver
   */
  private String getDriver() {
    return driver;
  }

  /**
   * Sets driver.
   * @param inDriver driver to set
   */
  private void setDriver(String inDriver) {
    this.driver = inDriver;
  }

  /**
   * Gets table key.
   * @return table primary key
   */
  private String getTableKey() {
    return tableKey;
  }

  /**
   * Sets table key.
   * @param inTableKey table key to set
   */
  private void setTableKey(String inTableKey) {
    this.tableKey = inTableKey;
  }

  /**
   * Gets table name.
   * @return table name
   */
  private String getTableName() {
    return tableName;
  }

  /**
   * Sets table name.
   * @param inTableName table name to set
   */
  private void setTableName(String inTableName) {
    this.tableName = inTableName;
  }

  /**
   * Listener.
   * @param e an event
   */
  public void actionPerformed(ActionEvent e) {
    if (e.getSource().equals(submit)) {
      setUser(userField.getText());
      setPswd(pswdField.getText());
      setUrl(urlField.getText());
      setDriver(driverField.getText());
      setTableKey(tableKeyField.getText());
      setTableName(tableField.getText());
      this.setVisible(false);
      this.dispose();
      try {
        TransactionDemo.fillTable(getUrl(), getUser(), getPswd(), getDriver(),
            getTableName(), getTableKey());
      } catch (ObviousException e1) {
        // TODO Auto-generated catch block
        e1.printStackTrace();
      }
    }
  }

}
