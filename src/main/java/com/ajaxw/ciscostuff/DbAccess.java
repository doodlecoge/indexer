package com.ajaxw.ciscostuff;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DbAccess {
	Connection conn = null;

	public void openConnection() {
		try {
//			String userName = "root";
//			String password = "pass321";
//			String url = "jdbc:mysql://localhost:3316/ccd";

			 String userName = "root";
			 String password = "CruTada";
			 String url = "jdbc:mysql://10.224.96.18:3306/ccd";

			Class.forName("com.mysql.jdbc.Driver").newInstance();
			conn = DriverManager.getConnection(url, userName, password);
		} catch (Exception e) {
			System.err.println("Cannot connect to database server");
		}

	}

	public void closeConnection() {
		if (this.conn != null) {
			try {
				this.conn.close();
			} catch (Exception e) {
				System.err.println("closeConnection: " + e.getMessage());
			}
		}
	}

	public ResultSet executeQuery(String sql) throws SQLException {
		Statement s = this.conn.createStatement();
		return s.executeQuery(sql);
	}

	public int executeUpdate(String sql) throws SQLException {
		Statement s = this.conn.createStatement();
		return s.executeUpdate(sql);
	}

}
