package com.ajaxw.main;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Properties;

import com.ajaxw.io.IterableFile;
import com.ajaxw.thread.ThreadPool;
import com.ajaxw.util.Helper;

public class Crawler {

	/**
	 * @param args
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 * @throws MalformedURLException
	 * @throws KeyManagementException
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws KeyManagementException,
			MalformedURLException, NoSuchAlgorithmException, IOException,
			InterruptedException {

//		Properties props = Helper.loadProperties("conf.properties");
//		System.setProperty("page_dir", props.getProperty("page_dir"));
//
//		int threads = 10;
//		if (args.length > 0)
//			threads = Integer.parseInt(args[0]);
//
//		ThreadPool pool = new ThreadPool(threads);
//
//		
//		for (String line : new IterableFile(props.getProperty("url_file"))) {
//			pool.exec(new CrawlerWorker(line));
//		}
//
//		pool.stop();

	}

}
