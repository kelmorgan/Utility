package com.fbn.api.newgen;

import com.fbn.utils.ConstantsI;
import com.fbn.utils.DbConnect;
import com.fbn.utils.XmlParser;

import java.util.Map;
import java.util.Set;

public class Controller implements ConstantsI {
    XmlParser xmlParser = new XmlParser();
    private String inputXml;
    private String outputXml;

    public String getSessionId(){
        String connectXml = RequestXml.getConnectCabinetXml(cabinetName,userName,password);
        try {
            String connectOutputXml = Api.executeCall(connectXml);
            System.out.println("output from connect api:"+connectOutputXml);
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
        System.out.println("input from upload api:"+inputXml);
        try {
            outputXml = Api.executeCall(inputXml);
            System.out.println("output from upload api:"+outputXml);
            xmlParser.setInputXML(outputXml);
            if(success(xmlParser.getValueOf("MainCode")))
                return xmlParser.getValueOf("ProcessInstanceId");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public void completeWorkItem (String sessionId, String wiName) {
        inputXml = RequestXml.getCompleteWorkItemXml(cabinetName,sessionId,wiName);
        System.out.println("input from completeworkitem-- "+inputXml);

        try {
            outputXml = Api.executeCall(inputXml);
            System.out.println("outputXml from completeworkitem-- "+outputXml);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void lockWorkItem(String sessionId,String wiName){
        inputXml = RequestXml.getLockWorkItemInputXml(cabinetName,sessionId,wiName);

        System.out.println("input from lock workitem-- "+inputXml);

        try {
            outputXml = Api.executeCall(inputXml);
            System.out.println("outputXml from lock workitem-- "+outputXml);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void unlockWorkItem (String sessionId,String wiName){
        inputXml = RequestXml.getUnlockWorkItemXml(cabinetName,sessionId,wiName);
        System.out.println("input from unlock workitem-- "+inputXml);

        try {
            outputXml = Api.executeCall(inputXml);
            System.out.println("outputXml from unlock workitem-- "+outputXml);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public Set<Map<String,String>> getRecords(String query){
        return new DbConnect(RequestXml.getSelectQueryXml(query,cabinetName)).getData();
    }
    public int updateRecords(String sessionId,String tableName,String columnName, String values,String condition){
        return new DbConnect(RequestXml.getUpdateQueryXml(cabinetName,sessionId,tableName,columnName,values,condition)).saveData();
    }
    public int insertRecords(String sessionId,String tableName,String columnName, String values){
        return new DbConnect(RequestXml.getInsertQueryXml(cabinetName,sessionId,tableName,columnName,values)).saveData();
    }
    public int deleteRecords(String sessionId,String tableName,String condition){
        return new DbConnect(RequestXml.getDeleteQueryXml(cabinetName,sessionId,tableName,condition)).saveData();
    }

    private boolean success(String response){
        return response.equalsIgnoreCase("0");
    }
}



