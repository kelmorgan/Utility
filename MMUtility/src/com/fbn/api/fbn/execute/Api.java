package com.fbn.api.fbn.execute;

import com.fbn.utils.SocketService;



public class Api {
    public static String executeCall(String serviceName, String requestXml){
        return new SocketService().executeIntegrationCall(serviceName,requestXml);
    }
}
