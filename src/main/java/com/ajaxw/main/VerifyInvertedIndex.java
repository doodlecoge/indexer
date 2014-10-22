package com.ajaxw.main;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class VerifyInvertedIndex {

	/**
	 * @param args
	 * @throws ClassNotFoundException
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public static void main(String[] args) throws FileNotFoundException,
			IOException, ClassNotFoundException {
		if (args.length != 1) {
			System.out
					.println("please specify invt file as the only argument.");
			return;
		}
		String fileName = args[0];

		InvertedIndex invtIdx = readInvtIdx(fileName);

		Map<String, List<Integer>> invt_idx = invtIdx.getInvtlst();
		Map<Integer, String> urls = invtIdx.getUrls();

		Iterator<String> keyIt = invt_idx.keySet().iterator();
		while (keyIt.hasNext()) {
			String key = keyIt.next();
			List<Integer> lst = invt_idx.get(key);

			int pre = -1;
			for (int i = 0; i < lst.size(); i++) {
				int cur = lst.get(i);
				if (pre >= cur) {
					System.out.println("err");
					return;
				}
				pre = cur;
			}
		}

		int len = urls.size();
		for (int i = 0; i < len; i++) {
			String url = urls.get(i);
			if (url == null) {
				System.out
						.println("-------------------------------------------");
			} else {
//				System.out.println(url);
			}
		}

		System.out.println("*** end ***");
	}

	public static InvertedIndex readInvtIdx(String file)
			throws FileNotFoundException, IOException, ClassNotFoundException {
		ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(
				new FileInputStream(file)));

		InvertedIndex invtIdx = (InvertedIndex) ois.readObject();
		ois.close();
		return invtIdx;
	}

}
