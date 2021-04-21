package com.fbn.api;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Set;

import com.newgen.wfdesktop.xmlapi.WFInputXml;

import ISPack.CPISDocumentTxn;
import ISPack.ISUtil.JPDBRecoverDocData;
import ISPack.ISUtil.JPISException;
import ISPack.ISUtil.JPISIsIndex;

public class RequestXML {
	
    public static String connectCabinetXML(String strCabinetName, String strUserName, String strPassword){
            StringBuffer strBuffer = null;
        
            strBuffer = new StringBuffer();
            strBuffer.append("<?xml version=1.0?>");
            strBuffer.append("<NGOConnectCabinet_Input>");
            strBuffer.append("<Option>NGOConnectCabinet</Option>");
            strBuffer.append("<CabinetName>" + strCabinetName + "</CabinetName>");
            strBuffer.append("<UserName>" + strUserName + "</UserName><UserPassword>" + strPassword + "</UserPassword>");
            strBuffer.append("<UserExist>N</UserExist><ListSysFolder>N</ListSysFolder>");
            strBuffer.append("<UserType>U</UserType>");
            strBuffer.append("</NGOConnectCabinet_Input>");
            
            return strBuffer.toString();

    }
    
    public static String ConnectCabinetXMLMain(String strCabinetName, String strUserName, String strPassword){
    	
    	return "<?xml version=1.0?>"
    			+ "<NGOConnectCabinet_Input>"
    			+ "<Option>NGOConnectCabinet</Option>"
    			+ "<Option>NGOConnectCabinet</Option>"
    			+ "<CabinetName>" + strCabinetName + "</CabinetName>"
    			+ "<UserName>" + strUserName + "</UserName><UserPassword>" + strPassword + "</UserPassword>"
    			+ "<UserExist>N</UserExist><ListSysFolder>N</ListSysFolder>"
    			+ "<UserType>U</UserType>"
    			+ "</NGOConnectCabinet_Input>";
    }
    
    public static String selectRequestQuery(String strCabinetName, int strSessionId, String Query){
         
    	 return "<?xml version=\"1.0\"?>" + "<APSelect>"
            + "<Option>APSelectWithColumnNames</Option>"
            + "<EngineName>" + strCabinetName + "</EngineName>"
            + "<SessionId>" + strSessionId + "</SessionId>"
            + "<Query>" + Query + "</Query>"
            + "</APSelect>";      
    }
    
    public static String WFUploadWorkitem(String cabinetName,
            String sessionID,
            String processDefID,
			   String processActivityID,
            String attribute,
			   String Documentsfields) 
{
return "<?xml version=\"1.0\"?><WFUploadWorkItem_Input><Option>WFUploadWorkItem</Option>\n"
+ "<EngineName>"+cabinetName+"</EngineName>\n"
+ "<SessionId>"+sessionID+"</SessionId>\n"
+ "<ProcessDefId>"+processDefID+"</ProcessDefId>\n"
+ "<InitiateFromActivityId>"+processActivityID+"</InitiateFromActivityId>\n"
+ "<Attributes>"+attribute+"</Attributes>\n"
+"<Documents>"+Documentsfields+"</Documents>\n"			
+ "</WFUploadWorkItem_Input>";
}
	
	
	public String getWMCompleteWorkItemXml(String cabinetName,
			String sessionId, String processInstanceId, String workItemId) {
		return "<?xml version=\"1.0\"?>\n"
			+ "<WMCompleteWorkItem_Input>\n"
			+ "<Option>WMCompleteWorkItem</Option>\n"
			+ "<EngineName>" + cabinetName + "</EngineName>\n"
			+ "<SessionId>" + sessionId + "</SessionId>\n"
			+ "<ProcessInstanceId>" + processInstanceId + "</ProcessInstanceId>\n"
			+ "<WorkItemId>" + workItemId + "</WorkItemId>\n"
			+ "<CountFlag>Y</CountFlag>\n"
			+ "</WMCompleteWorkItem_Input>\n";
	}

	
	public String getWMCompleteWorkItemWithSetXml(String cabinetName,
			String sessionId, String processInstanceId, String workItemId,String columnName, String colValue) {
			
		String[] cols=columnName.split(",");
		String[] values=colValue.split(",");
		
		String xml = 
			"<?xml version=\"1.0\"?>\n"
			+ "<WFCompleteWithSet_Input>\n"
			+ "<Option>WFCompleteWithSet</Option>\n"
			+ "<EngineName>" + cabinetName + "</EngineName>\n"
			+ "<SessionId>" + sessionId + "</SessionId>\n"
			+ "<NoOfRecordsToComplete>1</NoOfRecordsToComplete>\n"
			+ "<CompleteAlso>Y</CompleteAlso>\n"
			+ "<ProcessInstances>\n"
				+ "<ProcessInstance>\n"
					+ "<ProcessInstanceId>" + processInstanceId + "</ProcessInstanceId>\n"
					+ "<WorkItemId>" + workItemId + "</WorkItemId>\n"
					+ "<Attributes>\n" ;
		
		String attributeList="";
		for(int i=0;i<cols.length;i++){
			attributeList+="<Attribute>\n" 
							+ "<Name>" + cols[i] + "</Name>\n" 
							+ "<Value>" + values[i] + "</Value>\n" 
						+ "</Attribute>\n";
		}
		
		xml=xml+attributeList+
				 	"</Attributes>\n"
				+ "</ProcessInstance>\n"
			+ "</ProcessInstances>\n"
		+ "</WFCompleteWithSet_Input>";
		return xml;
	}


