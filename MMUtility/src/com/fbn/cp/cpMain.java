package com.fbn.cp;

public class cpMain implements Runnable {
    private final String sessionId;
    public cpMain(String sessionId) {
       this.sessionId = sessionId;
    }
    @Override
    public void run() {
        new PrimaryMarket(sessionId).run();
        //new SecondaryMarket(sessionId).run();
    }
}
