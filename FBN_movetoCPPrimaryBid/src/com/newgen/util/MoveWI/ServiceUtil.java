/**
 * ******************************************************************
 * NEWGEN SOFTWARE TECHNOLOGIES LIMITED Group : Product / Project : ICICI - CMS
 * Collections Process Module : CMS Collection File Name : ServiceUtil.java
 * Author : Sivashankar S Date written (DD/MM/YYYY) : 01/08/2014 Description :
 * CHANGE HISTORY
 * **********************************************************************************************
 * Date	Change By	Change Description (Bug No. (If Any)) (DD/MM/YYYY)
 * **********************************************************************************************
 */
package com.newgen.util.MoveWI;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.newgen.dmsapi.DMSCallBroker;
import com.newgen.dmsapi.DMSXmlResponse;
import com.newgen.iforms.custom.IFormReference;
import com.newgen.mvcbeans.model.wfobjects.WDGeneralData;
import com.newgen.util.MoveWI.ConnectToFinacle;
import com.newgen.util.MoveWI.SocketService;
import com.newgen.omni.jts.cmgr.XMLParser;
import com.newgen.omni.wf.util.app.NGEjbClient;
import com.newgen.util.logger.LogMessages;
import com.newgen.wfdesktop.xmlapi.WFCallBroker;
import com.newgen.wfdesktop.xmlapi.WFXmlList;
import com.newgen.wfdesktop.xmlapi.WFXmlResponse;



public class ServiceUtil {

    static String strCabinetName = "";
    static String strAppServerIp = "";
    static String strAppServerPort = "";
    static String strQueueID = "";
    String strAppServerType = "";
    static int intWrapperPort = 0;
    String strUserName = "";
    String strPassword = "";
    static String strSessionId = "";
    String strQueueId = "";
    String strdatelimit = "";
    String strBatchlimit = "";
    String processdefid="";
    
    int count = 0;
    private static NGEjbClient ngEjbClient = null;

    public ServiceUtil(String cabinet, String ip, String port, String servertype, String wrapperPort, 
    		String user, String pass,String datelimit, String batchlimit,String processdefid,String queueId) {
        strCabinetName = cabinet;
        strAppServerIp = ip;
        strAppServerPort = port;
        strAppServerType = servertype;
        intWrapperPort = Integer.parseInt(wrapperPort);
        strUserName = user;
        strPassword = pass;
        strdatelimit = datelimit;
        strBatchlimit = batchlimit;
        strQueueID=queueId;
        this.processdefid=processdefid;
    }

	/**
     * ******************************************************************************************************
     * Function Name : ConnectCabinet Date Written : 01/08/2014 Author :
     * Sivashankar.s Input Parameters : String strCabinetName,String
     * strJbossIp,String strJbossPort, String strUserName, String strPassword
     * Output Parameters : None Return Values : WFXmlResponse Description :
     * Connects to the given cabinet and returns XML response with Session id. *
     * ********************************************************************************************************
     */
    public int ConnectCabinet() throws Exception {
        StringBuffer strBuffer = null;
        String strXmlout = "";
        int conStatus = 0;
        LogMessages.logStatus("strCabinetName :: "+strCabinetName) ;
        LogMessages.logStatus("strAppServerIp :: "+strAppServerIp) ;
        LogMessages.logStatus("strAppServerPort :: "+strAppServerPort) ;
        LogMessages.logStatus("intWrapperPort :: "+intWrapperPort) ;
        try {
            strBuffer = new StringBuffer();
            strBuffer.append("<?xml version=1.0?>");
            strBuffer.append("<NGOConnectCabinet_Input>");
            strBuffer.append("<Option>NGOConnectCabinet</Option>");
            strBuffer.append("<CabinetName>" + strCabinetName + "</CabinetName>");
            strBuffer.append("<UserName>" + strUserName + "</UserName><UserPassword>" + strPassword + "</UserPassword>");
            strBuffer.append("<UserExist>N</UserExist><ListSysFolder>N</ListSysFolder>");
            strBuffer.append("<UserType>U</UserType>");
            strBuffer.append("</NGOConnectCabinet_Input>");
            LogMessages.logStatus("strBuffer :: ."+strBuffer);
            strXmlout = WFCallBroker.execute(strBuffer.toString(), strAppServerIp, intWrapperPort, 0);
            //ngEjbClient = NGEjbClient.getSharedInstance();
            LogMessages.logStatus("strXmlout :: ."+strXmlout);
            //ngEjbClient.initialize(strAppServerIp,String.valueOf(intWrapperPort),strAppServerType);
            //strXmlout = ngEjbClient.makeCall(strBuffer.toString());
          
            WFXmlResponse xmlResponse = new WFXmlResponse(strXmlout);
            LogMessages.logStatus("xmlResponse :: ."+xmlResponse);
            if (xmlResponse.getVal("Status").equals("0")) {
                conStatus = 1;
                strSessionId = xmlResponse.getVal("UserDBId");
                System.out.println("Cabinet Connected Successfully.");
                LogMessages.logStatus("Cabinet Connected Successfully.");
                LogMessages.logStatus("Session ID : " + strSessionId);
                return conStatus;
            } else {
            	System.out.println("Cabinet Connection Failed. Check Wrapper!!!");
                LogMessages.logStatus("Cabinet Connection Failed. Check Wrapper!!!");
                LogMessages.logStatus("Input Xml : " + strBuffer.toString());
                LogMessages.logStatus("Output Xml : " + strXmlout);
                throw new Exception(xmlResponse.getVal("Subject") + " : " + xmlResponse.getVal("Description"));
            }
        } catch (Exception le) {
             
            LogMessages.logStatus("Exception in connecting cabinet : " + le.getMessage()+" Check Wrapper is Running");
            LogMessages.logStatus("Input Xml : " + strBuffer.toString());
            LogMessages.logStatus("Output Xml : " + strXmlout);
            LogMessages.logError("Exception in connecting cabinet and Check Wrapper is Running ", le);
            throw new Exception("Error in connecting cabinet : " + le.getMessage());
        }
    }

