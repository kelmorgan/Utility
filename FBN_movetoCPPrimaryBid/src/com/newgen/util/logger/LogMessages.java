/**
 * ******************************************************************
 * NEWGEN SOFTWARE TECHNOLOGIES LIMITED Group : CIG Product / Project :
 * HP-eInvoice Module : File Name : LogMessages.java Author : SIVASHANKAR S Date
 * written(DD/MM/YYYY) : 01/09/2014 Description :
 *
 * CHANGE HISTORY
 * ***********************************************************************************************************
 * Date	Change By	Change Description (Bug No. (If Any))
 * **********************************************************************************************************
 */
package com.newgen.util.logger;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public final class LogMessages {

    private static Logger errorLog = null;
    private static Logger statusLog = null;
    private static Properties logProp = null;

    // ----------------------------------------------------------------
    // Function Name : setLogFiles
    // Date Written : 31/08/2020
    // Author : Vinoth R
    // Input Parameters : none
    // Output Parameters : none
    // Return Values : none
    // Description : Set up Log File
    // ----------------------------------------------------------------
    public static boolean setLogFiles() {
        InputStream is = null;
        try {
            String filePath = System.getProperty("user.dir") + File.separator + "config" + File.separator + "log4j.properties";
            File file = new File(filePath);
            if (file.exists()) {
                is = new BufferedInputStream(new FileInputStream(filePath));
                logProp = new Properties();
                logProp.load(is);
                is.close();
                PropertyConfigurator.configure(logProp);
                errorLog = Logger.getLogger("ServiceError");
                statusLog = Logger.getLogger("ServiceStatus");
                dumpinitialLog();
                return true;
            } else {
                System.out.println("log4j.properties file not exits present in :" + filePath);
                return false;
            }
        } catch (Exception e) {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException te) {
                errorLog.info("Error in setting Logger : " + te);
            }
            e.printStackTrace();
            return false;
        }
    }

    public static Properties getProperties() {
        return logProp;
    }

    public static void dumpinitialLog() {

        errorLog.info("=============================================================");
        errorLog.info("Error Log Initialized");
        errorLog.info("=============================================================");

        statusLog.info("============================================================");
        statusLog.info("Status Log Initialized");
        statusLog.info("=============================================================");
    }

    public static void dumpfinalLog() {
        errorLog.info("=============================================================");
        errorLog.info("Error Log Completed");
        errorLog.info("=============================================================");

        statusLog.info("============================================================");
        statusLog.info("Status Log Completed");
        statusLog.info("=============================================================");
    }

    public static void logError(String errorMsg, Error error) {
        errorLog.info(errorMsg, error);
    }

    public static void logError(String errorMsg, Exception error) {
        errorLog.info(errorMsg, error);
    }

    public static void logStatus(String msg) {
        statusLog.info(msg);
    }

    public static void logStatus(String msg, boolean outline) {
        statusLog.info("============================================================");
        statusLog.info(msg.toUpperCase());
        statusLog.info("============================================================");
    }
}
