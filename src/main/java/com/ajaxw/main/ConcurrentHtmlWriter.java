package com.ajaxw.main;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

//import org.apache.log4j.Logger;

public class ConcurrentHtmlWriter {
//	private static Logger consoleLogger = Logger.getRootLogger();

	private DataOutputStream out;
	private String dir;
	private int files;
	private int pages;
	private final int maxpages = 5000;

	{
		files = 0;
		pages = 0;
	}

	public ConcurrentHtmlWriter(String dir) {
		this.dir = dir;
	}

	public synchronized void write(String url, String str) throws IOException {

		if (out == null) {
			files++;
			out = new DataOutputStream(new BufferedOutputStream(
					new FileOutputStream(dir + "\\" + files + ".dat")));

			System.out.println(dir);
		}

		pages++;

		byte[] bts = str.getBytes();
		final int len = bts.length;
		out.writeInt(len);
		out.writeInt(url.getBytes().length);
		out.write(url.getBytes());

		int i = 0;
		final int blockSize = 1024;
		final int end = len - len % blockSize;
		for (i = 0; i < end; i += 1024) {
			out.write(bts, i, 1024);
		}

		if (end != len) {
			out.write(bts, i, len - end);
		}

		if (pages >= maxpages) {
			pages = 0;
			out.flush();
			out.close();
			out = null;
		}
	}
}
