package com.fbn.tb;

import com.fbn.api.newgen.customservice.CompleteWorkItem;
import com.fbn.api.newgen.controller.Controller;
import com.fbn.api.newgen.customservice.CreateWorkItem;
import com.fbn.cp.IntegrationCall;
import com.fbn.utils.Commons;
import com.fbn.utils.ConstantsI;
import com.fbn.utils.LoadProp;
import com.fbn.utils.MailSetup;
import com.fbn.utils.Query;

import java.util.Map;
import java.util.Set;

public class PrimaryMarket extends Commons implements ConstantsI {
	private String postResp;
	private final String sessionId;

	public PrimaryMarket(String sessionId) {
        this.sessionId = sessionId;
    }
	

	public void main() {
		closeTbMarketWindow();
		processTbWorkitemsOnTreasuryUtilityWS();
		processTbFailedBids();
		processSuccessfulBids();
		
	}
	
	 //Update cutoff flag / close flag
	 private void closeTbMarketWindow(){
		    String mailSubject = "MONEY MARKET NOTIFICATION - TREASURY BILLS";
	    	String mailMessage = "";
	        resultSet = new Controller().getRecords(Query.getTbOpenWindowQuery(primaryMarket));
	        System.out.println(resultSet);
	        for (Map<String ,String> result : resultSet){
	        	 String date = result.get("CLOSEDATE");
	        	 String winame = result.get(wiName); 
	        	 	if (Commons.checkClosedDate(date)) {
	        	        String id = result.get("REFID");
	        	        String value = "'"+flag+"'";
	        	        String condition = "refid = '"+id+"'";
	        	        new Controller().updateRecords(sessionId, Query.setupTblName, Query.stColCloseFlag, value, condition);
	        	        //Send Mail to treasury officer
	        	        new MailSetup(sessionId,winame,fbnMailer,Commons.getUsersMailsInGroup("TUSERS"),"",mailSubject,mailMessage);
	        	 	}
	        }
	 }
	 
