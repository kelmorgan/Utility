package com.fbn.api.fbn.controller;

import com.fbn.api.fbn.execute.Api;
import com.fbn.api.fbn.generateXml.RequestXml;
import com.fbn.utils.*;
import org.apache.log4j.Logger;


public class Controller implements ConstantsI {
	private final Logger logger = LogGen.getLoggerInstance("FbnController");
	private String outputXml;
	private final XmlParser xmlParser = new XmlParser();
	public String getUserLimit(){
	    return Api.executeCall(fetchLimitServiceName, RequestXml.getUserLimitXml("SN022357"));
    }

    public String getSearchTxn(String startDate, String endDate, String acctNo, String amount, String debitCredit, String transParts ){
		try {
			outputXml = Api.executeCall(searchTranServiceName, RequestXml.searchRequestXml(startDate, endDate, acctNo, amount, debitCredit, transParts));
			final String noDuplicateMsg = "NO record exist for entered details";

			if (!Commons.isEmpty(outputXml)) {
				xmlParser.setInputXML(outputXml);
				String respFlag = xmlParser.getValueOf(apiStatus);
				if (isSuccess(respFlag)) {
					String message = xmlParser.getValueOf("Success_1");
					logger.info("message: " + message);
					String txnIdApi = xmlParser.getValueOf("tranId");
					logger.info("txnIdApi: " + txnIdApi);

					if (!Commons.isEmpty(txnIdApi))
						return "Duplicate record exist for this Transaction. Kindly Check Finacle";
					else if (message.trim().equalsIgnoreCase(noDuplicateMsg))
						return False;

				} else if (isFailed(respFlag)) {
					String errCode = xmlParser.getValueOf("ErrorCode");
					String errDesc = xmlParser.getValueOf("ErrorDesc");
					String errType = xmlParser.getValueOf("ErrorType");
					logger.info("ErrorType : " + errType + " ErrorCode : " + errCode + " ErrorDesc : " + errDesc + ".");
					return "ErrorType : " + errType + " ErrorCode : " + errCode + " ErrorDesc : " + errDesc + ".";

				}
			} else {
				return apiNoResponse;
			}
		}catch (Exception e){
			return e.getMessage();
		}
	return null;
	}
    public String getPostTxn(String acct1, String sol1, String amount,String transParticulars, String partTranRemarks, String todayDate, String acct2, String sol2){
		try {
			outputXml = Api.executeCall(postServiceName, RequestXml.postTransactionXml(transType, transSubTypeC, acct1, sol1, debitFlag, amount, currencyNgn, transParticulars, partTranRemarks, todayDate, acct2, sol2, creditFlag, LoadProp.utilityUser));
			if (!Commons.isEmpty(outputXml)){
				xmlParser.setInputXML(outputXml);
				String status = xmlParser.getValueOf(apiStatus);

				if (isSuccess(status)){
					String txnId = xmlParser.getValueOf("TrnId");
					if (!Commons.isEmpty(txnId.trim()))
						return apiSuccess;
				}
				else if (isFailed(status)){
					String errCode = xmlParser.getValueOf("ErrorCode");
					String errDesc = xmlParser.getValueOf("ErrorDesc");
					String errType = xmlParser.getValueOf("ErrorType");
					logger.info("ErrorType : " + errType + " ErrorCode : " + errCode + " ErrorDesc : " + errDesc + ".");
					return "ErrorType : " + errType + " ErrorCode : " + errCode + " ErrorDesc : " + errDesc + ".";
				}
			}
			else return apiNoResponse;
		} catch (Exception e){
			return e.getMessage();
		}
		return null;
	}

	private boolean isSuccess(String data){
		return data.equalsIgnoreCase(apiSuccess);
	}
	private boolean isFailed(String data){
		return data.equalsIgnoreCase(apiFailed) || data.equalsIgnoreCase(apiFailure);
	}
}
