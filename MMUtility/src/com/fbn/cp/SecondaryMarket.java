package com.fbn.cp;

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
    	closeSmInvestmentWindow();
    }
 
    private void closeSmInvestmentWindow() {
    	
        resultSet = new Controller().getRecords(Query.getCpInvestmentClosedateTbl());
        String columns = "CLOSEDFLAG, STATUS";     
        for (Map<String, String> result : resultSet) {
        	    String date = result.get(investClosedate.toUpperCase());
        	    
        	    if(Commons.compareDate(date)) {
        	    	String id = result.get(investID.toUpperCase());
        	    	String values = "'Y', 'Closed'";
                	String condition = "INVESTMENTID = '"+id+"'";   	
                	new Controller().updateRecords(sessionId,Query.investmentTblName,columns,values,condition);
        	    	
        	    }
        }
    	
    }
    
    private void processAllSmBidsOnMaturity() {	
    	resultSet = new Controller().getRecords(Query.getAllBidsOnMaturity());
    	String wiName = "";
    	String columns = "MATUREDFLAG, PAIDFLAG, POSTINTEGRATIONMATUREFLAG";
        for (Map<String ,String> result : resultSet){
        	    String date = result.get(maturityDate.toUpperCase());
          
                if (Commons.isMatured(date)) {
                	 String id = result.get(bidCustIdCol.toUpperCase());
                     wiName = result.get(bidWinameCol.toUpperCase());
                     String custSol = result.get(bidCustSolCol.toUpperCase());
                     String custPrincipal = result.get(bidCustPrincipalCol.toUpperCase());
                     String branchSol = result.get(bidBranchSolCol.toUpperCase());
                     String allocationPercentage = result.get(bidAllocationPercentageCol.toUpperCase());
                	
                    //Perform all the neccessary posting
                     
                	String values = "'Y', 'Y', 'Y'";
                	String condition = "CUSTREFID = '"+id+"'";
                	new Controller().updateRecords(sessionId,Query.bidTblName,columns,values,condition);
                    new CompleteWorkItem(sessionId,wiName);
                }
        }
    	
    }
    
    
}
