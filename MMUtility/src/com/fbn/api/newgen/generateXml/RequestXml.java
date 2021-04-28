package com.fbn.api.newgen.generateXml;
public class RequestXml  {

	public static String getConnectCabinetXml(String cabinetName, String userName, String password){
		StringBuilder stringBuilder = new StringBuilder();

		stringBuilder.append("<?xml version=1.0?>");
		stringBuilder.append("<WMConnect_input>");
		stringBuilder.append("<Option>WMConnect</Option>");
		stringBuilder.append("<EngineName>").append(cabinetName).append("</EngineName>");
		stringBuilder.append("<ApplicationInfo>127.0.0.1</ApplicationInfo>");
		stringBuilder.append("<Participant>");
		stringBuilder.append("<Name>").append(userName).append("</Name>");
		stringBuilder.append("<Password>").append(password).append("</Password>");
		stringBuilder.append("<Scope></Scope>");
		stringBuilder.append("<UserExist>Y</UserExist>");
		stringBuilder.append("<ParticipantType>U</ParticipantType>");
		stringBuilder.append("</Participant>");
		stringBuilder.append("<WMConnect_input>");

		return stringBuilder.toString();

	}
	public static String getDisconnectCabinetXml(String cabinetName,String sessionId){
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("<?xml version=1.0?>");
		stringBuilder.append("<WMDisConnect_Input>");
		stringBuilder.append("<Option>WMDisConnect</Option>");
		stringBuilder.append("<EngineName>").append(cabinetName).append("</EngineName>");
		stringBuilder.append("<SessionID>").append(sessionId).append("</SessionID>");
		stringBuilder.append("</WMDisConnect_Input>");
		return stringBuilder.toString();
	}

	public  static String getCreateWorkItemXml1(String cabinetName, String sessionId, String processDefId,String queueId,String attributes){
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("<?xml version=1.0?>");
		stringBuilder.append("<WFUploadWorkItem_Input>");
		stringBuilder.append("<Option>WFUploadWorkItem</Option>");
		stringBuilder.append("<EngineName>").append(cabinetName).append("</EngineName>");
		stringBuilder.append("<SessionId>").append(sessionId).append("</SessionId>");
		stringBuilder.append("<ProcessDefId>").append(processDefId).append("</ProcessDefId>");
		stringBuilder.append("<VariantId></VariantId>");
		stringBuilder.append("<QueueId>").append(queueId).append("</QueueId>");
		stringBuilder.append("<InitiateFromActivityId></InitiateFromActivityId>");
		stringBuilder.append("<InitiateAlso>Y</InitiateAlso>");
		stringBuilder.append("<Attributes>").append(attributes).append("</Attributes>");
		stringBuilder.append("<Documents></Documents>");
		stringBuilder.append("</WFUploadWorkItem_Input>");
		return stringBuilder.toString();
	}
	
	public  static String getCreateWorkItemXml(String cabinetName, String sessionId, String processDefId,String queueId,String attributes,String initiateFlag){
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("<?xml version=1.0?>");
		stringBuilder.append("<WFUploadWorkItem_Input>");
		stringBuilder.append("<Option>WFUploadWorkItem</Option>");
		stringBuilder.append("<EngineName>").append(cabinetName).append("</EngineName>");
		stringBuilder.append("<SessionId>").append(sessionId).append("</SessionId>");
		stringBuilder.append("<ProcessDefId>").append(processDefId).append("</ProcessDefId>");
		stringBuilder.append("<QueueId>").append(queueId).append("</QueueId>");
		stringBuilder.append("<DataDefName></DataDefName>");
		stringBuilder.append("<UserDefVarFlag>Y</UserDefVarFlag>");
		stringBuilder.append("<Fields></Fields>");
		stringBuilder.append("<InitiateAlso>").append(initiateFlag).append("</InitiateAlso>");
		stringBuilder.append("<InitiateFromActivityId/>");
		stringBuilder.append("<Attributes>").append(attributes).append("</Attributes>");
		stringBuilder.append("<Documents></Documents>");
		stringBuilder.append("</WFUploadWorkItem_Input>");
		return stringBuilder.toString();
	}
	
