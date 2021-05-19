package com.fbn.start;

import com.fbn.api.newgen.controller.Controller;
import com.fbn.cp.CpMain;
import com.fbn.tb.TbMain;
import com.fbn.utils.ConstantsI;
import com.fbn.utils.LoadProp;
import com.fbn.utils.LogGen;
import org.apache.log4j.Logger;

public class Main implements ConstantsI {
    private final Logger logger = LogGen.getLoggerInstance("UtilityLogs");


    public void run() throws Exception {
       executeUtility();
    }
    private void disconnectSession (String sessionId){
        new Controller().disconnectSession(sessionId);
    }
    private void executeUtility () throws Exception {
        try {
            while (true) {
                String sessionId = new Controller().getSessionId();
                CpMain cpMain = new CpMain(sessionId);
                TbMain tbMain = new TbMain(sessionId);
                Thread cp = new Thread(cpMain);
                Thread tb = new Thread(tbMain);
                cp.start();
                tb.start();
                cp.join();
                tb.join();
                disconnectSession(sessionId);
                logger.info("Current thread name-- "+ Thread.currentThread().getName());
                Thread.sleep(Long.parseLong(LoadProp.sleepTime));
            }
        }
        catch (Exception e){
            logger.info("Exception occurred in Main class-- "+e.getMessage());
            throw new Exception("Exception occurred in Main Class-- "+ e.getMessage());
        }
    }
}
