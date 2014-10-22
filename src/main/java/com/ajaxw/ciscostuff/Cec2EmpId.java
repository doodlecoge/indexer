package com.ajaxw.ciscostuff;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.bcel.generic.ARRAYLENGTH;
import org.apache.bcel.generic.LSTORE;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.ajaxw.io.IterableFile;
import com.ajaxw.net.HttpEngine;
import com.ajaxw.thread.ThreadPool;

public class Cec2EmpId {
	static ConcurrentMap<String, Integer> cmap = new ConcurrentHashMap<String, Integer>();

	class Worker implements Runnable {
		String cec;

		public Worker(String cec) {
			this.cec = cec;
		}

		@Override
		public void run() {
			int eid = -1;
			try {
				eid = getEmployeeId(cec);
			} catch (Exception e) {
				// System.out.println("get eid error: " + cec + ": "
				// + e.getMessage());
			}
			cmap.put(cec, eid);
		}

	}

	/**
	 * @param args
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 * @throws MalformedURLException
	 * @throws KeyManagementException
	 */
	public static void main(String[] args) {
		// List<String> lst = getAllCec();
		// Map<String, Integer> map = getAllEmployeeIds(lst);
		//
		// BufferedWriter bw = new BufferedWriter(new FileWriter(
		// "E:\\tmp\\cec_eid.txt"));
		// Iterator<String> itkey = map.keySet().iterator();
		// while (itkey.hasNext()) {
		// String key = itkey.next();
		// int val = map.get(key);
		// bw.write(key + "," + val + "\n");
		// }

		// for (String line : new IterableFile("E:\\tmp\\cec_eid.txt")) {
		// String[] cols = line.split(",");
		// int eid = Integer.parseInt(cols[1]);
		// String cec = cols[0];
		//
		// if (eid != -1)
		// addCecEidMap(cec, eid);
		// }

		Cec2EmpId ins = new Cec2EmpId();
		ThreadPool pool = new ThreadPool(10);
		// ConcurrentMap<String, Integer> cmap = new ConcurrentHashMap<String,
		// Integer>();

		for (String line : new IterableFile("E:\\tmp\\cec_eid.txt")) {
			try {
				pool.addTask(ins.new Worker(line));
				System.out.println(".");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println("...");
		pool.joinAll();
		System.out.println("..2.");
		Iterator<String> it = cmap.keySet().iterator();
		while (it.hasNext()) {
			String key = it.next();
			System.out.println(key + ":" + cmap.get(key));
		}
		System.out.println("..3.");
	}

	public static List<String> getAllCec() {
		DbAccess db = new DbAccess();
		db.openConnection();
		List<String> lst = new ArrayList<String>();
		try {
			ResultSet rs = db
					.executeQuery("select * from users where name2 is not null and name2<>''");
			while (rs.next()) {
				lst.add(rs.getString("name2"));
			}
		} catch (SQLException e) {
			System.out.println(e);
		}
		db.closeConnection();
		return lst;
	}

	public static Map<String, Integer> getAllEmployeeIds(List<String> cecs)
			throws KeyManagementException, MalformedURLException,
			NoSuchAlgorithmException, IOException {

		Map<String, Integer> map = new HashMap<String, Integer>();

		for (String cec : cecs) {
			String cecid = cec.substring(0, cec.indexOf("@"));
			int eid = -1;
			try {
				eid = getEmployeeId(cecid);
			} catch (Exception e) {
			}
			map.put(cec, eid);
		}

		return map;
	}

	// public static int getEmployeeId(String cec) throws
	// KeyManagementException,
	// MalformedURLException, NoSuchAlgorithmException, IOException {
	// cec = cec.substring(0, cec.indexOf("@"));
	//
	// System.out.println(">>> " + cec);
	// HttpEngine eng = new HttpEngine();
	// String str = eng.get("http://wwwin-tools.cisco.com/dir/details/" + cec)
	// .getHtml();
	//
	// Document doc = Jsoup.parse(str);
	//
	// Elements els = doc
	// .select("table.orgtable > tbody > tr:eq(2) > td:eq(1)");
	// System.out.println("<<< " + cec);
	// return Integer.parseInt(els.get(1).text().trim());
	// }

	public static int getEmployeeId(String cec) throws KeyManagementException,
			MalformedURLException, NoSuchAlgorithmException, IOException {
		cec = cec.substring(0, cec.indexOf("@"));

		System.out.println(">>> " + cec);
		HttpEngine eng = new HttpEngine();
		String str = eng.get("http://wwwin-tools.cisco.com/dir/details/" + cec)
				.getHtml();

		Document doc = Jsoup.parse(str);

		Elements els = doc
				.select("table.orgtable > tbody > tr:matches(Employee ID) > td:eq(1)");
		System.out.println("<<< " + cec);
		return Integer.parseInt(els.get(0).text().trim());
	}

	public static void addCecEidMap(String cec, int eid) {
		DbAccess db = new DbAccess();
		db.openConnection();

		try {
			db.executeUpdate("insert into user_profiles (cec, eid) values ('"
					+ cec + "', " + eid + ")");
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println(cec + ":" + eid);
		}

		db.closeConnection();
	}
}
