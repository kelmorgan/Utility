package com.fbn.start;

import com.fbn.api.newgen.controller.Controller;
import com.fbn.utils.MailSetup;

public class startUtility {
	
	  public static void main(String[] args) {
	  String sessionId = new Controller().getSessionId();
	  new MailSetup(sessionId, "1", "FBN-00000000000134-MMW", "sn029216@firstbanknigeria.com", "sn029154@firstbanknigeria.com", "", "Testing Money Market Mail","My New message for Commercial Paper").getSendMail();
	 
	  //new Main().start();

	  }
}
