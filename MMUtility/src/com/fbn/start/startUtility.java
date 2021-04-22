package com.fbn.start;
import com.fbn.api.newgen.RequestXml;
import com.fbn.utils.ConstantsI;
import com.fbn.utils.DbConnect;
import com.fbn.utils.Query;


public class startUtility implements ConstantsI{
	
	  public static void main(String[] args) {
		  System.out.println(new DbConnect(RequestXml.selectRequestQuery(cabinetName,1, Query.getBidTblQuery())).getData());

	  }
}
