package com.ajaxw.irsystem;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

import com.ajaxw.ds.DocUrls;
import com.ajaxw.ds.Index1;
import com.ajaxw.ds.Range;

public class FindInvtLst {

	/**
	 * @param args
	 */

	private static String finvt_idx = "H:\\tmp\\bbb\\invt_idx.dat";
	private static String furls = "H:\\tmp\\bbb\\urls.objs";
	private static String fidx1 = "H:\\tmp\\bbb\\idx1.objs";

	public static void main(String[] args) throws IOException,
			ClassNotFoundException {
		Index1 idx1 = loadIdx1(fidx1);
		DocUrls urls = loadUrls(furls);

		Range rng = idx1.getRange("");
		List<Integer> lst = readInvtLst(rng);

		System.out.println("--------");
		int m = -1;
		for (Integer integer : lst) {
			if (integer <= m)
				System.out.println("err");
			m = integer;

			if ("http://scst.suda.edu.cn/Detail.aspx?id=753"
					.equalsIgnoreCase(urls.getUrls().get(integer)))
				System.out.println("found");
		}
	}

	public static List<Integer> readInvtLst(Range rng) throws IOException {
		int off = rng.getOff();
		int len = rng.getLen();

		List<Integer> lst = new ArrayList<Integer>();
		DataInputStream dis = new DataInputStream(new BufferedInputStream(
				new FileInputStream(finvt_idx)));

		dis.skipBytes(off * Integer.SIZE / Byte.SIZE);
		for (int i = 0; i < len; i++) {
			lst.add(dis.readInt());
		}
		dis.close();

		return lst;
	}

	public static Index1 loadIdx1(String file) throws IOException,
			ClassNotFoundException {
		ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(
				new FileInputStream(file)));

		Index1 idx1 = (Index1) ois.readObject();
		ois.close();
		return idx1;
	}

	public static DocUrls loadUrls(String file) throws IOException,
			ClassNotFoundException {
		ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(
				new FileInputStream(file)));

		DocUrls urls = (DocUrls) ois.readObject();
		ois.close();
		return urls;
	}
}
