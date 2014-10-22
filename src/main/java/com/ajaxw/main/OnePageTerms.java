package com.ajaxw.main;

import java.io.IOException;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.HashSet;
import java.util.Set;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.util.Version;

import com.ajaxw.net.HttpEngine;
import com.ajaxw.net.HttpResponse;

public class OnePageTerms {

	/**
	 * @param args
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 * @throws MalformedURLException
	 * @throws KeyManagementException
	 */
	public static void main(String[] args) throws KeyManagementException,
			MalformedURLException, NoSuchAlgorithmException, IOException {
		// TODO Auto-generated method stub
		HttpEngine eng = new HttpEngine();
		HttpResponse resp = eng.get("http://scst.suda.edu.cn/Detail.aspx?id=753");

		String str = resp.getHtml();
		Set<String> keys = getKeys(str);

		for (String string : keys) {
			System.out.println(string);
		}
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

}
