package com.ajaxw.ciscostuff;

import java.io.IOException;
import java.net.MalformedURLException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.ajaxw.io.IterableFile;
import com.ajaxw.net.HttpEngine;

public class ImportGrace {

	public static String ids = "(22,25,28,29,30,31,33,34,35,36,37,105,149,150,173,178,39,172,54,56,44,120,124,32,38,47,50,151,152,179,116,117,118,119,164,165,166,122,48,49,51,109,110,106,115,113,114)";
	public static List<String> lines = new ArrayList<String>();
	private static Logger logger = Logger.getLogger("fileLogger");

	public static void main(String[] args) {
		// init();
		// importUsers();

		// importNewUser();
		// deactiveUser();

		// setOrg(22);
		reOrg();
	}

	public static void reOrg() {
		String path = "E:\\tmp\\ccd_impt\\cs_reorg.csv";
		for (String line : new IterableFile(path)) {
			String[] cols = line.split(",");
			String cec = cols[0].trim();
			String orgId = cols[1].trim();

			int userCnt = getUserCount(cec);
			if (userCnt == 1) {
				if (!orgIdExists(orgId)) {
					System.out.println("not exist");
				}

				int userId = getUserId(cec);
				if (userId > 0) {
					changeOrg(userId, Integer.parseInt(orgId));
				} else {
					logger.info("userId:--" + userId);
				}
			} else {
				System.out.println("user cnt:" + userCnt);
			}
		}
	}

	public static boolean orgIdExists(String orgid) {
		DbAccess db = new DbAccess();
		db.openConnection();

		int id = -1;
		try {
			ResultSet rs = db.executeQuery("select id from orgs where id="
					+ orgid);
			rs.next();
			id = rs.getInt("id");

		} catch (SQLException e) {
			id = -1;
			logger.info("orgId:" + orgid + ":" + e.getMessage());
		}

		db.closeConnection();
		return id > 0;
	}

	public static void setOrg(int orgId) {
		String path = "E:\\tmp\\ccd_impt\\add.csv";

		for (String line : new IterableFile(path)) {
			String cec = line.trim();
			cec += "@cisco.com";

			int userId = getUserId(cec);

			if (userId > 0) {
				if (changeOrg(userId, orgId))
					System.out.println("set org:" + userId);
			} else {
				System.out.println("failed set org:" + userId);
			}
		}
	}

	public static boolean changeOrg(int userId, int orgId) {
		DbAccess db = new DbAccess();
		db.openConnection();

		try {
			db.executeUpdate("update users set org_id=" + orgId + " where id="
					+ userId);
		} catch (SQLException e) {
			logger.info("chageOrg:" + userId + ":" + orgId + ":"
					+ e.getMessage());
			return false;
		}

		db.closeConnection();

		return true;
	}

	public static void deactiveUser() {
		String path = "E:\\tmp\\ccd_impt\\deactive.csv";

		for (String line : new IterableFile(path)) {
			String cec = line.trim();
			cec += "@cisco.com";

			int userId = getUserId(cec);

			if (userId > 0) {
				if (deactive(userId))
					System.out.println("deactive:" + userId);
			} else {
				System.out.println("failed deactive:" + userId);
			}
		}
	}

	public static boolean deactive(int userId) {
		DbAccess db = new DbAccess();
		db.openConnection();

		try {
			db.executeUpdate("update users set active=0 where id=" + userId);
		} catch (SQLException e) {
			logger.info("deactive:" + userId + ":" + e.getMessage());
			return false;
		}

		db.closeConnection();
		return true;
	}

	public static void importNewUser() {
		String path = "E:\\tmp\\ccd_impt\\add.csv";

		for (String line : new IterableFile(path)) {
			String cec = line.trim();
			cec += "@cisco.com";

			int cnt = getUserCount(cec);
			if (cnt == 0) {
				String full_name = getFullName(cec);

				if (full_name == null) {
					logger.info("get full name failed:" + cec);
					continue;
				}

				addUser(cec, 154, full_name);

				int userId = getUserId(cec);

				if (userId > 0) {
					int roleCnt = getRoleEmployeeCount(userId);
					if (roleCnt == 0) {
						addRoleEmployee(userId);
					} else {
						logger.info("userId:" + userId + ",roleCnt:" + roleCnt);
					}
				} else {
					logger.info("userId:" + userId);
				}
			} else if (cnt == 1) {
				int userId = getUserId(cec);
				int roleCnt = getRoleEmployeeCount(userId);
				if (roleCnt == 0) {
					addRoleEmployee(userId);
				} else {
					logger.info("---userId:" + userId + ",roleCnt:" + roleCnt);
				}
			}
		}
	}

	public static int getRoleEmployeeCount(int userId) {
		DbAccess db = new DbAccess();
		db.openConnection();

		int cnt = 0;
		try {
			ResultSet rs = db
					.executeQuery("select count(role_id) cnt from roles_users where role_id=4 and user_id="
							+ userId);
			rs.next();
			cnt = rs.getInt("cnt");
		} catch (SQLException e) {
			cnt = -1;
			logger.info("getRoleEmployeeCount:" + userId + ":" + e.getMessage());
		}

		db.closeConnection();
		return cnt;
	}

