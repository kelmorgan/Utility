package com.fbn.cp;

public class cpMain implements Runnable {
      public cpMain(String sessionId) {
       this.sessionId = sessionId;
    }

    private  String sessionId;

    @Override
    public void run() {
        System.out.println("cpMain running");
    }
}
