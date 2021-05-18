package com.fbn.cp;

import com.fbn.api.fbn.controller.Controller;
import com.fbn.utils.Commons;
import com.fbn.utils.ConstantsI;

public class IntegrationCall implements ConstantsI {
    private static String searchResp;
    private static String postResp;
    private static final String startDate = Commons.monthsFromNow(6);
    private static final String endDate = Commons.getCurrentDate();
    
    public static String postTransaction(String debitAcct,String debitSol,String amount,String transParts,String remarks,String creditAcct,String creditSol){
        searchResp = new Controller().getSearchTxn(startDate,endDate,debitAcct,amount,debitFlag,transParts);
        if (isSearchSuccess(searchResp)){
            postResp = new Controller().getPostTxn(debitAcct,debitSol,amount,transParts,remarks,Commons.getCurrentDate(),creditAcct,creditSol);
            if(isPostSuccess(postResp)) return apiSuccess;
            else return apiFailed;
        }
        return empty;
    }

    public static String postSuccessBids(String debitAcct,String debitSol,String amount,String transParts1,String transPart2,String remarks,String creditAcct,String creditSol,String allocationPercentage){
       //Posting reversal to customer

        searchResp = new Controller().getSearchTxn(startDate,endDate,debitAcct,amount,debitFlag,transParts1);
        if (isSearchSuccess(searchResp)){
            postResp = new Controller().getPostTxn(debitAcct,debitSol,amount,transParts1,remarks,Commons.getCurrentDate(),creditAcct,creditSol);
            if(!isPostSuccess(postResp)) return apiFailed;
        

        // Posting adjusted investment
            String investmentCapital = String.valueOf( Double.parseDouble(amount) * ( Double.parseDouble(allocationPercentage)/100));
            String debitCusAcct = creditAcct;
            String creditCpAcct = debitAcct;
            String creditCpSol = debitSol;
            String debitCusSol = creditSol;
        String searchResp2 = new Controller().getSearchTxn(startDate,endDate,debitCusAcct,investmentCapital,debitFlag,transPart2);

        if (isSearchSuccess(searchResp2)){
            String postResp2 = new Controller().getPostTxn(debitCusAcct,debitCusSol,investmentCapital,transPart2,remarks,Commons.getCurrentDate(),creditCpAcct,creditCpSol);
            if (isPostSuccess(postResp2)) return apiSuccess;
            else return apiFailure;
        }
        }
        return empty;
    }

    private static boolean isSearchSuccess(String data){
        return data.equalsIgnoreCase(False);
    }
    private static boolean isPostSuccess(String data){
        return data.equalsIgnoreCase(apiSuccess);
    }
}
