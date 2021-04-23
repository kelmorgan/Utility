package com.fbn.start;

import com.fbn.api.newgen.CompleteWorkItem;
import com.fbn.api.newgen.Controller;
import com.fbn.utils.Commons;
import com.fbn.utils.ConstantsI;
import com.fbn.utils.Query;

import java.util.Map;
import java.util.Set;

public class Main extends Thread implements ConstantsI {
    private Set<Map<String,String>> resultSet;
    private final String sessionId;

    public Main() {
        this.sessionId = new Controller().getSessionId();
   }
    private void closeMarketWindow(){
        resultSet = new Controller().getRecords(Query.getCpOpenWindowQuery());
        System.out.println(resultSet);
        for (Map<String ,String> result : resultSet){
            String date = result.get("CLOSEDATE");
            System.out.println(date);
            String wiName = result.get("WINAME");
            System.out.println(wiName);
            String id = result.get("REFID");
            System.out.println(id);
            String value = "'"+flag+"'";
            String condition = "refid = '"+id+"'";

            if (Commons.compareDate(date)) {
                new Controller().updateRecords(sessionId, Query.setupTblName, Query.stColCloseFlag, value, condition);
                new CompleteWorkItem(sessionId,wiName,"CLOSEFLAG","Y");
            }
        }
    }
    private void disconnectSession (){
        new Controller().disconnectSession(sessionId);
    }

    public void run() {
        closeMarketWindow();
        disconnectSession();
       // new cpMain().run();
    }
}
