package com.fbn.start;
import com.fbn.api.newgen.Controller;
import com.fbn.utils.ConstantsI;
import com.fbn.utils.Query;


public class startUtility implements ConstantsI{
	
	  public static void main(String[] args) {
	  	String value = "'T'";
	  	String condition = "refid = ''";
	  	System.out.println(new Controller().updateRecords(new Controller().getSessionId(),Query.setupTblName,Query.stColCloseFlag,value,condition));
	  }
}
