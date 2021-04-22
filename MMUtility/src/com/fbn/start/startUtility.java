package com.fbn.start;
import com.fbn.api.newgen.Controller;
import com.fbn.api.newgen.RequestXml;
import com.fbn.utils.ConstantsI;
import com.fbn.utils.DbConnect;
import com.fbn.utils.Query;


public class startUtility implements ConstantsI{
	
	  public static void main(String[] args) {
		  System.out.println(new DbConnect(RequestXml.getSelectQueryXml(Query.getSetupTblQuery(),cabinetName)).getData());

	  }
}
