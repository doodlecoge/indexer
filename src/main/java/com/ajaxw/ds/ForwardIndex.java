package com.ajaxw.ds;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class ForwardIndex implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6641453443142773593L;

	private Set<String> keys;
	private int docId;
	private String url;

	public ForwardIndex(int docId, String url) {
		this.docId = docId;
		this.url = url;
		keys = new HashSet<String>();
	}

	public void add(Set<String> keys) {
		this.keys.addAll(keys);
	}

	public Set<String> getKeys() {
		return this.keys;
	}

	public int getDocId() {
		return this.docId;
	}

	public String getUrl() {
		return this.url;
	}
}
