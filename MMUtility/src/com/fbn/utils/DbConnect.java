package com.fbn.utils;
import com.fbn.api.RequestXML;
import com.newgen.wfdesktop.xmlapi.WFCallBroker;
import com.newgen.wfdesktop.xmlapi.WFXmlResponse;

public class DbConnect implements ConstantsI{
	
	private String queryXml;
	private int sessionId;
	
	public DbConnect(){
		try {
			this.sessionId = getConnection();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public DbConnect(String queryXml){
		this.queryXml = queryXml;
	}
 
     
    private int getConnection() throws Exception {
        
        try {
        
             String strxmlin = RequestXML.ConnectCabinetXML(CabinetName, UserName, Password); 
             System.out.println("strxmlin : " + strxmlin);
    
             String strXmlout = WFCallBroker.execute(strxmlin, AppServerIp, WrapperPort, 0);
             System.out.println("strXmlout : " + strXmlout);
             
             WFXmlResponse xmlResponse = new WFXmlResponse(strXmlout);
             System.out.println("xmlResponse : " + xmlResponse);
             
             if (xmlResponse.getVal("Status").equals("0")) {
          
                int strSessionId = Integer.parseInt(xmlResponse.getVal("UserDBId"));
                 System.out.println("Cabinet Connected Successfully.");
                 System.out.println("Session ID : " + strSessionId);
                 
                 return strSessionId;
                 
             } 
             else {
            	
             	System.out.println("Cabinet Connection Failed. Check Wrapper!!!");
             
               throw new Exception(xmlResponse.getVal("Subject") + " : " + xmlResponse.getVal("Description"));

             }
           
            } 
        catch (Exception le) {   
            
            throw new Exception("Error in connecting cabinet : " + le.getMessage());
        }
      
    } 
    
    public String selectBids() {
        String strOutputXml = "";
        
        try {
        	System.out.println("inside try : ");
        	
        	String strInputXml = RequestXML.selectRequestQuery(CabinetName, getConnection(), Query.selectAllBid());
        	System.out.println("AP Select - strInputXml : " + strInputXml);
        	
            strOutputXml = WFCallBroker.execute(strInputXml, AppServerIp, WrapperPort, 1);
            System.out.println("AP Select - strOutputXml : " + strOutputXml);
            } catch (Exception e){          
              }
            return strOutputXml;
         }
    
    
   public String getData() {
        
        try {
       
        	return WFCallBroker.execute(queryXml, AppServerIp, WrapperPort, 1);
           
            } catch (Exception e){          
              }
            return null;
     }

public int getSessionId() {
	return sessionId;
}
    	
}
