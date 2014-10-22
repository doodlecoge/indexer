package com.ajaxw.ciscostuff;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GetSubOrgs {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		getAllSubOrgs(105);
	}

	public static void getAllSubOrgs(int orgId) {

		ArrayList<Integer> lst1 = new ArrayList<Integer>();
		ArrayList<Integer> lst2 = new ArrayList<Integer>();

		lst2.add(orgId);

		while (lst2.size() > 0) {
			int id = lst2.remove(0);
			lst1.add(id);
			lst2.addAll(getSubOrgs(id));
		}

		for (Integer id : lst1) {
			System.out.println(id);
		}
	}

	public static List<Integer> getSubOrgs(int orgId) {
		DbAccess db = new DbAccess();
		db.openConnection();

		List<Integer> lst = new ArrayList<Integer>();

		try {
			ResultSet rs = db
					.executeQuery("select id from orgs where parent_id="
							+ orgId);
			while (rs.next()) {
				int id = rs.getInt("id");
				lst.add(id);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		db.closeConnection();
		return lst;
	}

}
