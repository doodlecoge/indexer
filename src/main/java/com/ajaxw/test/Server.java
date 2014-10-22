package com.ajaxw.test;

import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadPoolServer.Args;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TTransportException;
import org.apache.thrift.protocol.TBinaryProtocol.Factory;

public class Server {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Server svr = new Server();
		svr.start();
	}

	private void start() {
		try {
			TServerSocket serverTransport = new TServerSocket(
					Integer.parseInt(IRSysConf.getProperty("port")));
			IRSystem.Processor<IRSystemImpl> processor = new IRSystem.Processor<IRSystemImpl>(
					new IRSystemImpl());
			Factory protFactory = new TBinaryProtocol.Factory(true, true);

			Args args = new Args(serverTransport);
			args.processor(processor);
			args.protocolFactory(protFactory);

			TServer server = new TThreadPoolServer(args);

			System.out.println("starting server ...");
			server.serve();
		} catch (TTransportException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
