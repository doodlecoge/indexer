package com.ajaxw.test;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.util.Version;

import com.ajaxw.constant.CONST;
import com.ajaxw.ds.DocUrls;
import com.ajaxw.ds.Index1;
import com.ajaxw.ds.Range;
import com.ajaxw.util.Helper;

public class QuerySystem {
	private String finvt_idx;
	private Index1 idx1;
	private DocUrls urls;

	public QuerySystem() throws IOException, ClassNotFoundException {
		String dir = IRSysConf.getProperty("ir_dir");

		String fidx1 = dir + CONST.PATH_SEP + IRSysConf.getProperty("idx1");
		finvt_idx = dir + CONST.PATH_SEP + IRSysConf.getProperty("invt_idx");
		String furls = dir + CONST.PATH_SEP + IRSysConf.getProperty("urls");

		idx1 = loadIdx1(fidx1);
		urls = loadUrls(furls);
	}

	public Set<String> ir(String query) throws IOException {
		String queryString = query;
		Set<String> keys = getKeys(queryString);

		List<Range> items = new ArrayList<Range>();
		for (String key : keys) {
			Range rng = idx1.getRange(key);
			if (rng != null)
				items.add(rng);
		}

		if (items.size() == 0) {
			System.out.println("find 0 resluts!");
			return null;
		}

		List<Integer> rst = query(items);
		System.out.println("find " + rst.size() + " results:");
		Set<String> set = new HashSet<String>();
		for (Integer docId : rst) {
			set.add(urls.getUrls().get(docId));
		}

		return set;
	}

	public List<Integer> query(List<Range> ranges) throws IOException {
		if (ranges.size() == 0)
			return null;

		Collections.sort(ranges, new Comparator<Range>() {
			public int compare(Range o1, Range o2) {
				// return o1.getOff() - o2.getOff();
				return o1.getLen() - o2.getLen();
			}
		});

		Range rng0 = ranges.get(0);
		List<Integer> shortest = readInvtLst(rng0);
		if (ranges.size() == 1)
			return shortest;

		for (int i = 1; i < ranges.size(); i++) {
			List<Integer> lst = readInvtLst(ranges.get(i));
			shortest = intersection(shortest, lst);
		}
		return shortest;
	}

	public void dumpIds(List<Integer> lst, BufferedWriter bw)
			throws IOException {
		for (Integer integer : lst) {
			bw.write(integer.toString() + ",");
		}
		bw.write("\n");
	}

	public List<Integer> intersection(List<Integer> lst1, List<Integer> lst2)
			throws IOException {
		// use merge to find intersection

		int len1 = lst1.size();
		int len2 = lst2.size();

		int i = 0;
		int j = 0;

		List<Integer> lst = new ArrayList<Integer>();

		for (; i < len1 && j < len2;) {
			int a = lst1.get(i);
			int b = lst2.get(j);

			if (a == b) {
				lst.add(lst1.get(i));
				i++;
				j++;
			} else if (a < b)
				i++;
			else
				j++;
		}

		return lst;
	}

	public List<Integer> readInvtLst(Range rng) throws IOException {
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

	public DocUrls loadUrls(String file) throws IOException,
			ClassNotFoundException {
		ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(
				new FileInputStream(file)));

		DocUrls urls = (DocUrls) ois.readObject();
		ois.close();
		return urls;
	}

	public Index1 loadIdx1(String file) throws IOException,
			ClassNotFoundException {
		ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(
				new FileInputStream(file)));

		Index1 idx1 = (Index1) ois.readObject();
		ois.close();
		return idx1;
	}

	public Set<String> getKeys(String str) throws IOException {
		Set<String> keys = new HashSet<String>();

		SmartChineseAnalyzer analyzer = new SmartChineseAnalyzer(
				Version.LUCENE_31);
		StringReader reader;
		TokenStream ts;
		CharTermAttribute cta;

		reader = new StringReader(str);
		ts = analyzer.tokenStream("content", reader);
		cta = ts.getAttribute(CharTermAttribute.class);
		while (ts.incrementToken()) {
			keys.add(cta.toString());
		}

		return keys;
	}

}
