package com.newgen.util.MoveWI;

import java.awt.Event;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.newgen.iforms.custom.IFormReference;
import com.newgen.util.logger.LogMessages;
import org.apache.log4j.Logger;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.util.Properties;

public class SocketService {
	
        String wiName;
       
        String propFile = System.getProperty("user.dir") + File.separator + "INTEGRATION_Properties" + File.separator + "Integration.properties";
        Properties propbasic = loadPropertyFile(propFile);
        String SocketIP = propbasic.getProperty("SocketIP");
        String tmpSocketPort = propbasic.getProperty("SocketPort");
        int SocketPort = Integer.parseInt(tmpSocketPort);
        String inputxml;
    
    public String executeIntegrationCall(String  serviceName, String inputParam, String inputXml){
   
    	LogMessages.logStatus("Inside executeIntegrationCall :: ");  
         
   
	LogMessages.logStatus("Inside executeIntegrationCall :: serviceName :: "+serviceName);  
	LogMessages.logStatus("Inside executeIntegrationCall :: inputParam :: "+inputParam);  
	LogMessages.logStatus("Inside executeIntegrationCall :: inputXml ::"+inputXml);  
	 String result = "";
	  String varInputXML = "";
   // String[] argRD=inputParam.split("~");
    
    if("placeLien".equalsIgnoreCase(serviceName))
    {
    	inputxml=propbasic.getProperty("placeLien_inputxml");
    }
    else if("removeLien".equalsIgnoreCase(serviceName))
    {
    	inputxml=propbasic.getProperty("removeLien_inputxml");
    }
    else if("fetchAccountBVN".equalsIgnoreCase(serviceName)) {
    	inputxml=propbasic.getProperty("fetchAccountBVN_inputxml");
    }
   /* else if("".equalsIgnoreCase(serviceName)) {
    	inputxml=propbasic.getProperty("fetchAccountBVN_inputxml");
    }*/
    else if("SuccessDebit".equalsIgnoreCase(serviceName)) {
    	LogMessages.logStatus("Inside executeIntegrationCall :: inside serviceName ::" +serviceName);  
    	 varInputXML = serviceName + "~" + inputXml + "~";
    	 
    }
    else if("FailureCredit".equalsIgnoreCase(serviceName)) {
    	
    	LogMessages.logStatus("Inside executeIntegrationCall :: inside serviceName ::" +serviceName); 
        varInputXML = serviceName + "~" + inputxml + "~";
        
    }
    
//    if("FetchCIStaffDetails".equalsIgnoreCase(serviceName))
//    {
//    	CI_inputxml = CI_inputxml.replaceAll("&<StaffID>&",argRD[0]);
//        logger.info("Replace inputParam >> :::::" + CI_inputxml);
//        varInputXML = serviceName+"~"+CI_inputxml+"~"+inputParam+"~";
//        logger.info("InputXML To Socket >> :::::" + varInputXML);   
//    }

//    if("postSampleRequestToFinacle".equalsIgnoreCase(serviceName)) {
//        CI_inputxml = inputXml;
//        varInputXML = serviceName + "~" + CI_inputxml + "~";
//    }
//    
//    if("postRequestToFinacle".equalsIgnoreCase(serviceName)) {
//        CI_inputxml = inputXml;
//        varInputXML = serviceName + "~" + CI_inputxml + "~";
//    }
    
    if ("placeLien".equalsIgnoreCase(serviceName)){
        inputxml = inputXml;
        varInputXML = serviceName + "~" + inputxml + "~";
      
        
        
    }if ("removeLien".equalsIgnoreCase(serviceName)){
        inputxml = inputXml;
        varInputXML = serviceName + "~" + inputxml + "~";
       
    }
    
    if("fetchAccountBVN".equalsIgnoreCase(serviceName)) {
    	inputxml= inputxml.replaceAll("&<acctId>&", inputParam);
    	
    	varInputXML = serviceName+"~"+inputxml+"~"+inputParam+"~";
    	
    }

    try
    {
     
      Socket socket = new Socket(SocketIP, SocketPort);
      LogMessages.logStatus("Client Connected.....::" ); 
      System.out.println("Client Connected.....");
      
      DataOutputStream dout = new DataOutputStream(socket.getOutputStream());
      LogMessages.logStatus("varInputXML ::" +varInputXML); 
      if ((varInputXML != null) && (varInputXML.length() > 0))
      {
        dout.write(varInputXML.getBytes("UTF-16LE"));
        dout.flush();
      }

      try
      {
    	 
    	  DataInputStream in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
          byte[] readBuffer = new byte[1000000];
          int num = in.read(readBuffer);
         
          if (num > 0) {  
            byte[] arrayBytes = new byte[num];
            System.arraycopy(readBuffer, 0, arrayBytes, 0, num);
            result = new String(arrayBytes, "UTF-16LE");
            LogMessages.logStatus("result ::" +result); 
            System.out.println("Result===>>" + result);
          }
         

      }
      catch (SocketException localSocketException)
      {
    	  LogMessages.logStatus("localSocketException.printStackTrace() ::" +localSocketException.getMessage()); 
    	  localSocketException.printStackTrace();
      }
      catch (IOException i)
      {
        i.printStackTrace();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    return result;
  }
    
  	public static Properties loadPropertyFile(String filePath) {
		

		Properties propObj = null;
		try {
			propObj = new Properties();
			propObj.load(new FileInputStream(filePath));
		} catch (FileNotFoundException e) {
			
			propObj = null;
		} catch (IOException e) {
		
			propObj = null;
		}

		
		return propObj;
	}
    
}