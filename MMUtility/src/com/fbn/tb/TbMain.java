package com.fbn.tb;

import com.fbn.cp.PrimaryMarket;

public class TbMain implements Runnable {
	private final String sessionId;
	
	public TbMain(String sessionId) {
		this.sessionId = sessionId;
    }
	
	@Override
	public void run() {
	     // new PrimaryMarket(sessionId).run();
	     // new SecondaryMarket(sessionId).run();
	}

}
