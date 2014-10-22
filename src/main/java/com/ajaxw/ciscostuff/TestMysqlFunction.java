package com.ajaxw.ciscostuff;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TestMysqlFunction {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		DbAccess db = new DbAccess();

		db.openConnection();
		try {
			ResultSet rs = db
					.executeQuery("select ORG_PATH(org_id) path, org_id from users limit 10");
			while (rs.next()) {
				String path = rs.getString("path");
				int oid = rs.getInt("org_id");

				System.out.println(path + ", " + oid);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		db.closeConnection();
	}

}
