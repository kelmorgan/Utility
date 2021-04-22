package com.fbn.api.newgen;

public class CreateWorkItem {
    public static String createWorkItem(String attributes){
        return new Controller().getCreatedWorkItem(new Controller().getSessionId(),attributes);
    }
}
