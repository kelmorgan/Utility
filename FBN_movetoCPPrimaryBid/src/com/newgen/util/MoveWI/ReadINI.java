/**
 * ******************************************************************
 * NEWGEN SOFTWARE TECHNOLOGIES LIMITED Group : Product / Project : ICICI - CMS
 * Collections Process Module : CMS Collection File Name : ConfigReader.java
 * Author : Sivashankar S Date written (DD/MM/YYYY) : 01/08/2014 Description :
 * CHANGE HISTORY
 * **********************************************************************************************
 * Date	Change By	Change Description (Bug No. (If Any)) (DD/MM/YYYY)
 ***********************************************************************************************
 */
package com.newgen.util.MoveWI;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import com.newgen.util.logger.LogMessages;

public class ReadINI {

    public static String strServerIp = "";
    public static String strServerPort = "";
    public static String strServerType = "";
    public static String strWrapperPort = "";
    public static String strCabinetName = "";
    public static String strUserName = "";
    public static String strPassword = "";
    
    public static String strProcessdefID ="";
    public static String strProcessName ="";
    public static String strQueueID ="";
    public static String strWorkstepName ="";
    public static String dateLimit ="";
    public static String batchlimit = "";
    
    public static boolean bool = false;
    public static String startTime = "", endTime = "", intervalTime="";
    
    public static void ReadIni() throws Exception {
        try {
            String strPropertyPath = System.getProperty("user.dir") + File.separator + "config" + File.separator 
            		+"prop.ini";
            FileInputStream File_Ini = new FileInputStream(strPropertyPath);
            Properties prop = new Properties();
            System.out.println("Reading INI file");
            LogMessages.logStatus("Reading INI file");
            prop.load(File_Ini);
            strServerIp = prop.getProperty("AppServerIp");
            strServerPort = prop.getProperty("AppServerPort");
            strServerType = prop.getProperty("AppServerType");
            strWrapperPort = prop.getProperty("WrapperPort");
            strCabinetName = prop.getProperty("CabinetName");
            strUserName = prop.getProperty("UserName");
            strPassword = prop.getProperty("Password");
            strProcessdefID = prop.getProperty("ProcessdefID");
            strProcessName = prop.getProperty("ProcessName");
            strQueueID = prop.getProperty("QueueId");
            strWorkstepName = prop.getProperty("WorkstepName");
            dateLimit = prop.getProperty("dateLimit");
            batchlimit = prop.getProperty("batchlimit");
            startTime = prop.getProperty("STARTTIME");
            endTime = prop.getProperty("ENDTIME");
            intervalTime = prop.getProperty("INTERVALTIME");
            
            if (strServerIp.equalsIgnoreCase("")) {
                System.out.println("AppServerIp value is missing in the Ini File.");
                LogMessages.logStatus("AppServerIp value is missing in the Ini File.");
                bool = false;
                return;
            } else if (strServerPort.equalsIgnoreCase("")) {
                System.out.println("AppServerPort value is missing in the Ini File.");
                LogMessages.logStatus("AppServerPort value is missing in the Ini File.");
                bool = false;
                return;
            } else if (strServerType.equalsIgnoreCase("")) {
                System.out.println("AppServerType value is missing in the Ini File.");
                LogMessages.logStatus("AppServerType value is missing in the Ini File.");
                bool = false;
                return;
            } else if  (strWrapperPort.equalsIgnoreCase("")) {
                System.out.println("WrapperPort value is missing in the Ini File.");
                LogMessages.logStatus("WrapperPort value is missing in the Ini File.");
                bool = false;
                return;
            } else if (strCabinetName.equalsIgnoreCase("")) {
                System.out.println("CabinetName value is missing in the Ini File.");
                LogMessages.logStatus("CabinetName value is missing in the Ini File.");
                bool = false;
                return;
            } else if (strUserName.equalsIgnoreCase("")) {
                System.out.println("UserName value is missing in the Ini File.");
                LogMessages.logStatus("UserName value is missing in the Ini File.");
                bool = false;
                return;
            } else if (strPassword.equalsIgnoreCase("")) {
                System.out.println("Password value is missing in the Ini File.");
                LogMessages.logStatus("Password value is missing in the Ini File.");
                bool = false;
                return;
            } else if (strProcessdefID.equalsIgnoreCase("")) {
                System.out.println("ProcessdefID value is missing in the Ini File.");
                LogMessages.logStatus("ProcessdefID value is missing in the Ini File.");
                bool = false;
                return;
            } else if (strProcessName.equalsIgnoreCase("")) {
                System.out.println("ProcessName value is missing in the Ini File.");
                LogMessages.logStatus("ProcessName value is missing in the Ini File.");
                bool = false;
                return;
            } else if (strQueueID.equalsIgnoreCase("")) {
                System.out.println("QueueId value is missing in the Ini File.");
                LogMessages.logStatus("QueueId value is missing in the Ini File.");
                bool = false;
                return;
            } else if (strWorkstepName.equalsIgnoreCase("")) {
                System.out.println("WorkstepName value is missing in the Ini File.");
                LogMessages.logStatus("WorkstepName value is missing in the Ini File.");
                bool = false;
                return;
            } else if (startTime.equalsIgnoreCase("")) {
                System.out.println("STARTTIME value is missing in the Ini File.");
                LogMessages.logStatus("STARTTIME value is missing in the Ini File.");
                bool = false;
                return;
            } else if (endTime.equalsIgnoreCase("")) {
                System.out.println("ENDTIME value is missing in the Ini File.");
                LogMessages.logStatus("ENDTIME value is missing in the Ini File.");
                bool = false;
                return;
            } else if (intervalTime.equalsIgnoreCase("")) {
                System.out.println("INTERVALTIME value is missing in the Ini File.");
                LogMessages.logStatus("INTERVALTIME value is missing in the Ini File.");
                bool = false;
                return;
            } else {
            	System.out.println("Read INI file success");
                bool = true;
            }
        } catch (Exception e) {
            bool = false;
            LogMessages.logStatus("Exception in ReadIni " + e.getMessage());
            LogMessages.logError(e.getMessage(), e);
            throw new Exception("Exception in ReadIni : " + e.getMessage());
        }
    }
}
