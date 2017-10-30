package com.apitest.core;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public interface ITestScript {

    default Logger getLogger(){
        return LogManager.getLogger(getClass());
    }
}
