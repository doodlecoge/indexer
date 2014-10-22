package com.ajaxw.ciscostuff;

import java.io.IOException;
import java.net.MalformedURLException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.ajaxw.io.IterableFile;
import com.ajaxw.net.HttpEngine;

public class CsvImportCcdUser {

	private static Logger logger = Logger.getLogger(CsvImportCcdUser.class);

	public static void main(String[] args) throws SQLException,
			KeyManagementException, MalformedURLException,
			NoSuchAlgorithmException, IOException {
		// String csv = "E:\\tmp\\cloud_services.csv";
		String csv = "E:\\tmp\\impt.csv";
		// processFile("");
		// validateFile(csv);
		csvAddUser(csv);
	}

	public static void csvAddUser(String path) throws KeyManagementException,
			MalformedURLException, NoSuchAlgorithmException, IOException {
		int lineNo = 0;
		for (String line : new IterableFile(path)) {
			lineNo++;
			User u = new User(line);

			if (u.getActive() == 1) {
				logger.info(">>> (" + lineNo + ") add/set user: "
						+ u.toString());

				int cnt = u.getCntByCecOrWbx();
				int orgCnt = u.getOrgCnt();

				if (cnt == 0) {
					logger.info("add user: " + u.getCecMail());

					if (orgCnt == 1) {
//						u.add();
					} else {
						logger.info("add: org cnt: " + orgCnt);
					}
				} else if (cnt == 1) {
					logger.info("user exists");
					if (orgCnt == 1) {
						 u.update();
					} else {
						logger.info("set: org cnt: " + orgCnt);
					}
				} else {
					logger.info("user count: " + cnt);
				}

				logger.info("<<< (" + lineNo + ") add/set user: "
						+ u.toString());
			} else {
				logger.info("active = " + u.getActive());
			}
		}
	}

	public static void validateFile(String fn) throws SQLException,
			KeyManagementException, MalformedURLException,
			NoSuchAlgorithmException, IOException {

		String fmt = "http://wwwin-tools.cisco.com/dir/vcard/%s.vcf";
		int lineNo = 0;

		for (String line : new IterableFile(fn)) {
			lineNo++;
			if (lineNo == 1)
				continue;

			User u = new User(line);

			// deactive user
			if (u.getActive() == 0) {
				int cnt = u.getCntByCecOrWbx();
				// user not exists or find multiple records
				if (cnt != 1)
					System.out.println(lineNo + ": cnt: " + cnt + u.toString());
				// deactive user
				else
					u.deactive();

				// add user or change user's org
			} else {
				int cnt = u.getCntByCecOrWbx();

				// no/multiple record found
				if (cnt != 1) {
					System.out.println(lineNo + ": cnt: " + cnt + u.toString());
					// no found, add it
					if (cnt == 0) {
						int orgCnt = u.getOrgCnt();
						// multiple org found with same name
						if (orgCnt != 1) {
							System.out.println("org cnt: " + orgCnt + ":"
									+ u.toString());
							// only one org found
						} else {
							int orgId = u.getOrgId();
							// System.out.println("orgId: " + orgId);
							u.add();
						}
					}

					// user found with only one record
				} else {
					// u.testFullName();
					int orgCnt = u.getOrgCnt();

					// multiple org found with same name
					if (orgCnt != 1) {
						System.out.println("org cnt: " + orgCnt + ":"
								+ u.toString());
						// only one org found
					} else {
						int orgId = u.getOrgId();
						// System.out.println("orgId: " + orgId);
						u.update();
					}
				}
			}
		}
	}

	public static String getFullName(String cec) throws KeyManagementException,
			MalformedURLException, NoSuchAlgorithmException, IOException {
		String fmt = "http://wwwin-tools.cisco.com/dir/vcard/%s.vcf";
		String vcard = String.format(fmt, cec);

		HttpEngine eng = new HttpEngine();
		String content = eng.get(vcard).getHtml();
		Pattern ptn = Pattern.compile("^fn[ ]*:[ ]*([a-zA-Z0-9 ]*)");
		Matcher m = ptn.matcher(content);

		if (m.find())
			return m.group(1);
		else
			return null;
	}

}
