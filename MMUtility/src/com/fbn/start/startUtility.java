package com.fbn.start;
import com.fbn.api.RequestXML;
import com.fbn.utils.ConstantsI;
import com.fbn.utils.DbConnect;
import com.fbn.utils.Query;


public class startUtility implements ConstantsI{
	
	  public static void main(String[] args) {
		  DbConnect obj = new DbConnect();
		  
		  try {
			  System.out.println(new DbConnect(RequestXML.selectRequestQuery(CabinetName, obj.getSessionId(), Query.selectAllBid())).getData());
		
		} catch (Exception e) {
			e.printStackTrace();
		}
	  }
}
