package com.apitest.core.scriptTest;

import org.testng.Assert;
import org.testng.annotations.Test;

public class PreFlowTest {

    @Test
    public void preFlow1(TestFlowData data){
        Assert.assertEquals(data.getName(),"preFlowName");
    }

    @Test
    public void preFlow2(PreFlowData data){
        System.out.println(data.getAge());

    }

}
