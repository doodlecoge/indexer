package com.ajaxw.main;

import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.util.Version;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.ajaxw.ds.ForwardIndex;
import com.ajaxw.util.Helper;

public class Indexer {
	private static int docId = 0;

	/**
	 * @param args
	 * @throws IOException
	 */
	public static Logger logger = Logger.getRootLogger();

	public static void main(String[] args) throws IOException {
		String fn = "conf.properties";
		Properties props = Helper.loadProperties(fn);

		String dir = props.getProperty("dir");
		String startDocId = props.getProperty("startDocId");
		String startFile = props.getProperty("startFile");

		docId = Integer.parseInt(startDocId);

		File[] files = new File(dir).listFiles();
		List<String> fileNames = new ArrayList<String>();
		fileNames.clear();

		Pattern ptn = Pattern.compile("([0-9]+)\\.dat$");

		for (File file : files) {
			String fileName = file.getName();
			Matcher m = ptn.matcher(fileName);

			if (!m.find())
				continue;

			fileNames.add(fileName);
		}

		Collections.sort(fileNames);

		System.out.println(">>> " + startFile);
		for (String string : fileNames) {
			System.out.println("  > " + string);
		}

		for (String fileName : fileNames) {
			if (startFile.compareTo(fileName) == 1)
				continue;

			logger.info("docId: " + docId + ", fileName: " + fileName);
			constructForwardIndex(new File(dir + "\\" + fileName));
		}
	}

	public static void constructForwardIndex(File file) throws IOException {
		System.out.println(file.getName());

		DataInputStream dis = new DataInputStream((new FileInputStream(file)));
		ObjectOutputStream oos = new ObjectOutputStream(
				new BufferedOutputStream(new FileOutputStream(file
						.getAbsoluteFile().toString() + ".objs")));

		while (true) {
			int ctlen = 0;
			int urllen = 0;

			try {
				ctlen = dis.readInt();
				urllen = dis.readInt();
			} catch (EOFException e) {
				break;
			}

			byte[] urls = new byte[urllen];
			dis.read(urls);
			String url = new String(urls);

			byte[] bts = new byte[ctlen];
			dis.read(bts);
			Set<String> keys = getAllKeys(new String(bts));

			if (keys.size() < 1)
				continue;

			ForwardIndex fidx = new ForwardIndex(docId++, url);
			fidx.add(keys);
			oos.writeObject(fidx);
		}

		dis.close();
		oos.close();
	}

	public static Set<String> getAllKeys(String html) throws IOException {
		Set<String> keys = new HashSet<String>();

		Document doc = Jsoup.parse(html);
		Elements titles = doc.getElementsByTag("title");
		for (Element title : titles) {
			Set<String> kk = getKeys(title.text());
			keys.addAll(kk);
		}

		Elements metas = doc.getElementsByTag("meta");
		for (Element meta : metas) {
			if (!(meta.hasAttr("http-equiv") && meta.hasAttr("content")))
				continue;

			String val = meta.attr("http-equiv");

			if (val.equalsIgnoreCase("Keywords")
					|| val.equalsIgnoreCase("Description"))
				keys.addAll(getAllKeys(meta.attr("content")));
		}

		Element body = doc.body();
		if (body == null)
			body = doc;

		removeSubTag(body, "script");
		removeSubTag(body, "style");

		Elements els = body.getAllElements();
		for (Element el : els) {
			keys.addAll(getKeys(el.text()));
		}

		return keys;
	}

	public static Set<String> getKeys(String str) throws IOException {
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

	public static void removeSubTag(Element el, String subTagName) {
		Elements els = el.getElementsByTag(subTagName);
		for (Element element : els) {
			element.remove();
		}
	}
}
