package com.ajaxw.test;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.ajaxw.ciscostuff.DbAccess;
import com.ajaxw.io.IterableFile;
import com.ajaxw.net.HttpEngine;
import com.ajaxw.util.Helper;

public class TTT {

	/**
	 * @param args
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 * @throws KeyManagementException
	 */
	public static void main(String[] args) throws IOException,
			KeyManagementException, NoSuchAlgorithmException {
//		String dir = "E:\\tmp\\ccd_expt\\";
//		BufferedWriter bw = new BufferedWriter(new FileWriter(dir + "new2.csv"));
//		int lineNo = 0;
//		for (String line : new IterableFile(dir + "new.csv")) {
//			lineNo++;
//
//			String cec = line.split(",")[0];
//			int orgId = getOrgId(cec);
//			int siteId = getSiteId(orgId);
//			String siteName = getOrgName(siteId);
//
//			bw.write(line);
//			bw.write(",");
//			bw.write(siteName);
//			bw.write("\n");
//		}
//		bw.close();
		
		int orgid = getOrgId("zhilwang@cisco.com");
		int siteId = getSiteId(orgid);
		System.out.println(getOrgName(siteId));
	}

	public static int getOrgId(String cec) {
		DbAccess db = new DbAccess();
		db.openConnection();
		int id = -1;
		try {
			ResultSet rs = db
					.executeQuery("select org_id from users where name2 = '"
							+ cec + "'");

			if (rs.next())
				id = rs.getInt("org_id");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		db.closeConnection();
		return id;
	}

	public static int getParentOrg(int orgId) {
		DbAccess db = new DbAccess();
		db.openConnection();
		int id = -1;
		try {
			ResultSet rs = db
					.executeQuery("select parent_id from orgs where id = "
							+ orgId);

			if (rs.next())
				id = rs.getInt("parent_id");

		} catch (SQLException e) {
			e.printStackTrace();
		}
		db.closeConnection();
		return id;
	}

	public static int getSiteId(int orgId) {
		int pid = getParentOrg(orgId);
		while (pid != 1) {
			orgId = pid;
			pid = getParentOrg(orgId);
		}

		return orgId;
	}

	public static String getOrgName(int orgId) {
		DbAccess db = new DbAccess();
		db.openConnection();
		String orgName = null;
		try {
			ResultSet rs = db.executeQuery("select name from orgs where id = "
					+ orgId);

			if (rs.next())
				orgName = rs.getString("name");

		} catch (SQLException e) {
			e.printStackTrace();
		}
		db.closeConnection();
		return orgName;
	}

	public static void attachEmployeeId() throws IOException,
			KeyManagementException, NoSuchAlgorithmException {
		String dir = "E:\\tmp\\ccd_expt\\";
		BufferedWriter bw = new BufferedWriter(new FileWriter(dir + "new.csv"));
		int lineNo = 0;
		for (String line : new IterableFile(dir
				+ "employee_credits_20120525135450635775963857.csv")) {
			lineNo++;
			if (lineNo == 1)
				continue;

			String cec = line.split(",")[0];
			cec = cec.substring(0, cec.indexOf("@"));

			String eid = getEmployeeId(cec);

			bw.write(line);
			bw.write(",");
			bw.write(eid);
			bw.write("\n");
		}
		bw.close();
	}

	public static String getEmployeeId(String cec)
			throws KeyManagementException, MalformedURLException,
			NoSuchAlgorithmException, IOException {
		HttpEngine eng = new HttpEngine();
		String str = eng.get("http://wwwin-tools.cisco.com/dir/details/" + cec)
				.getHtml();
		// System.out.println(str);

		Document doc = Jsoup.parse(str);

		Elements els = doc
				.select("table.orgtable > tbody > tr:eq(2) > td:eq(1)");

		for (Element el : els) {
			System.out.println(el.text());
		}

		return els.get(1).text().trim();
	}

}
