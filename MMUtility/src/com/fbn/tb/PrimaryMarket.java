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
	        
	        	 	if (Commons.compareDateTime(date)) {
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
		    
		    String column = "FAILEDATUTILWICREATEDFLG";
		    String value = "Y";
		    
		    resultSet = new Controller().getRecords(Query.getTbfailedAtTUtilWiCreatedFlg());
		    for (Map<String, String> result : resultSet) {		    	
		    	String failedAtTUtilWCreatedFlg = result.get(failedAtTUtilWiCreatedFlg);
		    	String id = result.get("REFID");
		    	
		    	if(failedAtTUtilWCreatedFlg.equalsIgnoreCase("N")) {
		    		String attributes = "<TB_UPMWU_REF>'"+id+"'</TB_UPMWU_REF><TB_FAILEDATTREASURYUTILITYFLG>Y</TB_FAILEDATTREASURYUTILITYFLG>";
		    		String wiName = new CreateWorkItem(sessionId,attributes,initiateFlagNo).getCreatedWorkItem(); //create a new workitem to be routed to Treasury_Officer_verifier queue --should have the marketrefid.
		    		String condition = "refid = '"+id+"'";
		    		new Controller().updateRecords(sessionId, Query.setupTblName, column, value, condition);	
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
	                   
            }
	    	 String column = "FAILEDRVSALSTOPSWICREATEDFLG";
	 		 String value = "Y";
	 		    
	 		 resultSet = new Controller().getRecords(Query.getTbfailedRvsalsTOpsWiCreatedFlg());
	 		 for (Map<String, String> result : resultSet) {		    	
	 		    	String failedAtTUtilWCreatedFlg = result.get(failedAtTUtilWiCreatedFlg);
	 		    	String id = result.get("REFID");
	 		    	
	 		    	if(failedAtTUtilWCreatedFlg.equalsIgnoreCase("N")) {
	 		    		String attributes = "<TB_UPMWU_REF>'"+id+"'</TB_UPMWU_REF><TB_REVERSALFAILEDATAPPRVDBIDSFLG>Y</TB_REVERSALFAILEDATAPPRVDBIDSFLG>";
	 		    		String wiName = new CreateWorkItem(sessionId,attributes,initiateFlagNo).getCreatedWorkItem(); //create a new workitem to be routed to Treasury_Officer_verifier queue --should have the marketrefid.
	 		    		String condition = "refid = '"+id+"'";
	 		    		new Controller().updateRecords(sessionId, Query.setupTblName, column, value, condition);	
	 		    	}
	 		 }
	  }
	 
	 //Moving workitems from awaiting maturity workstep to Matured workstep
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
		    			}	    			
		    		}
		    	}
		 }	 
	 }
	 
	 
	 private void processAllPmBidsOnTreasuryOpsMaturity(){
		 String column = "failedMatrtyPostTVerWiCreatedFlg";
		 resultSet = new Controller().getRecords(Query.getTbProcessBidsOnAwaitingMaturity());
		 for (Map<String, String> result : resultSet) {
			 String id = result.get(wiName);
			 String lienStatus = result.get("");
			 if(lienStatus == "N") {
				 //perform posting credit the principal or the interest to the customer based on ***
				 if (postingIsSucessful) {			 
				 }
				 else {
					 String value = "'N'";
	            	 String condition = "winame = '"+id+"'";
	            	 new Controller().updateRecords(sessionId,Query.extTblName,column,value,condition);
	            	 
				 }
				 new CompleteWorkItem(sessionId,wiName);
				 //send mail
				 String columnF = "FAILEDATUTILWICREATEDFLG";
				 String value = "Y";
				 Set<Map<String,String>> resultSets = new Controller().getRecords(Query.getfailedMatrtyPostTVerWiCreatedFlg());
				 for (Map<String, String> result1 : resultSets) {		    	
				    	String failedMatrtyPostTVerWiCreatedFlg = result.get("");
				    	String refid = result.get("REFID");	    	
				    	if(failedMatrtyPostTVerWiCreatedFlg.equalsIgnoreCase("N")) {
				    		String attributes = "<TB_UPMWU_REF>'"+id+"'</TB_UPMWU_REF><TB_FAILEDMATRTYPOSTTVERWICREATEDFLG>Y</TB_FAILEDMATRTYPOSTTVERWICREATEDFLG>";
				    		String wiName = new CreateWorkItem(sessionId,attributes,initiateFlagNo).getCreatedWorkItem(); //create a new workitem to be routed to Treasury_Officer_verifier queue --should have the marketrefid.
				    		String condition = "refid = '"+id+"'";
				    		new Controller().updateRecords(sessionId, Query.setupTblName, column, value, condition);	
				    	}
				 }			 
			 }		 
		 }	 
	 }

}
