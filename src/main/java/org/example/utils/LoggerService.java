package org.example.utils;

import java.util.logging.Logger;

public class LoggerService {
    static Logger log = Logger.getLogger(LoggerService.class.getName());

    public static void displayLog(String msg) {
        log.info(msg);
    }
}
