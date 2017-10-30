package com.apitest.core.scriptTest;

import com.apitest.annotations.FlowTag;

public class PreFlowData {

    @FlowTag
    private int age;

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
