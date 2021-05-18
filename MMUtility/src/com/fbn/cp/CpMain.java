package com.fbn.cp;

public class CpMain implements Runnable {
    private final String sessionId;
    public CpMain(String sessionId) {
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
