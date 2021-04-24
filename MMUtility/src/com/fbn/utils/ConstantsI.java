package com.fbn.utils;


public interface ConstantsI {
	
	
	String configPath = "";
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
}
