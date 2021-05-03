package com.fbn.cp;

import java.util.Map;
import java.util.Set;

import com.fbn.api.newgen.customservice.CompleteWorkItem;
import com.fbn.api.newgen.controller.Controller;
import com.fbn.utils.Commons;
import com.fbn.utils.Query;
import com.fbn.utils.ConstantsI;

public class SecondaryMarket extends Commons implements Runnable, ConstantsI {
    private  final String sessionId;
    public SecondaryMarket(String sessionId) {
        this.sessionId = sessionId;
    }

    @Override
    public void run() {
        closeCpMarketWindow();
    	closeSmInvestmentWindow();
    	processAllSmBidsOnMaturity();
    }

    private void closeCpMarketWindow(){
        Set<Map<String, String>> resultSet = new Controller().getRecords(Query.getCpOpenWindowQuery(cpSecondaryMarket));
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

            if (Commons.compareDateTime(date)) {
                new Controller().updateRecords(sessionId, Query.setupTblName, Query.stColCloseFlag, value, condition);
                new CompleteWorkItem(sessionId,wiName,"CLOSEFLAG","Y");
            }
        }
    }
 
    private void closeSmInvestmentWindow() {
    	
        resultSet = new Controller().getRecords(Query.getCpInvestmentClosedateTbl());
        String columns = "CLOSEDFLAG, STATUS";     
        for (Map<String, String> result : resultSet) {
        	    String date = result.get(investClosedate.toUpperCase());
        	    
        	    if(Commons.compareDateTime(date)) {
        	    	String id = result.get(investID.toUpperCase());
        	    	String values = "'Y', 'Closed'";
                	String condition = "INVESTMENTID = '"+id+"'";   	
                	new Controller().updateRecords(sessionId,Query.investmentTblName,columns,values,condition);
        	    	
        	    }
        }
    	
    }
    
    private void processAllSmBidsOnMaturity() {	
    	resultSet = new Controller().getRecords(Query.getCpAllBidsOnMaturity());
    	String wiName = "";
    	String columns = "MATUREDFLAG, PAIDFLAG, POSTINTEGRATIONMATUREFLAG";
        for (Map<String ,String> result : resultSet){
        	    String date = result.get(bidmaturityDate.toUpperCase());
          
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
