package com.fbn.api.fbn.controller;

import com.fbn.api.fbn.execute.Api;
import com.fbn.api.fbn.generateXml.RequestXml;
import com.fbn.utils.ConstantsI;

public class Controller implements ConstantsI {

	public String getUserLimit(){
	    return Api.executeCall(fetchLimitServiceName, RequestXml.getUserLimitXml("SN022357"));
    }
}
