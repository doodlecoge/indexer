package com.ajaxw.test;

import java.io.IOException;
import java.net.MalformedURLException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.codec.binary.Base64;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.ajaxw.ds.Parameter;
import com.ajaxw.net.HttpEngine;
import com.ajaxw.net.HttpResponse;
import com.ajaxw.util.Helper;

public class LoginGateway {
	public static String CharSeq = "0123456789abcdefghijklmnopqrstuvwxyz"
			.toLowerCase();

	/**
	 * @param args
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 * @throws KeyManagementException
	 */
	public static void main(String[] args) throws IOException,
			KeyManagementException, NoSuchAlgorithmException {
		if (args.length != 1) {
			System.out.println("please select type: login or logout");
			return;
		}

		String type = args[0];

		HttpEngine eng = new HttpEngine();
		Map<String, String> ps = getParams();

		ps.put("nw", "RadioButton1");
		ps.put("tm", "RadioButton6");

		if (type.equalsIgnoreCase("login")) {
			Parameter param = getAccount();
			ps.remove("Button2");
			ps.put("TextBox1", param.getKey());
			ps.put("TextBox2", param.getVal());
		} else {
			ps.remove("Button1");
			ps.put("TextBox1", "");
			ps.put("TextBox2", "");
		}

		List<Parameter> lst = new ArrayList<Parameter>();

		Iterator<String> keys = ps.keySet().iterator();
		while (keys.hasNext()) {
			String key = keys.next();
			System.out.println(key + ": " + ps.get(key));
			lst.add(new Parameter(key, ps.get(key)));
		}

		Parameter[] params = lst.toArray(new Parameter[lst.size()]);

		HttpResponse resp = eng
				.post("http://wg.suda.edu.cn/index.aspx", params);

		System.out.println(resp.getHtml());
	}

	public static Parameter getAccount() throws IOException {
		Properties props = Helper.loadProperties("cypher.properties");
		String acc = props.getProperty("acc");
		String encrypted = props.getProperty("encrypted");

		if (encrypted.equalsIgnoreCase("true")) {
			String times = acc.substring(acc.length() - 4);
			acc = acc.substring(0, acc.length() - 4);
			byte[] bytes = decodeNTimes(times.getBytes(), 1);
			int tm = Helper.nAry2Denary(new String(bytes), 3);
			bytes = decodeNTimes(acc.getBytes(), tm);
			acc = new String(bytes);
		}

		String[] ss = acc.split(",");
		String userName = ss[0];
		String pass = ss[1];

		return new Parameter(userName, pass);
	}

	public static Map<String, String> getParams()
			throws KeyManagementException, MalformedURLException,
			NoSuchAlgorithmException, IOException {

		HttpEngine eng = new HttpEngine();
		HttpResponse resp = eng.get("http://wg.suda.edu.cn/index.aspx");
		String html = resp.getHtml();
		Document doc = Jsoup.parse(html);

		Element form = doc.getElementById("form1");
		if (form == null)
			form = doc.getElementsByTag("form").first();

		Elements inputs = form.getElementsByTag("input");

		Map<String, String> params = new HashMap<String, String>();
		for (Element input : inputs) {
			if (!input.hasAttr("name"))
				continue;

			params.put(input.attr("name"),
					input.hasAttr("value") ? input.attr("value") : "");
		}

		return params;
	}

	public static byte[] encodeNtimes(byte[] bytes, int n) {
		for (int i = 0; i < n; i++) {
			bytes = Base64.encodeBase64(bytes);
		}

		return bytes;
	}

	public static byte[] decodeNTimes(byte[] bytes, int n) {
		for (int i = 0; i < n; i++) {
			bytes = Base64.decodeBase64(bytes);
		}
		return bytes;
	}
}