	public String getGetWorkItemInputXml(String cabinetName,
			String sessionId, String processInstanceId, String workItemId) {
	return  "<?xml version=\"1.0\" ?>\n"
				+ "<WMGetWorkItem_Input>\n"
				+ "<Option>WMGetWorkItem</Option>\n" 
				+ "<EngineName>"+ cabinetName + "</EngineName>\n" 
				+ "<SessionId>"+ sessionId + "</SessionId>\n" 
				+ "<ProcessInstanceId>"+ processInstanceId + "</ProcessInstanceId>\n"
				+ "<WorkItemId>" + workItemId + "</WorkItemId>\n"
				+ "</WMGetWorkItem_Input>";
	}
	
	public String getGetUnlockWorkItemInputXml(String cabinetName,
			String sessionId, String processInstanceId, String workItemId) {
		return "<?xml version=\"1.0\" ?>\n"
				+ "<WMUnlockWorkItem_Input>\n"
				+ "<Option>WMUnlockWorkItem</Option>\n" 
				+ "<EngineName>"+ cabinetName + "</EngineName>\n" 
				+ "<SessionId>"+ sessionId + "</SessionId>\n" 
				+ "<ProcessInstanceId>"+ processInstanceId + "</ProcessInstanceId>\n"
				+ "<WorkItemId>" + workItemId + "</WorkItemId>\n"
				+ "</WMUnlockWorkItem_Input>";
	}
	public String getConnectInputXml(String cabinetName, String username,
			String password) {
		return "<?xml version=\"1.0\"?>\n"
				+ "<WMConnect_Input>\n" 
				+ "<Option>WMConnect</Option>\n"
				+ "<EngineName>" + cabinetName + "</EngineName>\n"
				+ "<Participant>\n" 
				+ "<Name>" + username + "</Name>\n"
				+ "<Password>" + password + "</Password>\n" 
				+ "<Scope></Scope>\n"
				+ "<UserExist>N</UserExist>\n" 
				+ "<Locale>en-us</Locale>\n"
				+ "<ParticipantType>F</ParticipantType>\n" 
				+ "</Particpant>\n"
				+ "</WMConnect_Input>";
	}

	public String getFetchWorkItemAttributesXml(String processInstanceId,
			String workItemId, String sessionId, String cabinetName) {
		return "<?xml version=\"1.0\" ?>\n"
				+ "<WMFetchWorkItemAttributes_Input>\n"
				+ "<Option>WMFetchWorkItemAttributes</Option>\n"
				+ "<EngineName>" + cabinetName + "</EngineName>\n"
				+ "<SessionId>" + sessionId + "</SessionId>\n"
				+ "<ProcessInstanceId>" + processInstanceId+ "</ProcessInstanceId>\n" 
				+ "<WorkItemId>" + workItemId+ "</WorkItemId>\n" 
				+ "</WMFetchWorkItemAttributes_Input>";
	}
	
	public String getGetWorkItemAttributeValueXml(String processInstanceId,
			String workItemId, String sessionId, String cabinetName,String attributeName) {
		return "<?xml version=\"1.0\" ?>\n"
				+ "<WMGetWorkItemAttributeValue_Input>\n"
					+ "<Option>WMGetWorkItemAttributeValue</Option>\n"
					+ "<EngineName>"+cabinetName+"</EngineName>\n"
					+ "<SessionID>"+sessionId+"</SessionID>\n"
					+ "<ProcessInstanceId>"+processInstanceId+"</ProcessInstanceId>\n"
					+ "<WorkItemID>"+workItemId+"</WorkItemID>\n"
					+ "<Attribute>\n"
						+ "<Name>"+attributeName+"</Name>\n"
					+ "</Attribute>\n"
				+ "</ WMGetWorkItemAttributeValue_Input>" ;
	}

	public String getWFUploadWorkItemInputXml(String cabinetName,
			String sessionID, String processdefId, String attList) {
		WFInputXml wfInputXml = new WFInputXml();

		wfInputXml.appendStartCallName("WFUploadWorkItem", "Input");
		wfInputXml.appendTagAndValue("EngineName", cabinetName);
		wfInputXml.appendTagAndValue("SessionId", sessionID);
		wfInputXml.appendTagAndValue("ProcessDefId", processdefId);
		wfInputXml.appendTagAndValue("Attributes", attList);
		wfInputXml.appendEndCallName("WFUploadWorkItem", "Input");

		return wfInputXml.toString();
	}

	public String getWFUploadWorkItemInputXml(String cabinetName,
			String sessionID, String processdefId, String workstepId,
			String attList) {
		WFInputXml wfInputXml = new WFInputXml();

		wfInputXml.appendStartCallName("WFUploadWorkItem", "Input");
		wfInputXml.appendTagAndValue("EngineName", cabinetName);
		wfInputXml.appendTagAndValue("SessionId", sessionID);
		wfInputXml.appendTagAndValue("ProcessDefId", processdefId);
		wfInputXml.appendTagAndValue("InitiateFromActivityId", workstepId);
		wfInputXml.appendTagAndValue("Attributes", attList);
		wfInputXml.appendEndCallName("WFUploadWorkItem", "Input");

		return wfInputXml.toString();
	}

