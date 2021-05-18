package com.fbn.utils;

import org.apache.log4j.Logger;

import java.io.*;
import java.util.Properties;

public class LoadProp implements ConstantsI {
    private static final Logger logger =  LogGen.getLoggerInstance("LoadProp");
    public static String serverIp;
    public static String socketPort;
    public static String headOfficeCpAcctNo;
    public static String headOfficeCpSol;
    public static String headOfficeTbAcctNo;
    public static String headOfficeTbSol;
    public static String utilityUser;
    public static String sleepTime;


    static {
        try {
            logger.info("Start loading properties file");
            Properties properties = new Properties();
            InputStream in = new FileInputStream(configPath);
            properties.load(in);
            serverIp = properties.getProperty(appServerIpField);
            logger.info("serverIp-- "+serverIp);
            socketPort = properties.getProperty(appSocketPortField);
            logger.info("socketPort-- "+socketPort);
            headOfficeCpAcctNo = properties.getProperty(headOfficeCpAcctNo);
            headOfficeCpSol = properties.getProperty(headOfficeCpSol);
            headOfficeTbAcctNo = properties.getProperty(headOfficeTbAcctNo);
            headOfficeTbSol = properties.getProperty(headOfficeTbSol);
            sleepTime = properties.getProperty(sleepTimeLocal);
            logger.info("Done loading properties file");
        }
        catch (IOException ex){
            ex.printStackTrace();
            logger.error("Error occurred in load property file - IOException Exception-- "+ ex.getMessage());
        }
    }
}
