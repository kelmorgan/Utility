package com.fbn.utils;
import com.fbn.api.newgen.Api;

public class DbConnect implements ConstantsI{

	private String queryXml;

	public DbConnect(String queryXml){
		this.queryXml = queryXml;
	}

    
    
   public String getData() {
       try {
           return Api.executeCall(queryXml);
       } catch (Exception e) {
           e.printStackTrace();
       }
       return null;
   }
}