	public String getWFDeleteWorkItemInputXml(String cabinetName,
			String sessionID, String processdefId) {
		WFInputXml wfInputXml = new WFInputXml();

		wfInputXml.appendStartCallName("WFDeleteWorkItem", "Input");
		wfInputXml.appendTagAndValue("EngineName", cabinetName);
		wfInputXml.appendTagAndValue("SessionId", sessionID);
		wfInputXml.appendTagAndValue("ProcessInstanceID", processdefId);
		wfInputXml.appendTagAndValue("ReturnDMSInfo", "Y");
		wfInputXml.appendTagAndValue("DeleteHistoryFlag", "Y");
		wfInputXml.appendEndCallName("WFDeleteWorkItem", "Input");

		return wfInputXml.toString();
	}

	
	public String getNGODeleteFolderInputXml(String cabinetName,
			String sessionId, String folderIndex, String parentFolderIndex) {
		return "<?xml version=\"1.0\"?>\n"
				+ "<NGODeleteFolder_Input>\n"
				+ "<Option>NGODeleteFolder</Option>\n"
				+ "<CabinetName>" + cabinetName + "</CabinetName>\n"
				+ "<UserDBId>" + sessionId + "</UserDBId>\n"
				+ "<FolderIndex>" + folderIndex+ "<FolderIndex>\n"
				+ "<ParentFolderIndex>" + parentFolderIndex+ "</ParentFolderIndex>\n" 
				+ "<CheckOutFlag>Y</CheckOutFlag>\n"
				+ "<LockFlag>Y</LockFlag>\n"
				+ "</NGODeleteFolder_Input>";
	}

	public String getNGODeleteDocumentExtInputXml(String cabinetName,
			String sessionId, String docIndex, String parentFolderIndex) {
		return "<?xml version=\"1.0\"?>\n"
				+ "<NGODeleteDocumentExt_Input>\n"
				+ "<Option>NGODeleteDocumentExt</Option>\n"
				+ "<CabinetName>" + cabinetName + "</CabinetName>\n"
				+ "<UserDBId>" + sessionId + "</UserDBId>\n"
				+ "<Documents>\n" 
				+ "<Document>\n" 
				+ "<DocumentIndex>" + docIndex+ "</DocumentIndex>\n" 
				+ "<ParentFolderIndex>"+ parentFolderIndex + "</ParentFolderIndex>\n"
				+ "<ReferenceFlag>N</ReferenceFlag>\n" 
				+ "</Document>\n"
				+ "</Documents>\n" 
				+ "</NGODeleteDocumentExt_Input>";
	}

	public String getFetchWorkItemsInputXml(String processInstanceId,
			String lastWorkItemId, String sessionId, String cabinetName,
			String queueId,String noOfRecords) {
		return "<?xml version=\"1.0\"?>\n"
				+ "<WMFetchWorkItems_Input>\n"
				+ "<Option>WMFetchWorkItem</Option>\n" 
				+ "<EngineName>"+ cabinetName+ "</EngineName>\n"
				+ "<SessionID>"+ sessionId+ "</SessionID>\n"
				+ "<QueueId>"+ queueId+ "</QueueId>\n"
				+ "<BatchInfo>\n"
				+ "<NoOfRecordsToFetch>"+noOfRecords+"</NoOfRecordsToFetch>\n"
				+ "<LastWorkItem>"+ lastWorkItemId+ "</LastWorkItem>\n"
				+ "<LastValue></LastValue>\n"
				+ "<LastProcessInstance>"+ processInstanceId+ "</LastProcessInstance>\n"
				+ "</BatchInfo>\n" 
				+ "</WMFetchWorkItems_Input>";
	}

	public String getFetchWorkItemsListInputXml(String processInstanceId,
			String lastWorkItemId, String sessionId, String cabinetName,
			String queueId,String noOfRecords) {
		return "<?xml version=\"1.0\"?>\n"
				+ "<WMFetchWorkList_Input>\n"
				+ "<Option>WMFetchWorkList</Option>\n" 
				+ "<EngineName>"+ cabinetName+ "</EngineName>\n"
				+ "<SessionID>"+ sessionId+ "</SessionID>\n"
				+ "<QueueId>"+ queueId+ "</QueueId>\n"
				+ "<BatchInfo>\n"
				+ "<NoOfRecordsToFetch>"+noOfRecords+"</NoOfRecordsToFetch>\n"
				+ "<LastWorkItem>"+ lastWorkItemId+ "</LastWorkItem>\n"
				+ "<LastValue></LastValue>\n"
				+ "<LastProcessInstance>"+ processInstanceId+ "</LastProcessInstance>\n"
				+ "</BatchInfo>\n" 
				+ "</WMFetchWorkList_Input>";
	}

