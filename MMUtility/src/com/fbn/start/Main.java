package com.fbn.start;

import com.fbn.api.newgen.CompleteWorkItem;
import com.fbn.api.newgen.Controller;
import com.fbn.utils.Commons;
import com.fbn.utils.ConstantsI;
import com.fbn.utils.Query;

import java.util.Map;
import java.util.Set;

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
