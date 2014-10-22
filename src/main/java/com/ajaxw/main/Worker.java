package com.ajaxw.main;


import java.util.List;

import org.apache.log4j.Logger;

import com.ajaxw.net.HttpEngine;
import com.ajaxw.net.HttpResponse;

public class Worker implements Runnable {
	public static final String id = "worker";
	public static int num = 0;
	private String url;
	private static Logger errLogger = Logger.getLogger("err");
	private static Logger rstLogger = Logger.getLogger("rst");

	public Worker(String url) {
		this.url = url;
	}

	public void run() {
		HttpEngine eng = new HttpEngine();
		List<String> ct = null;
		try {
			HttpResponse resp = eng.get(url);
			if (resp.getResponseCode() != 200) {
				errLogger.debug(this.url + " - not 200");
				return;
			}
			ct = resp.getHeader("Content-Type");
		} catch (Exception e1) {
			errLogger.debug(this.url + " - " + e1.toString());
			return;
		}

		if (ct == null || ct.toString().indexOf("html") == -1) {
			errLogger.debug(this.url + " - " + ct);
			return;
		}

		synchronized (id) {
			num++;
		}

		rstLogger.debug(this.url);
	}

}
