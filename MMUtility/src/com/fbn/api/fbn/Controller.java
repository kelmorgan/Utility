package com.fbn.api.fbn;

import com.fbn.api.fbn.Api;
import com.fbn.utils.XmlParser;

public class Controller {

	public String getTranID(){
  
        try {
            //String OutputXml = Api.postCall();
            
           // xmlParser.setInputXML(OutputXml);
            
           // return xmlParser.getValueOf("TranId");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
