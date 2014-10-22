package com.ajaxw.ciscostuff;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import com.ajaxw.io.IterableFile;

public class Csv2Tree {

	public static void main(String[] args) {
		
		String dir = "E:\\tmp\\ccd_impt\\";

		String file = dir + "grace.csv";

		for (String line : new IterableFile(file)) {
			line = line.trim();
			if (",,,".equalsIgnoreCase(line))
				continue;

			String[] cols = line.split(",");

			if (cols.length == 4) {
				int cnt = getOrgCount(cols[0].trim() + " New");
				if (cnt == 1) {

				} else {
					System.out.println("cnt:" + cnt + ":orgName:" + cols[0]);
				}
			} else {
				System.out.println("******");
			}
		}
	}

	public static int getOrgId(String orgName) {
		orgName = orgName.trim();

		DbAccess db = new DbAccess();
		db.openConnection();

		int id = -1;
		try {
			ResultSet rs = db.executeQuery("select id from orgs where name='"
					+ orgName + "'");
			rs.next();
			id = rs.getInt("id");
		} catch (SQLException e) {
		}

		db.closeConnection();
		return id;
	}

	public static int getOrgCount(String orgName) {
		orgName = orgName.trim();

		DbAccess db = new DbAccess();
		db.openConnection();

		int cnt = 0;
		try {
			ResultSet rs = db
					.executeQuery("select count(id) cnt from orgs where name='"
							+ orgName + "'");
			rs.next();
			cnt = rs.getInt("cnt");
		} catch (SQLException e) {
			cnt = -1;
		}

		db.closeConnection();
		return cnt;
	}
}
