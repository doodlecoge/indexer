package com.ajaxw.main;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.ajaxw.ds.ForwardIndex;
import com.ajaxw.util.Helper;

public class ConstructInvertedIndex {

	/**
	 * @param args
	 * @throws IOException
	 */

	public static Logger logger = Logger.getRootLogger();

	public static void main(String[] args) throws FileNotFoundException,
			IOException, ClassNotFoundException {
		String fn = "conf.properties";
		Properties props = Helper.loadProperties(fn);

		String order = props.getProperty("constructOrder");
		String dir = props.getProperty("dir");
		String initFile = props.getProperty("initFile");

		String[] fileNames = order.split(",");

		InvertedIndex invtIdx = new InvertedIndex();
		if (initFile != null && initFile.length() > 0)
			invtIdx = readInvtIdx(dir + "\\" + initFile);

		System.out.println(">>> construct order:");
		for (String fileName : fileNames) {
			System.out.println("  > " + fileName);
		}

		for (String fileName : fileNames) {
			fileName = fileName.trim();
			logger.debug("merging file: " + fileName);
			
			ObjectInputStream ois = new ObjectInputStream(
					new BufferedInputStream(new FileInputStream(dir + "\\"
							+ fileName)));

			while (true) {
				ForwardIndex fidx;

				try {
					fidx = (ForwardIndex) ois.readObject();
				} catch (IOException e) {
					break;
				} catch (ClassNotFoundException e) {
					break;
				}

				invtIdx.add(fidx);
			}

			ois.close();

			// dump inverted list after merging each file
			// in case that program filed while processing
			String midFile = dir + "\\" + fileName + ".mid";
			logger.debug("dump 2: " + midFile);
			writeInvtIdx(invtIdx, midFile);
		}
	}

	public static InvertedIndex readInvtIdx(String file)
			throws FileNotFoundException, IOException, ClassNotFoundException {
		ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(
				new FileInputStream(file)));

		InvertedIndex invtIdx = (InvertedIndex) ois.readObject();
		ois.close();
		return invtIdx;
	}

	public static void writeInvtIdx(InvertedIndex invtIdx, String file)
			throws FileNotFoundException, IOException {
		ObjectOutputStream oos = new ObjectOutputStream(
				new BufferedOutputStream(new FileOutputStream(file)));
		oos.writeObject(invtIdx);
		oos.close();
	}

}
