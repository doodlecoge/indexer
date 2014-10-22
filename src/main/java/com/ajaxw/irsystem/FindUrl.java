package com.ajaxw.irsystem;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.List;

import com.ajaxw.ds.DocUrls;

public class FindUrl {

	/**
	 * @param args
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException,
			ClassNotFoundException {
		String urlfile = "H:\\tmp\\bbb\\urls.objs";
		DocUrls urls = loadUrls(urlfile);

		List<String> uuu = urls.getUrls();

		for (String u : uuu) {
			if (u.equalsIgnoreCase("http://scst.suda.edu.cn/Detail.aspx?id=753")) {
				System.out.println("equal");
			}
		}
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
