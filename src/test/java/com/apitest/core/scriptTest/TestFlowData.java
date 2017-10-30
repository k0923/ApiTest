package com.apitest.core.scriptTest;

import com.apitest.annotations.FlowTag;
import com.apitest.core.ApiBaseData;

public class TestFlowData extends ApiBaseData {

    @FlowTag
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
