package com.ajaxw.ciscostuff;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.ajaxw.io.IterableFile;

public class BuildOrgTree {

	public static Map<String, Org> tree = new HashMap<String, Org>();
	public static List<String> lines = new ArrayList<String>();

	public static String treePath = "HeFei";

	public static void main(String[] args) {
		init();

		testParentOrgExists();
		buildOrgTree();

		// Iterator<String> it = tree.keySet().iterator();
		// while(it.hasNext()) {
		// String key = it.next();
		// System.out.println(tree.get(key).getOrgName());
		// }
		List<Org> roots = getRoots();
		//
		for (Org org : roots) {
			printTree(org);

			System.out.println("******************************");
		}

		System.out.println("*** end ***");
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

	public static String getParentOrgName(String userName) {
		for (String line : lines) {
			String[] cols = line.split(",");

			if (cols[2].trim().equals(userName))
				return cols[0];
		}

		return null;
	}

	public static boolean managerExists(String userName) {
		for (String line : lines) {
			String[] cols = line.split(",");

			if (cols[2].trim().equals(userName)) {
				if (!cols[1].isEmpty())
					return true;
			}
		}

		return false;
	}

	public static void testParentOrgExists() {
		for (String line : lines) {
			String[] cols = line.split(",");

			String orgName = cols[0].trim();
			String pOwner = cols[1].trim();
			String owner = cols[2].trim();

			if (pOwner.isEmpty())
				System.out.println(line);

			// if (!managerExists(pOwner))
			// System.out.println(line);
		}
	}

	public static void buildOrgTree() {
		for (String line : lines) {
			String[] cols = line.split(",");

			String orgName = cols[0].trim();
			String pOwner = cols[1].trim();
			String owner = cols[2].trim();

			if (tree.containsKey(orgName))
				continue;

			Org org = new Org(orgName);
			tree.put(orgName, org);

			if (pOwner.isEmpty()) {
				// System.out.println(orgName);
				continue;
			}

			String pOrgName = getParentOrgName(pOwner);

			if (!tree.containsKey(pOrgName)) {
				System.out.println("parent not exist: " + pOwner + ": "
						+ pOrgName);
				continue;
			}

			Org pOrg = tree.get(pOrgName);

			org.setParent(pOrg);
			pOrg.getChildren().put(owner, org);
		}
	}

	public static List<Org> getRoots() {
		List<Org> roots = new ArrayList<Org>();

		Iterator<String> it = tree.keySet().iterator();

		while (it.hasNext()) {
			String key = it.next();
			Org org = tree.get(key);
			if (org.getParent() == null)
				roots.add(org);
		}

		return roots;
	}

	public static void printTree(Org subRoot) {
		Org tOrg = subRoot;
		String path = subRoot.getOrgName();

		// testOrgInDB(path);

		while (tOrg.getParent() != null) {
			tOrg = tOrg.getParent();
			path = tOrg.getOrgName() + ">" + path;
		}

		if (subRoot.getChildren().size() == 0)
			System.out.println(path);

		Iterator<String> it = subRoot.getChildren().keySet().iterator();

		while (it.hasNext()) {
			String key = it.next();
			printTree(subRoot.getChildren().get(key));
		}
	}

	public static void testOrgInDB(String orgName) {
		orgName = orgName.trim();
		orgName += " new";

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
			System.out.println("error: " + orgName);
		}

		if (cnt == 0) {
			System.out.println(orgName);
		}

		db.closeConnection();
	}
}