	public static String getWMGetProcessListInputXml(String cabinetName,
			String sessionId) {
		return "<?xml version=\"1.0\"?>\n"
				+ "<WMGetProcessList_Input>\n"
				+ "<Option>WMGetProcessList</Option>\n"
				+ "<EngineName>"+ cabinetName+ "</EngineName>\n"
				+ "<SessionId>"+ sessionId+ "</SessionId>\n"
				+ "<DataFlag>N</DataFlag>\n"
				+ "<LatestVersionFlag>Y</LatestVersionFlag>\n" 
				+ "<Filter>\n"
				+ "<StateFlag>E</StateFlag>\n"
				+ "<DateRange></DateRange>\n" 
				+ "</Filter>\n" 
				+ "<BatchInfo>\n" 
				+ "<SortOrder>A</SortOrder>\n"
				+ "</BatchInfo>\n" 
				+ "</WMGetProcessList_Input>";
	}

	public String getNGOAddDocumentInputXml(String cabinetName,
			String sessionID, String folderIndex, String docSize,
			String DocumentName, String strISIndex, String appName) {
		return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
				+ "<NGOAddDocument_Input>\n"
				+ "<Option>NGOAddDocument</Option>\n" 
				+ "<CabinetName>"+ cabinetName+ "</CabinetName>\n"
				+ "<UserDBId>"+ sessionID+ "</UserDBId>\n"
				+ "<GroupIndex>0</GroupIndex>\n"
				+ "<Document>\n"
				+ "<ParentFolderIndex>"+ folderIndex+ "</ParentFolderIndex>\n"
				+ "<AccessType>S</AccessType>\n"
				+ "<DocumentName>"+ DocumentName+ "</DocumentName>\n"
				+ "<CreatedByAppName>"+ appName+ "</CreatedByAppName>\n"
				+ "<DocumentSize>"+ docSize+ "</DocumentSize>\n"
				+ "<ISIndex>"+ strISIndex+ "</ISIndex>\n"
				+ "<DocumentType>N</DocumentType>\n"
				+ "<Comment>"+ DocumentName+ "</Comment>\n"
				+ "<EnableLog>Y</EnableLog>\n"
				+ "<FTSFlag>PP</FTSFlag>\n"
				+ "</Document>\n"
				+ "</NGOAddDocument_Input>";
	}
	public String getNGOAddDocumentInputXml(String cabinetName,
			String sessionID, String folderIndex, String docSize,String noOfPages,
			String DocumentName, String strISIndex, String appName) {
		return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
				+ "<NGOAddDocument_Input>\n"
				+ "<Option>NGOAddDocument</Option>\n" 
				+ "<CabinetName>"+ cabinetName+ "</CabinetName>\n"
				+ "<UserDBId>"+ sessionID+ "</UserDBId>\n"
				+ "<GroupIndex>0</GroupIndex>\n"
				+ "<Document>\n"
				+ "<ParentFolderIndex>"+ folderIndex+ "</ParentFolderIndex>\n"
				+ "<NoOfPages>"+noOfPages+"</NoOfPages>\n"
				+ "<AccessType>S</AccessType>\n"
				+ "<DocumentName>"+ DocumentName+ "</DocumentName>\n"
				+ "<CreatedByAppName>"+ appName+ "</CreatedByAppName>\n"
				+ "<DocumentSize>"+ docSize+ "</DocumentSize>\n"
				+ "<ISIndex>"+ strISIndex+ "</ISIndex>\n"
				+ "<DocumentType>N</DocumentType>\n"
				+ "<Comment>"+ DocumentName+ "</Comment>\n"
				+ "<EnableLog>Y</EnableLog>\n"
				+ "<FTSFlag>PP</FTSFlag>\n"
				+ "</Document>\n"
				+ "</NGOAddDocument_Input>";
	}

	public String getNGOAddDocumentInputXml(String cabinetName,
			String sessionID, String folderIndex, String docSize,String noOfPages,
			String upldDocType, String strISIndex,String appName,String docType) {
		return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
				+ "<NGOAddDocument_Input>\n"
				+ "<Option>NGOAddDocument</Option>\n" 
				+ "<CabinetName>"+ cabinetName+ "</CabinetName>\n"
				+ "<UserDBId>"+ sessionID+ "</UserDBId>\n"
				+ "<GroupIndex>0</GroupIndex>\n"
				+ "<Document>\n"
				+ "<ParentFolderIndex>"+ folderIndex+ "</ParentFolderIndex>\n"
				+ "<NoOfPages>"+noOfPages+"</NoOfPages>\n"
				+ "<AccessType>S</AccessType>\n"
				+ "<DocumentName>"+ upldDocType+ "</DocumentName>\n"
				+ "<CreatedByAppName>"+ appName+ "</CreatedByAppName>\n"
				+ "<DocumentSize>"+ docSize+ "</DocumentSize>\n"
				+ "<ISIndex>"+ strISIndex+ "</ISIndex>\n"
				+ "<DocumentType>"+docType+"</DocumentType>\n"
				+ "<Comment>"+ upldDocType+ "</Comment>\n"
				+ "<EnableLog>Y</EnableLog>\n"
				+ "<FTSFlag>PP</FTSFlag>\n"
				+ "</Document>\n"
				+ "</NGOAddDocument_Input>";
	}
	
