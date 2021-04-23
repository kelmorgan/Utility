package com.fbn.cp;

import java.util.Map;
import java.util.Set;

public class cpMain implements Runnable {
      public cpMain(String sessionId) {
       this.sessionId = sessionId;
    }

    private final String sessionId;
    private Set<Map<String,String>> resultSet;

    @Override
    public void run() {
        new PrimaryMarket(sessionId).run();
        //new SecondaryMarket(sessionId).run();
    }

}
