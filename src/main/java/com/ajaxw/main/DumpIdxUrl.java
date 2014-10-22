package com.ajaxw.main;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.ajaxw.ds.DocUrls;
import com.ajaxw.ds.Index1;
import com.ajaxw.ds.Range;
import com.ajaxw.util.Helper;

public class DumpIdxUrl {

	/**
	 * @param args
	 * @throws ClassNotFoundException
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public static void main(String[] args) throws FileNotFoundException,
			IOException, ClassNotFoundException {
		String fn = "conf.properties";
		Properties props = Helper.loadProperties(fn);

		String dir = props.getProperty("dir");
		String fileName = props.getProperty("mid_file");

		InvertedIndex invtIdx = readInvtIdx(dir + "\\" + fileName);

		Map<String, List<Integer>> invt_idx = invtIdx.getInvtlst();
		Map<Integer, String> urls = invtIdx.getUrls();

		Index1 idx1 = new Index1();
		DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(
				new FileOutputStream(dir + "\\invt_idx.dat")));

		int off = 0;
		Iterator<String> keyIt = invt_idx.keySet().iterator();
		while (keyIt.hasNext()) {
			String key = keyIt.next();
			List<Integer> lst = invt_idx.get(key);

			int len = lst.size();
			for (int docId : lst) {
				dos.writeInt(docId);
			}

			idx1.add(key, new Range(off, len));
			off += len;
		}

		System.out.println("off: " + off);

		dos.close();

		ObjectOutputStream oos = new ObjectOutputStream(
				new BufferedOutputStream(new FileOutputStream(dir
						+ "\\idx1.objs")));
		oos.writeObject(idx1);
		oos.close();

		int len = urls.size();
		DocUrls us = new DocUrls();

		for (int i = 0; i < len; i++) {
			String url = urls.get(i);
			if (url == null) {
				System.out
						.println("-------------------------------------------");
			} else {
				us.add(url);
			}
		}

		ObjectOutputStream uboj = new ObjectOutputStream(
				new BufferedOutputStream(new FileOutputStream(dir
						+ "\\urls.objs")));
		uboj.writeObject(us);
		uboj.close();

		System.out.println("*** end ***");
	}

	public static com.ajaxw.main.InvertedIndex readInvtIdx(String file)
			throws FileNotFoundException, IOException, ClassNotFoundException {
		ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(
				new FileInputStream(file)));

		com.ajaxw.main.InvertedIndex invtIdx = (com.ajaxw.main.InvertedIndex) ois
				.readObject();
		ois.close();
		return invtIdx;
	}
}
