package com.fbn.tb;

import java.util.Map;
import java.util.Set;

import com.fbn.api.newgen.controller.Controller;
import com.fbn.api.newgen.customservice.CompleteWorkItem;
import com.fbn.utils.Commons;
import com.fbn.utils.ConstantsI;
import com.fbn.utils.Query;


public class SecondaryMarket implements  ConstantsI {
	private  final String sessionId;   
    private Set<Map<String,String>> resultSet;

    public SecondaryMarket(String sessionId) {
        this.sessionId = sessionId;
    }
    

    public void main() {
    	
    }
    
    //Closing Secondary window
    private void closeTbMarketWindow(){
        Set<Map<String, String>> resultSet = new Controller().getRecords(Query.getTbOpenWindowQuery(secondaryMarket));
        System.out.println(resultSet);
        for (Map<String ,String> result : resultSet){
            String date = result.get("CLOSEDATE");
            System.out.println(date);
            String id = result.get("REFID");
            System.out.println(id);
            String value = "'"+flag+"'";
            String condition = "refid = '"+id+"'";

            if (Commons.checkClosedDate(date)) {
                new Controller().updateRecords(sessionId, Query.setupTblName, Query.stColCloseFlag, value, condition);
            }
        }
    }
    
    // Closing Issued bids(Investments)
    private void closeSmIssuedBillsWindow() {
    	
        resultSet = new Controller().getRecords(Query.getTbIssuedBillsClosedateTbl());
        String column = "TBSTATUS";     
        for (Map<String, String> result : resultSet) {
        	    String date = result.get("MATURITYDATE");
        	    
        	    if(Commons.checkClosedDate(date)) {
        	    	String id = result.get("WINAME");
        	    	String value = "'Closed'";
                	String condition = "WINAME = '"+id+"'";   	
                	new Controller().updateRecords(sessionId,Query.tbInvestmentTblName,column,value,condition);
        	    	
        	    }
        }
    	
    }
    
    //Moving work items from Awaiting maturity to treasury ops matured queue on maturity day
	 private void processWIfromAwaitingMaturityToMaturedWS() {
		 resultSet = new Controller().getRecords(Query.getTbProcessBidsOnAwaitingMaturity());
		 for (Map<String, String> result : resultSet) {
			 	String id = result.get(wiName);
		    	String maturityDate = result.get("");
		    	String lienStatus = result.get("");
		    	String TresaurylienStatus = result.get("");
		    	if (Commons.is7DaysToMaturity(maturityDate) && lienStatus == "Y") {
		    		//send mail to branch initiator, verifier, customer		
		    	}
		    	else {
		    		if(Commons.isMatured(maturityDate) && lienStatus == "N") {
		    			if(TresaurylienStatus == "N") {
		    				String column = "TB_DECISION";
		    				String value = "'Approve'";
		                    String condition = "winame = '"+id+"'";
		                    new Controller().updateRecords(sessionId, Query.extTblName, column, value, condition); 
		                    new CompleteWorkItem(sessionId,wiName);
		    			}	    			
		    		}
		    	}
		 }	 
	 }
	 
	 // Posting for Treasury Bill (Secondary Market) on Maturity Date
	 
    
    
    
    

}
