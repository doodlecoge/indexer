package com.ajaxw.ds;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DocUrls implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6165651319089430980L;

	private List<String> lst;
	{
		lst = new ArrayList<String>();
	}

	public void add(String url) {
		this.lst.add(url);
	}

	public List<String> getUrls() {
		return this.lst;
	}
}