	public static void addRoleEmployee(int userId) {
		DbAccess db = new DbAccess();
		db.openConnection();

		try {
			db.executeUpdate("insert into roles_users (user_id, role_id) values ("
					+ userId + ", 4)");
		} catch (SQLException e) {
			logger.info("addRoleEmployee:" + userId + ":" + e.getMessage());
		}

		db.closeConnection();
	}

	public static void importUsers() {
		logger.info("lines: " + lines.size());

		for (String line : lines) {
			System.out.println("****** " + line);
			String cols[] = line.split(",");

			String orgName = cols[0].trim() + " New";
			String cec = cols[3].trim();
			if (!cec.endsWith("@cisco.com"))
				cec += "@cisco.com";

			int orgcnt = getOrgCount(orgName);
			if (orgcnt == 1) {
				int orgID = getOrgId(orgName);
				if (orgID == -1) {
					continue;
				}

				int usercnt = getUserCount(cec);

				if (usercnt == 1) {
					int userID = getUserId(cec);
					if (userID == -1) {
						continue;
					}

					// updateUserOrg(userID, orgID);
					System.out.println("updateUserOrg," + userID + "," + orgID);
				} else if (usercnt == 0) {
					String fullName = getFullName(cec);
					if (null == fullName) {
						System.out.println(fullName);
						continue;
					}

					addUser(cec, orgID, fullName);
					System.out.println("add user," + cec + "," + orgID + ","
							+ fullName);
				} else {

					logger.info("user cnt:" + usercnt + ":" + line);
				}
			} else {
				logger.info("org cnt:" + orgcnt + ":" + line);
			}
		}
	}

	public static void init() {
		String dir = "E:\\tmp\\ccd_impt\\";
		String file = dir + "grace.csv";

		for (String line : new IterableFile(file)) {
			if (",,,".equalsIgnoreCase(line.trim()))
				continue;

			String[] cols = line.split(",");

			if (cols.length != 4)
				continue;

			lines.add(line);
		}
	}

	public static int getUserCount(String cec) {
		cec = cec.trim();

		DbAccess db = new DbAccess();
		db.openConnection();

		int cnt = 0;
		try {
			ResultSet rs = db
					.executeQuery("select count(id) cnt from users where active=1 and name2='"
							+ cec + "'");
			rs.next();
			cnt = rs.getInt("cnt");
		} catch (SQLException e) {
			cnt = -1;
			logger.error("getUserCount:" + cec + ":" + e.getMessage());
		}

		db.closeConnection();
		return cnt;
	}

	public static int getUserId(String cec) {
		cec = cec.trim();

		DbAccess db = new DbAccess();
		db.openConnection();

		int id = -1;
		try {
			ResultSet rs = db
					.executeQuery("select id from users where active=1 and name2='"
							+ cec + "'");
			rs.next();
			id = rs.getInt("id");
		} catch (SQLException e) {
			logger.info("getUserId:" + cec + ":" + e.getMessage());
		}

		db.closeConnection();
		return id;
	}

	public static String getFullName(String cec) {
		cec = cec.substring(0, cec.indexOf("@"));
		String fmt = "http://wwwin-tools.cisco.com/dir/vcard/%s.vcf";
		String vcard = String.format(fmt, cec);

		HttpEngine eng = new HttpEngine();
		String content = null;
		try {
			content = eng.get(vcard).getHtml();
		} catch (Exception e) {
			logger.info("getFullName:" + cec + ":" + e.getMessage());
			return null;
		}
		Pattern ptn = Pattern.compile("fn[ ]*:[ ]*([a-zA-Z0-9 ]+)",
				Pattern.CASE_INSENSITIVE);
		Matcher m = ptn.matcher(content);

		if (m.find())
			return m.group(1);
		else
			return null;
	}

	public static int getOrgId(String orgName) {
		orgName = orgName.trim();

		DbAccess db = new DbAccess();
		db.openConnection();

		int id = -1;
		try {
			ResultSet rs = db.executeQuery("select id from orgs where name='"
					+ orgName + "' and id in " + ids);
			rs.next();
			id = rs.getInt("id");
		} catch (SQLException e) {
			logger.info("getOrgId:" + orgName + ":" + e.getMessage());
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
							+ orgName + "' and id in " + ids);
			rs.next();
			cnt = rs.getInt("cnt");
		} catch (SQLException e) {
			cnt = -1;
		}

		db.closeConnection();
		return cnt;
	}

	public static void addUser(String cec, int orgID, String fullName) {
		DbAccess db = new DbAccess();
		db.openConnection();

		try {
			db.executeUpdate("insert into users (name2,org_id,full_name) values ('"
					+ cec + "'," + orgID + ",'" + fullName + "')");
		} catch (SQLException e) {
			logger.info("add user error:" + cec + "," + orgID + ","
					+ e.getMessage());
		}

		db.closeConnection();
	}

	public static void updateUserOrg(int userID, int orgID) {
		DbAccess db = new DbAccess();
		db.openConnection();

		try {
			db.executeUpdate("update users set org_id=" + orgID + " where id="
					+ userID);
		} catch (SQLException e) {
			logger.info("update user org error:" + userID + "," + orgID + ","
					+ e.getMessage());
		}

		db.closeConnection();
	}
}