	public String getNGODeleteDocumentInputXml(final String cabinetName,String sessionId,String parentFolderIndex,String docIndices){
		String requestXml= "<?xml version=\"1.0\"?>\n"
				+"<NGODeleteDocumentExt_Input>\n"
					+"<Option>NGODeleteDocumentExt</Option>\n"
					+"<CabinetName>"+cabinetName+"</CabinetName>\n"
					+"<UserDBId>"+sessionId+"</UserDBId>\n"
					+"<Documents>\n";
					
		String docIndexArr[]=docIndices.split(",");
		
		String docListXml="";
		
		for(String docIndex:docIndexArr){
			docListXml+="<Document>\n"
							+"<DocumentIndex>"+docIndex+"</DocumentIndex>\n"
							+"<ParentFolderIndex>"+parentFolderIndex+"</ParentFolderIndex>\n" 
							+"<ReferenceFlag>N</ReferenceFlag>\n" 
						+"</Document>\n";
		}
		
		requestXml=requestXml+docListXml
					+"</Documents>\n"     
					+"</NGODeleteDocumentExt_Input>";
					
		return requestXml;
	}
	
	/*
	 * ###################### Methods copied from Commons.java ########################
	 */

	/**
	 * Method for generating input xml for APSelect custom call
	 */
	public String getAPSelectInputXml(String engineName,
			String sessionId, String query) {
		return "<?xml version=\"1.0\"?>\n" 
			+ "<APSelect_Input>\n"
			+ "<Option>APSelect</Option>\n" 
			+ "<Query>" + query + "</Query>\n"
			+ "<EngineName>" + engineName + "</EngineName>\n" 
			+ "<SessionId>"+ sessionId + "</SessionId>\n" 
			+ "</APSelect_Input>";
	}

	public String getAPSelectWithColumnNamesInputXml(String engineName,
			String sessionId, String query) {
		return 
		"<?xml version=\"1.0\"?>\n" 
		+ "<APSelectWithColumnNames_Input>\n"
		+ "<Option>APSelectWithColumnNames</Option>\n" 
		+ "<Query>"+ query + "</Query>\n" 
		+ "<EngineName>" + engineName+ "</EngineName>\n" 
		+ "<SessionId>"+ sessionId + "</SessionId>\n"
		+ "</APSelectWithColumnNames_Input>";
	}
	
	public String getAPSelectWithColumnNameInputXml(String query,String database) {

		return
			"<?xml version=\"1.0\"?>\n"
			+"<APSelect_Input>\n"
			+"<Option>APSelectWithColumnNames</Option>\n"
			+"<Query>" + query + "</Query>\n"
			+"<EngineName>" + database+ "</EngineName>\n"
			+"</APSelect_Input>\n";
	}
	
	public String getAPInsertInputXml(String engineName,
			String sessionId, String tableName, String columns, String values) {
		return "<?xml version=\"1.0\"?>\n"
				+ "<APInsert_Input>\n" 
				+ "<Option>APInsert</Option>\n" 
				+ "<TableName>"+ tableName + "</TableName>\n" 
				+ "<ColName>" + columns+ "</ColName>\n" 
				+ "<Values>" + values + "</Values>\n"
				+ "<EngineName>" + engineName + "</EngineName>\n" 
				+ "<SessionId>"+ sessionId + "</SessionId>\n" 
				+ "</APInsert_Input>";
	}

	public String getAPUpdateInputXml(String engineName,
			String sessionId, String tableName, String columnName,
			String values, String where) {
		return "<?xml version=\"1.0\"?>\n"
				+ "<APUpdate_Input>\n" 
				+ "<Option>APUpdate</Option>\n" 
				+ "<TableName>"+ tableName + "</TableName>\n" 
				+ "<ColName>" + columnName+ "</ColName>\n" 
				+ "<Values>" + values + "</Values>\n"
				+ "<WhereClause>" + where + "</WhereClause>\n" 
				+ "<EngineName>"+ engineName + "</EngineName>\n" 
				+ "<SessionId>" + sessionId+ "</SessionId>\n" 
				+ "</APUpdate_Input>\n";
	}

	public String getAPDeleteInputXml(String engineName,
			String sessionId, String tableName, String where) {
		return "<?xml version=\"1.0\"?>"
				+ "<APDelete_Input><Option>APDelete</Option>" + "<TableName>"
				+ tableName + "</TableName>" + "<WhereClause>" + where
				+ "</WhereClause>" + "<EngineName>" + engineName
				+ "</EngineName>" + "<SessionId>" + sessionId + "</SessionId>"
				+ "</APDelete_Input>";
	}

	public String getAPProcedureInputXml(String engineName,
			String sessionId, String param, String procName) {
		return "<?xml version=\"1.0\"?>" + "<APProcedure_Input>"
				+ "<Option>APProcedure</Option>" + "<SessionId>" + sessionId
				+ "</SessionId>\n" + "<ProcName>" + procName + "</ProcName>\n"
				+ "<Params>" + param + "</Params>" + "<EngineName>"
				+ engineName + "</EngineName>" + "<APProcedure_Input>";

	}
	
	public String getAPProcedureInputXML(String engineName,
			String sessionId, String param, String procName, String noOfCols) {
		return "<?xml version=\"1.0\"?>" 
			+ "<APProcedure_Input>" 
			+ "<Option>APProcedure</Option>" 
			+ "<SessionId>"+ sessionId + "</SessionId>\n" 
			+ "<ProcName>" + procName + "</ProcName>\n" 
			+ "<Params>" + param+ "</Params>" 
			+ "<EngineName>" + engineName + "</EngineName>" 
			+ "<NoOfCols>" + noOfCols + "</NoOfCols>"
			+ "<APProcedure_Input>";

	}
	
