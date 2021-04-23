package com.fbn.api.newgen;

public class CompleteWorkItem  {
    private final String wiName;
    private String attribute;
    private String value;
    private String sessionId;


    public CompleteWorkItem(String wiName) {
        this.wiName = wiName;
        completeWorkItem();
    }

    public CompleteWorkItem(String sessionId,String wiName, String attribute,String value){
        this.wiName = wiName;
        this.attribute = attribute;
        this.value = value;
        this.sessionId = sessionId;
        completeWorkItemWithAttribute();
    }

    private  void completeWorkItem(){
        String sessionId = new Controller().getSessionId();
        new Controller().unlockWorkItem(sessionId,wiName);
        new Controller().lockWorkItem(sessionId,wiName);
        new Controller().completeWorkItem(sessionId,wiName);
    }
    private  void completeWorkItemWithAttribute(){
        String condition = "wiName = '"+wiName+"'";
        String tableName = "moneyMarket_ext";
        String value = "'"+this.value+"'";
        new Controller().unlockWorkItem(sessionId,wiName);
        new Controller().lockWorkItem(sessionId,wiName);
        new Controller().updateRecords(sessionId, tableName,attribute,value,condition);
        new Controller().completeWorkItem(sessionId,wiName);
    }
}
