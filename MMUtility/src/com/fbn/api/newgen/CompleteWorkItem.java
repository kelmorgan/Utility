package com.fbn.api.newgen;

public class CompleteWorkItem  {

    public static void completeWorkItem(String wiName){
        String sessionId = new Controller().getSessionId();
        new Controller().unlockWorkItem(sessionId,wiName);
        new Controller().lockWorkItem(sessionId,wiName);
        new Controller().completeWorkItem(sessionId,wiName);
    }
}
