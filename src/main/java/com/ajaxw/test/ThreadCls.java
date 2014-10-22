package com.ajaxw.test;

import java.util.Calendar;

public class ThreadCls extends Thread {

	@Override
	public void run() {
		// TODO Auto-generated method stub
		System.out.println(Calendar.getInstance());
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		super.run();
	}

}
