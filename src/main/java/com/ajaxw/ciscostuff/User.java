package com.ajaxw.ciscostuff;

import java.io.IOException;
import java.net.MalformedURLException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.apache.xerces.impl.xpath.regex.Match;

import com.ajaxw.net.HttpEngine;

public class User {
	private Logger logger = Logger.getLogger("fileLogger");

	private String fullName;
	private String cecMail;
	private String wbxMail;
	private int active;
	private String orgName;

	private String line;
	private DbAccess db = new DbAccess();

	public String toString() {
		return this.line;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		if (fullName == null) {
			this.fullName = null;
			return;
		}

		String s = fullName.trim();

		if (s.isEmpty() || s.equalsIgnoreCase(""))
			this.fullName = null;
		else
			this.fullName = s;
	}

	public String getCecMail() {
		return cecMail;
	}

	public void setCecMail(String cecMail) {
		if (cecMail == null) {
			this.cecMail = null;
			return;
		}

		String s = cecMail.trim();

		if (s.isEmpty() || s.equalsIgnoreCase(""))
			this.cecMail = null;
		else {

			if (s.indexOf(" ") != -1
					&& !(s.indexOf("@") == -1 || s.endsWith("@cisco.com")))
				throw new IllegalArgumentException("cec mail error: " + s);

			if (s.indexOf("@") == -1)
				s += "@cisco.com";

			this.cecMail = s;
		}

	}

	public String getWbxMail() {
		return wbxMail;
	}

	public void setWbxMail(String wbxMail) {
		if (wbxMail == null) {
			this.wbxMail = null;
			return;
		}

		String s = wbxMail.trim();

		if (s.isEmpty() || s.equalsIgnoreCase(""))
			this.wbxMail = null;
		else {
			this.wbxMail = s;

			Pattern ptn = Pattern.compile(
					"^[a-z][0-9a-z.]*@((hf|hz|sz)\\.)?webex\\.com$",
					Pattern.CASE_INSENSITIVE);
			Matcher m = ptn.matcher(s);
			if (!m.find())
				throw new IllegalArgumentException("wbx mail error: " + s);
		}
	}

	public int getActive() {
		return active;
	}

	public void setActive(int active) {
		this.active = active;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		if (orgName == null) {
			this.orgName = null;
			return;
		}

		String s = orgName.trim();

		if (s.isEmpty() || s.equalsIgnoreCase(""))
			this.orgName = null;
		else
			this.orgName = s;
	}

	public User(String line) {
		this.line = line;

		String[] cols = line.split(",");

		this.setFullName(cols[0]);
		this.setActive(Integer.parseInt(cols[1]));
		this.setCecMail(cols[2]);
		this.setWbxMail(cols[3]);

		if (cols.length == 5)
			this.setOrgName(cols[4]);
		else
			this.setOrgName(null);
	}

	// public int cecCnt() {
	// if (this.getCecMail() == null)
	// return 0;
	//
	// try {
	// ResultSet rs = db.executeQuery(String.format(
	// "select count(id) cnt from users where name2='%s'",
	// this.getCecMail()));
	// rs.next();
	// return rs.getInt("cnt");
	// } catch (SQLException e) {
	// logger.info("cecCnt: " + this.getCecMail() + ": " + e.getMessage());
	// return -1;
	// }
	// }
	//
	// public int getIdByCec() {
	//
	// if (this.getCecMail() == null)
	// return -1;
	// try {
	// ResultSet rs = db
	// .executeQuery(String.format(
	// "select id from users where name2='%s'",
	// this.getCecMail()));
	// rs.next();
	// return rs.getInt("id");
	// } catch (SQLException e) {
	// logger.info("getIdByCec: " + this.getCecMail() + ": "
	// + e.getMessage());
	// return -1;
	// }
	// }

	public int getCntByCecOrWbx() {
		String where = " where active=1 and (1=0 ";

		if (this.getWbxMail() != null)
			where += " or name='" + this.getWbxMail() + "' ";
		if (this.getCecMail() != null)
			where += " or name2='" + this.getCecMail() + "' ";

		where += ")";

		try {
			db.openConnection();
			ResultSet rs = db.executeQuery("select count(id) cnt from users "
					+ where);

			rs.next();
			int cnt = rs.getInt("cnt");
			db.closeConnection();

			return cnt;
		} catch (SQLException e) {
			logger.info("getIdByCec: " + this.getCecMail() + ": "
					+ e.getMessage());
			return -1;
		}
	}

	public int getIdByCecOrWbx() {
		String where = " where active=1 and (1=0 ";

		if (this.getWbxMail() != null)
			where += " or name='" + this.getWbxMail() + "' ";
		if (this.getCecMail() != null)
			where += " or name2='" + this.getCecMail() + "' ";

		where += ")";

		try {
			db.openConnection();
			ResultSet rs = db.executeQuery("select id from users " + where);

			rs.next();
			int id = rs.getInt("id");
			db.closeConnection();

			return id;
		} catch (SQLException e) {
			logger.info("getIdByCec: " + this.getCecMail() + ": "
					+ e.getMessage());
			return -1;
		}
	}

