package com.ajaxw.test;

import java.io.IOException;
import java.util.Properties;

import com.ajaxw.util.Helper;

public class IRSysConf {
	private static Properties props;
	static {
		String fn = "irsys.conf";
		try {
			props = Helper.loadProperties(fn);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String getProperty(String key) {
		return props.getProperty(key);
	}
}
