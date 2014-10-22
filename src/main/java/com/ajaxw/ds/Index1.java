package com.ajaxw.ds;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Index1 implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9007129618173448013L;

	Map<String, Range> idx1;

	{
		this.idx1 = new HashMap<String, Range>();
	}

	public void add(String key, Range rng) {
		this.idx1.put(key, rng);
	}

	public Map<String, Range> getIdx1() {
		return this.idx1;
	}

	public Range getRange(String key) {
		return this.idx1.get(key);
	}
}
