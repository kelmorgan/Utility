package com.fbn.start;
import com.fbn.api.newgen.Controller;
import com.fbn.api.newgen.RequestXml;
import com.fbn.utils.ConstantsI;
import com.fbn.utils.DbConnect;
import com.fbn.utils.Query;


public class startUtility implements ConstantsI{
	
	  public static void main(String[] args) {
	  	 // String attribute = "<CP_UTILITYFLAG>Y</CP_UTILITYFLAG>";
		new Controller().completeWorkItem(new Controller().getSessionId(),wiName);
		//new Controller().unlockWorkItem(new Controller().getSessionId(),wiName);
		//new Controller().lockWorkItem(new Controller().getSessionId(),wiName);
	  }
}
