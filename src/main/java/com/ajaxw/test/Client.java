package com.ajaxw.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Set;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

public class Client {
	public static void main(String[] args) throws IOException {

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		while (true) {
			System.out.println("input query:");
			String str = br.readLine();
			query(str);
		}
	}

	public static void query(String queryString) {
		TTransport transport;
		try {
			transport = new TSocket("localhost", Integer.parseInt(IRSysConf
					.getProperty("port")));
			TProtocol protocol = new TBinaryProtocol(transport);
			IRSystem.Client client = new IRSystem.Client(protocol);
			transport.open();
			Set<String> set = client.query(queryString);

			System.out.println("Find " + set.size() + " records.");
			for (String rst : set) {
				System.out.println(rst);
			}
			transport.close();
		} catch (TTransportException e) {
			e.printStackTrace();
		} catch (TException e) {
			e.printStackTrace();
		}
	}
}
