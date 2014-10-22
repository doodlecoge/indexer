package com.ajaxw.irsystem;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.ajaxw.constant.CONST;
import com.ajaxw.io.IterableFile;
import com.ajaxw.util.Helper;

public class GetUncrawledUrls {

	// remove urls in file2 from file1,
	// and write the rest to file3
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		String dir = "H:\\ajaxw\\data\\url\\";
		String file1 = dir + "scst.urls";
		String file2 = dir + "clean.urls";
		String file3 = dir + "diff.urls";

		Map<String, Boolean> urls = getNewUrls(file1);
		BufferedWriter bw = Helper.getBufferedFileWriter(file3);

		for (String line : new IterableFile(file2)) {
			if (urls.containsKey(line))
				urls.put(line, true);
		}
		
		Iterator<String> us = urls.keySet().iterator();
		while(us.hasNext()) {
			String u = us.next();
			if(urls.get(u) == false){
				bw.write(u);
				bw.write(CONST.LF);
			}
		}
		
		bw.close();
		System.out.println("end");
	}

	public static Map<String, Boolean> getNewUrls(String file1)
			throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(file1));
		String line = null;

		Map<String, Boolean> urls = new HashMap<String, Boolean>();
		while ((line = br.readLine()) != null) {
			urls.put(line, false);
		}

		return urls;
	}
}
