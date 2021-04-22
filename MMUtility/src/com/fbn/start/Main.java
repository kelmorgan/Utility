package com.fbn.start;

import com.fbn.api.newgen.Controller;
import com.fbn.utils.Commons;
import com.fbn.utils.ConstantsI;
import com.fbn.utils.Query;

import java.util.Map;
import java.util.Set;

public class Main implements ConstantsI {
    private Set<Map<String,String>> resultSet;
    public void closeMarketWindow(){
        resultSet = new Controller().getRecords(Query.getOpenWindowQuery());
        System.out.println(resultSet);
        String sessionId = new Controller().getSessionId();
        for (Map<String ,String> result : resultSet){
            String date = result.get("CLOSEDATE");
            System.out.println(date);
            String wiName = result.get("WINAME");
            System.out.println(wiName);
            String id = result.get("REFID");
            System.out.println(id);
            String value = "'"+flag+"'";
            String condition = "refid = '"+id+"'";

            if (Commons.compareDate(date))
                new Controller().updateRecords(sessionId,Query.setupTblName,Query.stColCloseFlag,value,condition);
        }
    }
}