	public String getAPDeleteExtdInputXml(String engineName,
			String tableName, String where) {
		return "<?xml version=\"1.0\"?>"
				+ "<APDeleteExtd_Input>" 
				+ "<Option>APDeleteExtd</Option>" 
				+ "<TableName>" + tableName + "</TableName>" 
				+ "<WhereClause>" + where+ "</WhereClause>" 
				+ "<EngineName>" + engineName + "</EngineName>" 
				+ "</APDeleteExtd_Input>";
	}
	

	public String getAPProcedureExtdInputXml(String engineName,
			String param,String procName) {
		return "<?xml version=\"1.0\"?>" 
				+ "<APProcedureExtd_Input>"
				+ "<Option>APProcedureExtd</Option>" 
				+ "<ProcName>" + procName + "</ProcName>\n"
				+ "<Params>" + param + "</Params>" 
				+ "<EngineName>" + engineName + "</EngineName>" 
				+ "<APProcedureExtd_Input>";

	}	
	
	/**
	 * 
	 * @param engineName
	 * @param tableName
	 * @param columnName
	 * @param values
	 * @param where
	 * @return
	 */
	public String getAPUpdateExtdInputXml(String engineName,
			String tableName, String columnName,
			String values, String where) {
		return "<?xml version=\"1.0\"?>\n"
				+ "<APUpdateExtd_Input>\n" 
				+ "<Option>APUpdateExtd</Option>\n" 
				+ "<TableName>" + tableName + "</TableName>\n" 
				+ "<ColName>" + columnName + "</ColName>\n" 
				+ "<Values>" + values + "</Values>\n"
				+ "<WhereClause>" + where + "</WhereClause>\n" 
				+ "<EngineName>" + engineName + "</EngineName>\n" 
				+ "</APUpdateExtd_Input>\n";
	}
	
	public String getAPInsertExtdInputXml(String engineName,
			String tableName, String columns,String values) {
		return "<?xml version=\"1.0\"?>"
				+ "<APInsertExtd_Input>" 
				+ "<Option>APInsertExtd</Option>"
				+ "<TableName>" + tableName + "</TableName>"
				+ "<ColName>" + columns + "</ColName>" 
				+ "<Values>" + values + "</Values>" 
				+ "<EngineName>" + engineName + "</EngineName>" 
				+ "</APInsertExtd_Input>";
	}

	/**
	 * methods added from swift parser utility
	 */
	public String sendMail(String cabinetname, String sessionID,
			String serverIP, String port, String AttachmentPath,
			String AttachmentName, String volumeID, String mailFrom,
			String mailTo, String mailSubject, String mailMessage) {
		String attachmentIndex;
		if (AttachmentPath == null) {
			attachmentIndex = "";
		} else if (AttachmentPath.equalsIgnoreCase("")) {
			attachmentIndex = "";
		} else {
			File fppp = new File(AttachmentPath);
			if (fppp.exists()) {
				JPISIsIndex IsIndex = attachMailDoc(fppp, volumeID, serverIP,
						port, cabinetname);
				Integer intISIndex;
				int intVolumeId;
				intISIndex = (int) IsIndex.m_nDocIndex;
				intVolumeId = (int) IsIndex.m_sVolumeId;
				attachmentIndex = intISIndex + "#" + intVolumeId + "#;";
			} else {
				attachmentIndex = "";
			}
		}

		return "<?xml version=\"1.0\"?>\n"
				+ "<WFAddToMailQueue_Input>\n"
				+ "<Option>WFAddToMailQueue</Option>\n" 
				+ "<EngineName>"+ cabinetname+ "</EngineName>\n"
				+ "<SessionId>"+ sessionID+ "</SessionId>\n"
				+ "<MailFrom>"+ mailFrom+ "</MailFrom>\n"
				+ "<MailTo>"+ mailTo+ "</MailTo>\n"
				+ "<MailCC></MailCC>\n"
				+ "<MailSubject>"+ mailSubject+ "</MailSubject>\n"
				+ "<MailMessage>"+ mailMessage+ "</MailMessage>\n"
				+ "<AttachmentISIndex>"+ attachmentIndex+ "</AttachmentISIndex>\n"
				+ "<AttachmentNames>"+ AttachmentName+ ";</AttachmentNames>\n"
				+ "<AttachmentExts></AttachmentExts>\n"
				+ "</WFAddToMailQueue_Input>";

	}

	public JPISIsIndex attachMailDoc(File fppp, String volumeID,
			String serverIP, String port, String cabinetname) {
		JPDBRecoverDocData JPISDEC = new JPDBRecoverDocData();
		JPISDEC.m_cDocumentType = 'N';
		JPISDEC.m_sVolumeId = Short.parseShort(volumeID);
		JPISIsIndex IsIndex = new JPISIsIndex();
		try {
			if (port.startsWith("33")) {
				CPISDocumentTxn.AddDocument_MT(null, serverIP, (short) (Integer
						.parseInt(port)), cabinetname, JPISDEC.m_sVolumeId,
						fppp.getPath(), JPISDEC, "", IsIndex);
			} else {
				CPISDocumentTxn.AddDocument_MT(null, serverIP, (short) (Integer
						.parseInt(port)), cabinetname, JPISDEC.m_sVolumeId,
						fppp.getPath(), JPISDEC, "", "JNDI", IsIndex);
			}

		} catch (Exception e) {
		} catch (JPISException e) {
			final Writer result = new StringWriter();
			final PrintWriter printWriter = new PrintWriter(result);
			e.printStackTrace(printWriter);
		}
		return IsIndex;
	}
	
