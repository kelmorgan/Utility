package com.fbn.utils;
import com.fbn.api.newgen.execute.Api;

import java.util.Map;
import java.util.Set;

public class DbConnect implements ConstantsI{
	private final String queryXml;
	private String outputXml;
	private final XmlParser xmlParser = new XmlParser();
	public DbConnect(String queryXml){
		this.queryXml = queryXml;
	}

   public Set<Map<String,String>> getData() {
       try {
           outputXml = Api.executeCall(queryXml);
           System.out.println(outputXml);
           xmlParser.setInputXML(outputXml);

           if (success(xmlParser.getValueOf("MainCode")))
               return xmlParser.getXMLData(outputXml,"Record");
       } catch (Exception e) {
           e.printStackTrace();
       }
       return null;
   }
   public int saveData (){
       try {
          outputXml = Api.executeCall(queryXml);
           System.out.println(outputXml);
          xmlParser.setInputXML(outputXml);
          if (success(xmlParser.getValueOf("MainCode")))
              return 1;
       } catch (Exception e) {
           e.printStackTrace();
       }
       return -1;
   }

    private boolean success(String response){
        return response.equalsIgnoreCase("0");
    }
}
