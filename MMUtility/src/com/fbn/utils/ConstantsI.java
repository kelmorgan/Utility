package com.fbn.utils;


public interface ConstantsI {
	
	//Variables

	String apiSuccess = "SUCCESS";
	String apiFailed = "FAILED";
	String apiFailure = "FAILURE";
	String False = "false";
	String apiStatus = "Status";
	String currencyNgn = "NGN";
	String debitFlag = "D";
	String creditFlag = "C";
	String transType = "T";
	String transSubTypeC = "CI";
	String transSubTypeB = "BI";
	String apiNoResponse = "No Response Found";

	//String wiName = "FBN-00000000000134-MMW";
	String dbDateTimeFormat = "yyyy-MM-dd HH:mm:ss";
	String flag = "Y";
	String initiateFlagYes = "Y";
	String initiateFlagNo = "N";
	String empty = "";
	String bidCustIdCol = "CUSTREFID";
	String bidCustSolCol = "CUSTSOL";
	String bidCustAcctNoCol = "CUSTACCTNO";
	String bidCustPrincipalCol = "CUSTPRINCIPAL";
	String bidBranchSolCol = "BRANCHSOL";
	String bidFailedFlagCol = "FAILEDFLAG";
	String bidMarketTypeCol = "MARKETTYPE";
	String bidAllocatedFlagCol = "ALLOCATEDFLAG";
	String bidWinameCol = "BIDWINAME";
	String bidmaturityDate = "MATURITYDATE";
	String bidlienflag = "LIENFLAG";
	String bidAllocationPercentageCol = "ALLOCATIONPERCENTAGE";
	String investClosedate = "closedate"; 
	String investID = "investmentid";
	String cpPrimaryMarket = "primary";
	String cpSecondaryMarket = "secondary";

	String tbCustAcctNo = "TB_CUSTACCTNUM";
	String wiName = "WINAME";
	String failedAtTUtilWiCreatedFlg = "FAILEDATTUTILWICREATEDFLG";
	
	//CONFIG PROPERTIES
	String appServerIp = "172.16.249.62";
	String cabinetName = "fbnibpsuatcab";
	String processDefId = "46";
	String appServerType = "JTS";
	String appServerPort = "2809";
	int wrapperPort = 3333;
	String userName = "FBNTreasuryUtilityUser";
	String password = "system12345#";
	String queueId = "1143";
	String appServerIpField = "ServerIp";
	String appSocketPortField = "SocketPort";
	String configPath = "C:\\Users\\sn029154\\Documents\\Work\\Project\\util\\config\\mmutility.properties";
	String logPath = "C:\\Users\\sn029154\\Documents\\Work\\Project\\util\\logs";


	// API SERVICE NAMES
	String postServiceName = "postRequestToFinacle";
	String fetchOdaAcctServiceName ="CURRENTACCOUNT";
	String fetchCaaAcctServiceName ="SPECIALACCOUNT";
	String fetchSbaAcctServiceName ="SAVINGACCOUNT";
	String fetchLienServiceName = "FETCHLIEN";
	String fetchLimitServiceName = "CIGETUSERLIMIT";
	String searchTranServiceName = "CISEARCHTRANSACTION";
	String tokenValidationServiceName = "TOKENVALIDATION";
	
}
