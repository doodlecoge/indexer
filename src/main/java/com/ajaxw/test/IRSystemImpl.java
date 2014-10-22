package com.ajaxw.test;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.apache.thrift.TException;

public class IRSystemImpl implements IRSystem.Iface {

	@Override
	public Set<String> query(String queryString) throws TException {
		QuerySystem qs = null;
		try {
			qs = new QuerySystem();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		System.out.println(queryString);
		Set<String> set = null;
		try {
			set = qs.ir(queryString);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return set;
	}

}