	public void testFullName() {
		int cnt = this.getCntByCecOrWbx();
		if (cnt != 1) {
			throw new IllegalArgumentException("testFullName: cnt != 1");
		}

		int id = this.getIdByCecOrWbx();

		try {
			db.openConnection();
			ResultSet rs = db
					.executeQuery("select full_name from users where id=" + id);

			rs.next();
			String fn = rs.getString("full_name");
			db.closeConnection();

			if (!fn.equalsIgnoreCase(this.getFullName()))
				System.out.println("full Name not match:" + fn + ":"
						+ this.getFullName());
		} catch (SQLException e) {
			logger.info("getIdByCec: " + this.getCecMail() + ": "
					+ e.getMessage());

		}
	}

	// public int wbxCnt() {
	// if (this.getWbxMail() == null)
	// return 0;
	//
	// try {
	// ResultSet rs = db.executeQuery(String.format(
	// "select count(id) cnt from users where name='%s'",
	// this.getWbxMail()));
	// rs.next();
	// return rs.getInt("cnt");
	// } catch (SQLException e) {
	// logger.info("wbxCnt: " + this.getWbxMail() + ": " + e.getMessage());
	// return -1;
	// }
	// }
	//
	// public int getIdByWbx() {
	// if (this.getWbxMail() == null)
	// return -1;
	//
	// try {
	// ResultSet rs = db.executeQuery(String.format(
	// "select id from users where name='%s'", this.getWbxMail()));
	// rs.next();
	// return rs.getInt("cnt");
	// } catch (SQLException e) {
	// logger.info("getIdByWbx: " + this.getWbxMail() + ": "
	// + e.getMessage());
	// return -1;
	// }
	// }

	public void deactive() {
		System.out.println("deactive------");
		int cnt = this.getCntByCecOrWbx();
		if (cnt != 1) {
			throw new IllegalArgumentException("testFullName: cnt != 1");
		}

		int id = this.getIdByCecOrWbx();

		try {
			db.openConnection();
			db.executeUpdate("update users set active=0 where id=" + id);
		} catch (SQLException e) {
			logger.info("deactive: " + this.getWbxMail() + ": "
					+ e.getMessage());
		} finally {
			db.closeConnection();
		}
	}

	public void add() throws KeyManagementException, MalformedURLException,
			NoSuchAlgorithmException, IOException {
		System.out.println("add     ------");
		String full_name = this.getVcardFullName();
		DbAccess db = new DbAccess();
		try {
			db.openConnection();
			db.executeUpdate("insert into users (full_name, name2, org_id) "
					+ "values ('" + full_name + "', '" + this.getCecMail()
					+ "', " + this.getOrgId() + ")");
		} catch (SQLException e) {
			logger.info("add: " + this.getCecMail() + ": " + e.getMessage());
		} finally {
			db.closeConnection();
		}
	}

	public void update() {
		System.out.println("update  ------");
		int orgId = this.getOrgId();
		int userId = this.getIdByCecOrWbx();
		try {
			db.openConnection();
			db.executeUpdate("update users set org_id=" + orgId + " where id="
					+ userId);
		} catch (SQLException e) {
			logger.info("update: " + orgId + ": " + e.getMessage());
		} finally {
			db.closeConnection();
		}
	}

	public int getOrgCnt() {
		try {
			db.openConnection();
			ResultSet rs = db
					.executeQuery("select count(id) cnt from orgs where name='"
							+ this.getOrgName() + "'");
			rs.next();
			return rs.getInt("cnt");
		} catch (SQLException e) {
			logger.info("getIdByWbx: " + this.getWbxMail() + ": "
					+ e.getMessage());
		} finally {
			db.closeConnection();
		}

		return -1;
	}

	public int getOrgId() {
		try {
			db.openConnection();
			ResultSet rs = db.executeQuery("select id from orgs where name='"
					+ this.getOrgName() + "'");
			if (rs.next()) {
				return rs.getInt("id");
			}
		} catch (SQLException e) {
			logger.info("getIdByWbx: " + this.getWbxMail() + ": "
					+ e.getMessage());
		} finally {
			db.closeConnection();
		}

		return -1;
	}

	public String getVcardFullName() throws KeyManagementException,
			MalformedURLException, NoSuchAlgorithmException, IOException {
		String fmt = "http://wwwin-tools.cisco.com/dir/vcard/%s.vcf";
		String cecid = this.getCecMail().substring(0,
				this.getCecMail().indexOf("@"));
		String url = String.format(fmt, cecid);

		HttpEngine eng = new HttpEngine();
		String content = eng.get(url).getHtml();
		Pattern ptn = Pattern.compile("fn[ ]*:[ ]*([a-zA-Z0-9 ]*)",
				Pattern.CASE_INSENSITIVE);
		Matcher m = ptn.matcher(content);

		String vcardName = null;
		if (m.find())
			vcardName = m.group(1);

		return vcardName;
	}

	public void testVcarName() throws KeyManagementException,
			MalformedURLException, NoSuchAlgorithmException, IOException {
		String vcardName = this.getVcardFullName();

		if (vcardName != null && vcardName.equalsIgnoreCase(this.getFullName())) {

		} else {
			System.out.println(vcardName);
			System.out.println("fullName not match vcard");
		}
	}
}
