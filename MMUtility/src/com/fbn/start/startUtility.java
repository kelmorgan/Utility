package com.fbn.start;
import com.fbn.api.RequestXML;
import com.fbn.utils.ConstantsI;
import com.fbn.utils.DbConnect;
import com.fbn.utils.Query;
import com.fbn.utils.XmlParser;


public class startUtility implements ConstantsI{
	
	  public static void main(String[] args) {
		  DbConnect obj = new DbConnect();
		  
		  try {
			  String xml = new DbConnect(RequestXML.selectRequestQuery(CabinetName, obj.getSessionId(), Query.selectBids())).getData();

			  System.out.println(XmlParser.getXMLData(xml,"Record"));
		
		} catch (Exception e) {
			e.printStackTrace();
		}
	  }
}
