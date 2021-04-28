package com.fbn.api.newgen.customservice;

import com.fbn.api.newgen.controller.Controller;

public class CreateWorkItem {

    private final String sessionId;
    private final String initiateFlag;
    private final String attributes;

    public CreateWorkItem(String sessionId,String attributes, String initiateFlag) {
        this.sessionId = sessionId;
        this.attributes = attributes;
        this.initiateFlag = initiateFlag;
    }

    public  String getCreatedWorkItem(){
        return new Controller().getCreatedWorkItem(sessionId,attributes,initiateFlag);
    }
}
