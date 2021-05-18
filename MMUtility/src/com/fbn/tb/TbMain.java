package com.fbn.tb;


public class TbMain implements Runnable {
	private final String sessionId;
	
	public TbMain(String sessionId) {
		this.sessionId = sessionId;
    }
	
	@Override
	public void run() {
	   execute();
	}

	private void execute (){
		new PrimaryMarket(sessionId).main();
		new SecondaryMarket(sessionId).main();
	}
}
