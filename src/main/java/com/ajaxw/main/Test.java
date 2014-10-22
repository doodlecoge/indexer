package com.ajaxw.main;

import java.beans.XMLDecoder;
import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringEscapeUtils;
import org.openqa.selenium.internal.Base64Encoder;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.google.zxing.qrcode.encoder.QRCode;

public class Test {

	/**
	 * @param args
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 * @throws KeyManagementException
	 * @throws InterruptedException
	 * @throws ClassNotFoundException
	 */
	public static void main(String[] args) throws IOException,
			KeyManagementException, NoSuchAlgorithmException,
			InterruptedException, ClassNotFoundException {

		// Document doc = Jsoup.parse("<a></a>");
		// Elements els = doc.getElementsByTag("b");
		// System.out.println(els.size());
		// for (Element el : els) {
		// System.out.println(el.text());
		// }
		// SmartChineseAnalyzer analyzer = new SmartChineseAnalyzer(
		// Version.LUCENE_31);
		// StringReader reader;
		// TokenStream ts;
		// CharTermAttribute cta;
		//
		// reader = new
		// StringReader("苏州大学,苏州大学文正学院,苏大,苏州大学 - 文正学院,苏州,2008年招生,招生,全国著名独立学院,独立学院,教育部批准的,本三,民办本科,热门高校,苏南地区,就业,学院新闻中心,梦想从这里起飞");
		// ts = analyzer.tokenStream("content", reader);
		// cta = ts.getAttribute(CharTermAttribute.class);
		// while (ts.incrementToken()) {
		// System.out.println(cta.toString());
		// }

		// ObjectOutputStream oos = new ObjectOutputStream(
		// new BufferedOutputStream(new FileOutputStream("e:\\obj.oo")));
		//
		// ForwardIndex fwidx = new ForwardIndex(0);
		// fwidx.add("11");
		// fwidx.add("12");
		// fwidx.add("13");
		//
		// oos.writeObject(fwidx);
		// fwidx = new ForwardIndex(1);
		// fwidx.add("21");
		// fwidx.add("22");
		// fwidx.add("23");
		// oos.writeObject(fwidx);
		// oos.close();
		//
		// ObjectInputStream ois = new ObjectInputStream(new
		// BufferedInputStream(
		// new FileInputStream("e:\\obj.oo")));
		// fwidx = (ForwardIndex) ois.readObject();
		// System.out.println(fwidx.getKeys());
		//
		//
		// fwidx = (ForwardIndex) ois.readObject();
		// System.out.println(fwidx.getKeys());
		//
		//
		// ois.close();

		// DataInputStream dis = new DataInputStream(new BufferedInputStream(
		// new FileInputStream("e:\\data\\1.dat")));
		//
		// while (true) {
		// int num = 0;
		// try {
		// num = dis.readInt();
		// } catch (EOFException e) {
		// break;
		// }
		// byte[] bts = new byte[num];
		// dis.read(bts);
		// System.out.println(new String(bts));
		// // dis.skipBytes(num);
		// // System.out.println(num);
		//
		// // Document doc = Jsoup.parse(new String(bts));
		// // Element body = doc.body();
		// // Elements els = body.getElementsByTag("script");
		// // for (Element element : els) {
		// // element.remove();
		// // }
		// // els = body.getElementsByTag("style");
		// // for (Element element : els) {
		// // element.remove();
		// // }
		// // System.out.println(body.toString());
		// break;
		// }
		//
		// dis.close();

		// DataInputStream dis = new DataInputStream(new BufferedInputStream(
		// new FileInputStream("c:\\tmp\\1.dat")));
		//
		// int i = dis.readInt();
		// System.out.println(i);
		//
		// byte[] bts = new byte[i];
		// dis.read(bts, 0, i);
		//
		// System.out.println(bts[0]);
		// System.out.println(bts[1]);
		// System.out.println(bts[2]);
		// System.out.println(bts[3]);
		// System.out.println(new String(bts));
		// HttpEngine eng = new HttpEngine();
		//
		// String str = eng.get("http://wxy.suda.edu.cn/info.asp?ArticleID=330")
		// .getHeader("Content-Type").toString();

		// System.out.println(str);
		// String dir = "suda.urls";
		//
		// BufferedReader br = Helper.getBufferedFileReader(dir);
		// BufferedWriter bw = Helper.getBufferedFileWriter("suda.urls.new");
		//
		// HttpEngine eng = new HttpEngine();
		// String line = null;
		// while ((line = br.readLine()) != null) {
		// try {
		// List<String> ct = eng.get(line).getHeader("Content-Type");
		// if (ct == null || ct.toString().indexOf("html") == -1) {
		// System.out.println(ct);
		// continue;
		// }
		// } catch (Exception e) {
		// System.out.println(e);
		// continue;
		// }
		//
		// bw.write(line);
		// bw.write(CONST.LF);
		// }
		//
		// br.close();
		// bw.close();

		// HttpEngine eng = new HttpEngine();
		// Map<String, List<String>> hdrs = eng.get("http://www.google.com")
		// .getHeaders();
		//
		// Iterator<String> it = hdrs.keySet().iterator();
		//
		// while (it.hasNext()) {
		// String k = it.next();
		//
		// System.out.println(k + ": " + hdrs.get(k));
		// }

		// ThreadPool tp = new ThreadPool(10);
		// for (int i = 0; i < 1000; i++) {
		// tp.exec(new Runnable() {
		//
		// public void run() {
		// System.out.println('i');
		// }
		// });
		// }
		//
		// tp.stop();
		// System.out.println("end");

		// String imgPath = "e:\\qrcode3.png";
		// String str1 =
		// "eng,www,yx,xsc,cwc,jsyb,jyb,jwc,kczx,international,yjs,zxyxpt,hqglc,stpf,bwc,youth,xsst,zyz,zxbx,sdxyy,zjc,myauth";
		//
		// String str2 =
		// "bsh,yanhui,zkzs,cjy,oversea,ihanyu,xk1,hysgl,bkssw,xcb,skc,scit,rsc,fxcs,jgstpf,xb,gh,tjxh,fzb,sdkjy,sbc,zcjygs";
		//
		// String str3 =
		// "sdttc,lmscosu,dwzx,zsb,library1,libts,library,xk,cxzy,my,tzfw,file,card,toupiao2,toupiao1,zzb,jjs";
		// String str =
		// "eng,www,yx,xsc,cwc,jsyb,jyb,jwc,kczx,international,yjs,zxyxpt,hqglc,stpf,bwc,youth,xsst,zyz,zxbx,sdxyy,zjc,myauth,bsh,yanhui,zkzs,cjy,oversea,ihanyu,xk1,hysgl,bkssw,xcb,skc,scit,rsc,fxcs,jgstpf,xb,gh,tjxh,fzb,sdkjy,sbc,zcjygs,sdttc,lmscosu,dwzx,zsb,library1,libts,library,xk,cxzy,my,tzfw,file,card,toupiao2,toupiao1,zzb,jjs";
		//
		//
		// System.out.println(str.split(",").length);
		// String contents =
		// "sdttc,lmscosu,dwzx,zsb,library1,libts,library,xk,cxzy,my,tzfw,file,card,toupiao2,toupiao1,zzb,jjs";

		// int width = 300, height = 300;
		// Test handler = new Test();
		// handler.encode(str, width, height, "e:\\qrcode.png");
		// // handler.encode(str2, width, height, "e:\\qrcode2.png");
		// // handler.encode(str3, width, height, "e:\\qrcode3.png");
		//
		// System.out.println("Michael ,you have finished zxing encode.");
//		String sss = "&lt;thing&gt;&lt;namespaceID&gt;N2B93CGMUT80IDOV3I3J0UFXKT&lt;/namespaceID&gt;&lt;thingType&gt;resources&lt;/thingType&gt;&lt;thingName&gt;O0K9GTG88QLLDCN58F86HB7DD4&lt;/thingName&gt;&lt;ext&gt;&lt;WBX&gt;&lt;connect&gt;&lt;allocatedToObjectID&gt;O0K9GTG88QLLDCN58F86HB7DD4&lt;/allocatedToObjectID&gt;&lt;allocatedToObjectType&gt;org&lt;/allocatedToObjectType&gt;&lt;subscriptions&gt;&lt;subscription&gt;&lt;id&gt;123&lt;/id&gt;&lt;ticketID&gt;123&lt;/ticketID&gt;&lt;/subscription&gt;&lt;/subscriptions&gt;&lt;resources&gt;&lt;resource find=&quot;type&quot;&gt;&lt;type&gt;IMLogging&lt;/type&gt;&lt;limit&gt;&lt;/limit&gt;&lt;/resource&gt;&lt;resource find=&quot;type&quot;&gt;&lt;type&gt;user&lt;/type&gt;&lt;limit&gt;123&lt;/limit&gt;&lt;/resource&gt;&lt;resource find=&quot;type&quot;&gt;&lt;type&gt;storage&lt;/type&gt;&lt;limit&gt;125952&lt;/limit&gt;&lt;/resource&gt;&lt;/resources&gt;&lt;/connect&gt;&lt;distribution&gt;&lt;enableDistribution&gt;false&lt;/enableDistribution&gt;&lt;/distribution&gt;&lt;/WBX&gt;&lt;/ext&gt;&lt;/thing&gt;";
//		String xxx = URLDecoder.decode(sss);
//		System.out.println(xxx);
//		byte[] bb = Base64.decodeBase64("SFRUUC8xLjEgMjAwIE9LDQpTZXJ2ZXI6IEFwYWNoZS1Db3lvdGUvMS4xDQpQM1A6IENQPSJDQU8gRFNQIENPUiBDVVJvIEFETW8gREVWbyBUQUlvIENPTm8gT1VSIEJVUyBJTkQgUEhZIE9OTCBVTkkgUFVSIENPTSBOQVYgREVNIFNUQSIsIHBvbGljeXJlZj0iaHR0cDovL2V2ZXJlc3Qud2ViZXguY29tL3AzcC9Qb2xpY3lSZWZlcmVuY2UucDNwIg0KRjVfQ1JFREVOVElBTDogWjdsDQpTZXQtQ29va2llOiBGNV9DUkVERU5USUFMPVo3bDsgRXhwaXJlcz1UdWUsIDA1LUp1bi0yMDEyIDEwOjM1OjU3IEdNVA0KUHJhZ21hOiBuby1jYWNoZQ0KQ2FjaGUtQ29udHJvbDogbm8tY2FjaGUNCkNvbnRlbnQtVHlwZTogdGV4dC94bWwNClRyYW5zZmVyLUVuY29kaW5nOiBjaHVua2VkDQpDb250ZW50LUVuY29kaW5nOiBnemlwDQpWYXJ5OiBBY2NlcHQtRW5jb2RpbmcNCkRhdGU6IFR1ZSwgMDUgSnVuIDIwMTIgMDg6MzU6NTcgR01UDQoNCmENCh+LCAAAAAAAAAANCmMxDQplj8EOgjAMhu8+BeGumwlBD3NEB6hoTASNwg1h6hIcuA1Fn16C8+SpX9OvzV/kNLfCeFAhWckn5nAATYPyrMwZv0zM/c7vj00H99Dz1KQVw0hQWZVc0o7qQuFoT4gXRQjovgOtSJrVgqkXKbmiTTvLBM1xMipYEci7Zb9m73kg09XZZmFFIIQIdAYCf5uCqlpwjNS1DabL0sWJnXhxEIaWS2zib9bHZHGYxnC7tRD4OZq6ZN8jQH/zAQ0Mu3X9AAAADQowDQoNCg==");
//		System.out.println(new String(bb));

		
		String sss = "cmd=execute&task=RunScript&xml=%3C%3Fxml+version%3D%221.0%22+encoding%3D%22UTF-8%22%3F%3E%0A%0A%3Csteps%3E%0A++%3Cstep%3E%0A++++%3Ccmd%3Ecreate%3C%2Fcmd%3E%0A++++%3Ctype%3Egroup%3C%2Ftype%3E%0A++++%3Cxml%3E%0A++++++%3Cgroup%3E%0A++++++++%3CgroupName%3EGx712056997_01%3C%2FgroupName%3E%0A++++++%3C%2Fgroup%3E%0A++++%3C%2Fxml%3E%0A++++%3Cset+var%3D%22groupID%22+path%3D%22group%2FgroupID%22%2F%3E%0A++%3C%2Fstep%3E%0A++%3Cstep%3E%0A++++%3Ccmd%3Eadd%3C%2Fcmd%3E%0A++++%3Ctype%3Egroup%3C%2Ftype%3E%0A++++%3Cid%3EG0FWZO0V0GJ3F4X5FZSK7RNQVT%3C%2Fid%3E%0A++++%3Cxml%3E%0A++++++%3Cgroup%3E%0A++++++++%3CchildGroups%3E%0A++++++++++%3CchildGroup%3E%0A++++++++++++%3CgroupID%3E%24%24groupID%24%24%3C%2FgroupID%3E%0A++++++++++%3C%2FchildGroup%3E%0A++++++++%3C%2FchildGroups%3E%0A++++++%3C%2Fgroup%3E%0A++++%3C%2Fxml%3E%0A++++%3CcontinueOnError%3Efalse%3C%2FcontinueOnError%3E%0A++%3C%2Fstep%3E%0A++%3Cstep%3E%0A++++%3Ccmd%3Ecommit%3C%2Fcmd%3E%0A++%3C%2Fstep%3E%0A%3C%2Fsteps%3E%0A&cred=Z7lgdGRgzBNXrFTnPF0YfetsG00";
		String xxx = URLDecoder.decode(sss);
		System.out.println(xxx);
		
	}

	/**
	 * 编码
	 * 
	 * @param contents
	 * @param width
	 * @param height
	 * @param imgPath
	 */
	public void encode(String contents, int width, int height, String imgPath) {
		Map<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>();
		// 指定纠错等级
		hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
		// 指定编码格式
		hints.put(EncodeHintType.CHARACTER_SET, "ISO-8859-1");
		try {
			BitMatrix bitMatrix = new MultiFormatWriter().encode(contents,
					BarcodeFormat.QR_CODE, width, height, hints);

			MatrixToImageWriter
					.writeToFile(bitMatrix, "png", new File(imgPath));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
