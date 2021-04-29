package com.fbn.utils;

public class Query {
    public static String setupTblName = "mm_setup_tbl";
    public static String bidTblName = "mm_bid_tbl";
    public static String extTblName = "moneymarket_ext";
    public static String investmentTblName = "mm_sminvestments_tbl";
    public static String stColId = "refid";
    public static String stColCloseFlag = "closeflag";
    
	public static String getSetupTblQuery() {
        return  "select * from mm_setup_tbl";
    }
	public static String getBidTblQuery() {
        return  "select * from mm_bid_tbl";
    }
	public static String getCpOpenWindowQuery(String marketType) {
        return  "select closedate , winame,refid from mm_setup_tbl where process = 'Commercial Paper' and markettype = '"+marketType+"' and closeflag = 'N'";
    }
	public static String getTbOpenWindowQuery() {
        return  "select closedate, refid from mm_setup_tbl where process = 'Treasury Bills' and closeflag = 'N'";
    }
    public static String getCpPmBidsToProcessQuery () {
        return "select custrefid, tenor, rate, ratetype from mm_bid_tbl where process = 'Commercial Paper' and markettype= 'primary' and processflag ='N' and groupindexflag = 'N'";
    }
    public static String getTbWorkitemsOnTreasuryUtilityWS() {
    	return "select winame, tb_custAcctNum, tb_custAcctEmail, tb_custAcctEmail, tb_schemecode,tb_BrnchPri_LienID, tb_status from MoneyMarket_ext where g_currws = 'Treasury_Utility' and assign = 'TreasuryUtility'";
    }
    public static String getCpAllocatedPrimaryBids(String flag) {
    	return "select custrefid, bidwiname, custsol,custacctno, custprincipal, branchsol,allocationpercentage from mm_bid_tbl where failedflag = '"+flag+"' and process = 'Commercial Paper' and markettype = 'primary' and allocatedflag ='Y'";
    }
    public static String getTbAllocatedPrimaryBids(String bidstatus) {
    	return "select winame, tb_custAcctNum, tb_custAcctEmail, tb_custAcctEmail, tb_schemecode,tb_BrnchPri_LienID, tb_status from moneymarket_ext where g_currws = 'Approved_Bids_Utility' and bidstatus = '"+bidstatus+"'";
    }
    public static String getCpProcessPostingFailureFailedBids(String flag) {
    	return "select custrefid from mm_bid_tbl where failedflag = '"+flag+"' and process = 'Commercial Paper' and markettype = 'primary' and allocatedflag ='Y' and postintegrationflag = 'Y' and failedpostflag = 'Y'";	
    }
    public static String getCpProcessPostingFailureSuccessBids(String flag) {
    	return "select custrefid from mm_bid_tbl where failedflag = '"+flag+"' and process = 'Commercial Paper' and markettype = 'primary' and allocatedflag ='Y' and postintegrationflag = 'Y' and (failedpostflag = 'C' or failedpostflag = 'D')";	  	
    } 
    public static String getCpAllBidsOnMaturity(){
    	return "select custrefid, maturitydate, bidwiname, custsol,custacctno, custprincipal, branchsol,allocationpercentage from mm_bid_tbl Where ...";
    }
    public static String getCpInvestmentClosedateTbl() {
    	return "select investmentid, closedate from mm_sminvestments_tbl"; 
    }
    public static String getTbfailedAtTUtilWiCreatedFlg() {
    	return "select failedAtTUtilWiCreatedFlg from mm_setup_tbl where process = 'Treasury Bills' and markettype = 'primary'";
    }
    public static String getCpProcessBidsOnAwaitingMaturity(){
    	return "select custrefid, bidwiname, maturitydate, custemail, branchsol, lienflag from mm_bid_tbl where awaitingmaturityflag = 'Y'  and failedpostflag = 'N'";
    }
    
    
    
}