	public String getWMFetchWorkListWithLockInputXml(String engineName, 
			String sessionId, String queueId) {
		return "<?xml version=\"1.0\"?>\n"
			+ "<WFFetchWorkItemsWithLock_Input>\n"
			+ "<Option>WFFetchWorkItemsWithLock</Option>\n"
			+ "<EngineName>" + engineName + "</EngineName>\n"
			+ "<SessionId>" + sessionId + "</SessionId>\n"
			+ "<QueueId>" + queueId + "</QueueId>\n"
			+ "<DataFlag>Y</DataFlag>\n"
			+ "<CountFlag>Y</CountFlag>\n"
			+ "<OrderBy>2</OrderBy>\n" //order by ProcessInstanceId
			+ "</WFFetchWorkItemsWithLock_Input>\n";
	}
	
	/**
	 * This method returns the request xml for fetching workitem list over a queueid,
	 * in batches.
	 * @param engineName
	 * @param sessionId
	 * @param queueId
	 * @param batchSize
	 * @param lastProcessIntanceId : This is the Process Instance ID of the last workitem fetched in the previous batch. 
	 * This tag need not be specified while fetching the 1st batch.
	 * @param orderBy : 1 - PriorityLevel,2 - ProcessInstanceId,3 - ActivityName,4 - LockedByName,
	 * 5 - IntroducedBy,6 - InstrumentStatus,7 -CheckListCompleteFlag,8 - LockStatus,9 - WorkitemState,
	 * 10 - EntryDateTime,11 - ValidTill,12 - LockedTime,13 - IntroductionDateTime,17 - Status,18 - CreatedDateTime
	 * @param sortOrder : A – Ascending,D – Descending. If no value is specified for this Tag, the system defaults it to D
	 * @return : request xml for fetching workitems with locking them
	 */
	public String getWMFetchWorkListWithLockInputXmlNew(String engineName, 
			String sessionId, String queueId,String batchSize,String lastProcessIntanceId,String orderBy,String sortOrder) {
		return "<?xml version=\"1.0\"?>\n"
			+ "<WFFetchWorkItemsWithLock_Input>\n"
			+ "<Option>WFFetchWorkItemsWithLock</Option>\n"
			+ "<EngineName>" + engineName + "</EngineName>\n"
			+ "<SessionId>" + sessionId + "</SessionId>\n"
			+ "<QueueId>" + queueId + "</QueueId>\n"
			+ "<BatchInfo>\n"
				+ "<NoOfRecordsToFetch>" + batchSize + "</NoOfRecordsToFetch>\n"
				+ "<LastProcessInstance>"+lastProcessIntanceId+"</LastProcessInstance>\n"
				+ "<SortOrder>"+sortOrder+"</SortOrder>\n"
				+ "<OrderBy>"+orderBy+"</OrderBy>\n"
			+ "</BatchInfo>\n"
			+ "<DataFlag>Y</DataFlag>\n"
			+ "<CountFlag>Y</CountFlag>\n"
			+ "</WFFetchWorkItemsWithLock_Input>\n";
	}
	
