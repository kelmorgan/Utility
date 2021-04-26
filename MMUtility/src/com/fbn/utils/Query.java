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
	public static String getCpOpenWindowQuery() {
        return  "select closedate , winame,refid from mm_setup_tbl where process = 'Commercial Paper' and closeflag = 'N'";
    }
    public static String getBidTblQuery() {
        return  "select * from mm_bid_tbl";
    }
    public static String getCpPmBidsToProcessQuery () {
        return "select custrefid, tenor, rate, ratetype from mm_bid_tbl where process = 'Commercial Paper' and markettype= 'primary' and processflag ='N' and groupindexflag = 'N'";
    }
    public static String getCpAllocatedPrimaryBids(String flag) {
    	return "select custrefid, bidwiname, custsol,custacctno, custprincipal, branchsol,allocationpercentage from mm_bid_tbl where failedflag = '"+flag+"' and process = 'Commercial Paper' and markettype = 'primary' and allocatedflag ='Y'";
    }
    public static String getProcessPostingFailureFailedBids(String flag) {
    	return "select custrefid from mm_bid_tbl where failedflag = '"+flag+"' and process = 'Commercial Paper' and markettype = 'primary' and allocatedflag ='Y' and postintegrationflag = 'Y' and failedpostflag = 'Y'";	
    }
    public static String getProcessPostingFailureSuccessBids(String flag) {
    	return "select custrefid from mm_bid_tbl where failedflag = '"+flag+"' and process = 'Commercial Paper' and markettype = 'primary' and allocatedflag ='Y' and postintegrationflag = 'Y' and (failedpostflag = 'C' or failedpostflag = 'D')";	  	
    } 
   
}
