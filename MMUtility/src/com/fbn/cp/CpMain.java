package com.fbn.cp;

public class CpMain implements Runnable {
    private final String sessionId;
    public CpMain(String sessionId) {
       this.sessionId = sessionId;
    }
    @Override
    public void run() {
        new PrimaryMarket(sessionId).run();
        new SecondaryMarket(sessionId).run();
    }
}