	 //debit the customer with the principal
	 private void processTbWorkitemsOnTreasuryUtilityWS() {
		    String mailSubject = "MONEY MARKET NOTIFICATION - TREASURY BILLS";
	    	String mailMessage = "";
		    String columns = "TB_DECISION, TB_FAILEDATTREASURYUTILITYFLG";
		    resultSet = new Controller().getRecords(Query.getTbWorkitemsOnTreasuryUtilityWS()); //get all workitems at Treasury_Utility workstep
    
		    for (Map<String, String> result : resultSet) {	
		    	String custAcctNo = result.get(tbCustAcctNo);
		    	String id = result.get(wiName);
		    	String tbStatus = result.get("TB_STATUS");
                String custSol = result.get("_CUSTSOL"); //change to correct field
                String custPrincipal = result.get("_CSPRINCIPAL"); //change to correct field
                String tranPart ="TB/"+id.toUpperCase()+"/PRINCIPAL";
              	
                //Unlien customer's principal
		        //debit customer with the principal value
                postResp = IntegrationCall.postTransaction(custAcctNo,custSol,custPrincipal,tranPart,id,LoadProp.headOfficeTbAcctNo,LoadProp.headOfficeTbSol);
 
                if (postingIsSuccessful(postResp)) {
                	String values = "'Approve', 'N'";
                    String condition = "winame = '"+id+"'";
                	new Controller().updateRecords(sessionId, Query.extTblName, columns, values, condition); //change decision to approve and set flag to N 
                	new CompleteWorkItem(sessionId,id); //complete workitem
                }
                else if (postingNotSuccessful(postResp)) {
                	String values = "'Reject', 'Y'";
                    String condition = "winame = '"+id+"'";
                	new Controller().updateRecords(sessionId, Query.extTblName, columns, values, condition); //change decision to reject and set flag to Y
                	new CompleteWorkItem(sessionId,id); //complete workitem
                	//send an email to Treasury_Officer_verifier
                	new MailSetup(sessionId,id,fbnMailer,Commons.getUsersMailsInGroup("TUSERS"),"",mailSubject,mailMessage);
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
		        String mailSubject = "MONEY MARKET NOTIFICATION - TREASURY BILLS";
	    	    String mailMessage = "";
	    	    //String columnS = "TB_CUSTACTREVERSEDFLG";
	    	    String column = "TB_CUSTPRNCPLREVERSEDFLG";
	    	    resultSet = new Controller().getRecords(Query.getTbAllocatedPrimaryBids("Failed")); //get all workitems with "Failed" bidstatus
	    	    for (Map<String,String> result : resultSet){
	    		String custAcctNo = result.get(tbCustAcctNo);
		    	String id = result.get(wiName);
		    	String cusEmail = result.get("_CUSTEMAIL"); //change to the correct field
                String custSol = result.get("_CUSTSOL"); //change to the correct field
                String custPrincipal = result.get("_CUSTPRINCIPAL"); //change to the correct field
                String tranPart ="TB/"+id.toUpperCase()+"/FAILEDBID";

	            //perform reversal
                postResp = IntegrationCall.postTransaction(LoadProp.headOfficeTbAcctNo,LoadProp.headOfficeTbSol,custPrincipal,tranPart,id,custAcctNo,custSol);
                
                //Send notification to customer
                new MailSetup(sessionId,id,fbnMailer,cusEmail,Commons.getUsersMailsInGroup("TUSERS"),mailSubject,mailMessage);
                
                if (postingIsSuccessful(postResp)) { 
	             String values = "'Y'";
	             String condition = "winame = '"+id+"'";
	             new Controller().updateRecords(sessionId,Query.extTblName,column,values,condition);         
	             }
	             else {
	            	 String values = "'N'";
	            	 String condition = "winame = '"+id+"'";
	            	 new Controller().updateRecords(sessionId,Query.extTblName,column,values,condition);
	             }
	        }
	    }
	 
	//Processing Primary Market Successful Bids  - for workitems with bidstatus -Success, reverse the initial principal debit.
	 private void processSuccessfulBids() {
		    String mailSubject = "MONEY MARKET NOTIFICATION - TREASURY BILLS";
 	        String mailMessage = "";
	    	//String columnS = "TB_CUSTACTREVERSEDFLG";
	    	String column1 = "TB_CUSTPRNCPLREVERSEDFLG";
	    	String columnC = "tb_custBidDebitedFlg";
	    	String attribute = "FAILEDBID";
	    	resultSet = new Controller().getRecords(Query.getTbAllocatedPrimaryBids("Success")); //get all workitems with Success bidstatus
	    	for (Map<String,String> result : resultSet){
	    		String custAcctNo = result.get(tbCustAcctNo);//change the field to the correct name
		    	String id = result.get(wiName);
		    	String cusEmail = result.get("_CUSTEMAIL");
                String custSol = result.get(""); //change the field to the correct name
                String custPrincipal = result.get("_CUSTPRINCIPAL"); //change the field to the correct name
                String allocationPercentage = result.get("_ALLPERCENTAGE"); //change the field to the correct name
                String tranPart1 ="TB/"+id.toUpperCase()+"/REVERSAL";
                String tranPart2 ="TB/"+id.toUpperCase()+"/SUCCESSBID";
                
                String condition = "winame = '"+id+"'";

                postResp = IntegrationCall.postSuccessBids(LoadProp.headOfficeTbAcctNo,LoadProp.headOfficeTbSol,custPrincipal,tranPart1,tranPart2,id,custAcctNo,custSol,allocationPercentage);
                if (postingIsSuccessful(postResp)) {
                 // send notification to customer
                 new MailSetup(sessionId,id,fbnMailer,cusEmail,Commons.getUsersMailsInGroup("TUSERS"),mailSubject,mailMessage);
	             String value = "'Y'";           
	             new Controller().updateRecords(sessionId,Query.extTblName,column1,value,condition); 
	             }
                 else if (postResp.equalsIgnoreCase(apiFailed)){
              	     String value = "'C'";  
              	     new Controller().updateRecords(sessionId,Query.extTblName,columnC,value,condition);
              	     new CompleteWorkItem(sessionId,id); //complete workitem      	     
       	          }
                 else if (postResp.equalsIgnoreCase(apiFailure)){
              	     String value = "'D'";  
              	     new Controller().updateRecords(sessionId,Query.extTblName,columnC,value,condition);
              	     new CompleteWorkItem(sessionId,id); //complete workitem 
       	         }
	             else {
	            	 String values = "'N'";
	            	 new Controller().updateRecords(sessionId,Query.extTblName,column1,values,condition);
	            	 new CompleteWorkItem(sessionId,id); //complete workitem
	             }
	                   
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
		 String mailSubject = "MONEY MARKET NOTIFICATION - TREASURY BILLS";
 	     String mailMessage = "";
		 resultSet = new Controller().getRecords(Query.getTbProcessBidsOnAwaitingMaturity());
		 for (Map<String, String> result : resultSet) {
			 	String id = result.get(wiName);
			 	String cusEmail = result.get("_CUSTEMAIL");
		    	String maturityDate = result.get("");
		    	String lienStatus = result.get("");
		    	String TresaurylienStatus = result.get("");
		    	if (Commons.is7DaysToMaturity(maturityDate) && lienStatus.equalsIgnoreCase("N")) {
		    		//send mail to branch initiator, verifier, customer	
		    		new MailSetup(sessionId,id,fbnMailer,cusEmail,Commons.getUsersMailsInGroup("TUSERS"),mailSubject,mailMessage);
		    	}
		    	else {
		    		if(Commons.isMatured(maturityDate) && lienStatus.equalsIgnoreCase("N")) {
		    			if(TresaurylienStatus.equalsIgnoreCase("N") ) {
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
		 resultSet = new Controller().getRecords(Query.getTbProcessBidsOnTreasuryOperationMaturity());
		 for (Map<String, String> result : resultSet) {
			 String id = result.get(wiName);
			 String lienStatus = result.get("");
			 String cusPrincipal = result.get("");
			 String cusAcctNo = result.get("");
			 String cusSol = result.get("");
			 String tranPart ="TB/"+id.toUpperCase()+"/MATURED";
			 if(lienStatus.equalsIgnoreCase("N")) {
				 
				 //perform posting credit the principal or the interest to the customer based on *
				 postResp = IntegrationCall.postTransaction(LoadProp.headOfficeCpAcctNo,LoadProp.headOfficeCpSol,cusPrincipal,tranPart,id,cusAcctNo,cusSol);
				 
				 if (postingIsSuccessful(postResp)) {			 
				 }
				 else {
					 String value = "'N'";
	            	 String condition = "winame = '"+id+"'";
	            	 new Controller().updateRecords(sessionId,Query.extTblName,column,value,condition);
	            	 //send email
	            	 
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
	 
	 private boolean postingIsSuccessful (String data){
	        return data.equalsIgnoreCase(apiSuccess);
	    }
	    private boolean postingNotSuccessful (String data){
	        return data.equalsIgnoreCase(apiFailed);
	    }

}