    public void fetchqueue(String queueId,String casetype) throws Exception {
        StringBuffer strBuffer = null;
        String strXmlout = "";
        strQueueId = queueId;
        String strWorkItemId = "";
        try {
            strBuffer = new StringBuffer();
            strBuffer.append("<?xml version='1.0'?>");
            strBuffer.append("<WMFetchWorkList_Input>");
            strBuffer.append("<Option>WMFetchWorkList</Option>");
            strBuffer.append("<EngineName>" + strCabinetName + "</EngineName>");
            strBuffer.append("<SessionId>" + strSessionId + "</SessionId>");
            strBuffer.append("<CountFlag>Y</CountFlag>");
            strBuffer.append("<DataFlag>Y</DataFlag>");
            strBuffer.append("<ZipBuffer>N</ZipBuffer>");
            strBuffer.append("<FetchLockedFlag>N</FetchLockedFlag>");
            strBuffer.append("<Filter><QueueId>" + strQueueId + "</QueueId>");
            strBuffer.append("<Type>256</Type>");
            strBuffer.append("<Comparison>0</Comparison>");
            strBuffer.append("<FilterString></FilterString>");
            strBuffer.append("</Filter>");
            strBuffer.append("<BatchInfo>");
            strBuffer.append("<NoOfRecordsToFetch>"+strBatchlimit+"</NoOfRecordsToFetch>");
            strBuffer.append("<OrderBy>1</OrderBy>");
            strBuffer.append("<SortOrder>A</SortOrder>");
            strBuffer.append("</BatchInfo>");
            strBuffer.append("<QueueType>N</QueueType>");
            strBuffer.append("</WMFetchWorkList_Input>");
            strXmlout = WFCallBroker.execute(strBuffer.toString(), strAppServerIp, intWrapperPort, 0);
            //strXmlout = ngEjbClient.makeCall(strBuffer.toString());
            WFXmlResponse xmlResponse = new WFXmlResponse(strXmlout);
            if (xmlResponse.getVal("MainCode").equals("0")) {
                WFXmlList xmlList = xmlResponse.createList("Instruments", "Instrument");
                LogMessages.logStatus("fetchqueue :: xmlList :: "+xmlList);
                for (; xmlList.hasMoreElements(true); xmlList.skip(true)) {
                	LogMessages.logStatus("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
                	String strProcessInstanceId = xmlList.getVal("ProcessInstanceId");
                    strWorkItemId = xmlList.getVal("WorkItemId");
                    LogMessages.logStatus("fetchqueue:: Current Processing ProcessInstance Id is : " + strProcessInstanceId);
                    if(casetype.equalsIgnoreCase("Awaiting mature"))
                    {
                    	 LogMessages.logStatus("fetchqueue:: Current Processing awaiting mature");
                    	getworkitemfromawaitingqueue(strProcessInstanceId, strWorkItemId);
                    	LogMessages.logStatus("fetchqueue:: Awaiting Maturity Utility executed...");
                    }
                    else
                    {
                    	 // processwitomovediscard(strProcessInstanceId, strWorkItemId);
                    	
                    }
                   
                    //getworkitem(strProcessInstanceId, strWorkItemId);
                    strProcessInstanceId ="";
                }
            } else if (xmlResponse.getVal("MainCode").equals("18")) {
                System.out.println("No new case to process..");
                LogMessages.logStatus("fetchqueue :: No new case to process..");
            } else {
                System.out.println("Error in Fetching Workitem.");
                LogMessages.logStatus("Error in Fetching Workitem.");
                LogMessages.logStatus("Input Xml : " + strBuffer.toString());
                LogMessages.logStatus("Output Xml : " + strXmlout);
            }
        } catch (Exception le) {
            LogMessages.logStatus("Exception in fetch queue : " + le.getMessage());
            LogMessages.logStatus("Input Xml : " + strBuffer.toString());
            LogMessages.logStatus("Output Xml : " + strXmlout);
            LogMessages.logError("Exception in fetch queue.", le);
            throw new Exception("Exception in fetch queue : " + le.getMessage());
        }
    }

    public void getworkitem(String strProcessInstanceId, String strWorkItemId) {
        StringBuffer strBuffer = null;
        String strXmlout = "";
        try {
            strBuffer = new StringBuffer();
            /*strBuffer.append("<?xml version='1.0'?>");
            strBuffer.append("<WFGetWorkitemDataExt_Input>");
            strBuffer.append("<Option>WFGetWorkitemDataExt</Option>");
            strBuffer.append("<EngineName>" + strCabinetName + "</EngineName>");
            strBuffer.append("<SessionId>" + strSessionId + "</SessionId>");
            strBuffer.append("<ProcessInstanceId>" + strProcessInstanceId + "</ProcessInstanceId>");
            strBuffer.append("<WorkitemId>" + "1" + "</WorkitemId>");
            strBuffer.append("<QueueId>" + "802" + "</QueueId>");
            strBuffer.append("<QueueType></QueueType>");
            strBuffer.append("<DocOrderBy>DocumentSize</DocOrderBy>");
            strBuffer.append("<DocSortOrder>D</DocSortOrder>");
            strBuffer.append("<ObjectPreferenceList>W,D</ObjectPreferenceList>");
            strBuffer.append("<GenerateLog>Y</GenerateLog>");
            strBuffer.append("<ZipBuffer>N</ZipBuffer>");
            strBuffer.append("</WFGetWorkitemDataExt_Input>");*/
            strBuffer.append("<WMGetWorkItem_Input>" + "\n");
            strBuffer.append("<Option>WMGetWorkItem</Option>");
            strBuffer.append("<EngineName>" + strCabinetName+ "</EngineName>");
            strBuffer.append("<SessionId>" + strSessionId + "</SessionId>");
            strBuffer.append("<ProcessInstanceId>" + strProcessInstanceId + "</ProcessInstanceId>");
            strBuffer.append("<WorkItemId>1</WorkItemId>");
            strBuffer.append("</WMGetWorkItem_Input>");
           
            LogMessages.logStatus("getworkitem :: xmlResponse :: "+strBuffer.toString());
           // strXmlout = WFCallBroker.execute(strBuffer.toString(), strAppServerIp, Integer.parseInt(strAppServerPort), 0);
            strXmlout = WFCallBroker.execute(strBuffer.toString(), strAppServerIp, intWrapperPort, 1);
            WFXmlResponse xmlResponse = new WFXmlResponse(strXmlout);
            WFXmlList xmlList=null;
            LogMessages.logStatus("getworkitem :: xmlResponse :: "+xmlResponse);
            String market=""; 
           	String commercialmarket="";
           	String category ="";
            if (xmlResponse.getVal("MainCode").equals("0")) {
	        	String sWI_entrytime= "";
	        	sWI_entrytime = xmlResponse.getVal("EntryDateTime");
	        	LogMessages.logStatus("EntryDateTime - "+sWI_entrytime);
	        	System.out.println("ProcessInstanceId "+strProcessInstanceId+" is locked successfully.");
	        	LogMessages.logStatus("ProcessInstanceId "+strProcessInstanceId+" is locked successfully.");
	        	xmlList = xmlResponse.createList("Attributes", "Attribute");
       	       	for(; xmlList.hasMoreElements(true); xmlList.skip(true)){
       	       	   if(xmlList.getVal("Name").equalsIgnoreCase("MARKET"))
       	       	   {
       	       		market = xmlList.getVal("Value");
       	       	   }
       	       	   if(xmlList.getVal("Name").equalsIgnoreCase("cp_market"))
    	       	   {
       	       		commercialmarket = xmlList.getVal("Value");
    	       	   }
       	       	   if(xmlList.getVal("Name").equalsIgnoreCase("CATEGORYTYPE"))
       	       	   {
       	       		category = xmlList.getVal("Value");
       	       	   }	   
				}
       	       	LogMessages.logStatus("market ::"+market);
    	        LogMessages.logStatus("commercialmarket ::"+commercialmarket);
    	        LogMessages.logStatus("category ::"+category);
    	        LogMessages.logStatus("Going to call readData method....");
    	        
            	//boolean statusWebportalResponse = checkWebportalResponse(strProcessInstanceId,busseg,insCat,insType);
            	
    	        market=""; 
            	commercialmarket ="";
            	category ="";
            	
            	
            } else if (xmlResponse.getVal("MainCode").equals("18")) {
                System.out.println("No new case found.");
                LogMessages.logStatus("No new case found.");
            } else {
                System.out.println("Error in getting workitem data.");
                LogMessages.logStatus("Error in getting workitem data.");
                LogMessages.logStatus("Input Xml : " + strBuffer.toString());
                LogMessages.logStatus("Output Xml : " + strXmlout);
            }
        } catch (Exception e) {
            LogMessages.logStatus("Exception occured in getworkitem : " + e.getMessage());
            LogMessages.logError("Exception occured in getworkitem.", e);
        }
    }  

    public void unlockWIs(String pid) {
        StringBuffer strBuffer = new StringBuffer();
        String strOutputXml = "";
        try {
            strBuffer.append("<?xml version=1.0?>");
            strBuffer.append("<WMUnlockWorkItem_Input>");
            strBuffer.append("<Option>WMUnlockWorkItem</Option>");
            strBuffer.append("<EngineName>" + strCabinetName + "</EngineName>");
            strBuffer.append("<SessionId>" + strSessionId + "</SessionId>");
            strBuffer.append("<ProcessInstanceId>" + pid + "</ProcessInstanceId>");
            strBuffer.append("<WorkItemId>1</WorkItemId>");
            strBuffer.append("<Admin>Y</Admin>");
            strBuffer.append("</WMUnlockWorkItem_Input>");
            //strOutputXml = WFCallBroker.execute(strBuffer.toString(), strAppServerIp, Integer.parseInt(strAppServerPort), 0);
           // strOutputXml = ngEjbClient.makeCall(strBuffer.toString());
            LogMessages.logStatus("strBuffer :: "+strBuffer.toString());
            strOutputXml = WFCallBroker.execute(strBuffer.toString(), strAppServerIp, intWrapperPort, 1);
            LogMessages.logStatus("strOutputXml :: "+strOutputXml);
            WFXmlResponse xmlResponse = new WFXmlResponse(strOutputXml);
            if (xmlResponse.getVal("MainCode").equals("0")) {
                System.out.println("Unlock Workitem Sucessfull.");
                LogMessages.logStatus("Unlock Workitem Sucessfull.");
            }
           
        } catch (Exception e) {
            LogMessages.logStatus("Error in Unlock Workitem : " + e.getMessage());
            LogMessages.logError("Error in Unlock Workitem :  ", e);
        }
    }

    public void CompleteWorkItem(String sProcessInstId) {
        String strInputXml = "";
        String strOutputXml = "";
        try {
            strInputXml = "<?xml version='1.0'?>"
                    + "<WMCompleteWorkItem_Input>"
                    + "<Option>WMCompleteWorkItem</Option>"
                    + "<EngineName>" + strCabinetName + "</EngineName>"
                    + "<SessionId>" + strSessionId + "</SessionId>"
                    + "<ProcessInstanceId>" + sProcessInstId + "</ProcessInstanceId>"
                    + "<WorkItemId>1</WorkItemId>"
                    + "<AuditStatus></AuditStatus>"
                    + "<Comments></Comments>"
                    + "</WMCompleteWorkItem_Input>";
            //strOutputXml = WFCallBroker.execute(strInputXml.toString(), strAppServerIp, Integer.parseInt(strAppServerPort), 0);
            LogMessages.logStatus("Completed Workitem : strInputXml " + strInputXml);
            strOutputXml = WFCallBroker.execute(strInputXml, strAppServerIp, intWrapperPort, 1);
            LogMessages.logStatus("Completed Workitem : strOutputXml " + strOutputXml);
            WFXmlResponse xmlResponse = new WFXmlResponse(strOutputXml);
            if (xmlResponse.getVal("MainCode").equals("0")) {
            	 count++;
            	 LogMessages.logStatus("Completed Workitem : " + sProcessInstId);
                 System.out.println("Completed Workitem : " + sProcessInstId);
                 LogMessages.logStatus("Total work item completed : " + count);
                 System.out.println("Total work item completed : " + count);
                 
            } else {
               LogMessages.logStatus("Error In Completing Workitem : " + sProcessInstId);
            }
        } catch (Exception e) {
            LogMessages.logStatus("Exception Occured in Completing ProcessInstanceId  : " + e.getMessage());
            LogMessages.logStatus("[Exception Caught] Input Xml : " + strInputXml);
            LogMessages.logStatus("[Exception Caught] Output Xml : " + strOutputXml);
            LogMessages.logError("Exception Occured in Completing Workitem :  ", e);
        }
    }

    /**
     * ******************************************************************************************************
     * Function Name : DisConnectCabinet Date Written : 01/08/2014 Author :
     * Sivashankar.s Input Parameters : String strCabinetName, String
     * strJbossIp, String strJbossPort, String strSessionId Output Parameters :
     * None Return Values : None Description : Disconnects the given cabinet. *
     * ********************************************************************************************************
     */
    public boolean DisConnectCabinet() throws Exception {
        StringBuffer strBuffer = null;
        String strXmlout = "";

        try {
            strBuffer = new StringBuffer();
            strBuffer.append("<?xml version=1.0?>");
            strBuffer.append("<WMDisconnect_Input>");
            strBuffer.append("<Option>WMDisConnect</Option>");
            strBuffer.append("<EngineName>" + strCabinetName + "</EngineName>");
            strBuffer.append("<SessionId>" + strSessionId + "</SessionId>");
            strBuffer.append("</WMDisconnect_Input>");
           // strXmlout = WFCallBroker.execute(strBuffer.toString(), strAppServerIp, Integer.parseInt(strAppServerPort), 0);
            strXmlout = ngEjbClient.makeCall(strBuffer.toString());
            WFXmlResponse xmlResponse = new WFXmlResponse(strXmlout);
            if (xmlResponse.getVal("MainCode").equals("0")) {
                System.out.println("Cabinet disconnected successfully.");
                LogMessages.logStatus("Cabinet disconnected successfully.");
                return true;
            }
            if (!xmlResponse.getVal("MainCode").equals("0")) {
                LogMessages.logStatus("Error in Disconnecting Cabinet. Output Xml : " + strXmlout);
                return false;
            }
            return false;
        } catch (Exception le) {
            LogMessages.logStatus("Exception Caught in Disconnect Cabinet. Output Xml : " + strXmlout);
            LogMessages.logError("Exception Caught in Disconnect Cabinet ", le);
            return false;
        }
    }
    
    public boolean compareDates(String strentrydate) throws ParseException {
        try {     
        	System.out.println("I am in compareDates method");
        	LogMessages.logStatus("I am in compareDates method");
           		DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");                     
           		Date entrydate = sdf.parse(strentrydate);
           		//Date datelimit = sdf.parse("2016-02-01 00:00:00");
           		Date datelimit = sdf.parse(strdatelimit);
           		LogMessages.logStatus("entrydate---------"+sdf.format(entrydate));
           		LogMessages.logStatus("datelimit---------"+sdf.format(datelimit));	
           		if(entrydate.compareTo(datelimit)<0 || entrydate.compareTo(datelimit)==0){
           			System.out.println("--entrydate is before datelimit");
           			LogMessages.logStatus("--entrydate is before datelimit");
           			return true;
           		}else if(entrydate.compareTo(datelimit)>0){
           			System.out.println("**entrydate is after datelimit");
           			LogMessages.logStatus("**entrydate is after datelimit");
           			return false;
           		}else{
           			System.out.println("How to get here?");
           			LogMessages.logStatus("How to get here?");
           			return false;
           		}             	            
        } catch (Exception e) {      
            LogMessages.logStatus("Exception in convertDate():-" + e.getMessage());      
            return false;
        }
    }
    
    
    public boolean readData(String spid)
    {
    	return true;
    }
    
    @SuppressWarnings({ "null", "null", "null" })
	public void processandcalculatedata1(String winame)
    {
    LogMessages.logStatus("**inside processandcalculatedata1");
   String dbquerypersonalrate="",dbquerybankrate="",dbquerypersonalratematurity="",dbquerybankratematurity="";
  
  
   dbquerypersonalrate=" SELECT BI_CUSTOMERNAME,BI_CAN,WINAME,BI_CPINIDATE,BI_PRINCIPALAMOUNT, "
   +" BI_RATETYPE,BICP_TENOR,BI_PERSONALRATE,solid,CP_PrimaryUniqueRefNo  from "
   +" NG_EXT_MONEYMARKET where current_ws='Branch_System' and BI_RATETYPE='Personal Rate' and wicreatedflag IS NULL";
 
   dbquerybankrate=" SELECT BI_CUSTOMERNAME,BI_CAN,WINAME,BI_CPINIDATE,BI_PRINCIPALAMOUNT, "
   +" BI_RATETYPE,BICP_TENOR,BI_PERSONALRATE,solid,CP_PrimaryUniqueRefNo  from "
   +" NG_EXT_MONEYMARKET where current_ws='Branch_System' and BI_RATETYPE='Bank Rate'and wicreatedflag IS NULL ";
   
 String todaydate=null;

 LogMessages.logStatus("**inside processandcalculatedata1 :: dbquerypersonalrate :: "+dbquerypersonalrate);	
 try
 {
   WFXmlResponse xmlResponse = null;
   WFXmlList xmlList = null;
   String retvalxml = "";
   String sMainCode = "";
   String tenor,rate,totalamount,rate_type,Tr_count,Status,parent_winame,bicpinidate,bicustomername,bican,SOLID,cpunirefno;
   Date d= new Date();
	//today date in dd/MM/yyyy
todaydate=new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(d);
LogMessages.logStatus("todaydate::" + todaydate);

   retvalxml = APSelect(dbquerypersonalrate);
   LogMessages.logStatus("retvalxml1::" + retvalxml);
   xmlResponse = new WFXmlResponse(retvalxml);
   xmlList = xmlResponse.createList("Records", "Record");
  
   LogMessages.logStatus("xmlResponse::" + xmlResponse.toString());
   sMainCode = xmlResponse.getVal("MainCode");
   String  size=xmlResponse.getVal("TotalRetrieved");
   String winameperarr[] = new String[Integer.parseInt(size)];
   int wicountper=0;
   LogMessages.logStatus("sMainCode :: "+sMainCode);
   if (sMainCode.equals("0"))
   {
     if (xmlResponse.toString().contains("<Record>"))
     {
       for (; xmlList.hasMoreElements(); xmlList.skip())
       {	           
    	 parent_winame= xmlList.getVal("WINAME");
    	 winameperarr[wicountper++]=parent_winame;
    	 bicpinidate=xmlList.getVal("BI_CPINIDATE");
    	 bicpinidate= bicpinidate.split(" ")[0];
         tenor = xmlList.getVal("BICP_TENOR");
         rate = xmlList.getVal("BI_PERSONALRATE");
         totalamount = xmlList.getVal("BI_PRINCIPALAMOUNT");
         rate_type = xmlList.getVal("BI_RATETYPE");
        // Tr_count = xmlList.getVal("TRCOUNT");
         bicustomername=xmlList.getVal("BI_CUSTOMERNAME");
         bican=xmlList.getVal("BI_CAN");
         SOLID=xmlList.getVal("SOLID");
         Status="Awaiting Treasury";
         cpunirefno=xmlList.getVal("CP_PRIMARYUNIQUEREFNO");
         LogMessages.logStatus("tenor :: "+tenor);
         LogMessages.logStatus("rate :: "+rate);
         LogMessages.logStatus("totalamount :: "+totalamount);
         LogMessages.logStatus("rate_type :: "+rate_type);
         LogMessages.logStatus("cpunirefno :: "+cpunirefno);
        // LogMessages.logStatus("Tr_count :: "+Tr_count);
         LogMessages.logStatus("bicustomername :: "+bicustomername);
         LogMessages.logStatus("bican :: "+bican);
         LogMessages.logStatus("parent_winame :: "+parent_winame);
         LogMessages.logStatus("SOLID :: "+SOLID);
         //NG_MM_CP_UTILITYTABLE table name
        
         
         String wigroupindex="";
         String widata= winame.split("-")[1];
         LogMessages.logStatus("widata:: logic "+widata);
         wigroupindex=widata+tenor+rate+"Personal";
         LogMessages.logStatus("wigroupindex :: "+wigroupindex);
         String colnames="WIGroupIndex,accountname,accountno,parent_winame,bicpinidate,WINAME,TENOR,RATE,TOTALAMOUNT,"
         		+ "RATETYPE,TRSTATUS,insertedtime,SOLID,CPUNIQUEREFNO";
         String colvalues="'"+wigroupindex+"',"+"'"+bicustomername+"','"+bican+"',"+"'"+parent_winame+"','"
         		+bicpinidate+"',"+"'"+winame+"','"+tenor+"',"+"'"+rate+"','"+totalamount+"','"+rate_type+"','"
        		 +Status+"','"+todaydate+"','"+SOLID+"'"+",'"+cpunirefno+"'";
         boolean insertstatus=aPInsert_API("NG_MM_CP_UTILITYTABLE",colnames,colvalues);
         LogMessages.logStatus("insertstatus 1 :: "+insertstatus);
         tenor="";
         rate="";
         totalamount="";
         rate_type="";
         Tr_count="";
         
       }
       LogMessages.logStatus("winameperarr ::"+winameperarr.toString());
       LogMessages.logStatus("winameperarrlength ::"+winameperarr.length);
       //update wicreateflag in external table
      /* for(int i=0;i<winameperarr.length;i++)
       {
      	 LogMessages.logStatus("inside for loop process winame ::"+winameperarr[i]);
      	 String tname="NG_EXT_MONEYMARKET";
       	 String cname="wicreatedflag";
       	 String data="Y";
         String cvalue="'"+data+"'";
      	
           String where=" WINAME ='"+winameperarr[i]+"'";
      	 String retxml = APUpdate(tname, cname, cvalue, where);
      		WFXmlResponse xmlResponseupdate = new WFXmlResponse(retxml);
           if (xmlResponseupdate.getVal("MainCode").equals("0")) {
           	 LogMessages.logStatus("Updated sucessfully for " +winameperarr[i]);  
           } else {
           	 LogMessages.logStatus("Error in updation");
           } 
       }*/
     }
         
    
   }
   else if (sMainCode.equals("15")) {
	   LogMessages.logStatus("Error in Query");
     System.out.println("Error in Query");
   } else if (sMainCode.equals("18")) {
	   LogMessages.logStatus("No more records.");
     System.out.println("No more records.");
   }
  
 }
 catch (Exception e)
 {
	 LogMessages.logStatus("Exception in firstdbquerypersonl ():-" + e.getMessage());
	 LogMessages.logStatus("Exception in firstdbquerypersonl ():-" + e.getCause());
	 LogMessages.logStatus("Exception in firstdbquerypersonl ():-" + e.getStackTrace());
   System.out.println("Exception in firstdbquerypersonl ():-" + e.getMessage());
 }

 LogMessages.logStatus("**inside processandcalculatedata 1:: dbquerybankrate :: "+dbquerybankrate);	
 try
 {
   WFXmlResponse xmlResponse = null;
   WFXmlList xmlList = null;
   String retvalxml = "";
   String sMainCode = "";
   String tenor,rate,totalamount,rate_type,Tr_count,Status,parent_winame,bicpinidate,bicustomername,bican,SOLID,cpunirefno;
   retvalxml = APSelect(dbquerybankrate);
   LogMessages.logStatus("retvalxml2::" + retvalxml);
   xmlResponse = new WFXmlResponse(retvalxml);
   xmlList = xmlResponse.createList("Records", "Record");
   LogMessages.logStatus("xmlResponse::" + xmlResponse.toString());
   sMainCode = xmlResponse.getVal("MainCode");
   String  size=xmlResponse.getVal("TotalRetrieved");
   String winamebankarr[] = new String[Integer.parseInt(size)];
   int wicountbankr=0;
   LogMessages.logStatus("sMainCode :: "+sMainCode);
   if (sMainCode.equals("0"))
   {
     if (xmlResponse.toString().contains("<Record>"))
     {
       for (; xmlList.hasMoreElements(); xmlList.skip())
       {	 
    	 parent_winame= xmlList.getVal("WINAME");
    	 winamebankarr[wicountbankr++]=parent_winame;
      	 bicpinidate=xmlList.getVal("BI_CPINIDATE");  
      	 bicpinidate= bicpinidate.split(" ")[0];
         tenor = xmlList.getVal("BICP_TENOR");
         rate = "";
         totalamount = xmlList.getVal("BI_PRINCIPALAMOUNT");
         rate_type = xmlList.getVal("BI_RATETYPE");
        // Tr_count = xmlList.getVal("TRCOUNT");
         bicustomername=xmlList.getVal("BI_CUSTOMERNAME");
         bican=xmlList.getVal("BI_CAN");
         SOLID=xmlList.getVal("SOLID");
         cpunirefno=xmlList.getVal("CP_PRIMARYUNIQUEREFNO");
         Status="Awaiting Treasury";
         LogMessages.logStatus("tenor :: "+tenor);
         LogMessages.logStatus("rate :: "+rate);
         LogMessages.logStatus("tota2lamount :: "+totalamount);
         LogMessages.logStatus("rate_type :: "+rate_type);
         LogMessages.logStatus("SOLID :: "+SOLID);
         LogMessages.logStatus("cpunirefno :: "+cpunirefno);
        // LogMessages.logStatus("Tr_count :: "+Tr_count);
         
         //first table in form
         String wigroupindex=null;
         String widata= winame.split("-")[1];
         LogMessages.logStatus("widata:: logic "+widata);
         wigroupindex=widata+tenor+"Bank";
         LogMessages.logStatus("wigroupindex :: "+wigroupindex);
         String colnames="WIGroupIndex,accountname,accountno,parent_winame,bicpinidate,WINAME,TENOR,RATE,"
         		+ "TOTALAMOUNT,RATETYPE,TRSTATUS,insertedtime,SOLID,CPUNIQUEREFNO";
         String colvalues="'"+wigroupindex+"',"+"'"+bicustomername+"','"+bican+"',"+"'"+parent_winame+"','"
         		+ ""+bicpinidate+"',"+"'"+winame+"','"+tenor+"',"+"'"+rate+"','"+totalamount+"','"+rate_type+"',"
         				+ "'"+Status+"','"+todaydate+"','"+SOLID+"'"+",'"+cpunirefno+"'";
         boolean insertstatus=aPInsert_API("NG_MM_CP_UTILITYTABLE",colnames,colvalues);
         LogMessages.logStatus("insertstatus :: "+insertstatus);
            }
       LogMessages.logStatus("winamebankarr ::"+winamebankarr.toString());
       LogMessages.logStatus("winamebankarr ::"+winamebankarr.length);
       //update wicreateflag in external table
     /* for(int i=0;i<winamebankarr.length;i++)
       {
      	 LogMessages.logStatus("inside for loop process winamebankarr winame ::"+winamebankarr[i]);
      	 String tname="NG_EXT_MONEYMARKET";
       	 String cname="wicreatedflag";
      	 String cvalue="'Y'";
           String where=" WINAME ='"+winamebankarr[i]+"'";
      	 String retxml = APUpdate(tname, cname, cvalue, where);
      		WFXmlResponse xmlResponseupdate = new WFXmlResponse(retxml);
           if (xmlResponseupdate.getVal("MainCode").equals("0")) {
           	 LogMessages.logStatus("Updated sucessfully for " +winamebankarr[i]);  
           } else {
           	 LogMessages.logStatus("Error in updation");
           } 
       }*/
     }
     else
     {
    	 LogMessages.logStatus("NO RECORDS");
       System.out.println("NO RECORDS");
     }
    
   }
   else if (sMainCode.equals("15")) {
	   LogMessages.logStatus("Error in Query");
     System.out.println("Error in Query");
   } else if (sMainCode.equals("18")) {
	   LogMessages.logStatus("No more records.");
     System.out.println("No more records.");
   }
  
 }
 catch (Exception e)
 {
	 LogMessages.logStatus("Exception in processcalcualte1():-  dbquerybankrate ():-" + e.getMessage());
	 LogMessages.logStatus("Exception in processcalcualte1():- dbquerybankrate ():-" + e.getCause());
	 LogMessages.logStatus("Exception in processcalcualte1():- dbquerybankrate ():-" + e.getStackTrace());
   System.out.println("Exception in processcalcualte1():-" + e.getMessage());
 }

 }
    
    
    public void processandcalculatedata2(String winame)
    {
    LogMessages.logStatus("**inside processandcalculatedata2");
   /* Map<String, String> qValriableAttr = new HashMap<String, String>();
    Map<String, Map<String, String>> complexAttributes = new HashMap<String, Map<String, String>>();*/
   String dbquerypersonalrate="",dbquerybankrate="",dbquerypersonalratematurity="",dbquerybankratematurity="";
   
   dbquerypersonalratematurity=" SELECT count(*) as TransactionCount,TOTALAMOUNT,RATETYPE,TENOR,rate,BICPINIDATE,"
	   		+ "wigroupindex from NG_MM_CP_UTILITYTABLE where "
	  + " ratetype='Personal Rate' and winame ='"+winame+"' "
	  +" group by transactioncount,RATETYPE,TENOR,rate,BICPINIDATE,wigroupindex,TOTALAMOUNT ";
	   

   dbquerybankratematurity=" SELECT count(*) as TransactionCount,TOTALAMOUNT,RATETYPE,TENOR,rate,BICPINIDATE,"
	   		+ "wigroupindex from NG_MM_CP_UTILITYTABLE where "
	   	  + " ratetype='Bank Rate'  and winame ='"+winame+"' "
	   	  +" group by transactioncount,RATETYPE,TENOR,rate,BICPINIDATE,wigroupindex,TOTALAMOUNT ";


 LogMessages.logStatus("**inside processandcalculatedata2 :: dbquerypersonalratematurity :: "+dbquerypersonalratematurity);	
 try
 {
   WFXmlResponse xmlResponse = null;
   WFXmlList xmlList = null;
   String retvalxml = "";
   String sMainCode = "";
   String tenor,rate,principalamount,rate_type,Tr_count,Status,parent_winame,bicpinidate,wigroupindex;
   Date d= new Date();

   retvalxml = APSelect(dbquerypersonalratematurity);
   LogMessages.logStatus("retvalxml1::" + retvalxml);
   xmlResponse = new WFXmlResponse(retvalxml);
   xmlList = xmlResponse.createList("Records", "Record");
   LogMessages.logStatus("xmlResponse::" + xmlResponse.toString());
   sMainCode = xmlResponse.getVal("MainCode");
   LogMessages.logStatus("sMainCode :: "+sMainCode);
   if (sMainCode.equals("0"))
   {
     if (xmlResponse.toString().contains("<Record>"))
     {
       for (; xmlList.hasMoreElements(); xmlList.skip())
       {	           
    	
    	 bicpinidate=xmlList.getVal("BICPINIDATE");
    	 principalamount=xmlList.getVal("TOTALAMOUNT");
         tenor = xmlList.getVal("TENOR");
         rate = xmlList.getVal("RATE");
         wigroupindex = xmlList.getVal("WIGROUPINDEX");
         rate_type = xmlList.getVal("RATETYPE");
         Tr_count = xmlList.getVal("TRANSACTIONCOUNT");
         
         LogMessages.logStatus("tenor :: "+tenor);
         LogMessages.logStatus("rate :: "+rate);
         LogMessages.logStatus("wigroupindex :: "+wigroupindex);
         LogMessages.logStatus("rate_type :: "+rate_type);
         LogMessages.logStatus("Tr_count :: "+Tr_count);
         LogMessages.logStatus("principalamount :: "+principalamount);
         
         //To push data second table in form
         LogMessages.logStatus("wigroupindex :: "+wigroupindex);
         String colnames="WIGROUPINDEX,BICPINIDATE,"
            		+ "WINAME,TENOR,RATE,RATETYPE,TRANSACTIONCOUNT,TOTALAMOUNT";
         String colvalues="'"+wigroupindex+"','"+bicpinidate+"',"+"'"+winame+"','"+tenor+"',"+"'"+rate+"','"+rate_type+"','"+Tr_count+"'"+",'"+principalamount+"'";
         boolean insertstatus=aPInsert_API("NG_MM_CP_UTILITY_ALLOCATIONS",colnames,colvalues);
         LogMessages.logStatus("insertstatus 1 :: "+insertstatus);
         
         tenor="";
         rate="";
         principalamount="";
         rate_type="";
         Tr_count="";
         
       }
     
      
     }
     else
     {
    	 LogMessages.logStatus("NO RECORDS");
       System.out.println("NO RECORDS");
     }
   }
   else if (sMainCode.equals("15")) {
	   LogMessages.logStatus("Error in Query");
     System.out.println("Error in Query");
   } else if (sMainCode.equals("18")) {
	   LogMessages.logStatus("No more records.");
     System.out.println("No more records.");
   }
  
 }
 catch (Exception e)
 {
	 LogMessages.logStatus("Exception in ():-" + e.getMessage());
   System.out.println("Exception in ():-" + e.getMessage());
 }

 LogMessages.logStatus("**inside processandcalculatedata :: dbquerybankrate :: "+dbquerybankrate);	
 try
 {
   WFXmlResponse xmlResponse = null;
   WFXmlList xmlList = null;
   String retvalxml = "";
   String sMainCode = "";
   String tenor,rate,principalamount,rate_type,Tr_count,Status,parent_winame,bicpinidate,bicustomername,bican,wigroupindex;
   retvalxml = APSelect(dbquerybankratematurity);
   LogMessages.logStatus("retvalxml2::" + retvalxml);
   xmlResponse = new WFXmlResponse(retvalxml);
   xmlList = xmlResponse.createList("Records", "Record");
   LogMessages.logStatus("xmlResponse::" + xmlResponse.toString());
   sMainCode = xmlResponse.getVal("MainCode");
   LogMessages.logStatus("sMainCode :: "+sMainCode);
   if (sMainCode.equals("0"))
   {
     if (xmlResponse.toString().contains("<Record>"))
     {
       for (; xmlList.hasMoreElements(); xmlList.skip())
       {	 
    	   bicpinidate=xmlList.getVal("BICPINIDATE");
    	   principalamount=xmlList.getVal("TOTALAMOUNT");
           tenor = xmlList.getVal("TENOR");
           rate = xmlList.getVal("RATE");
           wigroupindex = xmlList.getVal("WIGROUPINDEX");
           rate_type = xmlList.getVal("RATETYPE");
           Tr_count = xmlList.getVal("TRANSACTIONCOUNT");
           
           LogMessages.logStatus("tenor :: "+tenor);
           LogMessages.logStatus("rate :: "+rate);
           LogMessages.logStatus("principalamount :: "+principalamount);
           
           LogMessages.logStatus("wigroupindex :: "+wigroupindex);
           LogMessages.logStatus("rate_type :: "+rate_type);
           LogMessages.logStatus("Tr_count :: "+Tr_count);
         
           //To push data second table in form
           LogMessages.logStatus("wigroupindex :: "+wigroupindex);
           String colnames="WIGROUPINDEX,BICPINIDATE,"
           		+ "WINAME,TENOR,RATE,RATETYPE,TRANSACTIONCOUNT,TOTALAMOUNT";
           String colvalues="'"+wigroupindex+"','"+bicpinidate+"',"+"'"+winame+"','"+tenor+"',"+"'"+rate+"','"+rate_type+"','"+Tr_count+"'"+",'"+principalamount+"'";
           boolean insertstatus=aPInsert_API("NG_MM_CP_UTILITY_ALLOCATIONS",colnames,colvalues);
           LogMessages.logStatus("insertstatus 2 :: "+insertstatus);
         
        /* //second table in form
         //calculate maturity date
         String maturitydate="";
         String colnames2="parent_winame,bicpinidate,WINAME,TENOR,RATE,TOTALAMOUNT,RATETYPE,TRANSACTIONCOUNT,TRSTATUS,insertedtime,maturitydate";
         String colvalues2="'"+parent_winame+"','"+bicpinidate+"',"+"'"+winame+"','"+tenor+"',"+"'"+rate+"','"+totalamount+"','"+rate_type+"','"+Tr_count+"','"
         +Status+"','"+todaydate+"'"+",'"+maturitydate+"'";
         boolean insertstatus2=aPInsert_API("NG_MM_CP_UTILITY_ALLOCSECTION",colnames2,colvalues2);
         LogMessages.logStatus("insertstatus 2 :: "+insertstatus2);*/
       }
     
      
     }
     else
     {
    	 LogMessages.logStatus("NO RECORDS");
       System.out.println("NO RECORDS");
     }
   }
   else if (sMainCode.equals("15")) {
	   LogMessages.logStatus("Error in Query");
     System.out.println("Error in Query");
   } else if (sMainCode.equals("18")) {
	   LogMessages.logStatus("No more records.");
     System.out.println("No more records.");
   }
  
 }
 catch (Exception e)
 {
	LogMessages.logStatus("Exception in GetdocindexFromDB():-" + e.getMessage());
   System.out.println("Exception in GetdocindexFromDB():-" + e.getMessage());
 }

 }
    
    
  public void postingintergrations(String winame)
  {
	  LogMessages.logStatus("postingintergrations :: Success and failure integrations :: "+winame );
	  
	  String fetchdata="SELECT trstatus,bankrate,CBNRATE,NEWALLOCPERC,PARENT_WINAME,investmentamount"
	  		+ " FROM NG_MM_CP_UTILITYTABLE WHERE integrationstatus='N' AND  EXECUTEINTGERATION IS NULL"
	  		+ " AND TRSTATUS!='Awaiting Treasury'";
	  LogMessages.logStatus("postingintergrations :: Success and failure integrations fetchdata :: "+fetchdata );
	  try
	  {
	    WFXmlResponse xmlResponse = null;
	    WFXmlList xmlList = null;
	    String retvalxml = "";
	    String sMainCode = "";
	    String trstatus,bankrate,CBNRATE,NEWALLOCPERC,PARENT_WINAME,investmentamount;
	    retvalxml = APSelect(fetchdata);
	    LogMessages.logStatus("retvalxml2::" + retvalxml);
	    xmlResponse = new WFXmlResponse(retvalxml);
	    xmlList = xmlResponse.createList("Records", "Record");
	    LogMessages.logStatus("xmlResponse::" + xmlResponse.toString());
	    sMainCode = xmlResponse.getVal("MainCode");
	    LogMessages.logStatus("sMainCode :: "+sMainCode);
	    if (sMainCode.equals("0"))
	    {
	      if (xmlResponse.toString().contains("<Record>"))
	      {
	        for (; xmlList.hasMoreElements(); xmlList.skip())
	        {	 
	        	trstatus=xmlList.getVal("TRSTATUS");
	        	bankrate=xmlList.getVal("BANKRATE");
	        	CBNRATE = xmlList.getVal("CBNRATE");
	        	NEWALLOCPERC = xmlList.getVal("NEWALLOCPERC");
	        	PARENT_WINAME=xmlList.getVal("PARENT_WINAME");
	        	investmentamount=xmlList.getVal("INVESTMENTAMOUNT");
	            LogMessages.logStatus("trstatus :: "+trstatus);
	            LogMessages.logStatus("bankrate :: "+bankrate);
	            LogMessages.logStatus("CBNRATE :: "+CBNRATE);
	            LogMessages.logStatus("NEWALLOCPERC :: "+NEWALLOCPERC);  
	            LogMessages.logStatus("PARENT_WINAME :: "+PARENT_WINAME);  
	            LogMessages.logStatus("investmentamount :: "+investmentamount);  
	            WFXmlResponse xmlResponse2 = null;
	    	    WFXmlList xmlList2 = null;
	    	    String retvalxml2 = "";
	    	    String sMainCode2 = "";
	            if(!trstatus.equalsIgnoreCase("") && !bankrate.equalsIgnoreCase("") 
	            		&& !CBNRATE.equalsIgnoreCase("")  && !NEWALLOCPERC.equalsIgnoreCase("") 
	            		&& !investmentamount.equalsIgnoreCase(""))
	            {
	            	if(trstatus.equalsIgnoreCase("Awaiting Maturity"))
	            	{
	            		 LogMessages.logStatus("inside  Awaiting Matu and cp.EXECUTEINTGERATION=NULLrity block:: "); 
	            		//success Integration
	            		 /* String successwinames="SELECT ACCOUNTNO,ACCOUNTNAME,trstatus,bankrate,CBNRATE,NEWALLOCPERC,PARENT_WINAME,investmentamount"
	            			  		+ " FROM NG_MM_CP_UTILITYTABLE WHERE TRSTATUS='"+trstatus+"' AND  integrationstatus='N'";
	            		  */
	            		  String successwinames="SELECT cp.ACCOUNTNO,cp.ACCOUNTNAME,cp.trstatus,cp.bankrate,cp.CBNRATE,"
	            		  		+ "cp.NEWALLOCPERC,"
								+ "cp.PARENT_WINAME,cp.investmentamount FROM NG_MM_CP_UTILITYTABLE  cp JOIN NG_EXT_MONEYMARKET ext "
								+ "on ext.winame=cp.parent_winame and ext.current_ws='Branch_System' and cp.trstatus='Awaiting Maturity' "
								+ " and cp.integrationstatus='N' and cp.EXECUTEINTGERATION IS NULL";
									            		  
	            		  LogMessages.logStatus("successwinames :: "+successwinames);  
	            		    retvalxml2 = APSelect(successwinames);
	            		    LogMessages.logStatus("retvalxml2::" + retvalxml2);
	            		    xmlResponse2 = new WFXmlResponse(retvalxml2);
	            		    xmlList2 = xmlResponse2.createList("Records", "Record");
	            		    LogMessages.logStatus("xmlResponse2::" + xmlResponse2.toString());
	            		    sMainCode2 = xmlResponse2.getVal("MainCode");
	            		    LogMessages.logStatus("sMainCode2 :: "+sMainCode2);
	            		    String parentwiname,investmentamt,accountno,accountname,solid;
	            		    if (sMainCode2.equals("0"))
	            		    {
	            		      if (xmlResponse2.toString().contains("<Record>"))
	            		      {
	            		        for (; xmlList2.hasMoreElements(); xmlList2.skip())
	            		        {	 
	            		        	
	            		        	parentwiname=xmlList2.getVal("PARENT_WINAME");
	            		        	investmentamt = xmlList2.getVal("INVESTMENTAMOUNT");
	            		        	accountno = xmlList2.getVal("ACCOUNTNO");
	            		        	accountname = xmlList2.getVal("ACCOUNTNAME");
	            		        	
	            		        	LogMessages.logStatus("parentwiname :: "+parentwiname);
	            		        	LogMessages.logStatus("investmentamt :: "+investmentamt);
	            		        	LogMessages.logStatus("accountno :: "+accountno);
	            		        	LogMessages.logStatus("accountname :: "+accountname);
	            		        	
	            		        	
	            		        	//call success integration
	            		        	if(!parentwiname.equalsIgnoreCase("") && !investmentamt.equalsIgnoreCase("")
	            		        			&& !accountno.equalsIgnoreCase("") )
	            		        	{
	            		        		LogMessages.logStatus(" Success debit Posting transaction for :: "+parentwiname);
	            		        		LogMessages.logStatus(" Success debit Posting transaction inside :: ");
	            		        		String response=SuccessIntegrationService(parentwiname, accountno,investmentamt);
		            		        	LogMessages.logStatus("Success Post response :: "+response);
		            		        	
		            		        	 if(response.equalsIgnoreCase("Success") ||
			            		            	 response.equalsIgnoreCase("Failed"))
		            		            		   {
		            		        		 LogMessages.logStatus("Success post rsponse inside success or failed :: "+response);
		            		        		    response=response;
		            		            		   }else
		            		            		   {
		            		            			   LogMessages.logStatus("Success post rsponse inside other:: "+response);
		            		            			   response="failed";   
		            		            		   }
		            		        	
		            		        	//update to Utility table
		            		        	String tname="NG_MM_CP_UTILITYTABLE";
		            		           	 String cname="integrationstatus,integrationtype,EXECUTEINTGERATION";
		            		           	 String integType="SuccessDebit";
		            		             String cvalue="'"+response+"','"+integType+"',"+"'Y'";
		            		             String where="parent_winame ='"+parentwiname+"'";
		            		              
		            		            	   String retxml = APUpdate(tname, cname, cvalue, where); 
			            		          		WFXmlResponse xmlResponseupdate = new WFXmlResponse(retxml);
			            		               if (xmlResponseupdate.getVal("MainCode").equals("0")) {
			            		               	 LogMessages.logStatus("Updated sucessfully for " +parentwiname);  
			            		               } else {
			            		               	 LogMessages.logStatus("Error in updation");
			            		               }    
		            		               
		            		          	
		            		               //update to external table
		            		                String tname1="NG_EXT_MONEYMARKET";
			            		           	 String cname1="APISTATUS,APITYPE";
			            		           	 String apitype="SuccessDebit";
			            		             String cvalue1="'"+response+"','"+apitype+"'";
			            		             String where1="winame ='"+parentwiname+"'";
			            		               
	            		            	   String retxml1 = APUpdate(tname1, cname1, cvalue1, where1); 
		            		          		WFXmlResponse xmlResponseupdate1 = new WFXmlResponse(retxml1);
		            		               if (xmlResponseupdate1.getVal("MainCode").equals("0")) {
		            		               	 LogMessages.logStatus("Updated sucessfully for " +parentwiname);  
		            		               } else {
		            		               	 LogMessages.logStatus("Error in updation");
		            		               }    
			            		               
		            		               unlockWIs(parentwiname);
		            		               getworkitem(parentwiname, parentwiname);
		            		               CompleteWorkItem(parentwiname);
		            		               
	            		        	}
	            		        	
	            		        	
	            		        }
	            		      }
	            		    }
	            	}
	            	if(trstatus.equalsIgnoreCase("Failed"))
	            	{
	            		 LogMessages.logStatus("inside  Failed block:: "); 
	            		//failure Integration
	            		/* String failedwinames="SELECT ACCOUNTNO,ACCOUNTNAME,trstatus,bankrate,CBNRATE,NEWALLOCPERC,PARENT_WINAME,investmentamount"
	            			  		+ " FROM NG_MM_CP_UTILITYTABLE WHERE TRSTATUS='"+trstatus+"' AND integrationstatus='N' " ;
	            		*/
	            		 String failedwinames="SELECT cp.ACCOUNTNO,cp.ACCOUNTNAME,cp.trstatus,cp.bankrate,cp.CBNRATE,"
+ "cp.NEWALLOCPERC,cp.PARENT_WINAME,cp.investmentamount "
+" FROM NG_MM_CP_UTILITYTABLE  cp JOIN NG_EXT_MONEYMARKET ext "
+" on ext.winame=cp.parent_winame and ext.current_ws='Branch_System' and cp.trstatus='Failed' "
+" and cp.integrationstatus='N' and EXECUTEINTGERATION IS NULL";
	            		 LogMessages.logStatus("failedwinames :: "+failedwinames);  
	            		 retvalxml2 = APSelect(failedwinames);
	            		    LogMessages.logStatus("retvalxml2::" + retvalxml2);
	            		    xmlResponse2 = new WFXmlResponse(retvalxml2);
	            		    xmlList2 = xmlResponse2.createList("Records", "Record");
	            		    LogMessages.logStatus("xmlResponse2::" + xmlResponse2.toString());
	            		    sMainCode2 = xmlResponse2.getVal("MainCode");
	            		    LogMessages.logStatus("sMainCode2 :: "+sMainCode2);
	            		    String parentwiname,investmentamt,accountno,accountname;
	            		    if (sMainCode2.equals("0"))
	            		    {
	            		      if (xmlResponse2.toString().contains("<Record>"))
	            		      {
	            		        for (; xmlList2.hasMoreElements(); xmlList2.skip())
	            		        {	 
	            		        	
	            		        	parentwiname=xmlList2.getVal("PARENT_WINAME");
	            		        	investmentamt = xmlList2.getVal("INVESTMENTAMOUNT");
	            		        	accountno = xmlList2.getVal("ACCOUNTNO");
	            		        	accountname = xmlList2.getVal("ACCOUNTNAME");
	            		        	LogMessages.logStatus("parentwiname :: "+parentwiname);
	            		        	LogMessages.logStatus("investmentamt :: "+investmentamt);
	            		        	LogMessages.logStatus("accountno :: "+accountno);
	            		        	
	            		        	//call FAilure integration
	            		        	if(!parentwiname.equalsIgnoreCase("") && !investmentamt.equalsIgnoreCase("")
	            		        			&& !accountno.equalsIgnoreCase("") )
	            		        	{
	            		        		LogMessages.logStatus(" Failure Credit Posting transaction for :: "+parentwiname);
	            		        		LogMessages.logStatus(" FAilure credit Posting transaction inside :: ");
	            		        		String response=FailureIntegrationService(parentwiname, accountno,investmentamt);
		            		        	LogMessages.logStatus("Failure Post response :: "+response);
		            		        	 if(response.equalsIgnoreCase("Success") ||
			            		            	 response.equalsIgnoreCase("Failed"))
		            		            		   {
		            		        		 LogMessages.logStatus("FAilure post rsponse inside success or failed :: "+response);
		            		        		    response=response;
		            		            		   }else
		            		            		   {
		            		            			   LogMessages.logStatus("FAilure post rsponse inside other:: "+response);
		            		            			   response="failed";   
		            		            		   }
	            		        	
		            		        	String tname="NG_MM_CP_UTILITYTABLE";
		            		           	 String cname="integrationstatus,integrationtype,EXECUTEINTGERATION";
		            		           	 String integType="FailureCredit";
		            		             String cvalue="'"+response+"','"+integType+"',"+"'Y'";
		            		             String where="parent_winame ='"+parentwiname+"'";
		            		             //update to utiility table
		            		              
	            		            	   String retxml = APUpdate(tname, cname, cvalue, where); 
		            		          		WFXmlResponse xmlResponseupdate = new WFXmlResponse(retxml);
		            		               if (xmlResponseupdate.getVal("MainCode").equals("0")) {
		            		               	 LogMessages.logStatus("Updated sucessfully for " +parentwiname);  
		            		               } else {
		            		               	 LogMessages.logStatus("Error in updation");
		            		               }    
		            		               
		    
		            		               //update to external table
		            		                String tname1="NG_EXT_MONEYMARKET";
			            		           	 String cname1="APISTATUS,APITYPE";
			            		           	 String apitype="FailureCredit";
			            		             String cvalue1="'"+response+"','"+apitype+"'";
			            		             String where1="winame ='"+parentwiname+"'";
			            		               
	            		            	   String retxml2 = APUpdate(tname1, cname1, cvalue1, where1); 
		            		          		WFXmlResponse xmlResponseupdate2 = new WFXmlResponse(retxml2);
		            		               if (xmlResponseupdate2.getVal("MainCode").equals("0")) {
		            		               	 LogMessages.logStatus("Updated sucessfully for " +parentwiname);  
		            		               } else {
		            		               	 LogMessages.logStatus("Error in updation");
		            		               }    
		            		               unlockWIs(parentwiname);
		            		               getworkitem(parentwiname, parentwiname);
		            		               CompleteWorkItem(parentwiname);
	            		        	}
	            		        	
	            		        }
	            		      }
	            		    }
	            	}
	            	
	            }
	            else
	            {
	            	 LogMessages.logStatus("No sufficient data to perform integration");
	            }
	        
	        }
	      
	       
	      }
	      
	      {
	     	 LogMessages.logStatus("NO RECORDS");
	        System.out.println("NO RECORDS");
	      }
	    }
	    else if (sMainCode.equals("15")) {
	 	   LogMessages.logStatus("Error in Query");
	      System.out.println("Error in Query");
	    } else if (sMainCode.equals("18")) {
	 	   LogMessages.logStatus("No more records.");
	      System.out.println("No more records.");
	    }
	   
	  }
	  catch (Exception e)
	  {
	 	LogMessages.logStatus("Exception in posting transaction :-" + e.getMessage() + "st "+e.getStackTrace() +"lm"+e.getLocalizedMessage());
	    System.out.println("Exception in in posting transaction  :-" + e.getMessage());
	  }

  }
  
  public void postingmaturtity() throws Exception
  {
	  LogMessages.logStatus(" postingmaturtity: postingmaturtity :: ifr");
	  //Read workitems from awaiting maturity---638 queueid in UAT --Money_Mkt_Awaiting_MaturityUtility Workstep
	  String casetype="Awaiting mature";
	  fetchqueue("638",casetype);
	  LogMessages.logStatus(" End of postingmaturtity :: ifr");

  }
  
  public String SuccessIntegrationService(String winame,String accno,String invesamt) {
		
		LogMessages.logStatus("Success inputXML: SuccessIntegrationService :: ifr");
		LogMessages.logStatus("Success inputXML: SuccessIntegrationService ::winame :: "+winame);
		LogMessages.logStatus("Success inputXML: SuccessIntegrationService ::accno :: "+accno);
		LogMessages.logStatus("Success inputXML: SuccessIntegrationService ::::invesamt :: "+invesamt);
		  Date d= new Date();
		 String todaydate=new SimpleDateFormat("YYYY-MM-DD HH:mm:ss").format(d);
		LogMessages.logStatus("Success inputXML: todaydate ::"+todaydate);
		
		String outputXml;
		String servicename="SuccessDebit";
	  
	   
		//String inputXML=reqXML.requestDebitCreditXML(serialNum, debitAcctId, debitBranchId, debitFlg, debitAmountValue,debitCurrencyCode, partTrnRmks, processType,todayDate );
		
		String inputXML="<XferTrnAddRequest><XferTrnAddRq><XferTrnHdr><TrnType>T</TrnType><TrnSubType>CP</TrnSubType>"
+ "</XferTrnHdr><XferTrnDetail><PartTrnRec>"
+ "<SerialNum>1</SerialNum>"
+ "<AcctId>"
+ "<AcctId>"+accno+"</AcctId>"
+ "<BankInfo>"
+ "<BranchId>201</BranchId>"
+ "</BankInfo>"
+ "</AcctId>"
+ "<CreditDebitFlg>D</CreditDebitFlg>"
+ "<TrnAmt>"
+ "<amountValue>"+invesamt+"</amountValue>"
+ "<currencyCode>NGN</currencyCode>"
+ "</TrnAmt>"
+ "<TrnParticulars>okay</TrnParticulars>"
+ "<PartTrnRmks>"+winame+"</PartTrnRmks>"
+ "<ValueDt>"+todaydate+"</ValueDt>"
+ "</PartTrnRec><PartTrnRec>"
+ "<SerialNum>2</SerialNum>"
+ "<AcctId><AcctId>2007660814</AcctId>"
+ "<BankInfo>"
+ "<BranchId>201</BranchId>"
+ "</BankInfo></AcctId>"
+ "<CreditDebitFlg>C</CreditDebitFlg><TrnAmt>"
+ "<amountValue>"+invesamt+"</amountValue>"
+ "<currencyCode>NGN</currencyCode></TrnAmt>"
+ "<TrnParticulars>okay</TrnParticulars>"
+ "<PartTrnRmks>"+winame+"</PartTrnRmks>"
+ "<ValueDt>"+todaydate+"</ValueDt>"
+ "</PartTrnRec>"
+ "</XferTrnDetail>"
+ "</XferTrnAddRq>"
+ "<XferTrnAdd_CustomData>"
+ "<TRAN isMultiRec=\"Y\"><SRLNUM>1</SRLNUM>"
+ "<TRANCODE>005</TRANCODE>"
+ "<REMARKS2>"+winame+"</REMARKS2></TRAN><TRAN isMultiRec=\"Y\">"
+ "<SRLNUM>2</SRLNUM>"
+ "<TRANCODE>005</TRANCODE><REMARKS2>"+winame+"</REMARKS2></TRAN></XferTrnAdd_CustomData>"
+ "</XferTrnAddRequest>";
		LogMessages.logStatus("Success inputXML:"+inputXML);
		String response = null;
	    String resFlag="";
	    String result = "";
		try{
			//SocketService socket= new SocketService();
			LogMessages.logStatus("Success inputXML: inside try");
			 if("SuccessDebit".equalsIgnoreCase(servicename)) {
		          LogMessages.logStatus("Welcome To SuccessDebit");
		           Properties p = new Properties();
	               p.load(new FileInputStream("config.properties")); 
	               String urlString="";
	               urlString = p.getProperty("SuccessDebit");
	               LogMessages.logStatus("Success urlString:"+urlString);
		           String outputResponseXml = "";
		          
		           ConnectToFinacle connecttoFinacle= new ConnectToFinacle();
		           try {
		               outputResponseXml =connecttoFinacle.callService(inputXML,urlString);
		               LogMessages.logStatus("Response Final==" + outputResponseXml);
		               result = outputResponseXml;
		               LogMessages.logStatus("result:::--" + result);
		                   System.out.println("result:::--" + result);
		                  // writeData(out, result);
		                 
		           } catch (Exception e) {
		               result = e.getMessage();
		              LogMessages.logStatus(" Connectfinacle exception result:::--" + result);
		               System.out.println("result:::--" + result);
		              // writeData(out, result);
		           }
		       } 
			//outputXml= socket.executeIntegrationCall(servicename, "1", inputXML);
			outputXml=result;
			LogMessages.logStatus("Response message SuccessIntegrationService:"+outputXml);
			XMLParser xmlParser= new XMLParser(outputXml);
			  resFlag = xmlParser.getValueOf("Status");
			  LogMessages.logStatus(" resFlag from  SuccessIntegrationService :"+resFlag);
	            if (outputXml != null && (outputXml.indexOf("<Status>") != -1)) {
	            	
	               // xmlParser.setInputXML(outputXml);
	            	LogMessages.logStatus("SuccessIntegration ::1 " );
	                LogMessages.logStatus("Status resFlag success post>>>>  : " + resFlag);

	                LogMessages.logStatus(" resFlag Success integration: " + resFlag);
	                if (outputXml != null && (resFlag.trim().equalsIgnoreCase("SUCCESS"))
	                       ) 
	                {
	                	 LogMessages.logStatus(" resFlag Success integration: inside success...123" );
	                	return resFlag;
	                }
	                }
	            else if (outputXml != null
                      && (resFlag.trim().equalsIgnoreCase("FAILURE") || resFlag.trim().equalsIgnoreCase("FAILED"))
                     ) {
	            	LogMessages.logStatus("SuccessIntegration ::2 " );
              	//ifr.setValue("POST_TO_FINACLE_STATUS", resFlag);
	            	 LogMessages.logStatus(" resFlag Success integration: inside failed...123" );
                  String errCode = xmlParser.getValueOf("ErrorCode");

                  LogMessages.logStatus(" success post errCode : " + errCode);
                  String errDesc = xmlParser.getValueOf("ErrorDesc");

                  LogMessages.logStatus(" success post errCode errDesc : " + errDesc);
                  String errType = xmlParser.getValueOf("ErrorType");

                  LogMessages.logStatus(" : errType : " + errType);

                  response = "SuccessIntegrationService Not Done Successfuly. Get ErrorType : " + errType + " ErrorCode : " + errCode
                          + " ErrorDesc : " + errDesc + ".";

                  LogMessages.logStatus("retMsg : 2 : " + response);
                  return resFlag;
              }
          else 
          {
        	  LogMessages.logStatus("SuccessIntegration ::3 " );
              response = "SuccessIntegrationService Not Found.";
              resFlag="Failed";
              LogMessages.logStatus(" retMsg : 3 : " + response);
              return resFlag;
            //  ifr.setValue("POST_TO_FINACLE_STATUS", response);
          }

		}
		 catch (Exception e) {

	            LogMessages.logStatus("Success integration: e.getMessage() : 3 : " + e.getMessage());
	            response = e.getMessage();
	            LogMessages.logStatus("SuccessIntegration ::4" );
	            LogMessages.logStatus("Success integration retMsg : 4 : " + response);

	            return response;
	        }
		return resFlag;
	}


  public String FailureIntegrationService(String winame,String accno,String invesamt) {
		
	  LogMessages.logStatus("FailureIntegrationService inputXML: FailureIntegrationService :: ifr");
	 
		LogMessages.logStatus("failure inputXML: FailureIntegrationService ::winame :: "+winame);
		LogMessages.logStatus("failure inputXML: FailureIntegrationService ::accno :: "+accno);
		LogMessages.logStatus("failure inputXML: FailureIntegrationService ::::invesamt :: "+invesamt);
	 
	  Date d= new Date();
	  String todaydate=new SimpleDateFormat("YYYY-MM-DD HH:mm:ss").format(d);
	  LogMessages.logStatus("FAilure inputXML: todaydate ::"+todaydate);
		
		String outputXml;
		String servicename="FailureCredit";
	   
		//String inputXML=reqXML.requestDebitCreditXML(serialNum, debitAcctId, debitBranchId, debitFlg, debitAmountValue,debitCurrencyCode, partTrnRmks, processType,todayDate );
		String inputXML="<XferTrnAddRequest><XferTrnAddRq><XferTrnHdr><TrnType>T</TrnType><TrnSubType>CP</TrnSubType>"
+ "</XferTrnHdr><XferTrnDetail><PartTrnRec>"
+ "<SerialNum>1</SerialNum>"
+ "<AcctId>"
+ "<AcctId>"+accno+"</AcctId>"
+ "<BankInfo>"
+ "<BranchId>201</BranchId>"
+ "</BankInfo>"
+ "</AcctId>"
+ "<CreditDebitFlg>C</CreditDebitFlg>"
+ "<TrnAmt>"
+ "<amountValue>"+invesamt+"</amountValue>"
+ "<currencyCode>NGN</currencyCode>"
+ "</TrnAmt>"
+ "<TrnParticulars>"+winame+"</TrnParticulars>"
+ "<PartTrnRmks>"+winame+"</PartTrnRmks>"
+ "<ValueDt>"+todaydate+"</ValueDt>"
+ "</PartTrnRec><PartTrnRec>"
+ "<SerialNum>2</SerialNum>"
+ "<AcctId><AcctId>2007660814</AcctId>"
+ "<BankInfo>"
+ "<BranchId>201</BranchId>"
+ "</BankInfo></AcctId>"
+ "<CreditDebitFlg>D</CreditDebitFlg><TrnAmt>"
+ "<amountValue>"+invesamt+"</amountValue>"
+ "<currencyCode>NGN</currencyCode></TrnAmt>"
+ "<TrnParticulars>okay</TrnParticulars>"
+ "<PartTrnRmks>"+winame+"</PartTrnRmks>"
+ "<ValueDt>"+todaydate+"</ValueDt>"
+ "</PartTrnRec>"
+ "</XferTrnDetail>"
+ "</XferTrnAddRq>"
+ "<XferTrnAdd_CustomData>"
+ "<TRAN isMultiRec=\"Y\"><SRLNUM>1</SRLNUM>"
+ "<TRANCODE>005</TRANCODE>"
+ "<REMARKS2>"+winame+"</REMARKS2></TRAN><TRAN isMultiRec=\"Y\">"
+ "<SRLNUM>2</SRLNUM>"
+ "<TRANCODE>005</TRANCODE><REMARKS2>"+winame+"</REMARKS2></TRAN></XferTrnAdd_CustomData>"
+ "</XferTrnAddRequest>";
		LogMessages.logStatus("FAilure Post inputXML:"+inputXML);
		String response = null;
	    String resFlag="";
	    String result = "";
		try{
			LogMessages.logStatus("FailureCredit inputXML: inside try");
			 if ("FailureCredit".equalsIgnoreCase(servicename)) {
		          LogMessages.logStatus("Welcome To FailureCredit");
		           Properties p = new Properties();
	               p.load(new FileInputStream("config.properties")); 
	               String urlString="";
	               urlString = p.getProperty("FailureCredit");
		          
		           String outputResponseXml="";
		           try {
		        	   ConnectToFinacle connecttoFinacle= new ConnectToFinacle();
		               outputResponseXml = connecttoFinacle.callService(inputXML,urlString);
		              LogMessages.logStatus("Response Final==" + outputResponseXml);
		                   result = outputResponseXml;
		                  LogMessages.logStatus("result:::--" + result);
		                   System.out.println("result:::--" + result);
		                 
		           } catch (Exception e) {
		               result = e.getMessage();
		              LogMessages.logStatus("result:::--" + result);
		               System.out.println("result:::--" + result);
		               //writeData(out, result);
		           }
		       } 
			//outputXml= socket.executeIntegrationCall(servicename, "", inputXML);
			 outputXml=result;
			 LogMessages.logStatus("Response message FailureIntegrationService:"+outputXml);
			XMLParser xmlParser= new XMLParser(outputXml);
			 resFlag = xmlParser.getValueOf("Status");
			 LogMessages.logStatus("resFlag from integration:::--" + resFlag);
	            if (outputXml != null && (outputXml.indexOf("<Status>") != -1)) {
	            	
	               // xmlParser.setInputXML(outputXml);
	               
	                LogMessages.logStatus("Status resFlag FAilure post>>>>  : " + resFlag);

	                LogMessages.logStatus(" resFlag Failure integration: " + resFlag);
	                if (outputXml != null && (resFlag.trim().equalsIgnoreCase("SUCCESS"))
	                        && (outputXml.indexOf("<ErrorDetail>") == -1)) 
	                {
	                	 LogMessages.logStatus("FAilureIntegration :: 1" );
	                	return resFlag;
	                }
	                }
	            else if (outputXml != null
                    && (resFlag.trim().equalsIgnoreCase("FAILURE") || resFlag.trim().equalsIgnoreCase("FAILED"))
                    && (outputXml.indexOf("<ErrorDetail>") != -1)) {
	            	 LogMessages.logStatus("FAilureIntegration :: 3" );
            	//ifr.setValue("POST_TO_FINACLE_STATUS", resFlag);
	            	 LogMessages.logStatus("FAilureIntegration :: 2" );
                String errCode = xmlParser.getValueOf("ErrorCode");

                LogMessages.logStatus(" FailureIntegrationService post errCode : " + errCode);
                String errDesc = xmlParser.getValueOf("ErrorDesc");

                LogMessages.logStatus(" FailureIntegrationService post errCode errDesc : " + errDesc);
                String errType = xmlParser.getValueOf("ErrorType");

                LogMessages.logStatus("Place Lien : errType : " + errType);

                response = "FAilureIntegrationService Not Done Successfuly. Get ErrorType : " + errType + " ErrorCode : " + errCode
                        + " ErrorDesc : " + errDesc + ".";
           
                LogMessages.logStatus("retMsg : 2 : " + response);
                return resFlag;
            }
        else 
        {
        	 LogMessages.logStatus("FAilureIntegration :: 4" );
            response = "FailureIntegrationService Not Found.";
            resFlag="Failed";
            
            LogMessages.logStatus("Place Lien : retMsg : 3 : " + response);
          //  ifr.setValue("POST_TO_FINACLE_STATUS", response);
            return resFlag;
        }

		}
		 catch (Exception e) {

	            LogMessages.logStatus("FailureIntegrationService e.getMessage() : 3 : " + e.getMessage());
	            response = e.getMessage();

	            LogMessages.logStatus("FailureIntegrationService retMsg : 4 : " + response);

	            return resFlag;
	        }
		return result;
		
	}

  public String postingIntegrationonMaturitydateService(String winame,String accno,String invesamt)
  {
		LogMessages.logStatus("postingIntegrationonMaturitydateService inputXML:  :: ifr");
		LogMessages.logStatus("postingIntegrationonMaturitydateService inputXML:  ::winame :: "+winame);
		LogMessages.logStatus("postingIntegrationonMaturitydateService inputXML:  ::accno :: "+accno);
		LogMessages.logStatus("postingIntegrationonMaturitydateService inputXML:  ::::invesamt :: "+invesamt);
		Date d= new Date();
		String todaydate=new SimpleDateFormat("YYYY-MM-DD HH:mm:ss").format(d);
		LogMessages.logStatus("postingIntegrationonMaturitydateService inputXML: todaydate ::"+todaydate);
		String outputXml;
		String servicename="SuccessDebit";
	  
		//String inputXML=reqXML.requestDebitCreditXML(serialNum, debitAcctId, debitBranchId, debitFlg, debitAmountValue,debitCurrencyCode, partTrnRmks, processType,todayDate );
		
		String inputXML="<XferTrnAddRequest><XferTrnAddRq><XferTrnHdr><TrnType>T</TrnType><TrnSubType>CP</TrnSubType>"
+ "</XferTrnHdr><XferTrnDetail><PartTrnRec>"
+ "<SerialNum>1</SerialNum>"
+ "<AcctId>"
+ "<AcctId>"+accno+"</AcctId>"
+ "<BankInfo>"
+ "<BranchId>201</BranchId>"
+ "</BankInfo>"
+ "</AcctId>"
+ "<CreditDebitFlg>D</CreditDebitFlg>"
+ "<TrnAmt>"
+ "<amountValue>"+invesamt+"</amountValue>"
+ "<currencyCode>NGN</currencyCode>"
+ "</TrnAmt>"
+ "<TrnParticulars>okay</TrnParticulars>"
+ "<PartTrnRmks>"+winame+"</PartTrnRmks>"
+ "<ValueDt>"+todaydate+"</ValueDt>"
+ "</PartTrnRec><PartTrnRec>"
+ "<SerialNum>2</SerialNum>"
+ "<AcctId><AcctId>2007660814</AcctId>"
+ "<BankInfo>"
+ "<BranchId>201</BranchId>"
+ "</BankInfo></AcctId>"
+ "<CreditDebitFlg>C</CreditDebitFlg><TrnAmt>"
+ "<amountValue>"+invesamt+"</amountValue>"
+ "<currencyCode>NGN</currencyCode></TrnAmt>"
+ "<TrnParticulars>okay</TrnParticulars>"
+ "<PartTrnRmks>"+winame+"</PartTrnRmks>"
+ "<ValueDt>"+todaydate+"</ValueDt>"
+ "</PartTrnRec>"
+ "</XferTrnDetail>"
+ "</XferTrnAddRq>"
+ "<XferTrnAdd_CustomData>"
+ "<TRAN isMultiRec=\"Y\"><SRLNUM>1</SRLNUM>"
+ "<TRANCODE>005</TRANCODE>"
+ "<REMARKS2>"+winame+"</REMARKS2></TRAN><TRAN isMultiRec=\"Y\">"
+ "<SRLNUM>2</SRLNUM>"
+ "<TRANCODE>005</TRANCODE><REMARKS2>"+winame+"</REMARKS2></TRAN></XferTrnAdd_CustomData>"
+ "</XferTrnAddRequest>";
		LogMessages.logStatus("postingIntegrationonMaturitydateService inputXML:"+inputXML);
		String response = null;
	    String resFlag="";
	    String result = "";
		try{
			//SocketService socket= new SocketService();
			LogMessages.logStatus("Success inputXML: inside try");
			 if("SuccessDebit".equalsIgnoreCase(servicename)) {
		          LogMessages.logStatus("Welcome To postingIntegrationonMaturitydateService");
		           Properties p = new Properties();
	               p.load(new FileInputStream("config.properties")); 
	               String urlString="";
	               urlString = p.getProperty("SuccessDebit");
	               LogMessages.logStatus("postingIntegrationonMaturitydateService urlString:"+urlString);
		           String outputResponseXml = "";
		          
		           ConnectToFinacle connecttoFinacle= new ConnectToFinacle();
		           try {
		               outputResponseXml =connecttoFinacle.callService(inputXML,urlString);
		               LogMessages.logStatus("Response Final==" + outputResponseXml);
		               result = outputResponseXml;
		               LogMessages.logStatus("result:::--" + result);
		                   System.out.println("result:::--" + result);
		                  // writeData(out, result);
		                 
		           } catch (Exception e) {
		               result = e.getMessage();
		              LogMessages.logStatus(" Connectfinacle exception result:::--" + result);
		               System.out.println("result:::--" + result);
		              // writeData(out, result);
		           }
		       } 
			//outputXml= socket.executeIntegrationCall(servicename, "1", inputXML);
			outputXml=result;
			LogMessages.logStatus("Response message postingIntegrationonMaturitydateService:"+outputXml);
			XMLParser xmlParser= new XMLParser(outputXml);
			  resFlag = xmlParser.getValueOf("Status");
			  LogMessages.logStatus(" resFlag from  postingIntegrationonMaturitydateService :"+resFlag);
	            if (outputXml != null && (outputXml.indexOf("<Status>") != -1)) {
	            	
	               // xmlParser.setInputXML(outputXml);
	            	LogMessages.logStatus("postingIntegrationonMaturitydateService ::1 " );
	                LogMessages.logStatus("Status resFlag postingIntegrationonMaturitydateService post>>>>  : " + resFlag);

	                LogMessages.logStatus(" resFlag  postingIntegrationonMaturitydateService: " + resFlag);
	                if (outputXml != null && (resFlag.trim().equalsIgnoreCase("SUCCESS"))
	                       ) 
	                {
	                	 LogMessages.logStatus(" resFlag postingIntegrationonMaturitydateService integration: inside ...123" );
	                	return resFlag;
	                }
	                }
	            else if (outputXml != null
                    && (resFlag.trim().equalsIgnoreCase("FAILURE") || resFlag.trim().equalsIgnoreCase("FAILED"))
                   ) {
	            	LogMessages.logStatus("postingIntegrationonMaturitydateService ::2 " );
            	//ifr.setValue("POST_TO_FINACLE_STATUS", resFlag);
	            	 LogMessages.logStatus(" resFlag postingIntegrationonMaturitydateService integration: inside failed...123" );
                String errCode = xmlParser.getValueOf("ErrorCode");

                LogMessages.logStatus(" postingIntegrationonMaturitydateService post errCode : " + errCode);
                String errDesc = xmlParser.getValueOf("ErrorDesc");

                LogMessages.logStatus(" postingIntegrationonMaturitydateService post errCode errDesc : " + errDesc);
                String errType = xmlParser.getValueOf("ErrorType");

                LogMessages.logStatus(" : errType : " + errType);

                response = "postingIntegrationonMaturitydateService Not Done Successfuly. Get ErrorType : " + errType + " ErrorCode : " + errCode
                        + " ErrorDesc : " + errDesc + ".";

                LogMessages.logStatus("retMsg : 2 : " + response);
                return resFlag;
            }
        else 
        {
      	  LogMessages.logStatus("postingIntegrationonMaturitydateService ::3 " );
            response = "postingIntegrationonMaturitydateService Not Found.";
            resFlag="Failed";
            LogMessages.logStatus(" retMsg : 3 : " + response);
            return resFlag;
          //  ifr.setValue("POST_TO_FINACLE_STATUS", response);
        }

		}
		 catch (Exception e) {

	            LogMessages.logStatus("postingIntegrationonMaturitydateService : e.getMessage() : 3 : " + e.getMessage());
	            response = e.getMessage();
	            LogMessages.logStatus("postingIntegrationonMaturitydateService ::4" );
	            LogMessages.logStatus("postingIntegrationonMaturitydateService retMsg : 4 : " + response);

	            return response;
	        }
		return resFlag;
	  
  }
  public void createWi() throws NumberFormatException, IOException, Exception
    {
	  
	   /* boolean windowchk=checkwindowtime();
    	//if cutofftime completes then create workitem
	    if(windowchk==true)
	    {
	    	LogMessages.logStatus("createWi  --> cutofftime completed so will create wi");
	    }
	    else
	    {
	    	LogMessages.logStatus("createWi  --> cutofftime not completed so wi will not create");
	    }*/
    	Date d=new Date();
    	String todaydate=new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(d);
    	LogMessages.logStatus("createWi  --> ONCHANGE todaydate. ::"+todaydate);
    	//LogMessages.logStatus("Branch_Initiator  --> ONCHANGE cpcutofftime. ::"+cpcutofftime);
    	//create workitem 
    	String wiName = uploadWorkItem(strSessionId, strCabinetName,
    					strAppServerIp, intWrapperPort, strAppServerType,
    					processdefid, "Y", "", "", "");
    	LogMessages.logStatus("createWi  --> wiName created:: "+wiName);
    	
    	if(wiName!=null)
    	{
    		
    		LogMessages.logStatus("createWi  --> wiName created::  inside not null "+wiName);	
    		processandcalculatedata1(wiName);
    		processandcalculatedata2(wiName);
    		postingintergrations(wiName);
    		
    	}
    	
    	

    	
    }
    
    
    
@SuppressWarnings("deprecation")
public boolean checkwindowtime() throws NumberFormatException, IOException, Exception
{
	
	LogMessages.logStatus("inside in checkwindowtime()");
	String query="SELECT cp_primarycutofftime, CP_CLOSEDATE,approvedmsg from NG_EXT_MONEYMARKET"
			+ " where windowstatus ='ACTIVE' AND market='Commercial Paper' "
			+" AND cp_market='Primary Market' AND cp_uniquerefno IS NOT NULL AND CP_OPENDATE IS NOT NULL  AND CP_CLOSEDATE IS NOT NULL";
	LogMessages.logStatus("inside in query :: "+query);
	 String cpcutofftime = null,cpclosedate=null,approvedsmsg=null,opendate=null;
	try
	 {
	   WFXmlResponse xmlResponse = null;
	   WFXmlList xmlList = null;
	   String retvalxml = "";
	   String sMainCode = "";
	  
	   retvalxml = APSelect(query);
	   LogMessages.logStatus("retvalxml::" + retvalxml);
	   xmlResponse = new WFXmlResponse(retvalxml);
	   xmlList = xmlResponse.createList("Records", "Record");
	   LogMessages.logStatus("xmlResponse::" + xmlResponse.toString());
	   sMainCode = xmlResponse.getVal("MainCode");
	   LogMessages.logStatus("sMainCode :: "+sMainCode);
	   if (sMainCode.equals("0"))
	   {
	     if (xmlResponse.toString().contains("<Record>"))
	     {
	       for (; xmlList.hasMoreElements(); xmlList.skip())
	       {	             
	    	   cpcutofftime = xmlList.getVal("CP_PRIMARYCUTOFFTIME");
	    	   cpclosedate = xmlList.getVal("CP_CLOSEDATE");
	    	   approvedsmsg=xmlList.getVal("APRROVEDMSG");
	    	   opendate=xmlList.getVal("CP_OPENDATE");
	         LogMessages.logStatus("cpcutofftime :: "+cpcutofftime);
	         LogMessages.logStatus("cpclosedate :: "+cpclosedate);
	         LogMessages.logStatus("approvedsmsg :: "+approvedsmsg);
	         Date d=new Date();
	         String todaydate=new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(d);
	         
	         if(new Date(todaydate).after(new Date(cpcutofftime)))
	         {
	        	 return true;
	         }
	         else
	         {
	        	 return false;
	         }
	       }
	     }
	     else
	     {
	    	 LogMessages.logStatus("NO RECORDS");
	       System.out.println("NO RECORDS");
	     }
	   }
	   else if (sMainCode.equals("15")) {
		   LogMessages.logStatus("Error in Query");
	     System.out.println("Error in Query");
	   } else if (sMainCode.equals("18")) {
		   LogMessages.logStatus("No more records.");
	     System.out.println("No more records.");
	   }
	  
	 }
	 catch (Exception e)
	 {
		LogMessages.logStatus("Exception in 123:-" + e.getMessage());
	   System.out.println("Exception in 123:-" + e.getMessage());
	 }

	return false;
	}   
       
  
    public void moveWiToDiscard()
    {
    	//Requests which isnot processed by brnach verifier after cutofftome...those requests move to discard ws
    	LogMessages.logStatus("inside in checkwindowtime()");
    	String query="SELECT cp_primarycutofftime, CP_CLOSEDATE,approvedmsg from NG_EXT_MONEYMARKET"
    			+ " where windowstatus ='ACTIVE' AND market='Commercial Paper' "
    			+" AND cp_market='Primary Market' AND cp_uniquerefno IS NOT NULL AND CP_OPENDATE IS NOT NULL  AND CP_CLOSEDATE IS NOT NULL";
    	LogMessages.logStatus("inside in query :: "+query);
    	 String cpcutofftime = null,cpclosedate=null,approvedsmsg=null,opendate=null;
    	try
    	 {
    	   WFXmlResponse xmlResponse = null;
    	   WFXmlList xmlList = null;
    	   String retvalxml = "";
    	   String sMainCode = "";
    	  
    	   retvalxml = APSelect(query);
    	   LogMessages.logStatus("retvalxml::" + retvalxml);
    	   xmlResponse = new WFXmlResponse(retvalxml);
    	   xmlList = xmlResponse.createList("Records", "Record");
    	   LogMessages.logStatus("xmlResponse::" + xmlResponse.toString());
    	   sMainCode = xmlResponse.getVal("MainCode");
    	   LogMessages.logStatus("sMainCode :: "+sMainCode);
    	   if (sMainCode.equals("0"))
    	   {
    	     if (xmlResponse.toString().contains("<Record>"))
    	     {
    	       for (; xmlList.hasMoreElements(); xmlList.skip())
    	       {	             
    	    	   cpcutofftime = xmlList.getVal("CP_PRIMARYCUTOFFTIME");
    	    	   cpclosedate = xmlList.getVal("CP_CLOSEDATE");
    	    	   approvedsmsg=xmlList.getVal("APRROVEDMSG");
    	    	   opendate=xmlList.getVal("CP_OPENDATE");
    	         LogMessages.logStatus("cpcutofftime :: "+cpcutofftime);
    	         LogMessages.logStatus("cpclosedate :: "+cpclosedate);
    	         LogMessages.logStatus("approvedsmsg :: "+approvedsmsg);
    	         Date d=new Date();
    	         String todaydate=new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(d);
    	         
    	         if(new Date(todaydate).after(new Date(cpcutofftime)))
    	         {
    	        	//check records are processed by branch verifier or not
    	        	 String casetype="branch_verifer";
    	        	 fetchqueue("629",casetype);
    	        	 processwitomovediscard("1","1");
    	         }
    	         else
    	         {
    	        	
    	         }
    	       }
    	     }
    	     else
    	     {
    	    	 LogMessages.logStatus("NO RECORDS");
    	       System.out.println("NO RECORDS");
    	     }
    	   }
    	   else if (sMainCode.equals("15")) {
    		   LogMessages.logStatus("Error in Query");
    	     System.out.println("Error in Query");
    	   } else if (sMainCode.equals("18")) {
    		   LogMessages.logStatus("No more records.");
    	     System.out.println("No more records.");
    	   }
    	  
    	 }
    	 catch (Exception e)
    	 {
    		LogMessages.logStatus("Exception in 123:-" + e.getMessage());
    	   System.out.println("Exception in 123:-" + e.getMessage());
    	 }

    	

    	
    	
    }
  
    public String APUpdate(String tname, String cname, String cvalue, String where) {
        String strOutputXml = "";
        String strInputXml = "";

        try {
            strInputXml = "<?xml version=\"1.0\"?>" + "<APUpdate>"
                    + "<Option>APUpdate</Option>"
                    + "<EngineName>" + strCabinetName + "</EngineName>"
                    + "<SessionId>" + strSessionId + "</SessionId>"
                    + "<TableName>" + tname + "</TableName>"
                    + "<ColName>" + cname + "</ColName>"
                    + "<Values>" + cvalue + "</Values>"
                    + "<WhereClause>" + where + "</WhereClause>"
                    + "</APUpdate>";
            System.out.println("APUpdate:: strInputXml :: " + strInputXml);
            LogMessages.logStatus("strInputXml :: "+strInputXml);
            strOutputXml = WFCallBroker.execute(strInputXml, strAppServerIp, intWrapperPort, 1);
            LogMessages.logStatus("strOutputXml :: "+strOutputXml);
        } catch (Exception e) {
            LogMessages.logStatus("Exception occured in APUpdate : " + e.getMessage());
            LogMessages.logStatus("[Exception Caught] Output Xml : " + strOutputXml);
            LogMessages.logError("Exception occured in APUpdate", e);
        }
        return strOutputXml;
    }
    
    public String APSelect(String Query) {
        String strOutputXml = "";
        String strInputXml = "";

        try {
            strInputXml = "<?xml version=\"1.0\"?>" + "<APSelect>"
                    + "<Option>APSelectWithColumnNames</Option>"
                    + "<EngineName>" + strCabinetName + "</EngineName>"
                    + "<SessionId>" + strSessionId + "</SessionId>"
                    + "<Query>" + Query + "</Query>"
                    + "</APSelect>";
            LogMessages.logStatus("strInputXml :: "+strInputXml);
            LogMessages.logStatus("strAppServerIp :: "+strAppServerIp);
            LogMessages.logStatus("intWrapperPort :: "+intWrapperPort);
           
            strOutputXml = WFCallBroker.execute(strInputXml, strAppServerIp, intWrapperPort, 1);
           // strOutputXml = ngEjbClient.makeCall(strInputXml.toString());
            LogMessages.logStatus("strOutputXml :: "+strOutputXml);
        } catch (Exception e) {
            LogMessages.logStatus("Exception occured in APSelect. " + e.getMessage());
            LogMessages.logStatus("[Exception Caught] Output Xml : " + strOutputXml);
            LogMessages.logError("Exception occured in APSelect", e);
        }
        return strOutputXml;
    }
    
    
    public static boolean aPInsert_API(String Tablename, String sColumnName,String sColumnValues) {
    	LogMessages.logStatus("aPInsert_API.java :: APInsert  ::APInsert executing.....");
    	LogMessages.logStatus("aPInsert_API.java :: APInsert  ::Tablename ::"+Tablename);
    	LogMessages.logStatus("aPInsert_API.java :: APInsert  ::sColumnName ::"+sColumnName);
    	LogMessages.logStatus("aPInsert_API.java :: APInsert  ::sColumnValues ::"+sColumnValues);
		
		boolean rtn = false;
		try {
			StringBuffer strBuffer = new StringBuffer();
			strBuffer.append("<?xml version=1.0?>");
			strBuffer.append("<APInsert_Input>");
			strBuffer.append("<Option>APInsert</Option>");
			strBuffer.append("<TableName>" + Tablename + "</TableName>");
			strBuffer.append("<ColName>" + sColumnName + "</ColName>");
			strBuffer.append("<Values>" + sColumnValues + "</Values>");
			strBuffer.append("<EngineName>" +strCabinetName  + "</EngineName>");
			strBuffer.append("<SessionId>" + strSessionId + "</SessionId>");
			strBuffer.append("</APInsert_Input>");
			LogMessages.logStatus("aPInsert_API.java :: APInsert  ::Input XML for apinsert" + strBuffer.toString());
			String xmlout = WFCallBroker.execute(strBuffer.toString(), strAppServerIp,intWrapperPort, 1);
			//String  xmlout = ngEjbClient.makeCall(strBuffer.toString());
			LogMessages.logStatus("aPInsert_APIs.java :: APInsert  ::Output xml" + xmlout);
			DMSXmlResponse xmlResponse = new DMSXmlResponse(xmlout);
			if (xmlResponse.getVal("MainCode").equals("0")) {
				LogMessages.logStatus("aPInsert_API.java :: APInsert  ::apinsert exceuted successfully...");
				LogMessages.logStatus("apinsert executed successfully");
				rtn = true;
			} else {
				LogMessages.logStatus("error in executing apinsert Tablename :: "+ Tablename);
				LogMessages.logStatus("aPInsert_API.java :: APInsert  ::error in apinsert exceution... Tablename :: "+ Tablename);
			}
		} catch (Exception e) {
			LogMessages.logStatus("aPInsert_API.java :: APInsert  ::EXCEPTION " + e.getMessage());
		}
		return rtn;
	}

    
    public String GetdocnamesFromDB(String sPid)
	   {
	     String sValue = "";
	     String sTableName = " PDBDOCUMENT A ,PDBDOCUMENTCONTENT B,SRM_EXTTABLE E ";
	     String sColumnName = " A.NAME ";
	     String sWhereCondition = " A.DOCUMENTINDEX=B.DOCUMENTINDEX AND  B.PARENTFOLDERINDEX= E.ITEMINDEX AND E.WI_NAME = '"+sPid+"' ";
	     try
	     {
	       WFXmlResponse xmlResponse = null;
	       WFXmlList xmlList = null;
	       String retvalxml = "";
	       String sMainCode = "";
	       String sQuery = "SELECT " + sColumnName.toUpperCase() + " FROM " + sTableName.toUpperCase() + " WHERE " + sWhereCondition;
	       System.out.println("sQuery:: GetdocnamesFromDB :: " + sQuery);
	       LogMessages.logStatus("sQuery:: GetdocnamesFromDB :: " + sQuery);
	       retvalxml = APSelect(sQuery);
	       LogMessages.logStatus("retvalxml::" + retvalxml);
	       xmlResponse = new WFXmlResponse(retvalxml);
	       xmlList = xmlResponse.createList("Records", "Record");
	       LogMessages.logStatus("xmlResponse::" + xmlResponse.toString());
	       sMainCode = xmlResponse.getVal("MainCode");
	       LogMessages.logStatus("sMainCode :: "+sMainCode);
	       if (sMainCode.equals("0"))
	       {
	         if (xmlResponse.toString().contains("<Record>"))
	         {
	           for (; xmlList.hasMoreElements(); xmlList.skip())
	           {	             
	             sValue += xmlList.getVal("NAME")+",";
	           }
	           LogMessages.logStatus("sValue :: "+sValue);
	           System.out.println("DOCUMENT NAME sValue:::" + sValue);
	         }
	         else
	         {
	        	 LogMessages.logStatus("NO RECORDS");
	           System.out.println("NO RECORDS");
	         }
	       }
	       else if (sMainCode.equals("15")) {
	    	   LogMessages.logStatus("Error in Query");
	         System.out.println("Error in Query");
	       } else if (sMainCode.equals("18")) {
	    	   LogMessages.logStatus("No more records.");
	         System.out.println("No more records.");
	       }
	       return sValue;
	     }
	     catch (Exception e)
	     {
	    	 LogMessages.logStatus("Exception in GetdocindexFromDB():-" + e.getMessage());
	       System.out.println("Exception in GetdocindexFromDB():-" + e.getMessage());
	     }
	     return sValue;
	   }
    
    public String GetSingleDatafromDB(String sQuery, String column)
	   {
	     String sValue = "";
	     try
	     {
	       WFXmlResponse xmlResponse = null;
	       WFXmlList xmlList = null;
	       String retvalxml = "";
	       String sMainCode = "";
	       LogMessages.logStatus("sQuery:: GetSingleDatafromDB :: " + sQuery);
	       LogMessages.logStatus("sQuery :: GetSingleDatafromDB :: " + sQuery);
	       retvalxml = APSelect(sQuery);
	       LogMessages.logStatus("retvalxml::" + retvalxml);
	       xmlResponse = new WFXmlResponse(retvalxml);
	       xmlList = xmlResponse.createList("Records", "Record");
	       LogMessages.logStatus("xmlResponse::" + xmlResponse.toString());
	       sMainCode = xmlResponse.getVal("MainCode");
	       LogMessages.logStatus("MainCode" + sMainCode);
	       if (sMainCode.equals("0"))
	       {
	         if (xmlResponse.toString().contains("<Record>"))
	         {
	           for (; xmlList.hasMoreElements(); xmlList.skip())
	           {	             
	             sValue = xmlList.getVal(column);
	           }
	           LogMessages.logStatus("sValue:::" + sValue);
	           System.out.println("sValue:::" + sValue);
	         }
	         else
	         {
	        	 LogMessages.logStatus("NO RECORDS");
	           System.out.println("NO RECORDS");
	         }
	       }
	       else if (sMainCode.equals("15")) {
	    	   LogMessages.logStatus("Error in Query");
	         System.out.println("Error in Query");
	       } else if (sMainCode.equals("18")) {
	    	   LogMessages.logStatus("No more records.");
	         System.out.println("No more records.");
	       }
	       return sValue;
	     }
	     catch (Exception e)
	     {
	    	 LogMessages.logStatus("Exception in GetSingleDatafromDB():-" + e.getMessage());
	       System.out.println("Exception in GetSingleDatafromDB():-" + e.getMessage());
	     }
	     return sValue;
	   }
    
    public String GetMultipleDatafromDB(String sQuery, String column)
	   {
	     String sValue = "";
	     try
	     {
	       WFXmlResponse xmlResponse = null;
	       WFXmlList xmlList = null;
	       String retvalxml = "";
	       String sMainCode = "";       
	       System.out.println("sQuery:: GetMultipleDatafromDB :: " + sQuery);
	       LogMessages.logStatus("sQuery :: GetMultipleDatafromDB :: " + sQuery);
	       retvalxml = APSelect(sQuery);
	       LogMessages.logStatus("retvalxml::" + retvalxml);
	       xmlResponse = new WFXmlResponse(retvalxml);
	       xmlList = xmlResponse.createList("Records", "Record");
	       LogMessages.logStatus("xmlResponse::" + xmlResponse.toString());
	       sMainCode = xmlResponse.getVal("MainCode");
	       LogMessages.logStatus("MainCode" + sMainCode);
	       if (sMainCode.equals("0"))
	       {
	         if (xmlResponse.toString().contains("<Record>"))
	         {
	           for (; xmlList.hasMoreElements(); xmlList.skip())
	           {	             
	             sValue += xmlList.getVal(column)+",";
	           }
	           LogMessages.logStatus("sValue:::" + sValue);
	           System.out.println("sValue:::" + sValue);
	         }
	         else
	         {
	        	 LogMessages.logStatus("NO RECORDS");
	           System.out.println("NO RECORDS");
	         }
	       }
	       else if (sMainCode.equals("15")) {
	    	   LogMessages.logStatus("Error in Query");
	         System.out.println("Error in Query");
	       } else if (sMainCode.equals("18")) {
	    	   LogMessages.logStatus("No more records.");
	         System.out.println("No more records.");
	       }
	       return sValue;
	     }
	     catch (Exception e)
	     {
	    	 LogMessages.logStatus("Exception in GetMultipleDatafromDB():-" + e.getMessage());
	     }
	     return sValue;
	   }
    
    public String uploadWorkItem(String sessionId, String cabinetName, String jtsAddress, int jtsPort,
			String appServerType, String processDefId, String initiateAlso, String string,
			String string2,  String approvedmsg)
			{	
    	 LogMessages.logStatus("Inside uploadWorkItem method ::");

    	 LogMessages.logStatus("Inside uploadWorkItem method strAppServerIp::" +strAppServerIp);
          try{
 
			StringBuilder wfUploadWorkItemInputXML = new StringBuilder();
			wfUploadWorkItemInputXML.append("<?xml version=\"1.0\"?>");
			wfUploadWorkItemInputXML.append("<WFUploadWorkItem_Input>");
			wfUploadWorkItemInputXML.append("<Option>WFUploadWorkItem</Option>");
			wfUploadWorkItemInputXML.append("<EngineName>" + cabinetName + "</EngineName>");
			wfUploadWorkItemInputXML.append("<SessionId>" + sessionId + "</SessionId>");
			wfUploadWorkItemInputXML.append("<ProcessDefId>" + processDefId + "</ProcessDefId>");
			wfUploadWorkItemInputXML.append("<QueueId>"+strQueueID+"</QueueId>");	
			wfUploadWorkItemInputXML.append("<DataDefName></DataDefName>");
			wfUploadWorkItemInputXML.append("<UserDefVarFlag>Y</UserDefVarFlag>");
			wfUploadWorkItemInputXML.append("<Fields></Fields>");
			wfUploadWorkItemInputXML.append("<InitiateAlso>Y</InitiateAlso>");
			wfUploadWorkItemInputXML.append("<InitiateFromActivityId/>");
			wfUploadWorkItemInputXML.append("<Attributes>");
			wfUploadWorkItemInputXML.append("<WICREATEFLAG>Y</WICREATEFLAG>");
			wfUploadWorkItemInputXML.append("</Attributes>");
			wfUploadWorkItemInputXML.append("<Documents>");
			wfUploadWorkItemInputXML.append("</Documents>");
			wfUploadWorkItemInputXML.append("</WFUploadWorkItem_Input>");
			//System.out.println("uploadWorkItem() :: wfUploadWorkItemInputXML :: "+ wfUploadWorkItemInputXML.toString());
			LogMessages.logStatus(" wfUploadWorkItemInputXML :: "+wfUploadWorkItemInputXML);
			String wfUploadWorkItemOutputXML=null;
			 wfUploadWorkItemOutputXML = WFCallBroker.execute(wfUploadWorkItemInputXML.toString(), strAppServerIp,intWrapperPort, 1);
			//wfUploadWorkItemOutputXML = makeCall(jtsAddress, (short) jtsPort, wfUploadWorkItemInputXML.toString());
			//System.out.println("uploadWorkItem() :: wfUploadWorkItemOutputXML :: "+ wfUploadWorkItemOutputXML.toString());
			LogMessages.logStatus("wfUploadWorkItemOutputXML ::"+wfUploadWorkItemOutputXML);
			if (wfUploadWorkItemOutputXML == null || wfUploadWorkItemOutputXML.trim().isEmpty()) {
				//System.out.println("uploadWorkItem() :: no outputXML generated while uploading workitem");			
				return  "no outputXML generated while uploading workitem" ;
			}
			XMLParser xmlParser = new XMLParser();
			xmlParser.setInputXML(wfUploadWorkItemOutputXML);			
			//System.out.println("uploadWorkItem() :: xmlParser.getValueOf(MainCode) :: "+xmlParser.getValueOf("MainCode"));
			if (xmlParser.getValueOf("MainCode").trim().equals("0")) {
				String processId = xmlParser.getValueOf("ProcessInstanceId");
				//System.out.println("uploadWorkItem() :: processId :: "+processId);
				LogMessages.logStatus("processId ::"+processId);
				return processId;
			} else {
				LogMessages.logStatus("Error");
				return xmlParser.getValueOf("Error");
			}
				
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			LogMessages.logStatus("Exception occurred :: "+e.getMessage());
			return "Exception occurred :: "+e.getMessage();	
		}
	
			
			
			}
    private String makeCall(String jtsAddress, short jtsPort, String inputXML) throws Exception {
		
		String outputXml = null;
		try {
			int debug = 0; // (0|1)
			outputXml = DMSCallBroker.execute(inputXML, jtsAddress, jtsPort, debug);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("OF setup incomplete");
		} catch (Error e) {
			e.printStackTrace();
			throw new Exception("OF setup incomplete");
		}
	return outputXml;
	}
	
    public void processwitomovediscard(String strProcessInstanceId, String strWorkItemId) {
        StringBuffer strBuffer = null;
        String strXmlout = "";
        try {
            strBuffer = new StringBuffer();
            /*strBuffer.append("<?xml version='1.0'?>");
            strBuffer.append("<WFGetWorkitemDataExt_Input>");
            strBuffer.append("<Option>WFGetWorkitemDataExt</Option>");
            strBuffer.append("<EngineName>" + strCabinetName + "</EngineName>");
            strBuffer.append("<SessionId>" + strSessionId + "</SessionId>");
            strBuffer.append("<ProcessInstanceId>" + strProcessInstanceId + "</ProcessInstanceId>");
            strBuffer.append("<WorkitemId>" + "1" + "</WorkitemId>");
            strBuffer.append("<QueueId>" + "802" + "</QueueId>");
            strBuffer.append("<QueueType></QueueType>");
            strBuffer.append("<DocOrderBy>DocumentSize</DocOrderBy>");
            strBuffer.append("<DocSortOrder>D</DocSortOrder>");
            strBuffer.append("<ObjectPreferenceList>W,D</ObjectPreferenceList>");
            strBuffer.append("<GenerateLog>Y</GenerateLog>");
            strBuffer.append("<ZipBuffer>N</ZipBuffer>");
            strBuffer.append("</WFGetWorkitemDataExt_Input>");*/
            strBuffer.append("<WMGetWorkItem_Input>" + "\n");
            strBuffer.append("<Option>WMGetWorkItem</Option>");
            strBuffer.append("<EngineName>" + strCabinetName+ "</EngineName>");
            strBuffer.append("<SessionId>" + strSessionId + "</SessionId>");
            strBuffer.append("<ProcessInstanceId>" + strProcessInstanceId + "</ProcessInstanceId>");
            strBuffer.append("<WorkItemId>1</WorkItemId>");
            strBuffer.append("</WMGetWorkItem_Input>");
           
            LogMessages.logStatus("getworkitem :: xmlResponse :: "+strBuffer.toString());
           // strXmlout = WFCallBroker.execute(strBuffer.toString(), strAppServerIp, Integer.parseInt(strAppServerPort), 0);
            strXmlout = WFCallBroker.execute(strBuffer.toString(), strAppServerIp, intWrapperPort, 1);
            WFXmlResponse xmlResponse = new WFXmlResponse(strXmlout);
            WFXmlList xmlList=null;
            LogMessages.logStatus("getworkitem :: xmlResponse :: "+xmlResponse);
           
            if (xmlResponse.getVal("MainCode").equals("0")) {
	        	String sWI_entrytime= "";
	        	sWI_entrytime = xmlResponse.getVal("EntryDateTime");
	        	LogMessages.logStatus("EntryDateTime - "+sWI_entrytime);
	        	System.out.println("ProcessInstanceId "+strProcessInstanceId+" is locked successfully.");
	        	LogMessages.logStatus("ProcessInstanceId "+strProcessInstanceId+" is locked successfully.");
	        	xmlList = xmlResponse.createList("Attributes", "Attribute");
       	       	for(; xmlList.hasMoreElements(true); xmlList.skip(true)){
       	       	   if(xmlList.getVal("Name").equalsIgnoreCase("DECISION"))
       	       	   {
       	       		String decision = xmlList.getVal("Value");
       	       		if(decision.equalsIgnoreCase(" ")||decision.equalsIgnoreCase(null))
       	       		{
       	       			//move wi case to discrd
       	       		}
       	       	   }
       	       	     
				}
       	       	
            	//boolean statusWebportalResponse = checkWebportalResponse(strProcessInstanceId,busseg,insCat,insType);
            	
    	        
            	
            	
            } else if (xmlResponse.getVal("MainCode").equals("18")) {
                System.out.println("No new case found.");
                LogMessages.logStatus("No new case found.");
            } else {
                System.out.println("Error in getting workitem data.");
                LogMessages.logStatus("Error in getting workitem data.");
                LogMessages.logStatus("Input Xml : " + strBuffer.toString());
                LogMessages.logStatus("Output Xml : " + strXmlout);
            }
        } catch (Exception e) {
            LogMessages.logStatus("Exception occured in getworkitem : " + e.getMessage());
            LogMessages.logError("Exception occured in getworkitem.", e);
        }
    }  

    public void getworkitemfromawaitingqueue(String strProcessInstanceId, String strWorkItemId) {
    	LogMessages.logStatus("indside getworkitemfromawaitingqueue - ");
    	StringBuffer strBuffer = null;
        String strXmlout = "";
        try {
            strBuffer = new StringBuffer();
            strBuffer.append("<WMGetWorkItem_Input>" + "\n");
            strBuffer.append("<Option>WMGetWorkItem</Option>");
            strBuffer.append("<EngineName>" + strCabinetName+ "</EngineName>");
            strBuffer.append("<SessionId>" + strSessionId + "</SessionId>");
            strBuffer.append("<ProcessInstanceId>" + strProcessInstanceId + "</ProcessInstanceId>");
            strBuffer.append("<WorkItemId>1</WorkItemId>");
            strBuffer.append("</WMGetWorkItem_Input>");
           
            LogMessages.logStatus("getworkitem :: xmlResponse :: "+strBuffer.toString());
           // strXmlout = WFCallBroker.execute(strBuffer.toString(), strAppServerIp, Integer.parseInt(strAppServerPort), 0);
            strXmlout = WFCallBroker.execute(strBuffer.toString(), strAppServerIp, intWrapperPort, 1);
            WFXmlResponse xmlResponse = new WFXmlResponse(strXmlout);
            WFXmlList xmlList=null;
            LogMessages.logStatus("getworkitem :: xmlResponse :: "+xmlResponse);
            String accno=""; 
           	String principalamount="";
           	String investmenttype ="";
           	String marketType="";
           	String lienstatus="",rate="",tenor="",leapyearstatus="",maturitydate="",intrestlyr="",intrestnlyr="";
            if (xmlResponse.getVal("MainCode").equals("0")) {
	        	String sWI_entrytime= "";
	        	sWI_entrytime = xmlResponse.getVal("EntryDateTime");
	        	LogMessages.logStatus("EntryDateTime - "+sWI_entrytime);
	        	System.out.println("ProcessInstanceId "+strProcessInstanceId+" is locked successfully.");
	        	LogMessages.logStatus("ProcessInstanceId "+strProcessInstanceId+" is locked successfully.");
	        	xmlList = xmlResponse.createList("Attributes", "Attribute");
       	       	for(; xmlList.hasMoreElements(true); xmlList.skip(true)){
       	       	//first check market type otr primary market or secondary market
       	       	 if(xmlList.getVal("Name").equalsIgnoreCase("Maturitydate"))
	  	       	   {
	       	    	marketType = xmlList.getVal("cp_market");
	  	       	   }
       	       	 if(marketType.equalsIgnoreCase("Primary Market")){
       	       	 //check maturity date of this workitem if maturity date comes reads data and post
	       	      if(xmlList.getVal("Name").equalsIgnoreCase("Maturitydate"))
	  	       	   {
	       	    	maturitydate = xmlList.getVal("Value");
	  	       	   }
	       	    	boolean checkdate=checkmaturitydatewithcurrentdate(maturitydate);
	       	    	if(checkdate==true)  
	       	    	{
	       	    		if(xmlList.getVal("Name").equalsIgnoreCase("BI_CAN"))
	        	       	   {
	        	       		accno = xmlList.getVal("Value");
	        	       	   }
	        	       	   if(xmlList.getVal("Name").equalsIgnoreCase("BI_PRINCIPALAMOUNT"))
	     	       	       {
	        	       		principalamount = xmlList.getVal("Value");
	     	       	       }
	        	       	   if(xmlList.getVal("Name").equalsIgnoreCase("BI_INVESTMENTTYPE"))
	        	       	   {
	        	       		investmenttype = xmlList.getVal("Value");
	        	       	   }
	        	           if(xmlList.getVal("Name").equalsIgnoreCase("cpbipr_lienstatus"))
	     	       	        {
	        	        	lienstatus = xmlList.getVal("Value");
	     	       	       }
		 	       	       if(xmlList.getVal("Name").equalsIgnoreCase("BI_PERSONALRATE"))
		 		       	   {
		 	       	    	rate = xmlList.getVal("Value");
		 		       	   }
		 	       	       if(xmlList.getVal("Name").equalsIgnoreCase("BICP_TENOR"))
		 		       	   {
		 	 	        	tenor = xmlList.getVal("Value");
		 		       	   }
		 	       	      if(xmlList.getVal("Name").equalsIgnoreCase("leapyearstatus"))
		 		       	   {
		 	       	    	leapyearstatus = xmlList.getVal("Value");
		 		       	   }
	 	       	    LogMessages.logStatus("details for winame :: -----"+strProcessInstanceId + "--------");  
	 	       		LogMessages.logStatus("accno ::"+accno);
	     	        LogMessages.logStatus("principalamount ::"+principalamount);
	     	        LogMessages.logStatus("investmenttype ::"+investmenttype);
	     	        LogMessages.logStatus("lienstatus ::"+lienstatus);
	     	        LogMessages.logStatus("rate  ::"+rate);
	     	        LogMessages.logStatus("tenor ::"+tenor);
	     	        //if principal==credit customer only
	     	        //if principal== credit principal+customer intrest+residual intrest
	     	        //checcklienstatus call
	     	        if(lienstatus.equalsIgnoreCase("false"))
	     	        {
	     	        	  //calculate customer insterest and residual interest 
		     	        String creditamount="";
		     	        if(investmenttype.equalsIgnoreCase("Principal"))
		     	        {
		     	        	 LogMessages.logStatus("inside principal method ::");
		     	        	 creditamount=principalamount;
		     	        	 LogMessages.logStatus("creditamount ::"+creditamount);
		     	        }
		     	        else if(investmenttype.equalsIgnoreCase("Principal+Interest"))
		     	        {
		     	        	LogMessages.logStatus("inside Principal+Interest method ::");
		     	        	 //customer's intrest--Principal *Rate* Tenor )/365*100 ) +
		     	    	       // Tenor/366), For Non Leap year -
		     	    	       // {(Principal * Tenor *Rate)/365 *100)
		     	    	        double customerinterest;
		     	    	        int ppamt=0,rateint=0,tenorint=0;
		     	    	        if(leapyearstatus.equalsIgnoreCase("true"))
		     	    	        {
		     	    	        	ppamt=Integer.parseInt(principalamount);
		     	    	        	rateint=Integer.parseInt(rate);
		     	    	        	tenorint=Integer.parseInt(tenor);
		     	    	        	customerinterest=(ppamt*rateint*tenorint)/365*100+ tenorint/366;
		     	    	        	LogMessages.logStatus("customerinterest ::"+customerinterest);
		     	    	        	
		     	    	        }else
		     	    	        {
		     	    	        	ppamt=Integer.parseInt(principalamount);
		     	    	        	rateint=Integer.parseInt(rate);
		     	    	        	tenorint=Integer.parseInt(tenor);
		     	    	        	customerinterest=(ppamt*rateint*tenorint)/365*100;
		     	    	        	LogMessages.logStatus("customerinterest ::"+customerinterest);
		     	    	        }
		     	    	        
		     	    	        double customerresiudalintrest=1000;
		     	    	        double cdtamount=customerinterest+customerresiudalintrest;
		     	    	        creditamount=String.valueOf(cdtamount);
		     	    	        LogMessages.logStatus("creditamount ::"+creditamount);
		     	        }  
		     	    	        
		     	        //posting integration for winame on  maturity date       
	 	    	       String response=postingIntegrationonMaturitydateService(strProcessInstanceId, accno,creditamount);
			           LogMessages.logStatus("postingIntegrationonMaturitydateService Post response :: "+response);
			           String matureStatus="";
			        	 if(response.equalsIgnoreCase("Success") ||  response.equalsIgnoreCase("Failed"))
			            	 {
			        		    LogMessages.logStatus("MAturity post rsponse inside success or failed :: "+response);
			        		    response=response;
			        		    if(response.equalsIgnoreCase("Success"))
			        		    {
			        		    	matureStatus="Matured";
			        		    }
			        		    else
			        		    {
			        		    	matureStatus="Awaiting Maturity";
			        		    }
			            	}else
			            		   {
			            			   LogMessages.logStatus("MAturity post rsponse inside other:: "+response);
			            			   response="failed";   
			            		   }
	        		        
	        		               //update to external table
	        		                String tname1="NG_EXT_MONEYMARKET";
	            		           	 String cname1="MaturityPostingstatus,MatureStatus";
	            		             String cvalue1="'"+response+"','"+matureStatus+"'";
	            		             String where1="winame ='"+strProcessInstanceId+"'";
	            		               
	    		            	   String retxml1 = APUpdate(tname1, cname1, cvalue1, where1); 
	        		          		WFXmlResponse xmlResponseupdate1 = new WFXmlResponse(retxml1);
	        		               if (xmlResponseupdate1.getVal("MainCode").equals("0")) {
	        		               	 LogMessages.logStatus("Updated sucessfully for " +strProcessInstanceId);  
	        		               } else {
	        		               	 LogMessages.logStatus("Error in updation");
	        		               }          
	        		               unlockWIs(strProcessInstanceId);
	        		               getworkitem(strProcessInstanceId, strProcessInstanceId);
	        		               CompleteWorkItem(strProcessInstanceId);
	        		     
	     	        }
	     	        else
	     	        {
	     	        	LogMessages.logStatus("Liened Status is true for this..So cannot posting for this.. " +strProcessInstanceId);
	     	           unlockWIs(strProcessInstanceId);
  		               getworkitem(strProcessInstanceId, strProcessInstanceId);
  		               CompleteWorkItem(strProcessInstanceId);
	     	        }  
	     	        }
	       	    	else
	       	    	{
	       	    	 LogMessages.logStatus("Posting date not arrived for this winame..." +strProcessInstanceId);
	       	    	 unlockWIs(strProcessInstanceId);
		             getworkitem(strProcessInstanceId, strProcessInstanceId);
		             CompleteWorkItem(strProcessInstanceId);
	       	    	}
       	       	 }
       	       	 else if(marketType.equalsIgnoreCase("Secondary Market"))
       	       	 {
       	       	//check maturity date of this workitem if maturity date comes reads data and post
   	       	      if(xmlList.getVal("Name").equalsIgnoreCase("CPSEC_HIDMATDATE"))
   	  	       	   {
   	       	    	maturitydate = xmlList.getVal("Value");
   	  	       	   }
   	       	    	boolean checkdate=checkmaturitydatewithcurrentdate(maturitydate);
   	       	    	if(checkdate==true)  
   	       	    	{ 
   	       	    	if(xmlList.getVal("Name").equalsIgnoreCase("CPBISEC_ACCNO"))
     	       	   {
     	       		accno = xmlList.getVal("Value");
     	       	   }
     	       	   if(xmlList.getVal("Name").equalsIgnoreCase("CPBISEC_PRINCIPAL"))
  	       	       {
     	       		principalamount = xmlList.getVal("Value");
  	       	       }
     	       	   if(xmlList.getVal("Name").equalsIgnoreCase("CPBISEC_INSTRUCTIONTYPE"))
     	       	   {
     	       		investmenttype = xmlList.getVal("Value");
     	       	   }
     	           if(xmlList.getVal("Name").equalsIgnoreCase("CPBISEC_LIENSTATUS"))
  	       	        {
     	        	lienstatus = xmlList.getVal("Value");
  	       	        } 
 	       	       if(xmlList.getVal("Name").equalsIgnoreCase("leapyearstatus"))
 		       	   {
 	       	    	leapyearstatus = xmlList.getVal("Value");
 		       	   }
 	       	      if(xmlList.getVal("Name").equalsIgnoreCase("cpbvsec_lyrintrate"))
		       	   {
	       	    	intrestlyr = xmlList.getVal("Value");
		       	   }
	 	       	    if(xmlList.getVal("Name").equalsIgnoreCase("cpbvsec_nonlyr"))
		       	   {
	      	    	intrestnlyr = xmlList.getVal("Value");
		       	   }
	       	    LogMessages.logStatus("details for winame :: -----"+strProcessInstanceId + "--------");  
	       		LogMessages.logStatus("accno ::"+accno);
	  	        LogMessages.logStatus("principalamount ::"+principalamount);
	  	        LogMessages.logStatus("investmenttype ::"+investmenttype);
	  	        LogMessages.logStatus("intrestlyr ::"+intrestlyr);
	  	       LogMessages.logStatus("intrestnlyr ::"+intrestnlyr);
	  	        
	  	        //checklienstatus
	  	       String lienstatusfromservice= placeLienService(accno,principalamount,marketType);
	  	       String creditamount="";
	  	       double cdtamount=0.0;
	  	       if(lienstatusfromservice.equalsIgnoreCase("false"))
	  	       {
	  	    	   
	  	       if(investmenttype.equalsIgnoreCase("Principal"))
	  	       {
	  	    	 LogMessages.logStatus("inside principal method ::");
	  	    	 cdtamount=Double.parseDouble(principalamount);
 	        	 LogMessages.logStatus("creditamount ::"+creditamount);
	  	       }
	  	       else if(investmenttype.equalsIgnoreCase("Principal plus Interest"))
	  	       {
	  	    	   if(leapyearstatus.equalsIgnoreCase("true"))
	  	    	   {
	  	    		 cdtamount=Double.parseDouble(intrestlyr)+Double.parseDouble(principalamount);;
	  	    	   }
	  	    	   else
	  	    	   {
	  	    		 cdtamount=Double.parseDouble(intrestnlyr)+Double.parseDouble(principalamount);
	  	    	   }
	  	    	   
	  	       }
	  	     //posting integration for winame on  maturity date       
	    	       String response=postingIntegrationonMaturitydateService(strProcessInstanceId, accno,creditamount);
	           LogMessages.logStatus("postingIntegrationonMaturitydateService Post response :: "+response);
	           String matureStatus="";
	        	 if(response.equalsIgnoreCase("Success") ||  response.equalsIgnoreCase("Failed"))
	            	 {
	        		    LogMessages.logStatus("MAturity post rsponse inside success or failed :: "+response);
	        		    response=response;
	        		    if(response.equalsIgnoreCase("Success"))
	        		    {
	        		    	matureStatus="Matured";
	        		    }
	        		    else
	        		    {
	        		    	matureStatus="Awaiting Maturity";
	        		    }
	            	}else
	            		   {
	            			   LogMessages.logStatus("MAturity post rsponse inside other:: "+response);
	            			   response="failed";   
	            		   }
    		        
    		               //update to external table
    		                String tname1="NG_EXT_MONEYMARKET";
        		           	 String cname1="MaturityPostingstatus,cpbisec_status";
        		             String cvalue1="'"+response+"','"+matureStatus+"'";
        		             String where1="winame ='"+strProcessInstanceId+"'";
        		               
		            	   String retxml1 = APUpdate(tname1, cname1, cvalue1, where1); 
    		          		WFXmlResponse xmlResponseupdate1 = new WFXmlResponse(retxml1);
    		               if (xmlResponseupdate1.getVal("MainCode").equals("0")) {
    		               	 LogMessages.logStatus("Updated sucessfully for " +strProcessInstanceId);  
    		               } else {
    		               	 LogMessages.logStatus("Error in updation");
    		               }          
    		               unlockWIs(strProcessInstanceId);
    		               getworkitem(strProcessInstanceId, strProcessInstanceId);
    		               CompleteWorkItem(strProcessInstanceId);
    		 
	  	       }
	  	       else
	  	       {
	  	    	 LogMessages.logStatus("Lienstaus is true..so cannot posting done..." +strProcessInstanceId);
	       	    	unlockWIs(strProcessInstanceId);
		            getworkitem(strProcessInstanceId, strProcessInstanceId);
		            CompleteWorkItem(strProcessInstanceId);	
	  	       }
   	       	    }
   	       	    else
       	    	{
       	    	LogMessages.logStatus("Posting date not arrived for this winame..." +strProcessInstanceId);
       	    	unlockWIs(strProcessInstanceId);
	            getworkitem(strProcessInstanceId, strProcessInstanceId);
	            CompleteWorkItem(strProcessInstanceId);	
       	    	}
	  	       
   	       	    	
       	       	 }
       	       	   
    	      }
       	       
    	          	
            } else if (xmlResponse.getVal("MainCode").equals("18")) {
                System.out.println("No new case found.");
                LogMessages.logStatus("No new case found.");
            } else {
                System.out.println("Error in getting workitem data.");
                LogMessages.logStatus("Error in getting workitem data.");
                LogMessages.logStatus("Input Xml : " + strBuffer.toString());
                LogMessages.logStatus("Output Xml : " + strXmlout);
            }
        } catch (Exception e) {
            LogMessages.logStatus("Exception occured in getworkitem : " + e.getMessage());
            LogMessages.logError("Exception occured in getworkitem.", e);
        }
    }  
    
    public boolean checkmaturitydatewithcurrentdate(String maturitydate) throws ParseException
    {
    	 LogMessages.logStatus("--Inside checkmaturitydatewithcurrentdate method..");
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date1 = sdf.parse(maturitydate);
        Date d= new Date();
	    String todaydate=new SimpleDateFormat("yyyy-MM-dd").format(d);
	    LogMessages.logStatus("--checkmaturitydatewithcurrentdate  --> todaydate. ::"+todaydate);
		Date date2= new Date(todaydate);
		LogMessages.logStatus("date1 : " + sdf.format(date1));
		LogMessages.logStatus("date2 : " + sdf.format(date2));
        if (date1.compareTo(date2) > 0) {
        	LogMessages.logStatus("Date1 is after Date2");
        	return false;
        } else if (date1.compareTo(date2) < 0) {
        	LogMessages.logStatus("Date1 is before Date2");
        	return false;
        } else if (date1.compareTo(date2) == 0) {
        	LogMessages.logStatus("Date1 is equal to Date2");
        	return true;
        } 
    	return false;
    }
    
    public String placeLienService(String accno,String lienamount,String markettype) throws FileNotFoundException, IOException {
		 LogMessages.logStatus("In place Lien");
			SocketService socket = new SocketService();
		
			String servicename="placeLien";
			String inputXML=placeLienInputXML(markettype,accno,lienamount);
			LogMessages.logStatus("PLace Lien inputXML:"+inputXML);
			String outputXml;
			String response = null;
			 Properties p = new Properties();
             p.load(new FileInputStream("config.properties")); 
             String urlString="";
             urlString = p.getProperty("placeLien");
             LogMessages.logStatus("Success urlString:"+urlString);
             String resFlag ="";
	           String outputResponseXml = "";
	           String result="";
	           ConnectToFinacle connecttoFinacle= new ConnectToFinacle();
	           try {
	               outputResponseXml =connecttoFinacle.callService(inputXML,urlString);
	               LogMessages.logStatus("Response Final==" + outputResponseXml);
	               XMLParser xmlParser= new XMLParser();
	               xmlParser.setInputXML(outputResponseXml);
	                 resFlag = xmlParser.getValueOf("Status");
	                LogMessages.logStatus("Status resFlag place lien>>>>: " + resFlag);
	               result = outputResponseXml;
	               LogMessages.logStatus("result:::--" + result);
	                   System.out.println("result:::--" + result);
	                  // writeData(out, result);     
	           } catch (Exception e) {
	               result = e.getMessage();
	              LogMessages.logStatus(" Connectfinacle exception result:::--" + result);
	               System.out.println("result:::--" + result);
	              // writeData(out, result);
	           }
			return resFlag;
			
		}
    public String placeLienInputXML(String markettype,String accno,String lienamount)
    {
	 String acctId = "";
    	String lienAmount = "";  
       
        if(markettype.equalsIgnoreCase("Primary Market"))
        {
        	acctId = 	accno;
	       lienAmount = lienamount;
        }
        else
        {
        	acctId = 	accno;
	    	lienAmount = lienamount;
        }
    	
    	String currency = "NGN"; 
    	String startDt = "2020-07-20T00:00:00.000"; 
    	String endDt = "2021-07-20T00:00:00.000"; 
		String winame =  "Eniola";//(String)wdgeneralObj.getM_strProcessInstanceId(); 
		String code = "999";
    	
    	
        String inputXML =
        "<ngXmlRequest>"+
        "<AcctLienAddRequest>"+
        "<AcctLienAddRq>"+
        "<AcctId>"+
        "<AcctId>"+acctId+"</AcctId>+"+
        "</AcctId>"+
        "<ModuleType>"+
        "ULIEN</ModuleType>"+
        "<LienDtls>"+
        "<NewLienAmt>"+
        "<amountValue>"+lienAmount+"</amountValue>"+
        "<currencyCode>"+currency+"</currencyCode>"+
        "</NewLienAmt>"+
        "<LienDt>"+
        "<StartDt>"+startDt+"</StartDt>"+
        "<EndDt>"+endDt+"</EndDt>"+
        "</LienDt>"+
        "<ReasonCode>"+code+"</ReasonCode>"+
        "<Rmks>"+winame+"</Rmks>"+
        "</LienDtls>"+
        "</AcctLienAddRq></AcctLienAddRequest></ngXmlRequest>";


       LogMessages.logStatus("this is place lien inputXml: "+inputXML);


        return inputXML;
    }





}