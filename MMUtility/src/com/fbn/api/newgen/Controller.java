package com.fbn.api.newgen;

import com.fbn.utils.ConstantsI;
import com.fbn.utils.XmlParser;
public class Controller implements ConstantsI {
    XmlParser xmlParser = new XmlParser();
    private String inputXml;
    private String outputXml;

    public String getSessionId(){
        String connectXml = RequestXml.getConnectCabinetXml(cabinetName,userName,password);
        try {
            String connectOutputXml = Api.executeCall(connectXml);
            xmlParser.setInputXML(connectOutputXml);

            if (success(xmlParser.getValueOf("MainCode")))
                return xmlParser.getValueOf("SessionId");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getCreatedWorkItem(String sessionId,String attributes){
        inputXml = RequestXml.getCreateWorkItemXml(cabinetName,sessionId,processDefId,queueId,attributes);
        try {
            outputXml = Api.executeCall(inputXml);
            xmlParser.setInputXML(outputXml);
            if(success(xmlParser.getValueOf("MainCode")))
                return xmlParser.getValueOf("ProcessInstanceId");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    private boolean success(String response){
        return response.equalsIgnoreCase("0");
    }
}



