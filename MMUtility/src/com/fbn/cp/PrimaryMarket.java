package com.fbn.cp;

import com.fbn.api.newgen.CompleteWorkItem;
import com.fbn.api.newgen.Controller;
import com.fbn.utils.ConstantsI;
import com.fbn.utils.Query;

import java.util.Map;
import java.util.Set;

public class PrimaryMarket implements Runnable,ConstantsI {
    private Set<Map<String,String>> resultSet;

    public PrimaryMarket(String sessionId) {
        this.sessionId = sessionId;
    }

    private final String sessionId;

    @Override
    public void run() {
        processPrimaryBids();
        processFailedBids();
    }

    private void  processPrimaryBids(){
        String attributes = "<CP_UTILITYFLAG>Y</CP_UTILITYFLAG>";
        String wiName = new Controller().getCreatedWorkItem(sessionId,attributes,initiateFlagNo);
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
    
    // Added by Amula
    private void processFailedBids() {
    	String columns = "POSTINTEGRATIONFLAG, REVERSALFLAG";
    	String wiName = "";
    	String attribute = "FAILEDBID";
    	resultSet = new Controller().getRecords(Query.getAllocatedBids("Y"));
    	for (Map<String,String> result : resultSet){
             String id = result.get(bidCustIdCol.toUpperCase());
             wiName = result.get(bidWinameCol.toUpperCase());
             String custSol = result.get(bidCustSolCol.toUpperCase());
             String custPrincipal = result.get(bidCustPrincipalCol.toUpperCase());
             String branchSol = result.get(bidBranchSolCol.toUpperCase());
             
             
           //perform reversal
            
             String values = "'Y', 'Y'";
             String condition = "CUSTREFID = '"+id+"'";
             new Controller().updateRecords(sessionId,Query.bidTblName,columns,values,condition);
        }
    	new CompleteWorkItem(sessionId,wiName,attribute,flag);
    }
    
    private void processSucessfulBids() {
    	String column = "POSTINTEGRATIONFLAG";
    	String wiName = "";
    	String attribute = "SUCCESSBID";
    	resultSet = new Controller().getRecords(Query.getAllocatedBids("N"));
    	for (Map<String,String> result : resultSet){
            String id = result.get(bidCustIdCol.toUpperCase());
            wiName = result.get(bidWinameCol.toUpperCase());
            String custSol = result.get(bidCustSolCol.toUpperCase());
            String custPrincipal = result.get(bidCustPrincipalCol.toUpperCase());
            String branchSol = result.get(bidBranchSolCol.toUpperCase());
            
            //credit the principal and debit customer principal based on allocation percentage
            
            String value = "'Y'";
            String condition = "CUSTREFID = '"+id+"'";
            new Controller().updateRecords(sessionId,Query.bidTblName,column,value,condition);
    	
    	}
    	new CompleteWorkItem(sessionId,wiName,attribute,flag);
    }

}
