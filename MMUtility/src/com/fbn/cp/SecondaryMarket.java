package com.fbn.cp;

import java.util.Map;

import com.fbn.api.newgen.customservice.CompleteWorkItem;
import com.fbn.api.newgen.controller.Controller;
import com.fbn.api.newgen.customservice.CreateWorkItem;
import com.fbn.utils.*;

public class SecondaryMarket extends Commons implements ConstantsI {
    private  final String sessionId;
    private String postResp;
    public SecondaryMarket(String sessionId) {
        this.sessionId = sessionId;
    }


    public void main() {
        closeCpMarketWindow();
    	processBidsOnAwaitingMaturity();
    	processMatureSmBids();
    	processPostFailedMatureSmBids();
    }

    private void closeCpMarketWindow(){
        resultSet = new Controller().getRecords(Query.getCpOpenWindowQuery(secondaryMarket));
        if (!resultSet.isEmpty()) {
            for (Map<String, String> result : resultSet) {
                String date = result.get("CLOSEDATE");
                System.out.println(date);
                String wiName = result.get("WINAME");
                System.out.println(wiName);
                String id = result.get("REFID");
                System.out.println(id);
                String value = "'" + flag + "'";
                String condition = "refid = '" + id + "'";

                if (Commons.checkClosedDate(date)) {
                    new Controller().updateRecords(sessionId, Query.setupTblName, Query.stColCloseFlag, value, condition);
                    new CompleteWorkItem(sessionId, wiName, "CLOSEFLAG", "Y");
                    closeSmInvestmentWindow(id);
                    break;
                }
            }
        }
    }
 
    private void closeSmInvestmentWindow(String id) {
        resultSet = new Controller().getRecords(Query.getCpInvestmentCloseDateQuery(id));
        if (!resultSet.isEmpty()) {
            String columns = "CLOSEDFLAG, STATUS, MATURED";
            for (Map<String, String> result : resultSet) {
                String date = result.get(investClosedate.toUpperCase());

                if (Commons.checkClosedDate(date)) {
                    String investmentId = result.get(investID.toUpperCase());
                    String values = "'Y', 'Closed', 'Y'";
                    String condition = "INVESTMENTID = '" + investmentId + "'";
                    new Controller().updateRecords(sessionId, Query.investmentTblName, columns, values, condition);

                }
            }
        }
    }

    private void processBidsOnAwaitingMaturity() {
        resultSet = new Controller().getRecords(Query.getCpProcessBidsOnAwaitingMaturity(secondaryMarket));
        if (!resultSet.isEmpty()) {
            String mailMessageB = "Kindly be informed that your Commercial Paper request initiated from your branch on lien has matured, liaise with the customer to perfect your obligations to enable you access your funds.<br>Thanks for choosing First bank.";
            String mailMessageC = "Kindly be informed that your Commercial Paper on Lien is 7 days to maturity, liaise with your branch to perfect your obligations to enable you access your funds.";
            String attribute = "MATURED";
            String mailSubject = "MONEY MARKET NOTIFICATION - COMMERCIAL PAPER ";


            for (Map<String, String> result : resultSet) {
                String id = result.get(bidCustIdCol);
                String bidWiName = result.get(bidWinameCol.toUpperCase());
                String cusEmail = result.get(bidCustEmail.toUpperCase());
                String branchSol = result.get(bidBranchSolCol.toUpperCase());
                String matureDate = result.get(bidmaturityDate);
                String lienFlag = result.get(bidlienflag);
                if (Commons.isDaysToMaturity(matureDate, 7) && lienFlag.equalsIgnoreCase("Y")) {
                    //send mail to Money_Market_Branch Initiator and Money_Market_Branch_Verifier
                    new MailSetup(sessionId, bidWiName, fbnMailer, Commons.getUsersMailsInGroup("TUSERS_'" + branchSol + "'"), Commons.getUsersMailsInGroup("TUSERS_'" + branchSol + "'"), mailSubject, mailMessageB);
                    //send mail to Customer
                    new MailSetup(sessionId, bidWiName, fbnMailer, cusEmail, empty, mailSubject, mailMessageC);
                } else {
                    if (Commons.isMatured(matureDate) && lienFlag.equalsIgnoreCase("N")) {
                        String column = "STATUS,MATUREDFLAG";
                        String value = "'Matured', 'Y'";
                        String condition = "CUSTREFID = '" + id + "'";
                        new Controller().updateRecords(sessionId, Query.bidTblName, column, value, condition);
                        new CompleteWorkItem(sessionId, bidWiName, attribute, flag);
                    }
                }
            }
        }
    }

