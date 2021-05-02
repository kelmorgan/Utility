package com.fbn.utils;

import org.apache.log4j.*;

public class LogGen implements ConstantsI {
    public static Logger getLoggerInstance(String loggerName) {
        Logger logger = null;
        try {
            logger = Logger.getLogger(loggerName);
            PatternLayout layout = new PatternLayout();
            layout.setConversionPattern("[%d{dd MMM yyyy HH:mm:ss:SSS}] (%F:%L) - %m%n");
            String logFile = ""+logPath+"/" + loggerName + ".log";
            RollingFileAppender appender = new RollingFileAppender((Layout)layout, logFile, true);
            appender.setMaxFileSize("1000KB");
            appender.setMaxBackupIndex(10);
            appender.setThreshold((Priority)Level.DEBUG);
            logger.removeAllAppenders();
            logger.addAppender((Appender)appender);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return logger;
    }

}
