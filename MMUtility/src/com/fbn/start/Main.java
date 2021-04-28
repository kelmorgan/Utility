package com.fbn.start;

import com.fbn.api.newgen.controller.Controller;
import com.fbn.utils.ConstantsI;

public class Main extends Thread implements ConstantsI {
    private final String sessionId;

    public Main() {
        this.sessionId = new Controller().getSessionId();
   }
    
    private void disconnectSession (){
        new Controller().disconnectSession(sessionId);
    }

    public void run() {
        //closeMarketWindow();
        disconnectSession();
       // new cpMain().run();
    }
}
