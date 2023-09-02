package com.hevodata.storage.util;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExceptionPrint {
    private static Logger logger = LoggerFactory.getLogger(ExceptionPrint.class);
    public static void perform(Exception ex){
        logger.error(getStackTrace(ex));
    }

    public static void perform(Throwable ex){
        logger.error(getStackTrace(ex));
    }

    public static String getStackTrace(Exception ex) {
        StringBuffer sb = new StringBuffer(500);
        StackTraceElement[] st = ex.getStackTrace();
        sb.append(ex.getClass().getName() + ": " + ex.getMessage() + "\n");
        for (int i = 0; i < st.length; i++) {
            sb.append("\t at " + st[i].toString() + "\n");
        }
        return sb.toString();
    }

    public static String getStackTrace(Throwable ex) {
        StringBuffer sb = new StringBuffer(500);
        StackTraceElement[] st = ex.getStackTrace();
        sb.append(ex.getClass().getName() + ": " + ex.getMessage() + "\n");
        for (int i = 0; i < st.length; i++) {
            sb.append("\t at " + st[i].toString() + "\n");
        }
        return sb.toString();
    }
}