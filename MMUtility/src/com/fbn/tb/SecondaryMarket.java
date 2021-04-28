package com.fbn.tb;

import java.util.Map;
import java.util.Set;

import com.fbn.api.newgen.CompleteWorkItem;
import com.fbn.api.newgen.Controller;
import com.fbn.utils.Commons;
import com.fbn.utils.Query;
import com.fbn.utils.ConstantsI;


public class SecondaryMarket implements Runnable, ConstantsI {
	private  final String sessionId;   
    private Set<Map<String,String>> resultSet;

    public SecondaryMarket(String sessionId) {
        this.sessionId = sessionId;
    }
    
    @Override
    public void run() {
    	
    }

}
