package com.fbn.start;
import com.newgen.omni.wf.util.app.NGEjbClient;
import com.newgen.omni.wf.util.excp.NGException;
import com.newgen.omni.wf.util.xml.api.CreateXML;

public class Commons {
    private CommonsProperties commonsProperties;

    public Commons() throws NGException {
        this.commonsProperties = new CommonsProperties();
       initializeNgb();
    }

    public void initializeNgb () throws NGException {
        System.out.println(commonsProperties.getAppServerIp()+commonsProperties.getAppServerPort()+commonsProperties.getAppServerType());
        NGEjbClient ngEjbClient = NGEjbClient.getSharedInstance();
       // System.out.println(commonsProperties.getAppServerIp()+commonsProperties.getAppServerPort()+commonsProperties.getAppServerType());
        ngEjbClient.initialize(commonsProperties.getAppServerIp(),commonsProperties.getAppServerPort(),commonsProperties.getAppServerType());
        System.out.println(ngEjbClient);
        commonsProperties.setNgEjbClient(ngEjbClient);
        //initializeSessionId();
    }
    public void initializeSessionId () throws NGException {
        String connectXml = CreateXML.WMConnect(commonsProperties.getUserName(),commonsProperties.getPassword(),commonsProperties.getCabinetName()).toString();
       // System.out.println(commonsProperties.getNgEjbClient());

        System.out.println(connectXml);


    }
}