	public static String getAddToMailQueue(String cabinetName, String sessionId, String processDefId, String activityId, String processInstanceId, String mailFrom, String mailTo, String mailCC, String mailSubject, String mailMessage) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("<?xml version=1.0?>");
		stringBuilder.append("<WFAddToMailQueue_Input>");
		stringBuilder.append("<Option>WFAddToMailQueue</Option>");
		stringBuilder.append("<EngineName>").append(cabinetName).append("</EngineName>");
		stringBuilder.append("<SessionId>").append(sessionId).append("</SessionId>");
		stringBuilder.append("<ProcessDefId>").append(processDefId).append("</ProcessDefId>");
		stringBuilder.append("<ActivityId>").append(activityId).append("<ActivityId>");
		stringBuilder.append("<ProcessInstanceId>").append(processInstanceId).append("</ProcessInstanceId>");
		stringBuilder.append("<WorkItemId>1</WorkItemId>");
		stringBuilder.append("<MailFrom>").append(mailFrom).append("</MailFrom>");
		stringBuilder.append("<MailTo>").append(mailTo).append("</MailTo>");
		stringBuilder.append("<MailCC>").append(mailCC).append("</MailCC>");
		stringBuilder.append("<MailSubject>").append(mailSubject).append("</MailSubject>");
		stringBuilder.append("<MailMessage>").append(mailMessage).append("</MailMessage>");
		stringBuilder.append("</WFAddToMailQueue_Input>");		
		return stringBuilder.toString();
	}

	public static String getLockWorkItemInputXml(String cabinetName, String sessionId, String processInstanceId) {
		return  "<?xml version=\"1.0\" ?>\n"
				+ "<WMGetWorkItem_Input>\n"
				+ "<Option>WMGetWorkItem</Option>\n"
				+ "<EngineName>"+ cabinetName + "</EngineName>\n"
				+ "<SessionId>"+ sessionId + "</SessionId>\n"
				+ "<ProcessInstanceId>"+ processInstanceId + "</ProcessInstanceId>\n"
				+ "<WorkItemId>1</WorkItemId>\n"
				+"<Admin>Y</Admin>"
				+ "</WMGetWorkItem_Input>";
	}

	public static String getUnlockWorkItemXml (String cabinetName, String sessionId, String processInstanceId){
		return  "<?xml version=\"1.0\" ?>\n"
				+ "<WMUnlockWorkItem_Input>\n"
				+ "<Option>WMUnlockWorkItem</Option>\n"
				+ "<EngineName>"+ cabinetName + "</EngineName>\n"
				+ "<SessionId>"+ sessionId + "</SessionId>\n"
				+ "<ProcessInstanceId>"+ processInstanceId + "</ProcessInstanceId>\n"
				+ "<WorkItemId>1</WorkItemId>\n"
				+"<Admin>Y</Admin>"
				+ "</WMUnlockWorkItem_Input>";
	}
	public static String getCompleteWorkItemXml(String cabinetName,
										   String sessionId, String processInstanceId) {
		return "<?xml version=\"1.0\"?>\n"
				+ "<WMCompleteWorkItem_Input>\n"
				+ "<Option>WMCompleteWorkItem</Option>\n"
				+ "<EngineName>" + cabinetName + "</EngineName>\n"
				+ "<SessionId>" + sessionId + "</SessionId>\n"
				+ "<ProcessInstanceId>" + processInstanceId + "</ProcessInstanceId>\n"
				+ "<WorkItemId>1</WorkItemId>\n"
				+ "<AuditStatus></AuditStatus>\n"
				+ "<Comments></Comments>\n"
				+ "</WMCompleteWorkItem_Input>\n";
	}

	public static String getFetchWorkListXml (String cabinetName,String sessionId,String queueId, String batchLimit){
		StringBuilder stringBuilder = new StringBuilder();

		stringBuilder.append("<?xml version='1.0'?>");
		stringBuilder.append("<WMFetchWorkList_Input>");
		stringBuilder.append("<Option>WMFetchWorkList</Option>");
		stringBuilder.append("<EngineName>").append(cabinetName).append("</EngineName>");
		stringBuilder.append("<SessionId>").append(sessionId).append("</SessionId>");
		stringBuilder.append("<CountFlag>Y</CountFlag>");
		stringBuilder.append("<DataFlag>Y</DataFlag>");
		stringBuilder.append("<ZipBuffer>N</ZipBuffer>");
		stringBuilder.append("<FetchLockedFlag>N</FetchLockedFlag>");
		stringBuilder.append("<Filter><QueueId>").append(queueId).append("</QueueId>");
		stringBuilder.append("<Type>256</Type>");
		stringBuilder.append("<Comparison>0</Comparison>");
		stringBuilder.append("<FilterString></FilterString>");
		stringBuilder.append("</Filter>");
		stringBuilder.append("<BatchInfo>");
		stringBuilder.append("<NoOfRecordsToFetch>").append(batchLimit).append("</NoOfRecordsToFetch>");
		stringBuilder.append("<OrderBy>1</OrderBy>");
		stringBuilder.append("<SortOrder>A</SortOrder>");
		stringBuilder.append("</BatchInfo>");
		stringBuilder.append("<QueueType>N</QueueType>");
		stringBuilder.append("</WMFetchWorkList_Input>");

		return stringBuilder.toString();
	}

	public static String getAssignAttributeXml(String cabinetName,String sessionId,String wiName,String attributeName,String value){
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("<?xml version='1.0'?>");
		stringBuilder.append("<WMAssignWorkItemAttribute_Input>");
		stringBuilder.append("<Option>WMAssignWorkItemAttribute</Option>");
		stringBuilder.append("<EngineName>").append(cabinetName).append("</EngineName>");
		stringBuilder.append("<SessionId>").append(sessionId).append("</SessionId>");
		stringBuilder.append("<ProcessInstanceId>").append(wiName).append("</ProcessInstanceId>");
		stringBuilder.append("<WorkItemId>1</WorkItemId>");
		stringBuilder.append("<Attributes>");
		stringBuilder.append("<Attribute>");
		stringBuilder.append("<Name>").append(attributeName).append("</Name>");
		stringBuilder.append("<Type>10</Type>");
		stringBuilder.append("<Value>").append(value).append("</Value>");
		stringBuilder.append("</Attribute>");
		stringBuilder.append("</Attributes>");
		
		

		return stringBuilder.toString();
	}
	public static String getSelectQueryXml(String query,String cabinetName) {
		return  "<?xml version=\"1.0\"?>\n"
				+"<APSelect_Input>\n"
				+"<Option>APSelectWithColumnNames</Option>\n"
				+"<Query>" + query + "</Query>\n"
				+"<EngineName>" + cabinetName+ "</EngineName>\n"
				+"</APSelect_Input>\n";
	}
	public static String getInsertQueryXml(String engineName,
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
	public static String getUpdateQueryXml(String engineName,
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
	public static String getDeleteQueryXml(String engineName,
									String sessionId, String tableName, String where) {
		return "<?xml version=\"1.0\"?>"
				+ "<APDelete_Input><Option>APDelete</Option>" + "<TableName>"
				+ tableName + "</TableName>" + "<WhereClause>" + where
				+ "</WhereClause>" + "<EngineName>" + engineName
				+ "</EngineName>" + "<SessionId>" + sessionId + "</SessionId>"
				+ "</APDelete_Input>";
	}

    private static String connectCabinetXML(String strCabinetName, String strUserName, String strPassword){
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

	private String getFetchWorkItemAttributesXml(String processInstanceId,
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
	
	private String getGetWorkItemAttributeValueXml(String processInstanceId,
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

	private String getAPProcedureInputXml(String engineName,
			String sessionId, String param, String procName) {
		return "<?xml version=\"1.0\"?>" + "<APProcedure_Input>"
				+ "<Option>APProcedure</Option>" + "<SessionId>" + sessionId
				+ "</SessionId>\n" + "<ProcName>" + procName + "</ProcName>\n"
				+ "<Params>" + param + "</Params>" + "<EngineName>"
				+ engineName + "</EngineName>" + "<APProcedure_Input>";

	}
	
	private String getAPProcedureInputXML(String engineName,
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
	
	private String getAPDeleteExtdInputXml(String engineName,
			String tableName, String where) {
		return "<?xml version=\"1.0\"?>"
				+ "<APDeleteExtd_Input>" 
				+ "<Option>APDeleteExtd</Option>" 
				+ "<TableName>" + tableName + "</TableName>" 
				+ "<WhereClause>" + where+ "</WhereClause>" 
				+ "<EngineName>" + engineName + "</EngineName>" 
				+ "</APDeleteExtd_Input>";
	}
	

	private String getAPProcedureExtdInputXml(String engineName,
			String param,String procName) {
		return "<?xml version=\"1.0\"?>" 
				+ "<APProcedureExtd_Input>"
				+ "<Option>APProcedureExtd</Option>" 
				+ "<ProcName>" + procName + "</ProcName>\n"
				+ "<Params>" + param + "</Params>" 
				+ "<EngineName>" + engineName + "</EngineName>" 
				+ "<APProcedureExtd_Input>";

	}	
	

	private String getAPUpdateExtdInputXml(String engineName,
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
	
	private String getAPInsertExtdInputXml(String engineName,
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

}

