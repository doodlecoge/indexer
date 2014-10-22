package com.ajaxw.ciscostuff;

import java.util.HashMap;
import java.util.Map;

public class Org {
	private String orgName;
	private Org parent;
	private Map<String, Org> children;

	public String getOrgName() {
		return orgName;
	}

	public Org getParent() {
		return parent;
	}

	public void setParent(Org parent) {
		this.parent = parent;
	}

	public Map<String, Org> getChildren() {
		return children;
	}

	public Org(String orgName) {
		this.orgName = orgName;
		children = new HashMap<String, Org>();
		this.parent = null;
	}
}
