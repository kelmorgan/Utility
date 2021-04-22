package com.fbn.api.newgen;

import com.fbn.utils.ConstantsI;
import com.fbn.utils.XmlParser;

public class Controller implements ConstantsI {
    XmlParser xmlParser = new XmlParser();

    public String getSessionId(){
        String connectXml = RequestXml.getConnectCabinetXml(cabinetName,userName,password);
        try {
            String connectOutputXml = Api.executeCall(connectXml);
            xmlParser.setInputXML(connectOutputXml);

            if (xmlParser.getValueOf("MainCode").equalsIgnoreCase("0"))
                return xmlParser.getValueOf("SessionId");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
