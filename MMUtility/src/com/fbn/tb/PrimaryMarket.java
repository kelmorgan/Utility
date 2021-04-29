package com.fbn.tb;

import com.fbn.api.newgen.customservice.CompleteWorkItem;
import com.fbn.api.newgen.controller.Controller;
import com.fbn.api.newgen.customservice.CreateWorkItem;
import com.fbn.utils.Commons;
import com.fbn.utils.ConstantsI;
import com.fbn.utils.Query;

import java.util.Map;
import java.util.Set;

public class PrimaryMarket implements Runnable,ConstantsI {
	private Set<Map<String,String>> resultSet;
	private final String sessionId;
	
	boolean postingIsSucessful = true; //for structure purpose
	
	public PrimaryMarket(String sessionId) {
        this.sessionId = sessionId;
    }
	
	@Override
	public void run() {
		closeTbMarketWindow();
		processTbWorkitemsOnTreasuryUtilityWS();
		processTbFailedBids();
		processSuccessfulBids();
		
	}
	
	 //Update cutoff flag / close flag
	 private void closeTbMarketWindow(){
	        resultSet = new Controller().getRecords(Query.getTbOpenWindowQuery());
	        System.out.println(resultSet);
	        for (Map<String ,String> result : resultSet){
	        	 String date = result.get("CLOSEDATE");
	        
	        	 	if (Commons.compareDate(date)) {
	        	        String id = result.get("REFID"); 
	        	        String value = "'"+flag+"'";
	        	        String condition = "refid = '"+id+"'";
	        	        new Controller().updateRecords(sessionId, Query.setupTblName, Query.stColCloseFlag, value, condition);
	        	        //Send Mail
	        	 	}
	        }
	 }
	 
	 //Debiting  principal
	 private void processTbWorkitemsOnTreasuryUtilityWS() {
		    String columns = "TB_DECISION, FAILEDATTREASURYFLG";
		    String attribute = "<TB_UTILITYFLAG>Y</TB_UTILITYFLAG>";
		    resultSet = new Controller().getRecords(Query.getTbWorkitemsOnTreasuryUtilityWS()); //get all workitems at Treasury_Utility workstep
    
		    for (Map<String, String> result : resultSet) {
		    	
		    	
		    	String custAcctNo = result.get(tbCustAcctNo);
		    	String id = result.get(wiName);
		    	String tbStatus = result.get("TB_STATUS"); //create this variable on MoneyMarket_EXT
                String custSol = result.get("");
                String custPrincipal = result.get("");
                String branchSol = result.get("");
		    	
                //Unlien customer's principal
		        //debit customer with the principal value 
                if (postingIsSucessful) {
                	String values = "'Approve', 'N'";
                    String condition = "winame = '"+id+"'";
                	new Controller().updateRecords(sessionId, Query.extTblName, columns, values, condition); //change decision to approve and set flag to N 
                	new CompleteWorkItem(sessionId,id); //complete workitem
                }
                else {
                	String values = "'Reject', 'Y'";
                    String condition = "winame = '"+id+"'";
                	new Controller().updateRecords(sessionId, Query.extTblName, columns, values, condition); //change decision to reject and set flag to Y
                	new CompleteWorkItem(sessionId,id); //complete workitem
                }
                 
		    }
		    resultSet = new Controller().getRecords(Query.getTbfailedAtTUtilWiCreatedFlg());
		    for (Map<String, String> result : resultSet) {
		    	String failedAtTUtilWCreatedFlg = result.get(failedAtTUtilWiCreatedFlg);
		    	if(failedAtTUtilWCreatedFlg.equalsIgnoreCase("N")) {
		    		String wiName = new CreateWorkItem(sessionId,attribute,initiateFlagNo).getCreatedWorkItem(); //create a new workitem to be routed to Treasury_Officer_verifier queue --should have the marketrefid.
		    			
		    	}
		    }
		    	 
	 }
	 
	 //Processing Primary Market Failed Bids  - for workitems with bidstatus -Failed, reverse the initial principal debit.
	 private void processTbFailedBids() {
	    	//String columnS = "TB_CUSTACTREVERSEDFLG";
	    	String columnF = "TB_CUSTPRNCPLREVERSEDFLG";
	    	String wiName = empty;
	    	resultSet = new Controller().getRecords(Query.getTbAllocatedPrimaryBids("Failed")); //get all workitems with "Failed" bidstatus
	    	for (Map<String,String> result : resultSet){
	    		String custAcctNo = result.get(tbCustAcctNo);
		    	String id = result.get(wiName);
                String custSol = result.get("");
                String custPrincipal = result.get("");
                String branchSol = result.get("");

	             //perform reversal
                //Send notification
	             if (postingIsSucessful) {  
	             String values = "'Y'";
	             String condition = "winame = '"+id+"'";
	             new Controller().updateRecords(sessionId,Query.extTblName,columnF,values,condition);         
	             }
	             else {
	            	 String values = "'N'";
	            	 String condition = "winame = '"+id+"'";
	            	 new Controller().updateRecords(sessionId,Query.extTblName,columnF,values,condition);
	             }
	        }
	    }
	 
	//Processing Primary Market Successful Bids  - for workitems with bidstatus -Success, reverse the initial principal debit.
	 private void processSuccessfulBids() {
	    	//String columnS = "TB_CUSTACTREVERSEDFLG";
	    	String columnF = "TB_CUSTPRNCPLREVERSEDFLG";
	    	String attribute = "FAILEDBID";
	    	resultSet = new Controller().getRecords(Query.getTbAllocatedPrimaryBids("Success")); //get all workitems with Success bidstatus
	    	for (Map<String,String> result : resultSet){
	    		String custAcctNo = result.get(tbCustAcctNo);
		    	String id = result.get(wiName);
                String custSol = result.get("");
                String custPrincipal = result.get("");
                String branchSol = result.get("");

	             //perform reversal - debit approved bid based on metrics and Instruction type
                 // send notification
                
	             if (postingIsSucessful) {  
	             String values = "'Y'";
	             String condition = "winame = '"+id+"'";
	             new Controller().updateRecords(sessionId,Query.extTblName,columnF,values,condition); 
	             }
	             else {
	            	 String values = "'N'";
	            	 String condition = "winame = '"+id+"'";
	            	 new Controller().updateRecords(sessionId,Query.extTblName,columnF,values,condition);
	             }
	             new CompleteWorkItem(sessionId,id); //complete workitem
	             
	             String wiName = new CreateWorkItem(sessionId,attribute,initiateFlagNo).getCreatedWorkItem(); //create Attribute
            }	    	
	  }

		 

}
