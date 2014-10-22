package com.ajaxw.ds;

import java.io.Serializable;

public class Range implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4757101019736927792L;
	private int off;
	private int len;

	public int getOff() {
		return off;
	}

	public int getLen() {
		return len;
	}

	public Range(int off, int len) {
		this.off = off;
		this.len = len;
	}
}
