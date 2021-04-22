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
	public static String getOpenWindowQuery() {
        return  "select closedate , winame,refid from mm_setup_tbl where closeflag = 'N'";
    }
    public static String getBidTblQuery() {
        return  "select * from mm_bid_tbl";
    }
}
