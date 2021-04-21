package com.fbn.utils;

import org.apache.log4j.Logger;

import java.io.*;
import java.util.Properties;

public class LoadProp implements ConstantsI {
    public static Logger logger;


    static {
        try {
            logger = LogGen.getLoggerInstance("LoadPropLogs");
            logger.info("Start loading properties file");
            Properties properties = new Properties();
            InputStream in = new FileInputStream(configPath);
            properties.load(in);
        }
        catch  (UnsupportedEncodingException ex){
            ex.printStackTrace();
            logger.error("Error occurred in load property file - Unsupported Exception -- "+ ex.getMessage() );
        }
        catch (FileNotFoundException ex){
            ex.printStackTrace();
            logger.error("Error occurred in load property file - FileNotFoundException Exception-- "+ ex.getMessage());
        }
        catch (IOException ex){
            ex.printStackTrace();
            logger.error("Error occurred in load property file - IOException Exception-- "+ ex.getMessage());
        }
    }

}
