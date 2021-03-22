/**
 * ******************************************************************
 * NEWGEN SOFTWARE TECHNOLOGIES LIMITED 
 * Group : 
 * Product / Project : ADF
 * Process Module : SRM Utility
 * File Name : PushtWIs.java
 * Author : Sivashankar S 
 * Date written (DD/MM/YYYY) : 05/10/2017
 * Description :
 * CHANGE HISTORY
 * **********************************************************************************************
 * Date	Change By	Change Description (Bug No. (If Any)) (DD/MM/YYYY)
 * **********************************************************************************************
 */
package com.newgen.util.MoveWI;

import com.newgen.util.logger.LogMessages;
import com.newgen.wfdesktop.xmlapi.WFXmlResponse;
import java.text.SimpleDateFormat;
import java.util.Date;


//comment by lawal
//svn test aliu

public class PushtWIs extends Thread {

    private boolean run = true;

    public PushtWIs() {
    	super();
        System.out.println("Starting Utility.");
    }


    /**
     * ******************************************************************
     * Function Name : processCases 
     * Date Written :31/08/2020
     * Author : Vinoth R
     * Input Parameters : None 
     * Output Parameters : None Return
     * Values : None 
     * Description : 
     *
     ***********************************************************************
     */
    private void processCases() throws Exception {
        ServiceUtil objUtil = new ServiceUtil(ReadINI.strCabinetName, ReadINI.strServerIp,
                ReadINI.strServerPort, ReadINI.strServerType, ReadINI.strWrapperPort,
                ReadINI.strUserName, ReadINI.strPassword,ReadINI.dateLimit,ReadINI.batchlimit,ReadINI.strProcessdefID,ReadINI.strQueueID);

        int connectionStatus = 0;

        try {
            System.out.println("Connecting Cabinet..");
            LogMessages.logStatus("Connecting Cabinet..", true);
            connectionStatus = objUtil.ConnectCabinet();
            if (connectionStatus != 1) {
                System.out.println("Cabinet connection failed.");
                LogMessages.logStatus("Cabinet connection failed.");
            } else {
            	System.out.println("Move Workitem to Next Workstep");
            	System.out.println("**************************************************");
            	LogMessages.logStatus("Move Workitem to Next Workstep");
            	LogMessages.logStatus("**************************************************");
            	objUtil.postingmaturtity();
            	//to check window cutofftime to create WI
            	objUtil.createWi();
            	
            	//objUtil.checkwindowtime();
            	//Read data directly from based on ws name--no need to read wi
            	//objUtil.processandcalculatedata();
            	
            	
            	
            	//objUtil.unlockWIs();            
            	/*String valquery = "select * from queuedeftable where queueid = '"+ReadINI.strQueueID +
            			"' and queuename = '"+ReadINI.strWorkstepName+"'";
        		LogMessages.logStatus("Query :- "+ valquery);
                String outputxml = objUtil.APSelect(valquery);            	
                LogMessages.logStatus("outputxml :- "+ outputxml);
                WFXmlResponse valxml = new WFXmlResponse(outputxml);   
                if(valxml.getVal("TotalRetrieved").equalsIgnoreCase("1")){
                	String tdval = valxml.getVal("QueueID");
                	LogMessages.logStatus("QueueID is ---- > "+tdval);
                	//objUtil.fetchqueue(ReadINI.strQueueID);
                }else{
                	//No record found in queuedeftable for queueid : and queuename. Recheck the queueid and queuename 
                	System.out.println("Conflict in Queue ID and Workstep name. Kindly check in WFQueueDefTable for queue id : "+ReadINI.strQueueID + " and workstep name : "+ ReadINI.strWorkstepName);
            		LogMessages.logStatus("Conflict in Queue ID and Workstep name. Kindly check in WFQueueDefTable for queue id : "+ReadINI.strQueueID + " and workstep name : "+ ReadINI.strWorkstepName);
                }*/
                
            }
        } catch (Exception e) {
            LogMessages.logStatus("Error in processing Case : " + e.getMessage());
            LogMessages.logError("Error in processing the Case. ", e);
            throw new Exception("Error in processing Case : " + e.getMessage());
        } finally {
            if (connectionStatus == 1) {
                objUtil.DisConnectCabinet();
                System.out.println("Finally disconnect");
            }
        }

    }

