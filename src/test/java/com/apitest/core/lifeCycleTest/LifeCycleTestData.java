package com.apitest.core.lifeCycleTest;

import com.apitest.core.ApiBaseData;
import com.apitest.core.IDataLifeCycle;

public class LifeCycleTestData implements IDataLifeCycle {

    private String name;

    private String beforeResult;

    private String afterResult;

    private String finalResult;




    public String getBeforeResult() {
        return beforeResult;
    }

    public void setBeforeResult(String beforeResult) {
        this.beforeResult = beforeResult;
    }

    public String getAfterResult() {
        return afterResult;
    }

    public void setAfterResult(String afterResult) {
        this.afterResult = afterResult;
    }

    public String getFinalResult() {
        return finalResult;
    }

    public void setFinalResult(String finalResult) {
        this.finalResult = finalResult;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void beforeRun() {
        setBeforeResult("before");
    }

    @Override
    public void afterRun() {
        setAfterResult("after");
    }

    @Override
    public void finalRun() {
        setFinalResult("final");
    }
}
