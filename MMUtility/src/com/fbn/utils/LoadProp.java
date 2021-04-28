package com.fbn.utils;

import org.apache.log4j.Logger;

import java.io.*;
import java.util.Properties;

public class LoadProp implements ConstantsI {
    public static Logger logger;
    public static String serverIp;
    public static String serverPort;


    static {
        try {
            logger = LogGen.getLoggerInstance("LoadPropLogs");
            logger.info("Start loading properties file");
            Properties properties = new Properties();
            InputStream in = new FileInputStream(configPath);
            properties.load(in);
           serverIp = properties.getProperty(appServerIpLocal);
           serverPort = properties.getProperty(appServerPortLocal);
        }
        catch (IOException ex){
            ex.printStackTrace();
            logger.error("Error occurred in load property file - IOException Exception-- "+ ex.getMessage());
        }
    }
}
