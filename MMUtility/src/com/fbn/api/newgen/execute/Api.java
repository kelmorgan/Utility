package com.fbn.api.newgen.execute;

import com.fbn.utils.ConstantsI;
import com.newgen.wfdesktop.xmlapi.WFCallBroker;
public class Api implements ConstantsI {
	
    public static String executeCall (String request) throws Exception {
        return WFCallBroker.execute(request, appServerIp, wrapperPort, 1);
    }
}
