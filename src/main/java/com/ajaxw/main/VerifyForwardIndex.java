package com.ajaxw.main;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;

import com.ajaxw.ds.ForwardIndex;

public class VerifyForwardIndex {

	/**
	 * @param args
	 * @throws IOException
	 * @throws FileNotFoundException
	 * @throws ClassNotFoundException
	 */
	public static void main(String[] args) throws FileNotFoundException,
			IOException {

		ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(
				new FileInputStream("E:\\1.dat.objs")));

		while (true) {
			ForwardIndex fidx;
			try {
				fidx = (ForwardIndex) ois.readObject();
			} catch (ClassNotFoundException e) {
				break;
			}
			System.out.println(fidx.getDocId());
//			System.out.println(fidx.getUrl());
//			Set<String> keys = fidx.getKeys();
//
//			for (String string : keys) {
//				System.out.println(string);
//			}

		}
	}

}
