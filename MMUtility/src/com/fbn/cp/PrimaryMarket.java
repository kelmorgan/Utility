package com.fbn.cp;

import com.fbn.api.newgen.CompleteWorkItem;
import com.fbn.api.newgen.Controller;
import com.fbn.utils.Commons;
import com.fbn.utils.ConstantsI;
import com.fbn.utils.Query;
import java.util.Map;
import java.util.Set;

public class PrimaryMarket implements Runnable,ConstantsI {
    private Set<Map<String,String>> resultSet;

    boolean postingIsSucessful = true; //for structure purpose

    public PrimaryMarket(String sessionId) {
        this.sessionId = sessionId;
    }

    private final String sessionId;

    @Override
    public void run() {
        processPrimaryBids();
        processFailedBids();
        processPostingFailureFailedBids();
        processSuccessfulBids();
        processPostingFailureSuccessBids();
        processAllBidsOnMaturity();
    }

    private void  processPrimaryBids(){
        String attribute = "<CP_UTILITYFLAG>Y</CP_UTILITYFLAG>";
        String wiName = new Controller().getCreatedWorkItem(sessionId,attribute,initiateFlagNo);
        resultSet = new Controller().getRecords(Query.getCpPmBidsToProcessQuery());
        String columns = "utilitywiname,groupindex,groupindexflag,processflag";
        for (Map<String,String >result : resultSet){
            String id = result.get("CUSTREFID");
            String tenor = result.get("TENOR");
            String rate = result.get("RATE");
            String rateType = result.get("RATETYPE");
            String groupIndex = getCpGroupIndex(wiName,tenor,rateType,rate);
            String values = "'"+wiName+"','"+groupIndex+"','Y','Y'";
            String condition = "CUSTREFID = '"+id+"'";

            new Controller().updateRecords(sessionId,Query.bidTblName,columns,values,condition);
        }
       new CompleteWorkItem(sessionId,wiName);
    }
    private String getCpGroupIndex(String wiName,String tenor,String rateType, String rate){
        String strPattern = "^0+(?!$)";
        String groupLabel = "CPPMG";
        String pRateLabel = "P";
        String bRateLabel = "B";
        String [] wiNameArray = wiName.split("-");
        String wiNameTrim = wiNameArray[1];
        wiNameTrim = wiNameTrim.replaceAll(strPattern,"");

        return groupLabel + wiNameTrim + tenor + (isPRate(rateType) ? pRateLabel : bRateLabel) + (isPRate(rateType) ? rate : "");
    }
    private boolean isPRate(String rateType){
        String pRateType = "Personal";
        return rateType.equalsIgnoreCase(pRateType);
    }
    
    private void processFailedBids() {
    	String columnsS = "POSTINTEGRATIONFLAG, REVERSALFLAG";
    	String columnsF = "POSTINTEGRATIONFLAG, FAILEDPOSTFLAG";
    	String wiName = empty;
    	String attribute = "FAILEDBID";
    	resultSet = new Controller().getRecords(Query.getCpAllocatedPrimaryBids("Y"));
    	for (Map<String,String> result : resultSet){
             String id = result.get(bidCustIdCol.toUpperCase());
             wiName = result.get(bidWinameCol.toUpperCase());
             String custSol = result.get(bidCustSolCol.toUpperCase());
             String custPrincipal = result.get(bidCustPrincipalCol.toUpperCase());
             String branchSol = result.get(bidBranchSolCol.toUpperCase());

             //perform reversal
             if (postingIsSucessful) {  
             String values = "'Y', 'Y'";
             String condition = "CUSTREFID = '"+id+"'";
             new Controller().updateRecords(sessionId,Query.bidTblName,columnsS,values,condition);
             new CompleteWorkItem(sessionId,wiName,attribute,flag);
             
             }
             else {
            	 String values = "'Y', 'Y'";
                 String condition = "CUSTREFID = '"+id+"'";
            	 new Controller().updateRecords(sessionId,Query.bidTblName,columnsF,values,condition);
             }
        }
    }
    
    
    private void processPostingFailureFailedBids() {
    	String attribute = "<CP_UTILITYFLAG>F</CP_UTILITYFLAG>";
    	String wiName = new Controller().getCreatedWorkItem(sessionId,attribute,initiateFlagNo);
    	String column = "FAILEDTRANUTILITYWINAME";
    	String value = "'"+wiName+"'";
    	
    	resultSet = new Controller().getRecords(Query.getProcessPostingFailureFailedBids(flag));
        for (Map<String,String> result : resultSet){
        	String id = result.get(bidCustIdCol.toUpperCase());
        	String condition = "CUSTREFID = '"+id+"'";
        	new Controller().updateRecords(sessionId,Query.bidTblName,column,value,condition);
        }
 
    	new CompleteWorkItem(sessionId,wiName);
    }
    
    private void processSuccessfulBids() {
    	String columnS = "POSTINTEGRATIONFLAG";
    	String columnsF = "POSTINTEGRATIONFLAG, FAILEDPOSTFLAG";
    	String wiName = "";
    	String attribute = "SUCCESSBID";
    	resultSet = new Controller().getRecords(Query.getCpAllocatedPrimaryBids("N"));
    	for (Map<String,String> result : resultSet){
            String id = result.get(bidCustIdCol.toUpperCase());
            wiName = result.get(bidWinameCol.toUpperCase());
            String custSol = result.get(bidCustSolCol.toUpperCase());
            String custPrincipal = result.get(bidCustPrincipalCol.toUpperCase());
            String branchSol = result.get(bidBranchSolCol.toUpperCase());
            String allocationPercentage = result.get(bidAllocationPercentageCol.toUpperCase());

            //credit the principal and debit customer principal based on allocation percentage
            if (postingIsSucessful) {
            String value = "'Y'";
            String condition = "CUSTREFID = '"+id+"'";
            new Controller().updateRecords(sessionId,Query.bidTblName,columnS,value,condition);
            new CompleteWorkItem(sessionId,wiName,attribute,flag);
            }
            else {
           	     String value = "'N'";
                 String condition = "CUSTREFID = '"+id+"'";
           	     new Controller().updateRecords(sessionId,Query.bidTblName,columnsF,value,condition);
    	    }
         } 
    }
     

    private void processPostingFailureSuccessBids() {
    	String attribute = "<CP_UTILITYFLAG>S</CP_UTILITYFLAG>";
    	String wiName = new Controller().getCreatedWorkItem(sessionId,attribute,initiateFlagNo);
    	String column = "FAILEDTRANUTILITYWINAME";
    	String value = "'"+wiName+"'";
    	
    	resultSet = new Controller().getRecords(Query.getProcessPostingFailureSuccessBids("N"));
    	for (Map<String,String> result : resultSet){
        	String id = result.get(bidCustIdCol.toUpperCase());
        	String condition = "CUSTREFID = '"+id+"'";
        	new Controller().updateRecords(sessionId,Query.bidTblName,column,value,condition);
    	}
    	new CompleteWorkItem(sessionId,wiName);
    }
    
    private void processAllBidsOnMaturity() {	
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
