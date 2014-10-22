package com.ajaxw.test;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import javax.imageio.ImageIO;

import com.ajaxw.ds.Parameter;
import com.ajaxw.ds.Parameters;
import com.ajaxw.net.HttpEngine;
import com.ajaxw.net.HttpResponse;
import com.ajaxw.util.Helper;

public class Toupiao {

	static CheckCodeDet det = null;

	public static void main(String[] args) throws KeyManagementException,
			MalformedURLException, NoSuchAlgorithmException, IOException {
		String id = "46";
		String cnt = "1000";

		if (args.length == 2) {
			try {
				Integer.parseInt(args[0]);
				Integer.parseInt(args[1]);
				id = args[0];
				cnt = args[1];
			} catch (Exception e) {
			}
		}
		// 45-62
		for (int j = 0; j < 10; j++) {
			for (int i = 46; i < 63; i++) {
				toupiao("" + i, cnt);
			}
		}

	}

	public static void toupiao(String id, String scnt) throws IOException,
			KeyManagementException, NoSuchAlgorithmException {
		HttpEngine eng = new HttpEngine();
		HttpResponse resp = null;
		det = new CheckCodeDet();

		// TODO Auto-generated method stub
		// check=45&check=46&check=47&check=48&check=49&CheckCode=6043&sub=%CE%D2%D2%AA%CD%B6%C6%B1
		resp = eng.get("http://zhuanti.suda.edu.cn/54qingnian2010/index.htm");
		resp = eng
				.get("http://zhuanti.suda.edu.cn/54qingnian2010/plus/vote.asp");

		// int cnt = 50;
		// String id = "50"; // 46,50
		int cnt = Integer.parseInt(scnt);
		Parameters params = new Parameters();
		for (int i = 0; i < cnt; i++) {
			params.add("check", id);
		}

		params.add("sub", "%CE%D2%D2%AA%CD%B6%C6%B1", false);
		params.add("CheckCode", getCheckCode(eng));

		resp = eng.post(
				"http://zhuanti.suda.edu.cn/54qingnian2010/plus/vosave.asp",
				params);
		System.out.println(resp.getHtml());
		eng.clearCookie();
	}

	public static String getCheckCode(HttpEngine eng) throws IOException,
			KeyManagementException, NoSuchAlgorithmException {
		HttpResponse resp = eng
				.get("http://zhuanti.suda.edu.cn/54qingnian2010/Inc/checkcode.asp");
		ByteArrayOutputStream baos = resp.getContent();
		ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
		BufferedImage bi = ImageIO.read(bais);

		String code = det.detect(bi);
		Helper.saveAs(baos, "e:\\" + code + ".jpg");
		return code;
	}
}