	/**
	 * This method returns the request xml for fetching workitem list over a queueid,
	 * in batches.
	 * @param engineName : cabinet name
	 * @param sessionId : session id
	 * @param queueId : queue id for which workitems has to be fetched
	 * @param batchSize : size of each fetch
	 * @param lastValue : This tag will contain the last value of OrderBy tag. e.g., If value of OrderBy tag is 2 which means ProcessInstanceId. Hence this tag will contains the last value of ProcessInstanceId fetched.
	 * @param lastWorkItemId : This tag will contain the value of WorkItemId of the last ProcessInstance fetched
	 * @param lastProcessIntanceId : This is the Process Instance ID of the last workitem fetched in the previous batch. 
	 * This tag need not be specified while fetching the 1st batch.
	 * @param orderBy : 1 - PriorityLevel,2 - ProcessInstanceId,3 - ActivityName,4 - LockedByName,
	 * 5 - IntroducedBy,6 - InstrumentStatus,7 -CheckListCompleteFlag,8 - LockStatus,9 - WorkitemState,
	 * 10 - EntryDateTime,11 - ValidTill,12 - LockedTime,13 - IntroductionDateTime,17 - Status,18 - CreatedDateTime
	 * @param sortOrder : A – Ascending,D – Descending. If no value is specified for this Tag, the system defaults it to D
	 * @return : request xml for fetching workitems with locking them
	 */
	public String getWMFetchWorkListWithLockInputXmlNew(String engineName, 
			String sessionId, String queueId, String batchSize,
			String lastValue, String lastWorkItemId,
			String lastProcessIntanceId, String orderBy, String sortOrder) {
		return "<?xml version=\"1.0\"?>\n"
			+ "<WFFetchWorkItemsWithLock_Input>\n"
			+ "<Option>WFFetchWorkItemsWithLock</Option>\n"
			+ "<EngineName>" + engineName + "</EngineName>\n"
			+ "<SessionId>" + sessionId + "</SessionId>\n"
			+ "<QueueId>" + queueId + "</QueueId>\n"
			+ "<BatchInfo>\n"
				+ "<NoOfRecordsToFetch>" + batchSize + "</NoOfRecordsToFetch>\n"
				+ "<LastValue>" + lastValue + "</LastValue>\n"
				+ "<LastWorkItem>" + lastWorkItemId + "</LastWorkItem>\n"
				+ "<LastProcessInstance>"+lastProcessIntanceId+"</LastProcessInstance>\n"
				+ "<SortOrder>"+sortOrder+"</SortOrder>\n"
				+ "<OrderBy>"+orderBy+"</OrderBy>\n"
			+ "</BatchInfo>\n"
			+ "<DataFlag>Y</DataFlag>\n"
			+ "<CountFlag>Y</CountFlag>\n"
			+ "</WFFetchWorkItemsWithLock_Input>\n";
	}
	public String getWMSetAttributesInputXml(String engineName, 
			String sessionId, String processInstanceId, String workItemId,
			String columnName, String colValue) {
		return "<?xml version=\"1.0\"?>"
			+ "<WFSetAttributes_Input>"
			+ "<Option>WMAssignWorkItemAttributes</Option>"
			+ "<EngineName>" + engineName + "</EngineName>"
			+ "<SessionId>" + sessionId + "</SessionId>"
			+ "<ProcessInstanceId>" + processInstanceId + "</ProcessInstanceId>"
			+ "<WorkItemId>" + workItemId + "</WorkItemId>"
			+ "<Attributes>" 
				+ "<Attribute>" 
					+ "<Name>" + columnName + "</Name>" 
					+ "<Value>" + colValue + "</Value>" 
				+ "</Attribute>" 
			+ "</Attributes>"
			+ "</WFSetAttributes_Input>";
	}
	
	public static String getWMSetAttributesInputXmlNew(String engineName, 
			String sessionId, String processInstanceId, String workItemId,
			String columnName, String colValue) {
		String[] cols=columnName.split(",");
		String[] values=colValue.split(",");
		String xml = "<?xml version=\"1.0\"?>\n"
			+ "<WFSetAttributes_Input>\n"
			+ "<Option>WMAssignWorkItemAttributes</Option>\n"
			+ "<EngineName>" + engineName + "</EngineName>\n"
			+ "<SessionId>" + sessionId + "</SessionId>\n"
			+ "<ProcessInstanceId>" + processInstanceId + "</ProcessInstanceId>\n"
			+ "<WorkItemId>" + workItemId + "</WorkItemId>\n"
			+ "<Attributes>\n" ;
		String attributeList="";
		for(int i=0;i<cols.length;i++){
			attributeList+="<Attribute>\n" 
						+ "<Name>" + cols[i] + "</Name>\n" 
						+ "<Value>" + values[i] + "</Value>\n" 
						+ "</Attribute>\n";
		}
		
		xml=xml+attributeList
				+ "</Attributes>\n"
				+ "</WFSetAttributes_Input>\n";
		return xml;
	}
	
	public String getNGOGetDocumentListExtInputXml(String cabinetName,
			String sessionId, String folderIndex, String noOfRecordsToFetch) {
		return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
				+ "<NGOGetDocumentListExt_Input>\n"
				+ "<Option>NGOGetDocumentListExt</Option>\n" 
				+ "<CabinetName>"+ cabinetName + "</CabinetName>\n" 
				+ "<UserDBId>" + sessionId+ "</UserDBId>\n" 
				+ "<FolderIndex>" + folderIndex+ "</FolderIndex>\n" 
				+ "<NoOfRecordsToFetch>"+ noOfRecordsToFetch + "</NoOfRecordsToFetch>\n"
				+ "<OrderBy>2</OrderBy>\n" 
				+ "<DataAlsoFlag>N</DataAlsoFlag>\n"
				+ "</NGOGetDocumentListExt_Input>";
	}
	
	public String getNGOSearchDocumentExtInputXml(String cabinetName,String sessionId, String folderIndex, String docName) {
		return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
				+ "<NGOSearchDocumentExt_Input>"
				+ "<Option>NGOSearchDocumentExt</Option>" 
				+ "<CabinetName>"+ cabinetName + "</CabinetName>" 
				+ "<UserDBId>" + sessionId+ "</UserDBId>" 
				+ "<LookInFolder>" + folderIndex+ "</LookInFolder>" 
				+ "<Name>"+ docName + "</Name>"
				+ "</NGOSearchDocumentExt_Input>";
	}

	public String getWMDisconnectInputXml(String cabinetName, String sessionId) {
		return "<?xml version=\"1.0\"?>"
			+ "<WMDisConnect_Input>\n"
				+ "<Option>WMDisConnect</Option>\n" 
				+ "<EngineName>"+cabinetName+"</EngineName>\n"
				+ "<SessionID>"+sessionId+"</SessionID>\n" 
			+ "</WMDisConnect_Input>\n";
	}

}