    /*
     * Function Name        : run 
     * Input Parameters     : none
     * Output parameters    : none 
     * Return Values        : none 
     * Description          : This function is called to run the utility on a periodic basis
     * Global Variables     : startTime,endTime,strSleep
     */
    public void run() {
    	long interval=0;
        long inSec=0;
        long inMin=0;
        try {
            if (LogMessages.setLogFiles()) {
                LogMessages.logStatus("Starting Utility.");
                ReadINI.ReadIni();
                if (ReadINI.bool) {
                    while (run) {
                        Date dtCurDate = new Date();
                        String currtime = "";
                        SimpleDateFormat sdfTime = new SimpleDateFormat("HHmm");
                        currtime = sdfTime.format(dtCurDate);
                        System.out.println("Utility Start Time :" + ReadINI.startTime);
                        System.out.println("Utility End Time :" + ReadINI.endTime);
                        System.out.println("Current Time :" + currtime);
                        if ((Integer.parseInt(currtime) >= Integer.parseInt(ReadINI.startTime)) && Integer.parseInt(currtime) <= Integer.parseInt(ReadINI.endTime)) {
                            try {
                            	interval=Integer.parseInt(ReadINI.intervalTime);
                                inSec = interval/1000;
                                inMin = inSec/60;
                                processCases();
                                if(inMin != 0){
                                	System.out.println("Utility Starts after  : " + inMin + "mins");
                                }else{
                                	System.out.println("Utility Starts after  : " + inSec + "sec");
                                }
                                Thread.sleep(interval);
                            } catch (InterruptedException ex) {
                            	 LogMessages.logStatus("InterruptedException " + ex.getMessage());
                            } catch (Exception ex) {
                            	System.out.println("Exception Occured : " + ex.getMessage());
                                LogMessages.logStatus("Exception Occured : " + ex.getMessage());
                                LogMessages.logError("Error in processing the Case. ", ex);
                            }
                        } else {
                            try {
                            	System.out.println("Utility start at : " + ReadINI.startTime);
                                Thread.sleep(3600000);
                            } catch (InterruptedException ex) {
                                System.out.println("InterruptedException " + ex.getMessage());
                                LogMessages.logStatus("Exception Occured : " + ex.getMessage());
                                LogMessages.logError("Error in processing the Case. ", ex);
                            }
                        }
                    }
                } else {
                    System.out.println("Error in INI file. Kindly check.");
                    LogMessages.logStatus("Error in INI file. Kindly check.");
                }
            } else {
                System.out.println("***********STOPPED************");
                System.out.println("Reason : Error in Set up log4j.");
                System.out.println("Solution : Kindly Check log4j.properties is pressent under user directory /utilityconfig folder ");
            }


        } catch (Exception ex) {
        	 try {
	            LogMessages.logStatus("Exception Occured : " + ex.getMessage());
	            LogMessages.logError("Exception Occured :", ex);
	            if(inMin != 0){
	            	System.out.println("Utility Starts after  : " + inMin + "mins");
	            }else{
	            	System.out.println("Utility Starts after  : " + inSec + "sec");
	            }	           
					Thread.sleep(interval);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }

    }

    /*
     * Function Name        : stopThread 
     * Input Parameters     : none
     * Output parameters    : none 
     * Return Values        : none 
     * Description          : This function is called to stop the utility
     */
    public void stopThread() {
        System.out.println("Thread going to exit");
        run = false;
        this.interrupt();
        System.out.println("Utility is stoped successfully");
    }

    /*
     * Function Name        : exitFun 
     * Input Parameters     : none
     * Output parameters    : none 
     * Return Values        : none 
     * Description          : This function is called to exit from program
     */
    public void exitFun() {
        run = false;
        this.interrupt();
    }
    
    
}