    private void processMatureSmBids(){
        resultSet = new Controller().getRecords(Query.getCpMaturedBids(secondaryMarket));
        if (!resultSet.isEmpty()) {
            String mailSubject = "MONEY MARKET NOTIFICATION - COMMERCIAL PAPER ";
            String columnsS = "POSTINTEGRATIONMATUREFLAG,PAIDFLAG";
            String columnsF = "POSTINTEGRATIONMATUREFLAG,FAILEDPOSTFLAG";
            String attribute = "MATURED";
            for (Map<String, String> result : resultSet) {
                String id = result.get(bidCustIdCol);
                String wiName = result.get(bidWinameCol);
                String cusAcc = result.get(bidCustAcctNoCol);
                String cusSol = result.get(bidCustSolCol);
                String cusMail = result.get(bidCustEmail);
                String principal = result.get(bidCustPrincipalCol);
                String rate = result.get(bidRate);
                String principalAtMaturity = result.get(bidPrincipalMaturityCol.toUpperCase());
                String interest = result.get(bidInterestCol.toUpperCase());
                String investmentType = result.get(bidInvestmentTypeCol.toUpperCase());
                String tranPart = "CP/" + id.toUpperCase() + "/MATUREDBID";

                String values = "'Y', 'Y'";
                String condition = "CUSTREFID = '" + id + "'";

                if (investmentType.equalsIgnoreCase(investmentTypePrincipalInterest))
                    principalAtMaturity = String.valueOf(Double.parseDouble(principalAtMaturity) + Double.parseDouble(interest));
                postResp = IntegrationCall.postTransaction(LoadProp.headOfficeCpAcctNo, LoadProp.headOfficeCpSol, principalAtMaturity, tranPart, wiName, cusAcc, cusSol);
                if (postResp != null) {
                    if (postingIsSuccessful(postResp)) {
                        new Controller().updateRecords(sessionId, Query.bidTblName, columnsS, values, condition);
                        new CompleteWorkItem(sessionId, wiName, attribute, flag);
                        String mailMessageS = "";
                        new MailSetup(sessionId, wiName, fbnMailer, Commons.getUsersMailsInGroup("TUSERS"), empty, mailSubject, mailMessageS);
                    } else if (postingNotSuccessful(postResp)) {
                        new Controller().updateRecords(sessionId, Query.bidTblName, columnsF, values, condition);
                        String mailMessageF = "";
                        new MailSetup(sessionId, wiName, fbnMailer, Commons.getUsersMailsInGroup("TUSERS"), empty, mailSubject, mailMessageF);
                    }
                }
            }
        }
    }
    private void processPostFailedMatureSmBids(){
        resultSet = new Controller().getRecords(Query.getCpPostFailMaturityBids(secondaryMarket));
        if (!resultSet.isEmpty()) {
            String attribute = "<CP_UTILITYFLAG>M</CP_UTILITYFLAG><G_SELECT_MARKET>cp_market</G_SELECT_MARKET><CP_SELECT_MARKET>" + secondaryMarket + "</CP_SELECT_MARKET>";
            String wiName = new CreateWorkItem(sessionId, attribute, initiateFlagNo).getCreatedWorkItem();
            String column = "FAILEDTRANUTILITYWINAME,FAILEDPOSTFLAG";
            String value = "'" + wiName + "','T'";


            for (Map<String, String> result : resultSet) {
                String id = result.get(bidCustIdCol.toUpperCase());
                String condition = "CUSTREFID = '" + id + "'";
                new Controller().updateRecords(sessionId, Query.bidTblName, column, value, condition);
            }
            new CompleteWorkItem(sessionId, wiName);
        }
    }
    private boolean postingIsSuccessful (String data){
        return data.equalsIgnoreCase(apiSuccess);
    }
    private boolean postingNotSuccessful (String data){
        return data.equalsIgnoreCase(apiFailed);
    }

}
