package com.ajaxw.main;

import java.io.IOException;

import com.ajaxw.io.IterableFile;
import com.ajaxw.thread.ThreadPool;

public class UrlClean {

	/**
	 * @param args
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws IOException,
			InterruptedException {
		// TODO Auto-generated method stub
		int threads = 20;
		if (args.length > 0)
			threads = Integer.parseInt(args[0]);

		ThreadPool pool = new ThreadPool(threads);

		// BufferedReader br = Helper.getBufferedFileReader("suda.urls");
		// String line = null;
		// while ((line = br.readLine()) != null) {
		// pool.exec(new Worker(line));
		// }

//		String file = "H:\\ajaxw\\data\\url\\scst.urls";
//		for (String line : new IterableFile(file)) {
//			pool.exec(new Worker(line));
//		}
//
//		pool.stop();
//
//		System.out.println("num: " + Worker.num);
	}

}
