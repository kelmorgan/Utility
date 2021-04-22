package com.fbn.start;
import com.fbn.api.newgen.Controller;
import com.fbn.api.newgen.RequestXml;
import com.fbn.utils.ConstantsI;
import com.fbn.utils.DbConnect;
import com.fbn.utils.Query;


public class startUtility implements ConstantsI{
	
	  public static void main(String[] args) {
	  	 // String attribute = "<CP_UTILITYFLAG>Y</CP_UTILITYFLAG>";
		  String sessionId = new Controller().getSessionId();
		  
		new Controller().unlockWorkItem(sessionId,wiName);
		new Controller().lockWorkItem(sessionId,wiName);
		new Controller().completeWorkItem(sessionId,wiName);
		
	
	  }
}
