package com.fbn.cp;

import com.fbn.api.newgen.customservice.CompleteWorkItem;
import com.fbn.api.newgen.controller.Controller;
import com.fbn.api.newgen.customservice.CreateWorkItem;
import com.fbn.utils.Commons;
import com.fbn.utils.ConstantsI;
import com.fbn.utils.LoadProp;
import com.fbn.utils.Query;
import com.fbn.utils.MailSetup;
import java.util.Map;


public class PrimaryMarket extends Commons implements Runnable,ConstantsI {

    private String postResp;

    public PrimaryMarket(String sessionId) {
        this.sessionId = sessionId;
    }

    private final String sessionId;

    @Override
    public void run() {
    	closeCpMarketWindow();
        processPrimaryBids();
        processFailedBids();
        processPostingFailureFailedBids();
        processSuccessfulBids();
        processPostingFailureSuccessBids();
    }
    

    private void closeCpMarketWindow(){
        resultSet = new Controller().getRecords(Query.getCpOpenWindowQuery(primaryMarket));
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

    private void  processPrimaryBids(){
    	
        String attribute = "<CP_UTILITYFLAG>Y</CP_UTILITYFLAG>";
        String wiName = new CreateWorkItem(sessionId,attribute,initiateFlagNo).getCreatedWorkItem();
        String mailSubject = "MONEY MARKET NOTIFICATION - COMMERCIAL PAPER ";
    	String mailMessage = "Commercial Paper Primary Bids with RefNumber: '"+wiName+"' are ready for processing.<br>Kindly login iBPS treat.";
        
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
       //send mail to TUSer
       new MailSetup(sessionId,wiName,fbnMailer,Commons.getUsersMailsInGroup("TUSERS"),empty,mailSubject,mailMessage);
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
    	String columnsS = "POSTINTEGRATIONFLAG,REVERSALFLAG";
    	String columnsF = "POSTINTEGRATIONFLAG,FAILEDPOSTFLAG";
    	String wiName;
    	String attribute = "FAILEDBID";
    	String mailSubject = "MONEY MARKET NOTIFICATION - COMMERCIAL PAPER ";
    	resultSet = new Controller().getRecords(Query.getCpAllocatedPrimaryBids("Y"));
    	for (Map<String,String> result : resultSet){
            String id = result.get(bidCustIdCol.toUpperCase());
            wiName = result.get(bidWinameCol.toUpperCase());
            String cusSol = result.get(bidCustSolCol.toUpperCase());
            String cusPrincipal = result.get(bidCustPrincipalCol.toUpperCase());
            String branchSol = result.get(bidBranchSolCol.toUpperCase());
            String cusAcctNo = result.get(bidCustAcctNoCol.toUpperCase());
            String cusEmail = result.get(bidCustEmail.toUpperCase());
            String bidWiname = result.get(bidWinameCol.toUpperCase());
            String tranPart ="CP/"+id.toUpperCase()+"/FAILEDBID";
            String values = "'Y', 'Y'";
            String condition = "CUSTREFID = '"+id+"'";
            postResp = IntegrationCall.postTransaction(LoadProp.headOfficeCpAcctNo,LoadProp.headOfficeCpSol,cusPrincipal,tranPart,wiName,cusAcctNo,cusSol);
            if (postingIsSuccessful(postResp)) {
                new Controller().updateRecords(sessionId,Query.bidTblName,columnsS,values,condition);
                new CompleteWorkItem(sessionId,wiName,attribute,flag);
                //sentMail
                String mailMessageS = "A successful reversal request for failed Primary Market Commercial Paper with number '"+wiName+"' now being reversed successfully.";
                new MailSetup(sessionId,bidWiname,fbnMailer,Commons.getUsersMailsInGroup("TUSERS"),empty,mailSubject,mailMessageS);
             }
             else if (postingNotSuccessful(postResp)) {
                new Controller().updateRecords(sessionId,Query.bidTblName,columnsF,values,condition);
                //sentMail
                String mailMessageF = "A reversal request for failed Primary Market Commercial Paper with number '"+wiName+"' has failed posting.<br>Please log into the iBPS workflow to execute action ";
                new MailSetup(sessionId,bidWiname,fbnMailer,Commons.getUsersMailsInGroup("TUSERS"),empty,mailSubject,mailMessageF);
             }
        }
    }
    
    
    private void processPostingFailureFailedBids() {
    	String mailSubject = "MONEY MARKET NOTIFICATION - COMMERCIAL PAPER ";
    	String attribute = "<CP_UTILITYFLAG>F</CP_UTILITYFLAG>";
    	String wiName = new CreateWorkItem(sessionId,attribute,initiateFlagNo).getCreatedWorkItem();
    	String column = "FAILEDTRANUTILITYWINAME";
    	String value = "'"+wiName+"'";
    	
    	resultSet = new Controller().getRecords(Query.getCpProcessPostingFailureFailedBids(flag));
        for (Map<String,String> result : resultSet){
        	String id = result.get(bidCustIdCol.toUpperCase());
        	String condition = "CUSTREFID = '"+id+"'";
        	new Controller().updateRecords(sessionId,Query.bidTblName,column,value,condition);
        }
 
    	new CompleteWorkItem(sessionId,wiName);
    	 //send mail to TUSer
    	String mailMessage = "Kindly login to post transactions Utility failed to post for failed Primary Market Commercial Paper with Workitem number '"+wiName+"'";
        new MailSetup(sessionId,wiName,fbnMailer,Commons.getUsersMailsInGroup("TUSERS"),empty,mailSubject,mailMessage);
    }
    
    private void processSuccessfulBids() {
    	String columnS = "POSTINTEGRATIONFLAG,AWAITINGMATURITYFLAG";
    	String columnsF = "POSTINTEGRATIONFLAG,FAILEDPOSTFLAG";
    	String wiName = "";
    	String attribute = "SUCCESSBID";
    	resultSet = new Controller().getRecords(Query.getCpAllocatedPrimaryBids("N"));
    	for (Map<String,String> result : resultSet){
            String id = result.get(bidCustIdCol.toUpperCase());
            wiName = result.get(bidWinameCol.toUpperCase());
            String cusSol = result.get(bidCustSolCol.toUpperCase());
            String cusPrincipal = result.get(bidCustPrincipalCol.toUpperCase());
            String branchSol = result.get(bidBranchSolCol.toUpperCase());
            String allocationPercentage = result.get(bidAllocationPercentageCol.toUpperCase());
            String cusAcctNo = result.get(bidCustAcctNoCol.toUpperCase());
            String condition = "CUSTREFID = '"+id+"'";
            String tranPart1 ="CP/"+id.toUpperCase()+"/REVERSAL";
            String tranPart2 ="CP/"+id.toUpperCase()+"/SUCCESSBID";


            postResp = new IntegrationCall().postSuccessBids(LoadProp.headOfficeCpAcctNo,LoadProp.headOfficeCpSol,cusPrincipal,tranPart1,tranPart2,wiName,cusAcctNo,cusSol,allocationPercentage);
            if (postingIsSuccessful(postResp)) {
            String value = "'Y','Y'";
            new Controller().updateRecords(sessionId,Query.bidTblName,columnS,value,condition);
            new CompleteWorkItem(sessionId,wiName,attribute,flag);
            }
            else if (postResp.equalsIgnoreCase(apiFailed)){
           	     String value = "'Y','C'";  
           	     new Controller().updateRecords(sessionId,Query.bidTblName,columnsF,value,condition);
    	    }
            else if (postResp.equalsIgnoreCase(apiFailure)){
          	     String value = "'Y','D'";  
          	     new Controller().updateRecords(sessionId,Query.bidTblName,columnsF,value,condition);
   	    }
         } 
    }
     

    private void processPostingFailureSuccessBids() {
    	String mailSubject = "MONEY MARKET NOTIFICATION - COMMERCIAL PAPER";
    	String attribute = "<CP_UTILITYFLAG>S</CP_UTILITYFLAG>";
    	String wiName = new CreateWorkItem(sessionId,attribute,initiateFlagNo).getCreatedWorkItem();
    	String column = "FAILEDTRANUTILITYWINAME";
    	String value = "'"+wiName+"'";
    	
    	resultSet = new Controller().getRecords(Query.getCpProcessPostingFailureSuccessBids("N"));
    	for (Map<String,String> result : resultSet){
        	String id = result.get(bidCustIdCol.toUpperCase());
        	String condition = "CUSTREFID = '"+id+"'";
        	new Controller().updateRecords(sessionId,Query.bidTblName,column,value,condition);
    	}
    	new CompleteWorkItem(sessionId,wiName);
    	//send mail to TUSer
    	String mailMessage = "Kindly login to post transactions Utility failed to post for Success bids Primary Market Commercial Paper with Workitem number '"+wiName+"'";
        new MailSetup(sessionId,wiName,fbnMailer,Commons.getUsersMailsInGroup("TUSERS"),empty,mailSubject,mailMessage);
    }
    

    
    private void processBidsOnAwaitingMaturity() {
    	String mailMessageB = "Kindly be informed that your Commercial Paper request initiated from your branch on lien has matured, liaise with the customer to perfect your obligations to enable you access your funds.<br>Thanks for choosing First bank.";
    	String mailMessageC = "Kindly be informed that your Commercial Paper on Lien is 7 days to maturity, liaise with your branch to perfect your obligations to enable you access your funds.";
    	String attribute = "MATURED";
    	String mailSubject = "MONEY MARKET NOTIFICATION - COMMERCIAL PAPER ";
   
    	resultSet = new Controller().getRecords(Query.getCpProcessBidsOnAwaitingMaturity());
    	for (Map<String, String> result : resultSet) {
			 	String id = result.get(bidCustIdCol);
			 	String bidWiname = result.get(bidWinameCol.toUpperCase());
			 	String cusEmail = result.get(bidCustEmail.toUpperCase());
			 	String branchSol = result.get(bidBranchSolCol.toUpperCase());
		    	String matureDate = result.get(bidmaturityDate);
		    	String lienFlag = result.get(bidlienflag);
		    	if (Commons.isDaysToMaturity(matureDate,7) && lienFlag == "Y") {
		    		//send mail to Money_Market_Branch Initiator and Money_Market_Branch_Verifier 
		    		new MailSetup(sessionId,bidWiname,fbnMailer,Commons.getUsersMailsInGroup("TUSERS_'"+branchSol+"'"),Commons.getUsersMailsInGroup("TUSERS_'"+branchSol+"'"),mailSubject,mailMessageB);
		    		//send mail to Customer
		    		new MailSetup(sessionId,bidWiname,fbnMailer,cusEmail,empty,mailSubject,mailMessageC);
		    	}
		    	else {
		    		if(Commons.isMatured(matureDate) && lienFlag.equalsIgnoreCase("N")) {
		    				String column = "STATUS,MATUREDFLAG";
		    				String value = "'Matured', 'Y'";
		    				String condition = "CUSTREFID = '"+id+"'";
		                    new Controller().updateRecords(sessionId, Query.bidTblName, column, value, condition);
		                    new CompleteWorkItem(sessionId,wiName,attribute,flag);
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
