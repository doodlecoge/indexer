package com.ajaxw.main;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.ajaxw.ds.ForwardIndex;
import com.ajaxw.util.Helper;

public class InvertedIndex implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2336462073102251253L;

	public static Logger logger = Logger.getRootLogger();

	private Map<String, List<Integer>> invtlst;
	private Map<Integer, String> urls;

	public Map<String, List<Integer>> getInvtlst() {
		return invtlst;
	}

	public Map<Integer, String> getUrls() {
		return urls;
	}

	public InvertedIndex() {
		this.invtlst = new HashMap<String, List<Integer>>();
		this.urls = new HashMap<Integer, String>();
	}

	public void add(ForwardIndex fidx) {
		int docId = fidx.getDocId();
		String url = fidx.getUrl();
		Set<String> keys = fidx.getKeys();

		// mapping:
		// docId <---> url
		this.urls.put(docId, url);

		for (String key : keys) {
			List<Integer> docIds = this.invtlst.get(key);
			if (docIds == null) {
				docIds = new ArrayList<Integer>();
				this.invtlst.put(key, docIds);
			}

			if (docIds.size() == 0 || docId > docIds.get(docIds.size() - 1))
				docIds.add(docIds.size(), docId);
			else {
				int idx = Helper.binarySearch(docId, docIds);
				if (idx >= 0)
					logger.debug("find duplicate: " + docId);
				else {
					idx = -idx - 1;
					docIds.add(idx, docId);
				}
			}
		}
	}
}
