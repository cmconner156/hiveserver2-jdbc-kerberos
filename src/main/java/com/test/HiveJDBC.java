package com.test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.DriverManager;

public class HiveJDBC {
  private static String driverName = "org.apache.hive.jdbc.HiveDriver";

  /**
 * @param args
 * @throws SQLException
   */
  public static void main(String[] args) throws SQLException {
      try {
      Class.forName(driverName);
    } catch (ClassNotFoundException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      System.exit(1);
    }
    System.setProperty("java.security.auth.login.config","gss-jaas.conf");
//    System.setProperty("sun.security.jgss.debug","true");
    System.setProperty("javax.security.auth.useSubjectCredsOnly","false");
    System.setProperty("java.security.krb5.conf","krb5.conf");

    Connection con = DriverManager.getConnection("jdbc:hive2://cdh42-1.test" +
      ".com:10000/default;principal=hive/cdh42-1.test.com@TEST.COM");
    Statement stmt = con.createStatement();

    String tableName = "testHiveDriverTable";
    stmt.execute("drop table if exists " + tableName);
    stmt.execute("create table " + tableName + " (username string, x string, " +
      "uid string, gid string, userdesc string, home string, " +
      "shell string) ROW FORMAT DELIMITED FIELDS TERMINATED BY ':'");

    // show tables
    String sql = "show tables '" + tableName + "'";
    System.out.println("Running: " + sql);
    ResultSet res = stmt.executeQuery(sql);
    if (res.next()) {
      System.out.println(res.getString(1));
    }
    // describe table
    sql = "describe " + tableName;
    System.out.println("Running: " + sql);
    res = stmt.executeQuery(sql);
    while (res.next()) {
      System.out.println(res.getString(1) + "\t" + res.getString(2));
    }

    // load data into table
    // NOTE: filepath has to be local to the hive server.  The above table
    // create matches /etc/passwd
    String filepath = "/etc/passwd";
    sql = "load data local inpath '" + filepath + "' into table " + tableName;
    System.out.println("Running: " + sql);
    stmt.execute(sql);

    // select * query
    sql = "select * from " + tableName;
    System.out.println("Running: " + sql);
    res = stmt.executeQuery(sql);
    while (res.next()) {
      System.out.println(String.valueOf(res.getString(1)) + "\t" + res
        .getString(2) + "\t" + res.getString(3) + "\t" + res.getString(4) +
        "\t" + res.getString(5) + "\t" + res.getString(6) + "\t" + res
        .getString(7));
    }

    // regular hive query
    sql = "select count(1) from " + tableName;
    System.out.println("Running: " + sql);
    res = stmt.executeQuery(sql);
    while (res.next()) {
      System.out.println(res.getString(1));
    }
  }
}

