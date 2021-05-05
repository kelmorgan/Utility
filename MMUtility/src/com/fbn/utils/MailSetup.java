package com.fbn.utils;

import com.fbn.api.newgen.controller.Controller;

public class MailSetup {
	
	private final String sessionId;
	private final String processInstanceId;
	private final String mailFrom;
	private final String mailTo;
	private final String mailCc;
	private final String mailSubject;
	private String mailMessage;
	


	
	public MailSetup(String sessionId, String processInstanceId, String mailFrom, String mailTo, String mailCc, String mailSubject, String mailMessage) {
		
		this.sessionId = sessionId;
		this.processInstanceId = processInstanceId;
		this.mailFrom = mailFrom;
		this.mailTo = mailTo;
		this.mailCc = mailCc;
		this.mailSubject = mailSubject;
		this.mailMessage = setMailMessage(mailMessage);

		sendMail();

	}
	
	

private void sendMail() {
	 new Controller().sendMail(sessionId, "1", processInstanceId, mailFrom, mailTo, mailCc, mailSubject, mailMessage);
	}
 
 private String setMailMessage(String mailMessage) {
    return this.mailMessage = "<html>" +
             "<body>" +
             "Dear User, <br>" +
             "<br>"+mailMessage+"<br>" +
             "<br> Please do not reply, this is a system generated mail. <br>" +
             "</body>" +
             "</html>";
 }

}
