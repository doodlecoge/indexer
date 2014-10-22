package com.ajaxw.main;

import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.ajaxw.net.HttpEngine;
import com.ajaxw.net.HttpResponse;
import com.ajaxw.util.Helper;

public class CrawlerWorker implements Runnable {
	private String url;
	private static Logger errLogger = Logger.getLogger("err");
	private static ConcurrentHtmlWriter out;

	static {
		out = new ConcurrentHtmlWriter(System.getProperty("page_dir"));
	}

	public CrawlerWorker(String url) {
		this.url = url;
	}

	public void run() {
		HttpEngine eng = new HttpEngine();
		try {
			HttpResponse resp = eng.get(url);
			String str = resp.getHtml();
			out.write(url, str);
		} catch (Exception e) {
			errLogger.debug(this.url + " - " + e.toString());
		}
	}

}
