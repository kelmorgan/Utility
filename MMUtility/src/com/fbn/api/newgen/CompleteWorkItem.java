package com.fbn.api.newgen;

public class CompleteWorkItem  {
    private String wiName;
    public CompleteWorkItem(String wiName) {
        this.wiName = wiName;
        completeWorkItem();
    }

    private  void completeWorkItem(){
        String sessionId = new Controller().getSessionId();
        new Controller().unlockWorkItem(sessionId,wiName);
        new Controller().lockWorkItem(sessionId,wiName);
        new Controller().completeWorkItem(sessionId,wiName);
    }
}
